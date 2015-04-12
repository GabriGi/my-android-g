package com.example.computergraphics;


import java.util.Random;

import com.example.computergraphics.controls.AlternativeController;
import com.example.computergraphics.controls.BasicController;
import com.example.computergraphics.controls.DifferentController;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {

	private GraphicsView myView;
	private Random rand = new Random();
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myView = new GraphicsView(this);
        setContentView(myView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	if(myView.isEnableTouching()){
	        switch (item.getItemId()) {
	        case R.id.action_basic_absolute:
	        	myView.setController(new BasicController(BasicController.ABSOLUTE_MODE), false);
	        	return true;
	        case R.id.action_basic_centered:
	        	myView.setController(new BasicController(BasicController.CENTERED_MODE), false);
	        	return true;
	        case R.id.action_alternative:
	        	myView.setController(new AlternativeController(), true);
	        	return true;
	        case R.id.action_different:
	        	myView.setController(new DifferentController(), true);
	        	return true;
	        case R.id.action_next_scenary:
	        	myView.setSceneryNumber(rand.nextInt(myView.getNumberOfScenery()));;
//	        	myView.setSceneryNumber(1);;
	        	return true;
	        default:
	        	return super.onOptionsItemSelected(item);
	        }
    	}else{
    		//Attendere prego.. Caricamento dello scenario in corso.
    		return super.onOptionsItemSelected(item);
    	}
    }
}
