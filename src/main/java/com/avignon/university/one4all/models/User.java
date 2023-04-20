package com.avignon.university.one4all.models;

public class User {
    public int id;
    public String login;
    public String password;
    public int role;

    public String image;

    public User(){}
    public User(String login, String password, int role) {
        this.login = login;
        this.password = password;
        this.role = role;
    }

    public User(String login, String password, int role, String image) {
        this.login = login;
        this.password = password;
        this.role = role;
        this.image = image;
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

    public String getImage() {
        return image;
    }
}
