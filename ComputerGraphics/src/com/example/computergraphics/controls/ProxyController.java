package com.example.computergraphics.controls;

import sfogl.integration.Node;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

public class ProxyController implements IController {
	
    private static final String DEBUG_TAG = "Gestures";
    private boolean scaling = false;

	private IController controller;

	public ProxyController(IController control) {
		this.controller = control;
	}

	public void setController(IController control) {
		this.controller = control;
	}
	
	@Override
	public void setActionsSet(ActionSet action) {
		controller.setActionsSet(action);
	}
	
	@Override
	public void setViewSize(int width, int height){
		controller.setViewSize(width, height);
	}
	
    /* ***************************************************************************** */
    /* *************************     OnGestureListener     ************************* */
    /* ***************************************************************************** */
    
	@Override
	public boolean onDown(MotionEvent e) {
        Log.d(DEBUG_TAG,"onDown");
		return controller.onDown(e);
	}

	@Override
	public void onShowPress(MotionEvent e) {
        if(!scaling){
        	Log.d(DEBUG_TAG, "onShowPress");
    		controller.onShowPress(e);
        }else{
        	Log.d(DEBUG_TAG, "onShowPress - scaling");
        }
	}

	@Override
	public void onLongPress(MotionEvent e) {
    	if(!scaling){
    		Log.d(DEBUG_TAG, "onLongPress");
    		controller.onLongPress(e);
        }else{
        	Log.d(DEBUG_TAG, "onLongPress - scaling");
        }
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
        Log.d(DEBUG_TAG, "onSingleTapUp");
		return controller.onSingleTapUp(e);
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		Log.d(DEBUG_TAG, "onScroll");
		return controller.onScroll(e1, e2, distanceX, distanceY);
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Log.d(DEBUG_TAG, "onFling");
		return controller.onFling(e1, e2, velocityX, velocityY);
	}

    /* ***************************************************************************** */
    /* *************************    OnDoubleTapListener    ************************* */
    /* ***************************************************************************** */
    
	@Override
	public boolean onSingleTapConfirmed(MotionEvent e) {
        Log.d(DEBUG_TAG, "onSingleTapConfirmed");
		return controller.onSingleTapConfirmed(e);
	}

	@Override
	public boolean onDoubleTap(MotionEvent e) {
        Log.d(DEBUG_TAG, "onDoubleTap");
		return controller.onDoubleTap(e);
	}

	@Override
	public boolean onDoubleTapEvent(MotionEvent e) {
        Log.d(DEBUG_TAG, "onDoubleTapEvent");
		return controller.onDoubleTapEvent(e);
	}

    /* *******************************************************************************/
    /* *************************  OnScaleGestureListener   ************************* */
    /* *******************************************************************************/

	@Override
	public boolean onScaleBegin(ScaleGestureDetector detector) {
        Log.d(DEBUG_TAG, "onScaleBegin");
        scaling = true;
		return controller.onScaleBegin(detector);
	}
    
	@Override
	public boolean onScale(ScaleGestureDetector detector) {
        Log.d(DEBUG_TAG, "onScale");
		return controller.onScale(detector);
	}

	@Override
	public void onScaleEnd(ScaleGestureDetector detector) {
		Log.d(DEBUG_TAG, "onScaleEnd");
        scaling = false;
		controller.onScaleEnd(detector);
	}

}
