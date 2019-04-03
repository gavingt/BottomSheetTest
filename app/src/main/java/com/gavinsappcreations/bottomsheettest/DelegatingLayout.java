package com.gavinsappcreations.bottomsheettest;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

public class DelegatingLayout extends LinearLayout {

    private boolean isDelegating;
    private ViewGroup delegateView;
    private int[] originalOffset = new int[2];

    public DelegatingLayout(Context context) {
        super(context);
    }

    public DelegatingLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DelegatingLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setDelegateView(ViewGroup view) {
        delegateView = view;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        // Clear delegating flag on touch start/end
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                isDelegating = false;
                originalOffset[0] = 0;
                originalOffset[1] = 0;
        }

        // If we're delegating, send all events to the delegate
        if (isDelegating) {

            // Offset location in case the delegate view has shifted since we last fed it a motion event.
            ev.offsetLocation(originalOffset[0] - delegateView.getLeft(), originalOffset[1] - delegateView.getTop());

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

            // May be more complicated to handle other edge cases
            // (i.e. non shared parent view descendants, translation, etc.)
            //
            // I would use getLocationInWindow to cover more cases, but
            // the current issue only touches getTop() so this simple efficient solution is fine.
            originalOffset[0] = delegateView.getLeft();
            originalOffset[1] = delegateView.getTop();

            // Send the touch event to the delegate
            return delegateView.onTouchEvent(ev);
        }

        // No delegation, handle like usual.
        return super.dispatchTouchEvent(ev);
    }
}
