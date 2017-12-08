package tv.dfyc.yckt.adapter;

import android.content.Context;
import android.media.Image;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
 * Created by admin on 2017-11-17.
 */

public class RecordPresenter extends OpenPresenter {

    private GeneralAdapter mAdapter;
    private Context mContext;
    private List<LessonDetail> mDetailList;
    private boolean mInDeleteMode  =false;
    private Boolean mIsBig = false;

    public RecordPresenter(Context context, List<LessonDetail> list) {
        mContext = context;
        mDetailList = list;
    }



    public void InDelteMode(boolean mode){
        mInDeleteMode = mode;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.record_item, parent, false);
        RecordPresenter.RecordViewHolder holder = new RecordPresenter.RecordViewHolder(view);
        holder.RecordImage = (RoundImageView) view.findViewById(R.id.record_detail_image);
        holder.LittleRecordName = (TextView)view.findViewById(R.id.little_record_detail_name);
        holder.RecordCharge = (ImageView)view.findViewById(R.id.record_detail_charge);
        holder.mRecord_layout_mask = (RelativeLayout)view.findViewById(R.id.record_layout_mask);
        holder.mRecord_delete = (ImageView)view.findViewById(R.id.record_delete);
        holder.mRecord_layout = (RelativeLayout)view.findViewById(R.id.record_layout);
        holder.LittleRecord_mask_layout = (LinearLayout)view.findViewById(R.id.little_record_mask_layout);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        final RecordPresenter.RecordViewHolder holder = (RecordPresenter.RecordViewHolder) viewHolder;
        Glide.with(mContext).load(mDetailList.get(position).getmLessonImageUrl()).placeholder(mDetailList.get(position).getmLessonImageID()).into(holder.RecordImage);
        holder.LittleRecordName.setText(mDetailList.get(position).getmLessonName());
        int isCharge = mDetailList.get(position).getIsCharge();
        if(isCharge == 0)
            holder.RecordCharge.setVisibility(View.VISIBLE);
        else
            holder.RecordCharge.setVisibility(View.INVISIBLE);

        if (mInDeleteMode){
            holder.mRecord_layout_mask.setVisibility(View.VISIBLE);
        }else {
            holder.mRecord_layout_mask.setVisibility(View.GONE);
        }

        holder.mRecord_delete.setTag("delete");
    }

    class RecordViewHolder extends OpenPresenter.ViewHolder{
        RoundImageView RecordImage;
        TextView LittleRecordName;
        ImageView RecordCharge;
        RelativeLayout mRecord_layout_mask;
        ImageView mRecord_delete;
        RelativeLayout mRecord_layout;
        LinearLayout LittleRecord_mask_layout;

        public RecordViewHolder(View itemView) {
            super(itemView);
        }
    }
}
