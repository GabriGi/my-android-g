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
	
    private Paint p;
    private Random rand = new Random();
    private Circle circle;
    
    public MyView(Context context, AttributeSet attrs) {
        super(context);
        p = new Paint();
        p.setAntiAlias(true);
        //TODO Vorrei che il cerchio inizialmente fosse posizionato al centro
        Log.v("MyView", "Width:"+this.getWidth()+"; Height"+this.getHeight());
    	circle = new Circle(this.getWidth()>>1, this.getHeight()<<1, 25+rand.nextInt(26));
    	circle.addObserver(this);
    }

    @Override
    public void update(Observable observable, Object data) {
    	invalidate();	
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.BLACK);	//Colore di sfondo
        /*
         * Disegno 100 cerchi di sfumature di blu
         * in posizioni casuali e di raggio casuale
         * che faranno da sfondo.
         * 
         * TODO tenere fermo lo sfondo e spostare solo il cerchio
         */

        for(int i=0;i<100;i++){			
        	float x=rand.nextFloat()*canvas.getWidth();
            float y=rand.nextFloat()*canvas.getHeight();
            float raggio=rand.nextFloat()*50;
            p.setColor(Color.argb(rand.nextInt(256), 0, 0, rand.nextInt(256)));
            canvas.drawCircle(x, y, raggio, p);
        }
        
        //Istruzione che sposta il cerchio dell'utente
        if(circle != null){
        	p.setColor(Color.BLUE);
            canvas.drawCircle(circle.getX(), circle.getY(), circle.getRadius(), p);
        }
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event){
        switch( event.getAction() ){
        case MotionEvent.ACTION_DOWN:
        case MotionEvent.ACTION_MOVE:
            int x = (int) event.getX(); //Coordinata x del tocco
            int y = (int) event.getY(); //Coordinata y del tocco
            circle.setX(x);
            circle.setY(y);
        }
        return true; //Evento gestito
    }
}

