//package mvc;
//
//import java.awt.Color;
//import java.awt.Graphics;
//import java.util.Observable;
//import java.util.Observer;
//
//import javax.swing.JPanel;
//
//public class CircleView extends JPanel implements Observer{
//	private static final long serialVersionUID = 1L;
//	
//	private Circle circle;
//	private boolean filled;
//
//	public CircleView(Circle circle) {
//		super();
//		this.circle = circle;
//		circle.addObserver(this);
//	}
//	
//	public CircleView(Circle circle, boolean filled) {
//		super();
//		this.circle = circle;
//		this.filled = filled;
//		circle.addObserver(this);
//	}
//	
//	@Override
//	public void update(Observable o, Object arg) {
//		repaint();
//	}
//	
//	@Override
//	protected void paintComponent(Graphics g) {
//		super.paintComponent(g);
//		if (filled) {
//			g.setColor(Color.BLUE);
//			g.fillOval(circle.getX()-circle.getRadius(), 
//					circle.getY()-circle.getRadius(), 
//					circle.getRadius()<<1, 
//					circle.getRadius()<<1);
//		} else {
//			g.drawOval(circle.getX()-circle.getRadius(), 
//					circle.getY()-circle.getRadius(), 
//					circle.getRadius()<<1, 
//					circle.getRadius()<<1);
//		}
//	}
//}
