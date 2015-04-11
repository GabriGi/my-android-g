package com.example.computergraphics.controls;

import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

public class BasicController implements IController {

	public static final int CENTERED_MODE = 1;
	public static final int ABSOLUTE_MODE = 2;
	
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

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		if(mode==CENTERED_MODE){
			actionSet.moveAvatarWith((e2.getX()-(viewWidth>>1))/(viewWidth>>1), 
									 ((viewHeight>>1)-e2.getY())/(viewHeight>>1));
		}else if(mode==ABSOLUTE_MODE){
			actionSet.moveAvatarWith((e2.getX()-(viewWidth>>1))/(viewWidth>>1), 
		   			   (viewHeight-(viewHeight>>3)-e2.getY())/(viewHeight));
		}
		return true;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		actionSet.jumpAvatar((e2.getX()-e1.getX())/viewWidth, (e1.getY()-e2.getY())/viewHeight);
		return true;
	}

    /* ***************************************************************************** */
    /* *************************    OnDoubleTapListener    ************************* */
    /* ***************************************************************************** */
    
	@Override
	public boolean onSingleTapConfirmed(MotionEvent e) {
		if(mode==CENTERED_MODE){
			actionSet.moveAvatarTo((e.getX()-(viewWidth>>1))/(viewWidth>>1), 
								   ((viewHeight>>1)-e.getY())/(viewHeight>>1), 
								   ActionSet.VELOCITY_WALK);
		}else if(mode==ABSOLUTE_MODE){
			actionSet.moveAvatarTo((e.getX()-(viewWidth>>1))/(viewWidth>>1), 
					   			   (viewHeight-(viewHeight>>3)-e.getY())/(viewHeight), 
								   ActionSet.VELOCITY_WALK);
		}
		return true;
	}

	@Override
	public boolean onDoubleTap(MotionEvent e) { return true; }

	private int doubleTapCounter = 0;
	private static final int MIN_DTC_FOR_SCROLL = 5;	// min2, max 5.
	
	@Override
	public boolean onDoubleTapEvent(MotionEvent e) {
		if(e.getAction()==MotionEvent.ACTION_DOWN) doubleTapCounter=0;
		doubleTapCounter++;
		Log.d("Gestures", "                 - count: "+doubleTapCounter);
		if(mode==CENTERED_MODE){
			actionSet.moveAvatarTo((e.getX()-(viewWidth>>1))/(viewWidth>>1), 
								   ((viewHeight>>1)-e.getY())/(viewHeight>>1), 
								   ActionSet.VELOCITY_RUN);
		}else if(mode==ABSOLUTE_MODE){
			actionSet.moveAvatarTo((e.getX()-(viewWidth>>1))/(viewWidth>>1), 
		   			   			   (viewHeight-(viewHeight>>3)-e.getY())/(viewHeight), 
					   			   ActionSet.VELOCITY_RUN);
		}
        if(e.getAction()==MotionEvent.ACTION_UP && doubleTapCounter>MIN_DTC_FOR_SCROLL) stopScrolling();
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
