package org.iesalandalus.programacion.alquilervehiculos;

import java.io.IOException;

import javax.naming.OperationNotSupportedException;
import javax.xml.parsers.ParserConfigurationException;

import org.iesalandalus.programacion.alquilervehiculos.controlador.Controlador;
import org.iesalandalus.programacion.alquilervehiculos.controlador.IControlador;
import org.iesalandalus.programacion.alquilervehiculos.modelo.FactoriaFuenteDatos;
import org.iesalandalus.programacion.alquilervehiculos.modelo.Modelo;
import org.iesalandalus.programacion.alquilervehiculos.modelo.ModeloCascada;
import org.iesalandalus.programacion.alquilervehiculos.vista.grafica.VistaGrafica;
import org.xml.sax.SAXException;

public class MainApp {

	public static void main(String[] args) {
		// �nimo!!!!
		Modelo modelo = new ModeloCascada(FactoriaFuenteDatos.FICHEROS);
		VistaGrafica vista = new VistaGrafica();
		IControlador controlador = new Controlador(modelo, vista);
		try {
			controlador.comenzar();
		} catch (OperationNotSupportedException e) {
			System.out.println(e.getMessage());
		} catch (ParserConfigurationException e) {
			System.out.println("ERROR: Error en la construcci�n del lector.");
		} catch (SAXException e) {
			System.out.println("ERROR: Error en la conversi�n del lector.");
		} catch (IOException e) {
			System.out.println("ERROR: Error en el acceso al fichero de origen.");
		}
	}
}
