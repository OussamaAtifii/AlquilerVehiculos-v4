package org.iesalandalus.programacion.alquilervehiculos.modelo.negocio.ficheros.utilidades;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class UtilidadesXml {
    private UtilidadesXml() {

    }

    public static Document xmlToDom(String rutaXml) throws ParserConfigurationException, SAXException, IOException {
        Document documento = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        documento = db.parse(rutaXml);

        return documento;
    }

    public static void domToXml(Document documento, String rutaXml) throws TransformerException {
        File f = new File(rutaXml);

        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

        StreamResult result = new StreamResult(f);
        DOMSource source = new DOMSource(documento);

        transformer.transform(source, result);
    }

    public static Document crearDomVacio(String raiz) throws ParserConfigurationException {
        Document documento = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        documento = db.newDocument();

        Element root = documento.createElement(raiz);
        documento.appendChild(root);

        return documento;
    }
}
