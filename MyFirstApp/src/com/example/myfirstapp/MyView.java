package com.example.myfirstapp;

import java.util.Observable;
import java.util.Observer;
import java.util.Random;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class MyView extends View implements Observer{

	static final private int MAX_RADIUS_OF_CIRCLE = 50;
	static final private int NUMBER_OF_CIRCLE = 64;
	private Circle[] background = new Circle[NUMBER_OF_CIRCLE];
	
    private Paint p;
    private Random rand = new Random(System.currentTimeMillis());
    private int viewWidth;
    private int viewHeight;
    
    private Circle moveableCircle;
    private int opacita = 200;	//255:Opaco->Banale, 0:Trasparente->Impossibile
    private int winnerTouch = 0;
    private boolean startNewGame = false;
    
    //These boolean are necessary to solve some bugs on resizing the view and starting new game.
    private boolean accessible = false;
    private boolean randomizeBackground = true;
    
    private GestureDetectorCompat gestureDetector;
    private MyController myController;
    
    public MyView(Context context) {
        super(context);
        initialize(context);
    }
    
    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }
    
    public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
        initialize(context);
	}
    
    public int getNumberOfTouch() {
		return myController.getNumberOfTouch();
	}
    
	public void setAccessible(boolean accessible) {
		this.accessible = accessible;
	}

	private void initialize(Context context){
        p = new Paint();
        p.setAntiAlias(true);
        myController = new MyController(context);
        gestureDetector = new GestureDetectorCompat(context, myController);
        gestureDetector.setOnDoubleTapListener(myController);
    }
	
	public void newGame(int difficolta){
		Log.d(VIEW_LOG_TAG, "newGame");
		opacita = difficolta;
		winnerTouch = 0;
		myController.setNumberOfTouch(0);
		myController.setDragging(false);
		myController.getMyScroller().forceFinished(true);
		moveableCircle = null;
		startNewGame = true;
		randomizeBackground = true;
		invalidate();
	}

    public void startPlay() {
    	Log.d(VIEW_LOG_TAG, "startPlay: "+this.getHeight()+", "+this.getWidth());
        viewWidth = this.getWidth();
        viewHeight = this.getHeight();
        
        int radius = (MAX_RADIUS_OF_CIRCLE>>1)+rand.nextInt(1+(MAX_RADIUS_OF_CIRCLE>>1));
		int x = rand.nextInt(viewWidth+1);
    	int y = rand.nextInt(viewHeight+1);
	//	int x = radius+rand.nextInt(viewWidth-(radius<<1)+1);	//Per collocarlo completamente all'interno dei bordi
    //	int y = radius+rand.nextInt(viewHeight-(radius<<1)+1);	//	  "		  "		  "		  "		  "		  "
        int color = Color.argb(opacita+rand.nextInt(256-opacita), 0, 0, rand.nextInt(256));
	//	int color = Color.YELLOW;	//DEBUG
    	moveableCircle = new Circle(x, y, radius, color);

    	myController.setMoveableCircle(moveableCircle);
    	myController.setDrawableSize(viewWidth, viewHeight);
    	moveableCircle.addObserver(this);
    }
    
	private Circle[] createNewCircleBackground(int maxX, int maxY, int maxRadious) {
    	for(int i=0;i<NUMBER_OF_CIRCLE;i++){
    		background[i] = new Circle((int)(rand.nextFloat()*maxX), (int)(rand.nextFloat()*maxY), 
    								   (int)(rand.nextFloat()*maxRadious), 
    								   Color.argb(rand.nextInt(256), 0, 0, rand.nextInt(256)));
	    }
		return background;
	}
    
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d(VIEW_LOG_TAG, "onDraw: " + this.getHeight()+", "+this.getWidth());
        
        viewWidth = this.getWidth();
        viewHeight = this.getHeight();
        
        canvas.drawColor(Color.BLACK);	//Background color
        
        if(accessible){
	        if(!myController.isDragging() && randomizeBackground){
	        	createNewCircleBackground(viewWidth,viewHeight, MAX_RADIUS_OF_CIRCLE);
	        }
        	randomizeBackground = true;
	        for(int i=0;i<NUMBER_OF_CIRCLE;i++){
		        p.setColor(background[i].getColor());
	        	canvas.drawCircle(background[i].getX(), background[i].getY(), background[i].getRadius(), p);
	        }
	        
	        //Istruzioni che creano e disegnano il cerchio che può essere mosso dall'utente
	        if(startNewGame){
	    		startPlay();
	    		startNewGame = false;
	        }
	        if(moveableCircle!=null){
	        	p.setColor(moveableCircle.getColor());
	    		canvas.drawCircle(moveableCircle.getX(), moveableCircle.getY(), moveableCircle.getRadius(), p);
	            if(myController.isDragging() || winnerTouch!=0){
	            	if(winnerTouch==0){
	        	        winnerTouch = myController.getNumberOfTouch();
					}
	        		p.setColor(Color.RED);
	        		//TODO this.setTextAlignment(TEXT_ALIGNMENT_CENTER);
	        		//     canvas.drawText("WIN!\n"+winnerTouch, moveableCircle.getX(), moveableCircle.getY(), p);
	        		canvas.drawText("WIN! "+winnerTouch, moveableCircle.getX()-20, moveableCircle.getY()+4, p);
	        	}
	        }
        }else{
        	randomizeBackground = false;
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(accessible){
        	super.onTouchEvent(event);
	    	myController.setToInvalidate(false);
	        gestureDetector.onTouchEvent(event);
	        if(myController.isToInvalidate()){
	        	invalidate();
	        }
        }
        return true; //Evento gestito
    }
    
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    	super.onSizeChanged(w, h, oldw, oldh);
    	myController.setDrawableSize(w, h);
    }
    
    @Override
    public void computeScroll() {
    	super.computeScroll();
    	Log.d(VIEW_LOG_TAG, "Scroll");
    	
    	if(myController.flingCircle(viewWidth, viewHeight)){
    		ViewCompat.postInvalidateOnAnimation(this);
    	}
    }

    @Override
    public void update(Observable observable, Object data) {
    	invalidate();	
    }
    
}