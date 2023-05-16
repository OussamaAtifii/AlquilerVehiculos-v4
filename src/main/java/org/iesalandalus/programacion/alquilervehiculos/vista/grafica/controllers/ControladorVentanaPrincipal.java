package org.iesalandalus.programacion.alquilervehiculos.vista.grafica.controllers;

import java.io.IOException;

import java.time.format.DateTimeFormatter;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.iesalandalus.programacion.alquilervehiculos.controlador.IControlador;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Alquiler;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Autobus;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Cliente;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Furgoneta;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Turismo;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Vehiculo;
import org.iesalandalus.programacion.alquilervehiculos.vista.grafica.utilities.Dialogos;
import org.xml.sax.SAXException;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class ControladorVentanaPrincipal {

    private final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Tabla clientes y sus columnas
    @FXML
    private TableView<Cliente> tvClientes;

    @FXML
    private TableColumn<Cliente, String> tcDni;

    @FXML
    private TableColumn<Cliente, String> tcNombre;

    @FXML
    private TableColumn<Cliente, String> tcTelefono;

    // Tabla vehiculos y sus columnas
    @FXML
    private TableView<Vehiculo> tvVehiculos;

    @FXML
    private TableColumn<Vehiculo, String> tcCilindrada;

    @FXML
    private TableColumn<Vehiculo, String> tcMarca;

    @FXML
    private TableColumn<Vehiculo, String> tcMatricula;

    @FXML
    private TableColumn<Vehiculo, String> tcModelo;

    @FXML
    private TableColumn<Vehiculo, String> tcPlazas;

    @FXML
    private TableColumn<Vehiculo, String> tcPma;

    // Tabla alquileres y sus columnas
    @FXML
    private TableView<Alquiler> tvAlquileres;

    @FXML
    private TableColumn<Alquiler, String> tcCliente;

    @FXML
    private TableColumn<Alquiler, String> tcFechaAlqui;

    @FXML
    private TableColumn<Alquiler, String> tcFechaDevo;

    @FXML
    private TableColumn<Alquiler, String> tcVehiculo;

    // Tabla alquileres(cliente o vehiculo) y sus columnas
    @FXML
    private TableView<Alquiler> tvAlquileresSecundario;

    @FXML
    private TableColumn<Alquiler, String> tcClienteAlquiler;

    @FXML
    private TableColumn<Alquiler, String> tcFechaAlquiAlquiler;

    @FXML
    private TableColumn<Alquiler, String> tcFechaDevoAlquiler;

    @FXML
    private TableColumn<Alquiler, String> tcVehiculoAlquiler;

    // Botones acceder a tablas
    @FXML
    private Button btnAlquileres;

    @FXML
    private Button btnClientes;

    @FXML
    private Button btnVehiculos;

    // Botones acceso a formulario
    @FXML
    private Button btnAnadirAlquiler;

    @FXML
    private Button btnAnadirCliente;

    @FXML
    private Button btnAnadirVehiculo;

    // TextFields busqueda:
    @FXML
    private TextField tfBuscarAlquilerCliente;

    @FXML
    private TextField tfBuscarAlquilerVehiculo;

    @FXML
    private TextField tfBuscarCliente;

    @FXML
    private TextField tfBuscarVehiculo;

    // Labels informacion alquiler:
    @FXML
    private Label lblInfoDniCliente;

    @FXML
    private Label lblInfoMarcaVehiculo;

    @FXML
    private Label lblInfoMatriculaVehiculo;

    @FXML
    private Label lblInfoModeloVehiculo;

    @FXML
    private Label lblInfoNombreCliente;

    @FXML
    private Label lblInfoPrecioAlquiler;

    @FXML
    private Label lblInfoTipoVehiculo;

    // Listas para insertar en tablas:
    private ObservableList<Cliente> obsClientes;
    private ObservableList<Vehiculo> obsVehiculos;
    private ObservableList<Alquiler> obsAlquileres;

    // Listas para filtros de busqueda:
    private ObservableList<Cliente> obsFiltroClientes;
    private ObservableList<Vehiculo> obsFiltroVehiculos;
    private ObservableList<Alquiler> obsFiltroAlquileres;

    // Controlador
    private IControlador controladorMVC;

    @FXML
    private void initialize() {
        obsClientes = FXCollections.observableArrayList();
        obsFiltroClientes = FXCollections.observableArrayList();

        obsVehiculos = FXCollections.observableArrayList();
        obsFiltroVehiculos = FXCollections.observableArrayList();

        obsAlquileres = FXCollections.observableArrayList();
        obsFiltroAlquileres = FXCollections.observableArrayList();

        // Al inicializar la app, mostrar datos de los clientes:
        setAtributosClientes();
        obsVehiculos.setAll(controladorMVC.getVehiculos());
        // Obtener alquilerse de cliente:
        tvClientes.setOnMouseClicked(e -> verAlquileres(e));
        // Obtener alquilerse de vehiculo:
        tvVehiculos.setOnMouseClicked(e -> verAlquileres(e));
    }

    public void setClientes() {
        obsClientes.setAll(controladorMVC.getClientes());
    }

    public void setControladorMVC(IControlador controlador) {
        controladorMVC = controlador;
    }

    // Insertar datos clientes en tabla:
    @FXML
    private void setAtributosClientes() {
        tfBuscarCliente.setVisible(true);
        tfBuscarVehiculo.setVisible(false);
        tfBuscarAlquilerCliente.setVisible(false);
        tfBuscarAlquilerVehiculo.setVisible(false);

        desactivarInfoAlquiler(true);
        activarBotonAnadir(btnAnadirCliente);

        tcDni.setCellValueFactory(cliente -> new SimpleStringProperty(cliente.getValue().getDni()));
        tcNombre.setCellValueFactory(cliente -> new SimpleStringProperty(cliente.getValue().getNombre()));
        tcTelefono.setCellValueFactory(cliente -> new SimpleStringProperty(cliente.getValue().getTelefono()));
        tvClientes.setItems(obsClientes);
        obsClientes.setAll(controladorMVC.getClientes());

        activarTabla(tvClientes);
    }

    // Insertar datos vehiculos en tabla:
    @FXML
    void setAtributosVehiculos(ActionEvent event) {
        tfBuscarCliente.setVisible(false);
        tfBuscarVehiculo.setVisible(true);
        tfBuscarAlquilerCliente.setVisible(false);
        tfBuscarAlquilerVehiculo.setVisible(false);

        desactivarInfoAlquiler(true);
        activarBotonAnadir(btnAnadirVehiculo);

        tcMarca.setCellValueFactory(new PropertyValueFactory<>("marca"));
        tcModelo.setCellValueFactory(new PropertyValueFactory<>("modelo"));
        tcMatricula.setCellValueFactory(new PropertyValueFactory<>("matricula"));

        tcCilindrada.setCellValueFactory(vehiculo -> {
            if (vehiculo.getValue() instanceof Turismo) {
                return new SimpleStringProperty(Integer.toString(((Turismo) vehiculo.getValue()).getCilindrada()));
            }
            return new SimpleStringProperty("-");

        });

        tcPma.setCellValueFactory(vehiculo -> {
            if (vehiculo.getValue() instanceof Furgoneta) {
                return new SimpleStringProperty(Integer.toString(((Furgoneta) vehiculo.getValue()).getPma()));
            }
            return new SimpleStringProperty("-");
        });

        tcPlazas.setCellValueFactory(vehiculo -> {
            if (vehiculo.getValue() instanceof Furgoneta) {
                return new SimpleStringProperty(Integer.toString(((Furgoneta) vehiculo.getValue()).getPlazas()));
            } else if (vehiculo.getValue() instanceof Autobus) {
                return new SimpleStringProperty(Integer.toString(((Autobus) vehiculo.getValue()).getPlazas()));
            }
            return new SimpleStringProperty("-");
        });

        tvVehiculos.setItems(obsVehiculos);
        obsVehiculos.setAll(controladorMVC.getVehiculos());

        activarTabla(tvVehiculos);
    }

    // Insertar datos alquileres en tabla
    @FXML
    void setAtributosAlquileres(ActionEvent event) {
        desactivarInfoAlquiler(false);

        tfBuscarCliente.setVisible(false);
        tfBuscarVehiculo.setVisible(false);
        tfBuscarAlquilerCliente.setVisible(true);
        tfBuscarAlquilerVehiculo.setVisible(true);
        activarBotonAnadir(btnAnadirAlquiler);

        tcCliente.setCellValueFactory(alquiler -> new SimpleStringProperty(alquiler.getValue().getCliente().getDni()));

        tcVehiculo.setCellValueFactory(
                alquiler -> new SimpleStringProperty(alquiler.getValue().getVehiculo().getMatricula()));

        tcFechaAlqui.setCellValueFactory(
                alquiler -> new SimpleStringProperty(alquiler.getValue().getFechaAlquiler().format(FORMATO_FECHA)));

        tcFechaDevo.setCellValueFactory(alquiler -> {
            if (alquiler.getValue().getFechaDevolucion() == null) {
                return new SimpleStringProperty("-");
            }
            return new SimpleStringProperty(alquiler.getValue().getFechaDevolucion().format(FORMATO_FECHA));
        });

        tvAlquileres.setItems(obsAlquileres);
        obsAlquileres.setAll(controladorMVC.getAlquileres());

        activarTabla(tvAlquileres);
    }

    // Formulario añadir cliente:
    @FXML
    void anadirCliente(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            ControladorAñadirCliente controlador = new ControladorAñadirCliente();
            loader.setController(controlador);
            loader.setLocation(getClass().getResource("../views/formularioCliente.fxml"));
            Parent raiz = loader.load();
            controlador = loader.getController();
            controlador.setControladorMVC(controladorMVC);
            controlador.setClientes(obsClientes);
            controlador.setFiltro(obsFiltroClientes);
            controlador.botonVisible();
            Scene escena = new Scene(raiz);
            Stage escenario = new Stage();
            escenario.setOnCloseRequest(e -> confirmarSalida(escenario, e));
            escenario.initModality(Modality.APPLICATION_MODAL);
            escenario.setScene(escena);
            escenario.setResizable(false);
            escenario.setTitle("Insertar cliente");
            escenario.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Formulario añadir vehiculo
    @FXML
    void anadirVehiculo(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            ControladorAñadirVehiculo controlador = new ControladorAñadirVehiculo();
            loader.setController(controlador);
            loader.setLocation(getClass().getResource("../views/formularioVehiculo.fxml"));
            Parent raiz = loader.load();
            controlador = loader.getController();
            controlador.setControladorMVC(controladorMVC);
            controlador.setFiltro(obsFiltroVehiculos);
            controlador.setVehiculos(obsVehiculos);
            Scene escena = new Scene(raiz);
            Stage escenario = new Stage();
            escenario.setOnCloseRequest(e -> confirmarSalida(escenario, e));
            escenario.initModality(Modality.APPLICATION_MODAL);
            escenario.setScene(escena);
            escenario.setResizable(false);
            escenario.setTitle("Insertar vehiculo");
            escenario.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Formulario añadir alquiler:
    @FXML
    void anadirAlquiler(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            ControladorAñadirAlquiler controlador = new ControladorAñadirAlquiler();
            loader.setController(controlador);
            loader.setLocation(getClass().getResource("../views/formularioAlquileres.fxml"));
            Parent raiz = loader.load();
            controlador = loader.getController();
            controlador.setControladorMVC(controladorMVC);
            controlador.setAlquileres(obsAlquileres);
            controlador.setFiltro(obsFiltroAlquileres);
            controlador.setClientes(obsClientes);
            controlador.setVehiculos(obsVehiculos);
            controlador.botonVisible();
            Scene escena = new Scene(raiz);
            Stage escenario = new Stage();
            escenario.setOnCloseRequest(e -> confirmarSalida(escenario, e));
            escenario.initModality(Modality.APPLICATION_MODAL);
            escenario.setScene(escena);
            escenario.setResizable(false);
            escenario.setTitle("Insertar alquiler");
            escenario.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void eliminarCliente(ActionEvent event) {
        Cliente cliente = tvClientes.getSelectionModel().getSelectedItem();

        // Confirmar y borrar cliente:
        if (cliente != null
                && Dialogos.mostrarDialogoConfirmacion("Borrar", "ñEstñs seguro de querer borrar el cliente?")) {
            try {
                controladorMVC.borrar(cliente);
                obsClientes.remove(cliente);
                obsFiltroClientes.remove(cliente);
                Dialogos.mostrarDialogoInformacion("Borrar Cliente", "Cliente borrado correctamente");

            } catch (Exception e) {
                Dialogos.mostrarDialogoError("Borrar Cliente", e.getMessage());
            }
        }

    }

    @FXML
    void eliminarVehiculo(ActionEvent event) {
        Vehiculo vehiculo = tvVehiculos.getSelectionModel().getSelectedItem();

        // Confirmar y borrar vehiculo:
        if (vehiculo != null
                && Dialogos.mostrarDialogoConfirmacion("Borrar", "ñEstñs seguro de querer borrar el vehiculo?")) {
            try {
                controladorMVC.borrar(vehiculo);
                obsVehiculos.remove(vehiculo);
                obsFiltroVehiculos.remove(vehiculo);
                Dialogos.mostrarDialogoInformacion("Borrar Vehiculo", "Vehiculo borrado correctamente");
            } catch (Exception e) {
                Dialogos.mostrarDialogoError("Borrar Vehiculo", e.getMessage());
            }
        }
    }

    @FXML
    void eliminarAlquiler(ActionEvent event) {
        Alquiler alquiler = tvAlquileres.getSelectionModel().getSelectedItem();

        // Confirmar y borrar vehiculo:
        if (alquiler != null
                && Dialogos.mostrarDialogoConfirmacion("Borrar", "ñEstñs seguro de querer borrar el alquiler?")) {
            try {
                controladorMVC.borrar(alquiler);
                obsAlquileres.remove(alquiler);
                obsFiltroAlquileres.remove(alquiler);
                Dialogos.mostrarDialogoInformacion("Borrar alquiler", "Alquiler borrado correctamente");
            } catch (Exception e) {
                Dialogos.mostrarDialogoError("Borrar alquiler", e.getMessage());
            }
        }
    }

    @FXML
    private void buscarCliente(KeyEvent event) {
        String filtroDni = this.tfBuscarCliente.getText();
        ControladorAñadirCliente.setBusqueda(filtroDni);

        if (filtroDni.isEmpty()) {
            tvClientes.setItems(obsClientes);
        } else {
            obsFiltroClientes.clear();
            for (Cliente cliente : obsClientes) {
                if (cliente.getDni().toLowerCase().contains(filtroDni.toLowerCase())) {
                    obsFiltroClientes.add(cliente);
                }
            }
            tvClientes.setItems(obsFiltroClientes);
        }
    }

    @FXML
    private void buscarVehiculo(KeyEvent event) {
        String filtroMatricula = tfBuscarVehiculo.getText();
        ControladorAñadirVehiculo.setBusqueda(filtroMatricula);

        if (filtroMatricula.isEmpty()) {
            tvVehiculos.setItems(obsVehiculos);
        } else {
            obsFiltroVehiculos.clear();
            for (Vehiculo vehiculo : obsVehiculos) {
                if (vehiculo.getMatricula().toLowerCase().contains(filtroMatricula.toLowerCase())) {
                    obsFiltroVehiculos.add(vehiculo);
                }
            }
            tvVehiculos.setItems(obsFiltroVehiculos);
        }
    }

    @FXML
    private void buscarAlquiler(KeyEvent event) {
        String filtroDni = tfBuscarAlquilerCliente.getText();
        String filtroMatricula = tfBuscarAlquilerVehiculo.getText();

        ControladorAñadirAlquiler.setBusquedaDni(filtroDni);
        ControladorAñadirAlquiler.setBusquedaMatricula(filtroMatricula);

        if (filtroDni.isEmpty() && filtroMatricula.isEmpty()) {
            tvAlquileres.setItems(obsAlquileres);
        } else if (!filtroDni.isEmpty()) {
            obsFiltroAlquileres.clear();
            for (Alquiler alquiler : obsAlquileres) {
                if (alquiler.getCliente().getDni().toLowerCase().contains(filtroDni.toLowerCase())) {
                    obsFiltroAlquileres.add(alquiler);
                }
            }
            tvAlquileres.setItems(obsFiltroAlquileres);
        } else {

            obsFiltroAlquileres.clear();
            for (Alquiler alquiler : obsAlquileres) {
                if (alquiler.getVehiculo().getMatricula().toLowerCase().contains(filtroMatricula.toLowerCase())) {
                    obsFiltroAlquileres.add(alquiler);
                }
            }
            tvAlquileres.setItems(obsFiltroAlquileres);

        }
    }

    // Modificar cliente seleccionado:
    @FXML
    void modificar(ActionEvent event) {
        Cliente cliente = tvClientes.getSelectionModel().getSelectedItem();

        if (cliente != null) {
            try {
                FXMLLoader loader = new FXMLLoader();
                ControladorAñadirCliente controlador = new ControladorAñadirCliente();
                loader.setController(controlador);
                loader.setLocation(getClass().getResource("../views/formularioCliente.fxml"));
                Parent raiz = loader.load();
                controlador = loader.getController();
                controlador.setControladorMVC(controladorMVC);
                controlador.setClientes(obsClientes);
                controlador.setTablaClientes(tvClientes);
                Scene escena = new Scene(raiz);
                controlador.cargarDatos(cliente);
                Stage escenario = new Stage();
                escenario.setOnCloseRequest(e -> confirmarSalida(escenario, e));
                escenario.initModality(Modality.APPLICATION_MODAL);
                escenario.setScene(escena);
                escenario.setResizable(false);
                escenario.setTitle("Modificar Cliente");
                escenario.showAndWait();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Dialogos.mostrarDialogoAdvertencia("Error modificacion cliente",
                    "Debe seleccionar un cliente para poder modificarlo");
        }
    }

    @FXML
    void devolver(ActionEvent event) {
        Alquiler alquiler = tvAlquileres.getSelectionModel().getSelectedItem();

        if (alquiler != null && alquiler.getFechaDevolucion() == null) {
            try {
                FXMLLoader loader = new FXMLLoader();
                ControladorAñadirAlquiler controlador = new ControladorAñadirAlquiler();
                loader.setController(controlador);
                loader.setLocation(getClass().getResource("../views/formularioAlquileres.fxml"));
                Parent raiz = loader.load();
                controlador = loader.getController();
                controlador.setControladorMVC(controladorMVC);
                controlador.setAlquileres(obsAlquileres);
                Scene escena = new Scene(raiz);
                controlador.cargarDatos(alquiler);
                Stage escenario = new Stage();
                escenario.setOnCloseRequest(e -> confirmarSalida(escenario, e));
                escenario.initModality(Modality.APPLICATION_MODAL);
                escenario.setScene(escena);
                escenario.setResizable(false);
                escenario.setTitle("Devolver Alquiler");
                escenario.showAndWait();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Dialogos.mostrarDialogoAdvertencia("ERROR Devolucion Alquiler",
                    "Debe seleccionar un alquiler aun no devuelto");
        }
    }

    @FXML
    void desactivarBuscarCliente(KeyEvent event) {
        tfBuscarAlquilerCliente.setDisable(true);
        tfBuscarAlquilerVehiculo.setDisable(false);
        if (tfBuscarAlquilerVehiculo.getText().isEmpty()) {
            tfBuscarAlquilerCliente.setDisable(false);
        }
    }

    @FXML
    void desactivarBuscarVehiculo(KeyEvent event) {
        tfBuscarAlquilerCliente.setDisable(false);
        tfBuscarAlquilerVehiculo.setDisable(true);
        if (tfBuscarAlquilerCliente.getText().isEmpty()) {
            tfBuscarAlquilerVehiculo.setDisable(false);
        }
    }

    private void desactivarInfoAlquiler(Boolean desactivar) {
        if (desactivar) {
            lblInfoDniCliente.setVisible(false);
            lblInfoMarcaVehiculo.setVisible(false);
            lblInfoMatriculaVehiculo.setVisible(false);
            lblInfoModeloVehiculo.setVisible(false);
            lblInfoNombreCliente.setVisible(false);
            lblInfoPrecioAlquiler.setVisible(false);
            lblInfoTipoVehiculo.setVisible(false);
        } else {
            lblInfoDniCliente.setVisible(true);
            lblInfoMarcaVehiculo.setVisible(true);
            lblInfoMatriculaVehiculo.setVisible(true);
            lblInfoModeloVehiculo.setVisible(true);
            lblInfoNombreCliente.setVisible(true);
            lblInfoPrecioAlquiler.setVisible(true);
            lblInfoTipoVehiculo.setVisible(true);

        }
    }

    // Activar el boton añadir pasado por parametro y desactivar los demas:
    private void activarBotonAnadir(Button button) {

        if (button.equals(btnAnadirCliente)) {
            btnAnadirCliente.setVisible(true);
            btnAnadirVehiculo.setVisible(false);
            btnAnadirAlquiler.setVisible(false);
            tvAlquileresSecundario.setVisible(true);
        }
        if (button.equals(btnAnadirVehiculo)) {
            btnAnadirCliente.setVisible(false);
            btnAnadirVehiculo.setVisible(true);
            btnAnadirAlquiler.setVisible(false);
            tvAlquileresSecundario.setVisible(true);
        }
        if (button.equals(btnAnadirAlquiler)) {
            btnAnadirCliente.setVisible(false);
            btnAnadirVehiculo.setVisible(false);
            btnAnadirAlquiler.setVisible(true);
            tvAlquileresSecundario.setVisible(false);
        }
    }

    // Activar la tabla pasada por parametro y desactivar las demas:
    private void activarTabla(TableView<?> tabla) {

        if (tabla.equals(tvClientes)) {
            tvClientes.setVisible(true);
            tvVehiculos.setVisible(false);
            tvAlquileres.setVisible(false);
        }
        if (tabla.equals(tvVehiculos)) {
            tvClientes.setVisible(false);
            tvVehiculos.setVisible(true);
            tvAlquileres.setVisible(false);
        }
        if (tabla.equals(tvAlquileres)) {
            tvClientes.setVisible(false);
            tvVehiculos.setVisible(false);
            tvAlquileres.setVisible(true);
        }
    }

    private void confirmarSalida(Stage escenarioPrincipal, WindowEvent event) {
        if (Dialogos.mostrarDialogoConfirmacion("Salir", "¿Estas seguro de que quieres salir de la aplicacion?",
                escenarioPrincipal)) {
            escenarioPrincipal.close();
        } else
            event.consume();
    }

    @FXML
    void mostrarInfoAlquiler(MouseEvent event) {
        Alquiler alquiler = tvAlquileres.getSelectionModel().getSelectedItem();
        if (alquiler != null) {
            lblInfoDniCliente.setText("Dni cliente: " + alquiler.getCliente().getDni());
            lblInfoNombreCliente.setText("Nombre cliente: " + alquiler.getCliente().getNombre());
            lblInfoMarcaVehiculo.setText("Marca vehiculo: " + alquiler.getVehiculo().getMarca());
            lblInfoModeloVehiculo.setText("Modelo vehiculo: " + alquiler.getVehiculo().getModelo());
            lblInfoMatriculaVehiculo
                    .setText("Matricula vehiculo: " + alquiler.getVehiculo().getMatricula());
            lblInfoTipoVehiculo
                    .setText("Tipo vehiculo: " + alquiler.getVehiculo().getClass().getSimpleName());

            if (alquiler.getPrecio() != 0) {
                lblInfoPrecioAlquiler.setText("Precio: " + alquiler.getPrecio() + "€");
            } else {
                lblInfoPrecioAlquiler.setText("Alquiler aun no devuelto");
            }
        }

    }

    public void verAlquileres(MouseEvent event) {
        Cliente cliente = null;
        Vehiculo vehiculo = null;

        cliente = tvClientes.getSelectionModel().getSelectedItem();
        vehiculo = tvVehiculos.getSelectionModel().getSelectedItem();

        if (cliente != null) {
            tcClienteAlquiler.setCellValueFactory(
                    alquiler -> new SimpleStringProperty(alquiler.getValue().getCliente().getDni()));
            tcVehiculoAlquiler.setCellValueFactory(
                    alquiler -> new SimpleStringProperty(alquiler.getValue().getVehiculo().getMatricula()));
            tcFechaAlquiAlquiler.setCellValueFactory(
                    alquiler -> new SimpleStringProperty(alquiler.getValue().getFechaAlquiler().format(FORMATO_FECHA)));
            tcFechaDevoAlquiler.setCellValueFactory(
                    alquiler -> {
                        return alquiler.getValue().getFechaDevolucion() != null
                                ? new SimpleStringProperty(
                                        alquiler.getValue().getFechaDevolucion().format(FORMATO_FECHA))
                                : new SimpleStringProperty("-");
                    });

            tvAlquileresSecundario.setItems(obsAlquileres);
            obsAlquileres.setAll(controladorMVC.getAlquileres(cliente));
        }

        if (vehiculo != null) {
            tcClienteAlquiler.setCellValueFactory(
                    alquiler -> new SimpleStringProperty(alquiler.getValue().getCliente().getDni()));
            tcVehiculoAlquiler.setCellValueFactory(
                    alquiler -> new SimpleStringProperty(alquiler.getValue().getVehiculo().getMatricula()));
            tcFechaAlquiAlquiler.setCellValueFactory(
                    alquiler -> new SimpleStringProperty(alquiler.getValue().getFechaAlquiler().format(FORMATO_FECHA)));
            tcFechaDevoAlquiler.setCellValueFactory(
                    alquiler -> {
                        return alquiler.getValue().getFechaDevolucion() != null
                                ? new SimpleStringProperty(
                                        alquiler.getValue().getFechaDevolucion().format(FORMATO_FECHA))
                                : new SimpleStringProperty("-");
                    });
            tvAlquileresSecundario.setItems(obsAlquileres);
            obsAlquileres.setAll(controladorMVC.getAlquileres(vehiculo));
        }
    }

    @FXML
    void salir(ActionEvent event) {
        try {
            controladorMVC.terminar();
        } catch (TransformerException | ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

}
