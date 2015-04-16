package com.example.computergraphics.controls.actionSet;

import java.util.Timer;
import java.util.TimerTask;

import sfogl.integration.Node;
import sfogl.integration.SFCamera;
import shadow.math.SFMatrix3f;
import shadow.math.SFVertex3f;
import android.content.Context;
import android.widget.Scroller;

public class ActionSet{

	public static final float VELOCITY_RUN = 0.05f;
	public static final float VELOCITY_WALK = VELOCITY_RUN/2;
	public static final float SCALE_MIN = 0.5f;
	public static final float SCALE_DEF = 1.0f;
	public static final float SCALE_MAX = 2.0f;
	public static final float ROT_X_MIN = -0.8f;
	public static final float ROT_X_DEF = 0.0f;
	public static final float ROT_X_MAX = 0.8f;
	public static final int FLING_EVENT_NULL = 0;
	public static final int FLING_EVENT_CAMERA = 1;
	
	private Timer timer = new Timer();
	private static final int TIMER_PERIOD = 25;
	private MoveAvatarTimeTask moveAvatarTask;
	private JumpAvatarTimeTask jumpAvatarTask;
	
	private RotateCameraTimeTask rotateCameraTask;
	private Scroller myScroller;
	private float prevScrollerX, prevScrollerY;
	private int flingEvent = FLING_EVENT_NULL;
	
	private float roomDimension;
	private float scale = SCALE_DEF;
	private float rotX = ROT_X_DEF;
	private float rotY = 0;
	private boolean jumpEnabled = false;	//Occhio a cambiare il valore nel menu_main.xml
	
	private Node all;
	private Node avatar;
	
	private SFCamera cam;
	
	public ActionSet(Context context, Node node, SFCamera cam) {
		this.myScroller = new Scroller(context);
		this.all = node;
		this.avatar = node.getSonNodes().get(0);
		this.cam = cam;
	}
	
	public void setRoomDimension(float roomDimension) {
		this.roomDimension = roomDimension;
	}
	
	public void setJumpEnabled(boolean jumpEnabled) {
		this.jumpEnabled = jumpEnabled;
	}
	
	public float getScale() {
		return scale;
	}
	
	public float getRotX() {
		return rotX;
	}
	
	public float getRotY() {
		return rotY;
	}
	
	public int getFlingEvent() {
		return flingEvent;
	}
	
	public void stopMoving() {
		myScroller.forceFinished(true);
		if(moveAvatarTask!=null) moveAvatarTask.cancel();
		if(rotateCameraTask!=null) rotateCameraTask.cancel();
		timer.purge();
	}
	
	public void stopMovingAndJumping() {
		if(jumpAvatarTask!=null) jumpAvatarTask.cancel();
		stopMoving();
	}
	
	public void restoreDefaultValues() {
		scale = SCALE_DEF;
		rotX = ROT_X_DEF;
		rotY = 0;
		this.avatar = all.getSonNodes().get(0);
		SFVertex3f position = new SFVertex3f(); avatar.getRelativeTransform().getPosition(position);
		all.getRelativeTransform().setPosition(-position.getX(),-position.getY(),-position.getZ());
	}
	
	private SFVertex3f getXYZFromUV(float destU, float destV) {
		float destX = destU*roomDimension;
		float destY = 0;
		float destZ = destV*roomDimension*2;
		SFVertex3f dest = cam.getWorldRotation(SFMatrix3f.getRotationY(rotY)).Mult(new SFVertex3f(destX, destY, destZ));
		return dest;
	}
	
	/**
	 * Permette di muovere l'avatar in una certa posizione degli assi X e Z, 
	 * prendendo come parametri le due componenti (u e v) dello schermo (con valori compresi tra -1 e 1)
	 */
	public void moveAvatarTo(float destU, float destV, float velocity){
		SFVertex3f start = new SFVertex3f(); avatar.getRelativeTransform().getPosition(start);
		SFVertex3f space = getXYZFromUV(destU, destV); 
		SFVertex3f dest = new SFVertex3f(start.getV()); dest.add3f(space);
		float velX = (float)(velocity/Math.sqrt(1+(space.getZ()/space.getX())*(space.getZ()/space.getX())));
		float velZ = (float)(velocity/Math.sqrt(1+(space.getX()/space.getZ())*(space.getX()/space.getZ())));
		if(space.getX()<0) velX=0-velX;
		if(space.getZ()<0) velZ=0-velZ;
		
		stopMoving();
		moveAvatarTask = new MoveAvatarTimeTask(avatar, dest, velX, velZ, all);
		timer.schedule(moveAvatarTask, 0, TIMER_PERIOD);
	}
	
