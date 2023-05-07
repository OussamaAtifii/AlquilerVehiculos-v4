package org.iesalandalus.programacion.alquilervehiculos.modelo.negocio;

import java.io.IOException;
import java.util.List;

import javax.naming.OperationNotSupportedException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Cliente;
import org.xml.sax.SAXException;

public interface IClientes {

    void comenzar() throws OperationNotSupportedException, ParserConfigurationException, SAXException, IOException;

    void terminar() throws ParserConfigurationException, TransformerException;

    List<Cliente> get();

    void insertar(Cliente cliente) throws OperationNotSupportedException;

    void modificar(Cliente cliente, String nombre, String telefono) throws OperationNotSupportedException;

    Cliente buscar(Cliente cliente);

    void borrar(Cliente cliente) throws OperationNotSupportedException;
}