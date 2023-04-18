package com.avignon.university.one4all.models.dao;

import com.avignon.university.one4all.models.Panier;
import com.avignon.university.one4all.models.QueryResponse;
import com.avignon.university.one4all.models.ResponseState;
import com.avignon.university.one4all.models.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SigninModel {

    public static QueryResponse signin(String login, String password) {
            String query = "SELECT * FROM  Users WHERE  login = ? AND password = ?";
            User user = null;
            QueryResponse result = new QueryResponse();
            try (Connection connection = Database.connect("one4All.sqlite")) {
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, login);
                statement.setString(2, password);
                ResultSet rs = statement.executeQuery();
                if (rs.next()) {
                    int idUser = rs.getInt("id");
                    String username = rs.getString("login");
                    String pass = rs.getString("password");
                    int role = rs.getInt("role");
                    user = new User(username, pass, role);
                    result.response = user;
                    result.message = "Connection réussi";
                    result.state = ResponseState.SUCCESS;
                }else{
                    result.response = null;
                    result.message = "Pas d'utilisateur avec ces  identifiants";
                    result.state = ResponseState.NOT_FOUND;
                }
            } catch (SQLException e) {
                Logger.getAnonymousLogger().log(
                        Level.SEVERE,
                        LocalDateTime.now() + ":" +e.getMessage());
            }
            return result;

    }

    public User getUser(int idUser) {

        String query = "SELECT * FROM  Users WHERE  id = " + idUser;
        User user = null;
        try (Connection connection = Database.connect("one4All.sqlite")) {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                String username = rs.getString("login");
                String pass = rs.getString("password");
                int role = rs.getInt("role");
                user = new User(username, pass, role);
            }
        } catch (SQLException e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load this user data from database ");
        }
        return user;
    }

}
