package com.example.computergraphics.controls;

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
		// TODO Auto-generated method stub
		return true;
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
		return true;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
//		if(mode==ABSOLUTE_MODE){
//			actionSet.moveAvatarWith((e2.getX()-(viewWidth>>1))/(viewWidth>>1), (e2.getY()-(viewHeight>>1))/(viewHeight>>1));
//		}else if(mode==RELATIVE_MODE){
//			actionSet.moveAvatarWith(e2.getX()-e1.getX(), e2.getY()-e1.getY());
//		}
		return true;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		return true;
	}

    /* ***************************************************************************** */
    /* *************************    OnDoubleTapListener    ************************* */
    /* ***************************************************************************** */
    
	@Override
	public boolean onSingleTapConfirmed(MotionEvent e) {
		actionSet.moveAvatarTo((e.getX()-(viewWidth>>1))/(viewWidth>>1), (e.getY()-(viewHeight>>1))/(viewHeight>>1));
		return true;
	}

	@Override
	public boolean onDoubleTap(MotionEvent e) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean onDoubleTapEvent(MotionEvent e) {
		// TODO Auto-generated method stub
		return true;
	}

    /* *******************************************************************************/
    /* *************************  OnScaleGestureListener   ************************* */
    /* *******************************************************************************/

	@Override
	public boolean onScaleBegin(ScaleGestureDetector detector) {
		// TODO Auto-generated method stub
		return true;
	}
    
	@Override
	public boolean onScale(ScaleGestureDetector detector) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void onScaleEnd(ScaleGestureDetector detector) {
		// TODO Auto-generated method stub
	}

}
