package tv.dfyc.yckt.custom;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by admin on 2017-9-26.
 */

public class FeedbackItemSpacing extends RecyclerView.ItemDecoration{

    private int left_space;
    private int top_space;
    private int right_space;
    private int bottom_space;

    public FeedbackItemSpacing(int left, int top, int right, int bottom) {
        this.left_space = left;
        this.top_space = top;
        this.right_space = right;
        this.bottom_space = bottom;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int pos = parent.getChildAdapterPosition(view);
        if((pos+1)%3 == 0 ){
            outRect.right = 0;
        } else {
            outRect.right = right_space;
        }
        outRect.top = 0;
        outRect.left = 0;
        outRect.bottom = bottom_space;

    }
}
