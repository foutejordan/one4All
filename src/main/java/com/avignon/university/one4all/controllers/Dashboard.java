package com.avignon.university.one4all.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import com.github.javafaker.Faker;

import java.util.concurrent.TimeUnit;


public class Dashboard {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        testJavaFaker();
        welcomeText.setText("Welcome to JavaFX Application!");
    }

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
}