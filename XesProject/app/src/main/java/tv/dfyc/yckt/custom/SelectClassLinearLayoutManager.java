package tv.dfyc.yckt.custom;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Button;

import tv.dfyc.yckt.R;

/**
 * Created by admin on 2017-11-29.
 */

public class SelectClassLinearLayoutManager extends LinearLayoutManager {
    private boolean isScrollEnabled = true;

    public SelectClassLinearLayoutManager(Context context) {
        super(context);
    }

    public void setScrollEnabled(boolean flag) {
        this.isScrollEnabled = flag;
    }

    private SelectClassLinearLayoutManager.OnDownKeyListener mDownListener;
    public interface  OnDownKeyListener{
        void onDownKey(View focused);
    }

    public void setDownKeyListener(SelectClassLinearLayoutManager.OnDownKeyListener listener){
        mDownListener = listener;
    }

    private SelectClassLinearLayoutManager.OnUpKeyListener mUpListener;
    public interface  OnUpKeyListener{
        void onUpKey(View focused);
    }

    public void setUpKeyListener(SelectClassLinearLayoutManager.OnUpKeyListener listener){
        mUpListener = listener;
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

    private Button mButton;

    public void setFocusFailedButton(Button button) {
        mButton = button;
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
    public View onInterceptFocusSearch(View focused, int direction) {
        if (direction == View.FOCUS_DOWN) {
            if (mDownListener != null)
            mDownListener.onDownKey(focused);
        }
        else if (direction == View.FOCUS_UP) {
            if (mUpListener != null)
                mUpListener.onUpKey(focused);
            return mButton;
        } else if (direction == View.FOCUS_LEFT) {
            if(mLeftListener != null)
                mLeftListener.onLeftKey(focused);
        } else if (direction == View.FOCUS_RIGHT) {
            if(mRightListener != null)
                mRightListener.onRightKey(focused);
            int pos = getPosition(getFocusedChild());
            View view = getChildAt(pos);
            if (view != null && pos != getItemCount()) {
                View subView = view.findViewById(R.id.top_one);
                if (subView != null && subView.equals(focused)) {
                    View firstView = getChildAt(pos+1);
                    if (firstView != null) {
                        View firstSubView = firstView.findViewById(R.id.top_one);
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
