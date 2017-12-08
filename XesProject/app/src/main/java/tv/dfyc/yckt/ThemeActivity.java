package tv.dfyc.yckt;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.open.androidtvwidget.leanback.adapter.GeneralAdapter;
import com.open.androidtvwidget.leanback.mode.ItemHeaderPresenter;
import com.open.androidtvwidget.leanback.mode.ItemListPresenter;
import com.open.androidtvwidget.leanback.mode.ListRow;
import com.open.androidtvwidget.leanback.mode.ListRowPresenter;
import com.open.androidtvwidget.leanback.recycle.RecyclerViewTV;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import tv.dfyc.yckt.adapter.ThemeItemPresenter;
import tv.dfyc.yckt.beans.AddCollectionResult;
import tv.dfyc.yckt.beans.DeleteCollectionResult;
import tv.dfyc.yckt.beans.JsonGoodsListData;
import tv.dfyc.yckt.beans.ThemeBean;
import tv.dfyc.yckt.beans.ThemeList;
import tv.dfyc.yckt.beans.ThemeListItem;
import tv.dfyc.yckt.custom.RoundImageView;
import tv.dfyc.yckt.detail.BuyThemeActivity;
import tv.dfyc.yckt.detail.DetailActivity;
import tv.dfyc.yckt.network.CallBackUtil;
import tv.dfyc.yckt.network.GsonUtil;
import tv.dfyc.yckt.network.OkhttpUtil;
import tv.dfyc.yckt.util.GlobleData;
import tv.dfyc.yckt.util.PreferencesUtil;

/**
 * 专题页面
 */
public class ThemeActivity extends Activity implements View.OnFocusChangeListener {

