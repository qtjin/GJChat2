package com.gj.gjchat2.ui.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class MyViewPager extends ViewPager {

	private float xDistance, yDistance, xLast, yLast;
	
	public MyViewPager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public MyViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
	    switch (ev.getAction()) {
	    case MotionEvent.ACTION_DOWN:
		    xDistance = yDistance = 0f;
		    xLast = ev.getX();
		    yLast = ev.getY();
	    break;
	    case MotionEvent.ACTION_MOVE:
		    final float curX = ev.getX();
		    final float curY = ev.getY();
		
		    xDistance += Math.abs(curX - xLast);
		    yDistance += Math.abs(curY - yLast);
		    
		    if (xDistance > yDistance) {
			    return true;
			}
//		    if (xDistance > 2&& xDistance > yDistance) {
//		    	return false;
//		    }
	//	    xLast = curX;
	//	    yLast = curY;
	    break;
	    case MotionEvent.ACTION_UP:
	    	final float curuX = ev.getX();
	    	final float curuY = ev.getY();
	    	
		    xDistance += Math.abs(curuX - xLast);
		    yDistance += Math.abs(curuY - yLast);
	    	
	    	if (xDistance > yDistance) {
	    		return true;
	    	}
	    break;
	    }
	    return super.onInterceptTouchEvent(ev);
	}
	
	
}
