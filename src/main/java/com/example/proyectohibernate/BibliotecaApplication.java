package com.example.proyectohibernate;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class BibliotecaApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(BibliotecaApplication.class.getResource("/com/example/proyectohibernate/libro-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Ventana registro libro");
        stage.setScene(scene);
        stage.show();
    }
}
