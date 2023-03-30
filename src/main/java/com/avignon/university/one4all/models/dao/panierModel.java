package com.avignon.university.one4all.models.dao;

import com.avignon.university.one4all.models.Comment;
import com.avignon.university.one4all.models.Panier;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class panierModel {

    public int addStayToBasket(int idSejour, int idUser) {
        int id = (int) CRUDHelper.create(
                "Basket",
                new String[]{"idSejour", "idUser"},
                new Object[]{idSejour, idUser},
                new int[]{Types.INTEGER, Types.VARCHAR, Types.DATE});

        return id;
    }

    // Requete a completer pour selectionner les sejours avec le join
    public ArrayList<Panier> readBasket() {
        String query = "SELECT * FROM  Basket";
        ArrayList<Panier> sejours = new ArrayList<>();
        try (Connection connection = Database.connect("one4All.sqlite")) {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                int idSejour = rs.getInt("idSejour");
                int idUser = rs.getInt("idUser");

                sejours.add(new Panier(idSejour, idUser));
            }
        } catch (SQLException e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load data from database ");
        }
        return sejours;
    }
}
