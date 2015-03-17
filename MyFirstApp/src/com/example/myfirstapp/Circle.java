package com.example.myfirstapp;

import java.util.Observable;

public class Circle extends Observable{

	private int x,y, radius;
	private int color;
	
	public Circle(int x, int y, int radius, int color) {
		super();
		this.x = x;
		this.y = y;
		this.radius = radius;
		this.color = color;
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
	
	public int getColor() {
		return color;
	}
}
