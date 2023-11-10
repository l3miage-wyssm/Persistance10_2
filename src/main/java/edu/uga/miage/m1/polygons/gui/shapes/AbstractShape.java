package edu.uga.miage.m1.polygons.gui.shapes;

import edu.uga.miage.m1.polygons.gui.persistence.Visitable;
import edu.uga.miage.m1.polygons.gui.persistence.Visitor;

public abstract class AbstractShape implements SimpleShape, Visitable{
	
	private int mX;
	
	private int mY;
	
	private String type;
	
	protected AbstractShape(int x,int y, String type) {
		mX = x-25;
		mY= y-25;
		this.type=type;
	}
	
	@Override
	public int getX() {
		return mX;
	}
	
	@Override
	public int getY() {
		return mY;
	}
	
	@Override
	public String getType() {
		return type;
	}
	
	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
}