package ru.lozovoi.service;

import ru.lozovoi.entity.Session;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class SessionService {

    private final static List<Session> sessions = new ArrayList<>();

    private final static List<Session> synchronizedSessions = Collections.synchronizedList(sessions);

    public String createSession(String name, String password) {
        Session session = new Session(name, password);
        if(!haveToken(session.getToken())){
            synchronizedSessions.add(session);
            return "Ok";
        }
        System.out.println(countSessions());
        return "already exists";
    }

    public void deleteToken(String token) {
        Session session = synchronizedSessions.stream().filter(sess -> sess.getToken().equals(token)).findFirst().get();
        synchronizedSessions.remove(session);
    }

    public int countSessions() {
        return synchronizedSessions.size();
    }

    public boolean haveToken(String token) {
        return synchronizedSessions.stream()
                .anyMatch(session -> session.getToken()
                        .equals(token));
    }

    public Optional<Session> getSession(String token){
        return sessions.stream().filter(session -> session.getToken().equals(token)).findFirst();
    }
}
