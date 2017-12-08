package tv.dfyc.yckt.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import tv.dfyc.yckt.R;
import tv.dfyc.yckt.info.search.HotSearchGirdLayoutManager;

/**
 * Created by admin on 2017-11-27.
 */

public class SelectClassGirdLayoutmanager extends GridLayoutManager {

    private GeneralSelectClassAdapter mLessonAdapter;

    public void setFocusFailedAdapter(GeneralSelectClassAdapter lessonAdapter) {
        mLessonAdapter = lessonAdapter;
    }

    public SelectClassGirdLayoutmanager(Context context, int spanCount) {
        super(context, spanCount);
    }

    private SelectClassGirdLayoutmanager.OnDownKeyListener mDownListener;
    public interface  OnDownKeyListener{
        void onDownKey(View focused);
    }

    private SelectClassGirdLayoutmanager.OnUpKeyListener mUpListener;
    public interface OnUpKeyListener {
        void onUpKey();
    }

    private SelectClassGirdLayoutmanager.OnLeftKeyListener mLeftListener;
    public interface OnLeftKeyListener {
        void onLeftKey();
    }

    private SelectClassGirdLayoutmanager.OnRightKeyListener mRightListener;
    public interface OnRightKeyListener {
        void onRightKey();
    }


    public void setDownKeyListener(SelectClassGirdLayoutmanager.OnDownKeyListener listener){
        mDownListener = listener;
    }

    public void setUpKeyListener(SelectClassGirdLayoutmanager.OnUpKeyListener listener){
        mUpListener = listener;
    }

    public void setLeftKeyListener(SelectClassGirdLayoutmanager.OnLeftKeyListener listener){
        mLeftListener = listener;
    }

    public void setRightKeyListener(SelectClassGirdLayoutmanager.OnRightKeyListener listener){
        mRightListener = listener;
    }

    public SelectClassGirdLayoutmanager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }

    public SelectClassGirdLayoutmanager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public View onFocusSearchFailed(View focused, int focusDirection, RecyclerView.Recycler recycler, RecyclerView.State state) {
        View nextFocus = super.onFocusSearchFailed(focused, focusDirection, recycler, state);
        if(focusDirection == View.FOCUS_DOWN)
            return focused;
        if(focusDirection == View.FOCUS_UP) {
            if(mLessonAdapter != null)
                return mLessonAdapter.getSelectedView();
        }
        if (focusDirection == View.FOCUS_RIGHT){
            return  recycler.getViewForPosition(0);
        }
        return null;
    }

    @Override
    public View onInterceptFocusSearch(View focused, int direction) {
        if (direction == View.FOCUS_UP) {
            int iPos = getPosition(focused);
            if(mUpListener != null)
                mUpListener.onUpKey();
        } else if(direction == View.FOCUS_LEFT) {
            int pos = getPosition(getFocusedChild());
            View view = getChildAt(pos);
            if (view != null && pos != 0) {
                View subView = view.findViewById(R.id.hot_search_item);
                if (subView != null && subView.equals(focused)) {
                    View firstView = getChildAt(pos-1);
                    if (firstView != null) {
                        View firstSubView = firstView.findViewById(R.id.hot_search_item);
                        if (firstSubView != null) {
                            return firstSubView;
                        }
                    }
                    return focused;
                }
            }
        } else if (direction == View.FOCUS_DOWN){
            int iPos = getPosition(focused);
                if(mDownListener != null)
                    mDownListener.onDownKey(focused);
        }else if (direction == View.FOCUS_RIGHT) {
            int pos = getPosition(getFocusedChild());
            View view = getChildAt(pos);
            if (view != null && pos != getItemCount()) {
                View subView = view.findViewById(R.id.hot_search_item);
                if (subView != null && subView.equals(focused)) {
                    View firstView = getChildAt(pos+1);
                    if (firstView != null) {
                        View firstSubView = firstView.findViewById(R.id.hot_search_item);
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
