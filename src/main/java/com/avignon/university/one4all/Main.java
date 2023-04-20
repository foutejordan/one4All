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
    }

    public static void main(String[] args) {
        Database.init(false);
        launch();
    }

}