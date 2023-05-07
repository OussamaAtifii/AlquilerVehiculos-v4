package org.iesalandalus.programacion.alquilervehiculos.vista.grafica.controllers;

import java.time.LocalDate;

import java.time.format.DateTimeFormatter;

import org.iesalandalus.programacion.alquilervehiculos.controlador.IControlador;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Alquiler;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Cliente;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Vehiculo;
import org.iesalandalus.programacion.alquilervehiculos.vista.grafica.utilities.Dialogos;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ControladorAñadirAlquiler {

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @FXML
    private TextField tfFormCliente;

    @FXML
    private TextField tfFormFechaAlqui;

    @FXML
    private TextField tfFormFechaDevo;

    @FXML
    private TextField tfFormVehiculo;

    @FXML
    private Button btnFormAlquilerAñadir;

    @FXML
    private Button btnFormAlquilerCancelar;

    @FXML
    private Button btnFormAlquilerDevolver;

    @FXML
    private Label lblFechaDevo;

    private IControlador controladorMVC;
    ObservableList<Alquiler> alquileres;
    ObservableList<Alquiler> obsFiltroAlquileres;
    private static String busquedaDni;
    private static String busquedaMatricula;

    @FXML
    private void initialize() {
    }

    public void setFiltro(ObservableList<Alquiler> filtro) {
        this.obsFiltroAlquileres = filtro;
    }

    public void setControladorMVC(IControlador controlador) {
        controladorMVC = controlador;
    }

    public void setAlquileres(ObservableList<Alquiler> alquileres) {
        this.alquileres = alquileres;
    }

    public static void setBusquedaDni(String filtroDni) {
        busquedaDni = filtroDni;
    }

    public static void setBusquedaMatricula(String filtroMatricula) {
        busquedaMatricula = filtroMatricula;
    }

    public void cargarDatos(Alquiler alquiler) {
        tfFormCliente.setDisable(true);
        tfFormCliente.setText(alquiler.getCliente().getDni());
        tfFormVehiculo.setDisable(true);
        tfFormVehiculo.setText(alquiler.getVehiculo().getMatricula());
        tfFormFechaAlqui.setDisable(true);
        tfFormFechaAlqui.setText(alquiler.getFechaAlquiler().format(FORMATO_FECHA));
    }

    public void botonVisible() {
        btnFormAlquilerAñadir.setVisible(true);
        btnFormAlquilerDevolver.setVisible(false);
        lblFechaDevo.setVisible(false);
        tfFormFechaDevo.setVisible(false);
    }

    @FXML
    void cancelar(ActionEvent event) {
        Stage escenario = (Stage) ((Node) event.getSource()).getScene().getWindow();
        if (Dialogos.mostrarDialogoConfirmacion("Cancelar", "¿Seguro quieres cancelar la operación?", escenario)) {
            escenario.close();
        } else {
            event.consume();
        }
    }

    @FXML
    void devolver(ActionEvent event) {
        try {
            Alquiler alquiler = new Alquiler(controladorMVC.buscar(Cliente.getClienteConDni(tfFormCliente.getText())),
                    Vehiculo.getVehiculoConMatricula(tfFormVehiculo.getText()),
                    LocalDate.parse(tfFormFechaAlqui.getText(), FORMATO_FECHA));

            btnFormAlquilerAñadir.setVisible(false);
            btnFormAlquilerDevolver.setVisible(true);
            lblFechaDevo.setVisible(true);
            tfFormFechaDevo.setVisible(true);

            alquileres.remove(alquiler);

            controladorMVC.devolver(alquiler.getVehiculo(), LocalDate.parse(tfFormFechaDevo.getText(),
                    FORMATO_FECHA));

            alquileres.setAll(controladorMVC.getAlquileres());

            Dialogos.mostrarDialogoConfirmacion("Devolver alquiler", "Alquiler devuelto correctamente");
            Stage escenario = (Stage) ((Node) event.getSource()).getScene().getWindow();
            escenario.close();
        } catch (Exception e) {
            Dialogos.mostrarDialogoError("Devolver Alquiler", e.getMessage());
        }
    }

    @FXML
    void anadir(ActionEvent event) {
        try {
            Cliente cliente = controladorMVC.buscar(Cliente.getClienteConDni(tfFormCliente.getText()));

            Vehiculo vehiculo = controladorMVC.buscar(Vehiculo.getVehiculoConMatricula(tfFormVehiculo.getText()));

            LocalDate fechaAlquiler = LocalDate.parse(tfFormFechaAlqui.getText(), FORMATO_FECHA);

            Alquiler alquiler = new Alquiler(cliente, vehiculo, fechaAlquiler);

            controladorMVC.insertar(alquiler);
            alquileres.add(alquiler);

            if (busquedaDni != null && cliente.getDni().toLowerCase().contains(busquedaDni.toLowerCase())) {
                obsFiltroAlquileres.add(alquiler);
            }
            if (busquedaMatricula != null
                    && vehiculo.getMatricula().toLowerCase().contains(busquedaMatricula.toLowerCase())) {
                obsFiltroAlquileres.add(alquiler);
            }

            Stage escenario = (Stage) ((Node) event.getSource()).getScene().getWindow();
            escenario.close();

        } catch (Exception e) {
            Dialogos.mostrarDialogoError("Inserccion Alquiler", e.getMessage());
        }
    }
}