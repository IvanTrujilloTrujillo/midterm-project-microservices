package com.ironhack.manageAccountservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Set;


public class User {
    //Properties
    private Long id;
    private String name;
    private String username;
    private String password;

    private Set<Role> roles;

    //Constructors
    public User() {
    }

    public User(String name, String username, String password) {
        setName(name);
        setUsername(username);
        setPassword(password);
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
