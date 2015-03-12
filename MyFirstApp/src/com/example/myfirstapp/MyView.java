package com.example.myfirstapp;

import java.util.Random;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class MyView extends View{
    private float x,y;
    private float raggio;
    private Paint p;
    private Random rand = new Random();
    
    public MyView(Context context, AttributeSet attrs) {
        super(context);
        p = new Paint();
        p.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.BLACK);	//Colore di sfondo
        for(int i=0;i<100;i++){			//Disegno 100 cerchi di sfumature di blu in posizioni casuali e di raggio casuale
            x=rand.nextFloat()*canvas.getWidth();
            y=rand.nextFloat()*canvas.getHeight();
            raggio=rand.nextFloat()*50;
            p.setColor(Color.argb(rand.nextInt(256), 0, 0, rand.nextInt(256)));
            canvas.drawCircle(x, y, raggio, p);
        }
    }
    
    public boolean onTouchEvent(MotionEvent event){
        switch( event.getAction() ){
        case MotionEvent.ACTION_DOWN:
//            float x = event.getX(); //Coordinata x del tocco
//            float y = event.getY(); //Coordinata y del tocco
            invalidate();
        }
        return true; //Evento gestito
    }
}

