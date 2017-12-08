package tv.dfyc.yckt.info.personal;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.open.androidtvwidget.bridge.RecyclerViewBridge;
import com.open.androidtvwidget.leanback.adapter.GeneralAdapter;
import com.open.androidtvwidget.leanback.mode.ListRowPresenter;
import com.open.androidtvwidget.leanback.mode.OpenPresenter;
import com.open.androidtvwidget.leanback.recycle.GridLayoutManagerTV;
import com.open.androidtvwidget.leanback.recycle.RecyclerViewTV;
import com.open.androidtvwidget.view.MainUpView;

import tv.dfyc.yckt.R;
import tv.dfyc.yckt.adapter.GeneralInfoAdapter;
import tv.dfyc.yckt.adapter.PurchasePresenter;
import tv.dfyc.yckt.adapter.RecordPresenter;
import tv.dfyc.yckt.detail.BuyListActivity;
import tv.dfyc.yckt.detail.BuyThemeActivity;
import tv.dfyc.yckt.detail.DetailActivity;
import tv.dfyc.yckt.detail.PlayVideoActivity;
import tv.dfyc.yckt.ThemeActivity;
import tv.dfyc.yckt.network.CallBackUtil;
import tv.dfyc.yckt.network.GsonUtil;
import tv.dfyc.yckt.network.OkhttpUtil;
import tv.dfyc.yckt.util.PreferencesUtil;
import tv.dfyc.yckt.util.GlobleData;
import tv.dfyc.yckt.custom.GridLayoutRecordManangerTv;
import tv.dfyc.yckt.custom.GuessLikeSpacing;
import tv.dfyc.yckt.beans.JsonAboutUsData;
import tv.dfyc.yckt.beans.JsonCollectRecordData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import tv.dfyc.yckt.beans.JsonCommonResult;
import tv.dfyc.yckt.beans.JsonGoodsListData;
import tv.dfyc.yckt.beans.JsonPurchaseRecordData;
import tv.dfyc.yckt.beans.JsonVideoDetailData;
import tv.dfyc.yckt.beans.JsonViewRecordData;
import tv.dfyc.yckt.util.LessonDetail;
import tv.dfyc.yckt.custom.LessonMenuSpacing;
import tv.dfyc.yckt.custom.LinearLayoutManagerPurchase;
import tv.dfyc.yckt.custom.PersonalLayoutManager;
import tv.dfyc.yckt.util.XESUtil;

import static tv.dfyc.yckt.util.GlobleData.HTTP_PARAM_LIBRARYID;
import static tv.dfyc.yckt.util.GlobleData.HTTP_URL_CHANNEL_DETAIL;
import static tv.dfyc.yckt.util.GlobleData.HTTP_URL_DELCOLLECT;
import static tv.dfyc.yckt.util.GlobleData.HTTP_URL_DELHISTORY;
import static tv.dfyc.yckt.util.GlobleData.HTTP_VERSION_KEY;
import static tv.dfyc.yckt.util.GlobleData.HTTP_VERSION_VALUE;

/**
 * Created by admin on 2017-11-20.
 */

public class PersonalPageNewActivity extends Activity {
    private static final String TAG = "personalPageNewActivity";
    private Context mContext;
    private RelativeLayout mPurchase_record;
    private RelativeLayout mConnect_us;
    private RelativeLayout mPurchase_detail;
    private RelativeLayout mConnect_us_detail;
    private TextView mRecord;
    private TextView mConnect_version;
    private ArrayList<HashMap<String, Object>> mListItem = new ArrayList<HashMap<String, Object>>();
    private ArrayList<HashMap<String, Object>> mPurchase_list = new ArrayList<HashMap<String, Object>>();
    private ArrayList<HashMap<String, Object>> mUpdate_purchase_list = new ArrayList<HashMap<String, Object>>();
    private TextView weekTextView;
    private TextView timeTextView;
    private TextView dateTextView;
    private MsgReceiver msgReceiver;
    private LinearLayout mPurchase_no_record;

    private final int VIEW_RECORD_MODE=1;
    private final int COLLECT_RECORD_MODE=2;
    private final int PURCHASE_MODE =3;
    private final int CONNECT_US_MODE = 4;
    private int personal_mode = 1;
    private RecyclerViewTV mPersonal_info_list;
    private GeneralInfoAdapter mGeneralAdapter;
    private ArrayList<String> mTextList;
    private ArrayList<Integer> mWhiteImageList;
    private ArrayList<Integer> mGrayImageList;
    private ArrayList<Integer> mBlackImageList;
    private RecyclerViewTV mPurchase_recycler_view;
    private GeneralAdapter mPurchaseAdapter;
    private RelativeLayout mView_record;
    private Button mView_record_delete;
    private ArrayList<LessonDetail> mViewRecordList ;
    private TextView mViewRecordPageText;

    private ArrayList<LessonDetail> mCollectRecordList;
    private ArrayList<LessonDetail> mUpdateCollectRecordList;
    private ArrayList<LessonDetail> mUpdateViewRecordList;
    private GeneralAdapter mCollectRecordAdapter;
    private GeneralAdapter mViewRecordAdapter;
    private RelativeLayout mCollectRecord;
    private RecyclerViewTV mCollectRecordRecyclView;
    private MainUpView mCollectRecordHoverState;
    private RecyclerViewBridge mCollectRecordHoverBridge;
    private View mCollectRecordOldView;
    private TextView mCollectRecordPageText;
    private int mCurrentCollectSelectPosition;
    private int mPreCollectSelectPosition = 0;
    private Button mCollect_button_delete;
    private static Boolean COLLECT_DELETE_MODE = false;
    private static Boolean VIEW_DELETE_MODE = false;
    private RelativeLayout mPurchuse_record_lauout;
    private LinearLayout mCollectScrollBg;
    private View mCollectScrollBar;
    private float mCollectScrollBarDistance;
    private RecordPresenter mRecyclerViewPresenter;
    private RecordPresenter mViewRecyclerViewPresenter;
    private MainUpView mViewRecordHoverState;
    private RecyclerViewBridge mViewRecordHoverBridge;
    private LinearLayout mViewRecordScrollBg;
    private View mViewRecordScrollBar;
    private float mViewRecordScrollBarDistance;
    //private ViewRecordListPageLayout mViewRecordLayout;
    private View mViewOldView;
    private RecyclerViewTV mViewRecordRecyclerView;
    private RelativeLayout mConnect_us_layout;
    private int mViewRecordTotalRows;
    private int mCollectTotalRows;
    private ImageView mUsImage;
    private int mViewTotalPage;
    private int mCollectTotalPage;
    private int mPurchaseTotalPage;
    private TextView mPurchasePageText;
    private int mPurchaseTotalRows;
    private LinearLayout mView_no_record;
    private LinearLayout mCollect_no_record;
    private RelativeLayout mView_result;
    private RelativeLayout mCollect_result;
    private Handler mHandler;
    private JsonVideoDetailData jsonData;
    private int mHaveOrder = 0;
    private JsonGoodsListData jsonGoodsList;
    private RelativeLayout mTmp;
    private String mUsImageUrl;
    private PersonalLayoutManager mListlayoutManager;
    private GridLayoutRecordManangerTv mCollectgridlayoutManager;

