package com.avignon.university.one4all.controllers;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class Signin implements Initializable {

    @FXML
    private PasswordField password_textfield;

    @FXML
    private CheckBox show_password_checkbox;

    @FXML
    private Hyperlink create_account_btn;

    @FXML
    private Hyperlink show_password_btn;

    @FXML
    private TextField username_textfield;

    @FXML
    private TextField password_content_textfield;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    public void onShowPasswordClicked(ActionEvent event){
        password_content_textfield.setText(password_textfield.getText());
        password_textfield.setVisible(!password_textfield.isVisible());
        password_content_textfield.setVisible(!password_content_textfield.isVisible());
        show_password_checkbox.setSelected(!show_password_checkbox.isSelected());
    }

    private final ReadOnlyObjectWrapper<Boolean> isCreateAccountClicked = new ReadOnlyObjectWrapper<>();

    public ReadOnlyObjectProperty<Boolean> isCreateAccountClickedProperty() {
        return isCreateAccountClicked.getReadOnlyProperty() ;
    }

    public void onCreateAccountClicked(ActionEvent event){
        isCreateAccountClicked.set(true);
    }
}
