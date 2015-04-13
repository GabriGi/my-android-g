package com.example.computergraphics.controls;

import com.example.computergraphics.controls.actionSet.ActionSet;

import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.ScaleGestureDetector.OnScaleGestureListener;

public interface IController extends OnGestureListener, OnDoubleTapListener, OnScaleGestureListener {

	public void setActionsSet(ActionSet action);
	
	public void setViewSize(int width, int height);
	
	public void stopScrolling();
	
}
