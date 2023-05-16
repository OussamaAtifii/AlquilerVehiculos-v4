package org.iesalandalus.programacion.alquilervehiculos;

import java.io.IOException;

import javax.naming.OperationNotSupportedException;
import javax.xml.parsers.ParserConfigurationException;

import org.iesalandalus.programacion.alquilervehiculos.controlador.Controlador;
import org.iesalandalus.programacion.alquilervehiculos.modelo.FactoriaFuenteDatos;
import org.iesalandalus.programacion.alquilervehiculos.modelo.Modelo;
import org.iesalandalus.programacion.alquilervehiculos.modelo.ModeloCascada;
import org.iesalandalus.programacion.alquilervehiculos.vista.Vista;
import org.iesalandalus.programacion.alquilervehiculos.vista.grafica.VistaGrafica;
import org.iesalandalus.programacion.alquilervehiculos.vista.texto.VistaTexto;
import org.xml.sax.SAXException;

public class MainApp {
	static Modelo modelo = null;
	static Vista vista = null;
	static Controlador controlador = null;

	public static void main(String[] args) {
		// Ánimo!!!!
		ejecutarOpcion(args);

		controlador = new Controlador(modelo, vista);
		try {
			controlador.comenzar();
		} catch (OperationNotSupportedException e) {
			System.out.println(e.getMessage());
		} catch (ParserConfigurationException e) {
			System.out.println("ERROR: Error en la construccion del lector.");
		} catch (SAXException e) {
			System.out.println("ERROR: Error en la conversion del lector.");
		} catch (IOException e) {
			System.out.println("ERROR: Error en el acceso al fichero de origen.");
		}
	}

	private static void ejecutarOpcion(String[] args) {
		if (args.length == 1) {
			switch (args[0]) {
				case "memoria-texto":
					modelo = new ModeloCascada(FactoriaFuenteDatos.MEMORIA);
					vista = new VistaTexto();
					break;
				case "memoria-grafica":
					modelo = new ModeloCascada(FactoriaFuenteDatos.MEMORIA);
					vista = new VistaGrafica();
					break;
				case "fichero-texto":
					modelo = new ModeloCascada(FactoriaFuenteDatos.FICHEROS);
					vista = new VistaTexto();
					break;
				case "fichero-grafica":
					modelo = new ModeloCascada(FactoriaFuenteDatos.FICHEROS);
					vista = new VistaGrafica();
					break;
				case "mysql-texto":
					modelo = new ModeloCascada(FactoriaFuenteDatos.MYSQL);
					vista = new VistaTexto();
					break;
				case "mysql-grafica":
					modelo = new ModeloCascada(FactoriaFuenteDatos.MYSQL);
					vista = new VistaGrafica();
					break;
				default:
					modelo = new ModeloCascada(FactoriaFuenteDatos.MYSQL);
					vista = new VistaGrafica();
					System.err.println("Argumento no valido: " + args[0]);
					System.out.println("Se ejecutara la aplicacion en el modo por defecto.");
			}
		} else {
			modelo = new ModeloCascada(FactoriaFuenteDatos.MYSQL);
			vista = new VistaGrafica();
		}
	}

}
