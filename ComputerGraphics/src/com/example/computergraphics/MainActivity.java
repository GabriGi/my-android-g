package com.example.computergraphics;


import com.example.computergraphics.controls.AlternativeController;
import com.example.computergraphics.controls.BasicController;
import com.example.computergraphics.controls.DifferentController;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

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
        switch (item.getItemId()) {
        case R.id.action_basic_absolute:
        	myView.setController(new BasicController(BasicController.ABSOLUTE_MODE), false);
        	return true;
        case R.id.action_basic_relative:
        	myView.setController(new BasicController(BasicController.RELATIVE_MODE), false);
        	return true;
        case R.id.action_alternative:
        	myView.setController(new AlternativeController(), true);
        	return true;
        case R.id.action_different:
        	myView.setController(new DifferentController(), true);
        	return true;
        default:
        	return super.onOptionsItemSelected(item);
        }
    }
}
