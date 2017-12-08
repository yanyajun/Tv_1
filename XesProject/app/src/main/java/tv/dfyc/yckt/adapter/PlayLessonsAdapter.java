package tv.dfyc.yckt.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.open.androidtvwidget.leanback.recycle.RecyclerViewTV;

import tv.dfyc.yckt.R;
import tv.dfyc.yckt.beans.JsonVideoDetailData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/27 0027.
 */

public class PlayLessonsAdapter extends RecyclerView.Adapter<PlayLessonsAdapter.ViewHolder>{
    private Context mContext;
    private List<JsonVideoDetailData.detailData.videoDetail> mLessonList;
    private RecyclerViewTV mListView;
    private int mSelectedPos = -1;
    private String mPlayName;
    private boolean mMultiFlag = false;
    private boolean mFirstRun = true;

    public void setMultiFlag(boolean muilt) {
        mMultiFlag = muilt;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view,int position);
    }

    public interface OnItemSelectListener{
        void onItemSelect(View view,int position);
    }

    public void setOnItemSelectListener(PlayLessonsAdapter.OnItemSelectListener listener){
        mSelectListener = listener;
    }

    public void setOnItemClickListener(PlayLessonsAdapter.OnItemClickListener listener){
        mListener = listener;
    }

    private PlayLessonsAdapter.OnItemClickListener mListener;
    private PlayLessonsAdapter.OnItemSelectListener mSelectListener;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView lessonName;
        RelativeLayout lessonLayout;
        public ViewHolder(View view) {
            super(view);
            lessonName = (TextView) view.findViewById(R.id.play_page_one_lesson_name);
            lessonLayout = (RelativeLayout) view.findViewById(R.id.play_page_one_lesson_layout);
        }
    }

    public PlayLessonsAdapter(Context context, RecyclerViewTV listview) {
        mContext = context;
        mLessonList = new ArrayList<>();
        mListView = listview;
        mFirstRun = true;
        mMultiFlag = false;
        mSelectedPos = -1;
    }

    public void ClearData() {
        mLessonList.clear();
        notifyDataSetChanged();
    }

    public int getSelectPos() {
        return mSelectedPos;
    }

    public void UpdateData(String cur_name, int cur_postion, List<JsonVideoDetailData.detailData.videoDetail> list) {
        mFirstRun = true;
        mLessonList = list;
        notifyDataSetChanged();
        mPlayName = cur_name;
        mSelectedPos = cur_postion;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_play_page_lessons, parent,false);
        ViewHolder holder = new ViewHolder(view);
        holder.setIsRecyclable(false);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.lessonLayout.setFocusable(true);
        if(mMultiFlag) {
            holder.lessonName.setText(mLessonList.get(position % mLessonList.size()).getVideo_name());
            holder.lessonLayout.setTag(mLessonList.get(position % mLessonList.size()));

            if((position % mLessonList.size()) == (mSelectedPos % mLessonList.size())) {
                mSelectedPos = position;
                holder.lessonLayout.setSelected(true);
                if(mFirstRun) {
                    holder.lessonLayout.requestFocus();
                    mFirstRun = false;
                }
                holder.lessonLayout.setBackground(ContextCompat.getDrawable(mContext, R.drawable.play_lessons_select_shape));
            }
        } else {
            holder.lessonName.setText(mLessonList.get(position).getVideo_name());
            holder.lessonLayout.setTag(mLessonList.get(position));

            if(position == mSelectedPos) {
                mSelectedPos = position;
                holder.lessonLayout.setSelected(true);
                if(mFirstRun) {
                    holder.lessonLayout.requestFocus();
                    mFirstRun = false;
                }
                holder.lessonLayout.setBackground(ContextCompat.getDrawable(mContext, R.drawable.play_lessons_select_shape));
            }
        }

        if(mSelectListener != null) {
            holder.lessonLayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    holder.lessonLayout.setSelected(hasFocus);
                    if (hasFocus) {
                        mSelectListener.onItemSelect(holder.lessonLayout, position);
                    }
                }
            });
        }

        if(mListener != null) {
            holder.lessonLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSelectedPos = position;
                    mListener.onItemClick(holder.lessonLayout,position);
                }
            });
            holder.lessonLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mListener.onItemClick(holder.lessonLayout,position);
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if(mMultiFlag)
            return mLessonList.size() * 2000;
        else
            return mLessonList.size();
    }
}
