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
                "login TEXT," +
                "password TEXT," +
                "image TEXT," +
                "role INTEGER )";
        try (Connection connection = Database.connect("one4All.sqlite")) {
            Statement statement = connection.createStatement();
            statement.execute(query);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + " : "+e.getMessage());
        }
    }

    public static void deleteAllTables(){
        String tables[] = {"Sejours", "Users", "Comments", "Paniers", "Historiques"};

        try (Connection connection = Database.connect("one4All.sqlite")) {
            for (String table_name:tables) {
                String query = "DROP TABLE IF EXISTS "+table_name;
                Statement statement = connection.createStatement();
                statement.executeUpdate(query);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + " : "+e.getMessage());
        }
    }

    public static  void createSejoursTable() {
        String query = "CREATE TABLE IF NOT EXISTS Sejours (" +
                "id INTEGER  NOT NULL PRIMARY KEY ," +
                "idHote INTEGER ," +
                "date_debut TEXT," +
                "date_fin TEXT ," +
                "prix REAL ," +
                "lieux TEXT," +
                "titre TEXT," +
                "nbrePersonnes INTEGER, " +
                "image TEXT," +
                "statut INTEGER," +
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
                "message TEXT," +
                "date TEXT," +
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

    public static  void createPaniersTable() {
        String query = "CREATE TABLE IF NOT EXISTS Paniers (" +
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

    public static  void createHistoriquesTable() {
        String query = "CREATE TABLE IF NOT EXISTS Historiques (" +
                "id INTEGER  NOT NULL PRIMARY KEY ," +
                "idSejour INTEGER ," +
                "idHote INTEGER ," +
                "idUser INTEGER ," +
                "date_debut TEXT," +
                "date_fin TEXT ," +
                "prix REAL ," +
                "lieux TEXT," +
                "titre TEXT," +
                "nbrePersonnes INTEGER, " +
                "image TEXT," +
                "statut INTEGER," +
                "FOREIGN KEY (idSejour) REFERENCES Sejours (id),"+
                "FOREIGN KEY (idUser) REFERENCES Users (id),"+
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

    public static void init(Boolean reset){
        if(Database.checkDrivers()){
            if(reset){
                Database.deleteAllTables();
            }

            Database.createUsersTable();
            Database.createSejoursTable();
            Database.createCommentsTable();
            Database.createPaniersTable();
            Database.createHistoriquesTable();

            if(reset){
                UserModel.initUsersTable(20);
                SejourModel.initSejoursTable(100);
                CommentModel.initCommentsTable(20);
                PanierModel.initPaniersTable(30);
                HistoriqueModel.initHistoriquesTable(20);
            }

        }
    }
}
