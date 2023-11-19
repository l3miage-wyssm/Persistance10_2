package edu.uga.miage.m1.polygons.gui.command;

import java.awt.Graphics2D;

import edu.uga.miage.m1.polygons.gui.JDrawingFrame;
import edu.uga.miage.m1.polygons.gui.shapes.SimpleShape;

public class CommandShape implements Command {

    private SimpleShape shape;

    private JDrawingFrame frame;

    public CommandShape(SimpleShape shape, JDrawingFrame frame) {
        this.shape = shape;
        this.frame = frame;
    }

    @Override
    public void execute() {
        Graphics2D g2 = (Graphics2D) frame.getMPanel().getGraphics();
        shape.draw(g2);
    }

    @Override
    public void undo() {
        frame.getShapesList().remove(shape);
    }
}