    private Handler mUIHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:    // 图片加载完成
//                    mView_record_delete.setFocusable(true);
//                    mView_record_delete.requestFocus();
                    break;
                case 1:
                    mCollect_button_delete.setFocusable(true);
                    mCollect_button_delete.requestFocus();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_page);
        mContext = this;
        initView();
        initData();
        startConnectus();
        getPersonalDataFromHttp();
        initInfoList();
        initViewRecordRecycler();
        initCollectRecycler();
        initPurchaseRecycler();
        initButtonClick();
        msgReceiver = new MsgReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(GlobleData.MESSAGE_UPDATE_TIME);
        intentFilter.addAction(GlobleData.MESSAGE_UPDATE_DETAIL_DATA);
        registerReceiver(msgReceiver, intentFilter);
    }

    private void initView() {
        mTmp = (RelativeLayout)findViewById(R.id.tmp);
        mCollect_result = (RelativeLayout) findViewById(R.id.collect_result);
        mView_result = (RelativeLayout) findViewById(R.id.view_result);
        mView_no_record = (LinearLayout) findViewById(R.id.view_no_record);
        mCollect_no_record = (LinearLayout) findViewById(R.id.collect_no_record);
        mPurchasePageText = (TextView) findViewById(R.id.purchase_pageText);
        mUsImage = (ImageView) findViewById(R.id.us_image);
        mConnect_us_layout = (RelativeLayout) findViewById(R.id.connect_us_layout);
        //mViewRecordLayout = (ViewRecordListPageLayout)findViewById(R.id.view_record_layout);
        mCollectScrollBg = (LinearLayout) findViewById(R.id.collect_scroll_bg);
        mCollectScrollBar = (View) findViewById(R.id.collect_scroll_bar);
        mViewRecordScrollBg = (LinearLayout) findViewById(R.id.view_scroll_bg);
        mViewRecordScrollBar = (View) findViewById(R.id.view_scroll_bar);
        mPersonal_info_list = (RecyclerViewTV) findViewById(R.id.personal_info_list);
        mViewRecordRecyclerView = (RecyclerViewTV) findViewById(R.id.view_record_recycler_view);
        mTextList = new ArrayList<String>() {{
            add("观看记录");
            add("收藏记录");
            add("订购记录");
            add("关于我们");
        }};
        mWhiteImageList = new ArrayList<Integer>() {{
            add(R.drawable.white_view_record);
            add(R.drawable.white_collect_record);
            add(R.drawable.white_purchase_record);
            add(R.drawable.white_connect_us);
        }};
        mGrayImageList = new ArrayList<Integer>() {{
            add(R.drawable.gray_view_record);
            add(R.drawable.gray_collect_record);
            add(R.drawable.gray_purchase_record);
            add(R.drawable.gray_connect_us);
        }};
        mBlackImageList = new ArrayList<Integer>() {{
            add(R.drawable.black_view_record);
            add(R.drawable.black_collect_record);
            add(R.drawable.black_purchase_record);
            add(R.drawable.black_connect_us);
        }};
        mPurchase_recycler_view = (RecyclerViewTV) findViewById(R.id.purchase_recycler_view);
        mView_record = (RelativeLayout) findViewById(R.id.view_record);
        mView_record_delete = (Button) findViewById(R.id.view_record_delete);
        mViewRecordPageText = (TextView) findViewById(R.id.view_record_pageText);
        mRecord = (TextView) findViewById(R.id.personal);
        mPurchase_detail = (RelativeLayout) findViewById(R.id.purchase_detail);
        mConnect_us_detail = (RelativeLayout) findViewById(R.id.connect_us_detail);
        mConnect_version = (TextView) findViewById(R.id.version);
        weekTextView = (TextView) findViewById(R.id.week);
        timeTextView = (TextView) findViewById(R.id.time);
        dateTextView = (TextView) findViewById(R.id.date);
        mPurchase_no_record = (LinearLayout) findViewById(R.id.purchase_no_record);
        mCollectRecord = (RelativeLayout) findViewById(R.id.collect_record);
        mCollectRecordRecyclView = (RecyclerViewTV) findViewById(R.id.collect_record_recycler_view);
        mCollectRecordPageText = (TextView) findViewById(R.id.collect_record_pageText);
        mCollect_button_delete = (Button) findViewById(R.id.collect_record_delete);
        mPurchuse_record_lauout = (RelativeLayout) findViewById(R.id.purchuse_record_layout);
        mConnect_version.setText(getVersion());

        mCollect_button_delete.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN){
                    mCollectRecordHoverState.setVisibility(View.INVISIBLE);
                }
                return false;
            }
        });
    }


