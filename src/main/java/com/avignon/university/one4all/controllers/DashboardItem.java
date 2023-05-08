package com.avignon.university.one4all.controllers;

import com.avignon.university.one4all.models.*;
import com.avignon.university.one4all.models.dao.HistoriqueModel;
import com.avignon.university.one4all.models.dao.SejourModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class DashboardItem implements Initializable {
    @FXML
    private Label enCours_lbl;

    @FXML
    private Label montant_lbl;

    @FXML
    private Label nbDisponible_lbl;

    @FXML
    private Label nbReservation_lbl;

    @FXML
    private Label nbSejours_lbl;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    User connectedUser;

    public void setConnectedUser(User user){
        connectedUser = user;
        initValues();
    }

    CountType reservation = new CountType();
    private void initReservationValues(){
        reservation.type = SejourStatut.RESERVE.getValue();
        try {
            QueryResponse queryCountStatut = HistoriqueModel.getCountByStatut(connectedUser);
            if(queryCountStatut.state == ResponseState.OK){
                if(queryCountStatut.response!=null && !queryCountStatut.response.isEmpty()){
                    for (Object obj:queryCountStatut.response) {
                        CountType c = (CountType) obj;
                        if(c.type == reservation.type){
                            reservation = c;
                        } else if (c.type == SejourStatut.EN_COURS_VALIDATION.getValue()) {
                            enCoursValidation = c;
                        }

                    }
                }
            }
        }catch (Exception e){
            System.out.println("ERREUR: "+e.getMessage());
        }
    }

    MontantType montant = new MontantType();
    private void initMontantValues(){
        try {
            QueryResponse queryMontant = HistoriqueModel.getTotalAmount(connectedUser);
            if(queryMontant.state == ResponseState.OK){
                if(queryMontant.response!=null && !queryMontant.response.isEmpty()){
                    montant = (MontantType) queryMontant.response.get(0);
                }
            }
        }catch (Exception e){
            System.out.println("ERREUR: "+e.getMessage());
        }
    }

    CountType sejours = new CountType();
    CountType disponibles = new CountType();
    CountType enCoursValidation = new CountType();

    private void initSejourValues(){
        enCoursValidation.type = SejourStatut.EN_COURS_VALIDATION.getValue();

        try {
            QueryResponse queryCountStatut = SejourModel.getCountByStatut(connectedUser);
            if(queryCountStatut.state == ResponseState.OK){
                if(queryCountStatut.response!=null && !queryCountStatut.response.isEmpty()){
                    for (Object obj:queryCountStatut.response) {
                        CountType c = (CountType) obj;
                        if(c.type !=SejourStatut.RESERVE.getValue()){
                            disponibles.count+=c.count;
                        }
                        sejours.count+=c.count;
                    }
                }
            }
        }catch (Exception e){
            System.out.println("ERREUR: "+e.getMessage());
        }
    }

    public void initValues(){
        initReservationValues();
        initMontantValues();
        initSejourValues();
        nbSejours_lbl.setText(sejours.count+"");
        montant_lbl.setText(montant.total+" €");
        nbDisponible_lbl.setText(disponibles.count+"");
        nbReservation_lbl.setText(reservation.count+"");
        enCours_lbl.setText(enCoursValidation.count+"");
    }
}
