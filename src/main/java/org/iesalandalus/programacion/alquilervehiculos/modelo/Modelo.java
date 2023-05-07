package org.iesalandalus.programacion.alquilervehiculos.modelo;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import javax.naming.OperationNotSupportedException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Alquiler;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Cliente;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Vehiculo;
import org.iesalandalus.programacion.alquilervehiculos.modelo.negocio.IAlquileres;
import org.iesalandalus.programacion.alquilervehiculos.modelo.negocio.IClientes;
import org.iesalandalus.programacion.alquilervehiculos.modelo.negocio.IFuenteDatos;
import org.iesalandalus.programacion.alquilervehiculos.modelo.negocio.IVehiculos;
import org.xml.sax.SAXException;

public abstract class Modelo {
    private IClientes clientes;
    private IVehiculos vehiculos;
    private IAlquileres alquileres;
    private IFuenteDatos fuenteDatos;

    protected Modelo(FactoriaFuenteDatos factoriaFuenteDatos) {
        if (factoriaFuenteDatos == null) {
            throw new NullPointerException("ERROR: La fuente de datos no puede ser nula.");
        }
        setFuenteDatos(factoriaFuenteDatos.crear());

        clientes = fuenteDatos.crearClientes();
        vehiculos = fuenteDatos.crearVehiculos();
        alquileres = fuenteDatos.crearAlquileres();
    }

    protected IClientes getClientes() {
        return clientes;
    }

    protected IVehiculos getVehiculos() {
        return vehiculos;
    }

    protected IAlquileres getAlquileres() {
        return alquileres;
    }

    protected void setFuenteDatos(IFuenteDatos fuenteDatos) {
        this.fuenteDatos = fuenteDatos;
    }

    public void comenzar()
            throws OperationNotSupportedException, ParserConfigurationException, SAXException, IOException {
        clientes.comenzar();
        vehiculos.comenzar();
        alquileres.comenzar();
    }

    public void terminar() throws TransformerException, ParserConfigurationException, SAXException, IOException {
        clientes.terminar();
        vehiculos.terminar();
        alquileres.terminar();
        System.out.println("Modelo finalizado.");
    }

    public abstract void insertar(Cliente cliente) throws OperationNotSupportedException;

    public abstract void insertar(Vehiculo vehiculo) throws OperationNotSupportedException;

    public abstract void insertar(Alquiler alquiler) throws OperationNotSupportedException;

    public abstract Cliente buscar(Cliente cliente);

    public abstract Vehiculo buscar(Vehiculo vehiculo);

    public abstract Alquiler buscar(Alquiler alquiler);

    public abstract void modificar(Cliente cliente, String nombre, String telefono)
            throws OperationNotSupportedException;

    public abstract void devolver(Cliente cliente, LocalDate fechaDevolucion) throws OperationNotSupportedException;

    public abstract void devolver(Vehiculo vehiculo, LocalDate fechaDevolucion) throws OperationNotSupportedException;

    public abstract void borrar(Cliente cliente) throws OperationNotSupportedException;

    public abstract void borrar(Vehiculo vehiculo) throws OperationNotSupportedException;

    public abstract void borrar(Alquiler alquiler) throws OperationNotSupportedException;

    public abstract List<Cliente> getListaClientes();

    public abstract List<Vehiculo> getListaVehiculos();

    public abstract List<Alquiler> getListaAlquileres();

    public abstract List<Alquiler> getListaAlquileres(Cliente cliente);

    public abstract List<Alquiler> getListaAlquileres(Vehiculo vehiculo);

}
