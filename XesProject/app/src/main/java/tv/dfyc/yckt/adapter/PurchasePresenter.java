package tv.dfyc.yckt.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.open.androidtvwidget.leanback.adapter.GeneralAdapter;
import com.open.androidtvwidget.leanback.mode.OpenPresenter;

import java.util.ArrayList;
import java.util.HashMap;

import tv.dfyc.yckt.R;

/**
 * Created by admin on 2017-11-16.
 */

public class PurchasePresenter extends OpenPresenter {

    private Context mContext;
    private GeneralAdapter mAdapter;
    private ArrayList<HashMap<String, Object>> listData;

    public interface OnItemSelectListener{
        void onItemSelect(View view,int position);
    }

    public void setOnItemSelectListener(PurchasePresenter.OnItemSelectListener listener){
        mSelectListener = listener;
    }



    private PurchasePresenter.OnItemSelectListener mSelectListener;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.purchase_record_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OpenPresenter.ViewHolder viewHolder, int position) {
        final PurchasePresenter.ViewHolder holder = (PurchasePresenter.ViewHolder) viewHolder;
        holder.mPurchase_record_name.setText(listData.get(position).get("purchase_name").toString());
        holder.mPurchase_validity_date.setText(listData.get(position).get("purchase_date").toString());
        holder.mPurchase_sum.setText(listData.get(position).get("purchase_sum").toString());
        holder.mPurchase_record_state.setText(listData.get(position).get("purchase_state").toString());
        if(mSelectListener != null) {
            holder.mPurchase_background.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        holder.mPurchase_record_name.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                        holder.mPurchase_record_name.setMarqueeRepeatLimit(-1);
                        holder.mPurchase_record_name.setSelected(true);
                        holder.mPurchase_record_name.setTextColor(Color.BLACK);
                        holder.mPurchase_validity_date.setTextColor(Color.BLACK);
                        holder.mPurchase_sum.setTextColor(Color.BLACK);
                        holder.mPurchase_record_state.setTextColor(Color.BLACK);
                        holder.mPurchase_background.setBackground(ContextCompat.getDrawable(mContext, R.drawable.purchase_list_focus_shape));
                    } else {
                        holder.mPurchase_record_name.setSelected(false);
                        holder.mPurchase_record_name.setTextColor(0xfff1f1f1);
                        holder.mPurchase_validity_date.setTextColor(0xfff1f1f1);
                        holder.mPurchase_sum.setTextColor(0xfff1f1f1);
                        holder.mPurchase_record_state.setTextColor(0xfff1f1f1);
                        holder.mPurchase_background.setBackground(ContextCompat.getDrawable(mContext, R.color.transparent));
                    }
                }
            });
        }
    }


    public PurchasePresenter(Context context, ArrayList<HashMap<String, Object>> data){
        this.mContext=context;
        this.listData=data;
    }

    @Override
    public void setAdapter(GeneralAdapter adapter) {
        this.mAdapter = adapter;
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    static class ViewHolder extends OpenPresenter.ViewHolder {
        TextView mPurchase_record_name;
        TextView mPurchase_validity_date;
        TextView mPurchase_sum;
        TextView mPurchase_record_state;
        RelativeLayout mPurchase_background;
        public ViewHolder(View view) {
            super(view);
            mPurchase_record_name = (TextView)view.findViewById(R.id.purchase_record_name);
            mPurchase_validity_date = (TextView)view.findViewById(R.id.purchase_validity_date);
            mPurchase_sum = (TextView)view.findViewById(R.id.purchase_sum);
            mPurchase_record_state = (TextView)view.findViewById(R.id.purchase__record_state);
            mPurchase_background = (RelativeLayout)view.findViewById(R.id.purchase_background);
        }
    }
}
