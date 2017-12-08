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

import com.open.androidtvwidget.leanback.recycle.RecyclerViewTV;

import java.util.ArrayList;

import tv.dfyc.yckt.R;

/**
 * Created by admin on 2017-11-20.
 */

public class GeneralSelectClassAdapter extends RecyclerView.Adapter<GeneralSelectClassAdapter.ViewHolder> {

    private static final String TAG = "GeneralInfoAdapter";
    private Context mContext;
    private ArrayList<String> mTextList;

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

    public void setOnItemSelectListener(GeneralSelectClassAdapter.OnItemSelectListener listener){
        mSelectListener = listener;
    }

    public void setOnItemClickListener(GeneralSelectClassAdapter.OnItemClickListener listener){
        mListener = listener;
    }

    private GeneralSelectClassAdapter.OnItemClickListener mListener;
    private GeneralSelectClassAdapter.OnItemSelectListener mSelectListener;

    public void setSelectedBackground(View focused) {
        if(focused != null) {
            if(mListView == null)
                return;
            for(int i = 0; i < mListView.getChildCount(); ++i) {
                View item_view = mListView.getChildAt(i);
                if(item_view == null)
                    continue;
                GeneralSelectClassAdapter.ViewHolder holder = (GeneralSelectClassAdapter.ViewHolder) mListView.getChildViewHolder(item_view);
                if(holder != null && holder.mButton.equals(focused)) {
                    mSelectedPos = i;
                    holder.mButton.setBackground(ContextCompat.getDrawable(mContext, R.drawable.select_button_focus));
                    holder.mButton.setTextColor(Color.BLACK);
                }
            }
        }
    }

    public View setDefaultView(int pos) {
        View view = null;
        if(mListView == null)
            return view;
        View item_view = mListView.getChildAt(pos);
        if (item_view == null)
            return null;
        GeneralSelectClassAdapter.ViewHolder holder = (GeneralSelectClassAdapter.ViewHolder) mListView.getChildViewHolder(item_view);
        if (holder != null)
            view = holder.mButton;
        return view;
    }

    public View setSelectPos(int pos){
        View view = null;
        if(mListView == null)
            return null;
        for(int i = 0; i < mListView.getChildCount(); ++i) {
            View item_view = mListView.getChildAt(i);
            if(item_view == null)
                continue;
            GeneralSelectClassAdapter.ViewHolder holder = (GeneralSelectClassAdapter.ViewHolder) mListView.getChildViewHolder(item_view);
            if(holder != null) {
                if (i == pos){
                    holder.mButton.setBackground(ContextCompat.getDrawable(mContext, R.drawable.select_button_select));
                    holder.mButton.setTextColor(0x80ffffff);
                    return mListView.getChildAt(pos);
                }else {
                    holder.mButton.setBackground(ContextCompat.getDrawable(mContext, R.drawable.select_class_group));
                    holder.mButton.setTextColor(Color.WHITE);
                }

            }
        }
        return null;
    }


    public View getSelectedView() {
        View view = null;
        if(mListView == null)
            return view;
        for(int i = 0; i < mListView.getChildCount(); ++i) {
            View item_view = mListView.getChildAt(i);
            if(item_view == null)
                continue;
            GeneralSelectClassAdapter.ViewHolder holder = (GeneralSelectClassAdapter.ViewHolder) mListView.getChildViewHolder(item_view);
            if(holder != null && i == mSelectedPos) {
                view = holder.mButton;
                break;
            }
        }
        return view;
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        Button mButton;
        public ViewHolder(View view) {
            super(view);
            mButton = (Button) view.findViewById(R.id.top_one);
        }
    }

    public GeneralSelectClassAdapter(Context context, ArrayList<String> list,RecyclerViewTV tv_view) {
        mContext = context;
        mListView = tv_view;
        mTextList = list;
        mSelectedPos = -1;
        mFirstRun = true;
    }

    @Override
    public GeneralSelectClassAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_class_button_item, parent,false);
        GeneralSelectClassAdapter.ViewHolder holder = new GeneralSelectClassAdapter.ViewHolder(view);
        holder.setIsRecyclable(false);
        return holder;
    }

    @Override
    public void onBindViewHolder(final GeneralSelectClassAdapter.ViewHolder holder, final int position) {
        holder.mButton.setText(mTextList.get(position));

        if(mFirstRun) {
            mFirstRun = false;
            if(position == 0) {
                mSelectedPos = 0;
                holder.mButton.setBackground(ContextCompat.getDrawable(mContext, R.drawable.select_button_select));
                holder.mButton.setTextColor(0x80ffffff);
            }
        } else {
            if (position == mSelectedPos) {
                holder.mButton.setBackground(ContextCompat.getDrawable(mContext, R.drawable.select_button_focus));
                holder.mButton.setTextColor(Color.BLACK);
            }
        }

        if(mSelectListener != null) {
            holder.mButton.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        mSelectListener.onItemSelect(holder.mButton, position);
                        for(int i = 0; i < mListView.getChildCount(); ++i) {
                            View item_view = mListView.getChildAt(i);
                            GeneralSelectClassAdapter.ViewHolder holder = (GeneralSelectClassAdapter.ViewHolder) mListView.getChildViewHolder(item_view);
                            if(i == position) {
                                holder.mButton.setBackground(ContextCompat.getDrawable(mContext, R.drawable.select_button_focus));
                                holder.mButton.setTextColor(Color.BLACK);
                            } else {
                                if(mSelectedPos == i) {
                                    holder.mButton.setBackground(ContextCompat.getDrawable(mContext, R.drawable.select_class_group));
                                    holder.mButton.setTextColor(Color.WHITE);
                                }
                            }
                        }
                    } else {
                        for(int i = 0; i < mListView.getChildCount(); ++i) {
                            View item_view = mListView.getChildAt(i);
                            GeneralSelectClassAdapter.ViewHolder holder = (GeneralSelectClassAdapter.ViewHolder) mListView.getChildViewHolder(item_view);
                            if(mSelectedPos == i) {
                                holder.mButton.setBackground(ContextCompat.getDrawable(mContext, R.drawable.select_button_select));
                                holder.mButton.setTextColor(0x80ffffff);
                            } else {
                                holder.mButton.setBackground(ContextCompat.getDrawable(mContext, R.drawable.select_class_group));
                                holder.mButton.setTextColor(Color.WHITE);

                            }
                        }
                    }
                }
            });
        }

        if(mListener != null) {
            holder.mButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSelectedPos = position;
                    mListener.onItemClick(holder.mButton,position);
                    for(int i = 0; i < mListView.getChildCount(); ++i) {
                        View item_view = mListView.getChildAt(i);
                        GeneralSelectClassAdapter.ViewHolder holder = (GeneralSelectClassAdapter.ViewHolder) mListView.getChildViewHolder(item_view);
                        if(i == position) {
                            holder.mButton.setBackground(ContextCompat.getDrawable(mContext, R.drawable.select_button_focus));
                            holder.mButton.setTextColor(Color.BLACK);
                        } else {
                            holder.mButton.setBackground(ContextCompat.getDrawable(mContext, R.drawable.select_class_group));
                           // holder.mButton.setBackground(ContextCompat.getDrawable(mContext, R.color.transparent));
                            holder.mButton.setTextColor(0x80ffffff);
                        }
                    }
                }
            });
            holder.mButton.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mListener.onItemClick(holder.mButton,position);
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
