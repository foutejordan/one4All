package com.avignon.university.one4all.models.dao;

import com.avignon.university.one4all.models.Panier;
import com.avignon.university.one4all.models.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SigninModel {

    public static User signin(String login, String password) {
            String query = "SELECT * FROM  User WHERE  login = " + login + " WHERE password = " + password;
            User user = null;
            try (Connection connection = Database.connect("one4All.sqlite")) {
                PreparedStatement statement = connection.prepareStatement(query);
                ResultSet rs = statement.executeQuery();
                while (rs.next()) {
                    int idUser = rs.getInt("id");
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
