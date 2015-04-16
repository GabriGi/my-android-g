package com.example.computergraphics.controls.actionSet;

import java.util.TimerTask;

import sfogl.integration.Node;
import shadow.math.SFMatrix3f;
import shadow.math.SFVertex3f;

/**
 * Task che si occupa del movimento dell'avatar
 */
public class MoveAvatarTimeTask extends TimerTask{

	private Node avatar, similAvatarBody, all;
	private float startX, startZ, finalX, finalZ, velocityX, velocityZ;
	private float avatarBodyY;
	private int t = 0;
	
	/**
	 * @param avatar il nodo dell'avatar
	 * @param destination la posizione di destinazione
	 * @param velocityX la componente X della velocita'
	 * @param velocityZ la componente Z della velocita'
	 * @param all il nodo alla base del sistema di riferimento
	 */
	public MoveAvatarTimeTask(Node avatar, SFVertex3f destination, float velocityX, float velocityZ, Node all) {
		this.avatar = avatar;
		this.similAvatarBody = avatar.getSonNodes().get(0).clodeNode();
		startX = avatar.getRelativeTransform().getV()[9];
		avatarBodyY = similAvatarBody.getRelativeTransform().getV()[10];
		startZ = avatar.getRelativeTransform().getV()[11];
		this.finalX = destination.getX();
		this.finalZ = destination.getZ();
		this.velocityX = velocityX;
		this.velocityZ = velocityZ;
		this.all = all;
		float angle = (float) (Math.atan2(velocityZ, velocityX)-Math.PI/2);
		avatar.getRelativeTransform().setMatrix(SFMatrix3f.getRotationY(-angle));
	}
	
	@Override
	public void run() {
		float currX = avatar.getRelativeTransform().getV()[9];
		float currY = avatar.getRelativeTransform().getV()[10];
		float currZ = avatar.getRelativeTransform().getV()[11];
		
		float tempCurrX = currX + velocityX;
		float tempCurrZ = currZ + velocityZ;
		if((tempCurrX>finalX&&finalX>startX)||(tempCurrX<finalX&&finalX<startX)) tempCurrX=finalX;
		if((tempCurrZ>finalZ&&finalZ>startZ)||(tempCurrZ<finalZ&&finalZ<startZ)) tempCurrZ=finalZ;
		similAvatarBody.getRelativeTransform().setPosition(tempCurrX, currY+avatarBodyY, tempCurrZ);
		boolean touchObstacle = similAvatarBody.coveredBySonNodes(all.getSonNodes().get(1));
		//Log.d("task", "tXZ "+touchObstacle);
		if(touchObstacle){
			similAvatarBody.getRelativeTransform().setPosition(currX, currY+avatarBodyY, tempCurrZ);
			touchObstacle = similAvatarBody.coveredBySonNodes(all.getSonNodes().get(1));
			//Log.d("task", "tZ "+touchObstacle);
			if(touchObstacle){
				similAvatarBody.getRelativeTransform().setPosition(tempCurrX, currY+avatarBodyY, currZ);
				touchObstacle = similAvatarBody.coveredBySonNodes(all.getSonNodes().get(1));
				//Log.d("task", "tX "+touchObstacle);
				if(touchObstacle){
					 cancel();
				}else{
					currX = tempCurrX;
				}
			}else{
				currZ = tempCurrZ;
			}
			t++;
			if(t>19) cancel();
		}else{
			currX = tempCurrX;
			currZ = tempCurrZ;
			t=0;
		}
		
		avatar.getRelativeTransform().setPosition(currX, currY, currZ);
		
		SFMatrix3f matrix = new SFMatrix3f();
		all.getRelativeTransform().getMatrix(matrix);
		SFVertex3f position = matrix.Mult(new SFVertex3f(-currX, 0, -currZ));
		all.getRelativeTransform().setPosition(position);
		
		if((currX==finalX&&currZ==finalZ)) cancel();
	}
}
