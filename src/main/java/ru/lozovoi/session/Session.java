package ru.lozovoi.session;

import akka.http.javadsl.model.headers.BasicHttpCredentials;

import java.util.Objects;

public class Session {


    private String name;
    private String password;

    public Session(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getToken() {
        return BasicHttpCredentials.createBasicHttpCredentials(name, password).token();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Session session = (Session) o;
        return Objects.equals(name, session.name) && Objects.equals(password, session.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getToken());
    }
}
