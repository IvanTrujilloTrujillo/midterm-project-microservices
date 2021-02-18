package com.ironhack.manageAccountservice.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class Role {
    //Properties
    private Long id;
    private String name;
    private User user;

    //Constructors
    public Role() {
    }

    public Role(String name, User user) {
        setName(name);
        setUser(user);
    }

    //Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
