package com.avignon.university.one4all.multithreading;

import com.avignon.university.one4all.models.Sejour;
import com.github.javafaker.Faker;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DataModel {

    public Sejour getSejour(int row, int col){
        return sejours[row][col];
    }

    private final int nRows;
    private final int nCols;
    private final Sejour[][] sejours;

    public DataModel(int nRows, int nCols) {
        this.nRows = nRows;
        this.nCols = nCols;
        sejours = new Sejour[nRows][nCols];
    }

    private void loadSejours(){
        Faker faker = new Faker();
        for (int row = 0; row < nRows; row++) {
            for (int col = 0; col < nCols; col++) {
                Sejour sejour = new Sejour();
                Date dateDebut =  new Date(faker.date().future(12, TimeUnit.DAYS).getTime());
                Date dateFin = new Date(faker.date().future(24, TimeUnit.DAYS).getTime());
                String lieu = faker.address().city();
                String titre = faker.funnyName().name();
                int nombrePersonne = faker.number().randomDigitNotZero();
                int idxImage  = faker.number().numberBetween(1, 5);

                sejour.setLieu(lieu);
                sejour.setTitre(titre);
                sejour.setDateDebut(dateDebut);
                sejour.setDateFin(dateFin);
                sejour.setNombrePersonnes(nombrePersonne);
                sejour.setImage("house"+idxImage+".png");

                sejours[row][col] = sejour;
            }

        }
    }
}
