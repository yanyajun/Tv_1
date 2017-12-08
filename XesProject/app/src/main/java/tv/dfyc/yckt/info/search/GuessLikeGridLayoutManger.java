package tv.dfyc.yckt.info.search;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;


/**
 * Created by admin on 2017-11-16.
 */

public class GuessLikeGridLayoutManger extends GridLayoutManager {
    public GuessLikeGridLayoutManger(Context context, int spanCount) {
        super(context, spanCount);
    }

    private GuessLikeGridLayoutManger.OnDownKeyListener mDownListener;
    public interface  OnDownKeyListener{
        void onDownKey();
    }

    private GuessLikeGridLayoutManger.OnUpKeyListener mUpListener;
    public interface OnUpKeyListener {
        void onUpKey();
    }

    private GuessLikeGridLayoutManger.OnLeftKeyListener mLeftListener;
    public interface OnLeftKeyListener {
        void onLeftKey();
    }

    public void setDownKeyListener(GuessLikeGridLayoutManger.OnDownKeyListener listener){
        mDownListener = listener;
    }

    public void setUpKeyListener(GuessLikeGridLayoutManger.OnUpKeyListener listener){
        mUpListener = listener;
    }

    public void setLeftKeyListener(GuessLikeGridLayoutManger.OnLeftKeyListener listener){
        mLeftListener = listener;
    }

    public GuessLikeGridLayoutManger(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }

    public GuessLikeGridLayoutManger(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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
            //if(iPos == 0 || iPos == 1 || iPos == 2 || iPos == 3) {
                if(mUpListener != null)
                    mUpListener.onUpKey();
           // }
        } else if(direction == View.FOCUS_LEFT) {
            int iPos = getPosition(focused);
            if(iPos  == 0) {
                if(mLeftListener != null)
                    mLeftListener.onLeftKey();
            }
        } else if (direction == View.FOCUS_DOWN){
            int iPos = getPosition(focused);
            //if (iPos == 4 || iPos == 5 || iPos == 6 || iPos == 7) {
                if(mDownListener != null)
                    mDownListener.onDownKey();
           // }
        }
        return super.onInterceptFocusSearch(focused, direction);
    }
}
