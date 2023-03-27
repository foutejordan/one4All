package com.avignon.university.one4all.controllers;

import com.avignon.university.one4all.Main;
import com.avignon.university.one4all.models.Sejour;
import com.github.javafaker.Faker;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

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
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initSejoursContainer();
    }

    public void initSejoursContainer(){
        testJavaFaker();
    }

    protected void testJavaFaker() {
        try {
            List<Sejour> apps = new ArrayList<>();
            Faker faker = new Faker();
            int column = 0;
            int row = 0;
            sejours_container.getChildren().clear();
            for (int i = 0; i < 10; i++) {
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
                sejourCardController.setData(sejour);
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
}
