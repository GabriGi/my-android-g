//package mvc;
//
//import java.awt.event.MouseEvent;
//import java.awt.event.MouseListener;
//import java.awt.event.MouseMotionListener;
//
//public class CircleController implements MouseListener,
//								MouseMotionListener{
//
//	private Circle circle;
//	private boolean dragging;//=false
//	private int deltaX;
//	private int deltaY;
//	
//	public CircleController(Circle circle) {
//		super();
//		this.circle = circle;
//	}
//	
//	@Override
//	public void mousePressed(MouseEvent e) {
//		int mouseX=e.getX();
//		int mouseY=e.getY();
//		int x=circle.getX();
//		int y=circle.getY();
//		int radius=circle.getRadius();
//		if( (x-mouseX)*(x-mouseX)+(y-mouseY)*(y-mouseY)<
//				radius*radius){
//			dragging=true;
//			deltaX=mouseX-x;
//			deltaY=mouseY-y;
//		}
//	}
//	
//
//	@Override
//	public void mouseDragged(MouseEvent e) {
//		if(dragging){
//			int mouseX=e.getX();
//			int mouseY=e.getY();
//			circle.setX(mouseX-deltaX);
//			circle.setY(mouseY-deltaY);
//		}
//	}
//	@Override
//	public void mouseReleased(MouseEvent e) {
//		dragging=false;
//	}
//	
//	@Override
//	public void mouseMoved(MouseEvent e) {
//		
//	}
//	
//	@Override
//	public void mouseExited(MouseEvent e) {
//		
//	}
//	
//	@Override
//	public void mouseEntered(MouseEvent e) {
//		
//	}
//	
//	@Override
//	public void mouseClicked(MouseEvent e) {
//		
//	}
//}
