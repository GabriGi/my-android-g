package com.example.myfirstapp;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.widget.Scroller;

public class MyController implements OnGestureListener, OnDoubleTapListener {

    private static final String DEBUG_TAG = "Gestures";
    private Circle moveableCircle;
    private int rc=0;
    private boolean dragging = false;
	private int deltaX = 0;
	private int deltaY = 0;
    private int numberOfTouch = 0;
    private boolean toInvalidate = false;	//Se c'è da cambiare il background.
    private Scroller myScroller;
    private int drawableWidth;
    private int drawableHeight;
    
    public MyController(Context context) {
		myScroller = new Scroller(context);
	}
    
    public Scroller getMyScroller() {
		return myScroller;
	}
    
    public void setMoveableCircle(Circle circle) {
		this.moveableCircle = circle;
		rc = moveableCircle.getRadius();
	}
    
    public void setDrawableSize(int viewWidth, int viewHeight){
		drawableWidth = viewWidth-(rc<<1);
		drawableHeight = viewHeight-(rc<<1);
    }
    
    public boolean isDragging() {
		return dragging;
	}
    
    public void setDragging(boolean dragging) {
		this.dragging = dragging;
	}

	public void setToInvalidate(boolean toInvalidate) {
		this.toInvalidate = toInvalidate;
	}
    
    public boolean isToInvalidate() {
		return toInvalidate;
	}

    public int getNumberOfTouch() {
		return numberOfTouch;
	}
    
    public void setNumberOfTouch(int numberOfTouch) {
		this.numberOfTouch = numberOfTouch;
	}
    

	public boolean flingCircle(int viewWidth, int viewHeight) {
		boolean needsInvalidate = false;
		if(myScroller.computeScrollOffset()){
			int currX = myScroller.getCurrX()-deltaX-rc;
			int currY = myScroller.getCurrY()-deltaY-rc;
			
			int nx = (int)(currX / drawableWidth);
			int x = currX % drawableWidth;
			int ny = (int)(currY / drawableHeight);
			int y = currY % drawableHeight;
			
			if((nx%2)!=0){
				x = drawableWidth - Math.abs(x);
			}
			if((ny%2)!=0){
				y = drawableHeight - Math.abs(y);
			}
			moveableCircle.setX(Math.abs(x)+rc);
			moveableCircle.setY(Math.abs(y)+rc);
			needsInvalidate = true;
		}
		return needsInvalidate;
	}
    
	@Override
    public boolean onDown(MotionEvent event) {
        Log.d(DEBUG_TAG,"onDown");
       //Log.d(DEBUG_TAG,"onDown: " + event.toString());
        
        myScroller.forceFinished(true);
		numberOfTouch++;
    	dragging = false;
		
        if(moveableCircle!=null){ //Ovvero if(numberOfTouch == 1){
        	int x = (int)event.getX();
        	int y = (int)event.getY();
        	
        	int xc=moveableCircle.getX();
    		int yc=moveableCircle.getY();
    		rc=moveableCircle.getRadius();
            if((x-xc)*(x-xc) + (y-yc)*(y-yc) < rc*rc){
            	dragging=true;
            	moveableCircle.setX(xc); //this is to force the view update instead of use: toUpdate = true;
            	deltaX = x-xc;
            	deltaY = y-yc;
            }else{
            	toInvalidate = true;
            }
    	}else{
        	toInvalidate = true;
    	}
        
        Log.d(DEBUG_TAG,"onDown - end: numberOfTouch: " + numberOfTouch + " - dragging: " + dragging + " - toUpdate: " + toInvalidate);
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
        Log.d(DEBUG_TAG, "onScroll");
        //Log.d(DEBUG_TAG, "onScroll: " + e1.toString()+"   -|-   "+e2.toString()+"   -|-   "+" - "+distanceX+" - "+distanceY);

        if(dragging){
        	int x = (int)e2.getX()-deltaX;
        	int y = (int)e2.getY()-deltaY;
        	if(x<rc){
        		x = rc;
        	}else if(x>drawableWidth+rc){
        		x = drawableWidth+rc;
        	}
        	if(y<rc){
        		y = rc;
        	}else if(y>drawableHeight+rc){
        		y = drawableHeight+rc;
        	}
            moveableCircle.setX(x);
            moveableCircle.setY(y);
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
        int tempo = 2000;
        myScroller.startScroll((int)event2.getX(), (int)event2.getY(), (int)velocityX*tempo/4000, (int)velocityY*tempo/4000, tempo);
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
