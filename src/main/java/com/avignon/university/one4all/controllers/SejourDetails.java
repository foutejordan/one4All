package com.avignon.university.one4all.controllers;

import com.avignon.university.one4all.Main;
import com.avignon.university.one4all.models.Sejour;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class SejourDetails implements Initializable {

    @FXML
    private DatePicker dateDebut_dp;

    @FXML
    private DatePicker dateFin_dp;

    @FXML
    private ImageView image_iv;

    @FXML
    private Label lieu_lbl;

    @FXML
    private Label nombrePersone_lbl;

    @FXML
    private Label title_lbl;

    public Sejour getSejour() {
        return sejour;
    }

    private Sejour sejour;

    private final ReadOnlyObjectWrapper<Boolean> isGoToListClicked = new ReadOnlyObjectWrapper<>();

    public ReadOnlyObjectProperty<Boolean> isGoToListClickedProperty() {
        return isGoToListClicked.getReadOnlyProperty() ;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void setSejour(Sejour sejour){
        if(sejour != null){
            this.sejour = sejour;
            title_lbl.setText("Titre: "+sejour.getTitre());
            lieu_lbl.setText("Lieu: "+sejour.getLieu());
            nombrePersone_lbl.setText("Nombre de personnes: "+sejour.getNombrePersonnes());
            if(sejour.getImage()!=null && !sejour.getImage().isEmpty()){
                image_iv.setImage(new Image(String.valueOf(Main.class.getResource("images/"+sejour.getImage()))));
            }
            dateDebut_dp.setValue(sejour.getDateDebut().toLocalDate());
            dateFin_dp.setValue(sejour.getDateFin().toLocalDate());
        }
    }

    public void goToList(){
        this.isGoToListClicked.set(true);
    }
}
