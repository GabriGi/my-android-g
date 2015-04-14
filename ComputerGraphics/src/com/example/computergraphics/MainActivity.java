package com.example.computergraphics;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.computergraphics.controls.AlternativeController;
import com.example.computergraphics.controls.BasicController;
import com.example.computergraphics.controls.DifferentController;

public class MainActivity extends Activity {

	private GraphicsView myView;
	
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
	        case R.id.action_scenery00:
	        	myView.setSceneryNumber(0);;
	        	return true;
	        case R.id.action_scenery01:
	        	myView.setSceneryNumber(0);;
	        	return true;
	        case R.id.action_jump_enabled:
	        	item.setChecked(!item.isChecked());
	        	if(item.isChecked()) item.setTitle(R.string.action_disable_jump);
	        	else item.setTitle(R.string.action_enable_jump);
	        	myView.setJumpEnabled(item.isChecked());;
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
