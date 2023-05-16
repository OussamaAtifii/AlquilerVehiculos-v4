package org.iesalandalus.programacion.alquilervehiculos.vista;

import java.io.IOException;

import javax.naming.OperationNotSupportedException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.iesalandalus.programacion.alquilervehiculos.controlador.Controlador;
import org.iesalandalus.programacion.alquilervehiculos.controlador.IControlador;
import org.xml.sax.SAXException;

import javafx.application.Application;

public abstract class Vista extends Application {
    protected IControlador controladorMVC;

    public void setControlador(Controlador controlador) {
        if (controlador == null) {
            throw new NullPointerException("ERROR: El controlador no puede ser nulo.");
        }
        this.controladorMVC = controlador;
    }

    public abstract void comenzar()
            throws OperationNotSupportedException, ParserConfigurationException, SAXException, IOException;

    public abstract void terminar()
            throws TransformerException, ParserConfigurationException, SAXException, IOException;

}

// if (args[0].equals("memoria-texto")) {
// modelo = new ModeloCascada(FactoriaFuenteDatos.MEMORIA);
// vista = new VistaTexto();
// }
// if (args[0].equals("memoria-grafica")) {
// modelo = new ModeloCascada(FactoriaFuenteDatos.MEMORIA);
// vista = new VistaGrafica();
// }
// if (args[0].equals("fichero-texto")) {
// modelo = new ModeloCascada(FactoriaFuenteDatos.FICHEROS);
// vista = new VistaTexto();
// }
// if (args[0].equals("fichero-grafica")) {
// modelo = new ModeloCascada(FactoriaFuenteDatos.FICHEROS);
// vista = new VistaGrafica();
// }
// if (args[0].equals("mysql-texto")) {
// modelo = new ModeloCascada(FactoriaFuenteDatos.MYSQL);
// vista = new VistaTexto();
// }
// if (args[0].equals("mysql-grafica")) {
// modelo = new ModeloCascada(FactoriaFuenteDatos.MYSQL);
// vista = new VistaGrafica();
// }