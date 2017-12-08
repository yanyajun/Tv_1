package tv.dfyc.yckt.info.search;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import tv.dfyc.yckt.R;

/**
 * Created by admin on 2017-11-16.
 */

public class HotSearchGirdLayoutManager extends GridLayoutManager {

    public HotSearchGirdLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    private HotSearchGirdLayoutManager.OnDownKeyListener mDownListener;
    public interface  OnDownKeyListener{
        void onDownKey();
    }

    private HotSearchGirdLayoutManager.OnUpKeyListener mUpListener;
    public interface OnUpKeyListener {
        void onUpKey();
    }

    private HotSearchGirdLayoutManager.OnLeftKeyListener mLeftListener;
    public interface OnLeftKeyListener {
        void onLeftKey();
    }

    public void setDownKeyListener(HotSearchGirdLayoutManager.OnDownKeyListener listener){
        mDownListener = listener;
    }

    public void setUpKeyListener(HotSearchGirdLayoutManager.OnUpKeyListener listener){
        mUpListener = listener;
    }

    public void setLeftKeyListener(HotSearchGirdLayoutManager.OnLeftKeyListener listener){
        mLeftListener = listener;
    }

    public HotSearchGirdLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }

    public HotSearchGirdLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public View onFocusSearchFailed(View focused, int focusDirection, RecyclerView.Recycler recycler, RecyclerView.State state) {
        View nextFocus = super.onFocusSearchFailed(focused, focusDirection, recycler, state);
        return null;
    }

    @Override
    public View onInterceptFocusSearch(View focused, int direction) {
        if (direction == View.FOCUS_UP) {
            int iPos = getPosition(focused);
            //if(iPos == 0 || iPos == 1 || iPos == 2) {
                if(mUpListener != null)
                    mUpListener.onUpKey();
           // }
        } else if(direction == View.FOCUS_LEFT) {
            int iPos = getPosition(focused);
            if(iPos % 3 == 0) {
                if(mLeftListener != null)
                    mLeftListener.onLeftKey();
            }
        } else if (direction == View.FOCUS_DOWN){
            int iPos = getPosition(focused);
            if (iPos == 4 || iPos == 5 || iPos == 3) {
                if(mDownListener != null)
                    mDownListener.onDownKey();
            }
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
