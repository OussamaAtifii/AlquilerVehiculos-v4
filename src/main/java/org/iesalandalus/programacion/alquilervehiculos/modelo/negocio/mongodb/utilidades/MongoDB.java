package org.iesalandalus.programacion.alquilervehiculos.modelo.negocio.mongodb.utilidades;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.OperationNotSupportedException;

import org.bson.Document;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Alquiler;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Autobus;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Cliente;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Furgoneta;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Turismo;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Vehiculo;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.MongoException;
import com.mongodb.ServerAddress;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoDB {

	public static final DateTimeFormatter FORMATO_DIA = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	public static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("HH:mm");

	// private static final String SERVIDOR="localhost";
	private static final String SERVIDOR = "cluster0.uyprv4n.mongodb.net";
	private static final int PUERTO = 27017;
	private static final String BD = "alquilervehiculos";
	private static final String USUARIO = "alquilervehiculos";
	private static final String CONTRASENA = "alquilervehiculos-2023";

	public static final String CLIENTE = "cliente";
	public static final String NOMBRE = "nombre";
	public static final String DNI = "dni";
	public static final String TELEFONO = "telefono";
	public static final String CLIENTE_DNI = CLIENTE + "." + DNI;
	public static final String CLIENTE_NOMBRE = CLIENTE + "." + NOMBRE;
	public static final String CLIENTE_TELEFONO = CLIENTE + "." + TELEFONO;

	public static final String VEHICULO = "vehiculo";
	public static final String MARCA = "marca";
	public static final String MODELO = "modelo";
	public static final String MATRICULA = "matricula";
	public static final String TIPO = "tipo";
	public static final String TIPO_TURISMO = "turismo";
	public static final String TIPO_AUTOBUS = "autobus";
	public static final String TIPO_FURGONETA = "furgoneta";
	public static final String CILINDRADA = "cilindrada";
	public static final String PMA = "pma";
	public static final String PLAZAS = "plazas";
	public static final String VEHICULO_MATRICULA = VEHICULO + "." + MATRICULA;

	public static final String FECHA_ALQUILER = "fecha_alquiler";
	public static final String FECHA_DEVOLUCION = "fecha_devolucion";

	private static MongoClient conexion = null;

	private MongoDB() {
		// Evitamos que se cree el constructor por defecto
	}

	public static MongoDatabase getBD() {
		if (conexion == null) {
			establecerConexion();
		}

		return conexion.getDatabase(BD);
	}

	/*
	 * private static MongoClient establecerConexion() {
	 * Logger mongoLogger = Logger.getLogger( "org.mongodb.driver" );
	 * mongoLogger.setLevel(Level.SEVERE);
	 * if (conexion == null) {
	 * MongoCredential credenciales =
	 * MongoCredential.createScramSha1Credential(USUARIO, BD,
	 * CONTRASENA.toCharArray());
	 * conexion = MongoClients.create(
	 * MongoClientSettings.builder()
	 * .applyToClusterSettings(builder ->
	 * builder.hosts(Arrays.asList(new ServerAddress(SERVIDOR, PUERTO))))
	 * .credential(credenciales)
	 * .build());
	 * System.out.println("Conexión a MongoDB realizada correctamente.");
	 * }
	 * 
	 * 
	 * return conexion;
	 * }
	 */
	private static void establecerConexion() {

		String connectionString;
		ServerApi serverApi;
		MongoClientSettings settings;

		if (!SERVIDOR.equals("localhost")) {
			connectionString = "mongodb+srv://" + USUARIO + ":" + CONTRASENA + "@" + SERVIDOR
					+ "/?retryWrites=true&w=majority";
			serverApi = ServerApi.builder()
					.version(ServerApiVersion.V1)
					.build();

			settings = MongoClientSettings.builder()
					.applyConnectionString(new ConnectionString(connectionString))
					.serverApi(serverApi)
					.build();
		} else {
			connectionString = "mongodb://" + USUARIO + ":" + CONTRASENA + "@" + SERVIDOR + ":" + PUERTO;
			MongoCredential credenciales = MongoCredential.createScramSha1Credential(USUARIO, BD,
					CONTRASENA.toCharArray());

			settings = MongoClientSettings.builder()
					.applyConnectionString(new ConnectionString(connectionString))
					.credential(credenciales)
					.build();
		}

		// Creamos la conexión con el serveridos según el setting anterior
		conexion = MongoClients.create(settings);

		try {
			if (!SERVIDOR.equals("localhost")) {
				MongoDatabase database = conexion.getDatabase(BD);
				database.runCommand(new Document("ping", 1));
			}
		} catch (MongoException e) {
			e.printStackTrace();

		}

		System.out.println("Conexión a MongoDB realizada correctamente.");

	}

	public static void cerrarConexion() {
		if (conexion != null) {
			conexion.close();
			conexion = null;
			System.out.println("Conexión a MongoDB cerrada.");
		}
	}

	public static Document getDocumento(Cliente cliente) {
		if (cliente == null) {
			return null;
		}
		String nombre = cliente.getNombre();
		String dni = cliente.getDni();
		String telefono = cliente.getTelefono();
		return new Document().append(NOMBRE, nombre).append(DNI, dni).append(TELEFONO, telefono);
	}

	public static Cliente getCliente(Document documentoCliente) {
		if (documentoCliente == null) {
			return null;
		}
		return new Cliente(documentoCliente.getString(NOMBRE), documentoCliente.getString(DNI),
				documentoCliente.getString(TELEFONO));
	}

	public static Document getDocumento(Vehiculo vehiculo) {
		if (vehiculo == null) {
			return null;
		}
		String marca = vehiculo.getMarca();
		String modelo = vehiculo.getModelo();
		String matricula = vehiculo.getMatricula();
		Document dVehiculo = new Document().append(MARCA, marca).append(MODELO, modelo).append(MATRICULA, matricula);

		if (vehiculo instanceof Turismo) {
			int cilindrada = ((Turismo) vehiculo).getCilindrada();
			dVehiculo.append(TIPO, TIPO_TURISMO).append(CILINDRADA, cilindrada);

		} else if (vehiculo instanceof Autobus) {
			int nPlazas = ((Autobus) vehiculo).getPlazas();
			dVehiculo.append(TIPO, TIPO_AUTOBUS).append(PLAZAS, nPlazas);
		} else if (vehiculo instanceof Furgoneta) {
			int nPlazas = ((Furgoneta) vehiculo).getPlazas();
			int pma = ((Furgoneta) vehiculo).getPma();
			dVehiculo.append(TIPO, TIPO_FURGONETA).append(PLAZAS, nPlazas).append(PMA, pma);
		}

		return dVehiculo;
	}

	public static Vehiculo getVehiculo(Document documentoVehiculo) {
		Vehiculo vehiculo = null;

		if (documentoVehiculo == null) {
			return null;
		}

		String tipo = documentoVehiculo.getString(TIPO);
		if (tipo.equals(TIPO_TURISMO)) {
			vehiculo = new Turismo(documentoVehiculo.getString(MARCA), documentoVehiculo.getString(MODELO),
					documentoVehiculo.getString(MATRICULA), documentoVehiculo.getInteger(CILINDRADA));
		} else if (tipo.equals(TIPO_AUTOBUS)) {
			vehiculo = new Autobus(documentoVehiculo.getString(MARCA), documentoVehiculo.getString(MODELO),
					documentoVehiculo.getString(MATRICULA), documentoVehiculo.getInteger(PLAZAS));
		} else if (tipo.equals(TIPO_FURGONETA))
			vehiculo = new Furgoneta(documentoVehiculo.getString(MARCA), documentoVehiculo.getString(MODELO),
					documentoVehiculo.getInteger(PMA), documentoVehiculo.getInteger(PLAZAS),
					documentoVehiculo.getString(MATRICULA));

		return vehiculo;

	}

	public static Document getDocumento(Alquiler alquiler) {
		String fechaDevolucion = "";

		if (alquiler == null) {
			return null;
		}

		Cliente cliente = alquiler.getCliente();
		Vehiculo vehiculo = alquiler.getVehiculo();
		String fechaAlquiler = alquiler.getFechaAlquiler().format(FORMATO_DIA);

		if (alquiler.getFechaDevolucion() != null)
			fechaDevolucion = alquiler.getFechaDevolucion().format(FORMATO_DIA);

		Document dCliente = getDocumento(cliente);
		Document dVehiculo = getDocumento(vehiculo);

		return new Document().append(CLIENTE, dCliente).append(VEHICULO, dVehiculo)
				.append(FECHA_ALQUILER, fechaAlquiler).append(FECHA_DEVOLUCION, fechaDevolucion);
	}

	public static Alquiler getAlquiler(Document documentoAlquiler) {
		if (documentoAlquiler == null) {
			return null;
		}

		/*
		 * Document dCliente=(Document) documentoAlquiler.get(CLIENTE);
		 * String dni=dCliente.getString(DNI);
		 * Cliente cliente=Cliente.getClienteConDni(dni);
		 * Cliente clienteAlquiler=Clientes.getInstancia().buscar(cliente);
		 */

		Document dCliente = (Document) documentoAlquiler.get(CLIENTE);
		Cliente cliente = getCliente(dCliente);

		Document dVehiculo = (Document) documentoAlquiler.get(VEHICULO);
		Vehiculo vehiculo = getVehiculo(dVehiculo);

		LocalDate fechaAlquiler = LocalDate.parse(documentoAlquiler.getString(FECHA_ALQUILER), FORMATO_DIA);

		String fechaDevolucion = documentoAlquiler.getString(FECHA_DEVOLUCION);

		Alquiler alquiler = new Alquiler(cliente, vehiculo, fechaAlquiler);

		if (!fechaDevolucion.isEmpty())
			try {
				alquiler.devolver(LocalDate.parse(fechaDevolucion, FORMATO_DIA));
			} catch (OperationNotSupportedException e) {
				System.out.println(e.getMessage());
			}

		return alquiler;

	}

}
