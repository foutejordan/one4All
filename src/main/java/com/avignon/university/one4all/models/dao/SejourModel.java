package com.avignon.university.one4all.models.dao;

import com.avignon.university.one4all.models.QueryResponse;
import com.avignon.university.one4all.models.ResponseState;
import com.avignon.university.one4all.models.Sejour;
import com.avignon.university.one4all.models.User;
import com.github.javafaker.Faker;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SejourModel {


    public static QueryResponse createSejour(Sejour sejour){
        String query = "INSERT INTO Sejour (idHote, date_debut, date_fin, prix, lieux, titre, nbrePersonnes, statut, image) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        QueryResponse result = new QueryResponse();
        try (Connection connection = Database.connect("one4All.sqlite")) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, sejour.hote);
            statement.setString(2, sejour.dateDebut.toString());
            statement.setString(3, sejour.dateFin.toString());
            statement.setDouble(4, sejour.prix);
            statement.setString(5, sejour.lieu);
            statement.setString(6, sejour.titre);
            statement.setInt(7, sejour.nombrePersonnes);
            statement.setInt(8, sejour.statut);
            statement.setString(9, sejour.image);


            int idSejour = statement.executeUpdate();
            ArrayList<Object> ids = new ArrayList<>();
            if(idSejour > 0){
                result.state = ResponseState.SUCCESS;
                result.message = "Sejour insérer avec succès";
                ids.add(idSejour);
            }else{
                result.state = ResponseState.NOT_INSERTED;
                result.message = "Sejour non inséré";
            }
            result.response = ids;
        }catch (SQLException e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ":" +e.getMessage());
        }
        return result;
    }

    public static QueryResponse getAllSejours(){
        String query = "SELECT * FROM Sejour";
        QueryResponse result = new QueryResponse();
        ArrayList<Object> sejours = new ArrayList<>();

        try (Connection connection = Database.connect("one4All.sqlite")) {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                Sejour sejour = getValues(rs);
                sejours.add(sejour);
            }
            result.state = ResponseState.SUCCESS;
            result.message = "Tous les séjours obtenus avec succès";
            result.response = sejours;
        }catch (SQLException e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + " : "+e.getMessage());

        }
        return result;
    }

    private static Sejour getValues(ResultSet rs) throws SQLException {
        Sejour sejour = new Sejour();
        sejour.id = rs.getInt("id");
        sejour.hote = rs.getInt("idHote");
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
        String query = "SELECT * FROM Sejour WHERE id = ?";
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
            result.state = ResponseState.SUCCESS;
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
        String query = "SELECT * FROM Sejour WHERE idHote = ?";
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
            result.state = ResponseState.SUCCESS;
            result.message = "Séjours de l'utilisateur id = "+id+" extrait avec succès";
            result.response = sejours;
        }catch (SQLException e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + " : "+e.getMessage());

        }
        return result;
    }

    public static QueryResponse getSejourByMultiCriteria(String x, Date dateDebut, Date dateFin){
        // String query = "SELECT * FROM Sejour WHERE UPPER(titre) LIKE ? OR UPPER(lieux) LIKE ? OR nbrePersonnes = ? OR prix = ? OR date_debut = ? OR date_fin = ? COLLATE NOCASE";
        String query = "SELECT * FROM Sejour ";
        QueryResponse result = new QueryResponse();
        ArrayList<Object> sejours = new ArrayList<>();
        try (Connection connection = Database.connect("one4All.sqlite")) {
            String titrePart = "";
            String lieuPart = "";
            String nombrePersPart = "";
            String prixPart = "";
            String datePart = "";

            List<String> queryPart = new ArrayList<>();


            int xToInt = 0;

            x = x.trim();
            if(!x.trim().isEmpty()){
                titrePart = "UPPER(titre) LIKE '%"+x.toUpperCase()+"%'";
                queryPart.add(titrePart);

                lieuPart = "UPPER(lieux) LIKE  '%"+x.toUpperCase()+"%'";
                queryPart.add(lieuPart);

                try {
                    xToInt = Integer.parseInt(x);
                    nombrePersPart = "nbrePersonnes BETWEEN "+xToInt+" AND "+addToNumber(xToInt);
                    queryPart.add(nombrePersPart);

                    prixPart = "prix BETWEEN "+xToInt+" AND "+addToNumber(xToInt);
                    queryPart.add(prixPart);
                } catch (NumberFormatException e) {
                    System.out.println("Error: "+e.getMessage());

                }
            }

            if(dateDebut != null){
                datePart = "date_debut = ?";
                if(dateFin != null){
                    datePart += " AND date_fin = ?";
                    datePart = "("+datePart+")";
                }
            }else{
                if(dateFin != null){
                    datePart = "date_fin = ?";
                }
            }

            if(!datePart.isEmpty()){
                queryPart.add(datePart);
            }

            if(!queryPart.isEmpty()){
                query += "WHERE "+String.join(" OR ", queryPart) + " COLLATE NOCASE";
            }

            System.out.println(query);
            PreparedStatement statement = connection.prepareStatement(query);
            if(dateDebut != null){
                statement.setString(1, dateDebut.toString());
                if(dateFin != null){
                    statement.setString(2, dateFin.toString());
                }
            }else{
                if(dateFin != null){
                    statement.setString(1, dateFin.toString());
                }
            }
            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                Sejour sejour = getValues(rs);
                sejours.add(sejour);
            }
            result.state = ResponseState.SUCCESS;
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

    public static void initSejourTable(int nbSejours){
        Faker faker = new Faker();
        for(int i = 0; i < nbSejours; i++){
            Sejour sejour = new Sejour();
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
            sejour.hote = faker.random().nextInt(1, 10);
            sejour.prix = faker.number().randomDouble(2,100, 1000);
            sejour.statut = faker.random().nextInt(1, 2);
            createSejour(sejour);
        }
        System.out.println(nbSejours + " sejours created successfully");
    }

    // staut: 1-> disponible, 2-> occupe, 3-> en cours de validation
    public static QueryResponse changeSejourStatus(int statut, int idSejour) {

        String query = "UPDATE Sejour SET statut = ? WHERE id = ?";
        QueryResponse result = new QueryResponse();
        // Création de l'objet PreparedStatement pour exécuter la requête SQL
        try(Connection connection = Database.connect("one4All.sqlite")) {
            PreparedStatement rs = connection.prepareStatement(query);
            // Définition des paramètres de la requête SQL
            rs.setInt(1, statut);
            rs.setInt(2, idSejour);
            // Exécution de la requête SQL pour mettre à jour le champ "status" pour la réservation avec l'id spécifié
            int rowsUpdated = rs.executeUpdate();
            // Vérification du nombre de lignes mises à jour
            if (rowsUpdated > 0) {
                result.message = "Le champ 'statut' pour le sejour avec l'id " + idSejour + " a été mis à jour.";
                System.out.println("Le champ 'status' pour la sejour avec l'id " + idSejour + " a été mis à jour.");

            } else {
                System.out.println("Aucun sejour trouvée avec l'id " + idSejour + ".");
            }
        } catch (SQLException e) {

        }
        return result;
    }

    // Requêtes retournant le nombre total de séjours disponibles
    public static int totalAvailableSejour() {
        String query = "SELECT COUNT(*) as count FROM  Sejour WHERE statut = ?";
        int total = 0;
        try (Connection connection = Database.connect("one4All.sqlite")) {
            PreparedStatement statement = connection.prepareStatement(query);
            // je passe 1 car 1-> disponible dans la table
            statement.setInt(1, 1);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                total = rs.getInt("count");
            }
        } catch (SQLException e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load data from database ");
        }
        return total;
    }

    //Requêtes retournant le nombre total de séjours d'un hote en cours de validation
    // en cours de validation=> statut = 3; et idHote

    public static int totalSejourEnCours(int idHote) {
        String query = "SELECT COUNT(*) as count FROM  Sejour WHERE statut = ? AND idHote = ?";
        int total = 0;
        try (Connection connection = Database.connect("one4All.sqlite")) {
            PreparedStatement statement = connection.prepareStatement(query);
            // je passe 1 car 1-> disponible dans la table
            statement.setInt(1, 3);
            statement.setInt(2, idHote);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                total = rs.getInt("count");
            }
        } catch (SQLException e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load data from database ");
        }
        return total;
    }

    // nombre total de sejour
    public static int totalSejours() {
        String query = "SELECT COUNT(*) as count FROM  Sejour";
        int total = 0;
        try (Connection connection = Database.connect("one4All.sqlite")) {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                total = rs.getInt("count");
            }
        } catch (SQLException e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load data from database ");
        }
        return total;
    }
}
