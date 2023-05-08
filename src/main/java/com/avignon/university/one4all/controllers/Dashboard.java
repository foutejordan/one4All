package com.avignon.university.one4all.controllers;

import com.avignon.university.one4all.Main;
import com.avignon.university.one4all.models.*;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import com.github.javafaker.Faker;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;


public class Dashboard implements Initializable {
    @FXML
    private Button close_btn;

    @FXML
    private Button minimize_btn;

    @FXML
    private AnchorPane top_anchor;

    @FXML
    private AnchorPane center_anchor;

    @FXML
    private Button login_btn;

    @FXML
    private HBox login_hbox;

    @FXML
    private Button logout_btn;

    @FXML
    private HBox logout_hbox;

    @FXML
    private Button dashboard_btn;

    @FXML
    private Button historique_btn;

    @FXML
    private Button panier_btn;

    @FXML
    private Button sejours_btn;

    @FXML
    private Label menu_title;

    @FXML
    private Hyperlink signin_hyperlink;

    @FXML
    private Hyperlink signout_hyperlink;

    public static Stage pStage;

    public  Dialog dialogController = null;
    public  Stage dialogStage = null;

    public  DashboardItem dashboardItemController = null;
    public  SejoursItem sejoursItemController = null;

    public  SejourDetails sejourDetailsController = null;
    public  HistoriqueItem historiqueItemController = null;

    public  ValidationItem validationItemItemController = null;
    public  PanierItem panierItemController = null;

    public  Signin signinController = null;
    public  Signup signupController = null;

    public User connectedUser;

    @FXML
    private Label username_lbl;

    @FXML
    private ImageView profile_iv;

    @FXML
    private Label role_lbl;

    protected void testJavaFaker(){
        Faker faker = new Faker();
        String idSejour = faker.idNumber().ssnValid();
        String dateDebut = faker.date().future(12, TimeUnit.DAYS).toString();
        String dateFin = faker.date().future(24, TimeUnit.DAYS).toString();
        double price = faker.number().randomDouble(2,100,1000);
        String lieu = faker.address().city();
        String titre = faker.funnyName().name();
        int nombrePersonne = faker.number().randomDigitNotZero();

        String avatar = faker.avatar().image();

        System.out.println("ID Séjour: "+idSejour);
        System.out.println("Titre: "+titre);
        System.out.println("Date Début: "+dateDebut);
        System.out.println("Date Fin: "+dateFin);
        System.out.println("Price: "+price);
        System.out.println("Lieu: "+lieu);
        System.out.println("Nombre de personne: "+nombrePersonne);
        System.out.println("Avatar: "+avatar);

    }

    double x, y = 0;

    @FXML
    private Button validation_btn;
    @FXML
    public void close(){
        if(dialogStage == null || dialogController == null){
            initDialog();
        }
        if(!dialogStage.isShowing()){
            dialogController.setDialogToShow(DialogType.FERMETURE);
            dialogController.resultProperty().addListener((obs, oldResult, newResult)->{
                if (newResult){
                    Stage stage = (Stage) top_anchor.getScene().getWindow();
                    stage.close();
                }
                clearDialog();
            });
            dialogStage.show();
        }

    }

