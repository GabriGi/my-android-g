package com.example.computergraphics.controls.actionSet;

import java.util.TimerTask;

import com.example.computergraphics.MyNode;

import shadow.math.SFMatrix3f;
import shadow.math.SFVertex3f;

/**
 * Task che si occupa del movimento dell'avatar
 */
public class MoveAvatarTimeTask extends TimerTask{

	private MyNode avatar, similAvatar, all;
	private float startX, startZ, finalX, finalZ, velocityX, velocityZ;
	private int t = 0;
	
	/**
	 * @param avatar il nodo dell'avatar
	 * @param destination la posizione di destinazione
	 * @param velocityX la componente X della velocita'
	 * @param velocityZ la componente Z della velocita'
	 * @param all il nodo alla base del sistema di riferimento
	 */
	public MoveAvatarTimeTask(MyNode avatar, SFVertex3f destination, float velocityX, float velocityZ, MyNode all) {
		this.avatar = avatar;
		this.similAvatar = avatar.cloneSingleNodeWithoutSons();
		startX = avatar.getPosX();
		startZ = avatar.getPosZ();
		this.finalX = destination.getX();
		this.finalZ = destination.getZ();
		this.velocityX = velocityX;
		this.velocityZ = velocityZ;
		this.all = all;
		float angle = (float) (Math.atan2(velocityZ, velocityX)-Math.PI/2);
		SFMatrix3f m = SFMatrix3f.getScale(avatar.getScaleX(), avatar.getScaleY(), avatar.getScaleZ());
		avatar.getRelativeTransform().setMatrix(m.MultMatrix(SFMatrix3f.getRotationY(-angle)));
	}
	
	@Override
	public void run() {
		float currX = avatar.getPosX();
		float currY = avatar.getPosY();
		float currZ = avatar.getPosZ();
		
		float tempCurrX = currX + velocityX;
		float tempCurrZ = currZ + velocityZ;
		if((tempCurrX>finalX&&finalX>startX)||(tempCurrX<finalX&&finalX<startX)) tempCurrX=finalX;
		if((tempCurrZ>finalZ&&finalZ>startZ)||(tempCurrZ<finalZ&&finalZ<startZ)) tempCurrZ=finalZ;
		similAvatar.setPosition(tempCurrX, currY, tempCurrZ);
		boolean touchObstacle = similAvatar.coveredBySonNodes(((MyNode)all.getSonNodes().get(1)));
		//Log.d("task", "tXZ "+touchObstacle);
		if(touchObstacle){
			similAvatar.setPosition(currX, currY, tempCurrZ);
			touchObstacle = similAvatar.coveredBySonNodes(((MyNode)all.getSonNodes().get(1)));
			//Log.d("task", "tZ "+touchObstacle);
			if(touchObstacle){
				similAvatar.setPosition(tempCurrX, currY, currZ);
				touchObstacle = similAvatar.coveredBySonNodes(((MyNode)all.getSonNodes().get(1)));
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
		
		avatar.setPosition(currX, currY, currZ);
		
		SFMatrix3f matrix = new SFMatrix3f();
		all.getRelativeTransform().getMatrix(matrix);
		SFVertex3f position = matrix.Mult(new SFVertex3f(-currX, 0, -currZ));
		all.setPosition(position);
		
		if((currX==finalX&&currZ==finalZ)) cancel();
	}
}
