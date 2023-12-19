package ru.lozovoi.dto;

import ru.lozovoi.entity.Session;
import ru.lozovoi.entity.User;
import ru.lozovoi.service.SessionService;
import ru.lozovoi.service.UserService;

import java.time.LocalDateTime;
import java.util.Optional;

public class UserTo {
    private final String token;

    private Long id;

    private String name;

    private LocalDateTime created;

    private String email;

    public UserTo(String token) {
        this.token = token;
        SessionService sessionService = new SessionService();
        Optional<Session> session = sessionService.getSession(token);
        if (session.isPresent()) {
            UserService userService = new UserService();
            User user = userService.getUserByUserName(session.get().getEmail()).get();
            this.name = user.getName();
            this.email = user.getEmail();
            this.id = user.getId();
            this.created = user.getRegistered();
        }
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", created=" + created +
                ", email='" + email + '\'' +
                '}';
    }
}
