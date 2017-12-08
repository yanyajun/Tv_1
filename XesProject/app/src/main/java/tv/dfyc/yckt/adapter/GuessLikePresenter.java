package tv.dfyc.yckt.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
 * Created by admin on 2017-11-13.
 */

public class GuessLikePresenter extends OpenPresenter {
    private GeneralAdapter mAdapter;
    private Context mContext;
    private List<LessonDetail> mDetailList;

    public GuessLikePresenter(Context context, List<LessonDetail> list) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.guess_like_item, parent, false);
        GuessLikeViewHolder holder = new GuessLikeViewHolder(view);
        holder.GuessLikeImage = (RoundImageView) view.findViewById(R.id.guess_like_lesson_detail_image);
        holder.GuessLikeName = (TextView)view.findViewById(R.id.guess_like_detail_name);
        holder.GuessLikeCharge = (ImageView)view.findViewById(R.id.guess_like_lesson_detail_charge);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        GuessLikeViewHolder holder = (GuessLikeViewHolder) viewHolder;
        Glide.with(mContext).load(mDetailList.get(position).getmLessonImageUrl()).placeholder(mDetailList.get(position).getmLessonImageID()).into(holder.GuessLikeImage);
        holder.GuessLikeName.setText(mDetailList.get(position).getmLessonName());
        int isCharge = mDetailList.get(position).getIsCharge();
        if(isCharge == 0)
            holder.GuessLikeCharge.setVisibility(View.VISIBLE);
        else
            holder.GuessLikeCharge.setVisibility(View.INVISIBLE);
    }

    class GuessLikeViewHolder extends OpenPresenter.ViewHolder{
        RoundImageView GuessLikeImage;
        TextView GuessLikeName;
        ImageView GuessLikeCharge;


        public GuessLikeViewHolder(View itemView) {
            super(itemView);
        }
    }
}
