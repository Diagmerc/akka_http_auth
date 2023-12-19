package ru.lozovoi.service;

import ru.lozovoi.entity.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class UserService {

    private static final List<User> users = new ArrayList<>();

    private final static List<User> synchronizedUsers = Collections.synchronizedList(users);

    static {
        synchronizedUsers.add(new User(1l, "Alice", "email1@mail.ru", "pass"));
        synchronizedUsers.add(new User(2l, "Bob", "email2@mail.ru", "pass"));
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
}
