package tv.dfyc.yckt.custom;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by admin on 2017-7-26.
 */

public class CourseRecyclerItemSpace extends RecyclerView.ItemDecoration {

    private int left_space;
    private int top_space;
    private int right_space;
    private int bottom_space;

    public CourseRecyclerItemSpace(int left, int top, int right, int bottom) {
        this.left_space = left;
        this.top_space = top;
        this.right_space = right;
        this.bottom_space = bottom;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int pos = parent.getChildAdapterPosition(view);
        if(pos == 0 || pos == 1 || pos == 2) {
            outRect.top = top_space;
        } else {
            outRect.top = 0;
        }

        outRect.left = left_space;
        outRect.right = right_space;
        outRect.bottom = bottom_space;

//        if(pos == 0 || pos == 1 || pos == 2 || pos == 3) {
//            outRect.left = left_space;
//        } else {
//            outRect.left = 0;
//        }
//        if(pos % 4 == 0) {
//            outRect.top = top_space;
//        } else {
//            outRect.top = 0;
//        }
        //outRect.right = right_space;
        //outRect.bottom = bottom_space-outRect.top;
    }
}
