package org.iesalandalus.programacion.alquilervehiculos.vista.grafica;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.iesalandalus.programacion.alquilervehiculos.controlador.IControlador;
import org.iesalandalus.programacion.alquilervehiculos.vista.Vista;
import org.iesalandalus.programacion.alquilervehiculos.vista.grafica.controllers.ControladorVentanaPrincipal;
import org.iesalandalus.programacion.alquilervehiculos.vista.grafica.utilities.Dialogos;
import org.xml.sax.SAXException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class VistaGrafica extends Vista {
    private static VistaGrafica instancia = null;

    public VistaGrafica() {
        if (instancia != null) {
            controladorMVC = instancia.controladorMVC;
        } else {
            instancia = this;
        }

    }

    public void start(Stage escenarioPrincipal) {
        try {
            FXMLLoader loader = new FXMLLoader();
            ControladorVentanaPrincipal controlador = new ControladorVentanaPrincipal();
            controlador.setControladorMVC(controladorMVC);
            loader.setController(controlador);
            loader.setLocation(getClass().getResource("views/ventanaPrincipal.fxml"));
            Parent raiz = loader.load();
            controlador = loader.getController();

            controlador.setClientes();
            Scene escena = new Scene(raiz);
            escenarioPrincipal.setOnCloseRequest(e -> confirmarSalida(escenarioPrincipal, e));
            escenarioPrincipal.setTitle("Gestión de Alquileres");
            escenarioPrincipal.setScene(escena);
            escenarioPrincipal.setResizable(false);
            escenarioPrincipal.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public IControlador getControladorMVC() {
        return controladorMVC;
    }

    public void comenzar() {
        launch(this.getClass());
    }

    public void setControlador(IControlador controlador) {
        this.controladorMVC = controlador;
    }

    private void confirmarSalida(Stage escenarioPrincipal, WindowEvent event) {
        if (Dialogos.mostrarDialogoConfirmacion("Salir", "¿Estás seguro de que quieres salir de la aplicación?",
                escenarioPrincipal)) {
            try {
                this.controladorMVC.terminar();
                escenarioPrincipal.close();
            } catch (Exception e) {
                Dialogos.mostrarDialogoError("Error", e.getMessage());
            }
        } else
            event.consume();
    }

    public void terminar() throws TransformerException, ParserConfigurationException, SAXException, IOException {
        throw new UnsupportedOperationException("Unimplemented method 'terminar'");
    }

}
