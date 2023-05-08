package com.avignon.university.one4all.controllers;

import com.avignon.university.one4all.Main;
import com.avignon.university.one4all.models.QueryResponse;
import com.avignon.university.one4all.models.ResponseState;
import com.avignon.university.one4all.models.User;
import com.avignon.university.one4all.models.dao.SigninModel;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
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

    @FXML
    private Button btn_login;

    @FXML
    private AnchorPane container;

    double x, y = 0;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    private Hyperlink hide_password_btn;
    @FXML
    public void onShowPasswordClicked(ActionEvent event){

        password_textfield.setVisible(!password_textfield.isVisible());
        password_content_textfield.setVisible(!password_content_textfield.isVisible());
        show_password_btn.setVisible(!show_password_btn.isVisible());
        hide_password_btn.setVisible(!hide_password_btn.isVisible());
        if(event.getSource() == show_password_btn || event.getSource() == hide_password_btn){
            show_password_checkbox.setSelected(!show_password_checkbox.isSelected());
        }
    }

    public void onKeyReleasedInHiddenPwd(KeyEvent event){
        String pwd = password_content_textfield.getText();
        password_textfield.setText(pwd);
    }

    public void onKeyReleasedInPwd(KeyEvent event){
        String pwd = password_textfield.getText();
        password_content_textfield.setText(pwd);
    }

    public void onEnterKeyPressed(KeyEvent event){
        if(event.getCode() == KeyCode.ENTER){
            onLoggedInClicked();
        }
    }

    private final ReadOnlyObjectWrapper<Boolean> isCreateAccountClicked = new ReadOnlyObjectWrapper<>();

    private final ReadOnlyObjectWrapper<QueryResponse> isLoggedInClicked = new ReadOnlyObjectWrapper<>();


    public ReadOnlyObjectProperty<Boolean> isCreateAccountClickedProperty() {
        return isCreateAccountClicked.getReadOnlyProperty() ;
    }

    public ReadOnlyObjectProperty<QueryResponse> isLoggedInClickedProperty() {
        return isLoggedInClicked.getReadOnlyProperty() ;
    }

    public void onCreateAccountClicked(ActionEvent event){
        isCreateAccountClicked.set(true);
    }

    public void onLoggedInClicked(){
        String username = username_textfield.getText();
        String password = password_textfield.getText();
        QueryResponse response = SigninModel.signin(username, password);
        isLoggedInClicked.set(response);
    }

}
