package tv.dfyc.yckt.adapter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.open.androidtvwidget.leanback.adapter.GeneralAdapter;
import com.open.androidtvwidget.leanback.mode.OpenPresenter;
import tv.dfyc.yckt.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by admin on 2017-7-26.
 */

public class selectCoursePresenter extends OpenPresenter {
    private GeneralAdapter mAdapter;
    private Context mContext;

    private ArrayList<HashMap<String, Object>> mDetailList;

   public selectCoursePresenter(Context context, ArrayList<HashMap<String, Object>> list) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_course_item, parent, false);
        return new DetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        DetailViewHolder holder = (DetailViewHolder) viewHolder;
        holder.courseName.setText(mDetailList.get(position).get("select_class_name").toString());
        int pointIsFree = Integer.valueOf(mDetailList.get(position).get("select_point_isfree").toString()).intValue();
        int videoIsFree = Integer.valueOf(mDetailList.get(position).get("select_class_isfree").toString()).intValue();
        if (pointIsFree ==1 && videoIsFree == 2){
            holder.cornerImage.setVisibility(View.VISIBLE);
        }
    }

    class DetailViewHolder extends OpenPresenter.ViewHolder {

        public TextView courseName;
        public ImageView cornerImage;

        public DetailViewHolder(View itemView) {
            super(itemView);
            courseName = (TextView) itemView.findViewById(R.id.course_name);
            cornerImage = (ImageView)itemView.findViewById(R.id.corner_iamge);
        }

    }
}



