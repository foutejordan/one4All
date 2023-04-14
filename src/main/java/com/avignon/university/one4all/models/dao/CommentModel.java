package com.avignon.university.one4all.models.dao;

import com.avignon.university.one4all.models.Comment;
import com.avignon.university.one4all.models.Sejour;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CommentModel {



    public int addComment(int idSejour,int idUser, String message, Date date) {
        int id = (int) CRUDHelper.create(
                "Comment",
                new String[]{"idSejour","idUser", "message", "date"},
                new Object[]{idSejour,idUser, message, date},
                new int[]{Types.INTEGER,Types.INTEGER, Types.VARCHAR, Types.DATE});

        return id;
    }

    public ArrayList<Comment> readComments(int idStay) {
        String query = "SELECT * FROM  Comment WHERE idSejour = " + idStay;
        ArrayList<Comment> comments = new ArrayList<>();
        try (Connection connection = Database.connect("one4All.sqlite")) {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                int idSejour = rs.getInt("idSejour");
                int idUser = rs.getInt("idUser");
                String message = rs.getString("message");
                Date date = rs.getDate("date");

                comments.add(new Comment(idSejour,idUser, message, date));
            }
        } catch (SQLException e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load Persons from database ");
        }
        return comments;
    }
}
