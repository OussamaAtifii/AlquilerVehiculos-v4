package org.iesalandalus.programacion.alquilervehiculos.vista;

import java.io.IOException;

import javax.naming.OperationNotSupportedException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javafx.application.Application;

import org.iesalandalus.programacion.alquilervehiculos.controlador.Controlador;
import org.xml.sax.SAXException;

public abstract class Vista extends Application {
    protected Controlador controlador;

    public void setControlador(Controlador controlador) {
        if (controlador == null) {
            throw new NullPointerException("ERROR: El controlador no puede ser nulo.");
        }
        this.controlador = controlador;
    }

    public abstract void comenzar()
            throws OperationNotSupportedException, ParserConfigurationException, SAXException, IOException;

    public abstract void terminar()
            throws TransformerException, ParserConfigurationException, SAXException, IOException;

}
