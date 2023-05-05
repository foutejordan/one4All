package com.avignon.university.one4all.controllers;

import com.avignon.university.one4all.Main;
import com.avignon.university.one4all.models.QueryResponse;
import com.avignon.university.one4all.models.ResponseState;
import com.avignon.university.one4all.models.Sejour;
import com.avignon.university.one4all.models.dao.SejourModel;
import com.avignon.university.one4all.multithreading.DataModel;
import com.avignon.university.one4all.multithreading.DataService;
import com.github.javafaker.Faker;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
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
        try {
            initSejoursContainer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    private Button rechercher_btn;

    @FXML
    private DatePicker dateDebut_dp;

    @FXML
    private DatePicker dateFin_dp;

    @FXML
    private TextField titre_lieu_nbPersonne_tf;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private FlowPane sejoursContainer;

    // Nombre de séjours à afficher par page
    private static final int PAGE_SIZE = 60;

    // Numéro de la page actuellement affichée
    private int currentPage = 0;

    // Liste complète des séjours
    private ArrayList<Sejour> allSejours;

    // Liste des cartes de séjour actuellement affichées
    private List<Sejour> displayedSejourCards = new ArrayList<>();
    private static final int SCROLL_THRESHOLD = 200;

    public void initSejoursContainer() throws IOException {
        //testJavaFaker();
        //loadUsingThread();

        allSejours = SejourModel.getAllSejours();
        int numPages = (int) Math.ceil(allSejours.size() / (double) PAGE_SIZE);
        System.out.println(numPages);
        try {
            // Charger les cartes de séjour pour la première page
            loadSejourCards(currentPage);
        } catch (IOException e) {
            System.out.println("ERREUR: " + e.getMessage());
        }

        // Ajouter un écouteur pour le défilement de la grille de séjours
        sejours_container.setOnScroll(event -> {
            double deltaY = event.getDeltaY();
            double vValue = scrollPane.getVvalue();


            // Vérifier si l'utilisateur a défilé vers le bas
            if (deltaY > 0) {
                // Vérifier si nous sommes à la fin de la grille de séjours
                if (vValue == 1) {
                    System.out.println("Nous sommes en bas");
                    // Charger la page suivante si elle existe
                    if (currentPage < numPages - 1) {
                        currentPage++;
                        try {
                            loadSejourCards(currentPage);
                        } catch (IOException e) {
                            System.out.println("ERREUR: " + e.getMessage());
                        }
                    }
                }
            }else if (deltaY < 0) {
                // Vérifier si nous sommes au début de la grille de séjours
                if (vValue == 0){
                    // Charger les cartes de séjour pour la page précédente (si elle existe)
                    if (currentPage > 0) {
                        currentPage--;
                        try {
                            loadSejourCards(currentPage);

                            //scrollPane.setVvalue(0);
                        } catch (IOException e) {
                            System.out.println("ERREUR: " + e.getMessage());
                        }
                    }
                }
            }
        });

    }

    private void loadSejourCards(int page) throws IOException {
        // Vérifier si la page demandée est valide
        int numPages = (int) Math.ceil(allSejours.size() / (double) PAGE_SIZE);
        if (page < 0 || page >= numPages) {
            return;
        }

        // Déterminons l'indice de départ et le nombre d'éléments à afficher pour la page demandée
        int startIndex = page * PAGE_SIZE;
        int endIndex = Math.min(startIndex + PAGE_SIZE, allSejours.size());

        // Chargeons les cartes de séjour pour la page demandée
        displayedSejourCards = allSejours.subList(startIndex, endIndex);

        //displayedSejourCards.addAll(pageSejours);

        // Effacer le contenu précédent de la grille de séjours
        sejours_container.getChildren().clear();

        System.out.println(displayedSejourCards.size());
        int column = 0;
        int row = 0;
        for (Sejour sejour : displayedSejourCards) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("sejour-card.fxml"));
            AnchorPane sejourCard = loader.load();
            SejourCard sejourCardController = loader.getController();

            sejourCardController.onSejourClickedProperty().addListener((obs, oldResult, newResult) -> {
                if (newResult != null) {
                    onSejourClicked.set(newResult);
                }
            });
            sejourCardController.setSejour(sejour);

            sejours_container.add(sejourCard, column, row);
            GridPane.setMargin(sejourCard, new Insets(3));

            // Passer à la colonne suivante ou à la première colonne de la ligne suivante
            column++;
            if (column == 4) {
                column = 0;
                row++;
            }
        }
    }
    /*private void loadCards(QueryResponse qr) throws IOException {
        if(qr.state == ResponseState.SUCCESS){
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

                sejourCardController.onSejourClickedProperty().addListener((obs, oldResult, newResult)->{
                    if (newResult != null){
                        onSejourClicked.set(newResult);
                    }
                });
                sejourCardController.setSejour(sejour);
                if (column == 4){
                    column = 0;
                    row++;
                }
                sejours_container.add(sejourCard, column++, row);
                GridPane.setMargin(sejourCard, new Insets(3));
            }
        }
    }*/
    /*protected void testJavaFaker() {
        try {
            QueryResponse qr = SejourModel.getAllSejours();
            loadCards(qr);
        }catch (IOException e) {
            System.out.println("ERREUR: "+e.getMessage());
        }

    }*/


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
    public void onRechercherClicked() throws SQLException, IOException {
        String multi_value = titre_lieu_nbPersonne_tf.getText();
        System.out.println(multi_value);
        Date dateDebut = null;
        Date dateFin = null;
        LocalDate d1 = dateDebut_dp.getValue();
        LocalDate d2 = dateFin_dp.getValue();

        if(d1 != null){
            dateDebut = Date.valueOf(d1);
        }

        if(d2!=null){
            dateFin = Date.valueOf(d2);
        }

        allSejours = SejourModel.getSejourByMultiCriteria(multi_value,dateDebut,dateFin);
        System.out.println(allSejours.size());
        loadSejourCards(0);
        //initSejoursContainer();
        //loadCards(qr);
    }

    public void handleTextFieldChanged(KeyEvent event) throws IOException, SQLException {
        String newValue = titre_lieu_nbPersonne_tf.getText();

        allSejours = SejourModel.search(newValue);
        loadSejourCards(0);
        //initSejoursContainer();
    }

    public void handleDatePickerChanged(ActionEvent event) {
        LocalDate newValue = dateDebut_dp.getValue();
        LocalDate newValue2 = dateFin_dp.getValue();
        // Effectuez votre traitement ici
    }

}
