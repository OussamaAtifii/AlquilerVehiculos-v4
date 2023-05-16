package org.iesalandalus.programacion.alquilervehiculos.modelo.negocio.mysql;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.naming.OperationNotSupportedException;

import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Alquiler;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Cliente;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Vehiculo;
import org.iesalandalus.programacion.alquilervehiculos.modelo.negocio.IAlquileres;
import org.iesalandalus.programacion.alquilervehiculos.modelo.negocio.mysql.utilities.MySQL;

public class Alquileres implements IAlquileres {

	private Connection conexion = null;

	private static Alquileres instancia;

	private Alquileres() {

	}

	static Alquileres getInstancia() {
		if (instancia == null)
			instancia = new Alquileres();

		return instancia;

	}

	public void comenzar() {
		conexion = MySQL.establecerConexion();
	}

	public void terminar() {
		MySQL.cerrarConexion();
	}

	@Override
	public List<Alquiler> get() {
		List<Alquiler> alquileres = new ArrayList<>();

		try {
			String sentenciaStr = "select dni, matricula, fecha_alquiler, fecha_devolucion from alquileres order by fecha_alquiler";
			Statement sentencia = conexion.createStatement();
			ResultSet filas = sentencia.executeQuery(sentenciaStr);

			while (filas.next()) {
				String dni = filas.getString(1);
				String matricula = filas.getString(2);
				LocalDate fechaAlquiler = filas.getDate(3).toLocalDate();

				Cliente cliente = Clientes.getInstancia().buscar(Cliente.getClienteConDni(dni));

				Vehiculo vehiculo = Vehiculos.getInstancia().buscar(Vehiculo.getVehiculoConMatricula(matricula));

				Alquiler alquiler = new Alquiler(cliente, vehiculo, fechaAlquiler);

				if (filas.getDate(4) != null) {
					alquiler.devolver(filas.getDate(4).toLocalDate());
				}

				alquileres.add(alquiler);
			}
		} catch (SQLException e) {
			throw new IllegalArgumentException("ERROR:" + e.getMessage());
		} catch (OperationNotSupportedException e) {
			System.out.println(e.getMessage());
		}
		return alquileres;
	}

	public int getCantidad() {
		int tamano = 0;
		try {
			String sentenciaStr = "select count(*) from alquileres";
			Statement sentencia = conexion.createStatement();
			ResultSet filas = sentencia.executeQuery(sentenciaStr);
			if (filas.next()) {
				tamano = filas.getInt(1);
			}
		} catch (SQLException e) {
			throw new IllegalArgumentException("ERROR:" + e.getMessage());
		}

		return tamano;
	}

	@Override
	public void insertar(Alquiler alquiler) throws OperationNotSupportedException {
		if (alquiler == null)
			throw new NullPointerException("ERROR: No se puede insertar un alquiler nulo.");

		comprobarAlquiler(alquiler.getCliente(), alquiler.getVehiculo(), alquiler.getFechaAlquiler());

		try {
			String sentenciaStr = "insert into alquileres values (null, ?, ?, ?, null)";
			PreparedStatement sentencia = conexion.prepareStatement(sentenciaStr);
			sentencia.setString(1, alquiler.getCliente().getDni());
			sentencia.setString(2, alquiler.getVehiculo().getMatricula());
			sentencia.setDate(3, Date.valueOf(alquiler.getFechaAlquiler().plusDays(1)));
			sentencia.executeUpdate();
		} catch (SQLIntegrityConstraintViolationException e) {
			throw new OperationNotSupportedException("ERROR: Ya existe un alquiler igual.");
		} catch (SQLException e) {
			throw new OperationNotSupportedException("ERROR:" + e.getMessage());
		}

	}

	@Override
	public Alquiler buscar(Alquiler alquiler) {
		Alquiler alquilerBuscado = null;

		if (alquiler == null)
			throw new IllegalArgumentException("ERROR: No se puede buscar un alquiler nulo.");

		try {
			String sentenciaStr = "select dni, matricula, fecha_alquiler, fecha_devolucion from alquileres where dni=? and matricula=? and fecha_alquiler='"
					+ alquiler.getFechaAlquiler() + "'";
			PreparedStatement sentencia = conexion.prepareStatement(sentenciaStr);
			sentencia.setString(1, alquiler.getCliente().getDni());
			sentencia.setString(2, alquiler.getVehiculo().getMatricula());
			// sentencia.setDate(3, Date.valueOf(alquiler.getFechaAlquiler()));
			ResultSet filas = sentencia.executeQuery();
			if (filas.next()) {
				String dni = filas.getString(1);
				Cliente cliente = Clientes.getInstancia().buscar(Cliente.getClienteConDni(dni));
				String matricula = filas.getString(2);
				Vehiculo vehiculo = Vehiculos.getInstancia().buscar(Vehiculo.getVehiculoConMatricula(matricula));
				LocalDate fechaAlquiler = filas.getDate(3).toLocalDate();
				LocalDate fechaDevolucion = filas.getDate(4).toLocalDate();
				alquilerBuscado = new Alquiler(cliente, vehiculo, fechaAlquiler);
				if (fechaDevolucion != null)
					alquilerBuscado.devolver(fechaDevolucion);
			}
		} catch (SQLException e) {
			throw new IllegalArgumentException("ERROR:" + e.getMessage());
		} catch (OperationNotSupportedException e) {
			System.out.println(e.getMessage());
		}

		return alquilerBuscado;

	}

