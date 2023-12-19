package ru.lozovoi.entity;

import akka.http.javadsl.model.headers.BasicHttpCredentials;

import java.util.Objects;

public class Session {


    private String email;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    private String password;
    private String token;

    public Session(String name, String password) {
        this.email = name;
        this.password = password;
        this.token = BasicHttpCredentials.createBasicHttpCredentials(name, password).token();
    }

    public String getToken() {
        return token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Session session = (Session) o;
        return Objects.equals(email, session.email) && Objects.equals(password, session.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getToken());
    }
}
