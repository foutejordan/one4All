package com.avignon.university.one4all.models.dao;

import com.avignon.university.one4all.models.*;
import com.github.javafaker.Faker;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class panierModel {

    public int addStayToBasket(int idSejour, int idUser) {
        int id = (int) CRUDHelper.create(
                "Basket",
                new String[]{"idSejour", "idUser"},
                new Object[]{idSejour, idUser},
                new int[]{Types.INTEGER, Types.INTEGER});

        return id;
    }

    public static QueryResponse createPanier(Panier panier){
        String query = "INSERT INTO Panier (idSejour, idUser, statut) VALUES (?, ?, ?)";
        QueryResponse result = new QueryResponse();
        try (Connection connection = Database.connect("one4All.sqlite")) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, panier.idSejour);
            statement.setInt(2, panier.idUser);
            statement.setInt(3, panier.statut);

            int idPanier = statement.executeUpdate();
            ArrayList<Object> ids = new ArrayList<>();
            if(idPanier > 0){
                result.state = ResponseState.SUCCESS;
                result.message = "Produit ajouté dans le Panier avec succès";
                ids.add(idPanier);
            }else{
                result.state = ResponseState.NOT_INSERTED;
                result.message = "Produit non ajouté dans le panier";
            }
            result.response = ids;
        }catch (SQLException e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ":" +e.getMessage());
        }
        return result;
    }

    public static QueryResponse deletePanier(int id){
        String query = "DELETE FROM Panier WHERE id = ?";
        QueryResponse result = new QueryResponse();
        try (Connection connection = Database.connect("one4All.sqlite")) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);

            int idPanier = statement.executeUpdate();
            ArrayList<Object> ids = new ArrayList<>();
            if(idPanier > 0){
                result.state = ResponseState.SUCCESS;
                result.message = "Produit enlevé du Panier avec succès";
                ids.add(idPanier);
            }else{
                result.state = ResponseState.NOT_INSERTED;
                result.message = "Produit non enlevé dans le panier";
            }
            result.response = ids;
        }catch (SQLException e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ":" +e.getMessage());
        }
        return result;
    }


    public QueryResponse getAllPanierByUserId(int userID) {
        String query = "SELECT * FROM  Panier WHERE idUser = ? AND statut = 1";
        ArrayList<Object> sejours = new ArrayList<>();
        QueryResponse result = new QueryResponse();
        try (Connection connection = Database.connect("one4All.sqlite")) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, userID);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                int idSejour = rs.getInt("idSejour");
                QueryResponse qr =  SejourModel.getSejourById(idSejour);
                if(qr.state == ResponseState.SUCCESS){
                    for (Object x: qr.response) {
                        Sejour sejour =(Sejour) x;
                        sejours.add(sejour);
                    }
                }
            }
            result.response = sejours;
            result.message = "Les séjours du paniers obtenus avec succès";
            result.state = ResponseState.SUCCESS;
        } catch (SQLException e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load data from database ");
        }
        return result;
    }

    public static int removeSejour(int id) {
        int result = CRUDHelper.delete("Panier", id);
        return result;
    }


    public static void initPanierTable(int nbPanier){
        Faker faker = new Faker();
        for(int i = 0; i < nbPanier; i++){
            Panier panier = new Panier();
            panier.statut = 1;
            panier.idUser = faker.random().nextInt(1, 10);
            panier.idSejour = faker.random().nextInt(1, 50);
            createPanier(panier);
        }
        System.out.println(nbPanier + " Séjours ajouté dans le panier avec succès");
    }


}
