package com.avignon.university.one4all.models;

public class User {

    public String login;
    public String password;
    public int role;

    public User(String login, String password, int role) {
        this.login = login;
        this.password = password;
        this.role = role;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public int getRole() {
        return role;
    }
}
