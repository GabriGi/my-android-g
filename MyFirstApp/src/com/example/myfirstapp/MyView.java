package com.example.myfirstapp;

import java.util.Observable;
import java.util.Observer;
import java.util.Random;

import mvc.Circle;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class MyView extends View implements Observer{

	static final private int RADIUS_OF_CIRCLE = 50;
	static final private int NUMBER_OF_CIRCLE = 64;
	private int[][] background = new int[4][NUMBER_OF_CIRCLE];
    private Paint p;
    private Random rand = new Random();
    private Circle circle;
    private int circleColor;
    private int opacita = 128;	//255:Opaco->Banale, 0:Trasparente->Impossibile
    private Boolean dragging = false;
	private int deltaX = 0;
	private int deltaY = 0;
    private Boolean firstTouch = false;
    
    public MyView(Context context, AttributeSet attrs) {
        super(context);
        p = new Paint();
        p.setAntiAlias(true);
    }

    public void startPlay() {
    	circle = new Circle(rand.nextInt(this.getWidth()), rand.nextInt(this.getHeight()), (RADIUS_OF_CIRCLE>>1)+rand.nextInt(1+(RADIUS_OF_CIRCLE>>1)));
    	circleColor = Color.argb(opacita+rand.nextInt(256-opacita), 0, 0, rand.nextInt(256));
    	circle.addObserver(this);
    }
    
	//TODO sostituire con una struttura dati CircleBackground{circle[NUMBER_OF_CIRCLE], integer[NUMBER_OF_CIRCLE]}
	//     DOMANDA: così facendo però, l'accesso non sarebbe più lento?
    private int[][] createNewCircleBackground(int maxX, int maxY, int maxRadious) {
    	for(int i=0;i<NUMBER_OF_CIRCLE;i++){
    		background[1][i] = (int)(rand.nextFloat()*maxX);
    		background[2][i] = (int)(rand.nextFloat()*maxY);
    		background[3][i] = (int)(rand.nextFloat()*maxRadious);
    		background[0][i] = Color.argb(rand.nextInt(256), 0, 0, rand.nextInt(256));
	    }
		return background;
	}
    
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int viewWidth = this.getWidth();
        int viewHeight = this.getHeight();
        canvas.drawColor(Color.BLACK);	//Background color
        
        if(!dragging){	//Disegno 100 cerchi di sfumature di blu in posizioni casuali e di raggio casuale
        	createNewCircleBackground(viewWidth,viewHeight, RADIUS_OF_CIRCLE);	//..che faranno da sfondo.
        }
        for(int i=0;i<NUMBER_OF_CIRCLE;i++){
	        p.setColor(background[0][i]);
        	canvas.drawCircle(background[1][i], background[2][i], background[3][i], p);
        }
        
        //Istruzioni che creano e disegnano il cerchio che può essere mosso dall'utente
        if(firstTouch){	//TODO 216 : 70 241 (320)      35 167 (240) : 296
    		startPlay();
    		firstTouch = false;
        }
        if(circle!=null){
	    	p.setColor(circleColor);
	        canvas.drawCircle(circle.getX(), circle.getY(), circle.getRadius(), p);
        }
    }

    @Override
    public void update(Observable observable, Object data) {
    	invalidate();	
    }
    
    //TODO estrarre e creare la classe gestureManager (->CircleController)
    @Override
    public boolean onTouchEvent(MotionEvent event) {
	    	if(circle!=null){
		    	int x = (int)event.getX();
		    	int y = (int)event.getY();
		        
		        switch(event.getAction()){
		        case MotionEvent.ACTION_DOWN:
		        	int xc=circle.getX();
		    		int yc=circle.getY();
		    		int rc=circle.getRadius();
		        	dragging = false;
		            if((x-xc)*(x-xc) + (y-yc)*(y-yc) < rc*rc){
		            	dragging=true;
		            	deltaX = x-xc;
		            	deltaY = y-yc;
		            }else{
		            	invalidate();
		            }
		        case MotionEvent.ACTION_MOVE:
		            if(dragging){
			            circle.setX(x-deltaX);
			            circle.setY(y-deltaY);
		            }
		            break;
		        case MotionEvent.ACTION_UP:
		        	dragging = false;
		        }
	    	}else{
		    	firstTouch = true;
		    	invalidate();
	    	}
    	//return super.onTouchEvent(event);
    	return true; //Evento gestito
    }
}