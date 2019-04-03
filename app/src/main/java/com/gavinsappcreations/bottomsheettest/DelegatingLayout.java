package com.gavinsappcreations.bottomsheettest;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DelegatingLayout extends LinearLayout {

    public DelegatingLayout(Context context) {
        super(context);
    }

    public DelegatingLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DelegatingLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private boolean isDelegating;
    private ViewGroup delegateView;

    public void setDelegateView(ViewGroup view) {
        delegateView = view;
    }

    public boolean isDelegating() {
        return isDelegating;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        // Clear delegating flag on touch start/end
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                isDelegating = false;
        }

        // If we're delegating, send all events to the delegate
        if (isDelegating) {
            return delegateView.dispatchTouchEvent(ev);
        }

        // Check if the delegate wants to steal touches.
        isDelegating = delegateView.onInterceptTouchEvent(ev);

        if (isDelegating) {
            // If delegate has stolen, we should cancel any touch handling in our own view.
            MotionEvent cancel = MotionEvent.obtain(ev);
            cancel.setAction(MotionEvent.ACTION_CANCEL);
            super.dispatchTouchEvent(cancel);
            cancel.recycle();

            Log.d("LOG", "getY: " + ev.getY());

            // Send the touch event to the delegate
            return delegateView.onTouchEvent(ev);
        }

        // No delegation, handle like usual.
        return super.dispatchTouchEvent(ev);
    }
}




