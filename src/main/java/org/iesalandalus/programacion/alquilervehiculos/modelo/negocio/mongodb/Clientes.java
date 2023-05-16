package org.iesalandalus.programacion.alquilervehiculos.modelo.negocio.mongodb;

import static com.mongodb.client.model.Filters.eq;

import java.util.ArrayList;
import java.util.List;

import javax.naming.OperationNotSupportedException;

import org.bson.Document;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Cliente;
import org.iesalandalus.programacion.alquilervehiculos.modelo.negocio.IClientes;
import org.iesalandalus.programacion.alquilervehiculos.modelo.negocio.mongodb.utilidades.MongoDB;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.Updates;

public class Clientes implements IClientes {

	private static final String COLECCION = "clientes";

	private MongoCollection<Document> coleccionClientes;
	private static Clientes instancia;

	private Clientes() {

	}

	static Clientes getInstancia() {
		if (instancia == null)
			instancia = new Clientes();

		return instancia;
	}

	public void comenzar() {
		coleccionClientes = MongoDB.getBD().getCollection(COLECCION);
	}

	public void terminar() {
		MongoDB.cerrarConexion();
	}

	@Override
	public List<Cliente> get() {
		List<Cliente> clientes = new ArrayList<>();

		FindIterable<Document> coleccionClientesOrdenada = coleccionClientes.find().sort(Sorts.ascending(MongoDB.DNI));

		for (Document documentoCliente : coleccionClientesOrdenada) {

			Cliente cliente = MongoDB.getCliente(documentoCliente);
			clientes.add(cliente);
		}
		return clientes;
	}

	public int getCantidad() {
		return (int) coleccionClientes.countDocuments();
	}

	@Override
	public void insertar(Cliente cliente) throws OperationNotSupportedException {
		if (cliente == null)
			throw new NullPointerException("ERROR: No se puede insertar un cliente nulo.");

		if (buscar(cliente) == null)
			coleccionClientes.insertOne(MongoDB.getDocumento(cliente));
		else
			throw new OperationNotSupportedException("ERROR: Ya existe un cliente con ese DNI.");

	}

	@Override
	public Cliente buscar(Cliente cliente) {
		Document documentoCliente = coleccionClientes.find().filter(eq(MongoDB.DNI, cliente.getDni())).first();
		return MongoDB.getCliente(documentoCliente);

	}

	@Override
	public void borrar(Cliente cliente) throws OperationNotSupportedException {
		if (cliente == null)
			throw new NullPointerException("ERROR: No se puede borrar un cliente nulo.");

		if (buscar(cliente) != null) {
			coleccionClientes.deleteOne(eq(MongoDB.DNI, cliente.getDni()));
		} else {
			throw new OperationNotSupportedException("El cliente a borrar no existe.");
		}
	}

	@Override
	public void modificar(Cliente cliente, String nombre, String telefono) throws OperationNotSupportedException {

		if (cliente == null)
			throw new NullPointerException("ERROR: No se puede modificar un cliente nulo.");

		if (buscar(cliente) != null) {
			if (nombre != null && !nombre.isBlank()) {
				coleccionClientes.updateOne(eq(MongoDB.DNI, cliente.getDni()), Updates.set(MongoDB.NOMBRE, nombre));
				Alquileres.modificarNombreCliente(cliente, nombre);
			}
			// Modificar los alquileres de ese cliente
			if (telefono != null && !telefono.isBlank()) {
				coleccionClientes.updateOne(eq(MongoDB.DNI, cliente.getDni()), Updates.set(MongoDB.TELEFONO, telefono));
				Alquileres.modificarTelefonoCliente(cliente, telefono);
			}
		} else
			throw new OperationNotSupportedException("ERROR: No existe ningún cliente con ese DNI.");

	}

}
