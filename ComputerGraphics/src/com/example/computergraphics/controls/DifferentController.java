package com.example.computergraphics.controls;

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
		// TODO Auto-generated method stub
//		actionSet.moveAvatarWith((e2.getX()-e1.getX())/viewWidth, 
//				 				 (e1.getY()-e2.getY())/viewHeight);
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
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
	
//	private float startX, startY;
	
	@Override
	public boolean onDoubleTap(MotionEvent e) {
		// TODO Auto-generated method stub
//		startX = e.getX();
//		startY = e.getY();
		return false;
	}

	@Override
	public boolean onDoubleTapEvent(MotionEvent e) {
		// TODO Auto-generated method stub
//		actionSet.moveAvatarTo((e.getX()-startX)/viewWidth, (startY-e.getY())/viewHeight, ActionSet.VELOCITY_RUN);
		return false;
	}

    /* *******************************************************************************/
    /* *************************  OnScaleGestureListener   ************************* */
    /* *******************************************************************************/

	@Override
	public boolean onScaleBegin(ScaleGestureDetector detector) {
		// TODO Auto-generated method stub
		return false;
	}
    
	@Override
	public boolean onScale(ScaleGestureDetector detector) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onScaleEnd(ScaleGestureDetector detector) {
		// TODO Auto-generated method stub
	}

}
