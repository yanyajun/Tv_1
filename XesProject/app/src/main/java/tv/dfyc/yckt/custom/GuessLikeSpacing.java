package tv.dfyc.yckt.custom;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by admin on 2017-11-13.
 */

public class GuessLikeSpacing extends RecyclerView.ItemDecoration {

    private int left_space;
    private int top_space;
    private int right_space;
    private int bottom_space;

    public GuessLikeSpacing(int left, int top, int right, int bottom) {
        this.left_space = left;
        this.top_space = top;
        this.right_space = right;
        this.bottom_space = bottom;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int pos = parent.getChildAdapterPosition(view);
        outRect.top = top_space;
        outRect.left = left_space;
        outRect.right = right_space;
        outRect.bottom = bottom_space;
    }
}
