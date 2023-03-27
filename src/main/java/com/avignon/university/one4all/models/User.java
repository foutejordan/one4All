package com.avignon.university.one4all.models;

public class User {
    public int id;
    public String username;
    public String password;
    public String image;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
