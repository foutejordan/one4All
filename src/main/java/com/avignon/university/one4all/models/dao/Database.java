package com.avignon.university.one4all.models.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Database {


    public static Connection connect(String location) {
        String dbPrefix = "jdbc:sqlite:";
        Connection connection;
        try {
            connection = DriverManager.getConnection(dbPrefix + location);
        } catch (SQLException exception) {
            Logger.getAnonymousLogger().log(Level.SEVERE,
                    LocalDateTime.now() + ": Could not connect to SQLite DB at " +
                            location);
            return null;
        }
        return connection;
    }

    public static boolean checkDrivers(){
        try {
            Class.forName("org.sqlite.JDBC");
            DriverManager.registerDriver(new org.sqlite.JDBC());
            System.out.println("Connection to sqlite done");
            return true;
        } catch (ClassNotFoundException | SQLException classNotFoundException) {
            Logger.getAnonymousLogger().log(Level.SEVERE, LocalDateTime.now() + ": Could not start SQLite Drivers");
        }
        return false;
    }

    public static  void createUsersTable() {
        String query = "CREATE TABLE IF NOT EXISTS Users (" +
                "id INTEGER NOT NULL PRIMARY KEY ," +
                "login VARCHAR," +
                "password VARCHAR," +
                "role INTEGER )";
        try (Connection connection = Database.connect("one4All.sqlite")) {
            Statement statement = connection.createStatement();
            statement.execute(query);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not create this table data from database ");
        }
    }

    // le role c'est soit 1 = hote , 2 = persosnne connecte ou 3=particuluer,
    public static  void createStaysTable() {
        String query = "CREATE TABLE IF NOT EXISTS Sejour (" +
                "id INTEGER  NOT NULL PRIMARY KEY ," +
                "idHote INTEGER ," +
                "date_debut DATE," +
                "date_fin DATE ," +
                "prix INTEGER ," +
                "lieux VARCHAR," +
                "titre VARCHAR," +
                "nbrePersonnes INTEGER, " +
                "statut VARCHAR," +
                "role INTEGER," +
                "FOREIGN KEY (idHote) REFERENCES Users (id) )";
        try (Connection connection = Database.connect("one4All.sqlite")) {
            Statement statement = connection.createStatement();
            statement.execute(query);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not create this table data from database ");
        }
    }

    public static  void createCommentsTable() {
        String query = "CREATE TABLE IF NOT EXISTS Comments (" +
                "id INTEGER  NOT NULL PRIMARY KEY ," +
                "idUser INTEGER, " +
                "idSejour INTEGER," +
                "message VARCHAR(50)," +
                "date DATE," +
                "FOREIGN KEY (idUser) REFERENCES Users (id)," +
                "FOREIGN KEY (idSejour) REFERENCES Sejour (id))";
        try (Connection connection = Database.connect("one4All.sqlite")) {
            Statement statement = connection.createStatement();
            statement.execute(query);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not create this table data from database ");
        }
    }

    public static  void createBasketsTable() {
        String query = "CREATE TABLE IF NOT EXISTS Panier (" +
                "id INTEGER  NOT NULL PRIMARY KEY ," +
                "idSejour INTEGER," +
                "idUser INTEGER," +
                "FOREIGN KEY (idUser) REFERENCES Users (id)," +
                "FOREIGN KEY (idSejour) REFERENCES Sejour (id) )";
        try (Connection connection = Database.connect("one4All.sqlite")) {
            Statement statement = connection.createStatement();
            statement.execute(query);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not create this table data from database ");
        }
    }

    public static  void createReservationTable() {
        String query = "CREATE TABLE IF NOT EXISTS Reservations (" +
                "id INTEGER  NOT NULL PRIMARY KEY ," +
                "idSejour INTEGER," +
                "message VARCHAR(100)," +
                "decision VARCHAR(50)," +
                "idCurentUser INTEGER," +
                "idHoteUser INTEGER," +
                "FOREIGN KEY (idCurentUser) REFERENCES Users (id)," +
                "FOREIGN KEY (idHoteUser) REFERENCES Users (id)," +
                "FOREIGN KEY (idSejour) REFERENCES Sejour (id) )";
        try (Connection connection = Database.connect("one4All.sqlite")) {
            Statement statement = connection.createStatement();
            statement.execute(query);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not create this table data from database ");
        }
    }

    public static void init(){
        if(Database.checkDrivers()){
            Database.createUsersTable();
            Database.createStaysTable();
            Database.createCommentsTable();
            Database.createBasketsTable();
            Database.createReservationTable();
        }
    }
}