	@Override
	public void borrar(Alquiler alquiler) throws OperationNotSupportedException {
		if (alquiler == null)
			throw new NullPointerException("ERROR: No se puede borrar un alquiler nulo.");

		try {
			String sentenciaStr = "delete from alquileres where fecha_alquiler='" + alquiler.getFechaAlquiler()
					+ "' and dni = ? and matricula= ?";
			PreparedStatement sentencia = conexion.prepareStatement(sentenciaStr);

			sentencia.setString(1, alquiler.getCliente().getDni());
			sentencia.setString(2, alquiler.getVehiculo().getMatricula());

			if (sentencia.executeUpdate() == 0)
				throw new OperationNotSupportedException("ERROR: No existe ningún alquiler con los datos indicados.");

		} catch (SQLException e) {
			throw new OperationNotSupportedException("ERROR:" + e.toString());
		}
	}

	@Override
	public List<Alquiler> get(Cliente cliente) {
		List<Alquiler> alquileresCliente = new ArrayList<>();

		try {
			String sentenciaStr = "select dni, matricula, fecha_alquiler, fecha_devolucion from alquileres where dni=?";
			PreparedStatement sentencia = conexion.prepareStatement(sentenciaStr);
			sentencia.setString(1, cliente.getDni());

			ResultSet filas = sentencia.executeQuery();
			while (filas.next()) {
				String dni = filas.getString(1);
				String matricula = filas.getString(2);
				LocalDate fechaAlquiler = filas.getDate(3).toLocalDate();

				Cliente clienteBuscar = Clientes.getInstancia().buscar(Cliente.getClienteConDni(dni));

				Vehiculo vehiculo = Vehiculos.getInstancia().buscar(Vehiculo.getVehiculoConMatricula(matricula));

				Alquiler alquiler = new Alquiler(clienteBuscar, vehiculo, fechaAlquiler);

				if (filas.getDate(4) != null) {
					alquiler.devolver(filas.getDate(4).toLocalDate());
				}

				alquileresCliente.add(alquiler);
			}
		} catch (SQLException e) {
			throw new IllegalArgumentException("ERROR:" + e.getMessage());
		} catch (OperationNotSupportedException e) {
			System.out.println(e.getMessage());
		}

		return alquileresCliente;
	}

	@Override
	public List<Alquiler> get(Vehiculo vehiculo) {
		List<Alquiler> alquileresVehiculo = new ArrayList<>();

		try {
			String sentenciaStr = "select dni, matricula, fecha_alquiler, fecha_devolucion from alquileres where matricula=?";
			PreparedStatement sentencia = conexion.prepareStatement(sentenciaStr);
			sentencia.setString(1, vehiculo.getMatricula());

			ResultSet filas = sentencia.executeQuery();
			while (filas.next()) {
				String dni = filas.getString(1);
				String matricula = filas.getString(2);
				LocalDate fechaAlquiler = filas.getDate(3).toLocalDate();

				Cliente cliente = Clientes.getInstancia().buscar(Cliente.getClienteConDni(dni));

				Vehiculo vehiculoBuscado = Vehiculos.getInstancia().buscar(Vehiculo.getVehiculoConMatricula(matricula));

				Alquiler alquiler = new Alquiler(cliente, vehiculoBuscado, fechaAlquiler);

				if (filas.getDate(4) != null) {
					alquiler.devolver(filas.getDate(4).toLocalDate());
				}

				alquileresVehiculo.add(alquiler);
			}
		} catch (SQLException e) {
			throw new IllegalArgumentException("ERROR:" + e.getMessage());
		} catch (OperationNotSupportedException e) {
			System.out.println(e.getMessage());
		}

		return alquileresVehiculo;
	}

