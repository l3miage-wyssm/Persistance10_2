package edu.uga.miage.m1.polygons.gui.command;

import java.awt.Graphics2D;
import java.util.ArrayList;

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
        Graphics2D g2 = (Graphics2D) frame.mPanel.getGraphics();
        shape.draw(g2);
    }

    @Override
    public void undo() {
        System.out.println("dans undo -> shape : " + shape.getType() + " x : " + shape.getX());
        for (SimpleShape shape : frame.shapesList) {
            System.out.println(shape.getType());
        }
        frame.shapesList.remove(shape);
        if (frame.shapesList.isEmpty()) {
            System.out.println("liste vide");
        } else {
            for (SimpleShape shape : frame.shapesList) {
                System.out.println(shape.getType());
            }
        }
    }
}
