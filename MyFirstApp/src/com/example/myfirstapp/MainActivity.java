package com.example.myfirstapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {
	
	public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
	private int instructionViewDimension = -1;
	
	public void startPlaying(View view) {
		Intent intent = new Intent(this, CircleViewGameActivity.class);
		TextView textView = (TextView) findViewById(R.id.instructionTextView);
	    	String message = textView.getText().toString();
		intent.putExtra(EXTRA_MESSAGE, message);
	    startActivity(intent);
	}
	
	public void hideView(View view) {
		TextView textView = (TextView) view;
		if(textView.getHeight()!=0){
			instructionViewDimension = textView.getHeight();
			textView.setHeight(0);
			//TODO far partire il gioco in automatico (non va)
//			MyView myView = (MyView) findViewById(R.id.myView1);
//			myView.startPlay();
		}else{
			textView.setHeight(instructionViewDimension); //TODO wrap_content ???
		}
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
        	hideView(findViewById(R.id.instructionTextView));
        	return true;
        case R.id.action_settings:
        	//openSettings();
        	return true;
        default:
        	return super.onOptionsItemSelected(item);
        }
    }
}
