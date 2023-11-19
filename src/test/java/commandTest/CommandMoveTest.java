package commandTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.uga.miage.m1.polygons.gui.command.CommandMove;
import edu.uga.miage.m1.polygons.gui.shapes.Circle;
import edu.uga.miage.m1.polygons.gui.shapes.SimpleShape;

class CommandMoveTest {

    private SimpleShape selectedShape;

    private CommandMove cmv;

    @BeforeEach
    void setUp() {
        selectedShape = new Circle(50, 50);

        cmv = new CommandMove(selectedShape);
    }

    @Test
    void setLocationTest() {
        cmv.setLocation(52, 52);
        cmv.execute();

        Assertions.assertEquals(27, selectedShape.getX());
        Assertions.assertEquals(27, selectedShape.getY());
    }

    @Test
    void testExecute() {
        cmv.setLocation(50, 50);
        cmv.execute();

        Assertions.assertEquals(25, selectedShape.getX());
        Assertions.assertEquals(25, selectedShape.getY());

    }

    @Test
    void testUndo() {
        cmv.execute();
        cmv.undo();

        Assertions.assertEquals(25, selectedShape.getX());
        Assertions.assertEquals(25, selectedShape.getY());
    }
}
