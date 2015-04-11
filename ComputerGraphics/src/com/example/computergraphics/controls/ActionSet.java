package com.example.computergraphics.controls;

import java.util.Timer;

import sfogl.integration.Node;
import sfogl.integration.SFCamera;
import shadow.math.SFMatrix3f;
import shadow.math.SFVertex3f;
import android.content.Context;

import com.example.computergraphics.controls.timerTask.JumpAvatarTimeTask;
import com.example.computergraphics.controls.timerTask.MoveAvatarTimeTask;

public class ActionSet{

	public static final float VELOCITY_RUN = 0.05f;
	public static final float VELOCITY_WALK = VELOCITY_RUN/2;
	public static final float SCALE_MIN = 0.5f;
	public static final float SCALE_DEF = 1.0f;
	public static final float SCALE_MAX = 2.0f;
	public static final float ROT_X_MIN = -0.8f;
	public static final float ROT_X_DEF = 0.0f;
	public static final float ROT_X_MAX = 0.8f;
	
	private Timer timer = new Timer();
	private static final int TIMER_PERIOD = 25;
	private MoveAvatarTimeTask moveAvatarTask;
	private JumpAvatarTimeTask jumpAvatarTask;
//	private Scroller scroller;
	
	private float scale = SCALE_DEF;
	private float rotX = ROT_X_DEF;
	private float rotY = 0;
	
	private Node all;
	private Node avatar;
	
	private SFCamera cam;
	
	public ActionSet(Context context, Node node, SFCamera cam) {
//		this.scroller = new Scroller(context);
		this.all = node;
		this.avatar = node.getSonNodes().get(0);
		this.cam = cam;
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
	
	public void stopMoving() {
		if(moveAvatarTask!=null) moveAvatarTask.cancel();
		timer.purge();
	}
	
	private SFVertex3f getXYZFromUV(float destU, float destV) {
		float destX = destU*2;		//Il fattore moltiplicativo non e' verificato, ma da prestazioni migliori
		float destY = 0;
		float destZ = destV*4;		//Il fattore moltiplicativo non e' verificato, ma da prestazioni migliori
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
		
//	//DEBUG - TELETRASPORTO
//		avatar.getRelativeTransform().setPosition(dest);
//		SFVertex3f allDest = new SFVertex3f(); allDest.subtract3f(dest);
//		
//		SFMatrix3f matrix = new SFMatrix3f();
//		all.getRelativeTransform().getMatrix(matrix);
//		SFVertex3f position = matrix.Mult(allDest);
//		all.getRelativeTransform().setPosition(position);
			
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
	
	/**
	 * Muove (ruota) la telecamera, spostando quindi la visuale (assi X e Y)
	 */
	public void rotationCamera(float uFactor, float vFactor){		//TODO Si puo' migliorare
		stopMoving();
		rotX += vFactor;
		rotY += uFactor;
		if(rotX<ROT_X_MIN) rotX=ROT_X_MIN;	//If less, the image is cut out.
		else if(rotX>ROT_X_MAX) rotX=ROT_X_MAX;	//If more, the avatar is no longer visible.
	}
	
	/**
	 * Avvicina/Allontana  la telecamera, ingrandendo/rimpicciolendo quindi la visuale (asse Z)
	 */
	public void zoomCamera(float zoomFactor){		//TODO  Si puo' migliorare
		stopMoving();
		scale = scale*zoomFactor;
		if(scale>SCALE_MAX) scale=SCALE_MAX;
		else if(scale<SCALE_MIN) scale=SCALE_MIN;
	}
}
