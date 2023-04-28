package com.avignon.university.one4all;

import com.avignon.university.one4all.controllers.Dashboard;
import com.avignon.university.one4all.models.Reservation;
import com.avignon.university.one4all.models.User;
import com.avignon.university.one4all.models.dao.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.w3c.dom.events.MouseEvent;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
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


        System.out.println(SejourModel.totalSejours());

        // lors de l'envoie de reservation, par defaut mettre le statut a 3 -> en cours de validation
        //ReservationModel.sendReservationRequest(6,3,2,5);

        // recupere les reservations envoye par l'utilisateur

        //ArrayList<Reservation> reservations = ReservationModel.readReservationByUserID(2);
        //System.out.println(reservations.get(0).getIdHote());

        //prendre la decision d'une reservation

        // 2-> validation
        //ReservationModel.makeReservationDecision(2,1,1);

        // 1-> refus,
        //ReservationModel.makeReservationDecision(1,2,3);

        // total des reservation recu
        //System.out.println(ReservationModel.totalOfreservations(8));

        //total sejour dispo
        //System.out.println(SejourModel.totalAvailableSejour());

        //total sejour en cours de validation ( pour hote )
       // System.out.println(SejourModel.totalSejourEnCours(5));

        //Total sejour d'un hote
        System.out.println(SejourModel.totalSejoursByHote(8));


    }

    public static void main(String[] args) {
        Database.init(true);
        launch();
    }

}