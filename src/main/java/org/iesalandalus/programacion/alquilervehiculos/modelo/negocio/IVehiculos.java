package org.iesalandalus.programacion.alquilervehiculos.modelo.negocio;

import java.io.IOException;
import java.util.List;

import javax.naming.OperationNotSupportedException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Vehiculo;
import org.xml.sax.SAXException;

public interface IVehiculos {

    void comenzar()  throws OperationNotSupportedException, ParserConfigurationException, SAXException, IOException;

    void terminar()  throws ParserConfigurationException, TransformerException;

    List<Vehiculo> get();

    void insertar(Vehiculo Vehiculo) throws OperationNotSupportedException;

    Vehiculo buscar(Vehiculo Vehiculo);

    void borrar(Vehiculo Vehiculo) throws OperationNotSupportedException;

}