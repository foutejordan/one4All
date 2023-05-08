package com.avignon.university.one4all.models;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class Sejour {
    public int id;
    public Date dateDebut;
    public Date dateFin;
    public String lieu;
    public String titre;
    public int idHote;
    public User hote;
    public int nombrePersonnes;

    public List<Comment> comments = new ArrayList<>();

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public int getStatut() {
        return statut;
    }

    public void setStatut(int statut) {
        this.statut = statut;
    }

    public double prix;

    public String image;


    public int statut;

    public Sejour() {
    }

    public Sejour(int idHote, Date dateDebut, Date dateFin, double prix, String lieu, String titre, int nombrePersonnes, int statut, String image){
        this.idHote = idHote;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.prix = prix;
        this.lieu = lieu;
        this.titre = titre;
        this.nombrePersonnes = nombrePersonnes;
        this.statut = statut;
        this.image = image;
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

    public int getIdHote() {
        return idHote;
    }

    public void setIdHote(int idHote) {
        this.idHote = idHote;
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
                ", idHote=" + idHote +
                ", hote=" + hote +
                ", nombrePersonnes=" + nombrePersonnes +
                ", comments=" + comments +
                ", prix=" + prix +
                ", image='" + image + '\'' +
                ", statut=" + statut +
                '}';
    }

    public User getHote() {
        return hote;
    }

    public void setHote(User hote) {
        this.hote = hote;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }


    public boolean isReserve(){
        return statut == SejourStatut.RESERVE.getValue();
    }

    public boolean isEnCoursValidation(){
        return statut == SejourStatut.EN_COURS_VALIDATION.getValue();
    }

}
