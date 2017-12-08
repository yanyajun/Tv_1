package tv.dfyc.yckt.custom;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import tv.dfyc.yckt.adapter.GeneralInfoAdapter;

/**
 * Created by admin on 2017-11-23.
 */

public class GridLayoutRecordManangerTv extends GridLayoutManager {

    private GeneralInfoAdapter mLessonAdapter;
    public void setFocusFailedAdapter(GeneralInfoAdapter lessonAdapter) {
        mLessonAdapter = lessonAdapter;
    }

    public GridLayoutRecordManangerTv(Context context, int spanCount) {
        super(context, spanCount);
    }

    private GridLayoutRecordManangerTv.OnDownKeyListener mDownListener;
    public interface  OnDownKeyListener{
        void onDownKey(int position);
    }

    private GridLayoutRecordManangerTv.OnUpKeyListener mUpListener;
    public interface OnUpKeyListener {
        void onUpKey(int position);
    }

    private GridLayoutRecordManangerTv.OnQuitKeyListener mQuitListener;
    public interface OnQuitKeyListener {
        void onQuitKey();
    }

    private GridLayoutRecordManangerTv.OnLeftKeyListener mLeftListener;
    public interface OnLeftKeyListener {
        void onLeftKey();
    }

    private GridLayoutRecordManangerTv.OnRightKeyListener mRightListener;
    public interface OnRightKeyListener {
        void onRightKey();
    }

    public void setDownKeyListener(GridLayoutRecordManangerTv.OnDownKeyListener listener){
        mDownListener = listener;
    }

    public void setUpKeyListener(GridLayoutRecordManangerTv.OnUpKeyListener listener){
        mUpListener = listener;
    }

    public void setQuitKeyListener(GridLayoutRecordManangerTv.OnQuitKeyListener listener){
        mQuitListener = listener;
    }

    public void setLeftKeyListener(GridLayoutRecordManangerTv.OnLeftKeyListener listener){
        mLeftListener = listener;
    }

    public void setRightKeyListener(GridLayoutRecordManangerTv.OnRightKeyListener listener){
        mRightListener = listener;
    }


    public GridLayoutRecordManangerTv(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }

    public GridLayoutRecordManangerTv(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public View onFocusSearchFailed(View focused, int focusDirection, RecyclerView.Recycler recycler, RecyclerView.State state) {
        View nextFocus = super.onFocusSearchFailed(focused, focusDirection, recycler, state);
        if(focusDirection == View.FOCUS_DOWN){
            return focused;
        }
        if(focusDirection == View.FOCUS_LEFT) {
            if(mLessonAdapter != null)
                return mLessonAdapter.getSelectedView();
        }
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
        if(fromPos >= count ) {
            //return null;
            //return findViewByPosition(findLastVisibleItemPosition());
        } else {
            if (fromPos > lastVisibleItemPos) {
                scrollToPosition(fromPos);
            }
        }
        if(direction == View.FOCUS_LEFT) {
            int iPos = getPosition(focused);
            //if (iPos % 4 == 0) {
                if (mLeftListener != null)
                    mLeftListener.onLeftKey();
            //}
        } else if(direction == View.FOCUS_UP) {
            int iPos = getPosition(focused);
            if (iPos == 0|| iPos == 1 || iPos == 2||iPos == 3){
                if (mQuitListener != null){
                    mQuitListener.onQuitKey();
                    return focused;
                }

            }
            if (mUpListener != null)
                mUpListener.onUpKey(fromPos);


        } else if(direction == View.FOCUS_DOWN) {
            if (mDownListener != null)
                mDownListener.onDownKey(fromPos);
        }else if(direction == View.FOCUS_RIGHT){
            int iPos = getPosition(focused);
            if ((iPos+1) % 4 == 0) {
            if (mRightListener != null)
                mRightListener.onRightKey();
            }
        }
        return super.onInterceptFocusSearch(focused, direction);
    }
}