	private void comprobarAlquiler(Cliente cliente, Vehiculo vehiculo, LocalDate fechaAlquiler)
			throws OperationNotSupportedException {
		List<Alquiler> listaAlquileresCliente = new ArrayList<>();

		listaAlquileresCliente = get(cliente);

		for (Alquiler a : listaAlquileresCliente) {
			if (a.getCliente().equals(cliente)) {
				if (a.getFechaDevolucion() == null)
					throw new OperationNotSupportedException("ERROR: El cliente tiene otro alquiler sin devolver.");
				else if (a.getFechaDevolucion().isAfter(fechaAlquiler) || a.getFechaDevolucion().equals(fechaAlquiler))
					throw new OperationNotSupportedException("ERROR: El cliente tiene un alquiler posterior.");

			}

			if (a.getVehiculo().equals(vehiculo)) {
				if (a.getFechaDevolucion() == null)
					throw new OperationNotSupportedException("ERROR: El vehículo está actualmente alquilado.");
				else if (a.getFechaDevolucion().isAfter(fechaAlquiler) || a.getFechaDevolucion().equals(fechaAlquiler))
					throw new OperationNotSupportedException("ERROR: El vehículo tiene un alquiler posterior.");
			}
		}

	}

	@Override
	public void devolver(Cliente cliente, LocalDate fechaDevolucion) throws OperationNotSupportedException {
		if (cliente == null)
			throw new NullPointerException("ERROR: No se puede devolver un alquiler de un cliente nulo.");

		if (fechaDevolucion == null)
			throw new NullPointerException("ERROR: No se puede devolver un alquiler con una fecha de devolución nula.");

		Alquiler alquiler = getAlquilerAbierto(cliente);
		if (alquiler == null)
			throw new OperationNotSupportedException("ERROR: No existe ningún alquiler abierto para ese cliente.");

		modificarFechaDevolucion(alquiler, fechaDevolucion);

	}

	private void modificarFechaDevolucion(Alquiler alquiler, LocalDate fechaDevolucion) {
		if (alquiler == null)
			throw new NullPointerException("ERROR: No se puede modificar un cliente nulo.");

		if (fechaDevolucion == null)
			throw new NullPointerException("ERROR: No se puede devolver un alquiler con una fecha de devolución nula.");

		try {
			String sentenciaStr = "update alquileres set fecha_devolucion='" + fechaDevolucion
					+ "' where dni = ? and matricula=? and fecha_alquiler='" + alquiler.getFechaAlquiler() + "'";

			PreparedStatement sentencia = conexion.prepareStatement(sentenciaStr);
			sentencia.setString(1, alquiler.getCliente().getDni());
			sentencia.setString(2, alquiler.getVehiculo().getMatricula());

			if (sentencia.executeUpdate() == 0) {
				throw new OperationNotSupportedException("ERROR: No existe ningún alquiler con los datos indicados.");
			}
		} catch (SQLException e) {
			throw new IllegalArgumentException("ERROR:" + e.getMessage());
		} catch (OperationNotSupportedException e) {
			System.out.println(e.getMessage());
		}

	}

	private Alquiler getAlquilerAbierto(Cliente cliente) {
		Alquiler alquilerEncontrado = null;
		List<Alquiler> listaAlquileresCliente = new ArrayList<>();

		if (cliente == null)
			throw new NullPointerException("ERROR: No se puede devolver un alquiler de un cliente nulo.");

		listaAlquileresCliente = get(cliente);

		Iterator<Alquiler> iterator = listaAlquileresCliente.iterator();
		while (iterator.hasNext() && alquilerEncontrado == null) {
			Alquiler alquiler = iterator.next();
			if (alquiler.getCliente().equals(cliente) && alquiler.getFechaDevolucion() == null)
				alquilerEncontrado = alquiler;
		}

		return alquilerEncontrado;
	}

	@Override
	public void devolver(Vehiculo vehiculo, LocalDate fechaDevolucion) throws OperationNotSupportedException {
		if (vehiculo == null)
			throw new NullPointerException("ERROR: No se puede devolver un alquiler de un vehículo nulo.");

		if (fechaDevolucion == null)
			throw new NullPointerException("ERROR: No se puede devolver un alquiler con una fecha de devolución nula.");

		Alquiler alquiler = getAlquilerAbierto(vehiculo);
		if (alquiler == null)
			throw new OperationNotSupportedException("ERROR: No existe ningún alquiler abierto para ese vehículo.");

		modificarFechaDevolucion(alquiler, fechaDevolucion);
	}

	private Alquiler getAlquilerAbierto(Vehiculo vehiculo) {
		Alquiler alquilerEncontrado = null;
		List<Alquiler> listaAlquileresVehiculo = new ArrayList<>();

		if (vehiculo == null)
			throw new NullPointerException("ERROR: No se puede devolver un alquiler de un vehiculo nulo.");

		listaAlquileresVehiculo = get(vehiculo);

		Iterator<Alquiler> iterator = listaAlquileresVehiculo.iterator();
		while (iterator.hasNext() && alquilerEncontrado == null) {
			Alquiler alquiler = iterator.next();
			if (alquiler.getVehiculo().equals(vehiculo) && alquiler.getFechaDevolucion() == null)
				alquilerEncontrado = alquiler;
		}

		return alquilerEncontrado;
	}

}
