package tv.dfyc.yckt.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.open.androidtvwidget.leanback.recycle.RecyclerViewTV;

import tv.dfyc.yckt.R;

import java.util.List;

/**
 * Created by Administrator on 2017/7/13 0013.
 */

public class LessonListAdapter extends RecyclerView.Adapter<LessonListAdapter.ViewHolder> {
    private static final String TAG = "LessonListAdapter";
    private Context mContext;
    private List<String> mClassList;
    private RecyclerViewTV mListView;
    private int mSelectedPos = -1;
    private boolean mFirstRun = true;
    private boolean mClassClicked = false;


    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view,int position);
    }

    public interface OnItemSelectListener{
        void onItemSelect(View view,int position);
    }

    public void setOnItemSelectListener(OnItemSelectListener listener){
        mSelectListener = listener;
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    private OnItemClickListener mListener;
    private OnItemSelectListener mSelectListener;

    static class ViewHolder extends RecyclerView.ViewHolder {
        Button classButton;
        public ViewHolder(View view) {
            super(view);
            classButton = (Button) view.findViewById(R.id.primary_class_button);
        }
    }

    public LessonListAdapter(Context context, List<String> list, RecyclerViewTV tv_view) {
        mContext = context;
        mClassList = list;
        mListView = tv_view;
        mSelectedPos = -1;
        mFirstRun = true;
    }

    public void SetClassClicked(boolean class_click) {
        mClassClicked = class_click;
    }

    public View getSelectedView() {
        View view = null;
        if(mListView == null)
            return view;
        for(int i = 0; i < mListView.getChildCount(); ++i) {
            View item_view = mListView.getChildAt(i);
            if(item_view == null)
                continue;
            ViewHolder holder = (ViewHolder) mListView.getChildViewHolder(item_view);
            if(holder != null && i == mSelectedPos) {
                view = holder.classButton;
                break;
            }
        }
        if(view == null) {
            View item_view = mListView.getChildAt(0);
            if(item_view != null) {
                ViewHolder holder = (ViewHolder) mListView.getChildViewHolder(item_view);
                if(holder != null) {
                    view = holder.classButton;
                    mSelectedPos = 0;
                }
            }
        }
        return view;
    }

    public void setSelectedBackground(View focused) {
        if(focused != null) {
            if(mListView == null)
                return;
            for(int i = 0; i < mListView.getChildCount(); ++i) {
                View item_view = mListView.getChildAt(i);
                if(item_view == null)
                    continue;
                ViewHolder holder = (ViewHolder) mListView.getChildViewHolder(item_view);
                if(holder != null && holder.classButton.equals(focused)) {
                    holder.classButton.setBackground(ContextCompat.getDrawable(mContext, R.drawable.list_page_lesson_focus_shadow));
                    holder.classButton.setPadding((int)getDimension(R.dimen.w_25), 0,0,0);
                    mSelectedPos = i;
                } else {
                    holder.classButton.setBackground(ContextCompat.getDrawable(mContext, R.drawable.nav_selected));
                    holder.classButton.setPadding((int)getDimension(R.dimen.w_25), 0,0,0);
                }
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lesson, parent,false);
        ViewHolder holder = new ViewHolder(view);
        holder.setIsRecyclable(false);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if(mClassList.size() == 0)
            return;
        holder.classButton.setText(mClassList.get(position));
        holder.classButton.setFocusable(true);

        if(mFirstRun) {
            if(position == 0) {
                holder.classButton.setBackground(ContextCompat.getDrawable(mContext, R.drawable.list_page_lesson_focus_shadow));
                mSelectedPos = 0;
                mFirstRun = false;
            }
        } else {
            if(position == mSelectedPos)
                holder.classButton.setBackground(ContextCompat.getDrawable(mContext, R.drawable.list_page_lesson_focus_shadow));
        }
        holder.classButton.setPadding((int)getDimension(R.dimen.w_25), 0,0,0);

        if(mSelectListener != null) {
            holder.classButton.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        mSelectListener.onItemSelect(holder.classButton, position);
                        if(mListView == null)
                            return;
                        for(int i = 0; i < mListView.getChildCount(); ++i) {
                            View item_view = mListView.getChildAt(i);
                            if(item_view == null)
                                continue;
                            ViewHolder holder = (ViewHolder) mListView.getChildViewHolder(item_view);
                            if(holder == null)
                                continue;
                            holder.classButton.setBackground(ContextCompat.getDrawable(mContext, R.drawable.nav_selected));
                            if(i == mSelectedPos) {
                                holder.classButton.requestFocus();
                                mSelectedPos = -1;
                            }
                        }
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mClassList.size();
    }

    public float getDimension(int id) {
        return mContext.getResources().getDimension(id);
    }
}
