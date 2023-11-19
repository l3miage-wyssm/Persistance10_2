package edu.uga.miage.m1.polygons.gui.persistence;

import java.util.logging.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import edu.uga.miage.m1.polygons.gui.shapes.SimpleShape;

/**
 * @author <a href=
 *         "mailto:christophe.saint-marcel@univ-grenoble-alpes.fr">Christophe</a>
 */

public class XMLVisitor implements Visitor {

    private Document document;
    private Element rootElement;

    public XMLVisitor() {
        Logger msg = Logger.getLogger("Error");
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            document = dBuilder.newDocument();
        } catch (ParserConfigurationException e) {
            msg.log(Level.WARNING, "erreur dans constructeur XMLVisitor", e);
        }
    }

    private Element createShapeElement(String type, int x, int y) {
        Element shapeElement = document.createElement("shape");
        Element typeElement = document.createElement("type");
        typeElement.appendChild(document.createTextNode(type));
        Element xElement = document.createElement("x");
        xElement.appendChild(document.createTextNode(Integer.toString(x)));
        Element yElement = document.createElement("y");
        yElement.appendChild(document.createTextNode(Integer.toString(y)));

        shapeElement.appendChild(typeElement);
        shapeElement.appendChild(xElement);
        shapeElement.appendChild(yElement);

        return shapeElement;
    }

    @Override
    public void visit(SimpleShape simpleShape) {
        rootElement = createShapeElement(simpleShape.getType(), simpleShape.getX(), simpleShape.getY());
    }

    public String getRepresentation() {
        return elementToString(rootElement);
    }

    private String elementToString(Element element) {

        Logger msg = Logger.getLogger("Error");
        try {
            document = element.getOwnerDocument();
            document.appendChild(element);
            javax.xml.transform.TransformerFactory transformerFactory = javax.xml.transform.TransformerFactory
                    .newInstance();

            // Disable access to external entities in XML parsing
            transformerFactory.setFeature(javax.xml.XMLConstants.FEATURE_SECURE_PROCESSING, true);
            transformerFactory.setAttribute(javax.xml.XMLConstants.ACCESS_EXTERNAL_DTD, "");
            transformerFactory.setAttribute(javax.xml.XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
            javax.xml.transform.Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(javax.xml.transform.OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.setOutputProperty(javax.xml.transform.OutputKeys.INDENT, "yes");
            javax.xml.transform.stream.StreamResult result = new javax.xml.transform.stream.StreamResult(
                    new java.io.StringWriter());
            javax.xml.transform.dom.DOMSource source = new javax.xml.transform.dom.DOMSource(document);
            transformer.transform(source, result);
            String xmlString = result.getWriter().toString();

            // Remove line breaks and extra whitespace

            xmlString = xmlString.trim();

            return xmlString;
        } catch (Exception e) {
            msg.log(Level.WARNING, "erreur dans elementToString XMLVisitor", e);
            return null;
        }
    }

    public void reset() throws ParserConfigurationException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        document = dBuilder.newDocument();
        rootElement = null;
    }

}
