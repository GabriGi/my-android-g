package com.example.myfirstapp;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {
	private int instructionViewDimension = 1;
	

	public void hideView(View view) {
		TextView textView = (TextView) view;
		if(instructionViewDimension!=0){
			instructionViewDimension = textView.getHeight();
		}
		textView.setHeight(0);
	}
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //setContentView(new MyView(this));        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	// Inflate the menu; this adds items to the action bar.
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
        case R.id.action_search:
        	//openSearch();
    		TextView textView = (TextView) findViewById(R.id.instructionTextView);
    		textView.setHeight(instructionViewDimension); //TODO wrap_content ???
        	return true;
        case R.id.action_settings:
        	//openSettings();
        	return true;
        default:
        	return super.onOptionsItemSelected(item);
        }
    }
}
