package tv.dfyc.yckt.custom;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.open.androidtvwidget.leanback.recycle.LinearLayoutManagerTV;

import tv.dfyc.yckt.R;
import tv.dfyc.yckt.adapter.GeneralInfoAdapter;

/**
 * Created by admin on 2017-11-16.
 */

public class LinearLayoutManagerPurchase extends LinearLayoutManagerTV {

    private GeneralInfoAdapter mLessonAdapter;
    public void setFocusFailedAdapter(GeneralInfoAdapter lessonAdapter) {
        mLessonAdapter = lessonAdapter;
    }

    private int[] mMeasuredDimension = new int[2];
    private boolean mIsAutoMeaure = false;

    public LinearLayoutManagerPurchase(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public LinearLayoutManagerPurchase(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public LinearLayoutManagerPurchase(Context context) {
        super(context);
    }

    /**
     * 用于leanback自动适应.
     */
    @Override
    public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {

        if (!mIsAutoMeaure) {
            super.onMeasure(recycler, state, widthSpec, heightSpec);
        } else {

            final int widthMode = View.MeasureSpec.getMode(widthSpec);
            final int heightMode = View.MeasureSpec.getMode(heightSpec);
            final int widthSize = View.MeasureSpec.getSize(widthSpec);
            final int heightSize = View.MeasureSpec.getSize(heightSpec);

            int width = 0;
            int height = 0;

            //
            int tempWidth = 0;
            int tempHeight = 0;

            for (int i = 0; i < getItemCount(); i++) {
                try {
                    measureScrapChild(recycler, i,
                            widthSpec,
                            View.MeasureSpec.makeMeasureSpec(i, View.MeasureSpec.UNSPECIFIED),
                            mMeasuredDimension);
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
                if (getOrientation() == HORIZONTAL) {
                    tempWidth += mMeasuredDimension[0];
                    if (i == 0) {
                        tempHeight = mMeasuredDimension[1];
                    }
                } else {  // VERTICAL
                    tempHeight += mMeasuredDimension[1];
                    if (i == 0) {
                        tempWidth = mMeasuredDimension[0];
                    }
                }
            }

            switch (widthMode) {
                case View.MeasureSpec.EXACTLY:
                case View.MeasureSpec.AT_MOST:
                    width = widthSize;
                    break;
                case View.MeasureSpec.UNSPECIFIED:
                default:
                    width = tempWidth;
                    break;
            }

            switch (heightMode) {
                case View.MeasureSpec.EXACTLY:
                case View.MeasureSpec.AT_MOST:
                    height = heightSize;
                    break;
                case View.MeasureSpec.UNSPECIFIED:
                default:
                    height = tempHeight;
                    break;
            }

            setMeasuredDimension(width, height);
        }

    }

    private void measureScrapChild(RecyclerView.Recycler recycler, int position, int widthSpec, int heightSpec, int[] measuredDimension) {
        View view = recycler.getViewForPosition(position);
        if (view != null) {
            RecyclerView.LayoutParams p = (RecyclerView.LayoutParams) view.getLayoutParams();
            int childHeightSpec = ViewGroup.getChildMeasureSpec(heightSpec,
                    getPaddingTop() + getPaddingBottom(), p.height);
            view.measure(widthSpec, childHeightSpec);
            measuredDimension[0] = view.getMeasuredWidth() + p.leftMargin + p.rightMargin;
            measuredDimension[1] = view.getMeasuredHeight() + p.bottomMargin + p.topMargin;
            recycler.recycleView(view);
        }
    }

    private LinearLayoutManagerPurchase.OnDownKeyListener mDownListener;
    public interface  OnDownKeyListener{
        void onDownKey();
    }

    private LinearLayoutManagerPurchase.OnUpKeyListener mUpListener;
    public interface OnUpKeyListener {
        void onUpKey();
    }

    private LinearLayoutManagerPurchase.OnLeftKeyListener mLeftListener;
    public interface OnLeftKeyListener {
        void onLeftKey();
    }


    public void setDownKeyListener(LinearLayoutManagerPurchase.OnDownKeyListener listener){
        mDownListener = listener;
    }

    public void setUpKeyListener(LinearLayoutManagerPurchase.OnUpKeyListener listener){
        mUpListener = listener;
    }

    public void setLeftKeyListener(LinearLayoutManagerPurchase.OnLeftKeyListener listener){
        mLeftListener = listener;
    }

    /**
     * 自动适应布局. (当height="wrap_..")
     */
    public void setAutoMeasureEnabled(boolean isAutoMeaure) {
        this.mIsAutoMeaure = isAutoMeaure;
    }

    @Override
    public View onFocusSearchFailed(View focused, int focusDirection, RecyclerView.Recycler recycler, RecyclerView.State state) {
        View nextFocus = super.onFocusSearchFailed(focused, focusDirection, recycler, state);
        if(focusDirection == View.FOCUS_LEFT) {
            if(mLessonAdapter != null)
                return mLessonAdapter.getSelectedView();
        }
        return null;
    }

    @Override
    public View onInterceptFocusSearch(View focused, int direction) {
        if (direction == View.FOCUS_UP) {
            View view = getChildAt(0);
            if (view != null) {
                View subView = view.findViewById(R.id.purchase_background);
                if (subView != null && subView.equals(focused)) {
                    return focused;
                }
            }
                mUpListener.onUpKey();
        } else if(direction == View.FOCUS_LEFT) {
            mLeftListener.onLeftKey();
        } else if (direction == View.FOCUS_DOWN){
            mDownListener.onDownKey();
        }
        return super.onInterceptFocusSearch(focused, direction);
    }
}
