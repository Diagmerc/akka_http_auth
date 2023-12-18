package ru.lozovoi.entity;

public class User {

    private final Long id;

    private final String name;

    private final String email;

    private final String password;

    public User() {
        this.name = "";
        this.id = null;
        this.email = "";
        this.password = "";
    }

    public User(String name, String password) {
        this.name = name;
        this.id = null;
        this.email = "";
        this.password = password;
    }

    public User(Long id, String name, String email, String password) {
        this.name = name;
        this.id = id;
        this.email = email;
        this.password = password;
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
}
