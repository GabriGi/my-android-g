package com.example.computergraphics.controls;

import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

public class AlternativeController implements IController {
	
	private ActionSet actionSet;

	private int viewWidth;
	private int viewHeight;
	
	private static final float CAMERA_SESIBILITY = -0.5f;
	
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
		// TODO Auto-generated method stub
	}
	
    /* ***************************************************************************** */
    /* *************************     OnGestureListener     ************************* */
    /* ***************************************************************************** */
    
	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			float xFactor = distanceX/viewWidth/CAMERA_SESIBILITY;
			float yFactor = distanceY/viewHeight/CAMERA_SESIBILITY;
			Log.d("Gestures", "         - rotation: "+xFactor+" "+yFactor);
			actionSet.rotationCamera(xFactor, yFactor);
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		// TODO Auto-generated method stub
		return false;
	}

    /* ***************************************************************************** */
    /* *************************    OnDoubleTapListener    ************************* */
    /* ***************************************************************************** */
    
	@Override
	public boolean onSingleTapConfirmed(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onDoubleTap(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onDoubleTapEvent(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
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
