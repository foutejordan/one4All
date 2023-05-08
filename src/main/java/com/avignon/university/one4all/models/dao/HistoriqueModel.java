package com.avignon.university.one4all.models.dao;

import com.avignon.university.one4all.models.*;
import com.github.javafaker.Faker;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HistoriqueModel {

    public static QueryResponse createHistorique(Historique historique){
        String query = "INSERT INTO Historiques (idHote, date_debut, date_fin, prix, lieux, titre, nbrePersonnes, statut, image, idSejour, idUser) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        QueryResponse result = new QueryResponse();
        result.state = ResponseState.NOT_OK;
        result.message = "Historique non inséré";
        try (Connection connection = Database.connect("one4All.sqlite")) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, historique.idHote);
            statement.setString(2, historique.dateDebut.toString());
            statement.setString(3, historique.dateFin.toString());
            statement.setDouble(4, historique.prix);
            statement.setString(5, historique.lieu);
            statement.setString(6, historique.titre);
            statement.setInt(7, historique.nombrePersonnes);
            statement.setInt(8, historique.statut);
            statement.setString(9, historique.image);
            statement.setInt(10, historique.idSejour);
            statement.setInt(11, historique.idUser);
            int nbRows = statement.executeUpdate();
            ArrayList<Object> data = new ArrayList<>();
            if(nbRows > 0){
                result.state = ResponseState.OK;
                result.message = "Historique inséré avec succès";
                data.add(historique);
            }
            result.response = data;
        }catch (SQLException e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ":" +e.getMessage());
        }
        return result;
    }

    public static QueryResponse updateHistorique(Historique historique){
        String query = "UPDATE Historiques SET idHote = ?, date_debut = ?, date_fin = ?, prix = ?, lieux = ?, titre = ?, nbrePersonnes = ?, statut = ?, image = ?, idUser = ?, idSejour = ? WHERE id =  ?";
        QueryResponse result = new QueryResponse();
        result.state = ResponseState.NOT_OK;
        result.message = "Sejour non modifié";
        try (Connection connection = Database.connect("one4All.sqlite")) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, historique.idHote);
            statement.setString(2, historique.dateDebut.toString());
            statement.setString(3, historique.dateFin.toString());
            statement.setDouble(4, historique.prix);
            statement.setString(5, historique.lieu);
            statement.setString(6, historique.titre);
            statement.setInt(7, historique.nombrePersonnes);
            statement.setInt(8, historique.statut);
            statement.setString(9, historique.image);
            statement.setInt(10, historique.idUser);
            statement.setInt(11, historique.idSejour);
            statement.setInt(12, historique.id);


            int nbRows = statement.executeUpdate();
            ArrayList<Object> data = new ArrayList<>();
            if(nbRows > 0){
                Sejour sejour = historique.toSejour();
                if(historique.statut == SejourStatut.REFUSE.getValue()){
                    sejour.statut = SejourStatut.LIBRE.getValue();
                }
                SejourModel.updateSejour(sejour);
                result.state = ResponseState.OK;
                result.message = "Historique modifié avec succès";
                data.add(historique);
            }
            result.response = data;
        }catch (SQLException e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ":" +e.getMessage());
        }
        return result;
    }

    public static QueryResponse getAllHistorquesByUserId(int userId){
        QueryResponse result = new QueryResponse();
        result.state = ResponseState.NOT_OK;
        result.message = "Historiques non récupérées";
        String query = "SELECT * FROM  Historiques WHERE idUser = ?";
        try (Connection connection = Database.connect("one4All.sqlite")) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, userId);
            ResultSet rs = statement.executeQuery();
            ArrayList<Object> data = new ArrayList<>();
            while (rs.next()) {
                Historique historique = getValues(rs);
                data.add(historique);
            }
            result.response = data;
            result.message = "Les séjours du paniers obtenus avec succès";
            result.state = ResponseState.OK;
        } catch (SQLException e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load data from database ");
        }
        return result;
    }


    public static QueryResponse getAllHistorquesByHoteId(int hoteId){
        QueryResponse result = new QueryResponse();
        result.state = ResponseState.NOT_OK;
        result.message = "Historiques non récupérées";
        String query = "SELECT * FROM  Historiques WHERE idHote = ?";
        try (Connection connection = Database.connect("one4All.sqlite")) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, hoteId);
            ResultSet rs = statement.executeQuery();
            ArrayList<Object> data = new ArrayList<>();
            while (rs.next()) {
                Historique historique = getValues(rs);
                data.add(historique);
            }
            result.response = data;
            result.message = "Les séjours du paniers obtenus avec succès";
            result.state = ResponseState.OK;
        } catch (SQLException e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load data from database ");
        }
        return result;
    }
    public static List<Historique> defaultHistoriques = new ArrayList<>();

    public static void initHistoriquesTable(int nbHistorique){
        ArrayList<Panier> paniersToValidate = new ArrayList<>();
        Panier panier = PanierModel.getFakerPanier();
        paniersToValidate.add(panier);
        for(int i = 1; i < nbHistorique; i++){
            while (true){
                panier = PanierModel.getFakerPanier();
                boolean isAlreadyAdded = false;
                for (Panier p:paniersToValidate ) {
                    if(panier.id == p.id){
                        isAlreadyAdded = true;
                        break;
                    }
                }
                if(isAlreadyAdded){
                    continue;
                }
                paniersToValidate.add(panier);
                defaultHistoriques.add(panier.toHistorique());
                break;
            }
        }
        PanierModel.validerPanier(paniersToValidate);
        System.out.println(nbHistorique + " Historiques ajouté dans le panier avec succès");
    }

    public static Historique getFakerHistorique(){
        Faker faker = new Faker();
        int index = faker.random().nextInt(0, defaultHistoriques.size() - 1);
        return defaultHistoriques.get(index);
    }

    public static Historique getValues(ResultSet rs) throws SQLException {
        Historique historique = new Historique();
        historique.id = rs.getInt("id");
        historique.idHote = rs.getInt("idHote");
        QueryResponse hoteQuery = UserModel.getUserById(historique.idHote);
        if(hoteQuery.state == ResponseState.OK){
            if(hoteQuery.response!=null && !hoteQuery.response.isEmpty()){
                historique.hote = (User) hoteQuery.response.get(0);
            }
        }
        historique.idSejour = rs.getInt("idSejour");
        historique.idUser = rs.getInt("idUser");
        QueryResponse userQuery = UserModel.getUserById(historique.idUser);
        if(userQuery.state == ResponseState.OK){
            if(userQuery.response!=null && !userQuery.response.isEmpty()){
                historique.user = (User) userQuery.response.get(0);
            }
        }
        historique.dateDebut = Date.valueOf(rs.getString("date_debut"));
        historique.dateFin = Date.valueOf(rs.getString("date_fin"));
        historique.prix = rs.getDouble("prix");
        historique.lieu = rs.getString("lieux");
        historique.titre = rs.getString("titre");
        historique.nombrePersonnes = rs.getInt("nbrePersonnes");
        historique.image = rs.getString("image");
        historique.statut = rs.getInt("statut");

        return historique;
    }

    public static QueryResponse getHistoriqueByMultiCriteria(Historique historique){
        // String query = "SELECT * FROM Sejour WHERE UPPER(titre) LIKE ? OR UPPER(lieux) LIKE ? OR nbrePersonnes = ? OR prix = ? OR date_debut = ? OR date_fin = ? COLLATE NOCASE";
        String query = "SELECT * FROM Historiques ";
        QueryResponse result = new QueryResponse();
        ArrayList<Object> historiques = new ArrayList<>();
        try (Connection connection = Database.connect("one4All.sqlite")) {
            String titrePart = "";
            String lieuPart = "";
            String nombrePersPart = "";
            String prixPart = "";
            String datePart = "";
            String statutPart = "";
            String idPart = "";

            List<String> queryPart = new ArrayList<>();

            if(historique.user!=null){
                if(historique.user.isVoyageur()){
                    idPart = "idUser = "+historique.idUser;
                } else if (historique.user.isHote()) {
                    idPart = "idHote = "+historique.idUser;
                }
                if(!idPart.isEmpty()){
                    queryPart.add(idPart);
                }
            }
            if(historique.statut!=0){
                statutPart = "statut = "+historique.statut;
                queryPart.add(statutPart);
            }

            List<String> xPart = new ArrayList<>();

            if(historique.titre!=null && !historique.titre.isEmpty()){
                titrePart = "UPPER(titre) LIKE '%"+historique.titre.toUpperCase()+"%'";
                xPart.add(titrePart);
            }

            if(historique.lieu!=null && !historique.lieu.isEmpty()){
                lieuPart = "UPPER(lieux) LIKE  '%"+historique.lieu.toUpperCase()+"%'";
                xPart.add(lieuPart);
            }

            if(historique.nombrePersonnes!=0){
                nombrePersPart = "nbrePersonnes BETWEEN "+historique.nombrePersonnes+" AND "+addToNumber(historique.nombrePersonnes);
                xPart.add(nombrePersPart);
            }

            if(historique.prix!=0){
                int prix = (int) historique.prix;
                prixPart = "prix BETWEEN "+prix+" AND "+addToNumber(prix);
                xPart.add(prixPart);
            }

            if(!xPart.isEmpty()){
                queryPart.add("( "+String.join(" OR ", xPart)+" )");
            }


            if(historique.dateDebut != null){
                datePart = "date_debut = ?";
                if(historique.dateFin != null){
                    datePart += " AND date_fin = ?";
                    datePart = "("+datePart+")";
                }
            }else{
                if(historique.dateFin != null){
                    datePart = "date_fin = ?";
                }
            }

            if(!datePart.isEmpty()){
                queryPart.add(datePart);
            }

            if(!queryPart.isEmpty()){
                query += "WHERE "+String.join(" AND ", queryPart) + " COLLATE NOCASE";
            }

            System.out.println(query);
            PreparedStatement statement = connection.prepareStatement(query);
            if(historique.dateDebut != null){
                statement.setString(1, historique.dateDebut.toString());
                if(historique.dateFin != null){
                    statement.setString(2, historique.dateFin.toString());
                }
            }else{
                if(historique.dateFin != null){
                    statement.setString(1, historique.dateFin.toString());
                }
            }
            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                Historique h = getValues(rs);
                historiques.add(h);
            }
            result.state = ResponseState.OK;
            result.message = "Liste des historiques obtenus avec succès";
            result.response = historiques;
        }catch (SQLException e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + " : "+e.getMessage());

        }
        return result;
    }

    public static QueryResponse getCountByStatut(User user){
        QueryResponse result = new QueryResponse();
        result.state = ResponseState.NOT_OK;
        result.message = "Les nombres d'historiques non récupérées";
        String query = "SELECT statut as type, COUNT(*) as count FROM historiques " +
                "WHERE idHote = ? " +
                "GROUP BY statut";
        try (Connection connection = Database.connect("one4All.sqlite")) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, user.id);
            ResultSet rs = statement.executeQuery();
            ArrayList<Object> data = new ArrayList<>();
            while (rs.next()) {
                CountType count = new CountType();
                count.type = rs.getInt("type");
                count.count = rs.getInt("count");
                data.add(count);
            }
            result.response = data;
            result.message = "Les nombres d'historiques  obtenus avec succès";
            result.state = ResponseState.OK;

        }catch (SQLException e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + " : "+e.getMessage());

        }
        return result;
    }

    public static QueryResponse getTotalAmount(User user){
        QueryResponse result = new QueryResponse();
        result.state = ResponseState.NOT_OK;
        result.message = "Le montant total non récupéré";
        String query = "SELECT SUM(prix) as total FROM Historiques " +
                "WHERE idHote = ? AND statut = ?";
        try (Connection connection = Database.connect("one4All.sqlite")) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, user.id);
            statement.setInt(2, SejourStatut.RESERVE.getValue());
            ResultSet rs = statement.executeQuery();
            ArrayList<Object> data = new ArrayList<>();
            while (rs.next()) {
                MontantType montantType = new MontantType();
                montantType.total = rs.getInt("total");
                data.add(montantType);
            }
            result.response = data;
            result.message = "Le montant total obtenus avec succès";
            result.state = ResponseState.OK;

        }catch (SQLException e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + " : "+e.getMessage());

        }
        return result;
    }

    private static int addToNumber(int number) {
        // Récupération du nombre de chiffres du nombre donné
        int numDigits = (int) Math.log10(number) + 1;

        // Calcul de la valeur à ajouter en fonction du nombre de chiffres
        int valueToAdd = (int) Math.pow(10, numDigits - 1);

        // Ajout de la valeur au nombre donné

        return number + valueToAdd;
    }
}
