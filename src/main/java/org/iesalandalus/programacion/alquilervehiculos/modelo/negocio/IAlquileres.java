package org.iesalandalus.programacion.alquilervehiculos.modelo.negocio;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import javax.naming.OperationNotSupportedException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Alquiler;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Cliente;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Vehiculo;
import org.xml.sax.SAXException;

public interface IAlquileres {

    void comenzar() throws OperationNotSupportedException, ParserConfigurationException, SAXException, IOException;

    void terminar() throws ParserConfigurationException, TransformerException;

    List<Alquiler> get();

    List<Alquiler> get(Cliente cliente);

    List<Alquiler> get(Vehiculo vehiculo);

    void insertar(Alquiler alquiler) throws OperationNotSupportedException;

    void devolver(Cliente cliente, LocalDate fechaDevolucion) throws OperationNotSupportedException;

    void devolver(Vehiculo vehiculo, LocalDate fechaDevolucion) throws OperationNotSupportedException;

    Alquiler buscar(Alquiler alquiler);

    void borrar(Alquiler alquiler) throws OperationNotSupportedException;


}