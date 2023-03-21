module com.avignon.university.one4all {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.avignon.university.one4all to javafx.fxml;
    exports com.avignon.university.one4all;
}