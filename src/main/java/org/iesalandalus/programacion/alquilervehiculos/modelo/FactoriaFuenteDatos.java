package org.iesalandalus.programacion.alquilervehiculos.modelo;

import org.iesalandalus.programacion.alquilervehiculos.modelo.negocio.IFuenteDatos;
import org.iesalandalus.programacion.alquilervehiculos.modelo.negocio.ficheros.FuenteDatosFicheros;
import org.iesalandalus.programacion.alquilervehiculos.modelo.negocio.memoria.FuenteDatosMemoria;

public enum FactoriaFuenteDatos {
    MEMORIA {
        IFuenteDatos crear() {
            return new FuenteDatosMemoria();
        }
    },
    FICHEROS {
        IFuenteDatos crear() {
            return new FuenteDatosFicheros();
        }
    },;

    abstract IFuenteDatos crear();
}
