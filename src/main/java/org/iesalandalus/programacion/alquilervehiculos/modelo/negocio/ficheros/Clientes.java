package org.iesalandalus.programacion.alquilervehiculos.modelo.negocio.ficheros;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.OperationNotSupportedException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Cliente;
import org.iesalandalus.programacion.alquilervehiculos.modelo.negocio.IClientes;
import org.iesalandalus.programacion.alquilervehiculos.modelo.negocio.ficheros.utilidades.UtilidadesXml;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Clientes implements IClientes {
    private static List<Cliente> coleccionClientes;
    private static Clientes instancia;
    private final String RUTA_FICHERO = "../AlquilerVehiculos-v3/src/datos/clientes.xml";
    private final String TELEFONO = "Telefono";
    private final String CLIENTE = "Cliente";
    private final String NOMBRE = "Nombre";
    private final String RAIZ = "Clientes";
    private final String DNI = "Dni";

    private Clientes() {
        coleccionClientes = new ArrayList<>();
    }

    static Clientes getInstancia() {
        // Si instancia es nula, se le da valor y se devuelve:
        if (instancia == null) {
            instancia = new Clientes();
        }

        return instancia;
    }

    @Override
    public void comenzar()
            throws OperationNotSupportedException, ParserConfigurationException, SAXException, IOException {
        leerXml();
    }

    private void leerXml()
            throws OperationNotSupportedException, ParserConfigurationException, SAXException, IOException {
        // Pasar xml a dom:
        Document documento = UtilidadesXml.xmlToDom(RUTA_FICHERO);

        // Obtener lista de nodos de todos los nodos cliente:
        NodeList listaClientes = documento.getElementsByTagName(CLIENTE);

        // Iteración sobre la lista de nodos, pasar los elementos a cliente e
        // insertarlos en la colección de clientes:
        for (int i = 0; i < listaClientes.getLength(); i++) {
            Element elemento = (Element) listaClientes.item(i);
            insertar(elementToClient(elemento));
        }
    }

    private Cliente elementToClient(Element elemento) {
        // Obtener valor del atributo dni:
        String dni = elemento.getAttribute(DNI);

        // Obtener valor del elemento nombre:
        String nombre = elemento.getElementsByTagName(NOMBRE).item(0).getTextContent();

        // Obtener valor del elemento telefono:
        String telefono = elemento.getElementsByTagName(TELEFONO).item(0).getTextContent();

        // Devolver cliente con los datos obtenidos:
        return new Cliente(nombre, dni, telefono);
    }

    @Override
    public void terminar() throws ParserConfigurationException, TransformerException {
        escribirXml();
    }

    private void escribirXml() throws ParserConfigurationException, TransformerException {
        // Crear dom vacío con la raiz:
        Document documento = UtilidadesXml.crearDomVacio(RAIZ);

        // Obtener elemento raiz del dom vacío:
        Element raiz = documento.getDocumentElement();

        // Iterar sobre la coleccion de clientes e ir transformando cada cliente a
        // elemento:
        for (Cliente cliente : coleccionClientes) {
            raiz.appendChild(clienteToElement(documento, cliente));
        }

        // Transformar el dom a xml:
        UtilidadesXml.domToXml(documento, RUTA_FICHERO);
    }

    private Element clienteToElement(Document dom, Cliente cliente) {

        // Crear elemento cliente:
        Element eCliente = dom.createElement(CLIENTE);

        // Crear atributo dni, darle valor y enlazar con elemento cliente:
        Attr attrDni = dom.createAttribute(DNI);
        attrDni.setValue(cliente.getDni());
        eCliente.setAttributeNode(attrDni);

        // Crear elemento nombre, darle valor y enlazar con elemento cliente:
        Element eNombre = dom.createElement(NOMBRE);
        eNombre.appendChild(dom.createTextNode(cliente.getNombre()));
        eCliente.appendChild(eNombre);

        // Crear elemento telefono, darle valor y enlazar con elemento cliente:
        Element eTelefono = dom.createElement(TELEFONO);
        eTelefono.appendChild(dom.createTextNode(cliente.getTelefono()));
        eCliente.appendChild(eTelefono);

        // Devolver elemento cliente con todos los elemento y atributos hijos enlazados:
        return eCliente;
    }

    @Override
    public List<Cliente> get() {
        return new ArrayList<>(coleccionClientes);
    }

    @Override
    public void insertar(Cliente cliente) throws OperationNotSupportedException {
        if (cliente == null) {
            throw new NullPointerException("ERROR: No se puede insertar un cliente nulo.");
        }
        if (coleccionClientes.contains(cliente)) {
            throw new OperationNotSupportedException("ERROR: Ya existe un cliente con ese DNI.");
        }
        coleccionClientes.add(cliente);
    }

    @Override
    public void modificar(Cliente cliente, String nombre, String telefono) throws OperationNotSupportedException {
        try {
            buscar(cliente);
        } catch (NullPointerException e) {
            throw new NullPointerException("ERROR: No se puede modificar un cliente nulo.");
        }

        if (buscar(cliente) == null) {
            throw new OperationNotSupportedException("ERROR: No existe ningún cliente con ese DNI.");
        }

        cliente = buscar(cliente);

        if (nombre != null && !nombre.isBlank()) {
            cliente.setNombre(nombre);
        }
        if (telefono != null && !telefono.isBlank()) {
            cliente.setTelefono(telefono);
        }
    }

    @Override
    public Cliente buscar(Cliente cliente) {
        int i;
        if (cliente == null) {
            throw new NullPointerException("ERROR: No se puede buscar un cliente nulo.");
        }
        if (coleccionClientes.contains(cliente)) {
            i = coleccionClientes.indexOf(cliente);
            cliente = coleccionClientes.get(i);
            return cliente;
        } else {
            return null;
        }
    }

    @Override
    public void borrar(Cliente cliente) throws OperationNotSupportedException {
        if (cliente == null) {
            throw new NullPointerException("ERROR: No se puede borrar un cliente nulo.");
        }
        if (!coleccionClientes.contains(cliente)) {
            throw new OperationNotSupportedException("ERROR: No existe ningún cliente con ese DNI.");
        }

        coleccionClientes.remove(cliente);
    }

}
