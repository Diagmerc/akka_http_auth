package ru.lozovoi.entity;

import java.time.LocalDateTime;

public class User extends BaseEntity{

    private final String name;

    private final String email;

    private final String password;

    private final LocalDateTime registered;

    public User() {
        this.name = "";
        this.email = "";
        this.password = "";
        this.registered = LocalDateTime.now();
    }

    public User(String name, String password) {
        this.name = name;
        this.email = "";
        this.password = password;
        this.registered = LocalDateTime.now();
    }

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.registered = LocalDateTime.now();
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public LocalDateTime getRegistered() {
        return registered;
    }
}
