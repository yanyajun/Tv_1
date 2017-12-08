package tv.dfyc.yckt.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.open.androidtvwidget.leanback.adapter.GeneralAdapter;
import com.open.androidtvwidget.leanback.mode.OpenPresenter;

import java.util.List;

import tv.dfyc.yckt.R;
import tv.dfyc.yckt.beans.ProductListBean;

/**
 * Created by Administrator on 2017/11/22 0022.
 */

public class ProductPresenter extends OpenPresenter {
    private GeneralAdapter mAdapter;
    private Context context;
    private List<ProductListBean.GoodDetail> products;

    public ProductPresenter(Context context, List<ProductListBean.GoodDetail> products) {
        this.context = context;
        this.products = products;
    }

    @Override
    public void setAdapter(GeneralAdapter adapter) {
        this.mAdapter = adapter;
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public OpenPresenter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_productlist, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OpenPresenter.ViewHolder viewHolder, int position) {
        ProductViewHolder mHolder = (ProductViewHolder) viewHolder;
        Glide.with(context).load(products.get(position).getProduct_image()).placeholder(R.drawable.load_detail).into(mHolder.iv_productlist_cover);
        mHolder.tv_productlist_title.setText(products.get(position).getProduct_name());
        mHolder.tv_productlist_price.setText(products.get(position).getProduct_price().split("元")[0]);
        String notice = products.get(position).getProduct_notice();
        if (notice.contains("有效期至")) {
            StringBuilder sb = new StringBuilder(notice);
            sb.insert(8, "\n");
            mHolder.tv_productlist_orderstate.setText(sb.toString());
        } else {
            mHolder.tv_productlist_orderstate.setText(notice);
        }
    }
}
