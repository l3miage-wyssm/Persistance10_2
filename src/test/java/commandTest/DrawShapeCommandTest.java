package commandTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.uga.miage.m1.polygons.gui.command.DrawShapeCommand;
import edu.uga.miage.m1.polygons.gui.persistence.Visitable;
import edu.uga.miage.m1.polygons.gui.shapes.Circle;
import edu.uga.miage.m1.polygons.gui.shapes.Square;
import edu.uga.miage.m1.polygons.gui.shapes.Triangle;


class DrawShapeCommandTest {

	private Visitable circle = new Circle(5,5) ;
	
	private Visitable triangle = new Triangle(5,5);
	
	private Visitable square = new Square(5,5);
	
	private List<Visitable> shapesList = new ArrayList<>();
	
	private List<Visitable> shapesListExpected = new ArrayList<>();
	
	private DrawShapeCommand drawCircle = new DrawShapeCommand(shapesList,circle);
	
	private DrawShapeCommand drawTriangle = new DrawShapeCommand(shapesList,triangle);
	
	private DrawShapeCommand drawSquare = new DrawShapeCommand(shapesList, square);
	
	@BeforeEach
	void setUp(){
		shapesListExpected.clear();
		shapesList.clear();
	}
	
	@Test
	void testExecuteCircle() {
		drawCircle.execute();
		
		shapesListExpected.add(circle);
		assertEquals(shapesList.get(0), shapesListExpected.get(0));
	}
	
	@Test
	void testExecuteTriangle(){
		drawTriangle.execute();
		
		shapesListExpected.add(triangle);
		assertEquals(shapesList.get(0),shapesListExpected.get(0));
	}
	
	@Test
	void testExecuteSquare(){
		drawSquare.execute();
		
		shapesListExpected.add(square);
		assertEquals(shapesList.get(0),shapesListExpected.get(0));
	}
	
	@Test
	void testUndoCircle() {
		drawCircle.execute();
		drawCircle.undo();
		
		Assertions.assertTrue(shapesList.isEmpty());
	}
	
	@Test
	void testUndoTriangle() {
		drawTriangle.execute();
		drawTriangle.undo();
		
		Assertions.assertTrue(shapesList.isEmpty());
	}
	
	@Test
	void testUndoSquare() {
		drawSquare.execute();
		drawSquare.undo();
		
		Assertions.assertTrue(shapesList.isEmpty());
	}
}