package com.avignon.university.one4all.models.dao;

import com.avignon.university.one4all.models.*;
import com.github.javafaker.Faker;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PanierModel {

    public static QueryResponse createPanier(Panier panier){
        String query = "INSERT INTO Paniers (idSejour, idUser) VALUES (?, ?)";
        QueryResponse result = new QueryResponse();
        try (Connection connection = Database.connect("one4All.sqlite")) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, panier.idSejour);
            statement.setInt(2, panier.idUser);

            int nbRows = statement.executeUpdate();
            ArrayList<Object> data = new ArrayList<>();
            result.state = ResponseState.NOT_OK;
            result.message = "Produit non ajouté dans le panier";
            if(nbRows > 0){
                if(panier.sejour!=null){
                    panier.sejour.statut = SejourStatut.AJOUTE_AU_PANIER.getValue();
                    QueryResponse sejourUpdateQuery = SejourModel.updateSejour(panier.sejour);
                    if(sejourUpdateQuery.state == ResponseState.OK){
                        if(sejourUpdateQuery.response!=null && !sejourUpdateQuery.response.isEmpty()){
                            result.state = ResponseState.OK;
                            result.message = "Produit ajouté dans le Panier avec succès";
                            data.add(panier);
                        }
                    }
                }
            }
            result.response = data;
        }catch (SQLException e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ":" +e.getMessage());
        }
        return result;
    }

    public static QueryResponse validerPanier(List<Panier> paniers){
        QueryResponse result = new QueryResponse();
        result.message = "Panier validé avec succès";
        result.state = ResponseState.OK;
        ArrayList<Object> data = new ArrayList<>();
        for (Panier panier:paniers) {
            QueryResponse queryDeleteAfterValidate = deletePanierAfterValidate(panier);
            if(queryDeleteAfterValidate.state == ResponseState.OK){
                data.add(panier);
                continue;
            }
            result.message = "Panier non validé avec succès";
            result.state = ResponseState.NOT_OK;
        }
        result.response = data;
        return result;
    }

    public static QueryResponse deletePanierAfterValidate(Panier panier){
        String query = "DELETE FROM Paniers WHERE idSejour = ?";
        QueryResponse result = new QueryResponse();
        try (Connection connection = Database.connect("one4All.sqlite")) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, panier.idSejour);

            int nbRows = statement.executeUpdate();
            ArrayList<Object> data = new ArrayList<>();
            result.state = ResponseState.NOT_OK;
            result.message = "Sejour non enlevé du panier";
            if(nbRows > 0){
                if(panier.sejour!=null){
                    panier.sejour.statut = SejourStatut.EN_COURS_VALIDATION.getValue();
                    QueryResponse sejourUpdateQuery = SejourModel.updateSejour(panier.sejour);
                    if(sejourUpdateQuery.state == ResponseState.OK){
                        if(sejourUpdateQuery.response!=null && !sejourUpdateQuery.response.isEmpty()){
                            Historique historique = panier.toHistorique();
                            QueryResponse createHistoriqueQuery = HistoriqueModel.createHistorique(historique);
                            if(createHistoriqueQuery.state == ResponseState.OK){
                                data.add(panier);
                            }
                        }
                    }
                }

            }
            result.state = ResponseState.OK;
            result.message = "Sejour enlevé du Panier avec succès";
            result.response = data;
        }catch (SQLException e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ":" +e.getMessage());
        }
        return result;
    }
    public static QueryResponse getAllPanierForSejour(Panier panier){
        String query = "SELECT * FROM  Paniers WHERE idSejour = ?";
        QueryResponse result = new QueryResponse();
        result.state = ResponseState.NOT_OK;
        result.message = "Paniers du séjour non trouvé avec succès";
        try (Connection connection = Database.connect("one4All.sqlite")) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, panier.idSejour);
            ResultSet rs = statement.executeQuery();
            ArrayList<Object> data = new ArrayList<>();
            while (rs.next()){
                Panier p = getValues(rs);
                data.add(p);
            }
            result.state = ResponseState.OK;
            result.response = data;
            result.message = "Paniers du séjour trouvé avec succès";
        }catch (SQLException e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ":" +e.getMessage());
        }
        return result;
    }
    public static Panier getValues(ResultSet rs) throws SQLException {
        Panier panier = new Panier();
        panier.id = rs.getInt("id");
        panier.idSejour = rs.getInt("idSejour");
        QueryResponse queryGetSejour =  SejourModel.getSejourById(panier.idSejour);
        if(queryGetSejour.state == ResponseState.OK){
            if(queryGetSejour.response!=null && !queryGetSejour.response.isEmpty()){
                panier.sejour = (Sejour) queryGetSejour.response.get(0);
            }
        }

        panier.idUser = rs.getInt("idUser");
        QueryResponse queryGetUser =  UserModel.getUserById(panier.idUser);
        if(queryGetUser.state == ResponseState.OK){
            if(queryGetUser.response!=null && !queryGetUser.response.isEmpty()){
                panier.user = (User) queryGetUser.response.get(0);
            }
        }
        return panier;
    }

    public static QueryResponse deleteSejourFromPanier(Panier panier){
        String query = "DELETE FROM Paniers WHERE idUser = ? AND idSejour = ?";
        QueryResponse result = new QueryResponse();
        try (Connection connection = Database.connect("one4All.sqlite")) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, panier.idUser);
            statement.setInt(2, panier.idSejour);

            int nbRows = statement.executeUpdate();
            ArrayList<Object> data = new ArrayList<>();
            result.state = ResponseState.NOT_OK;
            result.message = "Sejour non enlevé du panier";
            if(nbRows > 0){
                QueryResponse queryGetPaniers = getAllPanierForSejour(panier);
                if(queryGetPaniers.state == ResponseState.OK){
                    if(queryGetPaniers.response!=null && queryGetPaniers.response.isEmpty()){
                        if(panier.sejour!=null){
                            panier.sejour.statut = SejourStatut.LIBRE.getValue();
                            QueryResponse sejourUpdateQuery = SejourModel.updateSejour(panier.sejour);
                            if(sejourUpdateQuery.state == ResponseState.OK){
                                if(sejourUpdateQuery.response!=null && !sejourUpdateQuery.response.isEmpty()){
                                    data.add(panier);
                                }
                            }
                        }
                    }
                }
                result.state = ResponseState.OK;
                result.message = "Sejour enlevé du Panier avec succès";
                data.add(panier);
            }
            result.response = data;
        }catch (SQLException e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ":" +e.getMessage());
        }
        return result;
    }

    public static QueryResponse getPanierBySejourAndUser(Panier panier){
        String query = "SELECT * FROM  Paniers WHERE idSejour = ? AND idUser = ?";
        ArrayList<Object> paniers = new ArrayList<>();
        QueryResponse result = new QueryResponse();
        try (Connection connection = Database.connect("one4All.sqlite")) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, panier.idSejour);
            statement.setInt(2, panier.idUser);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Panier p = getValues(rs);
                paniers.add(p);
            }
            result.response = paniers;
            result.message = "Les séjours du paniers obtenus avec succès";
            result.state = ResponseState.OK;
        } catch (SQLException e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load data from database ");
        }
        return result;
    }
    public static QueryResponse getAllPanierByUserId(int userID) {
        String query = "SELECT * FROM  Paniers WHERE idUser = ?";
        ArrayList<Object> paniers = new ArrayList<>();
        QueryResponse result = new QueryResponse();
        try (Connection connection = Database.connect("one4All.sqlite")) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, userID);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Panier panier = getValues(rs);
                paniers.add(panier);
            }
            result.response = paniers;
            result.message = "Les séjours du paniers obtenus avec succès";
            result.state = ResponseState.OK;
        } catch (SQLException e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load data from database ");
        }
        return result;
    }
    public static List<Panier> defaultPaniers = new ArrayList<>();
    public static void initPaniersTable(int nbPanier){
        for(int i = 0; i < nbPanier; i++){
            Panier panier = new Panier();
            panier.id = i+1;

            panier.user = UserModel.getFakerVoyageur();
            panier.idUser = panier.user.id;

            panier.sejour = SejourModel.getFakerSejour();
            panier.idSejour = panier.sejour.id;

            defaultPaniers.add(panier);
            createPanier(panier);
        }
        System.out.println(nbPanier + " Séjours ajouté dans le panier avec succès");
    }

    public static Panier getFakerPanier(){
        Faker faker = new Faker();
        int index = faker.random().nextInt(0, defaultPaniers.size() - 1);
        return defaultPaniers.get(index);
    }
}
