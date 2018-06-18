package com.github.uuidcode.querydsl.test.entity;

import java.util.List;

public class Payload extends CoreObject<Payload> {
    private User user;
    private List<User> userList;

    public static Payload of() {
        return new Payload();
    }

    public List<User> getUserList() {
        return this.userList;
    }

    public Payload setUserList(List<User> userList) {
        this.userList = userList;
        return this;
    }
    public User getUser() {
        return this.user;
    }

    public Payload setUser(User user) {
        this.user = user;
        return this;
    }
}
