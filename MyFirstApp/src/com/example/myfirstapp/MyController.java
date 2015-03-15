package com.example.myfirstapp;

import mvc.Circle;
import android.util.Log;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;

public class MyController implements OnGestureListener, OnDoubleTapListener {

    private static final String DEBUG_TAG = "Gestures";
    private Circle moveableCircle;
    private boolean dragging = false;
	private int deltaX = 0;
	private int deltaY = 0;
    private int numberOfTouch = 0;
    private boolean toUpdate = false;	//Se c'è da cambiare il background.
    									//?????:
    									//Potrei pensare di passare anche lui come parametro cosi' come moveableCircle,
    									//gestendolo come observable (quindi crearlo come nuova classe (struttura dati)
    
    public void setmoveableCircle(Circle circle) {
		this.moveableCircle = circle;
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
		
        if(moveableCircle!=null){ //Ovvero if(numberOfTouch == 1){
        	int x = (int)event.getX();
        	int y = (int)event.getY();
        	
        	int xc=moveableCircle.getX();
    		int yc=moveableCircle.getY();
    		int rc=moveableCircle.getRadius();
            if((x-xc)*(x-xc) + (y-yc)*(y-yc) < rc*rc){
            	dragging=true;
            	moveableCircle.setX(xc); //this is to force the view update instead of use: toUpdate = true;
            	deltaX = x-xc;
            	deltaY = y-yc;
            }else{
            	toUpdate = true;
            }
    	}else{
        	toUpdate = true;
    	}
        
        Log.d(DEBUG_TAG,"onDown - end: numberOfTouch: " + numberOfTouch + " - dragging: " + dragging + " - toUpdate: " + toUpdate);
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
        
        //TODO Viene chiamato solo col tocco veloce..
        /*     Il risultato è un leggero bug grafico:
         * 		se  tocco o trascino lo sfondo oppure tocco il cerchio velocemente
         * 		e poi mostro/nascondo le istruzioni lo sfondo cambia;
         * 		Se trascino il cerchio o lo tocco e tengo premuto
         * 		e poi mostro/nascondo le istruzioni lo sfondo non cambia;
         */
    	dragging = false;
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        //Log.d(DEBUG_TAG, "onScroll");
        Log.d(DEBUG_TAG, "onScroll: " + e1.toString()+"   -|-   "+e2.toString()+"   -|-   "+" - "+distanceX+" - "+distanceY);

        if(dragging){
            moveableCircle.setX((int)e2.getX()-deltaX);
            moveableCircle.setY((int)e2.getY()-deltaY);
            //Oppure potrei usare:
            //  moveableCircle.setX(moveableCircle.getX()-(int)distanceX);
            //  moveableCircle.setY(moveableCircle.getY()-(int)distanceY);
            //Così posso evitare di istanziare deltaX e deltaY.
            //Paradossalmente questa soluzione sarebbe però piu' lenta
            //(Presumo per l'accesso a moveableCircle.getX()
        }
        return true;
    }

    @Override
    public void onLongPress(MotionEvent event) {
        Log.d(DEBUG_TAG, "onLongPress"); 
        //Log.d(DEBUG_TAG, "onLongPress: " + event.toString()); 
    }

    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {
        Log.d(DEBUG_TAG, "onFling");
        //Log.d(DEBUG_TAG, "onFling: " + event1.toString()+event2.toString() + " " + velocityX + " " + velocityY);
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
