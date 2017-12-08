package com.open.androidtvwidget.leanback.recycle;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by hailongqiu on 2016/8/25.
 */
public class GridLayoutManagerTV extends GridLayoutManager {
    public GridLayoutManagerTV(Context context, int spanCount) {
        super(context, spanCount);
    }

    private OnDownKeyListener mDownListener;
    public interface  OnDownKeyListener{
        void onDownKey(int position);
    }

    private OnUpKeyListener mUpListener;
    public interface OnUpKeyListener {
        void onUpKey(int position);
    }

    private OnLeftKeyListener mLeftListener;
    public interface OnLeftKeyListener {
        void onLeftKey();
    }

    public void setDownKeyListener(OnDownKeyListener listener){
        mDownListener = listener;
    }

    public void setUpKeyListener(OnUpKeyListener listener){
        mUpListener = listener;
    }

    public void setLeftKeyListener(OnLeftKeyListener listener){
        mLeftListener = listener;
    }

    public GridLayoutManagerTV(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }

    public GridLayoutManagerTV(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public View onFocusSearchFailed(View focused, int focusDirection, RecyclerView.Recycler recycler, RecyclerView.State state) {
        View nextFocus = super.onFocusSearchFailed(focused, focusDirection, recycler, state);
        if(focusDirection == View.FOCUS_DOWN)
            return focused;
        return null;
    }

    @Override
    public View onInterceptFocusSearch(View focused, int direction) {
        int count = getItemCount();
        int fromPos = getPosition(focused);
        int lastVisibleItemPos = findLastVisibleItemPosition();
        switch (direction) {
            case View.FOCUS_DOWN:
                fromPos+=4;
                break;
            case View.FOCUS_UP:
                fromPos-=4;
                break;
        }
        if(fromPos < 0 || fromPos >= count ) {
            return focused;
        } else {
            if (fromPos > lastVisibleItemPos) {
                scrollToPosition(fromPos);
            }
        }
        if(direction == View.FOCUS_LEFT) {
            int iPos = getPosition(focused);
            if (iPos % 4 == 0) {
                if (mLeftListener != null)
                    mLeftListener.onLeftKey();
            }
        } else if(direction == View.FOCUS_UP) {
            if (mUpListener != null)
                mUpListener.onUpKey(fromPos);
        } else if(direction == View.FOCUS_DOWN) {
            if (mDownListener != null)
                mDownListener.onDownKey(fromPos);
        }
        return super.onInterceptFocusSearch(focused, direction);
    }
}
