package tv.dfyc.yckt.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.open.androidtvwidget.leanback.adapter.GeneralAdapter;
import com.open.androidtvwidget.leanback.mode.OpenPresenter;

import java.util.List;

import tv.dfyc.yckt.R;
import tv.dfyc.yckt.beans.JsonSearchData;

/**
 * Created by admin on 2017-11-13.
 */

public class HotSearchPresenter extends OpenPresenter {

    private GeneralAdapter mAdapter;
    private Context mContext;
    private List<JsonSearchData.SearchData.Hotsearch_list> mDetailList;

    public HotSearchPresenter(Context context , List<JsonSearchData.SearchData.Hotsearch_list> list) {
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
    public OpenPresenter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hot_search_item, parent, false);
        return new HotSearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OpenPresenter.ViewHolder viewHolder, int position) {
        HotSearchViewHolder holder = (HotSearchViewHolder) viewHolder;
        holder.courseName.setText(mDetailList.get(position).getTitle());
    }

    class HotSearchViewHolder extends OpenPresenter.ViewHolder {

        public TextView courseName;

        public HotSearchViewHolder(View itemView) {
            super(itemView);
            courseName = (TextView) itemView.findViewById(R.id.course_name);
        }

    }
}
