package com.example.computergraphics.controls;

import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

public class DifferentController implements IController {

	private ActionSet actionSet;

	private int viewWidth;
	private int viewHeight;
	
	@Override
	public void setActionsSet(ActionSet actionSet) {
		this.actionSet = actionSet;
	}
	
	@Override
	public void setViewSize(int width, int height) {
		this.viewWidth = width;
		this.viewHeight = height;
	}
	
	@Override
	public void stopScrolling() {
		actionSet.stopMoving();
	}
	
    /* ***************************************************************************** */
    /* *************************     OnGestureListener     ************************* */
    /* ***************************************************************************** */
    
	@Override
	public boolean onDown(MotionEvent e) {
		actionSet.stopMoving();
		return true;
	}

	@Override
	public void onShowPress(MotionEvent e) { /*Nothing to do*/ }

	@Override
	public void onLongPress(MotionEvent e) {
		actionSet.stopMoving();
		float xFactor = (e.getX()-(viewWidth>>1))/(viewWidth>>1);
		Log.d("Gestures", "         - rotation: "+xFactor);
		actionSet.rotateCameraContinuously(xFactor);
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		actionSet.stopMoving();
		float xFactor = (e.getX()-(viewWidth>>1))/viewWidth*10000;
		float yFactor = (e.getY()-(viewHeight>>1))/viewHeight*5000;
		Log.d("Gestures", "         - rotation: "+xFactor+" "+yFactor);
		actionSet.startFlingCamera((int)xFactor, (int)yFactor);
		return true;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		actionSet.moveAvatarWith((e2.getX()-e1.getX())/(viewWidth>>2), (e1.getY()-e2.getY())/(viewHeight>>2));
		return true;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) { return true; }

    /* ***************************************************************************** */
    /* *************************    OnDoubleTapListener    ************************* */
    /* ***************************************************************************** */
	
	@Override
	public boolean onSingleTapConfirmed(MotionEvent e) {
		return true;
	}
	
	private float startX, startY;
	
	@Override
	public boolean onDoubleTap(MotionEvent e) {
		actionSet.stopMoving();
		startX = e.getX();
		startY = e.getY();
		actionSet.jumpAvatar(0, 0);
		return true;
	}

	@Override
	public boolean onDoubleTapEvent(MotionEvent e) {
		actionSet.moveAvatarWith((e.getX()-startX)/(viewWidth>>2), (startY-e.getY())/(viewHeight>>2));
		if(e.getAction()==MotionEvent.ACTION_UP) stopScrolling();
		return true;
	}

    /* *******************************************************************************/
    /* *************************  OnScaleGestureListener   ************************* */
    /* *******************************************************************************/

	@Override
	public boolean onScaleBegin(ScaleGestureDetector detector) { return true; }
    
	@Override
	public boolean onScale(ScaleGestureDetector detector) {
		Log.d("Gestures", "        - zoom:     "+(detector.getScaleFactor()));
		actionSet.zoomCamera((float)detector.getScaleFactor());
		return true;
	}

	@Override
	public void onScaleEnd(ScaleGestureDetector detector) {  }

}
