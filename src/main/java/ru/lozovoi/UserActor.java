package ru.lozovoi;

import akka.actor.AbstractActor;
import akka.actor.Props;

import akka.japi.pf.FI;
import ru.lozovoi.UserMessages.ActionPerformed;
import ru.lozovoi.UserMessages.CreateUserMessage;
import ru.lozovoi.UserMessages.GetUserMessage;

import java.io.IOException;

class UserActor extends AbstractActor {

    private UserService userService = new UserService();


    static Props props() {
        return Props.create(UserActor.class);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(UserMessages.CreateUserMessage.class, handleCreateUser())
                .match(UserMessages.GetUserMessage.class, handleGetUser())
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

//    private FI.UnitApply<GetUserMessage> handleAuth() {
//        return getUserMessage -> {
//            sender().tell(userService.getUser(getUserMessage.getUserId()), getSelf());
//        };
//    }
}
