package com.open.androidtvwidget.leanback.recycle;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by hailongqiu on 2016/8/25.
 */
public class NativeGridLayoutManagerTV extends GridLayoutManager {
    public NativeGridLayoutManagerTV(Context context, int spanCount) {
        super(context, spanCount);
    }

    public NativeGridLayoutManagerTV(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }

    public NativeGridLayoutManagerTV(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public View onFocusSearchFailed(View focused, int focusDirection, RecyclerView.Recycler recycler, RecyclerView.State state) {
        View nextFocus = super.onFocusSearchFailed(focused, focusDirection, recycler, state);
        return null;
    }

    private NativeGridLayoutManagerTV.OnDownKeyListener mDownListener;
    public interface  OnDownKeyListener{
        void onDownKey();
    }

    private NativeGridLayoutManagerTV.OnUpKeyListener mUpListener;
    public interface OnUpKeyListener {
        void onUpKey();
    }

    private NativeGridLayoutManagerTV.OnLeftKeyListener mLeftListener;
    public interface OnLeftKeyListener {
        void onLeftKey();
    }

    public void setDownKeyListener(NativeGridLayoutManagerTV.OnDownKeyListener listener){
        mDownListener = listener;
    }

    public void setUpKeyListener(NativeGridLayoutManagerTV.OnUpKeyListener listener){
        mUpListener = listener;
    }

    public void setLeftKeyListener(NativeGridLayoutManagerTV.OnLeftKeyListener listener){
        mLeftListener = listener;
    }

    @Override
    public View onInterceptFocusSearch(View focused, int direction) {
        int count = getItemCount();
        int fromPos = getPosition(focused);
        int lastVisibleItemPos = findLastVisibleItemPosition();
        switch (direction) {
            case View.FOCUS_DOWN:
                fromPos+=2;
                break;
            case View.FOCUS_UP:
                fromPos-=2;
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
            if (iPos % 2 == 0) {
                if (mLeftListener != null)
                    mLeftListener.onLeftKey();
            }
        } else if(direction == View.FOCUS_UP) {
            if (mUpListener != null)
                mUpListener.onUpKey();
        } else if(direction == View.FOCUS_DOWN) {
            if (mDownListener != null)
                mDownListener.onDownKey();
        }
        return super.onInterceptFocusSearch(focused, direction);
    }

}
