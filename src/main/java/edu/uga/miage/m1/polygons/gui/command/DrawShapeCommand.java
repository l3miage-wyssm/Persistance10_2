package edu.uga.miage.m1.polygons.gui.command;

import java.util.ArrayList;
import java.util.List;

import edu.uga.miage.m1.polygons.gui.persistence.Visitable;

public class DrawShapeCommand implements Command{
    
    private List<Visitable> shapesList = new ArrayList<>();
    private Visitable shape;

    public DrawShapeCommand(List<Visitable> shapesList, Visitable shape){
        this.shapesList = shapesList;
        this.shape = shape;
    }

    @Override
    public void execute(){
        shapesList.add(shape);
    }

    @Override
    public void undo(){
        shapesList.remove(shape);
    }
}
