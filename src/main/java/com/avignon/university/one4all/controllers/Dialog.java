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
    private Button fermeture_no_btn;

    @FXML
    private Button fermeture_yes_btn;

    @FXML
    private HBox header;

    @FXML
    private AnchorPane fermeture;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    public void onClick(ActionEvent event){
        if (event.getSource() == fermeture_no_btn){
            closeDialog();
            result.set(false);
        }else if(event.getSource() == fermeture_yes_btn){
            closeDialog();
            result.set(true);
        }
    }

    public void closeDialog(){
        Stage stage = (Stage)header.getScene().getWindow();
        stage.close();
    }

    private final ReadOnlyObjectWrapper<Boolean> result = new ReadOnlyObjectWrapper<>();

    public ReadOnlyObjectProperty<Boolean> resultProperty() {
        return result.getReadOnlyProperty() ;
    }

    public Boolean getResult() {
        return result.get();
    }

    public void setDialogToShow(String dialog){
        fermeture.setVisible(dialog.equals("fermeture"));
    }

}