    private TextView weekTextView;
    private TextView timeTextView;
    private TextView dateTextView;
    private Button ivbt_theme_order, ivbt_theme_collect;//订购，收藏按钮
    private RecyclerViewTV rv_theme_content;
    ListRowPresenter mListRowPresenter;
    List<ListRow> mListRows = new ArrayList<ListRow>();
    private ThemeBean themeBean;
    private List<ThemeList> themeLists = new ArrayList<>();
    private int specialid;
    private static final String TAG = "ThemeActivity";
    private Context mContext;
    private JsonGoodsListData jsonGoodsListData;
    private ThemeActivity.MsgReceiver mMsgReceiver;
    private boolean isCollected;
    private RoundImageView iv_theme_ad;
    private int rowLocation, flag = 0;
    private GeneralAdapter generalAdapter;

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
//                    Log.e("MSG_E", "刷新数据");
                    initView();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);
        mContext = ThemeActivity.this;
        specialid = getIntent().getIntExtra("specialid", -1);
        findView();
        getData();
        setTimeData();
        setListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mMsgReceiver);
    }


    /**
     * 设置监听
     */
    private void setListener() {
        //点击订阅按钮
        ivbt_theme_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到BuyThemeActivity页面
                String goods_endtime = themeBean.getData().getGoods_endtime();
                if (TextUtils.isEmpty(goods_endtime)) {
                    if (themeBean.getData().getIs_order() == 0) {
                        Intent intent = new Intent(mContext, BuyThemeActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("productData", jsonGoodsListData);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(mContext, goods_endtime, Toast.LENGTH_LONG).show();
                }
            }
        });

        ivbt_theme_collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCollected) {
                    Map<String, String> paramsMap = new HashMap<>();
                    paramsMap.put(GlobleData.HTTP_VERSION_KEY, GlobleData.HTTP_VERSION_VALUE);

                    String mac = PreferencesUtil.GetAuthorValue(mContext, GlobleData.PREFERENCE_AUTHOR_STBMAC, "");
                    paramsMap.put("Mac", mac);

                    String userID = PreferencesUtil.GetAuthorValue(mContext, GlobleData.PREFERENCE_AUTHOR_USER_ID, "");
                    paramsMap.put("UserId", userID);

                    paramsMap.put("historyid", themeBean.getData().getHistoryid() + "");

//                    Log.e("MSG_E","删除收藏参数"+paramsMap.toString());

                    OkhttpUtil.okHttpGet(GlobleData.HTTP_URL_DELCOLLECT, paramsMap, new CallBackUtil.CallBackString() {
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
                                    Toast.makeText(mContext, "删除收藏失败！", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            } catch (Exception e) {
                                Toast.makeText(mContext, "删除收藏失败！", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            DeleteCollectionResult result = GsonUtil.fromJson(response, DeleteCollectionResult.class);
//                            Log.e("MSG_E","删除收藏===="+result.toString());
                            if (result == null) {
                                Toast.makeText(mContext, "删除收藏失败！", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (result.getResult().equals("0")) {
//                            Log.e("MSG_E", result.getMessage());
                                ivbt_theme_collect.setText("收藏");
                                isCollected = false;
                                Toast.makeText(mContext, result.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    // 点击收藏按钮
//                Toast.makeText(ThemeActivity.this, "点击了收藏按钮", Toast.LENGTH_SHORT).show();
                    Map<String, String> paramsMap = new HashMap<>();
                    paramsMap.put(GlobleData.HTTP_VERSION_KEY, GlobleData.HTTP_VERSION_VALUE);

                    String mac = PreferencesUtil.GetAuthorValue(mContext, GlobleData.PREFERENCE_AUTHOR_STBMAC, "");
                    paramsMap.put("Mac", mac);

                    String userID = PreferencesUtil.GetAuthorValue(mContext, GlobleData.PREFERENCE_AUTHOR_USER_ID, "");
                    paramsMap.put("UserId", userID);

                    paramsMap.put("type", "2");
                    paramsMap.put("collectId", specialid + "");
//                    Log.e("MSG_E","收藏参数"+paramsMap.toString());

                    OkhttpUtil.okHttpGet(GlobleData.HTTP_URL_ADCOLLECT, paramsMap, new CallBackUtil.CallBackString() {
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
                                    Toast.makeText(mContext, "添加收藏失败！", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            } catch (Exception e) {
                                Toast.makeText(mContext, "添加收藏失败！", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            AddCollectionResult result = GsonUtil.fromJson(response, AddCollectionResult.class);
//                            Log.e("MSG_E","收藏===="+result.toString());
                            if (result == null) {
                                Toast.makeText(mContext, "添加收藏失败！", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (result.getResult().equals("0")) {
//                            Log.e("MSG_E", result.getMessage());
                                ivbt_theme_collect.setText("已收藏");
                                isCollected = true;
                                themeBean.getData().setHistoryid(result.getData().getHistoryid());
                                Toast.makeText(mContext, result.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
        ivbt_theme_order.setOnFocusChangeListener(this);
        ivbt_theme_collect.setOnFocusChangeListener(this);
        rv_theme_content.setOnChildViewHolderSelectedListener(new RecyclerViewTV.OnChildViewHolderSelectedListener() {
            @Override
            public void onChildViewHolderSelected(RecyclerView parent, RecyclerView.ViewHolder vh, int position) {
                rowLocation = position;
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_DOWN:   //向下键

                Log.e("MSG_E", "向下键---位置====" + rowLocation);
                if (rowLocation == 0 && flag == 0) {
                    flag = -1;
                    AnimatorSet animatorSet = new AnimatorSet();
                    ObjectAnimator ob1 = ObjectAnimator.ofFloat(iv_theme_ad, "scaleY",
                            1.0f, 0.0f);
                    ob1.setDuration(200);
                    ObjectAnimator ob2 = ObjectAnimator.ofFloat(iv_theme_ad, "translationY",
                            iv_theme_ad.getTranslationY(), -iv_theme_ad.getHeight() / 2);
                    ob2.setDuration(200);
                    ObjectAnimator ob3 = ObjectAnimator.ofFloat(rv_theme_content, "translationY",
                            rv_theme_content.getTranslationY(), rv_theme_content.getTranslationY() - (int) getDimension(R.dimen.h_360));
                    ob3.setDuration(200);
                    animatorSet.playTogether(ob1, ob2, ob3);
                    animatorSet.start();


                    ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) rv_theme_content.getLayoutParams();
                    params.height = rv_theme_content.getHeight() + (int) getDimension(R.dimen.h_360);
                    rv_theme_content.setLayoutParams(params);

                }
                break;
            case KeyEvent.KEYCODE_DPAD_UP:   //向上键
                Log.e("MSG_E", "向上键---位置====" + rowLocation);
                if (rowLocation == 0 && flag == -1) {
                    flag = 0;
                    AnimatorSet animatorSet = new AnimatorSet();
                    ObjectAnimator ob1 = ObjectAnimator.ofFloat(iv_theme_ad, "scaleY",
                            0.0f, 1.0f);
                    ob1.setDuration(200);
                    ObjectAnimator ob2 = ObjectAnimator.ofFloat(iv_theme_ad, "translationY",
                            iv_theme_ad.getTranslationY(), iv_theme_ad.getTranslationY() + iv_theme_ad.getHeight() / 2);
                    ob2.setDuration(200);
                    ObjectAnimator ob3 = ObjectAnimator.ofFloat(rv_theme_content, "translationY",
                            rv_theme_content.getTranslationY(), rv_theme_content.getTranslationY() + (int) getDimension(R.dimen.h_360));
                    ob3.setDuration(200);
                    animatorSet.playTogether(ob1, ob2, ob3);
                    animatorSet.start();
                    ob1.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) rv_theme_content.getLayoutParams();
                            params.height = rv_theme_content.getHeight() - (int) getDimension(R.dimen.h_360);
                            rv_theme_content.setLayoutParams(params);
                        }
                    });
                }
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 初始化控件
     */
    private void initView() {

        initRecycleView();
        Glide.with(this).load(themeBean.getData().getBackground_img()).into(iv_theme_ad);

        makeJsonGoodsListData();

        if (themeBean.getData().getIs_order() == 1) {
            //已订购
            ivbt_theme_order.setText("已订购");
        } else {
            ivbt_theme_order.setText("订购");
        }
        if (themeBean.getData().getIs_collection() == 1) {
            //已收藏
            ivbt_theme_collect.setText("已收藏");
            isCollected = true;

        } else {
            ivbt_theme_collect.setText("收藏");
            isCollected = false;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                rv_theme_content.scrollToPosition(0);
//                ivbt_theme_order.requestFocus();
            }
        }, 500);

    }

    private void makeJsonGoodsListData() {
        jsonGoodsListData = new JsonGoodsListData();
        jsonGoodsListData.setSeqNo(themeBean.getSeqNo() + "");
        jsonGoodsListData.setMessage(themeBean.getMessage());
        jsonGoodsListData.setResult(themeBean.getResult() + "");
        List<JsonGoodsListData.GoodDetail> lists = new ArrayList<>();
        JsonGoodsListData.GoodDetail goodDetail = new JsonGoodsListData.GoodDetail();
        goodDetail.setGoods_id(themeBean.getData().getGoods_id() + "");
        goodDetail.setName(themeBean.getData().getGoods_name());
        goodDetail.setDescription(themeBean.getData().getDescription());
        goodDetail.setPrice(themeBean.getData().getPrice());
        goodDetail.setRemarks(themeBean.getData().getRemarks());
        goodDetail.setThumb(themeBean.getData().getThumb());
        lists.add(goodDetail);
        jsonGoodsListData.setData(lists);
    }

    /**
     * 初始化recycleview
     */
    private void initRecycleView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rv_theme_content.setLayoutManager(layoutManager);
        rv_theme_content.setSelectedItemAtCentered(true); // 设置item在中间移动.

        // 添加数据
        for (int i = 0; i < themeLists.size(); i++) {
            String txt = themeLists.get(i).getType();
            // 添加一行的数据.
            ListRow listRow = new ListRow(txt); // 标题头.
            final List<ThemeListItem> goodsList = themeLists.get(i).getSublist();
            listRow.addAll(goodsList); // 添加列的数据.
            ThemeItemPresenter themeItemPresenter = new ThemeItemPresenter();
            listRow.setOpenPresenter(themeItemPresenter); // 设置列的item样式.
            // 添加一行的数据（标题头，列的数据)
            mListRows.add(listRow);

            listRow.getOpenPresenter().setOnItemClickListener(new RecyclerViewTV.OnItemClickListener() {
                @Override
                public void onItemClick(RecyclerViewTV parent, View itemView, int position) {
                    //item点击事件
                    Intent intent = new Intent(mContext, DetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("libraryId", goodsList.get(position).getLibrary_id());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });

        }
        mListRowPresenter = new ListRowPresenter(mListRows,
                new ItemHeaderPresenter(),
                new ItemListPresenter());
        mListRowPresenter.setDelayDefaultPos(0, 0, 300);
        generalAdapter = new GeneralAdapter(mListRowPresenter);

        rv_theme_content.setAdapter(generalAdapter);

    }

    /**
     * 设置时间控件
     */
    private void setTimeData() {
        mMsgReceiver = new ThemeActivity.MsgReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(GlobleData.MESSAGE_UPDATE_TIME);
        intentFilter.addAction(GlobleData.MESSAGE_ORDER_FINISH);
        registerReceiver(mMsgReceiver, intentFilter);
    }

    /**
     * 查找控件
     */
    private void findView() {
        iv_theme_ad = (RoundImageView) findViewById(R.id.iv_theme_ad);
        weekTextView = (TextView) findViewById(R.id.week_theme);
        timeTextView = (TextView) findViewById(R.id.time_theme);
        dateTextView = (TextView) findViewById(R.id.date_theme);
        ivbt_theme_order = (Button) findViewById(R.id.ivbt_theme_order);
        ivbt_theme_collect = (Button) findViewById(R.id.ivbt_theme_collect);
        rv_theme_content = (RecyclerViewTV) findViewById(R.id.rv_theme_content);
        ivbt_theme_order.setNextFocusLeftId(R.id.ivbt_theme_order);
        ivbt_theme_collect.setNextFocusRightId(R.id.ivbt_theme_collect);

    }

    /**
     * 获取数据
     */
    public void getData() {

        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put(GlobleData.HTTP_VERSION_KEY, GlobleData.HTTP_VERSION_VALUE);
        String userID = PreferencesUtil.GetAuthorValue(mContext, GlobleData.PREFERENCE_AUTHOR_USER_ID, "");
        paramsMap.put("UserId", userID);
        paramsMap.put("specialid", specialid + "");

//        Log.d("MSG_E", "专题页请求数据"+paramsMap.toString());
        OkhttpUtil.okHttpGet(GlobleData.HTTP_URL_SPECIAL, paramsMap, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                Log.d("MSG_E", "连接失败！");
            }

            @Override
            public void onResponse(String response) {
//                        Toast.makeText(ThemeActivity.this, "getMainPageData: " + response, Toast.LENGTH_SHORT).show();
                Log.d("MSG_E", response);
                try {
                    themeBean = GsonUtil.fromJson(response, ThemeBean.class);
                    if (themeBean.getResult() == 0) {
//                                Log.d("MSG_E", "数据返回成功!");
//                        Log.d("MSG_E",  themeBean.toString());
                        themeLists = themeBean.getData().getList();
                        Message message = Message.obtain();
                        message.what = 2;
                        mHandler.sendMessage(message);
                    } else {
//                                Log.d("MSG_E", "数据返回失败!");
                        Toast.makeText(ThemeActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
//                            Log.d("MSG_E", "数据返回失败!");
                    Toast.makeText(ThemeActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            ((TextView) v).setTextColor(getResources().getColor(R.color.black));
        } else {
            ((TextView) v).setTextColor(getResources().getColor(R.color.white));
        }
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

                        themeBean.getData().setIs_order(1);
                        ivbt_theme_order.setText("已订购");

                        final Dialog builder = new Dialog(ThemeActivity.this, R.style.dialogTransparent);
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

    public float getDimension(int id) {
        return getResources().getDimension(id);
    }
}
