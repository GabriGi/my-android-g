package mvc;

import java.util.Observable;

public class Circle extends Observable{

	private int x,y;
	private int radius;
	
	public Circle(int x, int y, int radius) {
		super();
		this.x = x;
		this.y = y;
		this.radius = radius;
	}
	
	public void setX(int x) {
		this.x = x;
		setChanged();
		notifyObservers();
	}
	
	public void setY(int y) {
		this.y = y;
		setChanged();
		notifyObservers();
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getRadius() {
		return radius;
	}
	
	public void setRadius(int radius) {
		this.radius = radius;
	}
}
