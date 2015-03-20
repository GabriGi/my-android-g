package com.example.myfirstapp;

import java.util.Observable;
import java.util.Observer;
import java.util.Random;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class MyView extends View implements Observer{

	static final private int RADIUS_OF_CIRCLE = 50;
	static final private int NUMBER_OF_CIRCLE = 64;
	private Circle[] background = new Circle[NUMBER_OF_CIRCLE];
	
    private Paint p;
    private Random rand = new Random(System.currentTimeMillis());
    
    private Circle moveableCircle;
    private int opacita = 200;	//255:Opaco->Banale, 0:Trasparente->Impossibile
    private int winnerTouch = 0;
    
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
    
    private void initialize(Context context){
        p = new Paint();
        p.setAntiAlias(true);
        myController = new MyController();
        gestureDetector = new GestureDetectorCompat(context, myController);
        gestureDetector.setOnDoubleTapListener(myController);

    }

    public void startPlay() {
    	Log.d(VIEW_LOG_TAG, this.getHeight()+", "+this.getWidth());
    	moveableCircle = new Circle(rand.nextInt(this.getWidth()+1), rand.nextInt(this.getHeight()+1), 
    						(RADIUS_OF_CIRCLE>>1)+rand.nextInt(1+(RADIUS_OF_CIRCLE>>1)), 
    				//		Color.argb(opacita+rand.nextInt(256-opacita), 0, 0, rand.nextInt(256)));
    						Color.YELLOW);
    	myController.setmoveableCircle(moveableCircle);
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
    	Log.d(VIEW_LOG_TAG, "ondraw: " + this.getHeight()+", "+this.getWidth());
        super.onDraw(canvas);
        int viewWidth = this.getWidth();
        int viewHeight = this.getHeight();
        canvas.drawColor(Color.BLACK);	//Background color
        
        if(!myController.isDragging()){	//Disegno 100 cerchi di sfumature di blu in posizioni casuali e di raggio casuale
        	createNewCircleBackground(viewWidth,viewHeight, RADIUS_OF_CIRCLE);	//..che faranno da sfondo.
        }
        for(int i=0;i<NUMBER_OF_CIRCLE;i++){
	        p.setColor(background[i].getColor());
        	canvas.drawCircle(background[i].getX(), background[i].getY(), background[i].getRadius(), p);
        }
        
        //Istruzioni che creano e disegnano il cerchio che può essere mosso dall'utente
        if(myController.getNumberOfTouch()==1){	//TODO 216 : 70 241 (320)      35 167 (240) : 296
    		startPlay();
        }
        if(moveableCircle!=null){
    		p.setColor(moveableCircle.getColor());
            if(!myController.isDragging() && winnerTouch==0){
    	        canvas.drawCircle(moveableCircle.getX(), moveableCircle.getY(), moveableCircle.getRadius(), p);
        	}else{
    	        canvas.drawCircle(moveableCircle.getX(), moveableCircle.getY(), moveableCircle.getRadius(), p);
    	        if(winnerTouch==0){
    	        	//Ricordando che il primo tocco fa partire il gioco, e che quindi non lo conto:
        	        winnerTouch = myController.getNumberOfTouch()-1;
				}
        		p.setColor(Color.RED);
        		//TODO this.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        		//     canvas.drawText("WIN!\n"+winnerTouch, moveableCircle.getX(), moveableCircle.getY(), p);
        		canvas.drawText("WIN! "+winnerTouch, moveableCircle.getX()-20, moveableCircle.getY()+5, p);
        	}
        }
    }

    @Override
    public void update(Observable observable, Object data) {
    	invalidate();	
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	myController.setToInvalidate(false);
        gestureDetector.onTouchEvent(event);
        if(myController.isToInvalidate()){
        	invalidate();
        }
        //return super.onTouchEvent(event);
        return true; //Evento gestito
    }
}