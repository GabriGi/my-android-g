package com.example.myfirstapp;

import mvc.Circle;
import android.util.Log;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;

public class MyController implements OnGestureListener, OnDoubleTapListener {

    private static final String DEBUG_TAG = "Gestures";
    private Circle circle;
    private boolean dragging = false;
	private int deltaX = 0;
	private int deltaY = 0;
    private int numberOfTouch = 0;
    private boolean toUpdate = false;	//Se c'è da cambiare il background.
    									//?????:
    									//Potrei pensare di passare anche lui come parametro cosi' come moveableCircle,
    									//gestendolo come observable (quindi crearlo come nuova classe (struttura dati)
    
    public void setCircle(Circle circle) {
		this.circle = circle;
	}
    
    public boolean isDragging() {
		return dragging;
	}
    
    public void setToInvalidate(boolean toInvalidate) {
		this.toUpdate = toInvalidate;
	}
    
    public boolean isToInvalidate() {
		return toUpdate;
	}
    
    public int getNumberOfTouch() {
		return numberOfTouch;
	}
    
	@Override
    public boolean onDown(MotionEvent event) {
//        Log.d(DEBUG_TAG,"onDown");
       //Log.d(DEBUG_TAG,"onDown: " + event.toString());
        
		numberOfTouch++;
    	dragging = false;
		
        if(circle!=null){ //Ovvero if(numberOfTouch == 1){
        	int x = (int)event.getX();
        	int y = (int)event.getY();
        	
        	int xc=circle.getX();
    		int yc=circle.getY();
    		int rc=circle.getRadius();
            if((x-xc)*(x-xc) + (y-yc)*(y-yc) < rc*rc){
            	dragging=true;
            	deltaX = x-xc;
            	deltaY = y-yc;
            }else{
            	toUpdate = true;
            }
    	}else{
        	toUpdate = true;
    	}
        
        Log.d(DEBUG_TAG,"onDown - end: numberOfTouch: " + numberOfTouch + " dragging: " + dragging + " toInvalidate" + toUpdate);
        return true;
    }

    @Override
    public void onShowPress(MotionEvent event) {
        Log.d(DEBUG_TAG, "onShowPress");
        //Log.d(DEBUG_TAG, "onShowPress: " + event.toString());
    }

    @Override
    public boolean onSingleTapUp(MotionEvent event) {
        Log.d(DEBUG_TAG, "onSingleTapUp");
        //Log.d(DEBUG_TAG, "onSingleTapUp: " + event.toString());
        
        //TODO Non viene sempre chiamato....
        /*     Il risultato è un leggero bug grafico:
         * 		Se trascino il cerchio (non tocco lo sfondo)
         * 		e poi mostro/nascondo le istruzioni lo sfondo non cambia;
         * 		se invece tocco lo sfondo (non trascino il cerchio)
         * 		e poi mostro/nascondo le istruzioni lo sfondo cambia;
         */
    	dragging = false;
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        Log.d(DEBUG_TAG, "onScroll");
        //Log.d(DEBUG_TAG, "onScroll: " + e1.toString()+"   -|-   "+e2.toString());
        
        int x = (int)e2.getX();
    	int y = (int)e2.getY();
        if(dragging){
            circle.setX(x-deltaX);
            circle.setY(y-deltaY);
        }
        return true;
    }

    @Override
    public void onLongPress(MotionEvent event) {
        Log.d(DEBUG_TAG, "onLongPress"); 
        //Log.d(DEBUG_TAG, "onLongPress: " + event.toString()); 
    }

    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2, 
            float velocityX, float velocityY) {
        Log.d(DEBUG_TAG, "onFling");
        //Log.d(DEBUG_TAG, "onFling: " + event1.toString()+event2.toString());
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent event) {
        Log.d(DEBUG_TAG, "onDoubleTap");
        //Log.d(DEBUG_TAG, "onDoubleTap: " + event.toString());
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent event) {
        Log.d(DEBUG_TAG, "onDoubleTapEvent");
        //Log.d(DEBUG_TAG, "onDoubleTapEvent: " + event.toString());
        return true;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent event) {
        Log.d(DEBUG_TAG, "onSingleTapConfirmed");
        //Log.d(DEBUG_TAG, "onSingleTapConfirmed: " + event.toString());
        return true;
    }
}
