package com.avignon.university.one4all.models;

public class Panier {

    public int id;
    public int idSejour;
    public Sejour sejour;
    public int idUser;
    public User user;

    public Panier(int idSejour, int idUser) {
        this.idSejour = idSejour;
        this.idUser = idUser;
    }

    public Panier() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdSejour() {
        return idSejour;
    }

    public void setIdSejour(int idSejour) {
        this.idSejour = idSejour;
    }

    public Sejour getSejour() {
        return sejour;
    }

    public void setSejour(Sejour sejour) {
        this.sejour = sejour;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Historique toHistorique(){
        Historique historique = new Historique();
        historique.idUser = idUser;
        historique.user = user;
        historique.idSejour = idSejour;
        if(sejour!=null){
            historique.idHote = sejour.idHote;
            historique.hote = sejour.hote;
            historique.dateDebut = sejour.dateDebut;
            historique.dateFin = sejour.dateFin;
            historique.image = sejour.image;
            historique.lieu = sejour.lieu;
            historique.nombrePersonnes = sejour.nombrePersonnes;
            historique.prix = sejour.prix;
            historique.titre = sejour.titre;
            historique.statut = sejour.statut;
        }


        return historique;
    }

    @Override
    public String toString() {
        return "Panier{" +
                "id=" + id +
                ", idSejour=" + idSejour +
                ", sejour=" + sejour +
                ", idUser=" + idUser +
                ", user=" + user +
                '}';
    }
}
