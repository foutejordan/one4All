package com.avignon.university.one4all.controllers;

import com.avignon.university.one4all.Main;
import com.avignon.university.one4all.models.*;
import com.avignon.university.one4all.models.dao.CommentModel;
import com.avignon.university.one4all.models.dao.PanierModel;
import com.avignon.university.one4all.models.dao.SejourModel;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ResourceBundle;

public class SejourDetails implements Initializable {

    @FXML
    private Label dateDebut_dp;

    @FXML
    private Label dateFin_dp;

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

    public Sejour getSejour() {
        return sejour;
    }

    private Sejour sejour;
    private Panier panier = new Panier();

    private final ReadOnlyObjectWrapper<Boolean> isGoToListClicked = new ReadOnlyObjectWrapper<>();

    public ReadOnlyObjectProperty<Boolean> isGoToListClickedProperty() {
        return isGoToListClicked.getReadOnlyProperty() ;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    private boolean isAlreadyInConnectedUserPanier(){
        Panier panier = new Panier();
        panier.user = connectedUser;
        panier.idUser = connectedUser.id;
        panier.sejour = sejour;
        if(sejour!=null){
            panier.idSejour = sejour.id;
        }
        QueryResponse queryGetPanier = PanierModel.getPanierBySejourAndUser(panier);
        if(queryGetPanier.state == ResponseState.OK){
            if(queryGetPanier.response!=null){
                return !queryGetPanier.response.isEmpty();
            }
        }
        return false;
    }
    private void setPanier(){
        panier.sejour = sejour;
        if(sejour!=null){
            panier.idSejour = sejour.id;
        }
        panier.user = connectedUser;
        if(connectedUser!=null){
            panier.idUser = connectedUser.id;
        }
    }
    public void setSejour(Sejour sejour){
        this.sejour = sejour;
        setPanier();
        if(!isUserConnected()){
            addToCart_btn.setVisible(false);
            removeFromCart_btn.setVisible(false);
        }else{
            if(connectedUser.isHote()){
                addToCart_btn.setVisible(false);
                removeFromCart_btn.setVisible(false);
            }else{
                boolean isInPanier = isAlreadyInConnectedUserPanier();
                if(isInPanier){
                    addToCart_btn.setVisible(false);
                    removeFromCart_btn.setVisible(true);
                }else{
                    if(sejour.isReserve() || sejour.isEnCoursValidation()){
                        addToCart_btn.setVisible(false);
                        removeFromCart_btn.setVisible(false);
                    }else{
                        addToCart_btn.setVisible(true);
                        removeFromCart_btn.setVisible(false);
                    }
                }
            }
        }
        if(sejour != null){
            title_lbl.setText("Titre: "+sejour.getTitre());
            lieu_lbl.setText("Lieu: "+sejour.getLieu());
            nombrePersone_lbl.setText("Nombre de personnes: "+sejour.getNombrePersonnes());
            if(sejour.getImage()!=null && !sejour.getImage().isEmpty()){
                image_iv.setImage(new Image(String.valueOf(Main.class.getResource("images/"+sejour.getImage()))));
            }
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            String dateDebut = formatter.format(sejour.dateDebut);
            dateDebut_dp.setText("Date de début: "+dateDebut);
            String dateFin = formatter.format(sejour.dateFin);
            dateFin_dp.setText("Date de fin: "+dateFin);
            hote_lbl.setText("Hôte: "+sejour.hote.login);
            initCommentVbox(sejour.comments);
        }
    }

    private User connectedUser = null;

    public void setConnectedUser(User user){
        connectedUser = user;
        addComment_pane.setVisible(isUserConnected());
        addToCart_btn.setVisible(isUserConnected() && connectedUser.isVoyageur());
    }

    public void goToList(){
        this.isGoToListClicked.set(true);
    }

    @FXML
    private VBox commentaires_vbox;

    @FXML
    private Pane addComment_pane;

    @FXML
    private Button addToCart_btn;

    @FXML
    private Button removeFromCart_btn;

    public void initCommentVbox(List<Comment> comments){
        commentaires_vbox.getChildren().clear();
        try{
            for (Comment comment:comments) {
                if(isSender(comment)){
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(Main.class.getResource("comment-sender.fxml"));
                    AnchorPane commentCard = loader.load();
                    CommentSender commentCardController = loader.getController();
                    commentCardController.setData(comment);
                    commentaires_vbox.getChildren().add(commentCard);
                }else{
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(Main.class.getResource("comment-receiver.fxml"));
                    AnchorPane commentCard = loader.load();
                    CommentReceiver commentCardController = loader.getController();
                    commentCardController.setData(comment);
                    commentaires_vbox.getChildren().add(commentCard);
                }
            }
        }catch (Exception e){
            System.out.println("ERREUR: "+e.getMessage());
        }

    }

    public boolean isUserConnected(){
        return connectedUser != null;
    }

    public boolean isSender(Comment comment){
        if(comment == null || connectedUser == null){
            return false;
        }
        return comment.idUser == connectedUser.id;
    }

    @FXML
    public void onAddToCartBtnClicked(){
        try{
            QueryResponse queryCreatePanier = PanierModel.createPanier(panier);
            if(queryCreatePanier.state == ResponseState.OK){
                if(queryCreatePanier.response!=null && !queryCreatePanier.response.isEmpty()){
                    panier = (Panier) queryCreatePanier.response.get(0);
                    sejour = panier.sejour;
                    setSejour(sejour);
                }
            }
        }catch (Exception e){
            System.out.println("ERREUR: "+e.getMessage());
        }
    }

    @FXML
    public void onRemoveFromCartBtnClicked(){
        try{
            QueryResponse queryCreatePanier = PanierModel.deleteSejourFromPanier(panier);
            if(queryCreatePanier.state == ResponseState.OK){
                if(queryCreatePanier.response!=null && !queryCreatePanier.response.isEmpty()){
                    panier = (Panier) queryCreatePanier.response.get(0);
                    sejour = panier.sejour;
                    setSejour(sejour);
                }
            }
        }catch (Exception e){
            System.out.println("ERREUR: "+e.getMessage());
        }
    }

    @FXML
    public void onAddCommentClicked(){
        Comment comment = getComment();
        if(isCommentCorrect(comment)){
            QueryResponse queryCreateComment = CommentModel.createComment(comment);
            if(queryCreateComment.state == ResponseState.OK){
                if(queryCreateComment.response!=null && !queryCreateComment.response.isEmpty()){
                    comment = (Comment) queryCreateComment.response.get(0);
                    sejour.comments.add(0, comment);
                    initCommentVbox(sejour.comments);
                    comment_txtArea.setText("");
                }
            }
        }
    }

    private boolean isCommentCorrect(Comment comment){
        return !comment.message.isEmpty();
    }
    @FXML
    private TextArea comment_txtArea;
    private Comment getComment(){
        Comment comment = new Comment();
        comment.message = comment_txtArea.getText();
        if(connectedUser!=null){
            comment.user = connectedUser;
            comment.idUser = connectedUser.id;
        }

        if(sejour!=null){
            comment.sejour = sejour;
            comment.idSejour = sejour.id;
        }
        return comment;
    }
}
