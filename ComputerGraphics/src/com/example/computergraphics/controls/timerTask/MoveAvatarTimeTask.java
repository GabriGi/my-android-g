package com.example.computergraphics.controls.timerTask;

import java.util.TimerTask;

import sfogl.integration.Node;
import shadow.math.SFMatrix3f;
import shadow.math.SFVertex3f;
import android.util.Log;

/**
 * Questo e' il task che il timer in playlistManager deve eseguire.
 */
public class MoveAvatarTimeTask extends TimerTask{

	private Node avatar, similAvatarBody, all;
	private float startX, startZ, currX, currZ, finalX, finalZ, velocityX, velocityZ;
	private float avatarBodyY;
	
	/**
	 *
	 * @param startX Il tempo di fine del video attuale
	 * @param videoManager Il gestore dei video
	 * @param playlistManager Il gestore della playlist
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
	}

	/**
	 * Se il player e' in esecuzione(non e' in pausa),
	 * calcola il tempo rimanente del video e
	 * passa il tempo attuale del video al playlistManager.
	 * Se il tempo rimanenente del video non cambia dal precedente campionamento,
	 * il video e' finito e passa al successivo della playlist.
	 */
	@Override
	public void run() {
		currX = avatar.getRelativeTransform().getV()[9];
		currZ = avatar.getRelativeTransform().getV()[11];
		currX += velocityX;
		currZ += velocityZ;
		if((currX>finalX&&finalX>startX)||(currX<finalX&&finalX<startX)) currX=finalX;
		if((currZ>finalZ&&finalZ>startZ)||(currZ<finalZ&&finalZ<startZ)) currZ=finalZ;
		similAvatarBody.getRelativeTransform().setPosition(currX, avatarBodyY, currZ);
		boolean touchObstacle = similAvatarBody.coveredBySonNodes(all.getSonNodes().get(2));
		Log.d("task", "msg");
		if(!touchObstacle){
			avatar.getRelativeTransform().setPosition(currX, 0, currZ);
		}else{
			cancel();
		}
		
		SFMatrix3f matrix = new SFMatrix3f();
		all.getRelativeTransform().getMatrix(matrix);
		SFVertex3f position = matrix.Mult(new SFVertex3f(-currX, 0, -currZ));
		all.getRelativeTransform().setPosition(position);
		
		if((currX==finalX&&currZ==finalZ)) cancel();
	}
}
