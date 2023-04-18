package com.avignon.university.one4all.controllers;

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

    private String type;

    @FXML
    private HBox header_sucessfully_logged_in;

    @FXML
    private AnchorPane sucessfully_logged_in;

    @FXML
    private Button sucessfully_logged_in_ok_btn;

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
        }
    }

    public void closeDialog(){
        Stage stage = null;
        switch (type){
            case "fermeture":
                stage = (Stage)header.getScene().getWindow();
                break;
            case "erreur":
                stage = (Stage)erreur_header.getScene().getWindow();
                break;
            case "no_user_found":
                stage = (Stage)header_no_user_found.getScene().getWindow();
                break;

            case "successfully_logged_in":
                stage = (Stage)sucessfully_logged_in.getScene().getWindow();
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

    public void setDialogToShow(String dialog){
        type = dialog;
        fermeture.setVisible(dialog.equals("fermeture"));
        erreur.setVisible(dialog.equals("erreur"));
        no_user_found.setVisible(dialog.equals("no_user_found"));
        sucessfully_logged_in.setVisible(dialog.equals("successfully_logged_in"));
    }

}
