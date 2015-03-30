package com.example.myfirstapp;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {
	private int instructionViewDimension = -1;
	private MyView myView;
	
	public void hideView(View view) {
		TextView textView = (TextView) view;
		myView.setAccessible(false);
		if(textView.getHeight()!=0){
			int temp = instructionViewDimension;
			instructionViewDimension = textView.getHeight();
			textView.setHeight(0);
			myView.setAccessible(true);
			if(temp==-1){
				myView.newGame(200);
			}
		}else{
			textView.setHeight(instructionViewDimension); //TODO wrap_content ???
		}
		//TODO ?????????? myView.requestLayout();
	}
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		myView = (MyView) findViewById(R.id.myView);
		myView.setAccessible(false);
//		myView = new MyView(this);
        //setContentView(myView);
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
        	if(findViewById(R.id.instructionTextView).getHeight()!=0){
        		hideView(findViewById(R.id.instructionTextView));
        	}
        	myView.newGame(200);
        	return true;
        default:
        	return super.onOptionsItemSelected(item);
        }
    }
}
