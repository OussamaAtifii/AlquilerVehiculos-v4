package org.iesalandalus.programacion.alquilervehiculos.vista.grafica.controllers;

import org.iesalandalus.programacion.alquilervehiculos.controlador.IControlador;

import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Cliente;
import org.iesalandalus.programacion.alquilervehiculos.vista.grafica.utilities.Dialogos;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ControladorAñadirCliente {

    private IControlador controladorMVC;
    private ObservableList<Cliente> clientes;
    private ObservableList<Cliente> obsFiltroClientes;
    private static String busqueda;
    @FXML
    private Button btnAnadir;

    @FXML
    private Button btnCancelar;

    @FXML
    private Label lblDni;

    @FXML
    private Label lblNombre;

    @FXML
    private Label lblTelefono;

    @FXML
    private TextField tfDni;

    @FXML
    private TextField tfNombre;

    @FXML
    private TextField tfTelefono;

    @FXML
    private Text txTitulo;

    @FXML
    private Button btnModificar;

    private TableView<Cliente> tablaClientes;

    public void setTablaClientes(TableView<Cliente> tablaClientes) {
        this.tablaClientes = tablaClientes;
    }

    @FXML
    private void initialize() {
    }

    public void setControladorMVC(IControlador controlador) {
        controladorMVC = controlador;
    }

    public void setClientes(ObservableList<Cliente> clientes) {
        this.clientes = clientes;
    }

    public void setFiltro(ObservableList<Cliente> filtro) {
        this.obsFiltroClientes = filtro;
    }

    public void cargarDatos(Cliente cliente) {
        tfDni.setDisable(true);
        tfDni.setText(cliente.getDni());
        tfNombre.setText(cliente.getNombre());
        tfTelefono.setText(cliente.getTelefono());
    }

    public void botonVisible() {
        btnAnadir.setVisible(true);
        btnModificar.setVisible(false);
    }

    @FXML
    void modificarCliente(ActionEvent event) {
        try {
            Cliente cliente = new Cliente(tfNombre.getText(), tfDni.getText(), tfTelefono.getText());
            btnAnadir.setVisible(false);
            btnModificar.setVisible(true);

            clientes.remove(cliente);
            controladorMVC.modificar(cliente, tfNombre.getText(), tfTelefono.getText());
            clientes.add(cliente);

            tablaClientes.refresh();
            Stage escenario = (Stage) ((Node) event.getSource()).getScene().getWindow();
            escenario.close();
            Dialogos.mostrarDialogoConfirmacion("Modificar cliente", "Cliente modificado correctamente");
        } catch (Exception e) {
            Dialogos.mostrarDialogoError("Error modificacion", e.getMessage());
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
    void anadirCliente(ActionEvent event) {
        try {
            Cliente cliente = new Cliente(tfNombre.getText(), tfDni.getText(), tfTelefono.getText());
            controladorMVC.insertar(cliente);
            clientes.add(cliente);

            if (busqueda != null && cliente.getDni().toLowerCase().contains(busqueda.toLowerCase())) {
                obsFiltroClientes.add(cliente);
            }

            Stage escenario = (Stage) ((Node) event.getSource()).getScene().getWindow();
            escenario.close();
            Dialogos.mostrarDialogoConfirmacion("Insertar Cliente", "Cliente insertado correctamente");
        } catch (Exception e) {
            Dialogos.mostrarDialogoError("Error insercci�n cliente", e.getMessage());
        }
    }

    public ObservableList<Cliente> getObsClientes() {
        return clientes;
    }

    public static void setBusqueda(String filtro) {
        busqueda = filtro;
    }

}