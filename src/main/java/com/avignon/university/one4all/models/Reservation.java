package com.avignon.university.one4all.models;

public class Reservation {

    int idSejour;

    public Reservation(int idSejour, int idCurentUser, int idHoteUser, String message, String decision) {
        this.idSejour = idSejour;
        this.idCurentUser = idCurentUser;
        this.idHoteUser = idHoteUser;
        this.message = message;
        this.decision = decision;
    }

    public int getIdSejour() {
        return idSejour;
    }

    public int getIdCurentUser() {
        return idCurentUser;
    }

    public int getIdHoteUser() {
        return idHoteUser;
    }

    public String getMessage() {
        return message;
    }

    public String getDecision() {
        return decision;
    }

    int idCurentUser;
    int idHoteUser;
    String message;
    String decision;
}
