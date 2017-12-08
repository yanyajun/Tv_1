package tv.dfyc.yckt.detail;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.open.androidtvwidget.bridge.RecyclerViewBridge;
import com.open.androidtvwidget.leanback.adapter.GeneralAdapter;
import com.open.androidtvwidget.leanback.mode.ListRowPresenter;
import com.open.androidtvwidget.leanback.mode.OpenPresenter;
import com.open.androidtvwidget.leanback.recycle.NativeGridLayoutManagerTV;
import com.open.androidtvwidget.leanback.recycle.RecyclerViewTV;
import com.open.androidtvwidget.view.MainUpView;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import tv.dfyc.yckt.R;
import tv.dfyc.yckt.adapter.ProductPresenter;
import tv.dfyc.yckt.beans.JsonCreateOrderData;
import tv.dfyc.yckt.beans.JsonGoodsListData;
import tv.dfyc.yckt.beans.ProductListBean;
import tv.dfyc.yckt.custom.RecyclerItemSpacing;
import tv.dfyc.yckt.custom.RecyclerItemSpacing_producList;
import tv.dfyc.yckt.network.CallBackUtil;
import tv.dfyc.yckt.network.GsonUtil;
import tv.dfyc.yckt.network.OkhttpUtil;
import tv.dfyc.yckt.util.GlobleData;
import tv.dfyc.yckt.util.PreferencesUtil;

/**
 * 订购（产品列表页面）
 */
public class ProductListActivity extends Activity implements RecyclerViewTV.OnItemListener {

