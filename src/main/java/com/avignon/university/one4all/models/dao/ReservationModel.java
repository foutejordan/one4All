package com.avignon.university.one4all.models.dao;

import com.avignon.university.one4all.models.Reservation;
import com.avignon.university.one4all.models.Sejour;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReservationModel {

    public int sendReservationRequest(int idSejour, String message, String decision, int idCurentUser, int idHoteUser) {
        int id = (int) CRUDHelper.create(
                "Reservations",
                new String[]{"idSejour", "message","decision", "idCurentUser", "idHoteUser"},
                new Object[]{idSejour, message, decision, idCurentUser, idHoteUser},
                new int[]{Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.INTEGER, Types.INTEGER});

        return id;
    }


    // recuperer les reservations envoyees par un utilisateur en passant son ID
    public ArrayList<Reservation> readReservationByUserID(int userID) {
        String query = "SELECT * FROM  Reservations WHERE idCurentUser =" + userID;
        ArrayList<Reservation> reservations = new ArrayList<>();
        try (Connection connection = Database.connect("one4All.sqlite")) {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                int idSejour = rs.getInt("idSejour");
                int idCurentUser = rs.getInt("idCurentUser");
                int idHoteUser = rs.getInt("idHoteUser");
                String message = rs.getString("message");
                String decision = rs.getString("decision");

                Reservation reservation = new Reservation(idSejour, idCurentUser, idHoteUser, message, decision);
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

    public ArrayList<Reservation> readReservationByHoteID(int hoteID) {
        String query = "SELECT * FROM  Reservations WHERE idHoteUser =" + hoteID;
        ArrayList<Reservation> reservations = new ArrayList<>();
        try (Connection connection = Database.connect("one4All.sqlite")) {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                int idSejour = rs.getInt("idSejour");
                int idCurentUser = rs.getInt("idCurentUser");
                int idHoteUser = rs.getInt("idHoteUser");
                String message = rs.getString("message");
                String decision = rs.getString("decision");

                Reservation reservation = new Reservation(idSejour, idCurentUser, idHoteUser, message, decision);
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

    public int makeReservationDecision(int idSejour, String message, String decision, int idCurentUser, int idHoteUser) {

        int id = (int) CRUDHelper.update(
                "Reservations",
                new String[]{"idSejour", "message","decision", "idCurentUser", "idHoteUser"},
                new Object[]{idSejour, message, decision, idCurentUser, idHoteUser},
                new int[]{Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.INTEGER, Types.INTEGER},
                "decision",
                2,
                2
                );

        return id;
    }

}
