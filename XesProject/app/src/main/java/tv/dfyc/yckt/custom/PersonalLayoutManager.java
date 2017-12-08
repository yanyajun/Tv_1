package tv.dfyc.yckt.custom;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import tv.dfyc.yckt.R;
import tv.dfyc.yckt.adapter.GeneralInfoAdapter;

/**
 * Created by admin on 2017-11-24.
 */

public class PersonalLayoutManager extends LinearLayoutManager {
    private boolean isScrollEnabled = true;

    private GeneralInfoAdapter mLessonAdapter;
    private GridLayoutRecordManangerTv mGridLayoutManager;

    public void setFocusFailedLayoutManager(GridLayoutRecordManangerTv grid_layout_manager) {
        mGridLayoutManager = grid_layout_manager;
    }

    public PersonalLayoutManager(Context context) {
        super(context);
    }

    public void setScrollEnabled(boolean flag) {
        this.isScrollEnabled = flag;
    }

    private OnLeftKeyListener mLeftListener;
    public interface OnLeftKeyListener {
        void onLeftKey(View focused);
    }
    public void setLeftKeyListener(OnLeftKeyListener listener){
        mLeftListener = listener;
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

    public void setFocusFailedAdapter(GeneralInfoAdapter lessonAdapter) {
        mLessonAdapter = lessonAdapter;
    }

    @Override
    public View onFocusSearchFailed(View focused, int focusDirection, RecyclerView.Recycler recycler, RecyclerView.State state) {
        if(focusDirection == View.FOCUS_RIGHT) {
            if(mGridLayoutManager != null) {
                View view = mGridLayoutManager.getChildAt(mGridLayoutManager.findFirstCompletelyVisibleItemPosition());
                if(view != null){
                    return view;
                }

            }
//            if(mLessonAdapter != null)
//                return mLessonAdapter.getSelectedView();
        }
        return super.onFocusSearchFailed(focused, focusDirection, recycler, state);
    }

    @Override
    public View onInterceptFocusSearch(View focused, int direction) {
        if (direction == View.FOCUS_DOWN) {
            View view = getChildAt(getChildCount() - 1);
            if (view != null) {
                View subView = view.findViewById(R.id.info_layout);
                if (subView != null && subView.equals(focused)) {
                    return focused;
                }
            }
        }
        else if (direction == View.FOCUS_UP) {
            View view = getChildAt(0);
            if (view != null) {
                View subView = view.findViewById(R.id.info_layout);
                if (subView != null && subView.equals(focused)) {
                    return focused;
                }
            }
        } else if (direction == View.FOCUS_LEFT) {
            if(mLeftListener != null)
                mLeftListener.onLeftKey(focused);
        } else if (direction == View.FOCUS_RIGHT) {
            if(mRightListener != null)
                mRightListener.onRightKey(focused);
        }
        return super.onInterceptFocusSearch(focused, direction);
    }
}
