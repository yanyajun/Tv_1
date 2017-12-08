package tv.dfyc.yckt.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import tv.dfyc.yckt.R;

/**
 * Created by admin on 2017-9-25.
 */

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.ViewHolder>{
    private Context mContext;
    private ArrayList<String> mCheckText;

//    private String[] mCheckText = {"增加名师指导","增加课后习题","增加考试测验","增加课程更新","丰富内容类型","减少操作步骤","增加专题数量"
//            ,"提升视频质量","增加微信客服"};

    //private Boolean[] selectArr={false,false,false,false,false,false,false,false,false};
    private ArrayList<Boolean> selectArr = new ArrayList<>();

    public FeedbackAdapter(Context c,ArrayList<String> list) {
        mContext = c;
        mCheckText = list;
    }

    public void setSelectArr(int size){
        if (size != 0){
            for (int i = 0;i<size;i++){
                selectArr.add(false);
            }
        }

    }
    @Override
    public FeedbackAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.feedback_recycler_item, parent, false);
        FeedbackAdapter.ViewHolder holder = new FeedbackAdapter.ViewHolder(v);
        holder.mCheckBox = (CheckBox) v.findViewById(R.id.checkBox);
        holder.mCheckBox_text = (TextView)v.findViewById(R.id.checkBox_text);
        holder.mCheckBox_relativelayout = (RelativeLayout) v.findViewById(R.id.checkBox_relativelayout);
        holder.mCheckBox_select_image = (RelativeLayout)v.findViewById(R.id.checkBox_select_image);
        return holder;
    }

    @Override
    public void onBindViewHolder(FeedbackAdapter.ViewHolder holder, final int position) {
        final FeedbackAdapter.ViewHolder feedback_holder = holder;
        feedback_holder.mCheckBox_text.setText(mCheckText.get(position));
        if (position == 0)
            holder.mCheckBox_relativelayout.requestFocus();
        feedback_holder.mCheckBox_relativelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (feedback_holder.mCheckBox.isChecked()){
                    selectArr.set(position,false);
                    feedback_holder.mCheckBox_select_image.setBackground(ContextCompat.getDrawable(mContext, R.color.transparent));
                    feedback_holder.mCheckBox.setChecked(false);
                }else {
                    selectArr.set(position,true);
                    feedback_holder.mCheckBox_select_image.setBackground(ContextCompat.getDrawable(mContext, R.drawable.ok_select));
                    feedback_holder.mCheckBox.setChecked(true);
                }
            }
        });
    }

    public String getSelectPosition() {
        String selectString = "";
        if (selectArr.size() != 0){
            for (int i= 0;i < selectArr.size();i++){
                if (selectArr.get(i) == true){
                    if (selectString.equals("")){
                        selectString +=(i+1);
                    }else {
                        selectString +=(","+(i+1));
                    }

                }
            }
        }
        return selectString;
    }




    @Override
    public int getItemCount() {
        return mCheckText.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox mCheckBox;
        TextView mCheckBox_text;
        RelativeLayout mCheckBox_relativelayout;
        RelativeLayout mCheckBox_select_image;
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
