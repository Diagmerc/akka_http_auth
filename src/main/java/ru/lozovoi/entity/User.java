package ru.lozovoi.entity;

import java.time.LocalDateTime;

public class User {

    private final Long id;

    private final String name;

    private final String email;

    private final String password;

    private final LocalDateTime registered;

    public User() {
        this.name = "";
        this.id = null;
        this.email = "";
        this.password = "";
        this.registered = LocalDateTime.now();
    }

    public User(String name, String password) {
        this.name = name;
        this.id = null;
        this.email = "";
        this.password = password;
        this.registered = LocalDateTime.now();
    }

    public User(Long id, String name, String email, String password) {
        this.name = name;
        this.id = id;
        this.email = email;
        this.password = password;
        this.registered = LocalDateTime.now();
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
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
