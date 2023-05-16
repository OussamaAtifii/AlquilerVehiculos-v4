package org.iesalandalus.programacion.alquilervehiculos.modelo.negocio.mongodb;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.naming.OperationNotSupportedException;

import org.bson.Document;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Alquiler;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Cliente;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Vehiculo;
import org.iesalandalus.programacion.alquilervehiculos.modelo.negocio.IAlquileres;
import org.iesalandalus.programacion.alquilervehiculos.modelo.negocio.mongodb.utilidades.MongoDB;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.Updates;

public class Alquileres implements IAlquileres {

	private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd-MM-yyyy");

	private static final String COLECCION = "alquileres";

	private MongoCollection<Document> coleccionAlquileres;
	private static Alquileres instancia;

	private Alquileres() {

	}

	static Alquileres getInstancia() {
		if (instancia == null)
			instancia = new Alquileres();

		return instancia;

	}

	public void comenzar() {
		coleccionAlquileres = MongoDB.getBD().getCollection(COLECCION);
	}

	public void terminar() {
		MongoDB.cerrarConexion();
	}

	@Override
	public List<Alquiler> get() {
		List<Alquiler> alquileres = new ArrayList<>();

		FindIterable<Document> coleccionAlquileresOrdenada = coleccionAlquileres.find()
				.sort(Sorts.ascending(MongoDB.FECHA_ALQUILER, MongoDB.CLIENTE_DNI));

		for (Document documentoAlquiler : coleccionAlquileresOrdenada) {
			Alquiler alquiler = MongoDB.getAlquiler(documentoAlquiler);
			alquileres.add(alquiler);
		}

		return alquileres;
	}

	public int getCantidad() {
		return (int) coleccionAlquileres.countDocuments();
	}

	@Override
	public void insertar(Alquiler alquiler) throws OperationNotSupportedException {
		if (alquiler == null)
			throw new NullPointerException("ERROR: No se puede insertar un alquiler nulo.");

		if (buscar(alquiler) == null) {
			comprobarAlquiler(alquiler.getCliente(), alquiler.getVehiculo(), alquiler.getFechaAlquiler());
			coleccionAlquileres.insertOne(MongoDB.getDocumento(alquiler));
		}

		else
			throw new OperationNotSupportedException("ERROR: Ya existe un alquiler en la base de datos.");

	}

	@Override
	public Alquiler buscar(Alquiler alquiler) {

		Document documentoAlquiler = coleccionAlquileres.find().filter(
				and(
						eq(MongoDB.FECHA_ALQUILER, alquiler.getFechaAlquiler().format(FORMATO_FECHA)),
						eq(MongoDB.CLIENTE_DNI, alquiler.getCliente().getDni()),
						eq(MongoDB.VEHICULO_MATRICULA, alquiler.getVehiculo().getMatricula().toString())))
				.first();

		return MongoDB.getAlquiler(documentoAlquiler);

	}

	@Override
	public void borrar(Alquiler alquiler) throws OperationNotSupportedException {
		if (alquiler == null)
			throw new NullPointerException("ERROR: No se puede borrar un alquiler nulo.");

		if (buscar(alquiler) != null) {
			coleccionAlquileres.deleteOne(
					and(
							eq(MongoDB.FECHA_ALQUILER, alquiler.getFechaAlquiler().format(FORMATO_FECHA)),
							eq(MongoDB.CLIENTE_DNI, alquiler.getCliente().getDni()),
							eq(MongoDB.VEHICULO_MATRICULA, alquiler.getVehiculo().getMatricula())));
		} else
			throw new OperationNotSupportedException("El alquiler a borrar no existe.");
	}

	@Override
	public List<Alquiler> get(Cliente cliente) {
		List<Alquiler> listaAlquileresCliente = new ArrayList<>();

		for (Document documentoAlquiler : coleccionAlquileres.find()
				.filter(eq(MongoDB.CLIENTE_DNI, cliente.getDni()))) {
			listaAlquileresCliente.add(MongoDB.getAlquiler(documentoAlquiler));
		}

		// ordenarPorPermanencia(reservasAula);

		return listaAlquileresCliente;
	}

	@Override
	public List<Alquiler> get(Vehiculo vehiculo) {
		List<Alquiler> listaAlquileresVehiculo = new ArrayList<>();

		for (Document documentoAlquiler : coleccionAlquileres.find()
				.filter(eq(MongoDB.VEHICULO_MATRICULA, vehiculo.getMatricula()))) {
			listaAlquileresVehiculo.add(MongoDB.getAlquiler(documentoAlquiler));
		}

		return listaAlquileresVehiculo;
	}

	private void comprobarAlquiler(Cliente cliente, Vehiculo vehiculo, LocalDate fechaAlquiler)
			throws OperationNotSupportedException {
		List<Alquiler> listaAlquileres = new ArrayList<>();

		listaAlquileres = get();

		for (Alquiler a : listaAlquileres) {
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

		coleccionAlquileres.updateOne(
				and(
						eq(MongoDB.FECHA_ALQUILER, alquiler.getFechaAlquiler().format(FORMATO_FECHA)),
						eq(MongoDB.CLIENTE_DNI, alquiler.getCliente().getDni()),
						eq(MongoDB.VEHICULO_MATRICULA, alquiler.getVehiculo().getMatricula())),
				Updates.set(MongoDB.FECHA_DEVOLUCION, fechaDevolucion.format(FORMATO_FECHA)));

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

	public static void modificarNombreCliente(Cliente cliente, String nombre) {
		getInstancia().coleccionAlquileres.updateOne(eq(MongoDB.CLIENTE_DNI, cliente.getDni()),
				Updates.set(MongoDB.CLIENTE_NOMBRE, nombre));
	}

	public static void modificarTelefonoCliente(Cliente cliente, String telefono) {
		getInstancia().coleccionAlquileres.updateOne(eq(MongoDB.CLIENTE_DNI, cliente.getDni()),
				Updates.set(MongoDB.CLIENTE_TELEFONO, telefono));
	}

}
