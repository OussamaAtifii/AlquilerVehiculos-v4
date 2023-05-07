package org.iesalandalus.programacion.alquilervehiculos.modelo.negocio.memoria;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.naming.OperationNotSupportedException;

import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Alquiler;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Cliente;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Vehiculo;
import org.iesalandalus.programacion.alquilervehiculos.modelo.negocio.IAlquileres;

public class Alquileres implements IAlquileres {
    private static List<Alquiler> coleccionAlquileres;

    public Alquileres() {
        coleccionAlquileres = new ArrayList<>();
    }

    @Override
    public List<Alquiler> get() {
        List<Alquiler> alquileresCliente = new ArrayList<>();

        for (Alquiler alquiler : coleccionAlquileres) {
            alquileresCliente.add(alquiler);
        }

        return alquileresCliente;
    }

    @Override
    public List<Alquiler> get(Cliente cliente) {
        List<Alquiler> alquileresCliente = new ArrayList<>();

        for (Alquiler alquiler : coleccionAlquileres) {
            if (alquiler.getCliente().equals(cliente)) {
                alquileresCliente.add(alquiler);
            }
        }
        return alquileresCliente;
    }

    @Override
    public List<Alquiler> get(Vehiculo vehiculo) {
        List<Alquiler> alquileresTurismo = new ArrayList<>();

        for (Alquiler alquiler : coleccionAlquileres) {
            if (alquiler.getVehiculo().equals(vehiculo)) {
                alquileresTurismo.add(alquiler);
            }
        }
        return alquileresTurismo;
    }

    @Override
    public void insertar(Alquiler alquiler) throws OperationNotSupportedException {
        if (alquiler == null) {
            throw new NullPointerException("ERROR: No se puede insertar un alquiler nulo.");
        }

        comprobarAlquiler(alquiler.getCliente(), alquiler.getVehiculo(), alquiler.getFechaAlquiler());
        coleccionAlquileres.add(alquiler);
    }

    private Alquiler getAlquilerAbierto(Cliente cliente) {
        Alquiler alquilerAbierto = null;
        for (Alquiler alquiler : get(cliente)) {
            if (alquiler.getFechaDevolucion() == null) {
                alquilerAbierto = alquiler;
            }
        }
        return alquilerAbierto;
    }

    @Override
    public void devolver(Cliente cliente, LocalDate fechaDevolucion) throws OperationNotSupportedException {
        Alquiler alquilerAbierto = getAlquilerAbierto(cliente);

        if (cliente == null) {
            throw new NullPointerException("ERROR: No se puede devolver un alquiler nulo.");
        }
        if (fechaDevolucion == null) {
            throw new NullPointerException("ERROR: La fecha de devolución no puede ser nula.");
        }
        if (alquilerAbierto == null) {
            throw new IllegalArgumentException("ERROR: No existe ningun alquiler abierto para el cliente introducido");
        }
        alquilerAbierto.devolver(fechaDevolucion);

    }

    private Alquiler getAlquilerAbierto(Vehiculo vehiculo) {
        Alquiler alquilerAbierto = null;
        for (Alquiler alquiler : get(vehiculo)) {
            if (alquiler.getFechaDevolucion() == null) {
                alquilerAbierto = alquiler;
                break;
            }
        }
        return alquilerAbierto;
    }

    @Override
    public void devolver(Vehiculo vehiculo, LocalDate fechaDevolucion) throws OperationNotSupportedException {
        Alquiler alquilerAbierto = getAlquilerAbierto(vehiculo);

        if (vehiculo == null) {
            throw new NullPointerException("ERROR: No se puede devolver un alquiler nulo.");
        }
        if (fechaDevolucion == null) {
            throw new NullPointerException("ERROR: La fecha de devolución no puede ser nula.");
        }
        if (alquilerAbierto == null) {
            throw new IllegalArgumentException("ERROR: No existe ningun alquiler abierto para el vehiculo introducido");
        }
        alquilerAbierto.devolver(fechaDevolucion);

    }

    @Override
    public Alquiler buscar(Alquiler alquiler) {
        int i;
        if (alquiler == null) {
            throw new NullPointerException("ERROR: No se puede buscar un alquiler nulo.");
        }
        if (coleccionAlquileres.contains(alquiler)) {
            i = coleccionAlquileres.indexOf(alquiler);
            alquiler = coleccionAlquileres.get(i);
            return alquiler;
        } else {
            return null;
        }
    }

    @Override
    public void borrar(Alquiler alquiler) throws OperationNotSupportedException {
        if (alquiler == null) {
            throw new NullPointerException("ERROR: No se puede borrar un alquiler nulo.");
        }
        if (!coleccionAlquileres.contains(alquiler)) {
            throw new OperationNotSupportedException("ERROR: No existe ningún alquiler igual.");
        }
        coleccionAlquileres.remove(alquiler);
    }

    private void comprobarAlquiler(Cliente cliente, Vehiculo vehiculo, LocalDate fechaAlquiler)
            throws OperationNotSupportedException {

        if (cliente == null) {
            throw new NullPointerException("ERROR: El cliente no puede ser nulo.");
        }
        if (vehiculo == null) {
            throw new NullPointerException("ERROR: El turismo no puede ser nulo.");
        }

        for (Alquiler alquiler : get(cliente)) {
            if (alquiler.getFechaDevolucion() == null) {
                throw new OperationNotSupportedException("ERROR: El cliente tiene otro alquiler sin devolver.");
            } else {
                if (alquiler.getFechaDevolucion().isAfter(fechaAlquiler) || alquiler.getFechaDevolucion().equals(fechaAlquiler)) {
                    throw new OperationNotSupportedException("ERROR: El cliente tiene un alquiler posterior.");
                }
            }
        }

        for (Alquiler alquiler : get(vehiculo)) {
            if (alquiler.getFechaDevolucion() == null) {
                throw new OperationNotSupportedException("ERROR: El turismo está actualmente alquilado.");
            } else {
                if (alquiler.getFechaDevolucion().isAfter(fechaAlquiler)|| alquiler.getFechaDevolucion().equals(fechaAlquiler)) {
                    throw new OperationNotSupportedException("ERROR: El turismo tiene un alquiler posterior.");
                }
            }
        }
    }

    public void comenzar() {
    }

    public void terminar() {
    }

}
