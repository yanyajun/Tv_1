package tv.dfyc.yckt.adapter;

import com.open.androidtvwidget.leanback.mode.OpenPresenter;
import com.open.androidtvwidget.view.LabelView;

import tv.dfyc.yckt.R;
import tv.dfyc.yckt.custom.RoundImageView;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailViewHolder extends OpenPresenter.ViewHolder {

	public RoundImageView detailImage;
	public TextView detailName;
	public TextView detailTimes;
	public ImageView detailCharge;
	
	public DetailViewHolder(View itemView) {
		super(itemView);
		detailImage = (RoundImageView) itemView.findViewById(R.id.primary_lesson_detail_image);
		detailName = (TextView) itemView.findViewById(R.id.primary_detail_name);
		detailTimes = (TextView) itemView.findViewById(R.id.primary_detail_times);
		detailCharge = (ImageView) itemView.findViewById(R.id.primary_lesson_detail_charge);
	}

}
