package tv.dfyc.yckt.custom;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.View;

import tv.dfyc.yckt.R;

/**
 * Created by admin on 2017-9-26.
 */

public class GridLayoutManagerFeedback extends  android.support.v7.widget.GridLayoutManager{

    private boolean isScrollEnabled = true;
    public GridLayoutManagerFeedback(Context context, int spanCount) {
        super(context, spanCount);
    }

    public void setScrollEnabled(boolean flag) {
        this.isScrollEnabled = flag;
    }

    @Override
    public boolean canScrollHorizontally() {
        return isScrollEnabled && super.canScrollHorizontally();
    }

    @Override
    public View onInterceptFocusSearch(View focused, int direction) {

        if (direction == View.FOCUS_RIGHT) {
            int pos = getPosition(getFocusedChild());
            View view = getChildAt(pos);
            if (view != null && pos != getItemCount()) {
                View subView = view.findViewById(R.id.checkBox_relativelayout);
                if (subView != null && subView.equals(focused)) {
                    View firstView = getChildAt(pos+1);
                    if (firstView != null) {
                        View firstSubView = firstView.findViewById(R.id.checkBox_relativelayout);
                        if (firstSubView != null) {
                            return firstSubView;
                        }
                    }
                    return focused;
                }
            }
        } else if (direction == View.FOCUS_LEFT) {
            int pos = getPosition(getFocusedChild());
            View view = getChildAt(pos);
            if (view != null && pos != 0) {
                View subView = view.findViewById(R.id.checkBox_relativelayout);
                if (subView != null && subView.equals(focused)) {
                    View firstView = getChildAt(pos-1);
                    if (firstView != null) {
                        View firstSubView = firstView.findViewById(R.id.checkBox_relativelayout);
                        if (firstSubView != null) {
                            return firstSubView;
                        }
                    }
                    return focused;
                }
            }
        }

        return super.onInterceptFocusSearch(focused, direction);
    }
}
