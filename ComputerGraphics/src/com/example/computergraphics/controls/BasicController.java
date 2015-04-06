package com.example.computergraphics.controls;

import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

public class BasicController implements IController {

	public static final int ABSOLUTE_MODE = 0;
	public static final int RELATIVE_MODE = 0;
	
	private int mode;
	private ActionSet actionSet;

	private int viewWidth;
	private int viewHeight;
	
	public BasicController(int mode){
		this.mode = mode;
	}
	
	@Override
	public void setActionsSet(ActionSet actionSet) {
		this.actionSet = actionSet;
	}
	
	@Override
	public void setViewSize(int width, int height) {
		this.viewWidth = width;
		this.viewHeight = height;
	}
	
    /* ***************************************************************************** */
    /* *************************     OnGestureListener     ************************* */
    /* ***************************************************************************** */
    
	@Override
	public boolean onDown(MotionEvent e) {
		return true;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO effetto molla
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO switch
		actionSet.jumpAvatar();
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return true;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		if(mode==ABSOLUTE_MODE){
			actionSet.moveAvatarWith((e2.getX()-(viewWidth>>1))/(viewWidth>>1), 
									 (e2.getY()-(viewHeight>>1))/(viewHeight>>1));
		}else if(mode==RELATIVE_MODE){
//			actionSet.moveAvatarWith(e2.getX()-e1.getX(), e2.getY()-e1.getY());
		}
		return true;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		// TODO
		actionSet.jumpAvatar();
		return true;
	}

    /* ***************************************************************************** */
    /* *************************    OnDoubleTapListener    ************************* */
    /* ***************************************************************************** */
    
	@Override
	public boolean onSingleTapConfirmed(MotionEvent e) {
		actionSet.moveAvatarTo((e.getX()-(viewWidth>>1))/(viewWidth>>1), 
							   (e.getY()-(viewHeight>>1))/(viewHeight>>1), 
							   ActionSet.VELOCITY_WALK);
		return true;
	}

	@Override
	public boolean onDoubleTap(MotionEvent e) {
		return true;
	}

	@Override
	public boolean onDoubleTapEvent(MotionEvent e) {
		// TODO switch (probabilmente non serve ed il metodo e' a posto cosi' :))
		actionSet.moveAvatarTo((e.getX()-(viewWidth>>1))/(viewWidth>>1), 
							   (e.getY()-(viewHeight>>1))/(viewHeight>>1), 
							   ActionSet.VELOCITY_RUN);
		return true;
	}

    /* *******************************************************************************/
    /* *************************  OnScaleGestureListener   ************************* */
    /* *******************************************************************************/

	@Override
	public boolean onScaleBegin(ScaleGestureDetector detector) {
		return true;
	}
    
	@Override
	public boolean onScale(ScaleGestureDetector detector) {
		// TODO non e' un buon metodo usare il getScaleFactor. Meglio forse i getX e getY.
		if(Math.abs(detector.getScaleFactor()-1)<0.025){
			Log.d("Gestures", "        - rotation: "+(Math.abs(detector.getScaleFactor()-1)));
			actionSet.rotationCamera();
		}else{
			Log.d("Gestures", "        - zoom:     "+(Math.abs(detector.getScaleFactor()-1)));
			actionSet.zoomCamera();
		}
		return true;
	}

	@Override
	public void onScaleEnd(ScaleGestureDetector detector) {
	}

}
