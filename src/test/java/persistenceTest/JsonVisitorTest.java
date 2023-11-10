package persistenceTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.uga.miage.m1.polygons.gui.persistence.JSonVisitor;
import edu.uga.miage.m1.polygons.gui.shapes.Circle;
import edu.uga.miage.m1.polygons.gui.shapes.Square;
import edu.uga.miage.m1.polygons.gui.shapes.Triangle;

class JsonVisitorTest{
	private JsonObjectBuilder jsonObjectBuilderExpected;
	
	private JSonVisitor jsonVisitor = new JSonVisitor();
	
	private Square square;
	
	private Triangle triangle;
	
	private Circle circle;
	
	@BeforeEach
	void setUp(){
		jsonObjectBuilderExpected = Json.createObjectBuilder();
		
		square = new Square(50, 50);
		
		triangle = new Triangle(50, 50);
		
		circle =  new Circle(50, 50);
	}
	
	@Test
	void visitSquareTest() {
		jsonVisitor.visit(square);
		
		jsonObjectBuilderExpected.add("type", square.getType())
								.add("x", square.getX())
				                .add("y", square.getY());
		
		JsonObject jsonObject = jsonObjectBuilderExpected.build();
		
		String resExpected = jsonObject.toString();
		
		assertEquals(jsonVisitor.getRepresentation(),resExpected);
	}
	
	@Test
	void visitCircleTest() {
		jsonVisitor.visit(circle);
		
		jsonObjectBuilderExpected.add("type", "circle")
								.add("x", circle.getX())
				                .add("y", circle.getY());
		
		JsonObject jsonObject = jsonObjectBuilderExpected.build();
		
		String resExpected = jsonObject.toString();
		
		assertEquals(jsonVisitor.getRepresentation(),resExpected);
	}
	
	@Test
	void visitTriangleTest() {
		jsonVisitor.visit(triangle);
		
		jsonObjectBuilderExpected.add("type", "triangle")
								.add("x", triangle.getX())
				                .add("y", triangle.getY());
		
		JsonObject jsonObject = jsonObjectBuilderExpected.build();
		
		String resExpected = jsonObject.toString();
		
		assertEquals(jsonVisitor.getRepresentation(),resExpected);
	}
	
	@Test
	void getRepresentationTest() {
		String res = jsonVisitor.getRepresentation();
		
		JsonObject jsonObject = jsonObjectBuilderExpected.build();
		
		String resExpected = jsonObject.toString();
		
		assertEquals(res,resExpected);
	}
	
	@Test
	void resetTest() {
		jsonObjectBuilderExpected = Json.createObjectBuilder();
		JsonObject jsonObject = jsonObjectBuilderExpected.build();
		String resExpected = jsonObject.toString();
		
		jsonVisitor.reset();
		
		assertEquals(jsonVisitor.getRepresentation(), resExpected);
	}
}