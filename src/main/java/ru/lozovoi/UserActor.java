package ru.lozovoi;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.japi.pf.FI;
import ru.lozovoi.entity.User;
import ru.lozovoi.service.SessionService;
import ru.lozovoi.service.UserMessages;
import ru.lozovoi.service.UserMessages.ActionPerformed;
import ru.lozovoi.service.UserMessages.CreateUserMessage;
import ru.lozovoi.service.UserMessages.GetUserMessage;
import ru.lozovoi.service.UserService;

import java.io.IOException;

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
                throw new IOException("session.errors.emailAlreadyRegistered");
            }
            ;
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

    private FI.UnitApply<UserMessages.LoginUserMessage> handleLoginUser() {
        return loginUserMessage -> {
            ;
            String loginResult = "";
            User user = loginUserMessage.getUser();
            if(userService.getUserByUserName(user.getName()).get().getPassword().equals(user.getPassword())){
                SessionService sessionService = new SessionService();
                sessionService.createSession(user.getName(), user.getPassword());
                loginResult = "200";
            }
            else{loginResult = "422";}
            sender()
                    .tell(new ActionPerformed(
                            String.format("User %s created.", loginResult)), getSelf());
        };
    }
}
