package org.iesalandalus.programacion.alquilervehiculos.modelo.negocio.mongodb;

import static com.mongodb.client.model.Filters.eq;

import java.util.ArrayList;
import java.util.List;

import javax.naming.OperationNotSupportedException;

import org.bson.Document;

import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Vehiculo;
import org.iesalandalus.programacion.alquilervehiculos.modelo.negocio.IVehiculos;
import org.iesalandalus.programacion.alquilervehiculos.modelo.negocio.mongodb.utilidades.MongoDB;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Sorts;

public class Vehiculos implements IVehiculos {

	private static final String COLECCION = "vehiculos";

	private MongoCollection<Document> coleccionVehiculos;
	private static Vehiculos instancia;

	private Vehiculos() {

	}

	static Vehiculos getInstancia() {
		if (instancia == null)
			instancia = new Vehiculos();

		return instancia;
	}

	public void comenzar() {
		coleccionVehiculos = MongoDB.getBD().getCollection(COLECCION);
	}

	public void terminar() {
		MongoDB.cerrarConexion();
	}

	@Override
	public List<Vehiculo> get() {
		List<Vehiculo> vehiculos = new ArrayList<>();

		FindIterable<Document> coleccionVehiculosOrdenada = coleccionVehiculos.find()
				.sort(Sorts.ascending(MongoDB.MATRICULA));

		for (Document documentoVehiculo : coleccionVehiculosOrdenada) {
			Vehiculo vehiculo = MongoDB.getVehiculo(documentoVehiculo);
			vehiculos.add(vehiculo);
		}
		return vehiculos;
	}

	public int getCantidad() {
		return (int) coleccionVehiculos.countDocuments();
	}

	@Override
	public void insertar(Vehiculo vehiculo) throws OperationNotSupportedException {
		if (vehiculo == null)
			throw new NullPointerException("ERROR: No se puede insertar un vehículo nulo.");

		if (buscar(vehiculo) == null)
			coleccionVehiculos.insertOne(MongoDB.getDocumento(vehiculo));
		else
			throw new OperationNotSupportedException("ERROR: Ya existe un vehículo con esa matrícula.");

	}

	@Override
	public Vehiculo buscar(Vehiculo vehiculo) {
		Document documentoVehiculo = coleccionVehiculos.find().filter(eq(MongoDB.MATRICULA, vehiculo.getMatricula()))
				.first();
		return MongoDB.getVehiculo(documentoVehiculo);

	}

	@Override
	public void borrar(Vehiculo vehiculo) throws OperationNotSupportedException {
		if (vehiculo == null)
			throw new NullPointerException("ERROR: No se puede borrar un vehículo nulo.");

		if (buscar(vehiculo) != null)
			coleccionVehiculos.deleteOne(eq(MongoDB.MATRICULA, vehiculo.getMatricula()));
		else
			throw new OperationNotSupportedException("El vehículo a borrar no existe.");

	}

}
