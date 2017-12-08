package tv.dfyc.yckt.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.open.androidtvwidget.view.RelativeMainLayout;

import tv.dfyc.yckt.R;
import tv.dfyc.yckt.adapter.GeneralSelectClassAdapter;

/**
 * Created by Administrator on 2017/11/16 0016.
 */

public class ListPageLayout extends RelativeLayout {

    private GeneralSelectClassAdapter mLessonAdapter;
    private boolean mIsInSelect;

    public void setFocusFailedAdapter(GeneralSelectClassAdapter lessonAdapter) {
        mLessonAdapter = lessonAdapter;

    }

    public void setIsInSelect(Boolean isInCalss){
        mIsInSelect = isInCalss;
    }

    public ListPageLayout(Context context) {
        super(context);
    }

    public ListPageLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private OnDownKeyListener mDownListener;
    public interface  OnDownKeyListener{
        void onDownKey(View focused);
    }

    private OnUpKeyListener mUpListener;
    public interface OnUpKeyListener {
        void onUpKey(View focused);
    }

    private OnLeftKeyListener mLeftListener;
    public interface OnLeftKeyListener {
        void onLeftKey(View focused);
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



    @Override
    public View focusSearch(View focused, int direction) {

        if (direction == View.FOCUS_DOWN){
//            if(mDownListener != null) {
//                mDownListener.onDownKey(focused);
//            }
            if(mLessonAdapter != null)
                if (mLessonAdapter.getItemCount()>0 && mIsInSelect)
                    return mLessonAdapter.setDefaultView(0);
        }
        return super.focusSearch(focused, direction);
    }
}
