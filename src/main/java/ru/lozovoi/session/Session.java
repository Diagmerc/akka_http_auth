package ru.lozovoi.session;

public class Session {
    public String getCredentials() {
        return credentials;
    }

    private final String credentials;

    public Session(String credentials) {
        this.credentials = credentials;
    }
}
