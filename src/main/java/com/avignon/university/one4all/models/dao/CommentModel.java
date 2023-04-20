package com.avignon.university.one4all.models.dao;

import com.avignon.university.one4all.models.*;
import com.github.javafaker.Faker;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CommentModel {


    public static QueryResponse createComment(Comment comment){
        String query = "INSERT INTO Comments (idUser, idSejour, message, date) VALUES (?, ?, ?, ?)";
        QueryResponse result = new QueryResponse();
        try (Connection connection = Database.connect("one4All.sqlite")) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, comment.idUser);
            statement.setInt(2, comment.idSejour);
            statement.setString(3, comment.message);
            statement.setString(4, comment.date.toString());

            int idComment = statement.executeUpdate();
            ArrayList<Object> ids = new ArrayList<>();
            if(idComment > 0){
                result.state = ResponseState.SUCCESS;
                result.message = "Commentaire insérer avec succès";
                ids.add(idComment);
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

    public static QueryResponse getAllComments(int idStay) {
        QueryResponse result = new QueryResponse();
        String query = "SELECT * FROM  Comment WHERE idSejour = ?";
        ArrayList<Object> comments = new ArrayList<>();
        try (Connection connection = Database.connect("one4All.sqlite")) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, idStay);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Comment comment = getValues(rs);
                comments.add(comment);
            }
            result.message = "Commentaires recupérés avec succès";
            result.state = ResponseState.SUCCESS;
            result.response = comments;
        } catch (SQLException e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load Persons from database ");
        }
        return result;
    }

    public static Comment getValues(ResultSet rs) throws SQLException {
        Comment comment = new Comment();
        comment.id = rs.getInt("id");
        comment.idSejour = rs.getInt("idSejour");
        comment.idUser = rs.getInt("idUser");
        comment.message = rs.getString("message");
        comment.date = java.sql.Date.valueOf(rs.getString("date"));
        return comment;
    }

    public static void initCommentTable(int nbComments){
        Faker faker = new Faker();
        for(int i = 0; i < nbComments; i++){
            Comment comment = new Comment();
            comment.message = faker.lorem().characters(10, 20, true);
            comment.idUser = faker.random().nextInt(1, 10);
            comment.idSejour = faker.random().nextInt(1, 50);
            comment.date =  new java.sql.Date(faker.date().past(12, TimeUnit.DAYS).getTime());
            createComment(comment);
        }
        System.out.println(nbComments + " comments created successfully");
    }
}
