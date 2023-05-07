package org.iesalandalus.programacion.alquilervehiculos.modelo.negocio.memoria;

import java.util.ArrayList;
import java.util.List;

import javax.naming.OperationNotSupportedException;

import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Cliente;
import org.iesalandalus.programacion.alquilervehiculos.modelo.negocio.IClientes;

public class Clientes implements IClientes {
    private List<Cliente> coleccionClientes;

    public Clientes() {
        coleccionClientes = new ArrayList<>();
    }

    @Override
    public void insertar(Cliente cliente) throws OperationNotSupportedException {
        if (cliente == null) {
            throw new NullPointerException("ERROR: No se puede insertar un cliente nulo.");
        }
        if (coleccionClientes.contains(cliente)) {
            throw new OperationNotSupportedException("ERROR: Ya existe un cliente con ese DNI.");
        }
        coleccionClientes.add(cliente);
    }

    @Override
    public Cliente buscar(Cliente cliente) {
        int i;
        if (cliente == null) {
            throw new NullPointerException("ERROR: No se puede buscar un cliente nulo.");
        }
        if (coleccionClientes.contains(cliente)) {
            i = coleccionClientes.indexOf(cliente);
            cliente = coleccionClientes.get(i);
            return cliente;
        } else {
            return null;
        }
    }

    @Override
    public void borrar(Cliente cliente) throws OperationNotSupportedException {
        if (cliente == null) {
            throw new NullPointerException("ERROR: No se puede borrar un cliente nulo.");
        }
        if (!coleccionClientes.contains(cliente)) {
            throw new OperationNotSupportedException("ERROR: No existe ningún cliente con ese DNI.");
        } else {
            coleccionClientes.remove(cliente);
        }
    }

    @Override
    public void modificar(Cliente cliente, String nombre, String telefono) throws OperationNotSupportedException {
        int i;
        try {
            buscar(cliente);
        } catch (NullPointerException e) {
            throw new NullPointerException("ERROR: No se puede modificar un cliente nulo.");
        }

        if (buscar(cliente) == null) {
            throw new OperationNotSupportedException("ERROR: No existe ningún cliente con ese DNI.");
        }

        i = coleccionClientes.indexOf(cliente);
        cliente = coleccionClientes.get(i);

        if (nombre != null && !nombre.isBlank()) {
            cliente.setNombre(nombre);
        }
        if (telefono != null && !telefono.isBlank()) {
            cliente.setTelefono(telefono);
        }

    }

    @Override
    public List<Cliente> get() {
        return new ArrayList<>(coleccionClientes);
    }

    public void comenzar() {

    }

    public void terminar() {

    }

}
