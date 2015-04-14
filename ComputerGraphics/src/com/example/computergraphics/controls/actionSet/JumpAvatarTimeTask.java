package com.example.computergraphics.controls.actionSet;

import java.util.TimerTask;

import sfogl.integration.Node;

/**
 * Task che si occupa del movimento verticale dell'avatar
 */
public class JumpAvatarTimeTask extends TimerTask{
	
	private float timerPeriod = 0.025f;
	private float t = 0;
	private static final float OBSTACLE_HEIGHT = 2*0.25f;
	private static final float a = 9.8f/2;
	public static final float START_VELOCITY = (float)Math.sqrt(4*a*OBSTACLE_HEIGHT)*1.15f;

	private Node avatar, similAvatarBody, all;
	private float startY, startVelocityY;
	private float avatarBodyY;
	
	private boolean jumping = true;
	private float lastUsefulT = 0;
	
	/**
	 * @param avatar il nodo dell'avatar
	 * @param destination la posizione di destinazione
	 * @param velocityX la componente X della velocita'
	 * @param velocityZ la componente Z della velocita'
	 * @param all il nodo alla base del sistema di riferimento
	 */
	public JumpAvatarTimeTask(Node avatar, float startVelocityY, Node all, float timerPeriod) {
		this.avatar = avatar;
		this.similAvatarBody = avatar.getSonNodes().get(0).clodeNode();
		avatarBodyY = similAvatarBody.getRelativeTransform().getV()[10];
		//avatar.getRelativeTransform().getV()[10] = 2*avatarBodyY;	//DEBUG Teletrasporto a mezz'aria.
		startY = avatar.getRelativeTransform().getV()[10];
		this.startVelocityY = startVelocityY;
		this.all = all;
		this.timerPeriod = timerPeriod/1000;
	}
	
	public boolean isJumping() {
		return jumping;
	}
	
	@Override
	public void run() {
		float currX = avatar.getRelativeTransform().getV()[9];
		float currY = avatar.getRelativeTransform().getV()[10];
		float currZ = avatar.getRelativeTransform().getV()[11];
		
		t += timerPeriod;
		float tempCurrY = startY+startVelocityY*t-a*t*t;
		if(tempCurrY<0) tempCurrY=0;
		similAvatarBody.getRelativeTransform().setPosition(currX, tempCurrY+avatarBodyY, currZ);
		boolean touchObstacle = similAvatarBody.coveredBySonNodes(all.getSonNodes().get(1));
		//Log.d("task", "tY "+touchObstacle);
		if(!touchObstacle){
			currY = tempCurrY;
			lastUsefulT = t;
			jumping = true;
			avatar.getRelativeTransform().setPosition(currX, currY, currZ);
		}else{
			//TODO Appare ridicolo che rimanga sull'ostacolo anche se appoggia solo una piccola parte del body.
			jumping = false;
			t = lastUsefulT;
		}
		if(currY==0){
			jumping = false;
			cancel();
		}
	}
}
