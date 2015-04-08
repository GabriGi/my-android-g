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
	public void stopScrolling() {
		actionSet.stopMoving();
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
		startX = e.getX();
		startY = e.getY();
		return true;
	}

	@Override
	public void onShowPress(MotionEvent e) { /*Nothing to do*/ }

	@Override
	public void onLongPress(MotionEvent e) { /*Nothing to do*/ }

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return true;
	}

	private float startX;
	private float startY;
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		if(mode==ABSOLUTE_MODE){
			actionSet.moveAvatarWith((e2.getX()-(viewWidth>>1))/(viewWidth>>1), 
									 (e2.getY()-(viewHeight>>1))/(viewHeight>>1));
		}else if(mode==RELATIVE_MODE){
			actionSet.moveAvatarWith((e2.getX()-startX)/viewWidth, (e2.getY()-startY)/viewHeight);
		}
		return true;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		// TODO
		actionSet.jumpAvatar((e2.getX()-e1.getX())/viewWidth, (e2.getY()-e1.getY())/viewHeight);
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

	private int doubleTapCounter = 0;
	@Override
	public boolean onDoubleTapEvent(MotionEvent e) {
		if(e.getAction()==MotionEvent.ACTION_DOWN) doubleTapCounter=0;
		doubleTapCounter++;
		Log.d("Gestures", "                 - count: "+doubleTapCounter);
		if(mode==ABSOLUTE_MODE){
			actionSet.moveAvatarTo((e.getX()-(viewWidth>>1))/(viewWidth>>1), 
								   (e.getY()-(viewHeight>>1))/(viewHeight>>1), 
								   ActionSet.VELOCITY_RUN);
		}else if(mode==RELATIVE_MODE){
			actionSet.moveAvatarTo((e.getX()-startX)/viewWidth, 
								   (e.getY()-startY)/viewHeight, 
					   			   ActionSet.VELOCITY_RUN);
		}													// min2, max 5.
        if(e.getAction()==MotionEvent.ACTION_UP && doubleTapCounter>5) stopScrolling();
		return true;
	}

    /* *******************************************************************************/
    /* *************************  OnScaleGestureListener   ************************* */
    /* *******************************************************************************/

	private float previousX;
	private float previousY;
	
	@Override
	public boolean onScaleBegin(ScaleGestureDetector detector) {
		previousX = detector.getFocusX();
		previousY = detector.getFocusY();
		return true;
	}
    
	@Override
	public boolean onScale(ScaleGestureDetector detector) {
		// TODO forse non e' un buon metodo usare il getScaleFactor. Meglio forse i getX e getY.
		if(Math.abs(detector.getScaleFactor()-1)<0.025){
			Log.d("Gestures", "        - rotation: "+(Math.abs(detector.getScaleFactor()-1)));
			float xFactor = (float) ((detector.getFocusX() - previousX) / viewWidth);
			float yFactor = (float) ((detector.getFocusY() - previousY) / viewHeight);
			previousX = detector.getFocusX();
			previousY = detector.getFocusY();
			actionSet.rotationCamera(xFactor, yFactor);
		}else{
			Log.d("Gestures", "        - zoom:     "+(Math.abs(detector.getScaleFactor()-1)));
			actionSet.zoomCamera((float)detector.getScaleFactor());
		}
		return true;
	}

	@Override
	public void onScaleEnd(ScaleGestureDetector detector) {
		actionSet.rotationCamera(0, 0);	//This is to force update and avoid graphics bug (Teleport in the next movement)
	}

}
