package com.snehadatta.zmap;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class CustomGestureListener implements View.OnTouchListener {
    private static final String TAG = "GestureDetector";
    private static final int LONG_PRESS_TIME = 1000; // 1 second long press

    private final GestureDetector gestureDetector;
    private final Handler handler = new Handler();
    private int pointerCount = 0;
    private boolean isLongPressTriggered = false;

    public CustomGestureListener(Context context) {
        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                handleSingleTap(pointerCount,context);
                return true;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                if (pointerCount == 3) {
                    if (distanceX > 50) {
                        Log.d(TAG, "3-finger swipe left");
                    } else if (distanceX < -50) {
                        Log.d(TAG, "3-finger swipe right");
                    }
                }
                return super.onScroll(e1, e2, distanceX, distanceY);
            }
        });
    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        pointerCount = event.getPointerCount();
        gestureDetector.onTouchEvent(event);

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                isLongPressTriggered = false;
                handler.postDelayed(() -> {
                    if (!isLongPressTriggered) {
                        handleLongPress(pointerCount);
                        isLongPressTriggered = true;
                    }
                }, LONG_PRESS_TIME);
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                handler.removeCallbacksAndMessages(null);
                break;
        }
        return true;
    }

    private void handleSingleTap(int pointerCount, Context context) {
        if (pointerCount == 2) {
            Toast.makeText(context, "2-finger single tap", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "2-finger single tap");
        } else if (pointerCount == 3) {
            Log.d(TAG, "3-finger single tap");
        }
    }

    private void handleLongPress(int pointerCount) {
        if (pointerCount == 2) {
            Log.d(TAG, "2-finger long press");
        } else if (pointerCount == 3) {
            Log.d(TAG, "3-finger long press");
        }
    }
}
