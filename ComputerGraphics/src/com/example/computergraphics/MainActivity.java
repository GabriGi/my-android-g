package com.example.computergraphics;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

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
    	Context context = getApplicationContext();
		int duration = Toast.LENGTH_SHORT;
		CharSequence text;
		Toast toast;
		if(myView.isEnableTouching()){
	        switch (item.getItemId()) {
	        case R.id.action_basic_absolute:
	        	myView.setController(new BasicController(BasicController.ABSOLUTE_MODE), false);
	        	text = "Absolute Controller selected.";
	    		toast = Toast.makeText(context, text, duration);
	    		break;
	        case R.id.action_basic_centered:
	        	myView.setController(new BasicController(BasicController.CENTERED_MODE), false);
	        	text = "Centered Controller selected.";
	    		toast = Toast.makeText(context, text, duration);
	    		break;
	        case R.id.action_alternative:
	        	myView.setController(new AlternativeController(), true);
	        	text = "Alternative Controller selected.";
	    		toast = Toast.makeText(context, text, duration);
	    		break;
	        case R.id.action_different:
	        	myView.setController(new DifferentController(), true);
	        	text = "Different Controller selected.";
	    		toast = Toast.makeText(context, text, duration);
	    		break;
	        case R.id.action_scenery00:
	        	myView.setSceneryNumber(0);
	        	text = "Loading Scenery 00. Wait please..";
	    		toast = Toast.makeText(context, text, duration);
	    		break;
	        case R.id.action_scenery01:
	        	myView.setSceneryNumber(1);
	        	text = "Loading Scenery 01. Wait please..";
	    		toast = Toast.makeText(context, text, duration);
	    		break;
	        case R.id.action_jump_enabled:
	        	item.setChecked(!item.isChecked());
	        	if(item.isChecked()) item.setTitle(R.string.action_disable_jump);
	        	else item.setTitle(R.string.action_enable_jump);
	        	myView.setJumpEnabled(item.isChecked());
	        	return true;
	        default:
	        	return super.onOptionsItemSelected(item);
	        }
	        toast.show();
	        return true;
    	}else{
        	text = "Please wait. Loading in progress.";
    		toast = Toast.makeText(context, text, duration);
	        toast.show();
    		return super.onOptionsItemSelected(item);
    	}
    }
}
