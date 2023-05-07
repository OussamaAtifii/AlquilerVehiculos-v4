package org.iesalandalus.programacion.alquilervehiculos.controlador;

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

public interface IControlador {

    public void comenzar()
            throws OperationNotSupportedException, ParserConfigurationException, SAXException, IOException;

    public void terminar() throws TransformerException, ParserConfigurationException, SAXException, IOException;

    public void insertar(Cliente cliente) throws OperationNotSupportedException;

    public void insertar(Vehiculo vehiculo) throws OperationNotSupportedException;

    public void insertar(Alquiler alquiler) throws OperationNotSupportedException;

    public Cliente buscar(Cliente cliente);

    public Vehiculo buscar(Vehiculo vehiculo);

    public Alquiler buscar(Alquiler alquiler);

    public void modificar(Cliente cliente, String nombre, String telefono) throws OperationNotSupportedException;

    public void devolver(Cliente cliente, LocalDate fechaDevolucion) throws OperationNotSupportedException;

    public void devolver(Vehiculo vehiculo, LocalDate fechaDevolucion) throws OperationNotSupportedException;

    public void borrar(Cliente cliente) throws OperationNotSupportedException;

    public void borrar(Vehiculo vehiculo) throws OperationNotSupportedException;

    public void borrar(Alquiler alquiler) throws OperationNotSupportedException;

    public List<Cliente> getClientes();

    public List<Vehiculo> getVehiculos();

    public List<Alquiler> getAlquileres();

    public List<Alquiler> getAlquileres(Cliente cliente);

    public List<Alquiler> getAlquileres(Vehiculo Vehiculo);

}
