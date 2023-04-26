package com.avignon.university.one4all.models.dao;

import com.avignon.university.one4all.models.QueryResponse;
import com.avignon.university.one4all.models.Reservation;
import com.avignon.university.one4all.models.Sejour;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReservationModel {

    public int sendReservationRequest(int idSejour, int statut, int idUser, int idHote) {
        int id = (int) CRUDHelper.create(
                "Reservations",
                new String[]{"idSejour", "statut","idUser", "idHote"},
                new Object[]{idSejour, statut, idUser, idHote},
                new int[]{Types.INTEGER, Types.INTEGER, Types.INTEGER, Types.INTEGER});

        return id;
    }


    // recuperer les reservations envoyees par un utilisateur en passant son ID
    public ArrayList<Reservation> readReservationByUserID(int idUser) {
        String query = "SELECT * FROM  Reservations WHERE idUser =" + idUser;
        ArrayList<Reservation> reservations = new ArrayList<>();
        try (Connection connection = Database.connect("one4All.sqlite")) {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                int idSejour = rs.getInt("idSejour");
                int statut = rs.getInt("statut");
                int IdUser = rs.getInt("idUser");
                int idHote = rs.getInt("idHote");


                Reservation reservation = new Reservation(idSejour, statut, IdUser, idHote);
                reservations.add(reservation);
            }
        } catch (SQLException e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load data from database ");
        }
        return reservations;
    }

    // Recuperer les reservations que l'hote a recu en passant son ID

    public ArrayList<Reservation> readReservationByHoteID(int idHote) {
        String query = "SELECT * FROM  Reservations WHERE idHote =" + idHote;
        ArrayList<Reservation> reservations = new ArrayList<>();
        try (Connection connection = Database.connect("one4All.sqlite")) {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                int idSejour = rs.getInt("idSejour");
                int statut = rs.getInt("statut");
                int IdUser = rs.getInt("idUser");


                Reservation reservation = new Reservation(idSejour, statut, IdUser, idHote);
                reservations.add(reservation);
            }
        } catch (SQLException e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load data from database ");
        }
        return reservations;
    }

    // refuser ou valider une reservation par l'hote, prend une chaine de caractere decision: "Refuse" ou "Valide"
    // cela prend l'id de la reservation a valider et un entier decison: 1-> refus, 2-> accepte
    public QueryResponse makeReservationDecision(int decision, int reservationID, int idSejour) {

        String query = "UPDATE reservations SET statut = ? WHERE id = ?";
        QueryResponse result = new QueryResponse();
        // Création de l'objet PreparedStatement pour exécuter la requête SQL
        try(Connection connection = Database.connect("one4All.sqlite")) {
            PreparedStatement rs = connection.prepareStatement(query);
            // Définition des paramètres de la requête SQL
            rs.setInt(1, decision);
            rs.setInt(2, reservationID);
            // Exécution de la requête SQL pour mettre à jour le champ "status" pour la réservation avec l'id spécifié
            int rowsUpdated = rs.executeUpdate();
            // Vérification du nombre de lignes mises à jour
            if (rowsUpdated > 0) {
                result.message = "Le champ 'statut' pour la réservation avec l'id " + reservationID + " a été mis à jour.";
                System.out.println("Le champ 'status' pour la réservation avec l'id " + reservationID + " a été mis à jour.");

                // il faut changer le statut du sejour
                SejourModel.changeSejourStatus(decision, idSejour);
            } else {
                System.out.println("Aucune réservation trouvée avec l'id " + reservationID + ".");
            }
        } catch (SQLException e) {

        }
        return result;
    }

    // Requêtes retournant le nombre total de réservation recu par l'Hote

    public int totalOfreservations(int hoteID) {
        String query = "SELECT COUNT(*) as count FROM  Reservations WHERE idHote = ?";
        int total = 0;
        try (Connection connection = Database.connect("one4All.sqlite")) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, hoteID);
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
