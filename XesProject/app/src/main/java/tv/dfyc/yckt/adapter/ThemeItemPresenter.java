package tv.dfyc.yckt.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.open.androidtvwidget.leanback.mode.DefualtListPresenter;

import tv.dfyc.yckt.R;
import tv.dfyc.yckt.beans.ThemeListItem;
import tv.dfyc.yckt.custom.OpenCardView;
import tv.dfyc.yckt.custom.RoundImageView;

/**
 * Created by android on 2017/11/22.
 */

public class ThemeItemPresenter extends DefualtListPresenter {

    private boolean mIsSelect;
    private Drawable focus;
    private Drawable normal;
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
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_theme_item, parent, false);
        return new ViewHolder(itemView);
    }

    public void setSelect(boolean isSelect) {
        this.mIsSelect = isSelect;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        normal = viewHolder.view.getResources().getDrawable(R.color.transparent);
        focus = viewHolder.view.getResources().getDrawable(R.drawable.yellow_white_border);
        ThemeListItem themeListItem = ((ThemeListItem) getItem(position));
        OpenCardView openCardView = (OpenCardView) viewHolder.view;

        final TextView tv = (TextView) openCardView.findViewById(R.id.tv_content);
        RoundImageView iv = (RoundImageView) openCardView.findViewById(R.id.iv_content);
        Glide.with(context).load(themeListItem.getThumb_app_h()).into(iv);
        tv.setText(themeListItem.getTitle());

        openCardView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    ((OpenCardView) v).setShadowDrawable(focus);
                    v.animate().scaleX(1.05f).scaleY(1.05f).setDuration(200).start();
                    tv.setMaxLines(2);
//                    ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) tv.getLayoutParams();
//                    params.height = (int)getDimension(R.dimen.h_80);
//                    params.setMargins(0,(int)getDimension(R.dimen.h_274),0,(int)getDimension(R.dimen.h_6));//4个参数按顺序分别是左上右下
//                    tv.setLayoutParams(params);
//                    tv.setTextSize(getDimension(R.dimen.h_27));
                } else {
                    ((OpenCardView) v).setShadowDrawable(normal);
                    v.animate().scaleX(1.0f).scaleY(1.0f).setDuration(200).start();
                    tv.setMaxLines(1);
//                    ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) tv.getLayoutParams();
//                    params.height = (int)getDimension(R.dimen.h_45);
//                    params.setMargins(0,(int)getDimension(R.dimen.h_308),0,0);//4个参数按顺序分别是左上右下
//                    tv.setLayoutParams(params);
//                    tv.setTextSize(getDimension(R.dimen.h_26));
                }
            }
        });
    }
    public float getDimension(int id) {
        return context.getResources().getDimension(id);
    }
}
