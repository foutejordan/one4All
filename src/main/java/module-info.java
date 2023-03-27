module com.avignon.university.one4all {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafaker;
    requires java.sql;


    opens com.avignon.university.one4all to javafx.fxml;
    exports com.avignon.university.one4all;
    exports com.avignon.university.one4all.controllers;
    exports com.avignon.university.one4all.models;
    opens com.avignon.university.one4all.controllers to javafx.fxml;
}