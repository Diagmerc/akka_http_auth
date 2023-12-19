package ru.lozovoi.service;

import ru.lozovoi.entity.User;

import java.io.Serializable;

public interface UserMessages {

    class CreateUserMessage implements Serializable {

        private static final long serialVersionUID = 1L;
        private final User user;
        public CreateUserMessage(User user) {
            this.user = user;
        }

        public User getUser() {
            return user;
        }
    }

    class GetUserMessage implements Serializable {
        private static final long serialVersionUID = 1L;
        private final Long userId;

        public GetUserMessage(Long userId) {
            this.userId = userId;
        }

        public Long getUserId() {
            return userId;
        }
    }
    class LoginUserMessage implements Serializable {
        private static final long serialVersionUID = 1L;
        private final User user;
        public LoginUserMessage(User user) {
            this.user = user;
        }

        public User getUser() {
            return user;
        }
    }
}
