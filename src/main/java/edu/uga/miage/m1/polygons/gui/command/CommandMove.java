package edu.uga.miage.m1.polygons.gui.command;

import edu.uga.miage.m1.polygons.gui.shapes.SimpleShape;

public class CommandMove implements Command {

    private SimpleShape selectedShape;

    private int x;

    private int y;

    private int startX;

    private int startY;

    public CommandMove(SimpleShape selectedShape) {
        this.selectedShape = selectedShape;
        this.startX = selectedShape.getX();
        this.startY = selectedShape.getY();
    }

    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void execute() {
        selectedShape.setX(x - 25);
        selectedShape.setY(y - 25);
    }

    @Override
    public void undo() {
        selectedShape.setX(startX);
        selectedShape.setY(startY);
    }

}
