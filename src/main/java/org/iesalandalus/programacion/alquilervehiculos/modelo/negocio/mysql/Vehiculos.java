package org.iesalandalus.programacion.alquilervehiculos.modelo.negocio.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.naming.OperationNotSupportedException;

import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Autobus;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Furgoneta;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Turismo;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Vehiculo;
import org.iesalandalus.programacion.alquilervehiculos.modelo.negocio.IVehiculos;
import org.iesalandalus.programacion.alquilervehiculos.modelo.negocio.mysql.utilities.MySQL;
import org.iesalandalus.programacion.alquilervehiculos.vista.texto.TipoVehiculo;

public class Vehiculos implements IVehiculos {
    private Connection conexion = null;

    private static Vehiculos instancia;

    private Vehiculos() {

    }

    static Vehiculos getInstancia() {
        // Si instancia es nula, se le da valor y se devuelve:
        if (instancia == null) {
            instancia = new Vehiculos();
        }
        return instancia;
    }

    @Override
    public void comenzar() {
        conexion = MySQL.establecerConexion();
    }

    @Override
    public void terminar() {
        MySQL.cerrarConexion();
    }

    @Override
    public List<Vehiculo> get() {
        Vehiculo vehiculo;
        List<Vehiculo> vehiculos = new ArrayList<>();

        try {
            String sentenciaStr = "select matricula, modelo, marca, tipo, cilindrada, plazas, pma from vehiculos order by marca";
            Statement sentencia = conexion.createStatement();
            ResultSet filas = sentencia.executeQuery(sentenciaStr);
            while (filas.next()) {
                String matricula = filas.getString(1);
                String modelo = filas.getString(2);
                String marca = filas.getString(3);
                String tipo = filas.getString(4);

                if (tipo.toLowerCase().equals(TipoVehiculo.TURISMO.toString().toLowerCase())) {
                    int cilindrada = filas.getInt(5);
                    vehiculo = new Turismo(marca, modelo, matricula, cilindrada);
                    vehiculos.add(vehiculo);
                }
                if (tipo.toLowerCase().equals(TipoVehiculo.FURGONETA.toString().toLowerCase())) {
                    int plazas = filas.getInt(6);
                    int pma = filas.getInt(7);
                    vehiculo = new Furgoneta(marca, modelo, pma, plazas, matricula);
                    vehiculos.add(vehiculo);
                }
                if (tipo.toLowerCase().equals(TipoVehiculo.AUTOBUS.toString().toLowerCase())) {
                    int plazas = filas.getInt(6);
                    vehiculo = new Autobus(marca, modelo, matricula, plazas);
                    vehiculos.add(vehiculo);
                }

            }
        } catch (SQLException e) {
            throw new IllegalArgumentException("ERROR: " + e.getMessage());
        }

        return vehiculos;
    }

    @Override
    public void insertar(Vehiculo vehiculo) throws OperationNotSupportedException {
        if (vehiculo == null) {
            throw new NullPointerException("ERROR: No se puede insertar un vehiculo nulo.");
        }

        try {
            System.out.println(vehiculo);
            String sentenciaStr = "insert into vehiculos values (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement sentencia = conexion.prepareStatement(sentenciaStr);

            sentencia.setString(1, vehiculo.getMatricula());
            sentencia.setString(2, vehiculo.getModelo());
            sentencia.setString(3, vehiculo.getMarca());

            if (vehiculo instanceof Turismo) {
                Turismo turismo = new Turismo((Turismo) vehiculo);

                sentencia.setString(4, TipoVehiculo.TURISMO.toString().toLowerCase());
                sentencia.setInt(5, turismo.getCilindrada());
                sentencia.setNull(6, Types.TINYINT);
                sentencia.setNull(7, Types.INTEGER);
                sentencia.executeUpdate();
            }
            if (vehiculo instanceof Furgoneta) {
                Furgoneta furgoneta = new Furgoneta((Furgoneta) vehiculo);

                sentencia.setString(4, TipoVehiculo.FURGONETA.toString().toLowerCase());
                sentencia.setNull(5, Types.INTEGER);
                sentencia.setInt(6, furgoneta.getPlazas());
                sentencia.setInt(7, furgoneta.getPma());
                sentencia.executeUpdate();
            }
            if (vehiculo instanceof Autobus) {
                Autobus autobus = new Autobus((Autobus) vehiculo);

                sentencia.setString(4, TipoVehiculo.AUTOBUS.toString().toLowerCase());
                sentencia.setNull(5, Types.INTEGER);
                sentencia.setInt(6, autobus.getPlazas());
                sentencia.setNull(7, Types.INTEGER);
                sentencia.executeUpdate();
            }

        } catch (

        SQLIntegrityConstraintViolationException e) {
            throw new OperationNotSupportedException("ERROR: Ya existe un alquiler igual.");
        } catch (SQLException e) {
            throw new OperationNotSupportedException("ERROR:" + e.getMessage());
        }
    }

    @Override
    public Vehiculo buscar(Vehiculo vehiculo) {
        Vehiculo vehiculoBuscado = null;

        if (vehiculo == null) {
            throw new NullPointerException("ERROR: No se puede buscar un vehiculo nulo.");
        }

        try {
            String sentenciaStr = "select matricula, modelo, marca, tipo, cilindrada, plazas, pma from vehiculos where matricula = ?";

            PreparedStatement sentencia = conexion.prepareStatement(sentenciaStr);

            sentencia.setString(1, vehiculo.getMatricula());
            ResultSet filas = sentencia.executeQuery();
            if (filas.next()) {
                String matricula = filas.getString(1);
                String modelo = filas.getString(2);
                String marca = filas.getString(3);
                String tipo = filas.getString(4);

                if (tipo.toLowerCase().equals(TipoVehiculo.TURISMO.toString().toLowerCase())) {
                    int cilindrada = filas.getInt(5);
                    vehiculoBuscado = new Turismo(marca, modelo, matricula, cilindrada);
                }
                if (tipo.toLowerCase().equals(TipoVehiculo.FURGONETA.toString().toLowerCase())) {
                    int plazas = filas.getInt(6);
                    int pma = filas.getInt(7);
                    vehiculoBuscado = new Furgoneta(marca, modelo, pma, plazas, matricula);
                }
                if (tipo.toLowerCase().equals(TipoVehiculo.AUTOBUS.toString().toLowerCase())) {
                    int plazas = filas.getInt(6);
                    vehiculoBuscado = new Autobus(marca, modelo, matricula, plazas);
                }
            }

        } catch (SQLException e) {
            throw new IllegalArgumentException("ERROR:" + e.getMessage());
        }

        return vehiculoBuscado;
    }

    @Override
    public void borrar(Vehiculo vehiculo) throws OperationNotSupportedException {
        if (vehiculo == null) {
            throw new NullPointerException("ERROR: No se puede borrar un vehiculo nulo.");
        }

        try {
            String sentenciaStr = "delete from vehiculos where matricula = ?";
            PreparedStatement sentencia = conexion.prepareStatement(sentenciaStr);

            sentencia.setString(1, vehiculo.getMatricula());
            if (sentencia.executeUpdate() == 0) {
                throw new OperationNotSupportedException("ERROR: No existe ningún alquiler con los datos indicados.");
            }

        } catch (SQLException e) {
            throw new OperationNotSupportedException("ERROR:" + e.getMessage());
        }
    }

}