    public void clearDialog(){
        if(dialogStage!=null){
            dialogStage.close();
            dialogStage = null;
            dialogController = null;
        }
    }
    public void initDialog(){
        try {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("dialog.fxml"));
            Parent parent = loader.load();
            dialogController = loader.getController();

            dialogStage = new Stage();
            Scene scene = new Scene(parent);
            dialogStage.setScene(scene);
            dialogStage.setResizable(false);
            dialogStage.setAlwaysOnTop(true);

            dialogStage.setX(pStage.getX() + 300);
            dialogStage.setY(pStage.getY() + 100);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initStyle(StageStyle.UNDECORATED);

            scene.setOnMousePressed(evt->{
                x = evt.getSceneX();
                y = evt.getSceneY();
            });

            scene.setOnMouseDragged(evt->{
                dialogStage.setX(evt.getScreenX() - x);
                dialogStage.setY(evt.getScreenY() - y);
            });


        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void initDashboardMenuItem(){
        try {
            menu_title.setText(": Dashboard");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("dashboard-item.fxml"));
            AnchorPane anchorPane = loader.load();
            dashboardItemController = loader.getController();
            dashboardItemController.setConnectedUser(connectedUser);
            center_anchor.getChildren().add(anchorPane);
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void getStyleOnClick(Button source, Button btn){
        if(source == btn){
            if(!btn.getStyleClass().contains("menu-btn-selected")){
                btn.getStyleClass().add("menu-btn-selected");
            }
        }else{
            btn.getStyleClass().remove("menu-btn-selected");
        }
    }

    public void initSejoursMenuItem(){
        try {
            menu_title.setText(": Séjours");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("sejours-item.fxml"));
            AnchorPane anchorPane = loader.load();
            sejoursItemController = loader.getController();
            sejoursItemController.setConnectedUser(connectedUser);
            sejoursItemController.onSejourClickedProperty().addListener((obs, oldResult, newResult)->{
                if (newResult != null){
                    center_anchor.getChildren().clear();
                    setStyleOnClick(sejours_btn);
                    initSejourDetails(newResult);
                }
            });
            center_anchor.getChildren().add(anchorPane);
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void initSejourDetails(Sejour sejour){
        try {
            menu_title.setText(": Détails");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("sejour-details.fxml"));
            AnchorPane anchorPane = loader.load();
            sejourDetailsController = loader.getController();
            sejourDetailsController.setConnectedUser(connectedUser);
            sejourDetailsController.setSejour(sejour);
            sejourDetailsController.isGoToListClickedProperty().addListener((obs, oldResult, newResult)->{
                if (newResult){
                    center_anchor.getChildren().clear();
                    initSejoursMenuItem();
                }
            });
            center_anchor.getChildren().add(anchorPane);
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void initHistoriqueMenuItem(){
        try {
            menu_title.setText(": Historique");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("historique-item.fxml"));
            AnchorPane anchorPane = loader.load();
            historiqueItemController = loader.getController();
            historiqueItemController.setConnectedUser(connectedUser);
            historiqueItemController.onSejourClickedProperty().addListener((obs, oldResult, newResult)->{
                if (newResult != null){
                    center_anchor.getChildren().clear();
                    setStyleOnClick(sejours_btn);
                    initSejourDetails(newResult);
                }
            });
            center_anchor.getChildren().add(anchorPane);
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void initValidationMenuItem(){
        try {
            menu_title.setText(": Validation");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("validation-item.fxml"));
            AnchorPane anchorPane = loader.load();
            validationItemItemController = loader.getController();
            validationItemItemController.setConnectedUser(connectedUser);
            validationItemItemController.onSejourClickedProperty().addListener((obs, oldResult, newResult)->{
                if (newResult != null){
                    center_anchor.getChildren().clear();
                    setStyleOnClick(sejours_btn);
                    initSejourDetails(newResult);
                }
            });
            center_anchor.getChildren().add(anchorPane);
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void initPanierMenuItem(){
        try {
            menu_title.setText(": Panier");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("panier-item.fxml"));
            AnchorPane anchorPane = loader.load();
            panierItemController = loader.getController();
            panierItemController.setConnectedUser(connectedUser);
            panierItemController.onPanierClickedProperty().addListener((obs, oldResult, newResult)->{
                if (newResult != null){
                    center_anchor.getChildren().clear();
                    setStyleOnClick(sejours_btn);
                    initSejourDetails(newResult.sejour);
                }
            });
            panierItemController.onValidateClickedProperty().addListener((obs, oldResult, newResult)->{
                if (newResult){
                    center_anchor.getChildren().clear();
                    setStyleOnClick(validation_btn);
                    initValidationMenuItem();
                }
            });
            center_anchor.getChildren().add(anchorPane);
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void initSignin(){
        center_anchor.getChildren().clear();
        try {
            menu_title.setText(": Connexion");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("signin.fxml"));
            AnchorPane anchorPane = loader.load();
            signinController = loader.getController();
            signinController.isCreateAccountClickedProperty().addListener((obs, oldResult, newResult)->{
                if (newResult){
                    initSignup();
                }
            });
            signinController.isLoggedInClickedProperty().addListener((obs, oldResult, newResult)->{
                if(dialogStage == null || dialogController == null){
                    initDialog();
                }
                if(!dialogStage.isShowing()){
                    if (newResult.state == ResponseState.ERROR){
                        dialogController.setDialogToShow(DialogType.ERREUR_SERVER);
                        dialogController.resultProperty().addListener((d_obs, d_oldResult, d_newResult)->{
                            if (d_newResult){
                                clearDialog();
                            }
                        });
                    }else if(newResult.state == ResponseState.NOT_OK){
                        dialogController.setDialogToShow(DialogType.NO_USER_FOUND);
                        dialogController.resultProperty().addListener((d_obs, d_oldResult, d_newResult)->{
                            if (d_newResult){
                                clearDialog();
                            }
                        });
                    }else{
                        dialogController.setDialogToShow(DialogType.SUCCESSFULLY_LOGGED_IN);
                        dialogController.resultProperty().addListener((d_obs, d_oldResult, d_newResult)->{
                            if (d_newResult){
                                clearDialog();
                                User user = (User)newResult.response.get(0);
                                setUserInfo(user);
                                on_user_logged_in();

                            }
                        });

                    }

                    dialogStage.show();
                }
            });
            center_anchor.getChildren().add(anchorPane);
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void initSignup(){
        center_anchor.getChildren().clear();
        try {
            menu_title.setText(": Création compte");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("signup.fxml"));
            AnchorPane anchorPane = loader.load();
            signupController = loader.getController();
            signupController.isLoginClickedProperty().addListener((obs, oldResult, newResult)->{
                if (newResult){
                    initSignin();
                }
            });
            center_anchor.getChildren().add(anchorPane);
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    public void minimize(){
        Stage stage = (Stage) top_anchor.getScene().getWindow();
        stage.setIconified(true);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        on_app_launched();
    }

    @FXML
    public void switch_login_logout(ActionEvent event){
        if (event.getSource() == login_btn || event.getSource() == signin_hyperlink){
            login_hbox.setVisible(false);
            initSignin();
        }else if(event.getSource() == logout_btn || event.getSource() == signout_hyperlink){
            logout_hbox.setVisible(false);
            clearUserInfo();
            on_user_logged_out();
        }
    }

    public void on_app_launched(){
        toggleItems();
        center_anchor.getChildren().clear();
        setStyleOnClick(sejours_btn);
        initSejoursMenuItem();
    }

    public void toggleItems(){
        login_hbox.setVisible(!isUserConnected());
        logout_hbox.setVisible(isUserConnected());
        dashboard_btn.setVisible(isUserConnected() && connectedUser.isHote());
        panier_btn.setVisible(isUserConnected() && connectedUser.isVoyageur());
        historique_btn.setVisible(isUserConnected());
        validation_btn.setVisible(isUserConnected());
    }

    public void on_user_logged_in(){
       toggleItems();
       if(isUserConnected()){
           if(connectedUser.isHote()){
               setStyleOnClick(dashboard_btn);
               initDashboardMenuItem();
           } else if (connectedUser.isVoyageur()) {
               setStyleOnClick(sejours_btn);
               initSejoursMenuItem();
           }
       }
    }


    public void on_user_logged_out(){
        toggleItems();
        setStyleOnClick(sejours_btn);
        initSejoursMenuItem();
    }

    @FXML
    public void onMenuItemClicked(ActionEvent event){
        Button source = (Button) event.getSource();
        center_anchor.getChildren().clear();

        if(source == dashboard_btn){
            initDashboardMenuItem();
        }else if(source == sejours_btn){
            initSejoursMenuItem();
        }else if(source == historique_btn){
            initHistoriqueMenuItem();
        }else if(source == panier_btn){
            initPanierMenuItem();
        } else if (source == validation_btn) {
            initValidationMenuItem();
        }

        setStyleOnClick(source);
    }

    public void setStyleOnClick(Button source){
        getStyleOnClick(source, dashboard_btn);
        getStyleOnClick(source, historique_btn);
        getStyleOnClick(source, panier_btn);
        getStyleOnClick(source, sejours_btn);
        getStyleOnClick(source, validation_btn);
    }

    private void setUserInfo(User user){
        connectedUser = user;
        username_lbl.setText(user.login);
        if(user.image != null){
            profile_iv.setImage(new Image(String.valueOf(Main.class.getResource("images/"+user.image))));
        }
        setRoleStyle();
        role_lbl.setText(Role.intToEnum(user.role).name());

    }

    public void setRoleStyle(){
        if(isUserConnected()){
            role_lbl.getStyleClass().remove("role-inconnu");
            if(connectedUser.isVoyageur()){
                role_lbl.getStyleClass().remove("role-hote");
                role_lbl.getStyleClass().add("role-voyageur");
            } else if (connectedUser.isHote()) {
                role_lbl.getStyleClass().remove("role-voyageur");
                role_lbl.getStyleClass().add("role-hote");
            }
        }else{
            role_lbl.getStyleClass().remove("role-hote");
            role_lbl.getStyleClass().remove("role-voyageur");
            role_lbl.getStyleClass().add("role-inconnu");
        }

    }

    public boolean isUserConnected(){
        return connectedUser!=null;
    }

    private void clearUserInfo(){
        connectedUser = null;
        username_lbl.setText("User");
        role_lbl.setText("Role");
        profile_iv.setImage(new Image(String.valueOf(Main.class.getResource("images/admin.png"))));
        setRoleStyle();
    }
}