package com.avignon.university.one4all.models.dao;

import com.avignon.university.one4all.models.Comment;
import com.avignon.university.one4all.models.Panier;
import com.avignon.university.one4all.models.Sejour;

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
                new int[]{Types.INTEGER, Types.INTEGER});

        return id;
    }


    public ArrayList<Sejour> readBasket(int userID) {
        String query = "SELECT * FROM  Basket WHERE idUser =" + userID;
        ArrayList<Sejour> sejours = new ArrayList<>();
        try (Connection connection = Database.connect("one4All.sqlite")) {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                int idSejour = rs.getInt("idSejour");

                Sejour sejour = SejourModel.readSejourbyID(idSejour);

                sejours.add(sejour);
            }
        } catch (SQLException e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load data from database ");
        }
        return sejours;
    }

    public static int removeSejour(int id, String tablename) {
        int result = CRUDHelper.delete(tablename, id);
        return result;
    }


}
