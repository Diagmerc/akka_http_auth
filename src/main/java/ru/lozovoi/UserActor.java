package ru.lozovoi;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.japi.pf.FI;
import ru.lozovoi.entity.User;
import ru.lozovoi.service.SessionService;
import ru.lozovoi.service.UserMessages;
import ru.lozovoi.service.UserMessages.CreateUserMessage;
import ru.lozovoi.service.UserMessages.GetUserMessage;
import ru.lozovoi.service.UserService;

class UserActor extends AbstractActor {

    private UserService userService = new UserService();

    static Props props() {
        return Props.create(UserActor.class);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(UserMessages.LoginUserMessage.class, handleLoginUser())
                .match(UserMessages.CreateUserMessage.class, handleCreateUser())
                .match(UserMessages.GetUserMessage.class, handleGetUser())
                .build();
    }

    private FI.UnitApply<CreateUserMessage> handleCreateUser() {
        return createUserMessage -> {
            if (userService.getUserByEmail(createUserMessage.getUser().getEmail()).isPresent()) {
                sender()
                        .tell(new String(
                                "session.errors.emailAlreadyRegistered"), getSelf());
            } else {
                userService.createUser(createUserMessage.getUser());
                sender()
                        .tell(new String(
                                ""), getSelf());
            }
        };
    }

    private FI.UnitApply<GetUserMessage> handleGetUser() {
        return getUserMessage -> {
            sender().tell(userService.getUser(getUserMessage.getUserId()), getSelf());
        };
    }

    private FI.UnitApply<UserMessages.LoginUserMessage> handleLoginUser() {
        return loginUserMessage -> {
            User user = loginUserMessage.getUser();
            if (userService.getUserByUserName(user.getName()).isPresent()) {
                if (userService.getUserByUserName(user.getName()).get().getPassword().equals(user.getPassword())) {
                    SessionService sessionService = new SessionService();
                    sessionService.createSession(user.getName(), user.getPassword());
                    sender().tell(userService.getUserByUserName(loginUserMessage.getUser().getName()), getSelf());
                }
            } else {
                sender().tell("bad credentials", getSelf());
            }
        };
    }
}
