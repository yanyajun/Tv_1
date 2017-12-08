package tv.dfyc.yckt.custom;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import tv.dfyc.yckt.adapter.ClassListAdapter;
import tv.dfyc.yckt.adapter.LessonListAdapter;

/**
 * Created by yyj on 2017/11/27 0027.
 */

public class GridLayoutManagerListPage extends GridLayoutManager {
    public GridLayoutManagerListPage(Context context, int spanCount) {
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
        void onLeftKey(int position);
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

    private ClassListAdapter mClassAdapter;
    private LessonListAdapter mLessonAdapter;
    public void setFocusFailedAdapter(ClassListAdapter classAdapter, LessonListAdapter lessonAdapter) {
        if(classAdapter != null)
            mClassAdapter = classAdapter;
        if(lessonAdapter != null)
            mLessonAdapter = lessonAdapter;
    }

    public GridLayoutManagerListPage(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }

    public GridLayoutManagerListPage(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        return super.scrollVerticallyBy(dy, recycler, state);
    }

    @Override
    public View onFocusSearchFailed(View focused, int focusDirection, RecyclerView.Recycler recycler, RecyclerView.State state) {
        View nextFocus = super.onFocusSearchFailed(focused, focusDirection, recycler, state);
        if(focusDirection == View.FOCUS_DOWN)
            return focused;
        if(focusDirection == View.FOCUS_LEFT) {
            if(mClassAdapter == null && mLessonAdapter != null)
                return mLessonAdapter.getSelectedView();
            if(mClassAdapter != null && mLessonAdapter == null)
                return mClassAdapter.getSelectedView();
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
        if(fromPos < 0 || fromPos >= count ) {
            //return focused; // 最后一行没有占满时，焦点在倒数第二行时，按下焦点停在当前item(去掉)
        } else {
            if (fromPos > lastVisibleItemPos) {
                scrollToPosition(fromPos);
            }
        }
        if(direction == View.FOCUS_LEFT) {
            int iPos = getPosition(focused);
            if (iPos % 4 == 0) {
                if (mLeftListener != null)
                    mLeftListener.onLeftKey(fromPos);
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
