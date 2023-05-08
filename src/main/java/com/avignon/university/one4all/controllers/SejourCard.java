package com.avignon.university.one4all.controllers;

import com.avignon.university.one4all.Main;
import com.avignon.university.one4all.models.MenuItemType;
import com.avignon.university.one4all.models.Sejour;
import com.avignon.university.one4all.models.User;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

public class SejourCard implements Initializable {

    @FXML
    private Label dateDebut_lbl;

    @FXML
    private Label dateFin_lbl;

    @FXML
    private ImageView image_iv;

    @FXML
    private Label lieu_lbl;

    @FXML
    private Label nombrePersone_lbl;

    @FXML
    private Label title_lbl;

    @FXML
    private Label prix_lbl;

    @FXML
    private Label hote_lbl;

    private Sejour sejour;


    private final ReadOnlyObjectWrapper<Sejour> onSejourClicked = new ReadOnlyObjectWrapper<>();

    public ReadOnlyObjectProperty<Sejour> onSejourClickedProperty() {
        return onSejourClicked.getReadOnlyProperty() ;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    private MenuItemType menuItemType;
    public void setMenuItemType(MenuItemType menuItemType){
        this.menuItemType = menuItemType;

    }
    public void setSejour(Sejour sejour){
        if(sejour != null){
            this.sejour = sejour;
            title_lbl.setText("Titre: "+sejour.titre);
            lieu_lbl.setText("Lieu: "+sejour.lieu);
            nombrePersone_lbl.setText("Nombre de personnes: "+sejour.nombrePersonnes);
            if(sejour.getImage()!=null && !sejour.image.isEmpty()){
                image_iv.setImage(new Image(String.valueOf(Main.class.getResource("images/"+sejour.getImage()))));
            }
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            String dateDebut = formatter.format(sejour.dateDebut);
            dateDebut_lbl.setText("Date de début: "+dateDebut);
            String dateFin = formatter.format(sejour.dateFin);
            dateFin_lbl.setText("Date de fin: "+dateFin);
            prix_lbl.setText("Prix: € "+sejour.prix);
            hote_lbl.setText("Hôte: "+sejour.hote.login);

        }
    }

    public Sejour getSejour() {
        return sejour;
    }

    private User connectedUser = null;
    public void setConnectedUser(User user){
        connectedUser = user;
    }

    @FXML
    public void onClick(){
        onSejourClicked.set(this.sejour);
    }
}
