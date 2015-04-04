package com.example.computergraphics.controls;

import java.util.Timer;

import com.example.computergraphics.controls.timerTask.MoveAvatarTimeTask;

import android.content.Context;
import android.util.Log;
import android.widget.Scroller;
import sfogl.integration.Node;
import shadow.math.SFMatrix3f;
import shadow.math.SFVertex3f;

public class ActionSet {

	public static final float VELOCITY_RUN = 0.1f;
	public static final float VELOCITY_WALK = VELOCITY_RUN/2;
	
	private Timer timer = new Timer();
	private static final int TIMER_PERIOD = 50;
	private MoveAvatarTimeTask task;
//	private Scroller scroller;
	
	private float dimensionOfRoom;
	private float scaleOfAll;
	
	private Node all;
	private Node avatar;
	private Node camera;
	
	public ActionSet(Context context, Node node, float dimensionOfRoom, float scaleOfAll) {
//		this.scroller = new Scroller(context);
		this.dimensionOfRoom = dimensionOfRoom;
		this.scaleOfAll = scaleOfAll;
		this.all = node;
		this.avatar = node.getSonNodes().get(0);
		this.camera = node.getSonNodes().get(0);
	}
	
//    /**
//     * 
//     * @return true if is still flinging, false if the scrolling is ended.
//     */
//	public boolean flingCircle() {
//		if(scroller.computeScrollOffset()){
//			int currX = scroller.getCurrX();
//			int currZ = scroller.getCurrY();
//			Log.d("Gestures", currX +" "+currZ);
//			avatar.getRelativeTransform().setPosition(currX, 0, currZ);
//			return true;
//		}else{
//			return false;
//		}
//	}
	
	/**
	 * Permette di muovere l'avatar negli assi X e Z (sistema di riferimento dello schermo)
	 * in una certa posizione (valori compresi tra -1 e 1)
	 */
	public void moveAvatarTo(float destX , float destZ){
		destX = destX/scaleOfAll;
		destZ = 0-destZ/scaleOfAll;
		float spaceX = destX - avatar.getRelativeTransform().getV()[9];
		float spaceZ = destZ - avatar.getRelativeTransform().getV()[11];
		float velX = (float)(VELOCITY_WALK/Math.sqrt(1+(spaceZ/spaceX)*(spaceZ/spaceX)));
		float velZ = (float)(VELOCITY_WALK/Math.sqrt(1+(spaceX/spaceZ)*(spaceX/spaceZ)));
		if(spaceX<0) velX=0-velX;
		if(spaceZ<0) velZ=0-velZ;
		//Log.d("task", "Velocity tot: "+(Math.sqrt(velX*velX+velZ*velZ)));	//Deve essere = VELOCITY_WALK o VELOCITY_RUN
		
//TODO ragionare in questa direzione:
//		SFMatrix3f matrix = new SFMatrix3f();
//		all.getRelativeTransform().getMatrix(matrix);
//		SFVertex3f position = matrix.Mult(new SFVertex3f(-currX, 0, -currZ));
//		all.getRelativeTransform().setPosition(position);
		
		
		if(task!=null){
			task.cancel();
			timer.purge();
		}
		task = new MoveAvatarTimeTask(avatar, destX, destZ, velX, velZ, all, scaleOfAll);
		timer.schedule(task, 0, TIMER_PERIOD);
	}
	
	/**
	 * Permette di muovere l'avatar negli assi X e Z (sistema di riferimento dello schermo)
	 * con velocita' costante (valori compresi tra -1 e 1)
	 */
	public void moveAvatarWith(float velocitaX , float velocitaZ){
		velocitaZ = 0-velocitaZ;
		if(task!=null){
			task.cancel();
			timer.purge();
		}
		task = new MoveAvatarTimeTask(avatar, velocitaX, velocitaZ, velocitaX, velocitaZ, all, scaleOfAll);
		timer.schedule(task, 0, 200);
//		scroller.fling(startX, startY, velX, velZ, -min, min, -min, min);
		//int tempo = 2000;
        //myScroller.startScroll((int)event2.getX(), (int)event2.getY(), (int)velocityX*tempo/4000, (int)velocityY*tempo/4000, tempo);
	}
	
	/**
	 * Permette all'avatar di saltare (sistema di riferimento dello schermo):
	 * movimento verticale sull'asse Y + eventuale spinta in avanti (moveAvatar)
	 */
	public void jumpAvatar(){
		//TODO ABS/REL, direzione
	}
	
	/**
	 * Muove (ruota) la telecamera, spostando quindi la visuale
	 * (asse X) (sistema di riferimento dello schermo)
	 */
	public void rotationCamera(){
		
	}
	
	/**
	 * Avvicina/Allontana  la telecamera, ingrandendo/rimpicciolendo quindi la visuale
	 * (asse Z) (sistema di riferimento dello schermo)
	 */
	public void zoomCamera(){
		
	}
}
