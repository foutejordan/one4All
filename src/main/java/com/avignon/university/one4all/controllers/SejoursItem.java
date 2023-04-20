package com.avignon.university.one4all.controllers;

import com.avignon.university.one4all.Main;
import com.avignon.university.one4all.models.QueryResponse;
import com.avignon.university.one4all.models.ResponseState;
import com.avignon.university.one4all.models.Sejour;
import com.avignon.university.one4all.models.dao.SejourModel;
import com.avignon.university.one4all.multithreading.DataModel;
import com.avignon.university.one4all.multithreading.DataService;
import com.github.javafaker.Faker;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
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

    @FXML
    private Button rechercher_btn;

    @FXML
    private DatePicker dateDebut_dp;

    @FXML
    private DatePicker dateFin_dp;

    @FXML
    private TextField titre_lieu_nbPersonne_tf;


    public void initSejoursContainer(){
        testJavaFaker();
        //loadUsingThread();
    }

    private void loadCards(QueryResponse qr) throws IOException {
        if(qr.state == ResponseState.SUCCESS){
            int column = 1;
            int row = 1;
            int size = qr.response.size();
            sejours_container.getChildren().clear();
            for (int i = 1; i <= size; i++) {
                Sejour sejour = (Sejour) qr.response.get(i-1);
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
        }
    }
    protected void testJavaFaker() {
        try {
            QueryResponse qr = SejourModel.getAllSejours();
            loadCards(qr);
        }catch (IOException e) {
            System.out.println("ERREUR: "+e.getMessage());
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

    @FXML
    public void onRechercherClicked(){
        try {
            String multi_value = titre_lieu_nbPersonne_tf.getText();
            Date dateDebut = null;
            Date dateFin = null;
            LocalDate d1 = dateDebut_dp.getValue();
            LocalDate d2 = dateFin_dp.getValue();

            if(d1 != null){
                dateDebut = Date.valueOf(d1);
            }

            if(d2!=null){
                dateFin = Date.valueOf(d2);
            }

            QueryResponse qr = SejourModel.getSejourByMultiCriteria(multi_value, dateDebut, dateFin);
            loadCards(qr);
        } catch (IOException e) {
            System.out.println("ERREUR: "+e.getMessage());
        }
    }


}
