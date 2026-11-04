module com.example.proyectohibernate {
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.persistence;


    opens com.example.proyectohibernate to javafx.fxml;
    exports com.example.proyectohibernate;
}