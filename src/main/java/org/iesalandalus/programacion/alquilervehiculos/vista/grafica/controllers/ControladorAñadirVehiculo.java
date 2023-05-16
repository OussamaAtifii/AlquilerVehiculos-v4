package org.iesalandalus.programacion.alquilervehiculos.vista.grafica.controllers;

import org.iesalandalus.programacion.alquilervehiculos.controlador.IControlador;

import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Autobus;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Furgoneta;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Turismo;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Vehiculo;
import org.iesalandalus.programacion.alquilervehiculos.vista.grafica.utilities.Dialogos;
import org.iesalandalus.programacion.alquilervehiculos.vista.texto.TipoVehiculo;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ControladorAñadirVehiculo {

    private IControlador controladorMVC;
    private ObservableList<Vehiculo> vehiculos;
    private ObservableList<Vehiculo> obsFiltroVehiculos;
    private String tipoVehiculo;
    private static String busqueda;

    @FXML
    private MenuButton mbTipoVehiculo;

    @FXML
    private Button btnAnadir;

    @FXML
    private Button btnCancelar;

    @FXML
    private Text txTitulo;

    @FXML
    private Button btnModificar;

    @FXML
    private MenuItem miAutobus;

    @FXML
    private MenuItem miFurgoneta;

    @FXML
    private MenuItem miTurismo;

    @FXML
    private TextField tfCilindrada;

    @FXML
    private TextField tfMarca;

    @FXML
    private TextField tfMatricula;

    @FXML
    private TextField tfModelo;

    @FXML
    private TextField tfPlazas;

    @FXML
    private TextField tfPma;

    @FXML
    private Label lblCilindrada;

    @FXML
    private Label lblMarca;

    @FXML
    private Label lblMatricula;

    @FXML
    private Label lblModelo;

    @FXML
    private Label lblPlazas;

    @FXML
    private Label lblPma;

    @FXML

    private void setDesplegable() {
        TipoVehiculo[] listaTipoVehiculo = TipoVehiculo.values();
        for (TipoVehiculo tipoVehiculo : listaTipoVehiculo) {
            mbTipoVehiculo.getItems().add(new MenuItem(tipoVehiculo.toString()));
        }

        for (MenuItem menuItem : mbTipoVehiculo.getItems()) {
            if (menuItem.getText().equals("Turismo")) {
                menuItem.setOnAction(e -> turismoSeleccionado(e));
            }
            if (menuItem.getText().equals("Autobus")) {
                menuItem.setOnAction(e -> autobusSeleccionado(e));
            }
            if (menuItem.getText().equals("Furgoneta")) {
                menuItem.setOnAction(e -> furgonetaSeleccionado(e));
            }
        }
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
    private void initialize() {
        setDesplegable();
        tfPlazas.setVisible(false);
        lblPlazas.setVisible(false);

        tfCilindrada.setVisible(false);
        lblCilindrada.setVisible(false);

        tfPma.setVisible(false);
        lblPma.setVisible(false);
    }

    public void setControladorMVC(IControlador controlador) {
        controladorMVC = controlador;
    }

    public void setFiltro(ObservableList<Vehiculo> filtro) {
        this.obsFiltroVehiculos = filtro;
    }

    public void setVehiculos(ObservableList<Vehiculo> vehiculos) {
        this.vehiculos = vehiculos;
    }

    @FXML
    void autobusSeleccionado(ActionEvent event) {
        mbTipoVehiculo.setText("Autobus");
        tipoVehiculo = "Autobus";

        tfPlazas.setVisible(true);
        lblPlazas.setVisible(true);
        tfCilindrada.setVisible(false);
        lblCilindrada.setVisible(false);
        tfPma.setVisible(false);
        lblPma.setVisible(false);
    }

    @FXML
    void furgonetaSeleccionado(ActionEvent event) {
        mbTipoVehiculo.setText("Furgoneta");
        tipoVehiculo = "Furgoneta";

        tfPlazas.setVisible(true);
        lblPlazas.setVisible(true);
        tfCilindrada.setVisible(false);
        lblCilindrada.setVisible(false);
        tfPma.setVisible(true);
        lblPma.setVisible(true);
    }

    @FXML
    void turismoSeleccionado(ActionEvent event) {
        mbTipoVehiculo.setText("Turismo");
        tipoVehiculo = "Turismo";

        tfPlazas.setVisible(false);
        lblPlazas.setVisible(false);
        tfCilindrada.setVisible(true);
        lblCilindrada.setVisible(true);
        tfPma.setVisible(false);
        lblPma.setVisible(false);
    }

    @FXML
    void anadir(ActionEvent event) {
        String marca = tfMarca.getText();
        String modelo = tfModelo.getText();
        String matricula = tfMatricula.getText();

        Vehiculo vehiculo = null;
        try {
            if (tipoVehiculo.equals("Turismo")) {
                int cilindrada = Integer.parseInt(tfCilindrada.getText());
                vehiculo = new Turismo(marca, modelo, matricula, cilindrada);
                System.out.println(vehiculo);
            }

            if (tipoVehiculo.equals("Furgoneta")) {
                int plazas = Integer.parseInt(tfPlazas.getText());
                int pma = Integer.parseInt(tfPma.getText());
                vehiculo = new Furgoneta(marca, modelo, pma, plazas, matricula);
            }

            if (tipoVehiculo.equals("Autobus")) {
                int plazas = Integer.parseInt(tfPlazas.getText());
                vehiculo = new Autobus(marca, modelo, matricula, plazas);
            }

            controladorMVC.insertar(vehiculo);
            vehiculos.add(vehiculo);

            if (busqueda != null && vehiculo.getMatricula().toLowerCase().contains(busqueda.toLowerCase())) {
                obsFiltroVehiculos.add(vehiculo);
            }

            Stage escenario = (Stage) ((Node) event.getSource()).getScene().getWindow();
            escenario.close();

            Dialogos.mostrarDialogoConfirmacion("Insertar Vehiculo", "Vehiculo insertado correctamente");

        } catch (Exception e) {
            Dialogos.mostrarDialogoError("Error insercciñn vehiculo", e.getMessage());
        }

    }

    public static void setBusqueda(String filtroMatricula) {
        busqueda = filtroMatricula;
    }

}