package com.avignon.university.one4all.models.dao;

import com.avignon.university.one4all.models.Sejour;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SejourModel {

    public int createSejour(int idHote, Date date_debut, Date date_fin, int prix, String lieux, String titre, int nbrePersonnes, String statut) {
        int id = (int) CRUDHelper.create(
                "User",
                new String[]{"idHote", "date_debut", "date_fin", "prix", "lieux", "titre", "nbrePersonnes", "statut"},
                new Object[]{idHote, date_debut, date_fin, prix, lieux, titre, nbrePersonnes, statut},
                new int[]{Types.INTEGER, Types.DATE, Types.DATE, Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.INTEGER, Types.VARCHAR});

        return id;
    }

    public ArrayList<Sejour> readSejour() {
        String query = "SELECT * FROM  Sejour";
        ArrayList<Sejour> sejours = new ArrayList<>();
        try (Connection connection = Database.connect("one4All.sqlite")) {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                int idHote = rs.getInt("idHote");
                Date date_debut = rs.getDate("date_debut");
                Date date_fin = rs.getDate("date_debut");
                int prix = rs.getInt("prix");
                String lieux = rs.getString("lieux");
                String titre = rs.getString("titre");
                int nbrePersonnes = rs.getInt("nbrePersonnes");
                String statut = rs.getString("statut");
                sejours.add(new Sejour(idHote, date_debut, date_fin, prix, lieux, titre, nbrePersonnes, statut));
            }
        } catch (SQLException e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load Persons from database ");

        }

        return sejours;
    }
}
