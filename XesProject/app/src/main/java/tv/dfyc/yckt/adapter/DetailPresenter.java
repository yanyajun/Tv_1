package tv.dfyc.yckt.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.open.androidtvwidget.leanback.adapter.GeneralAdapter;
import com.open.androidtvwidget.leanback.mode.OpenPresenter;
import tv.dfyc.yckt.R;
import tv.dfyc.yckt.util.LessonDetail;

import java.util.List;

/**
 * 测试.
 * Created by hailongqiu on 2016/8/24.
 */
public class DetailPresenter extends OpenPresenter {
    private GeneralAdapter mAdapter;
    private Context mContext;
    private List<LessonDetail> mDetailList;

    public DetailPresenter(Context context, List<LessonDetail> list) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lesson_detail, parent, false);
        return new DetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        DetailViewHolder holder = (DetailViewHolder) viewHolder;
        Glide.with(mContext).load(mDetailList.get(position).getmLessonImageUrl()).placeholder(mDetailList.get(position).getmLessonImageID()).into(holder.detailImage);
        holder.detailName.setText(mDetailList.get(position).getmLessonName());
        holder.detailTimes.setText(mDetailList.get(position).getmLessonTimes());
        int isCharge = mDetailList.get(position).getIsCharge();
        if(isCharge == 0)
            holder.detailCharge.setVisibility(View.VISIBLE);
        else
            holder.detailCharge.setVisibility(View.INVISIBLE);
    }
}
