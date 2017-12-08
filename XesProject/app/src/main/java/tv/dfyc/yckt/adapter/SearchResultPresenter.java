package tv.dfyc.yckt.adapter;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.open.androidtvwidget.leanback.adapter.GeneralAdapter;
import com.open.androidtvwidget.leanback.mode.OpenPresenter;
import com.open.androidtvwidget.view.LabelView;

import java.util.List;

import tv.dfyc.yckt.R;
import tv.dfyc.yckt.custom.RoundImageView;
import tv.dfyc.yckt.util.LessonDetail;

/**
 * Created by admin on 2017-11-14.
 */

public class SearchResultPresenter extends OpenPresenter {
    private GeneralAdapter mAdapter;
    private Context mContext;
    private List<LessonDetail> mDetailList;

    public SearchResultPresenter(Context context, List<LessonDetail> list) {
        mContext = context;
        mDetailList = list;
    }

    @Override
    public void setAdapter(GeneralAdapter adapter) {
        this.mAdapter = adapter;
    }

    @Override
    public int getItemCount() {
        return mDetailList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_result_item, parent, false);
        SearchResultViewHolder holder = new SearchResultViewHolder(view);
        holder.SearchResultImage = (RoundImageView) view.findViewById(R.id.search_result_lesson_detail_image);
        holder.SearchResultName = (TextView)view.findViewById(R.id.search_result_detail_name);
        holder.SeearchResultCharge = (ImageView)view.findViewById(R.id.search_result_lesson_detail_charge);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        SearchResultViewHolder holder = (SearchResultViewHolder) viewHolder;
        Glide.with(mContext).load(mDetailList.get(position).getmLessonImageUrl()).placeholder(mDetailList.get(position).getmLessonImageID()).into(holder.SearchResultImage);
        holder.SearchResultName.setText(mDetailList.get(position).getmLessonName());
        int isCharge = mDetailList.get(position).getIsCharge();
        if(isCharge == 0)
            holder.SeearchResultCharge.setVisibility(View.VISIBLE);
        else
            holder.SeearchResultCharge.setVisibility(View.INVISIBLE);

    }

    class SearchResultViewHolder extends OpenPresenter.ViewHolder{
        RoundImageView SearchResultImage;
        TextView SearchResultName;
        ImageView SeearchResultCharge;

        public SearchResultViewHolder(View itemView) {
            super(itemView);
        }
    }

}
