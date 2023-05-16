package org.iesalandalus.programacion.alquilervehiculos.modelo.negocio.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.naming.OperationNotSupportedException;

import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Cliente;
import org.iesalandalus.programacion.alquilervehiculos.modelo.negocio.IClientes;
import org.iesalandalus.programacion.alquilervehiculos.modelo.negocio.mysql.utilities.MySQL;

public class Clientes implements IClientes {

    private Connection conexion = null;
    private static Clientes instancia;

    private Clientes() {

    }

    static Clientes getInstancia() {
        // Si instancia es nula, se le da valor y se devuelve:
        if (instancia == null) {
            instancia = new Clientes();
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
    public List<Cliente> get() {
        List<Cliente> clientes = new ArrayList<>();

        try {
            String sentenciaStr = "select nombre, dni, telefono from clientes order by nombre";
            Statement sentencia = conexion.createStatement();
            ResultSet filas = sentencia.executeQuery(sentenciaStr);

            while (filas.next()) {
                String nombre = filas.getString(1);
                String dni = filas.getString(2);
                String telefono = filas.getString(3);

                clientes.add(new Cliente(nombre, dni, telefono));
            }

        } catch (SQLException e) {
            throw new IllegalArgumentException("ERROR:" + e.getMessage());
        }

        return clientes;
    }

    @Override
    public void insertar(Cliente cliente) throws OperationNotSupportedException {
        if (cliente == null) {
            throw new NullPointerException("ERROR: No se puede insertar un cliente nulo.");
        }

        try {
            String sentenciaStr = "insert into clientes values(?, ?, ?)";
            PreparedStatement sentencia = conexion.prepareStatement(sentenciaStr);

            sentencia.setString(1, cliente.getDni());
            sentencia.setString(2, cliente.getNombre());
            sentencia.setString(3, cliente.getTelefono());
            sentencia.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new OperationNotSupportedException("ERROR: Ya existe un cliente igual.");
        } catch (SQLException e) {
            throw new OperationNotSupportedException("ERROR:" + e.getMessage());
        }
    }

    @Override
    public Cliente buscar(Cliente cliente) {
        Cliente clienteBuscado = null;

        if (cliente == null) {
            throw new NullPointerException("ERROR: No se puede buscar un cliente nulo.");
        }

        try {
            String sentenciaStr = "select nombre, dni, telefono from clientes where dni=?";
            PreparedStatement sentencia = conexion.prepareStatement(sentenciaStr);
            sentencia.setString(1, cliente.getDni());

            ResultSet filas = sentencia.executeQuery();
            if (filas.next()) {
                String nombre = filas.getString(1);
                String dni = filas.getString(2);
                String telefono = filas.getString(3);

                clienteBuscado = new Cliente(nombre, dni, telefono);
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException("ERROR:" + e.getMessage());
        }

        return clienteBuscado;

    }

    @Override
    public void borrar(Cliente cliente) throws OperationNotSupportedException {
        if (cliente == null) {
            throw new NullPointerException("ERROR: No se puede borrar un cliente nulo.");
        }
        try {
            String sentenciaStr = "delete from clientes where dni = ?";
            PreparedStatement sentencia = conexion.prepareStatement(sentenciaStr);
            sentencia.setString(1, cliente.getDni());

            if (sentencia.executeUpdate() == 0)
                throw new OperationNotSupportedException("ERROR: No existe ningún cliente con los datos indicados.");

        } catch (SQLException e) {
            throw new OperationNotSupportedException("ERROR:" + e.toString());
        }

    }

    @Override
    public void modificar(Cliente cliente, String nombre, String telefono) throws OperationNotSupportedException {
        if (cliente == null) {
            throw new NullPointerException("ERROR: No se puede modificar un cliente nulo.");
        }

        try {
            if (nombre != null && !nombre.isBlank()) {
                String sentenciaStr = "update clientes set nombre = ? where dni = ?";
                PreparedStatement sentencia = conexion.prepareStatement(sentenciaStr);

                sentencia.setString(1, nombre);
                sentencia.setString(2, cliente.getDni());

                if (sentencia.executeUpdate() == 0) {
                    throw new OperationNotSupportedException(
                            "ERROR: No existe ningún cliente con los datos indicados.");
                }
            }

            if (telefono != null && !telefono.isBlank()) {
                String sentenciaStr = "update clientes set telefono = ?  where dni = ?";
                PreparedStatement sentencia = conexion.prepareStatement(sentenciaStr);

                sentencia.setString(1, telefono);
                sentencia.setString(2, cliente.getDni());

                if (sentencia.executeUpdate() == 0) {
                    throw new OperationNotSupportedException(
                            "ERROR: No existe ningún cliente con los datos indicados.");
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (OperationNotSupportedException e) {
            System.out.println(e.getMessage());
        }
    }

}
