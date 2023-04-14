package com.avignon.university.one4all;

import com.avignon.university.one4all.controllers.Dashboard;
import com.avignon.university.one4all.models.User;
import com.avignon.university.one4all.models.dao.CRUDHelper;
import com.avignon.university.one4all.models.dao.Database;
import com.avignon.university.one4all.models.dao.SignupModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.w3c.dom.events.MouseEvent;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends Application {

    double x, y = 0;
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("dashboard.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        Dashboard.pStage = stage;
        stage.setTitle("One4All");
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);

        scene.setOnMousePressed(event->{
            x = event.getSceneX();
            y = event.getSceneY();
        });

        scene.setOnMouseDragged(event ->{
            stage.setX(event.getScreenX() - x);
            stage.setY(event.getScreenY() - y);
        });

        stage.show();
        checkDrivers();
        createUsersTable();
        createStaysTable();
        createCommentTable();
        createBasketTable();

//        alterTableSejour();
//        alterTableComment();
//        alterTableBasket();
        System.out.println(SignupModel.signup("jordqn", "123", 1));

    }

    public static void main(String[] args) {
        launch();

    }


    private static boolean checkDrivers() {
        try {
            Class.forName("org.sqlite.JDBC");
            DriverManager.registerDriver(new org.sqlite.JDBC());
            System.out.println("Connection to sqlite done");
            return true;
        } catch (ClassNotFoundException | SQLException classNotFoundException) {
            Logger.getAnonymousLogger().log(Level.SEVERE, LocalDateTime.now() + ": Could not start SQLite Drivers");
            return false;
        }
    }

    public static  void createUsersTable() {
        String query1 = "CREATE TABLE Users (" +
                "id INTEGER NOT NULL PRIMARY KEY ," +
                "login VARCHAR," +
                "password VARCHAR," +
                "role INTEGER )";
        try (Connection connection = Database.connect("one4All.sqlite")) {
            Statement statement = connection.createStatement();
            statement.executeQuery(query1);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not create this table data from database ");
        }
    }

    // le role c'est soit 1 = hote , 2 = persosnne connecte ou 3=particuluer,
    public static  void createStaysTable() {
        String query1 = "CREATE TABLE Sejour (" +
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
            statement.executeQuery(query1);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not create this table data from database ");
        }
    }


    public static  void createCommentTable() {
        String query1 = "CREATE TABLE Comments (" +
                "id INTEGER  NOT NULL PRIMARY KEY ," +
                "idUser INTEGER, " +
                "idSejour INTEGER," +
                "message VARCHAR(50)," +
                "date DATE," +
                "FOREIGN KEY (idUser) REFERENCES Users (id)," +
                "FOREIGN KEY (idSejour) REFERENCES Sejour (id))";
        try (Connection connection = Database.connect("one4All.sqlite")) {
            Statement statement = connection.createStatement();
            statement.executeQuery(query1);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not create this table data from database ");
        }
    }

    public static  void createBasketTable() {
        String query1 = "CREATE TABLE Panier (" +
                "id INTEGER  NOT NULL PRIMARY KEY ," +
                "idSejour INTEGER," +
                "idUser INTEGER," +
                "FOREIGN KEY (idUser) REFERENCES Users (id)," +
                "FOREIGN KEY (idSejour) REFERENCES Sejour (id) )";
        try (Connection connection = Database.connect("one4All.sqlite")) {
            Statement statement = connection.createStatement();
            statement.executeQuery(query1);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not create this table data from database ");
        }
    }

}