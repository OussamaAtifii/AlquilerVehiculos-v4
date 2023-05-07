package org.iesalandalus.programacion.alquilervehiculos.modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.naming.OperationNotSupportedException;

import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Alquiler;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Autobus;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Cliente;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Furgoneta;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Turismo;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Vehiculo;

public class ModeloCascada extends Modelo {

    public ModeloCascada(FactoriaFuenteDatos factoriaFuenteDatos) {
        super(factoriaFuenteDatos);
    }

    public void insertar(Cliente cliente) throws OperationNotSupportedException {
        getClientes().insertar(new Cliente(cliente));
    }

    public void insertar(Vehiculo vehiculo) throws OperationNotSupportedException {

        if (vehiculo instanceof Turismo) {
            getVehiculos().insertar(new Turismo((Turismo) vehiculo));
        }
        if (vehiculo instanceof Furgoneta) {
            getVehiculos().insertar(new Furgoneta((Furgoneta) vehiculo));
        }
        if (vehiculo instanceof Autobus) {
            getVehiculos().insertar(new Autobus((Autobus) vehiculo));
        }
    }

    public void insertar(Alquiler alquiler) throws OperationNotSupportedException {
        if (alquiler == null) {
            throw new NullPointerException("ERROR: No se puede realizar un alquiler nulo.");
        }

        Cliente cliente = getClientes().buscar(alquiler.getCliente());
        Vehiculo vehiculo = getVehiculos().buscar(alquiler.getVehiculo());

        if (cliente == null) {
            throw new OperationNotSupportedException("ERROR: No existe el cliente del alquiler.");
        }
        if (vehiculo == null) {
            throw new OperationNotSupportedException("ERROR: No existe el vehiculo del alquiler.");
        }

        getAlquileres().insertar(new Alquiler(cliente, vehiculo, alquiler.getFechaAlquiler()));

    }

    public Cliente buscar(Cliente cliente) {
        Cliente clienteBuscar = getClientes().buscar(cliente);
        if (clienteBuscar != null) {
            return new Cliente(clienteBuscar.getNombre(), clienteBuscar.getDni(), clienteBuscar.getTelefono());
        } else {
            return null;
        }
    }

    public Vehiculo buscar(Vehiculo vehiculo) {
        Vehiculo vehiculoBuscar = getVehiculos().buscar(vehiculo);
        if (vehiculoBuscar != null) {
            if (vehiculoBuscar instanceof Turismo) {
                Turismo turismo = (Turismo) vehiculoBuscar;
                return new Turismo(turismo.getMarca(), turismo.getModelo(), turismo.getMatricula(),
                        turismo.getCilindrada());
            } else if (vehiculoBuscar instanceof Furgoneta) {
                Furgoneta furgoneta = (Furgoneta) vehiculoBuscar;
                return new Furgoneta(furgoneta.getMarca(), furgoneta.getModelo(), furgoneta.getPma(),
                        furgoneta.getPlazas(), furgoneta.getMatricula());
            } else {
                Autobus autobus = (Autobus) vehiculoBuscar;
                return new Autobus(autobus.getMarca(), autobus.getModelo(), autobus.getMatricula(),
                        autobus.getPlazas());
            }
        } else {
            return null;
        }
    }

    public Alquiler buscar(Alquiler alquiler) {
        Alquiler alquilerDevolver = getAlquileres().buscar(alquiler);
        if (alquilerDevolver != null) {
            return new Alquiler(alquilerDevolver.getCliente(), alquilerDevolver.getVehiculo(),
                    alquilerDevolver.getFechaAlquiler());
        } else {
            return null;
        }
    }

    public void modificar(Cliente cliente, String nombre, String telefono) throws OperationNotSupportedException {
        getClientes().modificar(cliente, nombre, telefono);
    }

    public void devolver(Cliente cliente, LocalDate fechaDevolucion) throws OperationNotSupportedException {
        if (getClientes().buscar(cliente) == null) {
            throw new OperationNotSupportedException("ERROR: No existe el cliente introducido.");
        }
        getAlquileres().devolver(cliente, fechaDevolucion);
    }

    public void devolver(Vehiculo vehiculo, LocalDate fechaDevolucion) throws OperationNotSupportedException {
        if (getVehiculos().buscar(vehiculo) == null) {
            throw new OperationNotSupportedException("ERROR: No existe el vehiculo introducido.");
        }
        getAlquileres().devolver(vehiculo, fechaDevolucion);
    }

    public void borrar(Cliente cliente) throws OperationNotSupportedException {
        for (Alquiler alquiler : getAlquileres().get(cliente)) {
            getAlquileres().borrar(alquiler);
        }
        getClientes().borrar(cliente);
    }

    public void borrar(Vehiculo vehiculo) throws OperationNotSupportedException {
        for (Alquiler alquiler : getAlquileres().get(vehiculo)) {
            getAlquileres().borrar(alquiler);
        }
        getVehiculos().borrar(vehiculo);
    }

    public void borrar(Alquiler alquiler) throws OperationNotSupportedException {
        getAlquileres().borrar(alquiler);
    }

    public List<Cliente> getListaClientes() {
        List<Cliente> listaClientes = new ArrayList<>();
        for (Cliente cliente : getClientes().get()) {
            listaClientes.add(new Cliente(cliente.getNombre(), cliente.getDni(), cliente.getTelefono()));
        }
        return listaClientes;
    }

    public List<Vehiculo> getListaVehiculos() {
        List<Vehiculo> listaVehiculos = new ArrayList<>();
        for (Vehiculo vehiculo : getVehiculos().get()) {
            if (vehiculo instanceof Turismo) {
                Turismo turismo = (Turismo) vehiculo;
                listaVehiculos.add(new Turismo(turismo.getMarca(), turismo.getModelo(), turismo.getMatricula(),
                        turismo.getCilindrada()));
            }
            if (vehiculo instanceof Furgoneta) {
                Furgoneta furgoneta = (Furgoneta) vehiculo;
                listaVehiculos.add(new Furgoneta(furgoneta.getMarca(), furgoneta.getModelo(), furgoneta.getPma(),
                        furgoneta.getPlazas(), furgoneta.getMatricula()));
            }
            if (vehiculo instanceof Autobus) {
                Autobus autobus = (Autobus) vehiculo;
                listaVehiculos.add(new Autobus(autobus.getMarca(), autobus.getModelo(), autobus.getMatricula(),
                        autobus.getPlazas()));
            }
        }
        return listaVehiculos;
    }

    public List<Alquiler> getListaAlquileres() {
        List<Alquiler> listaAlquileres = new ArrayList<>();
        for (Alquiler alquiler : getAlquileres().get()) {
            listaAlquileres.add(alquiler);
        }
        return listaAlquileres;
    }

    public List<Alquiler> getListaAlquileres(Cliente cliente) {
        List<Alquiler> listaAlquileresCliente = new ArrayList<>();
        for (Alquiler alquiler : getAlquileres().get(cliente)) {
            listaAlquileresCliente.add(alquiler);
        }
        return listaAlquileresCliente;
    }

    public List<Alquiler> getListaAlquileres(Vehiculo vehiculo) {
        List<Alquiler> listaAlquileresVehiculo = new ArrayList<>();
        for (Alquiler alquiler : getAlquileres().get(vehiculo)) {
            listaAlquileresVehiculo.add(alquiler);
        }
        return listaAlquileresVehiculo;
    }

}
