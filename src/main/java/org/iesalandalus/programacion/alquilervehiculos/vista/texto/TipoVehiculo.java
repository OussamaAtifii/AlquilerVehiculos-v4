package org.iesalandalus.programacion.alquilervehiculos.vista.texto;

import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Autobus;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Furgoneta;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Turismo;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Vehiculo;

public enum TipoVehiculo {
    TURISMO("Turismo"), AUTOBUS("Autobus"), FURGONETA("Furgoneta");

    String nombre;

    private TipoVehiculo(String nombre) {
        this.nombre = nombre;
    }

    private static boolean esOrdinalValido(int ordinal) {
        TipoVehiculo[] tipoVehiculos = values();
        return ordinal < 0 || ordinal > tipoVehiculos.length;
    }

    public static TipoVehiculo get(int ordinal) {
        if (esOrdinalValido(ordinal)) {
            throw new IllegalArgumentException("El ordinal es invalido.");
        }

        TipoVehiculo[] tipoVehiculos = values();

        return tipoVehiculos[ordinal];
    }

    public static TipoVehiculo get(Vehiculo vehiculo) {
        if (vehiculo == null) {
            throw new NullPointerException("ERROR: El vehiculo no puede ser nulo.");
        }

        TipoVehiculo tipoVehiculo = null;

        if (vehiculo instanceof Turismo) {
            tipoVehiculo = TURISMO;
        }
        if (vehiculo instanceof Autobus) {
            tipoVehiculo = AUTOBUS;
        }
        if (vehiculo instanceof Furgoneta) {
            tipoVehiculo = FURGONETA;
        }

        return tipoVehiculo;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
