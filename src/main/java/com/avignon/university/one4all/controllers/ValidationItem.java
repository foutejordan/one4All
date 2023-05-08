package com.avignon.university.one4all.controllers;

import com.avignon.university.one4all.Main;
import com.avignon.university.one4all.models.*;
import com.avignon.university.one4all.models.dao.HistoriqueModel;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ValidationItem implements Initializable {

    @FXML
    private GridPane sejours_container;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    private User connectedUser;

    private final ReadOnlyObjectWrapper<Sejour> onSejourClicked = new ReadOnlyObjectWrapper<>();

    public ReadOnlyObjectProperty<Sejour> onSejourClickedProperty() {
        return onSejourClicked.getReadOnlyProperty() ;
    }


    public void setConnectedUser(User user){
        connectedUser = user;
        //loadData();
        onKeyReleased();
    }


    @FXML
    public void onRechercherAfter2Char(){
        String multi_value = xValues_txtfd.getText();
        if(multi_value != null && multi_value.length()>=2){
            onKeyReleased();;
        }
    }

    public void initHistoriqueContainer(List<Object> objects) throws IOException {
        sejours_container.getChildren().clear();
        int column = 1;
        int row = 1;
        int size = objects.size();
        for (int i = 1; i <= size; i++) {
            Historique historique = (Historique) objects.get(i-1);
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("sejour-card-actions-validation.fxml"));
            AnchorPane sejourCard = loader.load();
            SejourCardActionValidation sejourCardController = loader.getController();
            sejourCardController.onPanierClickedProperty().addListener((obs, oldResult, newResult)->{
                if (newResult != null){
                    onSejourClicked.set(newResult.sejour);
                }
            });
            sejourCardController.onValidateClickedProperty().addListener((obs, oldResult, newResult)->{
                if (newResult != null){
                    validatePanier(newResult);
                }
            });

            sejourCardController.onRefuserClickedProperty().addListener((obs, oldResult, newResult)->{
                if (newResult != null){
                    refuserPanier(newResult);
                }
            });
            sejourCardController.setData(historique.toPanier());
            sejourCardController.setConnectedUser(connectedUser);
            sejourCardController.setHistorique(historique);
            if (column == 4){
                column = 1;
                row++;
            }

            sejours_container.add(sejourCard, column++, row);

            GridPane.setMargin(sejourCard, new Insets(3));

        }
    }

    public void validatePanier(Historique historique){
        try{
            historique.statut = SejourStatut.RESERVE.getValue();
            QueryResponse queryUpdate = HistoriqueModel.updateHistorique(historique);
            if(queryUpdate.state == ResponseState.OK){
                onKeyReleased();
            }
        }catch (Exception e){
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + " : "+e.getMessage());
        }
    }

    public void refuserPanier(Historique historique){
        try{
            historique.statut = SejourStatut.REFUSE.getValue();
            QueryResponse queryUpdate = HistoriqueModel.updateHistorique(historique);
            if(queryUpdate.state == ResponseState.OK){
                onKeyReleased();
            }
        }catch (Exception e){
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + " : "+e.getMessage());
        }
    }


    @FXML
    public void onKeyReleased(){
        Historique historique = getSearchValues();
        try{
            QueryResponse queryMultiCriteria = HistoriqueModel.getHistoriqueByMultiCriteria(historique);
            if(queryMultiCriteria.state==ResponseState.OK){
                initHistoriqueContainer(queryMultiCriteria.response);
            }
        }catch (Exception e){
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + " : "+e.getMessage());
        }

    }

    @FXML
    private DatePicker dateDebut_dp;

    @FXML
    private DatePicker dateFin_dp;

    @FXML
    private TextField xValues_txtfd;



    private Historique getSearchValues(){
        Historique historique = new Historique();
        String xValue = xValues_txtfd.getText();
        historique.lieu = xValue;
        historique.titre = xValue;

        try {
            historique.prix = Double.parseDouble(xValue);
        }catch (Exception e){
            historique.prix = 0;
        }

        try {
            historique.nombrePersonnes = Integer.parseInt(xValue);
        }catch (Exception e){
            historique.nombrePersonnes = 0;
        }

        LocalDate dateDebut = dateDebut_dp.getValue();
        if(dateDebut!=null){
            historique.dateDebut = Date.valueOf(dateDebut);
        }

        LocalDate dateFin = dateFin_dp.getValue();
        if(dateFin!=null){
            historique.dateFin = Date.valueOf(dateFin);
        }

        historique.user = connectedUser;
        historique.idUser = connectedUser.id;

        historique.statut = SejourStatut.EN_COURS_VALIDATION.getValue();
        return historique;
    }

    @FXML
    public void onClearFields(){
        xValues_txtfd.setText("");
        dateFin_dp.setValue(null);
        dateDebut_dp.setValue(null);
        onKeyReleased();
    }
}
