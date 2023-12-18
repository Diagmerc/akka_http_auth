package ru.lozovoi.service;

import akka.http.javadsl.server.directives.SecurityDirectives;
import ru.lozovoi.entity.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class UserService {

    private static final List<User> users = new ArrayList<>();

    private final static List<User> synchronizedUsers = Collections.synchronizedList(users);

    static {
        synchronizedUsers.add(new User(1l, "Alice", "email1", "pass"));
        synchronizedUsers.add(new User(2l, "Bob", "email2", "pass"));
    }

    public Optional<User> getUser(Long id) {
        return synchronizedUsers.stream()
                .filter(user -> user.getId()
                        .equals(id))
                .findFirst();
    }

    public Optional<User> getUserByEmail(String email) {
        return synchronizedUsers.stream()
                .filter(user -> user.getEmail()
                        .equals(email))
                .findFirst();
    }

    public Optional<User> getUserByUserName(String name) {
        return synchronizedUsers.stream()
                .filter(user -> user.getEmail()
                        .equals(name))
                .findFirst();
    }

    public void createUser(User user) {
        synchronizedUsers.add(user);
    }

    public Function<Optional<SecurityDirectives.ProvidedCredentials>, Optional<User>> getPassAuthenticator() {
        SessionService sessionService = new SessionService();
        return
                opt -> {
                    if (opt.isPresent()) {
                        User user =
                                new User(opt.get().identifier(), opt.get()
                                        .verify(getUserByUserName
                                                (opt.get().identifier()).get().getPassword()) ?
                                        getUserByUserName(opt.get().identifier()).get().getPassword() : null);
                        if (user.getPassword() != null) {
                            String session = sessionService.createSession(user.getName(), user.getPassword());
                            System.out.println(session);
                        }
                        return Optional.of(user);
                    } else {
                        return Optional.empty();
                    }
                };
    }

    public Function<User, Boolean> hasUser() {
        return user -> getUserByUserName(user.getName())
                .get().getPassword().equals(user.getPassword());
    }
}
