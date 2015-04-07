package com.example.computergraphics.controls;

import java.util.Timer;

import com.example.computergraphics.controls.timerTask.MoveAvatarTimeTask;

import android.content.Context;
import android.util.Log;
import sfogl.integration.Node;
import shadow.math.SFMatrix3f;
import shadow.math.SFVertex3f;

public class ActionSet{

	public static final float VELOCITY_RUN = 0.05f;
	public static final float VELOCITY_WALK = VELOCITY_RUN/2;
	public static final float MAX_SCALE = 1.0f;
	public static final float MIN_SCALE = 0.1f;
	
	private Timer timer = new Timer();
	private static final int TIMER_PERIOD = 25;
	private MoveAvatarTimeTask moveAvatarTask;
//	private Scroller scroller;
	
	private float scale;
	private float rotX;
	private float rotY;
	
	private Node all;
	private Node avatar;
//	private Node camera;
	
	public ActionSet(Context context, Node node, float scaleOfAll, float rotX, float rotY) {
//		this.scroller = new Scroller(context);
		this.all = node;
		this.avatar = node.getSonNodes().get(0);
//		this.camera = node.getSonNodes().get(0);
		this.scale = scaleOfAll;
		this.rotX = rotX;
		this.rotY = rotY;
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
	}
	
	private void cancelAllActiveTask() {
		stopMoving();
		timer.purge();
	}
	
	private SFVertex3f getXYZFromUV(float destU, float destV) {
		float destX = destU/scale;
		float destZ = (0-destV/scale)/(float)Math.sin(rotX);
		SFMatrix3f matrix = SFMatrix3f.getRotationY(-rotY);
		return matrix.Mult(new SFVertex3f(destX, 0, destZ));
	}
	
	/**
	 * Permette di muovere l'avatar negli assi X e Z (sistema di riferimento dello schermo)
	 * in una certa posizione (valori compresi tra -1 e 1)
	 */
	public void moveAvatarTo(float destU, float destV, float velocity){
		SFVertex3f start = new SFVertex3f(); avatar.getRelativeTransform().getPosition(start);
		SFVertex3f dest = getXYZFromUV(destU, destV); dest.add3f(start);
		SFVertex3f space = new SFVertex3f(dest.getV()); space.subtract3f(start);
		float velX = (float)(velocity/Math.sqrt(1+(space.getZ()/space.getX())*(space.getZ()/space.getX())));
		float velZ = (float)(velocity/Math.sqrt(1+(space.getX()/space.getZ())*(space.getX()/space.getZ())));
		if(space.getX()<0) velX=0-velX;
		if(space.getZ()<0) velZ=0-velZ;
		
		cancelAllActiveTask();
		moveAvatarTask = new MoveAvatarTimeTask(avatar, dest, velX, velZ, all);
		timer.schedule(moveAvatarTask, 0, TIMER_PERIOD);
		
//		Log.d("task", "start: "+start);
//		Log.d("task", "dest: "+dest);
//		Log.d("task", "space: "+space);
		
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
	 * Permette di muovere l'avatar negli assi X e Z (sistema di riferimento dello schermo)
	 * con velocita' costante (valori compresi tra -1 e 1)
	 */
	public void moveAvatarWith(float velocityU , float velocityV){
		cancelAllActiveTask();
		float velocity = (float)Math.max(Math.abs(velocityU), Math.abs(velocityV)) * VELOCITY_RUN;
		moveAvatarTo(velocityU, velocityV, velocity);
	}
	
	/**
	 * Permette all'avatar di saltare (sistema di riferimento dello schermo):
	 * movimento verticale sull'asse Y + eventuale spinta in avanti (moveAvatar)
	 */
	public void jumpAvatar(){		//TODO ABS/REL, direzione
		cancelAllActiveTask();
	}
	
	/**
	 * Muove (ruota) la telecamera, spostando quindi la visuale
	 * (asse X) (sistema di riferimento dello schermo)
	 */
	public void rotationCamera(float yFactor, float xFactor){		//TODO Da sistemare (Graphic bug)
		cancelAllActiveTask();
		
		SFMatrix3f matrix = new SFMatrix3f();
		all.getRelativeTransform().getMatrix(matrix);
		
		rotY += yFactor;
		rotX += xFactor;
		if(rotX<0.1f) rotX=0.2f;	//Cant't be 0!!
		if(rotX>1.5f) rotX=1.5f;	//Little less of (float)Math.PI/2;
		
		SFVertex3f allDest = new SFVertex3f(); avatar.getRelativeTransform().getPosition(allDest); allDest.mult(-1);

		matrix.MultMatrix(SFMatrix3f.getRotationY(xFactor));
		matrix.MultMatrix(SFMatrix3f.getRotationY(yFactor));
		SFVertex3f position = matrix.Mult(allDest);
		all.getRelativeTransform().setPosition(position);
	}
	
	/**
	 * Avvicina/Allontana  la telecamera, ingrandendo/rimpicciolendo quindi la visuale
	 * (asse Z) (sistema di riferimento dello schermo)
	 */
	public void zoomCamera(float zoomFactor){		//TODO Da sistemare (Graphic bug)
		cancelAllActiveTask();
		
		SFMatrix3f matrix = new SFMatrix3f();
		all.getRelativeTransform().getMatrix(matrix);
		
		scale = scale*zoomFactor;
		if(scale>MAX_SCALE) scale=MAX_SCALE;
		else if(scale<MIN_SCALE) scale=MIN_SCALE;
		
		SFVertex3f allDest = new SFVertex3f(); avatar.getRelativeTransform().getPosition(allDest); allDest.mult(-1);
		
		matrix.MultMatrix(SFMatrix3f.getScale(scale, scale, scale));
		SFVertex3f position = matrix.Mult(allDest);
		all.getRelativeTransform().setPosition(position);
	}
}
