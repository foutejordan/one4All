package com.avignon.university.one4all.controllers;

import com.avignon.university.one4all.Main;
import com.avignon.university.one4all.models.*;
import com.avignon.university.one4all.models.dao.SejourModel;
import com.avignon.university.one4all.models.dao.UserModel;
import com.avignon.university.one4all.multithreading.DataModel;
import com.avignon.university.one4all.multithreading.DataService;
import com.github.javafaker.Faker;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

public class SejoursItem implements Initializable {
    @FXML
    private GridPane sejours_container;

    private final ReadOnlyObjectWrapper<Sejour> onSejourClicked = new ReadOnlyObjectWrapper<>();

    public ReadOnlyObjectProperty<Sejour> onSejourClickedProperty() {
        return onSejourClicked.getReadOnlyProperty() ;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    private Button clear_btn;

    @FXML
    private DatePicker dateDebut_dp;

    @FXML
    private DatePicker dateFin_dp;

    @FXML
    private TextField titre_lieu_nbPersonne_tf;

    @FXML
    private ChoiceBox<User> hote_cb;

    @FXML
    private ChoiceBox<SejourStatut> statut_cb;
    

    private void loadCards(QueryResponse qr) throws IOException {
        if(qr.state == ResponseState.OK){
            int column = 1;
            int row = 1;
            int size = qr.response.size();
            sejours_container.getChildren().clear();
            for (int i = 1; i <= size; i++) {
                Sejour sejour = (Sejour) qr.response.get(i-1);
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(Main.class.getResource("sejour-card.fxml"));
                AnchorPane sejourCard = loader.load();
                SejourCard sejourCardController = loader.getController();
                sejourCardController.setSejour(sejour);

                sejourCardController.onSejourClickedProperty().addListener((obs, oldResult, newResult)->{
                    if (newResult != null){
                        onSejourClicked.set(newResult);
                    }
                });
                if (column == 4){
                    column = 1;
                    row++;
                }
                sejours_container.add(sejourCard, column++, row);
                GridPane.setMargin(sejourCard, new Insets(3));
            }
        }
    }


    private void loadUsingThread(){
        int nData = 1000;
        int nCols = 2;
        int nRows = Math.round((float)nData/nCols);
        DataService dataService = new DataService(nRows, nCols);
        dataService.setOnSucceeded( event ->{
            DataModel dataModel = dataService.getValue();
            for (int row = 0; row < nRows; row++) {
                for (int column = 0; column < nCols; column++) {
                    try {
                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(Main.class.getResource("sejour-card.fxml"));
                        AnchorPane sejourCard = loader.load();
                        SejourCard sejourCardController = loader.getController();

                        Sejour sejour = dataModel.getSejour(row, column);
                        sejourCardController.setSejour(sejour);
                        sejours_container.add(sejourCard, column, row);
                        GridPane.setMargin(sejourCard, new Insets(3));

                    }catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

            }

        });

        dataService.start();

    }

    @FXML
    public void onRechercherAfter2Char(){
        String multi_value = titre_lieu_nbPersonne_tf.getText();
        if(multi_value != null && multi_value.length()>=2){
            onRechercherClicked();
        }
    }
    @FXML
    public void onRechercherClicked(){
        Sejour sejour = new Sejour();
        String multi_value = titre_lieu_nbPersonne_tf.getText();
        sejour.lieu = multi_value;
        sejour.titre = multi_value;

        try{
            sejour.prix = Double.parseDouble(multi_value);
        }catch (Exception e){
            sejour.prix = 0;
        }

        try{
            sejour.nombrePersonnes = Integer.parseInt(multi_value);
        }catch (Exception e){
            sejour.nombrePersonnes = 0;
        }


        LocalDate d1 = dateDebut_dp.getValue();
        LocalDate d2 = dateFin_dp.getValue();

        if(d1 != null){
            sejour.dateDebut = Date.valueOf(d1);
        }

        if(d2!=null){
            sejour.dateFin = Date.valueOf(d2);
        }

        User hote = hote_cb.getValue();
        if(hote!=null){
            sejour.hote = hote;
            sejour.idHote = hote.id;
        }

        SejourStatut statut = statut_cb.getValue();
        if(statut!=null){
            sejour.statut = statut.getValue();
        }

        try {
            QueryResponse qr = SejourModel.getSejourByMultiCriteria(sejour);
            loadCards(qr);
        } catch (IOException e) {
            System.out.println("ERREUR: "+e.getMessage());
        }
    }

    @FXML
    public void onClearBtnClicked(){
        titre_lieu_nbPersonne_tf.setText("");
        dateFin_dp.setValue(null);
        dateDebut_dp.setValue(null);
        hote_cb.setValue(null);

        if(isUserConnected() && connectedUser.isHote()){
            hote_cb.setValue(connectedUser);
        }
        statut_cb.setValue(null);
        onRechercherClicked();
    }

    public boolean isUserConnected(){
        return connectedUser!=null;
    }

    private User connectedUser = null;
    public void setConnectedUser(User user){
        connectedUser = user;
        hote_cb.setVisible(true);
        initHoteCB();
        if(isUserConnected() && user.isHote()){
            hote_cb.setVisible(false);
            hote_cb.setValue(connectedUser);
        }
        initStatutCB();
        onRechercherClicked();
    }

    public void initHoteCB(){
        try{
            ObservableList<User> listItems = FXCollections.observableArrayList();
            QueryResponse queryGetHotes = UserModel.getAllHoteUsers();
            if(queryGetHotes.state == ResponseState.OK){
                List<User> values = new ArrayList<>();
                for (Object obj:queryGetHotes.response ) {
                    User user = (User) obj;
                    values.add(user);
                }
                listItems.addAll(values);
                hote_cb.setItems(listItems);
                hote_cb.valueProperty().addListener((obsValue, oldValue, newValue)->{
                    onRechercherClicked();
                });
            }
        }catch (Exception e){
            System.out.println("ERREUR: "+e.getMessage());
        }


    }

    public void initStatutCB(){
        ObservableList<SejourStatut> listItems = FXCollections.observableArrayList();
        List<SejourStatut> values = new ArrayList<>();
        for (SejourStatut statut:SejourStatut.values() ) {
            if(statut!=SejourStatut.REFUSE){
                values.add(statut);
            }
        }
        listItems.addAll(values);
        statut_cb.setItems(listItems);
        statut_cb.valueProperty().addListener((obsValue, oldValue, newValue)->{
            onRechercherClicked();
        });
    }
}
