package com.avignon.university.one4all.controllers;

import com.avignon.university.one4all.Main;
import com.avignon.university.one4all.models.Sejour;
import com.avignon.university.one4all.multithreading.DataModel;
import com.avignon.university.one4all.multithreading.DataService;
import com.github.javafaker.Faker;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

public class SejoursItem implements Initializable {
    @FXML
    private GridPane sejours_container;

    private final ReadOnlyObjectWrapper<Sejour> onSejourClicked = new ReadOnlyObjectWrapper<>();

    public ReadOnlyObjectProperty<Sejour> onSejourClickedProperty() {
        return onSejourClicked.getReadOnlyProperty() ;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initSejoursContainer();
    }

    public void initSejoursContainer(){
        testJavaFaker();
        //loadUsingThread();
    }

    protected void testJavaFaker() {
        try {
            Faker faker = new Faker();
            int column = 1;
            int row = 1;
            int size = 50;
            sejours_container.getChildren().clear();
            for (int i = 1; i <= size; i++) {
                Sejour sejour = new Sejour();
                Date dateDebut =  new Date(faker.date().future(12, TimeUnit.DAYS).getTime());
                Date dateFin = new Date(faker.date().future(24, TimeUnit.DAYS).getTime());
                String lieu = faker.address().city();
                String titre = faker.funnyName().name();
                int nombrePersonne = faker.number().randomDigitNotZero();
                int idxImage  = faker.number().numberBetween(1, 5);

                sejour.setLieu(lieu);
                sejour.setTitre(titre);
                sejour.setDateDebut(dateDebut);
                sejour.setDateFin(dateFin);
                sejour.setNombrePersonnes(nombrePersonne);
                sejour.setImage("house"+idxImage+".png");


                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(Main.class.getResource("sejour-card.fxml"));
                AnchorPane sejourCard = loader.load();
                SejourCard sejourCardController = loader.getController();

                sejourCardController.onSejourClickedProperty().addListener((obs, oldResult, newResult)->{
                    if (newResult != null){
                        onSejourClicked.set(newResult);
                    }
                });
                sejourCardController.setSejour(sejour);
                if (column == 4){
                    column = 0;
                    row++;
                }
                sejours_container.add(sejourCard, column++, row);
                GridPane.setMargin(sejourCard, new Insets(3));
            }
        }catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    private void loadUsingThread(){
        int nData = 1000;
        int nCols = 2;
        int nRows = Math.round((float)nData/nCols);
        DataService dataService = new DataService(nRows, nCols);
        dataService.setOnSucceeded( event ->{
            DataModel dataModel = dataService.getValue();
            for (int row = 0; row < nRows; row++) {
                for (int column = 0; column < nCols; column++) {
                    try {
                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(Main.class.getResource("sejour-card.fxml"));
                        AnchorPane sejourCard = loader.load();
                        SejourCard sejourCardController = loader.getController();

                        Sejour sejour = dataModel.getSejour(row, column);
                        sejourCardController.setSejour(sejour);
                        sejours_container.add(sejourCard, column, row);
                        GridPane.setMargin(sejourCard, new Insets(3));

                    }catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

            }

        });

        dataService.start();

    }


}
