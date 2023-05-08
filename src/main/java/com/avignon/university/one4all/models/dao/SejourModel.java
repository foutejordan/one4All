package com.avignon.university.one4all.models.dao;

import com.avignon.university.one4all.models.*;
import com.github.javafaker.Faker;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SejourModel {


    public static QueryResponse createSejour(Sejour sejour){
        String query = "INSERT INTO Sejours (idHote, date_debut, date_fin, prix, lieux, titre, nbrePersonnes, statut, image) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        QueryResponse result = new QueryResponse();
        result.state = ResponseState.NOT_OK;
        result.message = "Sejour non inséré";
        try (Connection connection = Database.connect("one4All.sqlite")) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, sejour.idHote);
            statement.setString(2, sejour.dateDebut.toString());
            statement.setString(3, sejour.dateFin.toString());
            statement.setDouble(4, sejour.prix);
            statement.setString(5, sejour.lieu);
            statement.setString(6, sejour.titre);
            statement.setInt(7, sejour.nombrePersonnes);
            statement.setInt(8, sejour.statut);
            statement.setString(9, sejour.image);


            int nbRows = statement.executeUpdate();
            ArrayList<Object> data = new ArrayList<>();
            if(nbRows > 0){
                result.state = ResponseState.OK;
                result.message = "Sejour insérer avec succès";
                data.add(sejour);
            }
            result.response = data;
        }catch (SQLException e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ":" +e.getMessage());
        }
        return result;
    }

    public static QueryResponse updateSejour(Sejour sejour){
        String query = "UPDATE Sejours SET idHote = ?, date_debut = ?, date_fin = ?, prix = ?, lieux = ?, titre = ?, nbrePersonnes = ?, statut = ?, image = ? WHERE id =  ?";
        QueryResponse result = new QueryResponse();
        result.state = ResponseState.NOT_OK;
        result.message = "Sejour non modifié";
        try (Connection connection = Database.connect("one4All.sqlite")) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, sejour.idHote);
            statement.setString(2, sejour.dateDebut.toString());
            statement.setString(3, sejour.dateFin.toString());
            statement.setDouble(4, sejour.prix);
            statement.setString(5, sejour.lieu);
            statement.setString(6, sejour.titre);
            statement.setInt(7, sejour.nombrePersonnes);
            statement.setInt(8, sejour.statut);
            statement.setString(9, sejour.image);
            statement.setInt(10, sejour.id);


            int nbRows = statement.executeUpdate();
            ArrayList<Object> data = new ArrayList<>();
            if(nbRows > 0){
                result.state = ResponseState.OK;
                result.message = "Sejour modifié avec succès";
                data.add(sejour);
            }
            result.response = data;
        }catch (SQLException e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ":" +e.getMessage());
        }
        return result;
    }

    public static QueryResponse getAllSejours(User user){
        String query = "SELECT * FROM Sejours";
        QueryResponse result = new QueryResponse();
        ArrayList<Object> sejours = new ArrayList<>();

        if(user!=null && user.isHote()){
            query+= " WHERE idHote = "+user.id+" ORDER BY id ASC";
        }

        try (Connection connection = Database.connect("one4All.sqlite")) {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                Sejour sejour = getValues(rs);
                sejours.add(sejour);
            }
            result.state = ResponseState.OK;
            result.message = "Tous les séjours obtenus avec succès";
            result.response = sejours;
        }catch (SQLException e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + " : "+e.getMessage());

        }
        return result;
    }

    public static Sejour getValues(ResultSet rs) throws SQLException {
        Sejour sejour = new Sejour();
        sejour.id = rs.getInt("id");
        sejour.idHote = rs.getInt("idHote");
        QueryResponse userQuery = UserModel.getUserById(sejour.idHote);
        if(userQuery.state == ResponseState.OK){
            if(userQuery.response!=null && !userQuery.response.isEmpty()){
                sejour.hote = (User) userQuery.response.get(0);
            }
        }

        QueryResponse commentQuery = CommentModel.getAllComments(sejour.id);
        if(commentQuery.state == ResponseState.OK){
            if(commentQuery.response!=null && !commentQuery.response.isEmpty()){
                for (Object obj:commentQuery.response) {
                    Comment comment = (Comment) obj;
                    sejour.comments.add(comment);
                }
            }
        }
        sejour.dateDebut = Date.valueOf(rs.getString("date_debut"));
        sejour.dateFin = Date.valueOf(rs.getString("date_fin"));
        sejour.prix = rs.getDouble("prix");
        sejour.lieu = rs.getString("lieux");
        sejour.titre = rs.getString("titre");
        sejour.nombrePersonnes = rs.getInt("nbrePersonnes");
        sejour.image = rs.getString("image");
        sejour.statut = rs.getInt("statut");

        return sejour;
    }

    public static QueryResponse getSejourById(int id){
        String query = "SELECT * FROM Sejours WHERE id = ?";
        QueryResponse result = new QueryResponse();
        ArrayList<Object> sejours = new ArrayList<>();
        try (Connection connection = Database.connect("one4All.sqlite")) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                Sejour sejour = getValues(rs);
                sejours.add(sejour);
            }
            result.state = ResponseState.OK;
            result.message = "Séjour id = "+id+" extrait avec succès";
            result.response = sejours;
        }catch (SQLException e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + " : "+e.getMessage());

        }
        return result;
    }

    public static QueryResponse getSejourByHoteId(int id){
        String query = "SELECT * FROM Sejours WHERE idHote = ?";
        QueryResponse result = new QueryResponse();
        ArrayList<Object> sejours = new ArrayList<>();
        try (Connection connection = Database.connect("one4All.sqlite")) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                Sejour sejour = getValues(rs);
                sejours.add(sejour);
            }
            result.state = ResponseState.OK;
            result.message = "Séjours de l'utilisateur id = "+id+" extrait avec succès";
            result.response = sejours;
        }catch (SQLException e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + " : "+e.getMessage());

        }
        return result;
    }

    public static QueryResponse getSejourByMultiCriteria(Sejour sejour){
        // String query = "SELECT * FROM Sejour WHERE UPPER(titre) LIKE ? OR UPPER(lieux) LIKE ? OR nbrePersonnes = ? OR prix = ? OR date_debut = ? OR date_fin = ? COLLATE NOCASE";
        String query = "SELECT * FROM Sejours ";
        QueryResponse result = new QueryResponse();
        ArrayList<Object> sejours = new ArrayList<>();
        try (Connection connection = Database.connect("one4All.sqlite")) {
            String titrePart = "";
            String lieuPart = "";
            String nombrePersPart = "";
            String prixPart = "";
            String dateDebutPart = "";
            String dateFinPart = "";
            String hotePart = "";
            String statutPart = "";

            List<String> queryPart = new ArrayList<>();

            List<String> xPart = new ArrayList<>();

            if(sejour.statut!=0){
                statutPart = "statut = "+sejour.statut;
                queryPart.add(statutPart);
            }

            if(sejour.idHote!=0){
                hotePart = "idHote = "+sejour.idHote;
                queryPart.add(hotePart);
            }

            if(sejour.titre!=null && !sejour.titre.isEmpty()){
                titrePart = "UPPER(titre) LIKE '%"+sejour.titre.toUpperCase()+"%'";
                xPart.add(titrePart);
            }

            if(sejour.lieu!=null && !sejour.lieu.isEmpty()){
                lieuPart = "UPPER(lieux) LIKE  '%"+sejour.lieu.toUpperCase()+"%'";
                xPart.add(lieuPart);
            }

            try {
                if(sejour.nombrePersonnes!=0){
                    nombrePersPart = "nbrePersonnes BETWEEN "+sejour.nombrePersonnes+" AND "+addToNumber(sejour.nombrePersonnes);
                    xPart.add(nombrePersPart);
                }

                if(sejour.prix!=0){
                    int prixToInt = (int) sejour.prix;
                    prixPart = "prix BETWEEN "+prixToInt+" AND "+addToNumber(prixToInt);
                    xPart.add(prixPart);
                }

            } catch (NumberFormatException e) {
                System.out.println("Error: "+e.getMessage());

            }

            if(!xPart.isEmpty()){
                queryPart.add(" ( "+String.join(" OR ", xPart)+" ) ");
            }

            List<String> xDate = new ArrayList<>();
            if(sejour.dateDebut != null){
                dateDebutPart = "date_debut = '"+sejour.dateDebut+"'";
                xDate.add(dateDebutPart);
            }

            if(sejour.dateFin != null){
                dateFinPart = "date_fin = '"+sejour.dateFin+"'";
                xDate.add(dateFinPart);
            }

            if(!xDate.isEmpty()){
                queryPart.add(" ( "+String.join(" AND ", xDate)+" ) ");
            }

            if(!queryPart.isEmpty()){
                query += "WHERE "+String.join(" AND ", queryPart) + " COLLATE NOCASE";
            }

            System.out.println(query);
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                Sejour s = getValues(rs);
                sejours.add(s);
            }
            result.state = ResponseState.OK;
            result.message = "Liste des séjours obtenus avec succès";
            result.response = sejours;
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

    public static List<Sejour> defaultSejours = new ArrayList<>();
    public static void initSejoursTable(int nbSejours){
        Faker faker = new Faker();
        defaultSejours = new ArrayList<>();
        int nbHotes = UserModel.defaultHotes.size();
        for(int i = 0; i < nbSejours; i++){
            Sejour sejour = new Sejour();
            sejour.id = (i+1);
            // DateFin
            LocalDate dateFin = faker.date().future(30, TimeUnit.DAYS).toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();

            // Generate the delivery date, which is one day less than the order date
            LocalDate dateDebut = dateFin.minusDays(faker.random().nextInt(1, 20));

            sejour.dateDebut =  Date.valueOf(dateDebut);
            sejour.dateFin = Date.valueOf(dateFin);
            sejour.lieu = faker.address().city();
            sejour.titre = faker.pokemon().name();
            sejour.nombrePersonnes = faker.number().randomDigitNotZero();
            int idxImage  = faker.random().nextInt(1, 5);
            sejour.image = "house"+idxImage+".png";

            sejour.hote = UserModel.getFakerHote();
            sejour.idHote = sejour.hote.id;
            sejour.prix = faker.number().randomDouble(2,100, 1000);
            sejour.statut = SejourStatut.LIBRE.getValue();
            defaultSejours.add(sejour);
            createSejour(sejour);
        }
        System.out.println(nbSejours + " sejours created successfully");
    }

    public static Sejour getFakerSejour(){
        Faker faker = new Faker();
        int index = faker.random().nextInt(0, defaultSejours.size() - 1);
        return defaultSejours.get(index);
    }

    public static QueryResponse getCountByStatut(User user){
        QueryResponse result = new QueryResponse();
        result.state = ResponseState.NOT_OK;
        result.message = "Les nombres de sejours non récupérées";
        String query = "SELECT statut as type, COUNT(*) as count FROM Sejours " +
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
            result.message = "Les nombres de sejours  obtenus avec succès";
            result.state = ResponseState.OK;

        }catch (SQLException e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + " : "+e.getMessage());

        }
        return result;
    }
}
