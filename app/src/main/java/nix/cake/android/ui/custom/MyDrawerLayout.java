package nix.cake.android.ui.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class MyDrawerLayout extends DrawerLayout {
    private boolean isSwipeOpenEnabled = false;

    public MyDrawerLayout(@NonNull Context context) {
        super(context);
    }

    public MyDrawerLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyDrawerLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!isSwipeOpenEnabled && !isDrawerVisible(GravityCompat.START) && !isDrawerVisible(GravityCompat.END)) {
            return false;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!isSwipeOpenEnabled && !isDrawerVisible(GravityCompat.START) && !isDrawerVisible(GravityCompat.END)) {
            return false;
        }
        return super.onTouchEvent(ev);
    }

    public void setSwipeOpenEnabled(boolean swipeOpenEnabled) {
        isSwipeOpenEnabled = swipeOpenEnabled;
    }

    public boolean isSwipeOpenEnabled() {
        return isSwipeOpenEnabled;
    }
}
