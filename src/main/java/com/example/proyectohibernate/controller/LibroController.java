package com.example.proyectohibernate.controller;

import com.example.proyectohibernate.dao.LibroDao;
import com.example.proyectohibernate.model.Libro;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class LibroController {

    @FXML
    private TableView<Libro> tblLibros;

    @FXML private TextField txtTitulo;
    @FXML private TextField txtAutor;
    @FXML private TextField txtAnioPublicacion;
    @FXML private TextField txtPrecio;

    @FXML private Button btnGuardar;
    @FXML private Button btnActualizar;
    @FXML private Button btnEliminar;
    @FXML private Button btnModificar;
    @FXML private Button btncargar;



    @FXML private TableColumn<Libro, Long> colId;
    @FXML private TableColumn<Libro, String> colTitulo;
    @FXML private TableColumn<Libro, String> colAutor;
    @FXML private TableColumn<Libro, Integer> ColAnio;
    @FXML private TableColumn<Libro, Double> colPrecio;

    private final LibroDao libroDAO = new LibroDao();

    // ID del registro seleccionado
    private Long idSeleccionado = null;

    @FXML
    public void initialize() {

        colId.setCellValueFactory(
                new PropertyValueFactory<>("id")
        );

        colTitulo.setCellValueFactory(
                new PropertyValueFactory<>("titulo")
        );

        colAutor.setCellValueFactory(
                new PropertyValueFactory<>("autor")
        );

        ColAnio.setCellValueFactory(
                new PropertyValueFactory<>("anioPublicacion")
        );

        colPrecio.setCellValueFactory(
                new PropertyValueFactory<>("precio")
        );

        tblLibros.getSelectionModel()
                .selectedItemProperty()
                .addListener(
                        (ObservableValue<? extends Libro> observable,
                         Libro oldValue, Libro newValue) -> {

                            if (newValue != null) {
                                seleccionarLibro(newValue);
                            } else {
                                idSeleccionado = null;
                                actualizarBotones();
                            }
                        }
                );

        actualizarBotones();
        cargarLibros();
    }

    private void cargarLibros() {
        tblLibros.getItems().setAll(libroDAO.listar());
    }

    private void seleccionarLibro(Libro libro) {
        idSeleccionado = libro.getId();
        txtTitulo.setText(libro.getTitulo());
        txtAutor.setText(libro.getAutor());
        txtAnioPublicacion.setText(
                String.valueOf(libro.getAnioPublicacion())
        );
        txtPrecio.setText(
                String.valueOf(libro.getPrecio())
        );
        actualizarBotones();
    }

    private void actualizarBotones() {
        boolean sinSeleccion = idSeleccionado == null;
        btnModificar.setDisable(sinSeleccion);
        btnEliminar.setDisable(sinSeleccion);
    }

    private Libro obtenerLibroFormulario() {
        String titulo = txtTitulo.getText();
        String autor = txtAutor.getText();
        int anioPublicacion = Integer.valueOf(txtAnioPublicacion.getText());
        BigDecimal precio = new BigDecimal(txtPrecio.getText());
        return new Libro(0, titulo, autor, anioPublicacion, precio);
    }

    @FXML
    private void guardar() {
        try {
            // Validar campos vacíos
            if (txtTitulo.getText() == null || txtTitulo.getText().trim().isEmpty()) {
                mostrarAlerta(Alert.AlertType.ERROR, "Campo requerido", "El título es obligatorio");
                txtTitulo.requestFocus();
                return;
            }
            if (txtAutor.getText() == null || txtAutor.getText().trim().isEmpty()) {
                mostrarAlerta(Alert.AlertType.ERROR, "Campo requerido", "El autor es obligatorio");
                txtAutor.requestFocus();
                return;
            }
            if (txtAnioPublicacion.getText() == null || txtAnioPublicacion.getText().trim().isEmpty()) {
                mostrarAlerta(Alert.AlertType.ERROR, "Campo requerido", "El año de publicación es obligatorio");
                txtAnioPublicacion.requestFocus();
                return;
            }
            if (txtPrecio.getText() == null || txtPrecio.getText().trim().isEmpty()) {
                mostrarAlerta(Alert.AlertType.ERROR, "Campo requerido", "El precio es obligatorio");
                txtPrecio.requestFocus();
                return;
            }
            
            Libro libro = obtenerLibroFormulario();
            libroDAO.guardar(libro);
            cargarLibros();
            limpiarFormulario();
        } catch (NumberFormatException ex) {
            mostrarAlerta(Alert.AlertType.ERROR, "Formato inválido", "El año debe ser un número entero y el precio un número decimal");
        } catch (Exception ex) {
            ex.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo guardar el libro: " + ex.getMessage());
        }
    }

    @FXML
    private void modificar() {
        try {
            Libro libro = obtenerLibroFormulario();
            libro.setId(idSeleccionado);
            boolean modificado = libroDAO.modificar(libro);

            if(!modificado) {

            }
            limpiarFormulario();
            cargar();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void eliminar(){
        try{
            Alert confirmacion  = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmacion de eliminacion de datos.");
            confirmacion.setHeaderText("Eliminar libro");
            confirmacion.setContentText("¿Desea eliminar el libro seleccionado?");

            Optional<ButtonType> result = confirmacion.showAndWait();
            if(result.isEmpty() || result.get() != ButtonType.OK){
                return;
            }

            boolean eliminado = libroDAO.eliminar(idSeleccionado);
            if(!eliminado) {
                Alert alerta = new Alert(Alert.AlertType.ERROR);
                alerta.setTitle("Error al eliminar libro");
                alerta.setHeaderText("No se pudo eliminar el libro");
                alerta.setContentText("El libro no se pudo eliminar. Por favor, intente nuevamente.");
                alerta.showAndWait();
                return;
            }
            limpiarFormulario();
            cargar();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML private void cargar(){
        try{
            List<Libro > libros = libroDAO.listar();
            limpiarFormulario();
            tblLibros.setItems(FXCollections.observableArrayList(libros));

        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    @FXML
    private void limpiarFormulario(){
        tblLibros.getSelectionModel().clearSelection();

        idSeleccionado = null;
        txtTitulo.clear();
        txtAutor.clear();
        txtAnioPublicacion.clear();
        txtPrecio.clear();

        actualizarBotones();

    }
    
    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
