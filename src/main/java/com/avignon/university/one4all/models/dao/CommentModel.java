package com.avignon.university.one4all.models.dao;

import com.avignon.university.one4all.models.*;
import com.github.javafaker.Faker;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
            comment.date = new Date(System.currentTimeMillis());
            statement.setString(4, comment.date.toString());
            int nbComment = statement.executeUpdate();
            ArrayList<Object> ids = new ArrayList<>();
            if(nbComment > 0){
                result.state = ResponseState.OK;
                result.message = "Commentaire insérer avec succès";
                ids.add(comment);
            }else{
                result.state = ResponseState.NOT_OK;
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

    public static QueryResponse getAllComments(int idSejour) {
        QueryResponse result = new QueryResponse();
        String query = "SELECT * FROM  Comments WHERE idSejour = ? ORDER BY id ASC ";
        ArrayList<Object> comments = new ArrayList<>();
        try (Connection connection = Database.connect("one4All.sqlite")) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, idSejour);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Comment comment = getValues(rs);
                comments.add(comment);
            }
            result.message = "Commentaires recupérés avec succès";
            result.state = ResponseState.OK;
            result.response = comments;
        } catch (SQLException e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": "+e.getMessage());
        }
        return result;
    }

    public static Comment getValues(ResultSet rs) throws SQLException {
        Comment comment = new Comment();
        comment.id = rs.getInt("id");
        comment.idSejour = rs.getInt("idSejour");
        comment.idUser = rs.getInt("idUser");
        QueryResponse userResponse = UserModel.getUserById(comment.idUser);
        if(userResponse.state == ResponseState.OK){
            if(userResponse.response!=null && !userResponse.response.isEmpty()){
                comment.user = (User) userResponse.response.get(0);
            }
        }
        comment.message = rs.getString("message");
        comment.date = java.sql.Date.valueOf(rs.getString("date"));
        return comment;
    }

    public static List<Comment> defaultComments = new ArrayList<>();
    public static void initCommentsTable(int nbComments){
        Faker faker = new Faker();
        defaultComments = new ArrayList<>();
        List<User> users = UserModel.defaultUsers;
        List<Sejour> sejours = SejourModel.defaultSejours;
        for(int i = 0; i < nbComments; i++){
            Comment comment = new Comment();
            comment.id = i+1;
            comment.message = faker.lorem().sentence();

            comment.user = UserModel.getFakerUser();
            comment.idUser = comment.user.id;

            comment.sejour = SejourModel.getFakerSejour();
            comment.idSejour = comment.sejour.id;
            comment.date =  new java.sql.Date(faker.date().past(12, TimeUnit.DAYS).getTime());
            defaultComments.add(comment);
            createComment(comment);
        }
        System.out.println(nbComments + " comments created successfully");
    }

    public static Comment getFakerComment(){
        Faker faker = new Faker();
        int index = faker.random().nextInt(0, defaultComments.size() - 1);
        return defaultComments.get(index);
    }
}
