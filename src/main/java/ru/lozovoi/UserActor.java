package ru.lozovoi;

import akka.actor.AbstractActor;
import akka.actor.Props;

import akka.http.javadsl.model.headers.HttpCredentials;
import akka.japi.pf.FI;
import ru.lozovoi.UserMessages.ActionPerformed;
import ru.lozovoi.UserMessages.CreateUserMessage;
import ru.lozovoi.UserMessages.GetUserMessage;
import ru.lozovoi.session.SessionService;

import java.io.IOException;

class UserActor extends AbstractActor {

    private UserService userService = new UserService();
    private SessionService sessionService = new SessionService();


    static Props props() {
        return Props.create(UserActor.class);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(UserMessages.CreateUserMessage.class, handleCreateUser())
                .match(UserMessages.GetUserMessage.class, handleGetUser())
//                .match(UserMessages.AuthUserMessage.class, handleAuth())
                .build();
    }

    private FI.UnitApply<CreateUserMessage> handleCreateUser() {
        return createUserMessage -> {
            if(userService.getUserByEmail(createUserMessage.getUser().getEmail()).isPresent()){
                throw new IOException("session.errors.emailAlreadyRegistered");
            };
            userService.createUser(createUserMessage.getUser());
            sender()
                    .tell(new ActionPerformed(
                            String.format("User %s created.", createUserMessage.getUser().getName())), getSelf());
        };
    }

    private FI.UnitApply<GetUserMessage> handleGetUser() {
        return getUserMessage -> {
            sender().tell(userService.getUser(getUserMessage.getUserId()), getSelf());
        };
    }

//    private FI.UnitApply<UserMessages.AuthUserMessage> handleAuth() {
//        return authUserMessage -> {
//            if (userService.getUserByEmail(authUserMessage.getUser().getEmail()).get().getPassword()
//                    .equals(authUserMessage.getUser().getPassword())) {
//                HttpCredentials httpCredentials = sessionService.getHttpCredentials(authUserMessage);
//                sessionService.createSession(httpCredentials.token());
//                sender().tell("authenticate "+"count sessions =" + sessionService.countSessions(), getSelf());
//            }else sender().tell("not authenticate", getSelf());
//        };
//    }
}
