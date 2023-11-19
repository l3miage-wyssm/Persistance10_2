package persistenceTest;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import edu.uga.miage.m1.polygons.gui.persistence.XMLVisitor;
import edu.uga.miage.m1.polygons.gui.shapes.Circle;
import edu.uga.miage.m1.polygons.gui.shapes.Square;
import edu.uga.miage.m1.polygons.gui.shapes.Triangle;

class XMLVisitorTest {

	private XMLVisitor xmlVisitor;

	private Document document;

	private Square square;

	private Triangle triangle;

	private Circle circle;

	@BeforeEach
	void setUp() throws ParserConfigurationException {

		Logger msg = Logger.getLogger("Error");
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			document = dBuilder.newDocument();
		} catch (ParserConfigurationException e) {
			msg.log(Level.WARNING, "erreur dans constructeur XMLVisitor", e);
		}

		xmlVisitor = new XMLVisitor();

		square = new Square(50, 50);

		triangle = new Triangle(50, 50);

		circle = new Circle(50, 50);
	}

	String elementToStringPourTest(Element element) {
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

	@Test
	void createShapeElementCircleTest() {
		Element shapeElement = document.createElement("shape");
		Element typeElement = document.createElement("type");
		typeElement.appendChild(document.createTextNode(circle.getType()));
		Element xElement = document.createElement("x");
		xElement.appendChild(document.createTextNode(Integer.toString(circle.getX())));
		Element yElement = document.createElement("y");
		yElement.appendChild(document.createTextNode(Integer.toString(circle.getY())));

		shapeElement.appendChild(typeElement);
		shapeElement.appendChild(xElement);
		shapeElement.appendChild(yElement);

		xmlVisitor.visit(circle);

		Assertions.assertEquals(elementToStringPourTest(shapeElement), xmlVisitor.getRepresentation());
	}

	@Test
	void createShapeElementSquareTest() {
		Element shapeElement = document.createElement("shape");
		Element typeElement = document.createElement("type");
		typeElement.appendChild(document.createTextNode(square.getType()));
		Element xElement = document.createElement("x");
		xElement.appendChild(document.createTextNode(Integer.toString(square.getX())));
		Element yElement = document.createElement("y");
		yElement.appendChild(document.createTextNode(Integer.toString(square.getY())));

		shapeElement.appendChild(typeElement);
		shapeElement.appendChild(xElement);
		shapeElement.appendChild(yElement);

		xmlVisitor.visit(square);

		Assertions.assertEquals(elementToStringPourTest(shapeElement), xmlVisitor.getRepresentation());
	}

	@Test
	void createShapeElementTriangleTest() {
		Element shapeElement = document.createElement("shape");
		Element typeElement = document.createElement("type");
		typeElement.appendChild(document.createTextNode(triangle.getType()));
		Element xElement = document.createElement("x");
		xElement.appendChild(document.createTextNode(Integer.toString(triangle.getX())));
		Element yElement = document.createElement("y");
		yElement.appendChild(document.createTextNode(Integer.toString(triangle.getY())));

		shapeElement.appendChild(typeElement);
		shapeElement.appendChild(xElement);
		shapeElement.appendChild(yElement);

		xmlVisitor.visit(triangle);

		Assertions.assertEquals(elementToStringPourTest(shapeElement), xmlVisitor.getRepresentation());
	}
}