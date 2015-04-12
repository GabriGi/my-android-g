package com.example.computergraphics.controls.timerTask;

import java.util.TimerTask;

import com.example.computergraphics.controls.ActionSet;

/**
 * Task che si occupa della rotazione continua e costante nel tempo attorno all'asse Y della telecamera.
 */
public class RotateCameraTimeTask extends TimerTask{
	
	private ActionSet actionSet;
	private float velocityY;
	
	/**
	 * @param actionSet (verra' chiamato il metodo actionSet.rotationCamera(velocityY, 0);
	 * @param incrementY l'incremento da passare ad ogni clock
	 */
	public RotateCameraTimeTask(ActionSet actionSet, float incrementY) {
		this.actionSet = actionSet;
		this.velocityY = incrementY;
	}
	
	@Override
	public void run() {
		actionSet.rotationCamera(velocityY, 0);
	}
}
