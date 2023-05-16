package org.iesalandalus.programacion.alquilervehiculos.modelo.negocio.ficheros;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.OperationNotSupportedException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Autobus;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Furgoneta;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Turismo;
import org.iesalandalus.programacion.alquilervehiculos.modelo.dominio.Vehiculo;
import org.iesalandalus.programacion.alquilervehiculos.modelo.negocio.IVehiculos;
import org.iesalandalus.programacion.alquilervehiculos.modelo.negocio.ficheros.utilidades.UtilidadesXml;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Vehiculos implements IVehiculos {
    private List<Vehiculo> coleccionVehiculos;
    private static Vehiculos instancia = null;
    private final String RUTA_FICHERO = "../AlquilerVehiculos-v4/src/datos/vehiculos.xml";
    private final String RAIZ = "Vehiculos";
    private final String VEHICULO = "Vehiculo";
    private final String MARCA = "Marca";
    private final String MODELO = "Modelo";
    private final String MATRICULA = "Matricula";
    private final String CILINDRADA = "Cilindrada";
    private final String PLAZAS = "Plazas";
    private final String PMA = "Pma";
    private final String TIPO = "Tipo";
    private final String TURISMO = "Turismo";
    private final String AUTOBUS = "Autobus";
    private final String FURGONETA = "Furgoneta";

    private Vehiculos() {
        coleccionVehiculos = new ArrayList<>();
    }

    static Vehiculos getInstancia() {
        // Si instancia es nula, se le da valor y se devuelve:
        if (instancia == null) {
            instancia = new Vehiculos();
        }
        return instancia;
    }

    @Override
    public void comenzar()
            throws OperationNotSupportedException, ParserConfigurationException, SAXException, IOException {
        leerXml();
    }

    private void leerXml()
            throws ParserConfigurationException, SAXException, IOException, OperationNotSupportedException {
        // Pasar xml a dom:
        Document documento = UtilidadesXml.xmlToDom(RUTA_FICHERO);

        // Obtener lista de nodos de todos los nodos vehiculo:
        NodeList listaVehiculos = documento.getElementsByTagName(VEHICULO);

        // Iteración sobre la lista de nodos, pasar los elementos a vehiculo e
        // insertarlos en la colección de vehiculos:
        for (int i = 0; i < listaVehiculos.getLength(); i++) {
            Element elemento = (Element) listaVehiculos.item(i);
            insertar(elementToVehiculo(elemento));
        }
    }

    private Vehiculo elementToVehiculo(Element elemento) {
        Vehiculo vehiculo = null;

        // Obtener valor del atributo matricula:
        String matricula = elemento.getAttribute(MATRICULA);

        // Obtener valor del elemento marca:
        String marca = elemento.getElementsByTagName(MARCA).item(0).getTextContent();

        // Obtener valor del elemento modelo:
        String modelo = elemento.getElementsByTagName(MODELO).item(0).getTextContent();

        // Obtener los valor necesarios dependiendo del tipo de vehiculo
        // Si vehiculo es turismo:
        if (elemento.getAttribute(TIPO).equals(TURISMO)) {
            // Obtener valor del elemento cilindrada:
            String cilindrada = elemento.getElementsByTagName(CILINDRADA).item(0).getTextContent();

            // Darle como valor a vehiculo, un nuevo turismo con los datos obtenidos:
            vehiculo = new Turismo(marca, modelo, matricula, Integer.parseInt(cilindrada));
        }
        // Si vehiculo es furgoneta:
        if (elemento.getAttribute(TIPO).equals(FURGONETA)) {
            // Obtener valor del elemento plazas:
            String plazas = elemento.getElementsByTagName(PLAZAS).item(0).getTextContent();
            // Obtener valor del elemento pma:
            String pma = elemento.getElementsByTagName(PMA).item(0).getTextContent();
            // Darle como valor a vehiculo, una nueva furgoneta con los datos obtenidos:
            vehiculo = new Furgoneta(marca, modelo, Integer.parseInt(pma), Integer.parseInt(plazas), matricula);
        }
        // Si vehiculo es autobus:
        if (elemento.getAttribute(TIPO).equals(AUTOBUS)) {
            // Obtener valor del elemento plazas:
            String plazas = elemento.getElementsByTagName(PLAZAS).item(0).getTextContent();
            // Darle como valor a vehiculo, un nuevo autobus con los datos obtenidos:
            vehiculo = new Autobus(marca, modelo, matricula, Integer.parseInt(plazas));
        }

        // Devolver vehiculo con los datos obtenidos:
        return vehiculo;
    }

    @Override
    public void terminar() throws ParserConfigurationException, TransformerException {
        escribirXml();
    }

    // Obtener en string el tipo de vehiculo:
    private String getTipoVehiculo(Vehiculo vehiculo) {
        String tipo = "";

        if (vehiculo instanceof Turismo) {
            tipo = TURISMO;
        }
        if (vehiculo instanceof Furgoneta) {
            tipo = FURGONETA;
        }
        if (vehiculo instanceof Autobus) {
            tipo = AUTOBUS;
        }

        return tipo;
    }

    private void escribirXml() throws ParserConfigurationException, TransformerException {
        // Crear dom vacío con la raiz:
        Document documento = UtilidadesXml.crearDomVacio(RAIZ);

        // Obtener elemento raiz del dom vacío:
        Element raiz = documento.getDocumentElement();

        // Iterar sobre la coleccion de vehiculos e ir transformando cada vehiculo a
        // elemento:
        for (Vehiculo coleccionVehiculo : coleccionVehiculos) {
            raiz.appendChild(vehiculoToElement(documento, coleccionVehiculo));
        }

        // Transformar el dom a xml:
        UtilidadesXml.domToXml(documento, RUTA_FICHERO);
    }

    private Element vehiculoToElement(Document dom, Vehiculo vehiculo) {
        // Obtener el tipo de vehiculo del vehiculo obtenido por parámetro:
        String tipoVehiculo = getTipoVehiculo(vehiculo);

        // Crear elemento vehiculo:
        Element eVehiculo = dom.createElement(VEHICULO);

        // Crear atributo matricula, darle valor y enlazar con elemento vehiculo:
        Attr attrMatricula = dom.createAttribute(MATRICULA);
        attrMatricula.setValue(vehiculo.getMatricula());
        eVehiculo.setAttributeNode(attrMatricula);

        // Crear atributo tipo, darle valor y enlazar con el elemento vehiculo:
        Attr attrTipo = dom.createAttribute(TIPO);
        attrTipo.setValue(tipoVehiculo);
        eVehiculo.setAttributeNode(attrTipo);

        // Crear elemento marca, darle valor y enlazar con el elemento vehiculo:
        Element eMarca = dom.createElement(MARCA);
        eMarca.appendChild(dom.createTextNode(vehiculo.getMarca()));
        eVehiculo.appendChild(eMarca);

        // Crear elemento modelo, darle valor y enlazar con el elemento vehiculo:
        Element eModelo = dom.createElement(MODELO);
        eModelo.appendChild(dom.createTextNode(vehiculo.getModelo()));
        eVehiculo.appendChild(eModelo);

        // Crear elemento tipo:
        Element eTipo = dom.createElement(tipoVehiculo);

        // Si vehiculo es turismo:
        if (tipoVehiculo.equals(TURISMO)) {
            Turismo turismo = (Turismo) vehiculo;

            // Crear elemento cilindrada, darle valor y enlazar con el elemento tipo:
            Element eCilindrada = dom.createElement(CILINDRADA);
            eCilindrada.appendChild(dom.createTextNode(Integer.toString(turismo.getCilindrada())));
            eTipo.appendChild(eCilindrada);

            // Enlazar elemento tipo con el elemento vehiculo:
            eVehiculo.appendChild(eTipo);
        }
        // Si vehiculo es furgoneta:
        if (tipoVehiculo.equals(FURGONETA)) {
            Furgoneta furgoneta = (Furgoneta) vehiculo;

            // Crear elemento plazas, darle valor y enlazar con el elemento tipo:
            Element ePlazas = dom.createElement(PLAZAS);
            ePlazas.appendChild(dom.createTextNode(Integer.toString(furgoneta.getPlazas())));
            eTipo.appendChild(ePlazas);

            // Crear elemento pma, darle valor y enlazar con el elemento tipo:
            Element ePma = dom.createElement(PMA);
            ePma.appendChild(dom.createTextNode(Integer.toString(furgoneta.getPma())));
            eTipo.appendChild(ePma);

            // Enlazar elemento tipo con el elemento vehiculo:
            eVehiculo.appendChild(eTipo);
        }
        // Si vehiculo es autobus:
        if (tipoVehiculo.equals(AUTOBUS)) {
            Autobus autobus = (Autobus) vehiculo;

            // Crear elemento plazas, darle valor y enlazar con el elemento tipo:
            Element ePlazas = dom.createElement(PLAZAS);
            ePlazas.appendChild(dom.createTextNode(Integer.toString(autobus.getPlazas())));
            eTipo.appendChild(ePlazas);

            // Enlazar elemento tipo con el elemento vehiculo:
            eVehiculo.appendChild(eTipo);
        }

        // Devolver elemento vehiculo con todos los elementos y atributos hijos
        // enlazados:
        return eVehiculo;
    }

    @Override
    public List<Vehiculo> get() {
        return new ArrayList<>(coleccionVehiculos);
    }

    @Override
    public void insertar(Vehiculo vehiculo) throws OperationNotSupportedException {
        if (vehiculo == null) {
            throw new NullPointerException("ERROR: No se puede insertar un vehiculo nulo.");
        }
        if (coleccionVehiculos.contains(vehiculo)) {
            throw new OperationNotSupportedException("ERROR: Ya existe un vehiculo con esa matrícula.");
        }

        coleccionVehiculos.add(vehiculo);
    }

    @Override
    public Vehiculo buscar(Vehiculo vehiculo) {
        int i;
        if (vehiculo == null) {
            throw new NullPointerException("ERROR: No se puede buscar un vehiculo nulo.");
        }
        if (coleccionVehiculos.contains(vehiculo)) {
            i = coleccionVehiculos.indexOf(vehiculo);
            vehiculo = coleccionVehiculos.get(i);
        } else {
            vehiculo = null;
        }
        return vehiculo;

    }

    @Override
    public void borrar(Vehiculo vehiculo) throws OperationNotSupportedException {
        if (vehiculo == null) {
            throw new NullPointerException("ERROR: No se puede borrar un vehiculo nulo.");
        }
        if (!coleccionVehiculos.contains(vehiculo)) {
            throw new OperationNotSupportedException("ERROR: No existe ningún vehiculo con esa matrícula.");
        }
        coleccionVehiculos.remove(vehiculo);
    }

}
