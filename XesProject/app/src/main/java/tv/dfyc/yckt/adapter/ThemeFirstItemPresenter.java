package tv.dfyc.yckt.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.open.androidtvwidget.leanback.mode.DefualtListPresenter;

import tv.dfyc.yckt.R;
import tv.dfyc.yckt.custom.OpenCardView;

/**
 * Created by android on 2017/11/22.
 */

public class ThemeFirstItemPresenter extends DefualtListPresenter {

    private boolean mIsSelect;
    private Context context;

    /**
     * 你可以重写这里，传入AutoGridViewLayoutManger.
     */
    @Override
    public RecyclerView.LayoutManager getLayoutManger(Context context) {
        this.context = context;
        return super.getLayoutManger(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_theme_firstitem, parent, false);
        return new ViewHolder(itemView);
    }

    public void setSelect(boolean isSelect) {
        this.mIsSelect = isSelect;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        String id = (String) getItem(position);
        OpenCardView openCardView = (OpenCardView) viewHolder.view;
        ImageView iv = (ImageView) openCardView.findViewById(R.id.iv_theme_top);
        Glide.with(context).load(id).into(iv);
    }


}
