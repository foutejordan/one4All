package com.avignon.university.one4all.controllers;

import com.avignon.university.one4all.Main;
import com.avignon.university.one4all.models.Historique;
import com.avignon.university.one4all.models.MenuItemType;
import com.avignon.university.one4all.models.Panier;
import com.avignon.university.one4all.models.User;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

public class SejourCardAction implements Initializable {

    @FXML
    private Label dateDebut_lbl;

    @FXML
    private Label dateFin_lbl;

    @FXML
    private ImageView image_iv;

    @FXML
    private Label lieu_lbl;

    @FXML
    private Label nombrePersone_lbl;

    @FXML
    private Label title_lbl;

    @FXML
    private Label hote_lbl;

    @FXML
    private Label voyageur_lbl;

    private Panier panier;


    private final ReadOnlyObjectWrapper<Panier> onRemoveClicked = new ReadOnlyObjectWrapper<>();

    public ReadOnlyObjectProperty<Panier> onRemoveClickedProperty() {
        return onRemoveClicked.getReadOnlyProperty() ;
    }


    private final ReadOnlyObjectWrapper<Historique> onValidateClicked = new ReadOnlyObjectWrapper<>();

    public ReadOnlyObjectProperty<Historique> onValidateClickedProperty() {
        return onValidateClicked.getReadOnlyProperty() ;
    }

    private final ReadOnlyObjectWrapper<Historique> onRefuserClicked = new ReadOnlyObjectWrapper<>();

    public ReadOnlyObjectProperty<Historique> onRefuserClickedProperty() {
        return onRefuserClicked.getReadOnlyProperty() ;
    }
    private final ReadOnlyObjectWrapper<Panier> onPanierClicked = new ReadOnlyObjectWrapper<>();

    public ReadOnlyObjectProperty<Panier> onPanierClickedProperty() {
        return onPanierClicked.getReadOnlyProperty() ;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        toggleItems();
    }

    public void setData(Panier panier){
        if(panier != null){
            this.panier = panier;
            if(this.panier.sejour!=null){
                title_lbl.setText("Titre: "+this.panier.sejour.getTitre());
                lieu_lbl.setText("Lieu: "+this.panier.sejour.getLieu());
                nombrePersone_lbl.setText("Nombre de personnes: "+this.panier.sejour.getNombrePersonnes());
                if(this.panier.sejour.getImage()!=null && !this.panier.sejour.getImage().isEmpty()){
                    image_iv.setImage(new Image(String.valueOf(Main.class.getResource("images/"+this.panier.sejour.getImage()))));
                }
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                String dateDebut = formatter.format(this.panier.sejour.dateDebut);
                dateDebut_lbl.setText("Date de début: "+dateDebut);
                String dateFin = formatter.format(this.panier.sejour.dateFin);
                dateFin_lbl.setText("Date de fin: "+dateFin);
                hote_lbl.setText("Hôte: "+this.panier.sejour.hote.login);
                voyageur_lbl.setText("Voyageur: "+this.panier.user.login);
            }

        }
        toggleItems();
    }

    private Historique historique;
    public void setHistorique(Historique historique){
        this.historique = historique;
        toggleItems();
    }

    @FXML
    private Button remove_btn;
    @FXML
    private Button validate_btn;

    @FXML
    private Button refuse_btn;
    private MenuItemType menuItemType;

    private User connectedUser;

    public void setConnectedUser(User connectedUser) {
        this.connectedUser = connectedUser;
        toggleItems();
    }

    public boolean isUserConnected(){
        return connectedUser!=null;
    }

    public void setMenuItemType(MenuItemType menuItemType){
        this.menuItemType = menuItemType;
        toggleItems();

    }

    public void toggleItems(){
        remove_btn.setVisible(false);
        validate_btn.setVisible(false);
        refuse_btn.setVisible(false);
        if(menuItemType == MenuItemType.VALIDATION){
            remove_btn.setVisible(false);
            validate_btn.setVisible(isUserConnected() && connectedUser.isHote());
            refuse_btn.setVisible(isUserConnected() && connectedUser.isHote());
        }else if(menuItemType == MenuItemType.PANIER){
            remove_btn.setVisible(isUserConnected() && connectedUser.isVoyageur());
            validate_btn.setVisible(false);
            refuse_btn.setVisible(false);
        }


    }

    public Panier getPanier() {
        return panier;
    }

    @FXML
    public void remove(){
        onRemoveClicked.set(panier);
    }


    @FXML
    public void isPanierClicked(){
        onPanierClicked.set(panier);
    }

    @FXML
    public void isValidateClicked(){
        onValidateClicked.set(historique);
    }

    @FXML
    public void isRefuserClicked(){
        onRefuserClicked.set(historique);
    }
}
