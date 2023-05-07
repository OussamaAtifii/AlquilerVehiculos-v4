package org.iesalandalus.programacion.alquilervehiculos.modelo.negocio.memoria;

import java.util.ArrayList;
import java.util.List;

import javax.naming.OperationNotSupportedException;

import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Vehiculo;
import org.iesalandalus.programacion.alquilervehiculos.modelo.negocio.IVehiculos;

public class Vehiculos implements IVehiculos {
    private List<Vehiculo> coleccionVehiculos;

    public Vehiculos() {
        coleccionVehiculos = new ArrayList<>();
    }

    @Override
    public List<Vehiculo> get() {
        return new ArrayList<>(coleccionVehiculos);
    }

    @Override
    public void insertar(Vehiculo vehiculo) throws OperationNotSupportedException {
        if (vehiculo == null) {
            throw new NullPointerException("ERROR: No se puede insertar un vehiculo nulo.");
        }
        if (coleccionVehiculos.contains(vehiculo)) {
            throw new OperationNotSupportedException("ERROR: Ya existe un vehiculo con esa matrícula.");
        }

        coleccionVehiculos.add(vehiculo);
    }

    @Override
    public Vehiculo buscar(Vehiculo vehiculo) {
        int i;
        if (vehiculo == null) {
            throw new NullPointerException("ERROR: No se puede buscar un vehiculo nulo.");
        }
        if (coleccionVehiculos.contains(vehiculo)) {
            i = coleccionVehiculos.indexOf(vehiculo);
            vehiculo = coleccionVehiculos.get(i);
        } else {
            vehiculo = null;
        }
        return vehiculo;

    }

    @Override
    public void borrar(Vehiculo vehiculo) throws OperationNotSupportedException {
        if (vehiculo == null) {
            throw new NullPointerException("ERROR: No se puede borrar un vehiculo nulo.");
        }
        if (!coleccionVehiculos.contains(vehiculo)) {
            throw new OperationNotSupportedException("ERROR: No existe ningún vehiculo con esa matrícula.");
        }
        coleccionVehiculos.remove(vehiculo);
    }

    public void comenzar() {
    }

    public void terminar() {
    }

}
