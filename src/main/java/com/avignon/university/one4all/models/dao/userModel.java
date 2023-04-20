package com.avignon.university.one4all.models.dao;

import com.avignon.university.one4all.models.QueryResponse;
import com.avignon.university.one4all.models.ResponseState;
import com.avignon.university.one4all.models.Sejour;
import com.avignon.university.one4all.models.User;
import com.github.javafaker.Faker;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class userModel {

    public static QueryResponse getAllUsers(){
        String query = "SELECT * FROM Users";
        QueryResponse result = new QueryResponse();
        ArrayList<Object> users = new ArrayList<>();

        try (Connection connection = Database.connect("one4All.sqlite")) {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                User user = getValues(rs);
                users.add(user);
            }
            result.state = ResponseState.SUCCESS;
            result.message = "Tous les utilisateurs obtenus avec succès";
            result.response = users;
        }catch (SQLException e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + " : "+e.getMessage());

        }
        return result;
    }

    public static QueryResponse getUserById(int id){
        QueryResponse result = new QueryResponse();
        String query = "SELECT * FROM Users WHERE id = ?";
        ArrayList<Object> users = new ArrayList<>();
        try (Connection connection = Database.connect("one4All.sqlite")) {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                User user = getValues(rs);
                users.add(user);
            }
            result.state = ResponseState.SUCCESS;
            result.message = "User id = "+id+" extrait avec succès";
            result.response = users;
        }catch (SQLException e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + " : "+e.getMessage());

        }
        return  result;
    }


    public static QueryResponse updateUser(User user){
        QueryResponse result = new QueryResponse();

        return  result;
    }

    public static QueryResponse deleteUserById(int id){
        QueryResponse result = new QueryResponse();

        return  result;
    }

    public static QueryResponse deleteAllUsers(){
        String query = "DELETE FROM Users";
        QueryResponse result = new QueryResponse();
        ArrayList<Object> users = new ArrayList<>();
        try (Connection connection = Database.connect("one4All.sqlite")) {
            PreparedStatement statement = connection.prepareStatement(query);
            int nbDeleted = statement.executeUpdate();
            if(nbDeleted > 0){
                result.state = ResponseState.SUCCESS;
                users.add(nbDeleted);
                result.response = users;
                result.message = "Tous les utilisateurs supprimé avec succès";
            }else{
                result.state = ResponseState.ERROR;
                result.response = null;
                result.message = "Aucun utilisateur supprimé";
            }

        }catch (SQLException e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + " : "+e.getMessage());

        }
        return  result;
    }

    public static QueryResponse getUserByMultiCriteria(){
        QueryResponse result = new QueryResponse();

        return  result;
    }

    private static User getValues(ResultSet rs) throws SQLException {
        User user = new User();
        user.id = rs.getInt("id");
        user.login = rs.getString("login");
        user.password = rs.getString("password");
        user.image = rs.getString("image");
        user.role = rs.getInt("role");
        return user;
    }

    public static void initUsersTable(int nbUsers){
        Faker faker = new Faker();
        for(int i = 0; i < nbUsers; i++){
            User user = new User();
            user.role = faker.random().nextInt(1,2);
            user.login = faker.name().firstName();
            user.password = "123";
            user.image = "user0"+faker.random().nextInt(1, 5)+".png";
            SignupModel.signup(user);
        }

        System.out.println(nbUsers + " users created successfully");
    }
}
