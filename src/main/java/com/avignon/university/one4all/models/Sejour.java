package com.avignon.university.one4all.models;

import java.util.Date;

public class Sejour {
    int idHote;
    Date date_debut;
    Date date_fin;
    int prix;
    String lieux;
    String titre;

    public int getIdHote() {
        return idHote;
    }

    public Date getDate_debut() {
        return date_debut;
    }

    public Date getDate_fin() {
        return date_fin;
    }

    public int getPrix() {
        return prix;
    }

    public String getLieux() {
        return lieux;
    }

    public String getTitre() {
        return titre;
    }

    public int getNbrePersonnes() {
        return nbrePersonnes;
    }

    public String getStatut() {
        return statut;
    }

    public Sejour(int idHote, Date date_debut, Date date_fin, int prix, String lieux, String titre, int nbrePersonnes, String statut) {
        this.idHote = idHote;
        this.date_debut = date_debut;
        this.date_fin = date_fin;
        this.prix = prix;
        this.lieux = lieux;
        this.titre = titre;
        this.nbrePersonnes = nbrePersonnes;
        this.statut = statut;
    }

    int nbrePersonnes;
    String statut;


}
