package persistenceTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.uga.miage.m1.polygons.gui.persistence.XMLVisitor;
import edu.uga.miage.m1.polygons.gui.shapes.Circle;
import edu.uga.miage.m1.polygons.gui.shapes.Square;
import edu.uga.miage.m1.polygons.gui.shapes.Triangle;

class XMLVisitorTest {

	private XMLVisitor xmlVisitor;

	private Square square;

	private Triangle triangle;

	private Circle circle;

	@BeforeEach
	void setUp() throws ParserConfigurationException {

		xmlVisitor = new XMLVisitor();

		square = new Square(50, 50);

		triangle = new Triangle(50, 50);

		circle = new Circle(50, 50);
	}

	@Test
	void createShapeElementCircleTest() {
		String resExpected = "<shape><type>circle</type><x>" + circle.getX() + "</x><y>" + circle.getY()
				+ "</y></shape>";

		xmlVisitor.visit(circle);

		assertEquals(resExpected, xmlVisitor.getRepresentation());
	}

	@Test
	void createShapeElementSquareTest() {
		String resExpected = "<shape><type>square</type><x>" + square.getX() + "</x><y>" + square.getY()
				+ "</y></shape>";

		xmlVisitor.visit(square);

		assertEquals(resExpected, xmlVisitor.getRepresentation());
	}

	@Test
	void createShapeElementTriangleTest() {
		String resExpected = "<shape><type>triangle</type><x>" + triangle.getX() + "</x><y>" + triangle.getY()
				+ "</y></shape>";

		xmlVisitor.visit(triangle);

		assertEquals(resExpected, xmlVisitor.getRepresentation());
	}
}