    private TextView weekTextView;
    private TextView timeTextView;
    private TextView dateTextView;
    private RecyclerViewTV gv_product_list;
    private LinearLayout scroll_productlist_bg;//滚动条背景
    private View scroll_productlist_bar;//滚动条
    private float mScrollBarDistance;//滚动条比例
    private TextView page_productlist_text;//item总数显示控件
    private int itemCount;//产品行数
    private MainUpView product_list_upview;//焦点
    private RecyclerViewBridge bridget;
    private View oldView;
    private ProductListBean productListBean;
    private static final String TAG = "ProductListActivity";
    private Context mContext;
    private JsonCreateOrderData mOrderData;
    private ProductListActivity.MsgReceiver mMsgReceiver;
    private int location = 1;//滚动条当前所在位置
    private int productpos = -1;//所购买的商品在
    private ProductPresenter mRecyclerViewPresenter;
    private GeneralAdapter mDetailListAdapter;
    private View lastView;

    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    String sTimeData = msg.getData().getString("time");
                    dateTextView.setText(sTimeData.split(" ")[0]);
                    timeTextView.setText(sTimeData.split(" ")[1]);
                    weekTextView.setText(sTimeData.split(" ")[2]);
                    break;
                case 2:
                    initView();
                    break;
                case 3:
//                    mDetailListAdapter.notifyDataSetChanged();
                    TextView ts = (TextView) lastView.findViewById(R.id.tv_productlist_orderstate);
                    String notice = productListBean.getData().get(productpos).getProduct_notice();
                    if (notice.contains("有效期至")) {
                        StringBuilder sb = new StringBuilder(notice);
                        sb.insert(8, "\n");
                        ts.setText(sb.toString());
                    } else {
                        ts.setText(notice);
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        mContext = ProductListActivity.this;
        getData();
        findView();
        setTimeData();
        setListener();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mMsgReceiver);
    }

    /**
     * 排除 Leanback demo的RecyclerView.
     */
    private boolean isListRowPresenter() {
        GeneralAdapter generalAdapter = (GeneralAdapter) gv_product_list.getAdapter();
        OpenPresenter openPresenter = generalAdapter.getPresenter();
        return (openPresenter instanceof ListRowPresenter);
    }

    /**
     * 设置监听
     */
    private void setListener() {
        gv_product_list.setOnItemClickListener(new RecyclerViewTV.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerViewTV parent, View itemView, int position) {
                lastView = itemView;

                JsonGoodsListData.GoodDetail product = new JsonGoodsListData.GoodDetail();
                product.setThumb(productListBean.getData().get(position).getProduct_image());
                product.setPrice(productListBean.getData().get(position).getProduct_price());
                product.setDescription(productListBean.getData().get(position).getProduct_notice());
                product.setName(productListBean.getData().get(position).getProduct_name());
                product.setGoods_id(productListBean.getData().get(position).getGoods_id() + "");
                String goods_endtime = productListBean.getData().get(position).getGoods_endtime();
                if (TextUtils.isEmpty(productListBean.getData().get(position).getGoods_endtime())) {
                    //未下架
                    if (productListBean.getData().get(position).getProduct_notice().contains("已订购")) {
                        //重复订购
                        final Dialog builder = new Dialog(ProductListActivity.this, R.style.dialogTransparent);
                        RelativeLayout orderSuccDialog = (RelativeLayout) getLayoutInflater().inflate(R.layout.pop_dialog_reorder, null);
                        final Button mOk = (Button) orderSuccDialog.findViewById(R.id.popOK);

                        mOk.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                builder.dismiss();
                            }
                        });
                        builder.setContentView(orderSuccDialog);
                        builder.show();
                        builder.dismiss();
                        builder.setCancelable(true);
                        builder.show();
                        mOk.requestFocus();
                    } else {
                        //订购
                        productpos = position;
                        getOrder(product);
                    }
                } else {
                    //已下架
                    Toast.makeText(mContext, goods_endtime, Toast.LENGTH_LONG).show();
                }

            }
        });
        gv_product_list.setOnItemListener(this);
    }

    /**
     * 获取订单
     *
     * @param product
     */
    private void getOrder(final JsonGoodsListData.GoodDetail product) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put(GlobleData.HTTP_VERSION_KEY, GlobleData.HTTP_VERSION_VALUE);

        paramsMap.put("productType", product.getGoods_id());

        String userID = PreferencesUtil.GetAuthorValue(mContext, GlobleData.PREFERENCE_AUTHOR_USER_ID, "");
        paramsMap.put("UserId", userID);

        OkhttpUtil.okHttpGet(GlobleData.HTTP_URL_CREATEORDER, paramsMap, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                Toast.makeText(mContext, "连接失败！", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);
                try {
                    JSONObject jsonObj = new JSONObject(response);
                    if (jsonObj == null) {
                        Toast.makeText(mContext, "生成订单失败！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (Exception e) {
                    Toast.makeText(mContext, "生成订单失败！", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    mOrderData = GsonUtil.fromJson(response, JsonCreateOrderData.class);
                    if (mOrderData == null)
                        return;
                    if (mOrderData.getResult().equals("0")) {
                        String sOrderUrl = mOrderData.getData();
                        Intent intent = new Intent(mContext, OrderActivity.class);
                        intent.putExtra("product_good_info", product);
                        intent.putExtra("product_order_url", sOrderUrl);
                        startActivity(intent);
                    } else {
                        Toast.makeText(mContext, mOrderData.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(TAG, "json exception");
                }
            }
        });
    }

    /**
     * 设置控件
     */
    private void initView() {

        NativeGridLayoutManagerTV layoutManager = new NativeGridLayoutManagerTV(this, 2);
        layoutManager.setOrientation(GridLayoutManager.VERTICAL);
        gv_product_list.setLayoutManager(layoutManager);
        gv_product_list.setFocusable(false);
//        gv_product_list.setSelectedItemAtCentered(true); // 设置item在中间移动(晃动)
        gv_product_list.setSelectedItemOffset(0, (int) getDimension(R.dimen.h_200));//解决晃动问题

        mRecyclerViewPresenter = new ProductPresenter(this, productListBean.getData());
        mDetailListAdapter = new GeneralAdapter(mRecyclerViewPresenter);
        gv_product_list.setAdapter(mDetailListAdapter);
        gv_product_list.addItemDecoration(new RecyclerItemSpacing_producList(
                (int) getDimension(R.dimen.w_19), (int) getDimension(R.dimen.w_10), (int) getDimension(R.dimen.w_19), (int) getDimension(R.dimen.w_10)));

        layoutManager.setUpKeyListener(new NativeGridLayoutManagerTV.OnUpKeyListener() {
            @Override
            public void onUpKey() {
                scrollBarUp();
            }
        });

        layoutManager.setDownKeyListener(new NativeGridLayoutManagerTV.OnDownKeyListener() {
            @Override
            public void onDownKey() {
                scrollBarDown();
            }
        });

        product_list_upview.setEffectBridge(new RecyclerViewBridge());
        bridget = (RecyclerViewBridge) product_list_upview.getEffectBridge();
        // 设置移动边框的图片.
        bridget.setUpRectResource(R.drawable.yellow_white_border);
        gv_product_list.setDelayDefaultSelect(0, 300);
        setScroll();

    }

    @Override
    public void onItemPreSelected(RecyclerViewTV parent, View itemView, int position) {
        if (!isListRowPresenter()) {
            bridget.setVisibleWidget(true);
        }
        bridget.setUnFocusView(oldView);
    }

    @Override
    public void onItemSelected(RecyclerViewTV parent, View itemView, int position) {
        if (!isListRowPresenter()) {
            if (bridget.isVisibleWidget()) {
                bridget.setVisibleWidget(false);
            }
            bridget.setFocusView(itemView, 1.1f);
            oldView = itemView;
        }
    }

    @Override
    public void onReviseFocusFollow(RecyclerViewTV parent, View itemView, int position) {
        if (!isListRowPresenter()) {
            if (bridget.isVisibleWidget()) {
                bridget.setVisibleWidget(false);
            }
            bridget.setFocusView(itemView, 1.1f);
            oldView = itemView;
        }
    }

    /**
     * 设置滚动条
     */
    private void setScroll() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                ViewGroup.LayoutParams para1;
                para1 = scroll_productlist_bar.getLayoutParams();
                itemCount = (productListBean.getData().size() % 2 == 0) ? (productListBean.getData().size() / 2) : (productListBean.getData().size() / 2 + 1);
                mScrollBarDistance = scroll_productlist_bg.getHeight() / itemCount;
                para1.height = scroll_productlist_bg.getHeight() / itemCount;

                scroll_productlist_bar.setLayoutParams(para1);
                page_productlist_text.setText("共" + productListBean.getData().size() + "项");
            }
        }, 200);
    }

    /**
     * 设置时间控件
     */
    private void setTimeData() {

        mMsgReceiver = new ProductListActivity.MsgReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(GlobleData.MESSAGE_UPDATE_TIME);
        intentFilter.addAction(GlobleData.MESSAGE_ORDER_FINISH);
        registerReceiver(mMsgReceiver, intentFilter);

    }

    /**
     * 查找控件
     */
    private void findView() {
        weekTextView = (TextView) findViewById(R.id.week);
        timeTextView = (TextView) findViewById(R.id.time);
        dateTextView = (TextView) findViewById(R.id.date);
        gv_product_list = (RecyclerViewTV) findViewById(R.id.gv_product_list);
        product_list_upview = (MainUpView) findViewById(R.id.product_list_upview);
        page_productlist_text = (TextView) findViewById(R.id.page_productlist_text);
        scroll_productlist_bg = (LinearLayout) findViewById(R.id.scroll_productlist_bg);
        scroll_productlist_bar = findViewById(R.id.scroll_productlist_bar);
    }

    /**
     * 向下滚动
     */
    private void scrollBarDown() {
        location++;
        int iTranslation = (int) (scroll_productlist_bar.getTranslationY() + mScrollBarDistance);
        if (iTranslation > scroll_productlist_bg.getHeight() - scroll_productlist_bar.getHeight()) {
            iTranslation = 0;
        }
        if (location == itemCount) {
            iTranslation = scroll_productlist_bg.getHeight() - (int) mScrollBarDistance;
        }
        scroll_productlist_bar.setTranslationY(iTranslation);
    }

    /**
     * 向上滚动
     */
    private void scrollBarUp() {
        location--;
        int iTranslation = (int) (scroll_productlist_bar.getTranslationY() - mScrollBarDistance);
        if (iTranslation < 0) {
            iTranslation = 0;
        }
        if (location == 1) {
            iTranslation = 0;
        }
        scroll_productlist_bar.setTranslationY(iTranslation);
    }

    public void getData() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                Map<String, String> paramsMap = new HashMap<>();
                paramsMap.put(GlobleData.HTTP_VERSION_KEY, GlobleData.HTTP_VERSION_VALUE);

                String userID = PreferencesUtil.GetAuthorValue(mContext, GlobleData.PREFERENCE_AUTHOR_USER_ID, "");
                paramsMap.put("UserId", userID);

                OkhttpUtil.okHttpGet(GlobleData.HTTP_URL_ALLGOODS, paramsMap, new CallBackUtil.CallBackString() {
                    @Override
                    public void onFailure(Call call, Exception e) {
                        Log.d("MSG_E", "连接失败！");
                    }

                    @Override
                    public void onResponse(String response) {
//                        Toast.makeText(ProductListActivity.this, "getMainPageData: " + response, Toast.LENGTH_SHORT).show();
//                        Log.d("MSG_E", response);
                        try {
                            productListBean = GsonUtil.fromJson(response, ProductListBean.class);
                            if (productListBean.getResult().equals("0")) {
//                                Log.d("MSG_E", "数据返回成功!");
//                                Log.d("MSG_E",  produckListBean.toString());
                                if (productpos == -1) {
                                    Message message = Message.obtain();
                                    message.what = 2;
                                    mHandler.sendMessage(message);
                                } else {
                                    Message message = Message.obtain();
                                    message.what = 3;
                                    mHandler.sendMessage(message);
                                }

                            } else {
//                                Log.d("MSG_E", "数据返回失败!");
                                Toast.makeText(ProductListActivity.this, "获取产品列表数据失败", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
//                            Log.d("MSG_E", "数据返回失败!");
                            Toast.makeText(ProductListActivity.this, "获取产品列表数据失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).start();

    }

    public float getDimension(int id) {
        return getResources().getDimension(id);
    }

    public class MsgReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (GlobleData.MESSAGE_UPDATE_TIME.equals(intent.getAction())) {
                    String sTimeData = intent.getStringExtra("time");
                    dateTextView.setText(sTimeData.split(" ")[0]);
                    timeTextView.setText(sTimeData.split(" ")[1]);
                    weekTextView.setText(sTimeData.split(" ")[2]);
                }
                if (GlobleData.MESSAGE_ORDER_FINISH.equals(intent.getAction())) {
                    int iResult = intent.getIntExtra("order_finish", 0);
                    String sGoodName = intent.getStringExtra("good_name");
                    if (iResult == 1 && sGoodName != null && !sGoodName.isEmpty()) {
                        getData();

                        final Dialog builder = new Dialog(ProductListActivity.this, R.style.dialogTransparent);
                        LinearLayout orderSuccDialog = (LinearLayout) getLayoutInflater().inflate(R.layout.pop_dialog_order_success, null);
                        final Button mOk = (Button) orderSuccDialog.findViewById(R.id.popOK);
                        TextView mText = (TextView) orderSuccDialog.findViewById(R.id.popText);
                        mText.setText("您成功购买" + sGoodName);

                        mOk.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                builder.dismiss();
                            }
                        });
                        builder.setContentView(orderSuccDialog);
                        builder.show();
                        builder.dismiss();
                        builder.setCancelable(true);
                        builder.show();
                        mOk.requestFocus();
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "" + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