public String getVersion() {
    try {
        PackageManager manager = this.getPackageManager();
        PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
        String version = info.versionName;
            return  version;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
}
}

    private  void initData() {
        mViewRecordList = new ArrayList<>();
        mCollectRecordList = new ArrayList<>();
    }
    private void getPersonalDataFromHttp() {
        getViewReocrdDataFromHttp();
        getCollectReocrdDataFromHttp();
        getPurchaseReocrdDataFromHttp();
        getConnectUsDataFromHttp();

    }

    private void getViewReocrdDataFromHttp() {
        Map<String, String> paramsMap = new HashMap<>();
        String mac = PreferencesUtil.GetAuthorValue(mContext, GlobleData.PREFERENCE_AUTHOR_STBMAC, "");
        String userID = PreferencesUtil.GetAuthorValue(mContext,GlobleData.PREFERENCE_AUTHOR_USER_ID, "");
//        mac= "00:24:68:E8:AC:0E";
//        userID=83+"";
        paramsMap.put(GlobleData.HTTP_VERSION_KEY,GlobleData.HTTP_VERSION_VALUE);
        paramsMap.put("Mac", mac);
        paramsMap.put("UserId", userID);

       OkhttpUtil.okHttpGet(GlobleData.HTTP_URL_SOURCEHISTORY, paramsMap, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                Log.d(TAG, "连接失败！");
            }

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);
                try {
                    JSONObject jsonObj = new JSONObject(response);
                    if (jsonObj == null) {
                        Toast.makeText(mContext, "获取观看记录失败！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (Exception e) {
                    Toast.makeText(mContext, "获取观看记录失败！", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    JsonViewRecordData jsonData = GsonUtil.fromJson(response, JsonViewRecordData.class);
                    if (jsonData == null)
                        return;
                    if(jsonData.getResult().equals("0")) {
                        if(jsonData.getData().size() == 0){
                            mView_result.setVisibility(View.INVISIBLE);
                            mView_no_record.setVisibility(View.VISIBLE);
                        }else {
                            mView_result.setVisibility(View.VISIBLE);
                            mView_no_record.setVisibility(View.INVISIBLE);
                            mViewRecordList.clear();
                            for (int i = 0;i < jsonData.getData().size();i++){
                                LessonDetail one = new LessonDetail();
                                one.setmLessonImageUrl(jsonData.getData().get(i).getLibrary_image());
                                one.setmLessonName(jsonData.getData().get(i).getVideo_name());
                                one.setLibraryID(jsonData.getData().get(i).getLibrary_id());
                                one.setVideoId(jsonData.getData().get(i).getVideo_id());
                                one.setHistoryId(jsonData.getData().get(i).getHistoryid());
                                one.setLast_play_time(jsonData.getData().get(i).getLast_play_time());
                                one.setVideoDurtion(jsonData.getData().get(i).getVideo_duration());
                                one.setIsCharge(jsonData.getData().get(i).getIs_free());
                                one.setVideo_is_free(jsonData.getData().get(i).getVideo_is_free());
                                one.setmLessonImageID(R.drawable.load_list);
                                mViewRecordList.add(one);
                            }
                            mViewRecordAdapter.notifyDataSetChanged();
                            initViewRecordListViewItems();
                        }

                    } else {
                        mView_result.setVisibility(View.INVISIBLE);
                        mView_no_record.setVisibility(View.INVISIBLE);
                        Toast.makeText(mContext, jsonData.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    mView_result.setVisibility(View.INVISIBLE);
                    mView_no_record.setVisibility(View.INVISIBLE);
                    e.printStackTrace();
                }
            }
        });
    }

    private void getCollectReocrdDataFromHttp() {
        Map<String, String> paramsMap = new HashMap<>();
        String mac = PreferencesUtil.GetAuthorValue(mContext, GlobleData.PREFERENCE_AUTHOR_STBMAC, "");
        String userID = PreferencesUtil.GetAuthorValue(mContext,GlobleData.PREFERENCE_AUTHOR_USER_ID, "");
//        mac= "00:24:68:E8:AC:0E";
//        userID=83+"";
        paramsMap.put(GlobleData.HTTP_VERSION_KEY,GlobleData.HTTP_VERSION_VALUE);
        paramsMap.put("Mac", mac);
        paramsMap.put("UserId", userID);

         OkhttpUtil.okHttpGet(GlobleData.HTTP_URL_MYCOLLECT, paramsMap, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                Log.d(TAG, "连接失败！");
            }

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);
                try {
                    JSONObject jsonObj = new JSONObject(response);
                    if (jsonObj == null) {
                        Toast.makeText(mContext, "获取收藏记录失败！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (Exception e) {
                    Toast.makeText(mContext, "获取收藏记录失败！", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    JsonCollectRecordData jsonData = GsonUtil.fromJson(response, JsonCollectRecordData.class);
                    if (jsonData == null)
                        return;
                    if(jsonData.getResult().equals("0")) {
                        if(jsonData.getData().size() == 0){
                            mCollect_result.setVisibility(View.INVISIBLE);
                            mCollect_no_record.setVisibility(View.VISIBLE);

                        }else {
                            mCollect_result.setVisibility(View.VISIBLE);
                            mCollect_no_record.setVisibility(View.INVISIBLE);
                            mCollectRecordList.clear();
                            for (int i = 0;i < jsonData.getData().size();i++){
                                LessonDetail one = new LessonDetail();
                                Log.e("title = ",jsonData.getData().get(i).getTitle());
                                one.setmLessonImageUrl(jsonData.getData().get(i).getThumb_app_h());
                                one.setmLessonName(jsonData.getData().get(i).getTitle());
                                one.setLibraryID(jsonData.getData().get(i).getId());
                                one.setmLessonImageID(R.drawable.load_list);
                                one.setIsCharge(jsonData.getData().get(i).getIs_free());
                                one.setHistoryId(jsonData.getData().get(i).getHistoryid());
                                one.setType(jsonData.getData().get(i).getType());
                                mCollectRecordList.add(one);
                            }
                            mCollectRecordAdapter.notifyDataSetChanged();
                            initCollectListViewItems();
                        }

                    } else {
                        mCollect_result.setVisibility(View.INVISIBLE);
                        mCollect_no_record.setVisibility(View.INVISIBLE);
                        Toast.makeText(mContext, jsonData.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    mCollect_result.setVisibility(View.INVISIBLE);
                    mCollect_no_record.setVisibility(View.INVISIBLE);
                    e.printStackTrace();
                }
            }
        });
    }

    private void getPurchaseReocrdDataFromHttp() {
        Map<String, String> paramsMap = new HashMap<>();
        String mac = PreferencesUtil.GetAuthorValue(mContext, GlobleData.PREFERENCE_AUTHOR_STBMAC, "");
        String userID = PreferencesUtil.GetAuthorValue(mContext,GlobleData.PREFERENCE_AUTHOR_USER_ID, "");
//        mac= "00:24:68:E8:AC:0E";
//        userID=83+"";
        paramsMap.put(GlobleData.HTTP_VERSION_KEY,GlobleData.HTTP_VERSION_VALUE);
        paramsMap.put("Mac", mac);
        paramsMap.put("UserId", userID);

        OkhttpUtil.okHttpGet(GlobleData.HTTP_URL_ORDERLIST, paramsMap, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                Log.d(TAG, "连接失败！");
            }

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);
                try {
                    JSONObject jsonObj = new JSONObject(response);
                    if (jsonObj == null) {
                        Toast.makeText(mContext, "获取订购记录失败！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (Exception e) {
                    Toast.makeText(mContext, "获取订购记录失败！", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    JsonPurchaseRecordData jsonData = GsonUtil.fromJson(response, JsonPurchaseRecordData.class);
                    if (jsonData == null)
                        return;
                    if(jsonData.getResult().equals("0")) {
                        if(jsonData.getData().size() == 0){
                            mPurchase_no_record.setVisibility(View.VISIBLE);
                        }else {
                            mPurchase_no_record.setVisibility(View.GONE);
                            mPurchase_list.clear();
                            for (int i = 0;i < jsonData.getData().size();i++){
                                HashMap<String, Object> map = new HashMap<String, Object>();
                                map.put("purchase_name",jsonData.getData().get(i).getName());
                                map.put("purchase_date",jsonData.getData().get(i).getEndtime());
                                map.put("purchase_sum", jsonData.getData().get(i).getPostage());
                                map.put("purchase_state",jsonData.getData().get(i).getStatus());
                                mPurchase_list.add(map);
                            }
                            mPurchaseAdapter.notifyDataSetChanged();
                            mPurchasePageText.setText("共" + mPurchase_list.size() + "部");

                        }

                    } else {
                        Toast.makeText(mContext, jsonData.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getConnectUsDataFromHttp() {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put(GlobleData.HTTP_VERSION_KEY,GlobleData.HTTP_VERSION_VALUE);
        OkhttpUtil.okHttpGet(GlobleData.HTTP_URL_ABOUTUS,paramsMap,new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                Log.d(TAG, "连接失败！");
            }

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);
                try {
                    JSONObject jsonObj = new JSONObject(response);
                    if (jsonObj == null) {
                        Toast.makeText(mContext, "获取关于我们失败！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (Exception e) {
                    Toast.makeText(mContext, "获取关于我们失败！", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    JsonAboutUsData jsonData = GsonUtil.fromJson(response, JsonAboutUsData.class);
                    if (jsonData == null)
                        return;
                    if(jsonData.getResult().equals("0") && jsonData.getData().getImage_url() != null) {
//                        resetConnectUs(jsonData.getData().getImage_url());
//                        XESUtil.savePicture(mContext, GlobleData.PNG_CONNECT_US_NAME, mUsImageUrl);
                        Glide.with(mContext).load(jsonData.getData().getImage_url()).into(mUsImage);

                    } else {
                        Toast.makeText(mContext, jsonData.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void startConnectus() {
        mUsImage.setImageResource(R.drawable.us);
        try {
            if(XESUtil.isPictureExist(GlobleData.PNG_CONNECT_US_NAME)) {
                Log.d(TAG, "splash.png exist");
                Uri uri = Uri.fromFile(new File(XESUtil.getPicturePath(GlobleData.PNG_CONNECT_US_NAME)));
                mUsImage.setImageURI(uri);
            }
        } catch (Exception e) {
            Log.d(TAG, "startSplash exception");
            e.printStackTrace();
        }
    }

    private void resetConnectUs(String us_url) {
        mUsImageUrl = us_url;
        try {
            if(!XESUtil.isPictureExist(GlobleData.PNG_CONNECT_US_NAME)) {
                Log.d(TAG, "network conect_us.png");
                Glide.with(mContext).load(us_url).placeholder(R.drawable.us).into(mUsImage);
            }
        } catch (Exception e) {
            Log.d(TAG, "resetConnectUs Exception");
            e.printStackTrace();
        }
    }

    private void initViewRecordListViewItems() {

        // 知识点总数
        mViewRecordPageText.setText("共" + mViewRecordList.size() + "部");

        // 总页数
        if(mViewRecordList.size() / 8 == 0)
            mViewTotalPage = 1;
        else
            mViewTotalPage = mViewRecordList.size() / 8 + (mViewRecordList.size() % 8 == 0 ? 0:1);

        // 总行数
        if(mViewRecordList.size() / 4 == 0)
            mViewRecordTotalRows = 1;
        else
            mViewRecordTotalRows = mViewRecordList.size() / 4 + (mViewRecordList.size() % 4 == 0 ? 0:1);

        // 处理进度条
        if(mViewTotalPage == 1) {
            mViewRecordScrollBg.setVisibility(View.INVISIBLE);
        } else {
            mViewRecordScrollBg.setVisibility(View.VISIBLE);
            new Handler().postDelayed(new Runnable(){
                public void run() {
                    ViewGroup.LayoutParams para1;
                    para1 = mViewRecordScrollBar.getLayoutParams();
                    mViewRecordScrollBarDistance = mViewRecordScrollBg.getHeight() / mViewRecordTotalRows;
                    para1.height = mViewRecordScrollBg.getHeight() / mViewRecordTotalRows;

                    mViewRecordScrollBar.setLayoutParams(para1);
                    mViewRecordScrollBar.setTranslationY(0);
                }
            }, 200);
        }
    }

    private void initCollectListViewItems() {
        // 知识点总数
        mCollectRecordPageText.setText("共" + mCollectRecordList.size() + "部");

        // 总页数
        if(mCollectRecordList.size() / 8 == 0)
            mCollectTotalPage = 1;
        else
            mCollectTotalPage = mCollectRecordList.size() / 8 + (mCollectRecordList.size() % 8 == 0 ? 0:1);

        // 总行数
        if(mCollectRecordList.size() / 4 == 0)
            mCollectTotalRows = 1;
        else
            mCollectTotalRows = mCollectRecordList.size() / 4 + (mCollectRecordList.size() % 4 == 0 ? 0:1);

        // 处理进度条
        if(mCollectTotalPage == 1) {
            mCollectScrollBg.setVisibility(View.INVISIBLE);
        } else {
            mCollectScrollBg.setVisibility(View.VISIBLE);
            new Handler().postDelayed(new Runnable(){
                public void run() {
                    ViewGroup.LayoutParams para1;
                    para1 = mCollectScrollBar.getLayoutParams();
                    mCollectScrollBarDistance = mCollectScrollBg.getHeight() / mCollectTotalRows;
                    para1.height = mCollectScrollBg.getHeight() / mCollectTotalRows;

                    mCollectScrollBar.setLayoutParams(para1);
                    mCollectScrollBar.setTranslationY(0);
                }
            }, 200);
        }
    }

    @Override
    public void onBackPressed() {
        COLLECT_DELETE_MODE = false;
        super.onBackPressed();
    }

    private void initButtonClick(){
        mCollect_button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("COLLECT_DELETE_MODE=",COLLECT_DELETE_MODE+"");
                if (COLLECT_DELETE_MODE) {
                    COLLECT_DELETE_MODE = false;
                    mCollect_button_delete.setText("删除");
                    mRecyclerViewPresenter.InDelteMode(false);
                    mCollectRecordAdapter.notifyDataSetChanged();

                }else {
                    COLLECT_DELETE_MODE = true;
                    mCollect_button_delete.setText("完成");
                    mRecyclerViewPresenter.InDelteMode(true);
                    mCollectRecordAdapter.notifyDataSetChanged();
                }
            }
        });

        mView_record_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (VIEW_DELETE_MODE) {
                    VIEW_DELETE_MODE = false;
                    mView_record_delete.setText("删除");
                    mViewRecyclerViewPresenter.InDelteMode(false);
                    mViewRecordAdapter.notifyDataSetChanged();

                }else {
                    mView_record_delete.setText("完成");
                    VIEW_DELETE_MODE = true;
                    mViewRecyclerViewPresenter.InDelteMode(true);
                    mViewRecordAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void initInfoList() {
        mListlayoutManager = new PersonalLayoutManager(this);
        mListlayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mListlayoutManager.setScrollEnabled(false);
        mPersonal_info_list.setLayoutManager(mListlayoutManager);
        mPersonal_info_list.setFocusable(false);
        mGeneralAdapter = new GeneralInfoAdapter(this, mTextList,mWhiteImageList,mGrayImageList,mBlackImageList,mPersonal_info_list);
        mPersonal_info_list.setAdapter(mGeneralAdapter);
        //layoutManager.setFocusFailedAdapter(mGeneralAdapter);
        mPersonal_info_list.addItemDecoration(new LessonMenuSpacing(0, (int)getDimension(R.dimen.w_10), 0, (int)getDimension(R.dimen.w_20)));

        mGeneralAdapter.setOnItemSelectListener(new GeneralInfoAdapter.OnItemSelectListener() {
            @Override
            public void onItemSelect(View view, int position) {
                switchRecordPage(position);
            }
        });

        mListlayoutManager.setRightKeyListener(new PersonalLayoutManager.OnRightKeyListener() {
            @Override
            public void onRightKey(View focused) {

                mGeneralAdapter.setSelectedBackground(focused);
            }
        });

    }

    private boolean isViewRecordListRowPresenter(){
        GeneralAdapter generalAdapter = (GeneralAdapter) mViewRecordRecyclerView.getAdapter();
        OpenPresenter openPresenter = generalAdapter.getPresenter();
        return (openPresenter instanceof ListRowPresenter);
    }

    private boolean isCollectRecordListRowPresenter() {
        GeneralAdapter generalAdapter = (GeneralAdapter) mCollectRecordRecyclView.getAdapter();
        OpenPresenter openPresenter = generalAdapter.getPresenter();
        return (openPresenter instanceof ListRowPresenter);
    }

    private void initViewRecordRecycler() {
        GridLayoutRecordManangerTv gridlayoutManager = new GridLayoutRecordManangerTv(mContext,4);
        gridlayoutManager.setOrientation(GridLayoutManagerTV.VERTICAL);
        mViewRecordRecyclerView.setVerticalScrollBarEnabled(false);
        mViewRecordRecyclerView.setLayoutManager(gridlayoutManager);
        mViewRecordRecyclerView.setFocusable(false);
        //mViewRecordRecyclerView.setSelectedItemAtCentered(true); // 设置item在中间移动
        mViewRecordRecyclerView.setSelectedItemOffset(0,200);

        gridlayoutManager.setFocusFailedAdapter(mGeneralAdapter);

        mViewRecyclerViewPresenter = new RecordPresenter(mContext, mViewRecordList);
        mViewRecordAdapter = new GeneralAdapter(mViewRecyclerViewPresenter);

        mViewRecordAdapter.setHasStableIds(true);
        mViewRecordRecyclerView.setItemAnimator(null);

        mViewRecordRecyclerView.setAdapter(mViewRecordAdapter);
        mViewRecordRecyclerView.addItemDecoration(new GuessLikeSpacing((int)getDimension(R.dimen.w_20),(int)getDimension(R.dimen.w_20),(int)getDimension(R.dimen.w_20), (int)getDimension(R.dimen.w_20)));

        gridlayoutManager.setLeftKeyListener(new GridLayoutRecordManangerTv.OnLeftKeyListener() {
            @Override
            public void onLeftKey() {
                //mGeneralAdapter.notifyDataSetChanged();
            }
        });

        gridlayoutManager.setUpKeyListener(new GridLayoutRecordManangerTv.OnUpKeyListener(){
            @Override
            public void onUpKey(int position) {
                if(position < 4) {
                    mViewRecordScrollBar.setTranslationY(0);
                    return;
                }
                int iTranslation = (int)(mViewRecordScrollBar.getTranslationY() - mViewRecordScrollBarDistance);
                if(iTranslation < 0) {
                    return;
                }
                mViewRecordScrollBar.setTranslationY(iTranslation);
            }
        });
        gridlayoutManager.setDownKeyListener(new GridLayoutRecordManangerTv.OnDownKeyListener() {
            @Override
            public void onDownKey(int position) {
                if(mViewRecordList.size() % 4 == 0) {
                    if(position >= (mViewRecordList.size() / 4 - 1) * 4) {
                        mViewRecordScrollBar.setTranslationY(mViewRecordScrollBg.getHeight() - mViewRecordScrollBar.getHeight());
                        return;
                    }
                } else {
                    if(position >= (mViewRecordList.size() / 4) * 4) {
                        mViewRecordScrollBar.setTranslationY(mViewRecordScrollBg.getHeight() - mViewRecordScrollBar.getHeight());
                        return;
                    }
                }
                int iTranslation = (int)(mViewRecordScrollBar.getTranslationY() + mViewRecordScrollBarDistance);
                if(iTranslation > mViewRecordScrollBg.getHeight() - mViewRecordScrollBar.getHeight()) {
                    return;
                }

                mViewRecordScrollBar.setTranslationY(iTranslation);

            }
        });

//        gridlayoutManager.setQuitKeyListener(new GridLayoutRecordManangerTv.OnQuitKeyListener() {
//            @Override
//            public void onQuitKey() {
//                HandlerThread handlerThread = new HandlerThread("updateview");//创建一个handlerThread线程
//                handlerThread.start();//启动该线程
//                mHandler =  new Handler(handlerThread.getLooper()) {
//                    public void handleMessage(Message msg) {
//                        super.handleMessage(msg);
//                        mUIHandler.sendEmptyMessage(0);
//                    }
//                };
//
//                Message msg = new Message();
//                mHandler.sendMessage(msg);
//            }
//        });

        initViewRecordHover();
    }
    private void initViewRecordHover() {
        mViewRecordHoverState = (MainUpView) findViewById(R.id.view_record_upview);
        mViewRecordHoverState.setEffectBridge(new RecyclerViewBridge());
        mViewRecordHoverBridge = (RecyclerViewBridge) mViewRecordHoverState.getEffectBridge();
        mViewRecordHoverBridge.setUpRectResource(R.drawable.yellow_white_border);
        float density = getResources().getDisplayMetrics().density;
        RectF rectf = new RectF(getDimension(R.dimen.w_13) * density, getDimension(R.dimen.h_11) * density,
                getDimension(R.dimen.w_12) * density, getDimension(R.dimen.h_11) * density);
        mViewRecordHoverBridge.setDrawUpRectPadding(rectf);

        mViewRecordRecyclerView.setOnItemClickListener(new RecyclerViewTV.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerViewTV parent, View itemView, int position) {
                if (VIEW_DELETE_MODE){
                    removeViewDataFromHttp(position);
                }else {
                    playViewInViewRecord(position);
                }
            }
        });

        mViewRecordRecyclerView.setOnItemListener(new RecyclerViewTV.OnItemListener() {

            @Override
            public void onItemPreSelected(RecyclerViewTV parent, View itemView, int position) {
                if (!isViewRecordListRowPresenter()) {
                    mViewRecordHoverBridge.setUnFocusView(mViewOldView);
                    mViewRecordHoverBridge.setVisibleWidget(true);
                }
                itemView.findViewWithTag("delete").setBackgroundResource(R.drawable.little_delete);
                TextView text = (TextView) itemView.findViewById(R.id.little_record_detail_name);
                text.setMaxLines(1);
            }

            @Override
            public void onItemSelected(RecyclerViewTV parent, View itemView, int position) {
                if (!isViewRecordListRowPresenter()) {
                    if(mViewRecordHoverBridge.isVisibleWidget()) {
                        mViewRecordHoverBridge.setVisibleWidget(false);
                    }
                    itemView.findViewWithTag("delete").setBackgroundResource(R.drawable.big_delete);
                    TextView text = (TextView) itemView.findViewById(R.id.little_record_detail_name);
                    text.setMaxLines(2);
                    mViewRecordHoverBridge.setFocusView(itemView, 1.1f);
                    mViewOldView = itemView;

                }
            }

            @Override
            public void onReviseFocusFollow(RecyclerViewTV parent, View itemView, int position) {
                if (!isViewRecordListRowPresenter()) {
                    if(mViewRecordHoverBridge.isVisibleWidget()) {
                        mViewRecordHoverBridge.setVisibleWidget(false);
                    }
                    itemView.findViewWithTag("delete").setBackgroundResource(R.drawable.big_delete);
                    TextView text = (TextView) itemView.findViewById(R.id.little_record_detail_name);
                    text.setMaxLines(2);
                    mViewRecordHoverBridge.setFocusView(itemView, 1.1f);
                    mViewOldView = itemView;
                }
            }
        });
    }

    private void playViewInViewRecord(final int position){
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put(HTTP_VERSION_KEY,HTTP_VERSION_VALUE);
        paramsMap.put(HTTP_PARAM_LIBRARYID,Integer.toString(mViewRecordList.get(position).getLibraryID()));
        String mac = PreferencesUtil.GetAuthorValue(mContext, GlobleData.PREFERENCE_AUTHOR_STBMAC, "");
        String userID = PreferencesUtil.GetAuthorValue(mContext,GlobleData.PREFERENCE_AUTHOR_USER_ID, "");
//        mac= "00:24:68:E8:AC:0E";
//        userID=83+"";
        paramsMap.put("Mac", mac);
        paramsMap.put("UserId", userID);
        paramsMap.put("libraryId",Integer.valueOf(mViewRecordList.get(position).getLibraryID()).toString());
        OkhttpUtil.okHttpGet(HTTP_URL_CHANNEL_DETAIL, paramsMap, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                Log.d(TAG,"连接失败！");
            }
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);
                try {
                    JSONObject jsonObj = new JSONObject(response);
                    if(jsonObj == null) {
                        Toast.makeText(mContext, "获取视频详情失败！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (Exception e) {
                    Toast.makeText(mContext, "获取视频详情失败！", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    jsonData = GsonUtil.fromJson(response, JsonVideoDetailData.class);
                    if(jsonData == null)
                        return;
                    if(jsonData.getResult() == 1)
                        return;
                    // 判断知识点是否订购
                    getProductList(position, jsonData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getProductList(final int position,final JsonVideoDetailData jsonData){

        int iContinueIndex = -1;
        if (jsonData != null && jsonData.getData().getVideo_list().size() > 0) {
            for (int i = 0; i < jsonData.getData().getVideo_list().size(); ++i) {
                if (jsonData.getData().getLibrary_detail_id() ==
                        jsonData.getData().getVideo_list().get(i).getLibrary_detail_id()) {
                    iContinueIndex = i;
                    break;
                }
            }
        }

        final Dialog builder = new Dialog(PersonalPageNewActivity.this, R.style.dialogTransparent);
        final RelativeLayout loginDialog= (RelativeLayout) getLayoutInflater().inflate(R.layout.pop_dialog_continue_play,null);
        final Button mContinue = (Button)loginDialog.findViewById(R.id.pop_continue);
        final Button mRestart =  (Button)loginDialog.findViewById(R.id.pop_restart);
        final TextView mPlayTimeTextView = (TextView) loginDialog.findViewById(R.id.play_time_text);

        final int playIndex = iContinueIndex; //当前哪一集

        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put(GlobleData.HTTP_VERSION_KEY, GlobleData.HTTP_VERSION_VALUE);
        paramsMap.put(GlobleData.HTTP_PARAM_LIBRARYID, Integer.toString(mViewRecordList.get(position).getLibraryID()));
        OkhttpUtil.okHttpGet(GlobleData.HTTP_URL_GOODSLIST, paramsMap, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
            }

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);
                try {
                    JSONObject obj = new JSONObject(response);
                    if(obj == null) {
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
                try {
                    jsonGoodsList = GsonUtil.fromJson(response, JsonGoodsListData.class);
                    if(jsonGoodsList == null)
                        return;
                    if(jsonGoodsList.getResult().equals("0")) {
                        if(jsonGoodsList.getData().size() > 0) {
                            int isOrder = jsonData.getData().getIsOrder();
                            int isFree = jsonData.getData().getIs_free();

                            // 是否免费
                            if (isFree == 0) {
                                mHaveOrder = 1;
                            } else {
                                // 是否订购
                                mHaveOrder = isOrder;
                            }
                        }
                    } else {
                        int isOrder = jsonData.getData().getIsOrder();
                        int isFree = jsonData.getData().getIs_free();

                        // 是否免费
                        if (isFree == 0) {
                            mHaveOrder = 1;
                        } else {
                            // 是否订购
                            mHaveOrder = isOrder;
                        }
                    }
                    if(mHaveOrder != 1) {
                        //知识点未订购
                        if (mViewRecordList.get(position).getVideo_is_free() == 2){
                            //视频免费
                            mPlayTimeTextView.setText("您已经观看到" + jsonData.getData().getTitle()+"知识点第"+(playIndex+1)+"集的"+XESUtil.stringForTime(Integer.valueOf(mViewRecordList.get(position).getLast_play_time()).intValue()*1000) + "分钟，是否继续观看？");
                            builder.setContentView(loginDialog);
                            builder.setCancelable(true);
                            builder.show();
                            mContinue.requestFocus();
                            mRestart.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    playVideo(playIndex,0);
                                    builder.dismiss();
                                }
                            });

                            mContinue.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String durtion = jsonData.getData().getVideo_list().get(playIndex).getVideo_duration();
                                    int lastPlayTime = jsonData.getData().getLast_play_time();
                                    if (!durtion.equals("")){
                                        if (Integer.valueOf(durtion).intValue() - lastPlayTime < 5){
                                            if (playIndex != jsonData.getData().getVideo_list().size()-1){
                                                if(jsonData.getData().getVideo_list().get(playIndex+1).getVideo_is_free() == 2){
                                                    playVideo(playIndex+1, 0);
                                                }else {
                                                    //跳转到订购页面
                                                    sentToGoodlPage(jsonData.getData().getLibrary_id());
                                                    builder.dismiss();
                                                    return;
                                                }
                                            }else{
                                                if (jsonData.getData().getVideo_list().get(0).getVideo_is_free() == 2){
                                                    playVideo(0,0);
                                                }else {
                                                    sentToGoodlPage(jsonData.getData().getLibrary_id());
                                                }
                                                builder.dismiss();
                                                return;
                                            }
                                        }else{
                                            playVideo(playIndex, Integer.valueOf(jsonData.getData().getLast_play_time()));
                                        }
                                    }else {
                                        playVideo(playIndex, Integer.valueOf(jsonData.getData().getLast_play_time()));
                                    }
                                    builder.dismiss();
                                }
                            });
                        }else {
                            //不免费
                            //跳转到订购页面
                            sentToGoodlPage(jsonData.getData().getLibrary_id());
                            return;
                        }
                    }else {
                        mPlayTimeTextView.setText("您已经观看到" + jsonData.getData().getTitle()+"知识点第"+(playIndex+1)+"集的"+XESUtil.stringForTime(jsonData.getData().getLast_play_time() * 1000) + "分钟，是否继续观看？");
                        builder.setContentView(loginDialog);
                        builder.setCancelable(true);
                        builder.show();
                        mContinue.requestFocus();

                        mContinue.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String durtion = jsonData.getData().getVideo_list().get(playIndex).getVideo_duration();
                                int lastPlayTime = jsonData.getData().getLast_play_time();
                                // 对剧集进行鉴权
                                if (!durtion.equals("")) {
                                    if (playIndex == jsonData.getData().getVideo_list().size() - 1) {
                                        if (Integer.valueOf(durtion).intValue() - lastPlayTime < 5){
                                            //播放下一集,从头开始播放
                                            playVideo(0, 0);
                                        }else {
                                            playVideo(playIndex,Integer.valueOf(jsonData.getData().getLast_play_time()));
                                        }

                                    }else {
                                        if (Integer.valueOf(durtion).intValue() - lastPlayTime < 5){
                                            //播放下一集
                                            playVideo(playIndex+1, 0);
                                        }else {
                                            playVideo(playIndex,Integer.valueOf(jsonData.getData().getLast_play_time()));
                                        }
                                    }
                                }else {
                                    playVideo(playIndex,Integer.valueOf(jsonData.getData().getLast_play_time()));
                                }
                                builder.dismiss();
                            }
                        });

                        mRestart.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // 对剧集进行鉴权
                                playVideo(playIndex, 0);
                                builder.dismiss();
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void sentToGoodlPage(int libraryId) {
        try {
            if(jsonGoodsList != null && jsonGoodsList.getResult().equals("0")) {
                Intent intent;
                if(jsonGoodsList.getData() != null && jsonGoodsList.getData().size() > 0) {
                    if (jsonGoodsList.getData().size() == 1) {
                        if(jsonGoodsList.getData().get(0) == null) {
                            Toast.makeText(mContext, "获取商品信息失败！", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(jsonGoodsList.getData().get(0).getGoodstype() == 2)
                            intent = new Intent(mContext, BuyThemeActivity.class);
                        else
                            intent = new Intent(mContext, BuyListActivity.class);
                    } else {
                        intent = new Intent(mContext, BuyListActivity.class);
                    }
                    intent.putExtra("productData", (Serializable) jsonGoodsList);
                    intent.putExtra("libraryId", libraryId);
                    startActivity(intent);
                }
            } else {
                Toast.makeText(mContext, "获取商品信息失败！", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(mContext, "获取商品信息失败！", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void playVideo(final int video_index, final int play_time){
        int a =video_index;
        Intent intent = new Intent(mContext, PlayVideoActivity.class);
        intent.putExtra("play_video_json", (Serializable) jsonData);
        intent.putExtra("play_goodslist_json",(Serializable) jsonGoodsList);
        intent.putExtra("from",1);
        intent.putExtra("play_video_url", jsonData.getData().getVideo_list().get(video_index).getPlayurl());
        intent.putExtra("play_video_durtion", jsonData.getData().getVideo_list().get(video_index).getVideo_duration());
        intent.putExtra("play_video_name", jsonData.getData().getVideo_list().get(video_index).getVideo_name());
        intent.putExtra("play_video_id", jsonData.getData().getVideo_list().get(video_index).getLibrary_detail_id()+"");
        intent.putExtra("play_video_playtime", play_time);
        startActivity(intent);
    }

    private void removeViewDataFromHttp(final int position) {
        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put(GlobleData.HTTP_VERSION_KEY, GlobleData.HTTP_VERSION_VALUE);
        String mac = PreferencesUtil.GetAuthorValue(mContext, GlobleData.PREFERENCE_AUTHOR_STBMAC, "");
        String userID = PreferencesUtil.GetAuthorValue(mContext,GlobleData.PREFERENCE_AUTHOR_USER_ID, "");
//        mac= "00:24:68:E8:AC:0E";
//        userID=83+"";
        paramsMap.put("Mac", mac);
        paramsMap.put("UserID", userID);
        paramsMap.put("HistoryId", Integer.valueOf(mViewRecordList.get(position).getHistoryId()).toString());
        paramsMap.put("Type", "1");
        OkhttpUtil.okHttpPost(HTTP_URL_DELHISTORY, paramsMap, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                Toast.makeText(mContext, "删除观看记录失败！", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response) throws JSONException {
                Log.d(TAG, response);
                try {
                    JSONObject obj = new JSONObject(response);
                    if(obj == null) {
                        Toast.makeText(mContext, "删除观看记录失败！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (Exception e) {
                    Toast.makeText(mContext, "删除观看记录失败！", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    JsonCommonResult resultBean = GsonUtil.fromJson(response, JsonCommonResult.class);
                    if(resultBean == null)
                        return;
                    if(resultBean.getResult().equals("0")) {
                        mViewRecordList.remove(position);
                        mViewRecordPageText.setText("共"+mCollectRecordList.size()+"部");
                        mViewRecordAdapter.notifyDataSetChanged();
                        initViewRecordListViewItems();
                    } else {
                        Toast.makeText(mContext, resultBean.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initCollectRecycler(){
        mCollectgridlayoutManager = new GridLayoutRecordManangerTv(mContext,4);
        mCollectgridlayoutManager.setOrientation(GridLayoutManagerTV.VERTICAL);
        mCollectRecordRecyclView.setLayoutManager(mCollectgridlayoutManager);
        mCollectRecordRecyclView.setFocusable(false);
        //mCollectRecordRecyclView.setSelectedItemAtCentered(true); // 设置item在中间移动
        mCollectRecordRecyclView.setSelectedItemOffset(0,(int)getDimension(R.dimen.w_200));

        mCollectgridlayoutManager.setFocusFailedAdapter(mGeneralAdapter);
        mListlayoutManager.setFocusFailedLayoutManager(mCollectgridlayoutManager);

        mRecyclerViewPresenter = new RecordPresenter(mContext, mCollectRecordList);
        mCollectRecordAdapter = new GeneralAdapter(mRecyclerViewPresenter);

        mCollectRecordAdapter.setHasStableIds(true);
        mCollectRecordRecyclView.setItemAnimator(null);
        //mCollectRecordRecyclView.getItemAnimator().setChangeDuration(0);

        mCollectRecordRecyclView.setAdapter(mCollectRecordAdapter);
        mCollectRecordRecyclView.addItemDecoration(new GuessLikeSpacing((int)getDimension(R.dimen.w_20),(int)getDimension(R.dimen.w_20),(int)getDimension(R.dimen.w_20), (int)getDimension(R.dimen.w_20)));

        mCollectgridlayoutManager.setLeftKeyListener(new GridLayoutRecordManangerTv.OnLeftKeyListener() {
            @Override
            public void onLeftKey() {
                //mGeneralAdapter.notifyDataSetChanged();
            }
        });

        mCollectgridlayoutManager.setRightKeyListener(new GridLayoutRecordManangerTv.OnRightKeyListener() {
            @Override
            public void onRightKey() {
                mCollect_button_delete.setFocusable(true);
            }
        });

        mCollectgridlayoutManager.setUpKeyListener(new GridLayoutRecordManangerTv.OnUpKeyListener() {
            @Override
            public void onUpKey(int position) {
                mCollect_button_delete.setFocusable(false);
                if(position < 4) {
                    mCollectScrollBar.setTranslationY(0);
                    return;
                }
                int iTranslation = (int)(mCollectScrollBar.getTranslationY() - mCollectScrollBarDistance);
                if(iTranslation < 0) {
                    iTranslation = 0;
                }
                mCollectScrollBar.setTranslationY(iTranslation);
            }
        });
        mCollectgridlayoutManager.setDownKeyListener(new GridLayoutRecordManangerTv.OnDownKeyListener() {
            @Override
            public void onDownKey(int position) {
                if(mCollectRecordList.size() % 4 == 0) {
                    if(position >= (mCollectRecordList.size() / 4 - 1) * 4) {
                        mCollectScrollBar.setTranslationY(mCollectScrollBg.getHeight() - mCollectScrollBar.getHeight());
                        return;
                    }
                } else {
                    if(position >= (mCollectRecordList.size() / 4) * 4) {
                        mCollectScrollBar.setTranslationY(mCollectScrollBg.getHeight() - mCollectScrollBar.getHeight());
                        return;
                    }
                }
                int iTranslation = (int)(mCollectScrollBar.getTranslationY() + mCollectScrollBarDistance);
                if(iTranslation > mCollectScrollBg.getHeight() - mCollectScrollBar.getHeight()) {
                    return;
                }
                mCollectScrollBar.setTranslationY(iTranslation);

            }
        });

        mCollectgridlayoutManager.setQuitKeyListener(new GridLayoutRecordManangerTv.OnQuitKeyListener() {
            @Override
            public void onQuitKey() {
                mCollectRecordHoverState.setVisibility(View.INVISIBLE);
                if (mCollectRecordList.size() > 4)
                    mCollectRecordRecyclView.setDefaultSelect(3);
                else
                    mCollectRecordRecyclView.setDefaultSelect(mCollectRecordList.size()-1);
                HandlerThread handlerThread = new HandlerThread("updateview");//创建一个handlerThread线程
                handlerThread.start();//启动该线程
                mHandler =  new Handler(handlerThread.getLooper()) {
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        mUIHandler.sendEmptyMessage(1);
                    }
                };

                Message msg = new Message();
                mHandler.sendMessage(msg);
            }
        });


        initCollectRecordHover();
    }
    private void initCollectRecordHover() {
        mCollectRecordHoverState = (MainUpView) findViewById(R.id.collect_record_upview);
        mCollectRecordHoverState.setEffectBridge(new RecyclerViewBridge());
        mCollectRecordHoverBridge = (RecyclerViewBridge) mCollectRecordHoverState.getEffectBridge();
        mCollectRecordHoverBridge.setUpRectResource(R.drawable.yellow_white_border);
        float density = getResources().getDisplayMetrics().density;
        RectF rectf = new RectF(getDimension(R.dimen.w_13) * density, getDimension(R.dimen.h_11) * density,
                getDimension(R.dimen.w_12) * density, getDimension(R.dimen.h_11) * density);
        mCollectRecordHoverBridge.setDrawUpRectPadding(rectf);

        mCollectRecordRecyclView.setOnItemClickListener(new RecyclerViewTV.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerViewTV parent, View itemView, int position) {
                if (COLLECT_DELETE_MODE){
                    removeCollectRecordData(position);

                }else {
                    if (mCollectRecordList.get(position).getType() == 1){
                        Intent intent = new Intent(PersonalPageNewActivity.this, DetailActivity.class);
                        intent.putExtra("libraryId",Integer.valueOf(mCollectRecordList.get(position).getLibraryID()).intValue());
                        startActivity(intent);
                    }else if (mCollectRecordList.get(position).getType() == 2){
                        Intent intent = new Intent(PersonalPageNewActivity.this, ThemeActivity.class);
                        intent.putExtra("specialid",Integer.valueOf(mCollectRecordList.get(position).getLibraryID()).intValue());
                        startActivity(intent);
                    }


                }
            }
        });

        mCollectRecordRecyclView.setOnItemListener(new RecyclerViewTV.OnItemListener() {

            @Override
            public void onItemPreSelected(RecyclerViewTV parent, View itemView, int position) {
                if (!isCollectRecordListRowPresenter()) {
                    mCollectRecordHoverBridge.setUnFocusView(mCollectRecordOldView);
                    mCollectRecordHoverBridge.setVisibleWidget(true);
                }
                ImageView deleteImage = (ImageView) itemView.findViewById(R.id.record_delete);
                ViewGroup.LayoutParams params = deleteImage.getLayoutParams();
                params.height=(int)getDimension(R.dimen.w_48);
                params.width =(int)getDimension(R.dimen.w_48);
                deleteImage.setLayoutParams(params);
                itemView.findViewWithTag("delete").setBackgroundResource(R.drawable.little_delete);
                TextView text = (TextView) itemView.findViewById(R.id.little_record_detail_name);
                text.setMaxLines(1);

            }

            @Override
            public void onItemSelected(RecyclerViewTV parent, View itemView, int position) {
                mCurrentCollectSelectPosition  = position;
                if (!isCollectRecordListRowPresenter()) {
                    if(mCollectRecordHoverBridge.isVisibleWidget()) {
                        mCollectRecordHoverBridge.setVisibleWidget(false);
                    }
                    itemView.findViewWithTag("delete").setBackgroundResource(R.drawable.big_delete);
                    ImageView deleteImage = (ImageView) itemView.findViewById(R.id.record_delete);
                    ViewGroup.LayoutParams params = deleteImage.getLayoutParams();
                    params.height=(int)getDimension(R.dimen.w_80);
                    params.width =(int)getDimension(R.dimen.w_83);
                    deleteImage.setLayoutParams(params);
                    TextView text = (TextView) itemView.findViewById(R.id.little_record_detail_name);
                    text.setMaxLines(2);
                    mCollectRecordHoverBridge.setFocusView(itemView, 1.1f);
                    mCollectRecordOldView = itemView;

                }
            }

            @Override
            public void onReviseFocusFollow(RecyclerViewTV parent, View itemView, int position) {
                if (!isCollectRecordListRowPresenter()) {
                    if(mCollectRecordHoverBridge.isVisibleWidget()) {
                        mCollectRecordHoverBridge.setVisibleWidget(false);
                    }
                    itemView.findViewWithTag("delete").setBackgroundResource(R.drawable.big_delete);
                    ImageView deleteImage = (ImageView) itemView.findViewById(R.id.record_delete);
                    ViewGroup.LayoutParams params = deleteImage.getLayoutParams();
                    params.height=(int)getDimension(R.dimen.w_80);
                    params.width =(int)getDimension(R.dimen.w_83);
                    deleteImage.setLayoutParams(params);
                    TextView text = (TextView) itemView.findViewById(R.id.little_record_detail_name);
                    text.setMaxLines(2);
                    mCollectRecordHoverBridge.setFocusView(itemView, 1.1f);
                    mCollectRecordOldView = itemView;
                }
            }
        });
    }

    private void removeCollectRecordData(final int position) {

        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put(GlobleData.HTTP_VERSION_KEY, GlobleData.HTTP_VERSION_VALUE);
        String mac = PreferencesUtil.GetAuthorValue(mContext, GlobleData.PREFERENCE_AUTHOR_STBMAC, "");
        String userID = PreferencesUtil.GetAuthorValue(mContext,GlobleData.PREFERENCE_AUTHOR_USER_ID, "");
//        mac= "00:24:68:E8:AC:0E";
//        userID=83+"";
        paramsMap.put("Mac", mac);
        paramsMap.put("UserId", userID);
        paramsMap.put("historyid", Integer.valueOf(mCollectRecordList.get(position).getHistoryId()).toString());
        OkhttpUtil.okHttpPost(HTTP_URL_DELCOLLECT, paramsMap, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                Toast.makeText(mContext, "取消收藏失败！", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response) throws JSONException {
                Log.d(TAG, response);
                try {
                    JSONObject obj = new JSONObject(response);
                    if(obj == null) {
                        Toast.makeText(mContext, "取消收藏失败！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (Exception e) {
                    Toast.makeText(mContext, "取消收藏失败！", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    JsonCommonResult resultBean = GsonUtil.fromJson(response, JsonCommonResult.class);
                    if(resultBean == null)
                        return;
                    if(resultBean.getResult().equals("0")) {
                        int count = mCollectRecordList.size();
                        mCollectRecordList.remove(position);
                        mCollectRecordPageText.setText("共"+mCollectRecordList.size()+"部");
                        if (count == 1){
                            mCollect_no_record.setVisibility(View.VISIBLE);
                            mTmp.requestFocus();
                            mCollectRecordAdapter.notifyDataSetChanged();
                        }else if (position == count-1){
                            mCollectRecordRecyclView.setDefaultSelect(mCollectRecordList.size()-1);
                            //mCollectRecordAdapter.notifyItemRangeChanged(mCollectRecordList.size()-2,mCollectRecordList.size()-1);
                            mCollectRecordAdapter.notifyDataSetChanged();

                        }else {
                            mCollectRecordAdapter.notifyItemRangeChanged(position,position+1);
                        }

                        if (mCollectRecordList.size() == 0){
                            mGeneralAdapter.notifyDataSetChanged();
                            mCollect_result.setVisibility(View.INVISIBLE);
                            mCollect_no_record.setVisibility(View.VISIBLE);
                        }
                        initCollectListViewItems();
                    } else {
                        Toast.makeText(mContext, resultBean.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void initPurchaseRecycler() {
        LinearLayoutManagerPurchase layoutManager = new LinearLayoutManagerPurchase(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setSmoothScrollbarEnabled(false);
        mPurchase_recycler_view.setLayoutManager(layoutManager);
        mPurchase_recycler_view.setFocusable(false);

        layoutManager.setFocusFailedAdapter(mGeneralAdapter);

        PurchasePresenter mRecyclerViewPresenter = new PurchasePresenter(mContext, mPurchase_list);
        mPurchaseAdapter = new GeneralAdapter(mRecyclerViewPresenter);
        mPurchase_recycler_view.setAdapter(mPurchaseAdapter);

        mPurchase_recycler_view.addItemDecoration(new LessonMenuSpacing(0, 0, 0, (int)getDimension(R.dimen.w_10)));


        mRecyclerViewPresenter.setOnItemSelectListener(new PurchasePresenter.OnItemSelectListener() {
            @Override
            public void onItemSelect(View view, int position) {

            }
        });

        layoutManager.setDownKeyListener(new LinearLayoutManagerPurchase.OnDownKeyListener() {
            @Override
            public void onDownKey() {
            }
        });

        layoutManager.setUpKeyListener(new LinearLayoutManagerPurchase.OnUpKeyListener() {
            @Override
            public void onUpKey() {
            }
        });

        layoutManager.setLeftKeyListener(new LinearLayoutManagerPurchase.OnLeftKeyListener() {
            @Override
            public void onLeftKey() {
                //mGeneralAdapter.notifyDataSetChanged();
            }
        });
    }

    private void switchRecordPage(int position){
        switch (position){
            case 0:
                personal_mode = VIEW_RECORD_MODE;
                mView_record.setVisibility(View.VISIBLE);
                mCollectRecord.setVisibility(View.GONE);
                mPurchuse_record_lauout.setVisibility(View.GONE);
                mConnect_us_layout.setVisibility(View.GONE);
                break;
            case 1:
                personal_mode = COLLECT_RECORD_MODE;
                mView_record.setVisibility(View.GONE);
                mCollectRecord.setVisibility(View.VISIBLE);
                mPurchuse_record_lauout.setVisibility(View.GONE);
                mConnect_us_layout.setVisibility(View.GONE);
                //initListViewItems();
                break;

            case 2:
                personal_mode = PURCHASE_MODE;
                mView_record.setVisibility(View.GONE);
                mCollectRecord.setVisibility(View.GONE);
                mPurchuse_record_lauout.setVisibility(View.VISIBLE);
                mConnect_us_layout.setVisibility(View.GONE);
                break;
            case 3:
                personal_mode = CONNECT_US_MODE;
                mView_record.setVisibility(View.GONE);
                mCollectRecord.setVisibility(View.GONE);
                mPurchuse_record_lauout.setVisibility(View.GONE);
                mConnect_us_layout.setVisibility(View.VISIBLE);
                break;
        }
    }


    public float getDimension(int id) {
        return getResources().getDimension(id);
    }



    public class MsgReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if(GlobleData.MESSAGE_UPDATE_TIME.equals(intent.getAction())) {
                    String sTimeData = intent.getStringExtra("time");
                    dateTextView.setText(sTimeData.split(" ")[0]);
                    timeTextView.setText(sTimeData.split(" ")[1]);
                    weekTextView.setText(sTimeData.split(" ")[2]);
                }

                if (GlobleData.MESSAGE_UPDATE_DETAIL_DATA.equals(intent.getAction())){
                    getPersonalDataFromHttp();
                }

            } catch (Exception e) {
                Log.e(TAG, "" + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
