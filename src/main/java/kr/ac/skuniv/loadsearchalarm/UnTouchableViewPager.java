package kr.ac.skuniv.loadsearchalarm;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by qwdbs on 2017-07-20.
 */

public class UnTouchableViewPager extends ViewPager {
    public UnTouchableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }
}
