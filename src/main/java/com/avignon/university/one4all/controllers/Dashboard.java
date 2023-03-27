package com.avignon.university.one4all.controllers;

import com.avignon.university.one4all.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import com.github.javafaker.Faker;
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
    public  HistoriqueItem historiqueItemController = null;
    public  PanierItem panierItemController = null;

    public  Signin signinController = null;
    public  Signup signupController = null;

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
    public void close(){
        if(dialogStage == null || dialogController == null){
            initDialog();
        }
        if(!dialogStage.isShowing()){
            dialogController.setDialogToShow("fermeture");
            dialogController.resultProperty().addListener((obs, oldResult, newResult)->{
                if (newResult){
                    Stage stage = (Stage) top_anchor.getScene().getWindow();
                    stage.close();
                }
            });
            dialogStage.show();
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
            dashboardItemController = loader.getController();
            center_anchor.getChildren().add(loader.load());
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getStyleOnClick(Button source, Button btn){
        return source == btn ?"-fx-background-color:#ae2d3c;":"-fx-background-color:transparent;";
    }

    public void initSejoursMenuItem(){
        try {
            menu_title.setText(": Séjours");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("sejours-item.fxml"));
            sejoursItemController = loader.getController();
            center_anchor.getChildren().add(loader.load());
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void initHistoriqueMenuItem(){
        try {
            menu_title.setText(": Historique");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("historique-item.fxml"));
            historiqueItemController = loader.getController();
            center_anchor.getChildren().add(loader.load());
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void initPanierMenuItem(){
        try {
            menu_title.setText(": Panier");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("panier-item.fxml"));
            panierItemController = loader.getController();
            center_anchor.getChildren().add(loader.load());
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
            Node node = loader.load();
            signinController = loader.getController();
            signinController.isCreateAccountClickedProperty().addListener((obs, oldResult, newResult)->{
                if (newResult){
                    initSignup();
                }
            });
            center_anchor.getChildren().add(node);
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
            Node node = loader.load();
            signupController = loader.getController();
            signupController.isLoginClickedProperty().addListener((obs, oldResult, newResult)->{
                if (newResult){
                    initSignin();
                }
            });
            center_anchor.getChildren().add(node);
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
    }

    @FXML
    public void switch_login_logout(ActionEvent event){
        if (event.getSource() == login_btn || event.getSource() == signin_hyperlink){
            login_hbox.setVisible(false);
            initSignin();
        }else if(event.getSource() == logout_btn || event.getSource() == signout_hyperlink){
            logout_hbox.setVisible(false);
        }

    }

    @FXML
    public void switch_signin_signup(){

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
        }

        setStyleOnClick(source);
    }

    public void setStyleOnClick(Button source){
        dashboard_btn.setStyle(getStyleOnClick(source, dashboard_btn));
        historique_btn.setStyle(getStyleOnClick(source, historique_btn));
        panier_btn.setStyle(getStyleOnClick(source, panier_btn));
        sejours_btn.setStyle(getStyleOnClick(source, sejours_btn));
    }
}