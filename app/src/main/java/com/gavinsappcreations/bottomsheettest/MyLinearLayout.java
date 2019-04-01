package com.gavinsappcreations.bottomsheettest;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import androidx.annotation.Nullable;

public class MyLinearLayout extends LinearLayout {

    GestureDetector mDetector;
    boolean isScrolling = false;

    public MyLinearLayout(Context context) {
        super(context);
        mDetector = new GestureDetector(context, new MyGestureListener());
    }

    public MyLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mDetector = new GestureDetector(context, new MyGestureListener());
    }

    public MyLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mDetector = new GestureDetector(context, new MyGestureListener());
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.d("LOG", "isScrolling in onInterceptTouchEvent");
        return mDetector.onTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        isScrolling = mDetector.onTouchEvent(ev);
        return isScrolling;
    }



    // In the SimpleOnGestureListener subclass you should override
// onDown and any other gesture that you want to detect.
    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {


        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
            Log.i("TAG", "onScroll: ");

            return true;
        }

    }

}




