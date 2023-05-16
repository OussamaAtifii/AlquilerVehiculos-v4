package org.iesalandalus.programacion.alquilervehiculos.vista.grafica.controllers;

import java.time.LocalDate;

import java.time.format.DateTimeFormatter;

import org.iesalandalus.programacion.alquilervehiculos.controlador.IControlador;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Alquiler;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Cliente;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Vehiculo;
import org.iesalandalus.programacion.alquilervehiculos.vista.grafica.utilities.Dialogos;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.converter.LocalDateStringConverter;

public class ControladorAñadirAlquiler {

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @FXML
    private TextField tfFormCliente;

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

    @FXML
    private DatePicker dpFechaAlquiler;

    @FXML
    private DatePicker dpFechaDevo;

    @FXML
    private ListView<String> lvDniCliente;

    @FXML
    private ListView<String> lvMatriculaVehiculo;

    private IControlador controladorMVC;
    // Observable datos alquileres:
    private ObservableList<Alquiler> alquileres;
    // Observable datos busqueda alquileres:
    private ObservableList<Alquiler> obsFiltroAlquileres;
    // Observables datos clientes y vehiculos
    private ObservableList<Cliente> clientes;
    private ObservableList<Vehiculo> vehiculos;

    private static String busquedaDni;
    private static String busquedaMatricula;

    public void setClientes(ObservableList<Cliente> clientes) {
        this.clientes = clientes;
    }

    public void setVehiculos(ObservableList<Vehiculo> vehiculos) {
        this.vehiculos = vehiculos;
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

    @FXML
    private void initialize() {
        lvDniCliente.setVisible(false);
        lvMatriculaVehiculo.setVisible(false);
        inicializaCalendarioAlquiler();
    }

    // Insertar dni seleccionado en textfiel dni:
    @FXML
    void SeleccionDniAlquiler(MouseEvent event) {

        String dniSeleccionado = lvDniCliente.getSelectionModel().getSelectedItem();

        if (dniSeleccionado != null && !dniSeleccionado.isEmpty()) {
            tfFormCliente.setText(dniSeleccionado);
            lvDniCliente.setVisible(false);
        }
    }

    // Insertar matricula seleccionada en textfield matricula:
    @FXML
    void SeleccionMatriculaAlquiler(MouseEvent event) {
        String matriculaSeleccionada = lvMatriculaVehiculo.getSelectionModel().getSelectedItem();

        if (matriculaSeleccionada != null && !matriculaSeleccionada.isEmpty()) {
            tfFormVehiculo.setText(matriculaSeleccionada);
            lvMatriculaVehiculo.setVisible(false);
        }
    }

    private void inicializaCalendarioAlquiler() {
        dpFechaAlquiler.setConverter(new LocalDateStringConverter(FORMATO_FECHA, null));
        final Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {

            @Override
            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);

                        if (item.isAfter(LocalDate.now())) {
                            this.setDisable(true);
                        }
                    }
                };
            }
        };

        dpFechaAlquiler.setDayCellFactory(dayCellFactory);
        dpFechaDevo.setDayCellFactory(dayCellFactory);
    }

    public void cargarDatos(Alquiler alquiler) {
        tfFormCliente.setDisable(true);
        tfFormCliente.setText(alquiler.getCliente().getDni());
        tfFormVehiculo.setDisable(true);
        tfFormVehiculo.setText(alquiler.getVehiculo().getMatricula());
        dpFechaAlquiler.setDisable(true);
        dpFechaAlquiler.setValue(alquiler.getFechaAlquiler());
    }

    // Activar boton de añadir y desactivar datepicker de fecha devolucion
    public void botonVisible() {
        btnFormAlquilerAñadir.setVisible(true);
        btnFormAlquilerDevolver.setVisible(false);
        lblFechaDevo.setVisible(false);
        dpFechaDevo.setVisible(false);
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
                    Vehiculo.getVehiculoConMatricula(tfFormVehiculo.getText()), dpFechaAlquiler.getValue());

            btnFormAlquilerAñadir.setVisible(false);
            btnFormAlquilerDevolver.setVisible(true);
            lblFechaDevo.setVisible(true);
            dpFechaDevo.setVisible(true);

            alquileres.remove(alquiler);

            controladorMVC.devolver(alquiler.getVehiculo(), dpFechaDevo.getValue());

            alquileres.setAll(controladorMVC.getAlquileres());

            Dialogos.mostrarDialogoConfirmacion("Devolver alquiler", "Alquiler devuelto correctamente");
            Stage escenario = (Stage) ((Node) event.getSource()).getScene().getWindow();
            escenario.close();
        } catch (Exception e) {
            Dialogos.mostrarDialogoError("Devolver Alquiler", e.getMessage());
        }
    }

    // Mostrar los dni disponibles para poder realizar un alquiler:
    @FXML
    void mostrarListaDni(MouseEvent event) {
        lvMatriculaVehiculo.setVisible(false);
        ObservableList<String> listaDni = FXCollections.observableArrayList();

        if (lvDniCliente.getItems().size() == 0) {

            for (Cliente cliente : clientes) {
                listaDni.add(cliente.getDni());
            }

            for (Alquiler alquiler : alquileres) {
                if (alquiler.getFechaDevolucion() == null) {
                    listaDni.remove(alquiler.getCliente().getDni());
                }
            }
        }

        lvDniCliente.getItems().addAll(listaDni);
        if (!lvDniCliente.isVisible()) {
            lvDniCliente.setVisible(true);
        } else {
            lvDniCliente.setVisible(false);
        }
    }

    // Mostrar las matriculas disponibles para poder realizar un alquiler:
    @FXML
    void mostrarListaMatriculas(MouseEvent event) {
        lvDniCliente.setVisible(false);

        ObservableList<String> listaMatriculas = FXCollections.observableArrayList();

        if (lvMatriculaVehiculo.getItems().size() == 0) {
            for (Vehiculo vehiculo : vehiculos) {
                listaMatriculas.add(vehiculo.getMatricula());
            }

            for (Alquiler alquiler : alquileres) {
                if (alquiler.getFechaDevolucion() == null) {
                    listaMatriculas.remove(alquiler.getVehiculo().getMatricula());
                }
            }
        }

        lvMatriculaVehiculo.getItems().addAll(listaMatriculas);

        if (!lvMatriculaVehiculo.isVisible()) {
            lvMatriculaVehiculo.setVisible(true);
        } else {
            lvMatriculaVehiculo.setVisible(false);
        }

    }

    // Evento para añadir alquiler:
    @FXML
    void anadir(ActionEvent event) {
        try {
            Cliente cliente = controladorMVC.buscar(Cliente.getClienteConDni(tfFormCliente.getText()));
            Vehiculo vehiculo = controladorMVC.buscar(Vehiculo.getVehiculoConMatricula(tfFormVehiculo.getText()));
            LocalDate fechaAlquiler = dpFechaAlquiler.getValue();
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