package com.example.user.lskiller.View;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import java.util.IllegalFormatException;

/**
 * Created by USER on 2017/01/30.
 */

public class PagerLayout extends ViewPager {


    public PagerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalFormatException e) {
            e.printStackTrace();
            return false;
        }
    }
}
