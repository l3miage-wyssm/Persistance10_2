package commandTest;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.uga.miage.m1.polygons.gui.JDrawingFrame;
import edu.uga.miage.m1.polygons.gui.command.CommandShape;
import edu.uga.miage.m1.polygons.gui.shapes.Circle;
import edu.uga.miage.m1.polygons.gui.shapes.SimpleShape;
import edu.uga.miage.m1.polygons.gui.shapes.Square;
import edu.uga.miage.m1.polygons.gui.shapes.Triangle;

class CommandShapeTest {

    private List<SimpleShape> shapesList;

    private Circle circle;

    private Triangle triangle;

    private Square square;

    private CommandShape cms;

    private JDrawingFrame frame;

    private JPanel mPanel;

    @BeforeEach
    void SetUp() {
        shapesList = new ArrayList<>();

        circle = new Circle(50, 50);
        triangle = new Triangle(60, 60);
        square = new Square(70, 70);

        shapesList.add(circle);
        shapesList.add(triangle);
        shapesList.add(square);

        frame = new JDrawingFrame("null");

        frame.getShapesList().add(circle);
        frame.getShapesList().add(triangle);
        frame.getShapesList().add(square);

        cms = new CommandShape(square, frame);

        mPanel = new JPanel();
        mPanel.setBackground(java.awt.Color.WHITE);
        mPanel.setLayout(null);
        mPanel.setMinimumSize(new Dimension(400, 400));
        mPanel.addMouseListener(frame);
        mPanel.addMouseMotionListener(frame);

    }

    @Test
    void testUndo() {
        cms.undo();

        Assertions.assertEquals(2, frame.getShapesList().size());
        Assertions.assertEquals("circle", frame.getShapesList().get(0).getType());
        Assertions.assertEquals("triangle", frame.getShapesList().get(1).getType());
        Assertions.assertEquals(25, frame.getShapesList().get(0).getX());
        Assertions.assertEquals(35, frame.getShapesList().get(1).getX());
        Assertions.assertEquals(25, frame.getShapesList().get(0).getY());
        Assertions.assertEquals(35, frame.getShapesList().get(1).getY());

    }

}
