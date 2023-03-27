package com.avignon.university.one4all.controllers;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;

import java.net.URL;
import java.util.ResourceBundle;

public class Signup implements Initializable {
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.initRolesChoiceBox();
    }

    private final ReadOnlyObjectWrapper<Boolean> isLoginClicked = new ReadOnlyObjectWrapper<>();

    public ReadOnlyObjectProperty<Boolean> isLoginClickedProperty() {
        return isLoginClicked.getReadOnlyProperty() ;
    }

    @FXML
    public void onLoginclicked(ActionEvent event){
        isLoginClicked.set(true);
    }

    @FXML
    private ChoiceBox<String> roles_choicebox;

    ObservableList<String> roles = FXCollections.observableArrayList(
            "Voyageur", "Hôte"
    );

    public void initRolesChoiceBox(){
        roles_choicebox.setItems(roles);
    }
}