	/**
	 * Permette di muovere l'avatar lungo gli assi X e Z passasndogli una velocita' compresa tra -1 e 1 
	 * (nelle due componenti (u e v) dello schermo)
	 */
	public void moveAvatarWith(float velocityU , float velocityV){
		float velocity = (float)Math.max(Math.abs(velocityU), Math.abs(velocityV)) * VELOCITY_RUN;
		if(velocity>VELOCITY_RUN) velocity = VELOCITY_RUN;
		moveAvatarTo(velocityU, velocityV, velocity);
	}
	
	/**
	 * Permette all'avatar di saltare: movimento verticale sull'asse Y
	 *  + eventuale spinta in avanti (nelle due componenti (u e v) dello schermo)
	 */
	public void jumpAvatar(float directionU , float directionV){
		moveAvatarWith(directionU, directionV);
		if(jumpEnabled){
			if(jumpAvatarTask != null){
				if (!jumpAvatarTask.isJumping()) {
					jumpAvatarTask.cancel();
					jumpAvatarTask = new JumpAvatarTimeTask(avatar, JumpAvatarTimeTask.START_VELOCITY, all, TIMER_PERIOD);
					timer.schedule(jumpAvatarTask, TIMER_PERIOD / 2, TIMER_PERIOD);
				}
			}else{
				jumpAvatarTask = new JumpAvatarTimeTask(avatar, JumpAvatarTimeTask.START_VELOCITY, all, TIMER_PERIOD);
				timer.schedule(jumpAvatarTask, TIMER_PERIOD / 2, TIMER_PERIOD);
			}
		}
	}
	
	/**
	 * Muove (ruota) la telecamera, spostando quindi la visuale (assi X e Y)
	 */
	public void rotationCamera(float uFactor, float vFactor){
		rotX += vFactor;
		rotY += uFactor;
		if(rotX<ROT_X_MIN) rotX=ROT_X_MIN;
		else if(rotX>ROT_X_MAX) rotX=ROT_X_MAX;
		if(rotY>=2*Math.PI) rotY-=2*Math.PI;
		else if(rotY<0) rotY+=2*Math.PI;
	}
	
	/**
	 * Avvicina/Allontana  la telecamera, ingrandendo/rimpicciolendo quindi la visuale (asse Z)
	 */
	public void zoomCamera(float zoomFactor){
		scale = scale*zoomFactor;
		if(scale>SCALE_MAX) scale=SCALE_MAX;
		else if(scale<SCALE_MIN) scale=SCALE_MIN;
	}
	
	/**
	 * Inizia la rotazione della telecamera settando uno scroller, spostando quindi la visuale (assi X e Y).
	 * Usa {@link #flingCamera()} per impostare la posizione durante l'animazione.
	 */
	public void startFlingCamera(int velocityU, int velocityV){
		flingEvent = FLING_EVENT_CAMERA;
		prevScrollerX = 0;
		prevScrollerY = 0;
        myScroller.startScroll(0, 0, (int)(0.628*velocityV), (int)(0.628*velocityU), 2000);
    }
	
	/**
	 * Imposta la posizione della telecamera durante l'animazione iniziata da {@link #startFlingCamera(int, int)}.
	 */
	public boolean flingCamera() {
		if(myScroller.computeScrollOffset()){
			rotationCamera(myScroller.getCurrY()/1000f - prevScrollerY, myScroller.getCurrX()/1000f - prevScrollerX);
			prevScrollerY = myScroller.getCurrY()/1000f;
			prevScrollerX = myScroller.getCurrX()/1000f;
			return true;
		}else{
			flingEvent = FLING_EVENT_NULL;
			return false;
		}
	}
	
	public void rotateCameraContinuously(float velocityU){
		stopMoving();
		rotateCameraTask = new RotateCameraTimeTask(velocityU/32);
		timer.schedule(rotateCameraTask, 0, TIMER_PERIOD);
	}
	
	
	/**
	 * Task che si occupa della rotazione continua e costante nel tempo attorno all'asse Y della telecamera.
	 */
	private class RotateCameraTimeTask extends TimerTask{
		
		private float velocityY;
		
		/**
		 * Verra' chiamato il metodo 
		 * {@link ActionSet}.{@link #rotationCamera(float, float)}
		 * @param incrementY l'incremento da passare ad ogni clock
		 */
		public RotateCameraTimeTask(float incrementY) {
			this.velocityY = incrementY;
		}
		
		@Override
		public void run() {
			rotationCamera(velocityY, 0);
		}
	}
}
