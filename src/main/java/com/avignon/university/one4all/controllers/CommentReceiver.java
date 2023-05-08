package com.avignon.university.one4all.controllers;

import com.avignon.university.one4all.Main;
import com.avignon.university.one4all.models.Comment;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

public class CommentReceiver implements Initializable {

    @FXML
    private Label author_lbl;

    @FXML
    private ImageView avatar_iv;

    @FXML
    private TextArea comment_txtArea;

    @FXML
    private Label date_lbl;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void setData(Comment comment){
        comment_txtArea.setText(comment.message);
        if(comment.user != null){
            author_lbl.setText("Auteur: "+comment.user.login);
            avatar_iv.setImage(new Image(String.valueOf(Main.class.getResource("images/"+comment.user.image))));
        }
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String date = formatter.format(comment.date);
        date_lbl.setText("Date: "+date);
    }
}
