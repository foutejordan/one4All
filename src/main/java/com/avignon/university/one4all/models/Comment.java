package com.avignon.university.one4all.models;


import java.sql.Date;

public class Comment {

    public int id;
    public int idSejour;

    public int getIdUser() {
        return idUser;
    }

    public int idUser;
    public String message;
    public Date date;

    public Comment() {
    }

    public int getIdSejour() {
        return idSejour;
    }

    public String getMessage() {
        return message;
    }

    public Comment(int idSejour, int idUser, String message, Date date) {
        this.idSejour = idSejour;
        this.idUser = idUser;
        this.message = message;
        this.date = date;
    }

    public Date getDate() {
        return date;
    }


}
