package persistenceTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import edu.uga.miage.m1.polygons.gui.persistence.XMLVisitor;
import edu.uga.miage.m1.polygons.gui.shapes.Circle;
import edu.uga.miage.m1.polygons.gui.shapes.Square;
import edu.uga.miage.m1.polygons.gui.shapes.Triangle;

class XMLVisitorTest{
	
	private Document documentExpected;
	
	private Element rootElement;
	
	private XMLVisitor xmlVisitor;
	

	private Square square;
	
	private Triangle triangle;
	
	private Circle circle;
	
	@BeforeEach
	void setUp() throws ParserConfigurationException {
		 DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
         documentExpected = dBuilder.newDocument();
		
		xmlVisitor = new XMLVisitor();
		
		square = new Square(50, 50);
		
		triangle = new Triangle(50, 50);
		
		circle =  new Circle(50, 50);
	}
	
	@Test
	void createShapeElementCircleTest() {
	     String resExpected = "<shape><type>circle</type><x>"+circle.getX()+"</x><y>"+circle.getY()+"</y></shape>";
	     
	     xmlVisitor.visit(circle);
	     
	     assertEquals(resExpected, xmlVisitor.getRepresentation());
	}
	
	@Test
	void createShapeElementSquareTest() {
	     String resExpected = "<shape><type>square</type><x>"+square.getX()+"</x><y>"+square.getY()+"</y></shape>";
	     
	     xmlVisitor.visit(square);
	     
	     assertEquals(resExpected, xmlVisitor.getRepresentation());
	}
	
	@Test
	void createShapeElementTriangleTest() {
	     String resExpected = "<shape><type>triangle</type><x>"+triangle.getX()+"</x><y>"+triangle.getY()+"</y></shape>";
	     
	     xmlVisitor.visit(triangle);
	     
	     assertEquals(resExpected, xmlVisitor.getRepresentation());
	}
}