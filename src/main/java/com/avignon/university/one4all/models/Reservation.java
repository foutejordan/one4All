package com.avignon.university.one4all.models;

public class Reservation {
    public Reservation(int idSejour, int statut, int idUser, int idHote) {
        this.idSejour = idSejour;
        this.statut = statut;
        this.idUser = idUser;
        this.idHote = idHote;
    }

    int idSejour;
    int statut;
    int idUser;

    public int getIdSejour() {
        return idSejour;
    }

    public int getStatut() {
        return statut;
    }

    public int getIdUser() {
        return idUser;
    }

    public int getIdHote() {
        return idHote;
    }

    int idHote;


}
