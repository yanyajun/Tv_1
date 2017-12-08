package tv.dfyc.yckt.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.open.androidtvwidget.leanback.mode.OpenPresenter;

import tv.dfyc.yckt.R;

/**
 * Created by Administrator on 2017/11/22 0022.
 */

public class ProductViewHolder  extends OpenPresenter.ViewHolder {
    public TextView tv_productlist_title, tv_productlist_price, tv_productlist_orderstate;
    public ImageView iv_productlist_cover;
    public RelativeLayout rl_item_products;

    public ProductViewHolder(View itemView) {
        super(itemView);
        tv_productlist_title = (TextView) itemView.findViewById(R.id.tv_productlist_title);
        tv_productlist_price = (TextView) itemView.findViewById(R.id.tv_productlist_price);
        tv_productlist_orderstate = (TextView) itemView.findViewById(R.id.tv_productlist_orderstate);
        rl_item_products = (RelativeLayout) itemView.findViewById(R.id.rl_item_products);
        iv_productlist_cover = (ImageView) itemView.findViewById(R.id.iv_productlist_cover);
    }
}
