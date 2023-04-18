package com.avignon.university.one4all.controllers;

import com.avignon.university.one4all.Main;
import com.avignon.university.one4all.models.Sejour;
import com.github.javafaker.Faker;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

public class PanierItem implements Initializable {

    @FXML
    private GridPane sejours_container;

    private final ReadOnlyObjectWrapper<Sejour> onRemoveClicked = new ReadOnlyObjectWrapper<>();

    public ReadOnlyObjectProperty<Sejour> onRemoveClickedProperty() {
        return onRemoveClicked.getReadOnlyProperty() ;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        testJavaFaker();
    }

    protected void testJavaFaker() {
        try {
            Faker faker = new Faker();
            int column = 1;
            int row = 1;
            int size = 6;
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
                loader.setLocation(Main.class.getResource("sejour-card-remove.fxml"));
                AnchorPane sejourCard = loader.load();
                SejourCardRemove sejourCardController = loader.getController();

                sejourCardController.onRemoveClickedProperty().addListener((obs, oldResult, newResult)->{
                    if (newResult != null){
                        onRemoveClicked.set(newResult);
                    }
                });
                sejourCardController.setSejour(sejour);
                if (column == 4){
                    column = 1;
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
