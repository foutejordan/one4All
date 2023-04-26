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





}
