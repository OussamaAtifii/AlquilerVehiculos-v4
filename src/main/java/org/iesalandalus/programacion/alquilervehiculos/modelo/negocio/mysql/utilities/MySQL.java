package org.iesalandalus.programacion.alquilervehiculos.modelo.negocio.mysql.utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL {

	// private static final String HOST = "35.246.226.125";
	private static final String HOST = "alquilervehiculos.csxs4ksizjjv.us-east-1.rds.amazonaws.com";
	private static final String ESQUEMA = "alquilervehiculos";
	private static final String USUARIO = "admin";
	private static final String CONTRASENA = "alquilervehiculos-2023";

	private static Connection conexion = null;

	private MySQL() {
		// Evitamos que se creen instancias
	}

	public static Connection establecerConexion() {
		if (conexion != null)
			return conexion;
		try {
			String urlConexion = String.format("jdbc:mysql://%s/%s?user=%s&password=%s&useSSL=false&serverTimezon=UTC",
					HOST, ESQUEMA, USUARIO, CONTRASENA);
			conexion = DriverManager.getConnection(urlConexion);
			System.out.println("Conexión a MySQL realizada correctamente.");
		} catch (SQLException e) {
			System.out.println("ERROR MySQL:  " + e.toString());
		}
		return conexion;
	}

	public static void cerrarConexion() {
		try {
			if (conexion != null) {
				conexion.close();
				conexion = null;
				System.out.println("Conexión a MySQL cerrada correctamente.");
			}
		} catch (SQLException e) {
			System.out.println("ERROR MySQL: " + e.toString());
		}
	}

}
