package org.iesalandalus.programacion.alquilervehiculos.modelo.negocio.ficheros;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.naming.OperationNotSupportedException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Alquiler;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Cliente;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Vehiculo;
import org.iesalandalus.programacion.alquilervehiculos.modelo.negocio.IAlquileres;
import org.iesalandalus.programacion.alquilervehiculos.modelo.negocio.ficheros.utilidades.UtilidadesXml;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Alquileres implements IAlquileres {
    private static List<Alquiler> coleccionAlquileres;
    private static Alquileres instancia;
    private final String RUTA_FICHERO = "../AlquilerVehiculos-v3/src/datos/alquileres.xml";
    private final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final String RAIZ = "Alquileres";
    private final String ALQUILER = "Alquiler";
    private final String DNI_CLIENTE = "Dni";
    private final String MATRICULA_VEHICULO = "Matricula";
    private final String FECHA_ALQUILER = "FechaAlquiler";
    private final String FECHA_DEVOLUCION = "FechaDevolucion";

    private Alquileres() {
        coleccionAlquileres = new ArrayList<>();
    }

    static Alquileres getInstancia() {
        // Si instancia es nula, se le da valor y se devuelve:
        if (instancia == null) {
            instancia = new Alquileres();
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

        // Obtener lista de nodos de todos los nodos alquiler:
        NodeList listaAlquileres = documento.getElementsByTagName(ALQUILER);

        // Iteración sobre la lista de nodos, pasar los elementos a alquiler e
        // insertarlos en la colección de vehiculos:
        for (int i = 0; i < listaAlquileres.getLength(); i++) {
            Element elemento = (Element) listaAlquileres.item(i);
            insertar(elementToAlquiler(elemento));
        }
    }

    private Alquiler elementToAlquiler(Element elemento) throws OperationNotSupportedException {
        // Obtener el valor del atributo matricula:
        String matricula = elemento.getAttribute(MATRICULA_VEHICULO);

        // Obtener el valor del atributo dni:
        String dni = elemento.getAttribute(DNI_CLIENTE);

        // Obtener el valor del elemento fecha de alquiler:
        String fechaAlquiler = elemento.getElementsByTagName(FECHA_ALQUILER).item(0).getTextContent();

        // Obtener el valor del elemento fecha de devolución:
        String fechaDevolucion = elemento.getElementsByTagName(FECHA_DEVOLUCION).item(0).getTextContent();

        // Obtener el cliente utilizando la instancia de clientes:
        Cliente cliente = Clientes.getInstancia().buscar(Cliente.getClienteConDni(dni));

        // Obtener el vehiculo utilizando la instancia de vehiculos:
        Vehiculo vehiculo = Vehiculos.getInstancia().buscar(Vehiculo.getVehiculoConMatricula(matricula));

        // Crear alquiler con datos obtenidos:
        Alquiler alquiler = new Alquiler(cliente, vehiculo, LocalDate.parse(fechaAlquiler, FORMATO_FECHA));

        // Comprobar si el alquiler tiene fecha de devolución y devolverlo en caso de
        // que si la tuviera:
        if (!fechaDevolucion.equals("")) {
            alquiler.devolver(LocalDate.parse(fechaDevolucion, FORMATO_FECHA));
        }

        return alquiler;
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

        // Iterar sobre la coleccion de clientes e ir transformando cada alquiler a
        // elemento:
        for (int i = 0; i < coleccionAlquileres.size(); i++) {
            raiz.appendChild(alquilerToElement(documento, coleccionAlquileres.get(i)));
        }

        // Transformar el dom a xml:
        UtilidadesXml.domToXml(documento, RUTA_FICHERO);
    }

    private Element alquilerToElement(Document dom, Alquiler alquiler) {
        // Crear elemento cliente:
        Element eAlquiler = dom.createElement(ALQUILER);

        // Crear atributo matricula, darle valor y enlazar con alquiler
        Attr attrMatricula = dom.createAttribute(MATRICULA_VEHICULO);
        attrMatricula.setValue(alquiler.getVehiculo().getMatricula());
        eAlquiler.setAttributeNode(attrMatricula);

        // Crear atributo dni, darle valor y enlazar con alquiler:
        Attr attrDni = dom.createAttribute(DNI_CLIENTE);
        attrDni.setValue(alquiler.getCliente().getDni());
        eAlquiler.setAttributeNode(attrDni);

        // Crear elemento fecha alquiler, darle valor y enlazar con el elemento
        // alquiler:
        Element eFechaAlquiler = dom.createElement(FECHA_ALQUILER);
        eFechaAlquiler.appendChild(dom.createTextNode(alquiler.getFechaAlquiler().format(FORMATO_FECHA).toString()));
        eAlquiler.appendChild(eFechaAlquiler);

        // Crear elemento fecha devolución:
        Element eFechaDevolucion = dom.createElement(FECHA_DEVOLUCION);

        // Comprobar si fecha devolución es nulo, pasarlo a "" para no tener
        // NullPointerException,
        // en caso contrario, formateamos fecha y pasar a string:
        String fecha = (alquiler.getFechaDevolucion() == null) ? ""
                : alquiler.getFechaDevolucion().format(FORMATO_FECHA).toString();

        // Darle valor al elemento fecha devolución y enlazar con el elemento alquiler:
        eFechaDevolucion.appendChild(dom.createTextNode(fecha));
        eAlquiler.appendChild(eFechaDevolucion);

        // Devolver elemento alquiler con todos los elementos y atributos hijos
        // enlazados:
        return eAlquiler;
    }

    @Override
    public List<Alquiler> get() {
        return new ArrayList<>(coleccionAlquileres);
    }

    @Override
    public List<Alquiler> get(Cliente cliente) {
        List<Alquiler> alquileresCliente = new ArrayList<>();

        for (Alquiler alquiler : coleccionAlquileres) {
            if (alquiler.getCliente().equals(cliente)) {
                alquileresCliente.add(alquiler);
            }
        }
        return alquileresCliente;
    }

    @Override
    public List<Alquiler> get(Vehiculo vehiculo) {
        List<Alquiler> alquileresTurismo = new ArrayList<>();

        for (Alquiler alquiler : coleccionAlquileres) {
            if (alquiler.getVehiculo().equals(vehiculo)) {
                alquileresTurismo.add(alquiler);
            }
        }
        return alquileresTurismo;
    }

    @Override
    public void insertar(Alquiler alquiler) throws OperationNotSupportedException {
        if (alquiler == null) {
            throw new NullPointerException("ERROR: No se puede insertar un alquiler nulo.");
        }

        comprobarAlquiler(alquiler.getCliente(), alquiler.getVehiculo(), alquiler.getFechaAlquiler());
        coleccionAlquileres.add(alquiler);
    }

    private void comprobarAlquiler(Cliente cliente, Vehiculo vehiculo, LocalDate fechaAlquiler)
            throws OperationNotSupportedException {

        if (cliente == null) {
            throw new NullPointerException("ERROR: El cliente no puede ser nulo.");
        }
        if (vehiculo == null) {
            throw new NullPointerException("ERROR: El vehiculo no puede ser nulo.");
        }

        for (Alquiler alquiler : get(cliente)) {
            if (alquiler.getFechaDevolucion() == null) {
                throw new OperationNotSupportedException("ERROR: El cliente tiene otro alquiler sin devolver.");
            } else {
                if (alquiler.getFechaDevolucion().isAfter(fechaAlquiler)
                        || alquiler.getFechaDevolucion().equals(fechaAlquiler)) {
                    throw new OperationNotSupportedException("ERROR: El cliente tiene un alquiler posterior.");
                }
            }
        }

        for (Alquiler alquiler : get(vehiculo)) {
            if (alquiler.getFechaDevolucion() == null) {
                throw new OperationNotSupportedException("ERROR: El turismo está actualmente alquilado.");
            } else {
                if (alquiler.getFechaDevolucion().isAfter(fechaAlquiler)
                        || alquiler.getFechaDevolucion().equals(fechaAlquiler)) {
                    throw new OperationNotSupportedException("ERROR: El turismo tiene un alquiler posterior.");
                }
            }
        }
    }

    @Override
    public void devolver(Cliente cliente, LocalDate fechaDevolucion) throws OperationNotSupportedException {
        if (cliente == null) {
            throw new NullPointerException("ERROR: No se puede devolver un alquiler nulo.");
        }
        if (fechaDevolucion == null) {
            throw new NullPointerException("ERROR: La fecha de devolución no puede ser nula.");
        }

        Alquiler alquilerAbierto = getAlquilerAbierto(cliente);

        if (alquilerAbierto == null) {
            throw new IllegalArgumentException("ERROR: No existe ningún alquiler abierto para el cliente introducido");
        }
        alquilerAbierto.devolver(fechaDevolucion);

    }

    private Alquiler getAlquilerAbierto(Cliente cliente) {
        Alquiler alquilerAbierto = null;
        for (Alquiler alquiler : get(cliente)) {
            if (alquiler.getFechaDevolucion() == null) {
                alquilerAbierto = alquiler;
            }
        }
        return alquilerAbierto;
    }

    @Override
    public void devolver(Vehiculo vehiculo, LocalDate fechaDevolucion) throws OperationNotSupportedException {
        if (vehiculo == null) {
            throw new NullPointerException("ERROR: No se puede devolver un alquiler nulo.");
        }
        if (fechaDevolucion == null) {
            throw new NullPointerException("ERROR: La fecha de devolución no puede ser nula.");
        }

        Alquiler alquilerAbierto = getAlquilerAbierto(vehiculo);

        if (alquilerAbierto == null) {
            throw new IllegalArgumentException("ERROR: No existe ningún alquiler abierto para el vehiculo introducido");
        }
        alquilerAbierto.devolver(fechaDevolucion);

    }

    private Alquiler getAlquilerAbierto(Vehiculo vehiculo) {
        Alquiler alquilerAbierto = null;
        for (Alquiler alquiler : get(vehiculo)) {
            if (alquiler.getFechaDevolucion() == null) {
                alquilerAbierto = alquiler;
                break;
            }
        }
        return alquilerAbierto;
    }

    @Override
    public Alquiler buscar(Alquiler alquiler) {
        int i;
        if (alquiler == null) {
            throw new NullPointerException("ERROR: No se puede buscar un alquiler nulo.");
        }
        if (coleccionAlquileres.contains(alquiler)) {
            i = coleccionAlquileres.indexOf(alquiler);
            alquiler = coleccionAlquileres.get(i);
            return alquiler;
        } else {
            return null;
        }
    }

    @Override
    public void borrar(Alquiler alquiler) throws OperationNotSupportedException {
        if (alquiler == null) {
            throw new NullPointerException("ERROR: No se puede borrar un alquiler nulo.");
        }
        if (!coleccionAlquileres.contains(alquiler)) {
            throw new OperationNotSupportedException("ERROR: No existe ningún alquiler igual.");
        }
        coleccionAlquileres.remove(alquiler);
    }

}
