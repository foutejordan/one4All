package com.avignon.university.one4all;

import com.avignon.university.one4all.controllers.Dashboard;
import com.avignon.university.one4all.models.dao.CRUDHelper;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.w3c.dom.events.MouseEvent;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Types;
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
        insertPerson("jordan", "jordan@gmail.com");
        CRUDHelper.selectUser();
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
    public static void insertPerson(String username, String password) {
        //update database
        int id = (int) CRUDHelper.create(
                "User",
                new String[]{"username", "password"},
                new Object[]{username, password},
                new int[]{Types.VARCHAR, Types.VARCHAR});
    }

    private static Connection connect(String location) {
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
}