package com.snehadatta.zmap;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.webkit.WebView;

import androidx.annotation.NonNull;

public class GestureWebView extends WebView {
    private static final String TAG = "GestureWebView";
    private static final int LONG_PRESS_TIME = 1000; // 1-second threshold

    private final GestureDetector gestureDetector;
    private final Handler handler = new Handler();

    private int pointerCount = 0;
    private boolean isLongPressTriggered = false;

    private JavaScriptHandler javaScriptHandler;

    public GestureWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        gestureDetector = new GestureDetector(context, new GestureListener());
    }

    public void setJavaScriptHandler(JavaScriptHandler javaScriptHandler) {
        this.javaScriptHandler = javaScriptHandler;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        pointerCount = event.getPointerCount();

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                isLongPressTriggered = false;
                startLongPressTimer();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                handlePointerRelease();
                break;
        }

        gestureDetector.onTouchEvent(event);

        // Disable map movement when using more than 1 finger
        if (pointerCount > 1) {
            return true;
        } else {
            return super.onTouchEvent(event);
        }
    }

    private void startLongPressTimer() {
        handler.postDelayed(() -> {
            if (!isLongPressTriggered) {
                isLongPressTriggered = true;
                handleLongPress(pointerCount);
            }
        }, LONG_PRESS_TIME);
    }

    private void handlePointerRelease() {
        isLongPressTriggered = false;
        handler.removeCallbacksAndMessages(null);
    }

    private void handleSingleTap(int count) {
        Log.d(TAG, count + "-finger single tap");
    }

    private void handleLongPress(int count) {
        Log.d(TAG, count + "-finger long press");
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapUp(@NonNull MotionEvent e) {
            if (!isLongPressTriggered) {
                handleSingleTap(e.getPointerCount());
            }
            return super.onSingleTapUp(e);
        }

        /*@Override
        public void onLongPress(@NonNull MotionEvent e) {
            Log.d(TAG, "Long press detected with " + e.getPointerCount() + " fingers");
            super.onLongPress(e);
        }*/

        @Override
        public boolean onDoubleTap(@NonNull MotionEvent e) {
            if (!isLongPressTriggered) {
                Log.d(TAG, "Double tap detected with " + pointerCount + " fingers");
            }
            return super.onDoubleTap(e);
        }

        @Override
        public boolean onScroll(MotionEvent e1, @NonNull MotionEvent e2, float distanceX, float distanceY) {
            if (e1 == null) {
                return false;
            }
            float deltaX = e2.getX() - e1.getX();
            float deltaY = e2.getY() - e1.getY();
            int fingers = e2.getPointerCount();

            if (Math.abs(deltaX) > Math.abs(deltaY)) {
                if (Math.abs(deltaX) > 100) {
                    Log.d(TAG, (deltaX > 0) ? "Swipe Right with " + fingers + " fingers" : "Swipe Left with " + fingers + " fingers");
                }
            } else {
                if (Math.abs(deltaY) > 100) {
                    Log.d(TAG, (deltaY > 0) ? "Swipe Down with " + fingers + " fingers" : "Swipe Up with " + fingers + " fingers");
                }
            }
            return true;
        }
    }
}