package com.avignon.university.one4all.controllers;

import com.avignon.university.one4all.Main;
import com.avignon.university.one4all.models.*;
import com.avignon.university.one4all.models.dao.PanierModel;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PanierItem implements Initializable {

    @FXML
    private GridPane sejours_container;

    private final ReadOnlyObjectWrapper<Panier> onRemoveClicked = new ReadOnlyObjectWrapper<>();

    public ReadOnlyObjectProperty<Panier> onRemoveClickedProperty() {
        return onRemoveClicked.getReadOnlyProperty() ;
    }

    private final ReadOnlyObjectWrapper<Panier> onPanierClicked = new ReadOnlyObjectWrapper<>();

    public ReadOnlyObjectProperty<Panier> onPanierClickedProperty() {
        return onPanierClicked.getReadOnlyProperty() ;
    }
    private final ReadOnlyObjectWrapper<Boolean> onValidateClicked = new ReadOnlyObjectWrapper<>();

    public ReadOnlyObjectProperty<Boolean> onValidateClickedProperty() {
        return onValidateClicked.getReadOnlyProperty() ;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
    private User connectedUser = null;
    public void setConnectedUser(User user){
        connectedUser = user;
        loadAllData();
    }

    public boolean isUserConnected(){
        return connectedUser != null;
    }
    @FXML
    private Button validate_btn;
    List<Panier> paniers = new ArrayList<>();
    protected void loadAllData() {
        paniers = new ArrayList<>();
        if(isUserConnected()){
            try {
                QueryResponse panierQuery = PanierModel.getAllPanierByUserId(connectedUser.id);
                if(panierQuery.state == ResponseState.OK){
                    int column = 1;
                    int row = 1;
                    List<Object> values = panierQuery.response;
                    int size = values.size();
                    sejours_container.getChildren().clear();
                    for (int i = 1; i <= size; i++) {

                        Panier panier = (Panier) values.get(i-1);
                        paniers.add(panier);
                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(Main.class.getResource("sejour-card-actions-panier.fxml"));
                        AnchorPane sejourCard = loader.load();
                        SejourCardActionPanier sejourCardController = loader.getController();
                        sejourCardController.setData(panier);
                        sejourCardController.onPanierClickedProperty().addListener((obs, oldResult, newResult)->{
                            if (newResult != null){
                                onPanierClicked.set(newResult);
                            }
                        });
                        sejourCardController.onRemoveClickedProperty().addListener((obs, oldResult, newResult)->{
                            if (newResult != null){
                                deletePanier(newResult);
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

            }catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        validate_btn.setVisible(!paniers.isEmpty());
    }

    private void deletePanier(Panier panier){
        try{
            QueryResponse queryCreatePanier = PanierModel.deleteSejourFromPanier(panier);
            if(queryCreatePanier.state == ResponseState.OK){
                loadAllData();
            }
        }catch (Exception e){
            System.out.println("ERREUR: "+e.getMessage());
        }
    }

    @FXML
    public void validatePanier(){
        if(!paniers.isEmpty()){
            try{
                QueryResponse queryValidate = PanierModel.validerPanier(paniers);
                if(queryValidate.state == ResponseState.OK){
                    onValidateClicked.set(true);
                }
            }catch (Exception e){
                Logger.getAnonymousLogger().log(
                        Level.SEVERE,
                        LocalDateTime.now() + ":" +e.getMessage());
            }
        }


    }
}
