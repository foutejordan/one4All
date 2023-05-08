package com.avignon.university.one4all.models.dao;

import com.avignon.university.one4all.models.QueryResponse;
import com.avignon.university.one4all.models.ResponseState;
import com.avignon.university.one4all.models.Role;
import com.avignon.university.one4all.models.User;
import com.github.javafaker.Faker;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserModel {

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
            result.state = ResponseState.OK;
            result.message = "Tous les utilisateurs obtenus avec succès";
            result.response = users;
        }catch (SQLException e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + " : "+e.getMessage());

        }
        return result;
    }

    public static QueryResponse getAllHoteUsers(){
        String query = "SELECT * FROM Users WHERE role = ?";
        QueryResponse result = new QueryResponse();
        ArrayList<Object> users = new ArrayList<>();

        try (Connection connection = Database.connect("one4All.sqlite")) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, Role.HOTE.getValue());
            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                User user = getValues(rs);
                users.add(user);
            }
            result.state = ResponseState.OK;
            result.message = "Tous les hotes obtenus avec succès";
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
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                User user = getValues(rs);
                users.add(user);
            }
            result.state = ResponseState.OK;
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
                result.state = ResponseState.OK;
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

    public static List<User> defaultUsers = new ArrayList<>();
    public static List<User> defaultHotes = new ArrayList<>();
    public static List<User> defaultVoyageurs = new ArrayList<>();
    public static void initUsersTable(int nbUsers){
        defaultUsers = new ArrayList<>();
        Faker faker = new Faker();
        for(int i = 0; i < nbUsers; i++){
            User user = new User();
            user.id = (i+1);
            user.role = faker.random().nextInt(1, Role.values().length);
            user.login = faker.name().firstName();
            user.password = "123";
            user.image = "user0"+faker.random().nextInt(1, 5)+".png";
            if(user.role == Role.HOTE.getValue()){
                defaultHotes.add(user);
            } else if (user.role ==Role.VOYAGEUR.getValue()) {
                defaultVoyageurs.add(user);
            }
            defaultUsers.add(user);
            SignupModel.signup(user);
        }

        System.out.println(nbUsers + " users created successfully");
    }

    public static User getFakerUser(){
        Faker faker = new Faker();
        int index = faker.random().nextInt(0, defaultUsers.size() - 1);
        return defaultUsers.get(index);
    }

    public static User getFakerHote(){
        Faker faker = new Faker();
        int index = faker.random().nextInt(0, defaultHotes.size() - 1);
        return defaultHotes.get(index);
    }

    public static User getFakerVoyageur(){
        Faker faker = new Faker();
        int index = faker.random().nextInt(0, defaultVoyageurs.size() - 1);
        return defaultVoyageurs.get(index);
    }


}
