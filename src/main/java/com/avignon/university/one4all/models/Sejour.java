package com.avignon.university.one4all.models;

import java.sql.Date;

public class Sejour {
    private int id;
    private Date dateDebut;
    private Date dateFin;
    private String lieu;
    private String titre;
    private int hote;
    private int nombrePersonnes;

    private double prix;

    private String image;


    private String statut;

    public Sejour() {
    }

    public Sejour(int hote, Date dateDebut, Date dateFin, double prix, String lieu, String titre, int nombrePersonnes, String statut){
        this.hote = hote;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.prix = prix;
        this.lieu = lieu;
        this.titre = titre;
        this.nombrePersonnes = nombrePersonnes;
        this.statut = statut;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public int getHote() {
        return hote;
    }

    public void setHote(int hote) {
        this.hote = hote;
    }

    public int getNombrePersonnes() {
        return nombrePersonnes;
    }

    public void setNombrePersonnes(int nombrePersonnes) {
        this.nombrePersonnes = nombrePersonnes;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Sejour{" +
                "id=" + id +
                ", dateDebut=" + dateDebut +
                ", dateFin=" + dateFin +
                ", lieu='" + lieu + '\'' +
                ", titre='" + titre + '\'' +
                ", hote=" + hote +
                ", nombrePersonnes=" + nombrePersonnes +
                ", image='" + image + '\'' +
                '}';
    }
}
