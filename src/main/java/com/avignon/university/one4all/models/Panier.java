package com.avignon.university.one4all.models;

public class Panier {

    public int id;
    public int idSejour;
    public int idUser;

    public int statut;
    public Panier(int idSejour, int idUser) {
        this.idSejour = idSejour;
        this.idUser = idUser;
    }

    public Panier() {
    }
}
