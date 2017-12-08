package tv.dfyc.yckt.custom;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import tv.dfyc.yckt.R;
import tv.dfyc.yckt.adapter.LessonListAdapter;

/**
 * Created by Administrator on 2017/9/6 0006.
 */

public class LinearLayoutManagerClass extends LinearLayoutManager {
    private boolean isScrollEnabled = true;
    private LessonListAdapter mLessonAdapter;
    private GridLayoutManagerListPage mGridLayoutManager;

    public LinearLayoutManagerClass(Context context) {
        super(context);
    }

    public void setFocusFailedAdapter(LessonListAdapter lessonAdapter) {
        mLessonAdapter = lessonAdapter;
    }

    public void setFocusFailedLayoutManager(GridLayoutManagerListPage grid_layout_manager) {
        mGridLayoutManager = grid_layout_manager;
    }

    public void setScrollEnabled(boolean flag) {
        this.isScrollEnabled = flag;
    }

    private OnRightKeyListener mRightListener;
    public interface OnRightKeyListener {
        void onRightKey(View focused);
    }

    public void setRightKeyListener(OnRightKeyListener listener){
        mRightListener = listener;
    }

    @Override
    public boolean canScrollVertically() {
        //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
        return isScrollEnabled && super.canScrollVertically();
    }

    @Override
    public boolean canScrollHorizontally() {
        return isScrollEnabled && super.canScrollHorizontally();
    }

    @Override
    public View onFocusSearchFailed(View focused, int focusDirection, RecyclerView.Recycler recycler, RecyclerView.State state) {
        if(focusDirection == View.FOCUS_RIGHT) {
            if(mLessonAdapter != null)
                return mLessonAdapter.getSelectedView();
            if(mGridLayoutManager != null) {
                View view = mGridLayoutManager.getChildAt(mGridLayoutManager.findFirstCompletelyVisibleItemPosition());
                if(view != null)
                    return view;
            }
        }
        return super.onFocusSearchFailed(focused, focusDirection, recycler, state);
    }

    @Override
    public View onInterceptFocusSearch(View focused, int direction) {
        if (direction == View.FOCUS_DOWN) {
            View view = getChildAt(getChildCount() - 1);
            if (view != null) {
                View subView = view.findViewById(R.id.primary_class_button);
                if (subView != null && subView.equals(focused)) {
                    return focused;
                }
            }
        }
        else if (direction == View.FOCUS_UP) {
            View view = getChildAt(0);
            if (view != null) {
                View subView = view.findViewById(R.id.primary_class_button);
                if (subView != null && subView.equals(focused)) {
                    return focused;
                }
            }
        } else if (direction == View.FOCUS_RIGHT) {
            if(mRightListener != null)
                mRightListener.onRightKey(focused);
        }
        return super.onInterceptFocusSearch(focused, direction);
    }
}
