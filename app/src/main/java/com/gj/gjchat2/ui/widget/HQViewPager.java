package com.gj.gjchat2.ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 自定义ViewPager 控制不让其滑动
 *
 * @author Administrator
 *
 */
@SuppressLint("NewApi")
public class HQViewPager extends ViewPager {

    private boolean HAS_TOUCH_MODE = false;

    public HQViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public HQViewPager(Context context) {
        super(context);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (HAS_TOUCH_MODE)
            return super.onInterceptHoverEvent(ev);
        else
            return false;

    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (HAS_TOUCH_MODE)
            return super.onTouchEvent(ev);
        else
            return false;
    }

   /* @Override
    public boolean arrowScroll(int direction) {
        if (HAS_TOUCH_MODE) return false;
        if (direction != FOCUS_LEFT && direction != FOCUS_RIGHT) return false;
        return super.arrowScroll(direction);
    }*/

}
