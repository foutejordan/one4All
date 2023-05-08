package com.avignon.university.one4all.models;

import com.avignon.university.one4all.models.dao.CommentModel;

import java.sql.Date;
import java.util.ArrayList;

public class Historique {
    public int id;
    public int idSejour;
    public Date dateDebut;
    public Date dateFin;
    public String lieu;
    public String titre;
    public int nombrePersonnes;
    public double prix;
    public String image;
    public int statut;
    public int idHote;
    public User hote;
    public int idUser;
    public User user;


    public Sejour toSejour(){
        Sejour sejour=new Sejour();
        sejour.id = idSejour;
        sejour.dateDebut = dateDebut;
        sejour.dateFin = dateFin;
        sejour.lieu = lieu;
        sejour.titre = titre;
        sejour.nombrePersonnes = nombrePersonnes;
        sejour.prix = prix;
        sejour.image = image;
        sejour.statut = statut;
        sejour.idHote = idHote;
        sejour.hote = hote;
        QueryResponse queryGetComments = CommentModel.getAllComments(idSejour);
        if(queryGetComments.state == ResponseState.OK){
            if(queryGetComments.response!=null){
                ArrayList<Comment> comments = new ArrayList<>();
                for (Object obj:queryGetComments.response) {
                    Comment comment = (Comment) obj;
                    comments.add(comment);
                }
                sejour.comments = comments;
            }
        }
        return sejour;
    }

    public Panier toPanier(){
        Panier panier = new Panier();
        panier.idSejour = idSejour;
        panier.idUser = idUser;
        panier.sejour = toSejour();
        panier.user = user;
        return  panier;
    }
}
