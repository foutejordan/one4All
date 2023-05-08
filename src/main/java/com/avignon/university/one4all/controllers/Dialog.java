package com.avignon.university.one4all.controllers;

import com.avignon.university.one4all.models.DialogType;
import com.github.javafaker.Bool;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class Dialog implements Initializable {

    @FXML
    private AnchorPane erreur;

    @FXML
    private Button erreur_ok_btn;

    @FXML
    private Button fermeture_no_btn;

    @FXML
    private Button fermeture_yes_btn;

    @FXML
    private HBox header;

    @FXML
    private AnchorPane container;

    @FXML
    private AnchorPane fermeture;

    @FXML
    private AnchorPane no_user_found;

    @FXML
    private Button no_user_found_ok_btn;

    @FXML
    private HBox header_no_user_found;

    @FXML
    private HBox erreur_header;

    private DialogType type;

    @FXML
    private HBox header_sucessfully_logged_in;

    @FXML
    private AnchorPane sucessfully_logged_in;

    @FXML
    private Button sucessfully_logged_in_ok_btn;

    @FXML
    private Button add_to_cart_ok_btn;

    @FXML
    private AnchorPane add_to_cart_success;

    @FXML
    private Button remove_from_cart_ok_btn;

    @FXML
    private AnchorPane remove_from_cart_success;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    public void onClick(ActionEvent event){
        closeDialog();
        if (event.getSource() == fermeture_no_btn){
            result.set(false);
        }else if(event.getSource() == fermeture_yes_btn){
            result.set(true);
        }else if (event.getSource() == erreur_ok_btn){
            result.set(true);
        }else if(event.getSource() == no_user_found_ok_btn){
            result.set(true);
        }else if(event.getSource() == sucessfully_logged_in_ok_btn){
            result.set(true);
        }else if(event.getSource() == add_to_cart_ok_btn){
            result.set(true);
        }else if(event.getSource() == remove_from_cart_ok_btn){
            result.set(true);
        }
    }

    @FXML
    private HBox header_add_to_cart;

    @FXML
    private HBox header_remove_cart;

    public void closeDialog(){
        Stage stage = null;
        switch (type){
            case FERMETURE:
                stage = (Stage)header.getScene().getWindow();
                break;
            case ERREUR_SERVER:
                stage = (Stage)erreur_header.getScene().getWindow();
                break;
            case NO_USER_FOUND:
                stage = (Stage)header_no_user_found.getScene().getWindow();
                break;
            case SUCCESSFULLY_LOGGED_IN:
                stage = (Stage)sucessfully_logged_in.getScene().getWindow();
                break;

            case AJOUTER_AU_PANIER:
                stage = (Stage)header_add_to_cart.getScene().getWindow();
                break;

            case RETIRER_DU_PANIER:
                stage = (Stage)header_remove_cart.getScene().getWindow();
                break;


        }
        if(stage!=null){
            stage.close();
        }

    }

    private final ReadOnlyObjectWrapper<Boolean> result = new ReadOnlyObjectWrapper<>();

    public ReadOnlyObjectProperty<Boolean> resultProperty() {
        return result.getReadOnlyProperty() ;
    }

    public Boolean getResult() {
        return result.get();
    }

    public void setDialogToShow(DialogType dialog){
        type = dialog;
        fermeture.setVisible(dialog == DialogType.FERMETURE);
        erreur.setVisible(dialog == DialogType.ERREUR_SERVER);
        no_user_found.setVisible(dialog == DialogType.NO_USER_FOUND);
        sucessfully_logged_in.setVisible(dialog == DialogType.SUCCESSFULLY_LOGGED_IN);
        add_to_cart_success.setVisible(dialog == DialogType.AJOUTER_AU_PANIER);
        remove_from_cart_success.setVisible(dialog == DialogType.RETIRER_DU_PANIER);
    }

}
