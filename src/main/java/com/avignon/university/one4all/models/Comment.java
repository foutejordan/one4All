package com.avignon.university.one4all.models;

import java.util.Date;

public class Comment {

    int idSejour;
    String message;
    Date date;

    public int getIdSejour() {
        return idSejour;
    }

    public String getMessage() {
        return message;
    }

    public Date getDate() {
        return date;
    }

    public Comment(int idSejour, String message, Date date) {
        this.idSejour = idSejour;
        this.message = message;
        this.date = date;
    }
}
