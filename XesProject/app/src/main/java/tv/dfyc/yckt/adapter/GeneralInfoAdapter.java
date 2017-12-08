package tv.dfyc.yckt.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.open.androidtvwidget.leanback.recycle.RecyclerViewTV;

import java.util.ArrayList;
import java.util.List;

import tv.dfyc.yckt.R;

/**
 * Created by admin on 2017-11-15.
 */

public class GeneralInfoAdapter extends RecyclerView.Adapter<GeneralInfoAdapter.ViewHolder>{

    private static final String TAG = "GeneralInfoAdapter";
    private Context mContext;
    private ArrayList<String> mTextList;
    private ArrayList<Integer> mWhiteImageList;
    private ArrayList<Integer> mGrayImageList;
    private ArrayList<Integer> mBlackImageList;

    private RecyclerViewTV mListView;
    private int mSelectedPos = -1;
    private boolean mFirstRun = true;


    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view,int position);
    }

    public interface OnItemSelectListener{
        void onItemSelect(View view,int position);
    }

    public void setOnItemSelectListener(GeneralInfoAdapter.OnItemSelectListener listener){
        mSelectListener = listener;
    }

    public void setOnItemClickListener(GeneralInfoAdapter.OnItemClickListener listener){
        mListener = listener;
    }

    private GeneralInfoAdapter.OnItemClickListener mListener;
    private GeneralInfoAdapter.OnItemSelectListener mSelectListener;

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mImage;
        TextView mText;
        LinearLayout mInfo_layout;
        public ViewHolder(View view) {
            super(view);
            mImage = (ImageView) view.findViewById(R.id.info_image);
            mText = (TextView)view.findViewById(R.id.info_text);
            mInfo_layout = (LinearLayout)view.findViewById(R.id.info_layout);
        }
    }

    public GeneralInfoAdapter(Context context, ArrayList<String> list, ArrayList<Integer> whiteImageList,ArrayList<Integer> grayImageList,ArrayList<Integer> blackImageList,RecyclerViewTV tv_view) {
        mContext = context;
        mTextList = list;
        mWhiteImageList =  whiteImageList;
        mGrayImageList = grayImageList;
        mBlackImageList = blackImageList;
        mListView = tv_view;
        mSelectedPos = -1;
        mFirstRun = true;
    }

    public View getSelectedView() {
        View view = null;
        if(mListView == null)
            return view;
        for(int i = 0; i < mListView.getChildCount(); ++i) {
            View item_view = mListView.getChildAt(i);
            if(item_view == null)
                continue;
            GeneralInfoAdapter.ViewHolder holder = (GeneralInfoAdapter.ViewHolder) mListView.getChildViewHolder(item_view);
            if(holder != null && i == mSelectedPos) {
                view = holder.mInfo_layout;
                break;
            }
        }
        return view;
    }

    public void setSelectView(int pos) {
        mSelectedPos = pos;
        View view = null;
        if(mListView == null)
            return;
        View item_view = mListView.getChildAt(pos);
        if(item_view == null)
            return;
        GeneralInfoAdapter.ViewHolder holder = (GeneralInfoAdapter.ViewHolder) mListView.getChildViewHolder(item_view);

        holder.mText.setTextColor(Color.BLACK);
        holder.mImage.setBackgroundResource(mBlackImageList.get(mSelectedPos));
        holder.mInfo_layout.setBackground(ContextCompat.getDrawable(mContext, R.drawable.personal_select_shadow));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.info_item, parent,false);
       ViewHolder holder = new ViewHolder(view);
        holder.setIsRecyclable(false);
        return holder;
    }

    public void setSelectedBackground(View focused) {
        if(focused != null) {
            if(mListView == null)
                return;
            for(int i = 0; i < mListView.getChildCount(); ++i) {
                View item_view = mListView.getChildAt(i);
                if(item_view == null)
                    continue;
                GeneralInfoAdapter.ViewHolder holder = (GeneralInfoAdapter.ViewHolder) mListView.getChildViewHolder(item_view);
                if(holder != null && holder.mInfo_layout.equals(focused)) {
                    mSelectedPos = i;
                    holder.mText.setTextColor(Color.BLACK);
                    holder.mImage.setBackgroundResource(mBlackImageList.get(mSelectedPos));
                    holder.mInfo_layout.setBackground(ContextCompat.getDrawable(mContext, R.drawable.personal_select_shadow));
                }
//                else
//                    holder.mInfo_layout.setBackground(ContextCompat.getDrawable(mContext, R.drawable.nav_selected));
            }
        }
    }

    @Override
    public void onBindViewHolder(final GeneralInfoAdapter.ViewHolder holder, final int position) {
        holder.mText.setText(mTextList.get(position));
        holder.mImage.setBackgroundResource(mGrayImageList.get(position));
        holder.mInfo_layout.setFocusable(true);

        if(mFirstRun) {
            mFirstRun = false;
            if(position == 0) {
                mSelectedPos = 0;
                holder.mInfo_layout.setBackground(ContextCompat.getDrawable(mContext, R.drawable.personal_page_focus_shadow));
                holder.mText.setTextColor(0x80ffffff);
                holder.mImage.setBackgroundResource(mGrayImageList.get(mSelectedPos));
            }
        } else {
            if (position == mSelectedPos) {
                holder.mInfo_layout.requestFocus();
                holder.mText.setTextColor(Color.BLACK);
                holder.mImage.setBackgroundResource(mBlackImageList.get(mSelectedPos));
                holder.mInfo_layout.setBackground(ContextCompat.getDrawable(mContext, R.drawable.personal_select_shadow));
            }
        }

        if(mSelectListener != null) {
            holder.mInfo_layout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        mSelectListener.onItemSelect(holder.mInfo_layout, position);
                        for(int i = 0; i < mListView.getChildCount(); ++i) {
                            View item_view = mListView.getChildAt(i);
                            if(item_view == null)
                                continue;
                            GeneralInfoAdapter.ViewHolder holder = (GeneralInfoAdapter.ViewHolder) mListView.getChildViewHolder(item_view);
                            if(holder == null)
                                continue;
                            if(i == position) {
                                holder.mInfo_layout.setBackground(ContextCompat.getDrawable(mContext, R.drawable.personal_select_shadow));
                                holder.mText.setTextColor(Color.BLACK);
                                holder.mImage.setBackgroundResource(mBlackImageList.get(i));
                            }
                            else {
                                if(mSelectedPos == i) {
                                    holder.mInfo_layout.setBackground(ContextCompat.getDrawable(mContext, R.color.transparent));
                                    holder.mText.setTextColor(0x80ffffff);
                                    holder.mImage.setBackgroundResource(mGrayImageList.get(mSelectedPos));
                                }
                            }
                        }
                    }
                    else {
                        for(int i = 0; i < mListView.getChildCount(); ++i) {
                            View item_view = mListView.getChildAt(i);
                            GeneralInfoAdapter.ViewHolder holder = (GeneralInfoAdapter.ViewHolder) mListView.getChildViewHolder(item_view);
                            if(mSelectedPos == i) {
                                holder.mInfo_layout.setBackground(ContextCompat.getDrawable(mContext, R.drawable.personal_page_focus_shadow));
                                holder.mText.setTextColor(Color.WHITE);
                                holder.mImage.setBackgroundResource(mWhiteImageList.get(mSelectedPos));
                            } else {
                                holder.mInfo_layout.setBackground(ContextCompat.getDrawable(mContext, R.color.transparent));
                                holder.mText.setTextColor(0x80ffffff);
                                holder.mImage.setBackgroundResource(mGrayImageList.get(i));
                            }
                        }
                    }
                }
            });
        }

        if(mListener != null) {
            holder.mInfo_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSelectedPos = position;
                    mListener.onItemClick(holder.mInfo_layout,position);
                    for(int i = 0; i < mListView.getChildCount(); ++i) {
                        View item_view = mListView.getChildAt(i);
                        GeneralInfoAdapter.ViewHolder holder = (GeneralInfoAdapter.ViewHolder) mListView.getChildViewHolder(item_view);
                        if(i == position) {
                            holder.mInfo_layout.setBackground(ContextCompat.getDrawable(mContext, R.drawable.personal_select_shadow));
                            holder.mText.setTextColor(Color.BLACK);
                            holder.mImage.setBackgroundResource(mBlackImageList.get(position));
                        } else {
                            holder.mInfo_layout.setBackground(ContextCompat.getDrawable(mContext, R.color.transparent));
                            holder.mText.setTextColor(0x80ffffff);
                            holder.mImage.setBackgroundResource(mGrayImageList.get(i));
                        }
                    }
                }
            });
            holder.mInfo_layout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mListener.onItemClick(holder.mInfo_layout,position);
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mTextList.size();
    }
}
