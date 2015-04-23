package com.example.computergraphics.controls;

import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import com.example.computergraphics.controls.actionSet.ActionSet;

public class AlternativeController implements IController {
	
	private ActionSet actionSet;

	private int viewWidth;
	private int viewHeight;
	private float lastVelocityY=0;
	
	private static final float CAMERA_SESIBILITY = 0.5f;
	
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
	public void stopScrolling() { /*Nothing to do*/ }
	
    /* ***************************************************************************** */
    /* *************************     OnGestureListener     ************************* */
    /* ***************************************************************************** */
    
	@Override
	public boolean onDown(MotionEvent e) {
		actionSet.stopFlinging();
		return true; }

	@Override
	public void onShowPress(MotionEvent e) { /*Nothing to do*/ }

	@Override
	public void onLongPress(MotionEvent e) {
		lastVelocityY = (viewHeight-e.getY())/viewHeight;
		actionSet.jumpAvatar(0, lastVelocityY);
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) { return true; }

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			float xFactor = distanceX/viewWidth/CAMERA_SESIBILITY;
			float yFactor = distanceY/viewHeight/CAMERA_SESIBILITY;
//			Log.d("Gestures", "         - rotation: "+xFactor+" "+yFactor);
			actionSet.rotationCamera(xFactor, yFactor);
			if(actionSet.isMoving()){
				actionSet.moveAvatarWith(0, lastVelocityY);
			}
		return true;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		if(actionSet.isMoving()){
			actionSet.startFlingCamera((int)-velocityX, 0, 0, lastVelocityY);
		}else{
			actionSet.startFlingCamera((int)-velocityX, 0);
		}
		return true;
	}

    /* ***************************************************************************** */
    /* *************************    OnDoubleTapListener    ************************* */
    /* ***************************************************************************** */
    
	@Override
	public boolean onSingleTapConfirmed(MotionEvent e) {
		if(!actionSet.isMoving()){
			lastVelocityY = (viewHeight-e.getY())/viewHeight;
			actionSet.moveAvatarWith(0, lastVelocityY);
		}else{
			actionSet.stopMoving();
		}
		return true;
	}

	private float previousY;
	private int doubleTapCounter = 0;
	private static final int MIN_DTC_FOR_SCROLL = 6;	// min2, max 5.
	
	@Override
	public boolean onDoubleTap(MotionEvent e) {
		previousY = e.getY();
		return true;
	}
	
	@Override
	public boolean onDoubleTapEvent(MotionEvent e) {
		if(e.getAction()==MotionEvent.ACTION_DOWN) doubleTapCounter=0;
		doubleTapCounter++;
//		Log.d("Gestures", "                 - count: "+doubleTapCounter);
        if(doubleTapCounter>MIN_DTC_FOR_SCROLL && e.getAction()!=MotionEvent.ACTION_UP){
			float zoomFactor = e.getY()/previousY;
			previousY = e.getY();
    		actionSet.zoomCamera(zoomFactor);
        }else if(e.getAction()==MotionEvent.ACTION_UP && doubleTapCounter<=MIN_DTC_FOR_SCROLL){
    		actionSet.moveAvatarTo(0, 1, ActionSet.VELOCITY_RUN);
        }
		return true;
	}

    /* ***************************************************************************** */
    /* *************************  OnScaleGestureListener   ************************* */
    /* ***************************************************************************** */
	
	/* In teoria secondo le istruzioni qui non deve fare niente, qindi return false.
	 * Se volessi fare anche qui lo zoom, basta mettere return true.
	 */
	@Override
	public boolean onScaleBegin(ScaleGestureDetector detector) { return false; }	
    
	@Override
	public boolean onScale(ScaleGestureDetector detector) {
//		Log.d("Gestures", "        - zoom:     "+(detector.getScaleFactor()));
		actionSet.zoomCamera((float)detector.getScaleFactor());
		return true;
	}

	@Override
	public void onScaleEnd(ScaleGestureDetector detector) {  }
}
