package com.wc.dragphoto;
import android.annotation.TargetApi;
import android.view.MotionEvent;

@TargetApi(14)
class MotionEventCompatICS {
    public static int getButtonState(MotionEvent event) {
        return event.getButtonState();
    }
}