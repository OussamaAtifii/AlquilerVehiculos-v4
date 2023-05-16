package org.iesalandalus.programacion.alquilervehiculos.modelo;

import org.iesalandalus.programacion.alquilervehiculos.modelo.negocio.IFuenteDatos;
import org.iesalandalus.programacion.alquilervehiculos.modelo.negocio.ficheros.FuenteDatosFicheros;
import org.iesalandalus.programacion.alquilervehiculos.modelo.negocio.memoria.FuenteDatosMemoria;
import org.iesalandalus.programacion.alquilervehiculos.modelo.negocio.mongodb.FactoriaFuenteDatosMongoDB;
import org.iesalandalus.programacion.alquilervehiculos.modelo.negocio.mysql.FuenteDatosMysql;

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
    },
    MYSQL {
        IFuenteDatos crear() {
            return new FuenteDatosMysql();
        }
    },
    MONGODB {
        IFuenteDatos crear() {
            return new FactoriaFuenteDatosMongoDB();
        }
    };

    abstract IFuenteDatos crear();
}
