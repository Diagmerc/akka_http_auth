package ru.lozovoi;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.http.javadsl.marshallers.jackson.Jackson;
import akka.http.javadsl.model.StatusCodes;
import akka.http.javadsl.server.HttpApp;
import akka.http.javadsl.server.Route;
import akka.pattern.PatternsCS;
import akka.util.Timeout;
import ru.lozovoi.dto.UserTo;
import ru.lozovoi.entity.User;
import ru.lozovoi.service.SessionService;
import ru.lozovoi.service.UserMessages;
import ru.lozovoi.service.UserMessages.CreateUserMessage;
import ru.lozovoi.service.UserMessages.GetUserMessage;
import scala.concurrent.duration.Duration;

import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

import static akka.http.javadsl.server.PathMatchers.longSegment;
import static akka.http.javadsl.server.PathMatchers.segment;

class UserServer extends HttpApp {

    private final ActorRef userActor;

    Timeout timeout = new Timeout(Duration.create(5, TimeUnit.SECONDS));

    UserServer(ActorRef userActor) {
        this.userActor = userActor;
    }

    @Override
    public Route routes() {
        return path("register", this::postUser)
                .orElse(auth())
                .orElse(logout())
                .orElse(login())
                .orElse(path(segment("users").slash(longSegment()), id ->
                        route(getUser(id))));
    }

    private Route getUser(Long id) {
        return get(() -> {
            CompletionStage<Optional<User>> user = PatternsCS.ask(userActor, new GetUserMessage(id), timeout)
                    .thenApply(obj -> (Optional<User>) obj);

            return onSuccess(() -> user, performed -> {
                if (performed.isPresent())
                    return complete(StatusCodes.OK, performed.get(), Jackson.marshaller());
                else
                    return complete(StatusCodes.NOT_FOUND);
            });
        });
    }


    private Route postUser() {
        return route(post(() -> entity(Jackson.unmarshaller(User.class), user -> {
            CompletionStage<Object> userCreated = PatternsCS.ask(userActor, new CreateUserMessage(user), timeout);
            return onSuccess(() -> userCreated, performed -> {
                if (performed.equals("session.errors.emailAlreadyRegistered"))
                    return complete(StatusCodes.UNPROCESSABLE_ENTITY, performed, Jackson.marshaller());
                else
                    return complete(StatusCodes.OK, performed, Jackson.marshaller());
            });
        })));
    }

    public static void main(String[] args) throws Exception {
        ActorSystem system = ActorSystem.create("userServer");
        ActorRef userActor = system.actorOf(UserActor.props(), "userActor");
        UserServer server = new UserServer(userActor);
        server.startServer("localhost", 8080, system);
    }

    private Route login() {
        return route(path("login", () -> post(() -> entity(Jackson.unmarshaller(User.class), user -> {
            CompletionStage<Object> userLogin = PatternsCS.ask(userActor, new UserMessages.LoginUserMessage(user), timeout);
            return onSuccess(() -> userLogin, performed -> {
                if (!performed.toString().equals("bad credentials"))
                    return complete(StatusCodes.OK);
                else
                    return complete(StatusCodes.UNPROCESSABLE_ENTITY, "bad credentials");
            });
        }))));
    }

    private Route auth() {
        SessionService sessionService = new SessionService();
        return route(path("me", () -> extractCredentials(optCreds -> {
            if (optCreds.isPresent()) {
                String token = optCreds.get().token();
                if (sessionService.haveToken(token)) {
                    return complete(StatusCodes.OK, new UserTo(token).toString());
                } else return complete(StatusCodes.UNAUTHORIZED);
            } else {
                return complete(StatusCodes.UNAUTHORIZED);
            }
        })));
    }

    private Route logout() {
        SessionService sessionService = new SessionService();
        return route(path("logout", () -> extractCredentials(optCreds -> {
            if (optCreds.isPresent()) {
                if (sessionService.haveToken(optCreds.get().token())) {
                    sessionService.deleteToken(optCreds.get().token());
                }
            }
            return complete(StatusCodes.OK);
        })));
    }
}