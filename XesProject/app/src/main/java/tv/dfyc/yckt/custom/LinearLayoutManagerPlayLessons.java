package tv.dfyc.yckt.custom;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import tv.dfyc.yckt.R;

/**
 * Created by Administrator on 2017/9/6 0006.
 */

public class LinearLayoutManagerPlayLessons extends LinearLayoutManager {
    private boolean isScrollEnabled = true;
    private boolean mMultiFlag = false;

    public void setMultiFlag(boolean muilt) {
        mMultiFlag = muilt;
    }

    public LinearLayoutManagerPlayLessons(Context context) {
        super(context);
    }

    public void setScrollEnabled(boolean flag) {
        this.isScrollEnabled = flag;
    }

    @Override
    public boolean canScrollVertically() {
        return isScrollEnabled && super.canScrollVertically();
    }

    @Override
    public boolean canScrollHorizontally() {
        return isScrollEnabled && super.canScrollHorizontally();
    }

    @Override
    public View onInterceptFocusSearch(View focused, int direction) {
        /* 去掉循环列表
        if(!mMultiFlag) {
            if (direction == View.FOCUS_DOWN) {
                View view = getChildAt(getChildCount() - 1);
                View view_up = getChildAt(0);
                if (view != null && view.equals(focused) && view_up != null) {
                    return view_up;
                }
            } else if (direction == View.FOCUS_UP) {
                View view = getChildAt(0);
                View view_bottom = getChildAt(getChildCount() - 1);
                if (view != null && view.equals(focused) && view_bottom != null) {
                    return view_bottom;
                }
            }
        }
        */
        return super.onInterceptFocusSearch(focused, direction);
    }
}
