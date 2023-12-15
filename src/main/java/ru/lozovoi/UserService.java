package ru.lozovoi;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserService {

    private final static List<User> users = new ArrayList<>();

    static {
        users.add(new User(1l, "Alice", "email1", "pass"));
        users.add(new User(2l, "Bob", "email2", "pass"));
    }

    public Optional<User> getUser(Long id) {
        return users.stream()
                .filter(user -> user.getId()
                        .equals(id))
                .findFirst();
    }

    public Optional<User> getUserByEmail(String email) {
        return users.stream()
                .filter(user -> user.getEmail()
                        .equals(email))
                .findFirst();
    }

    public Optional<User> getUserByUserName(String name) {
        return users.stream()
                .filter(user -> user.getEmail()
                        .equals(name))
                .findFirst();
    }

    public void createUser(User user) {
        users.add(user);
    }

    public List<User> getUsers(){
        return users;
    }

}
