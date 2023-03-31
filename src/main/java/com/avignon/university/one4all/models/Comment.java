package com.avignon.university.one4all.models;

import java.util.Date;

public class Comment {

    int idSejour;

    public int getIdUser() {
        return idUser;
    }

    int idUser;
    String message;
    Date date;

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
