package ru.lozovoi.session;

import akka.http.javadsl.model.headers.BasicHttpCredentials;
import akka.http.javadsl.model.headers.HttpCredentials;
import ru.lozovoi.UserMessages.*;

import java.util.ArrayList;
import java.util.List;

public class SessionService {

    private final static List<Session> sessions = new ArrayList<>();

    public void createSession(String credentials) {
        sessions.add(new Session(credentials));
    }

    public void deleteSession(String credentials) {
        sessions.stream().filter(s -> credentials.equals(s.getCredentials()))
                .findFirst().ifPresent(sessions::remove);
    }

    public HttpCredentials getHttpCredentials(UserMessages.AuthUserMessage authUserMessage) {
        return BasicHttpCredentials.create(authUserMessage.getUser().getName(), authUserMessage.getUser().getPassword());
    }
}
