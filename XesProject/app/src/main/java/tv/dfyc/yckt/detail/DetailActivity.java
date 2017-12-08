package tv.dfyc.yckt.detail;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.open.androidtvwidget.bridge.EffectNoDrawBridge;
import com.open.androidtvwidget.bridge.RecyclerViewBridge;
import com.open.androidtvwidget.leanback.adapter.GeneralAdapter;
import com.open.androidtvwidget.leanback.mode.ListRowPresenter;
import com.open.androidtvwidget.leanback.mode.OpenPresenter;
import com.open.androidtvwidget.leanback.recycle.RecyclerViewTV;
import com.open.androidtvwidget.utils.Utils;
import com.open.androidtvwidget.view.MainUpView;
import tv.dfyc.yckt.R;
import tv.dfyc.yckt.adapter.GeneralSelectClassAdapter;
import tv.dfyc.yckt.adapter.SelectClassGirdLayoutmanager;
import tv.dfyc.yckt.adapter.selectCoursePresenter;
import tv.dfyc.yckt.custom.RoundImageView;
import tv.dfyc.yckt.network.CallBackUtil;
import tv.dfyc.yckt.network.GsonUtil;
import tv.dfyc.yckt.network.OkhttpUtil;
import tv.dfyc.yckt.util.PreferencesUtil;
import tv.dfyc.yckt.util.GlobleData;
import tv.dfyc.yckt.custom.GuessLikeSpacing;
import tv.dfyc.yckt.beans.JsonCommonResult;
import tv.dfyc.yckt.beans.JsonAddCollect;
import tv.dfyc.yckt.beans.JsonGoodsListData;
import tv.dfyc.yckt.beans.JsonLibraryIDData;
import tv.dfyc.yckt.beans.JsonVideoDetailData;
import tv.dfyc.yckt.custom.LessonMenuSpacing;
import tv.dfyc.yckt.custom.ListPageLayout;
import tv.dfyc.yckt.custom.SelectClassLinearLayoutManager;
import tv.dfyc.yckt.util.XESUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;

import static tv.dfyc.yckt.util.GlobleData.HTTP_PARAM_LIBRARYID;
import static tv.dfyc.yckt.util.GlobleData.HTTP_URL_ADDCOLLECT;
import static tv.dfyc.yckt.util.GlobleData.HTTP_URL_CHANNEL_DETAIL;
import static tv.dfyc.yckt.util.GlobleData.HTTP_URL_DELCOLLECT;
import static tv.dfyc.yckt.util.GlobleData.HTTP_VERSION_KEY;
import static tv.dfyc.yckt.util.GlobleData.HTTP_VERSION_VALUE;

/**
 * Created by Administrator on 2017/7/20 0020.
 */

public class DetailActivity extends Activity {
    private final static String TAG="DetailActivity";
    private Context mContext;
    private TextView mDetailPageTitle;
    private TextView mDetailPageInfo;
    private Button mBuyButton;     // 订购按钮
    private Button mPlayButton;    // 播放按钮
    private Button mSaveButton;    // 收藏按钮
    private Button mLessonsButton; // 选集按钮
    private boolean mFirstRun = true;
    private JsonVideoDetailData mDetailJsonData = null;
    private JsonGoodsListData mGoodsListJsonData;
    private int mLibraryId;
    private View mOldFocus; // 4.3以下版本需要自己保存.

    private TextView mDetail_page_name;
    private RoundImageView mDetail_page_image;

    ////////////////////////guess like/////////////////////////
    private RelativeLayout mGuessLikeLayout;
    private RelativeLayout mGuessLike0;
    private RoundImageView mGuessLikeImage0;
    private TextView mGuessLikeName0;
    private ImageView mGuessLikeFree0;
    private RelativeLayout mGuessLike1;
    private RoundImageView mGuessLikeImage1;
    private TextView mGuessLikeName1;
    private ImageView mGuessLikeFree1;
    private RelativeLayout mGuessLike2;
    private RoundImageView mGuessLikeImage2;
    private TextView mGuessLikeName2;
    private ImageView mGuessLikeFree2;
    private RelativeLayout mGuessLike3;
    private RoundImageView mGuessLikeImage3;
    private TextView mGuessLikeName3;
    private ImageView mGuessLikeFree3;
    private RelativeLayout mGuessLike4;
    private RoundImageView mGuessLikeImage4;
    private TextView mGuessLikeName4;
    private ImageView mGuessLikeFree4;
    private RelativeLayout mGuessLike5;
    private RoundImageView mGuessLikeImage5;
    private TextView mGuessLikeName5;
    private ImageView mGuessLikeFree5;
    private RelativeLayout mGuessLike6;
    private RoundImageView mGuessLikeImage6;
    private TextView mGuessLikeName6;
    private ImageView mGuessLikeFree6;

    private TextView mDetailTag1;
    private TextView mDetailTag2;
    private TextView mDetailTag3;

    private TextView mWeekTextView;
    private TextView mTimeTextView;
    private TextView mDateTextView;
    private MsgReceiver mMsgReceiver;

    private RelativeLayout mSelectLessonLayout;
    private TextView mTotalLessons;
    private RecyclerViewTV mSelect_class_info_list;
    private RecyclerViewTV mClass_detail_list;
    private MainUpView mClass_detail_upview;
    private RecyclerViewBridge mClassHoverBridge;
    private View mOldView;

    private int mHaveOrder = 0;
    private int mCollectID = 0;

    private GeneralSelectClassAdapter mGeneralAdapter;
    private ArrayList<String> mTextList  = new ArrayList<String>();
    private ArrayList<HashMap<String, Object>> mSelectDetailName = new ArrayList<HashMap<String, Object>>();
    private ArrayList<HashMap<String, Object>> mUpdateSelectDetailName = new ArrayList<>();
    private  GeneralAdapter mClassGeneralAdapter;
    private MainUpView mainUpView1;
    private TextView mPerson;
    private ListPageLayout mButton_relativeLayout;
    private Boolean mIsInSelectClass = false;

    private boolean mOtherCall;
    private Timer mTimer;
    private TimerTask mTimerTask;
    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    String sTimeData = msg.getData().getString("time");
                    mDateTextView.setText(sTimeData.split(" ")[0]);
                    mTimeTextView.setText(sTimeData.split(" ")[1]);
                    mWeekTextView.setText(sTimeData.split(" ")[2]);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mHaveOrder = 0;
        setContentView(R.layout.activity_detail_page);
        initView();

        Intent intent = this.getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                String sContentID = bundle.getString("ContentID");
                if (sContentID != null && !sContentID.isEmpty()) { // 第三方调用的逻辑
                    mOtherCall = true;
                    setTimeData();
                    getLibraryIDByContentID(sContentID);
                } else { // 应用内自己调用的逻辑
                    mLibraryId = bundle.getInt("libraryId");
                    boolean bOtherCall = bundle.getBoolean("otherCall");
                    if(PreferencesUtil.GetAuthorValue(mContext,GlobleData.PREFERENCE_DETAIL_PAGE_LIBRARY_ID, "").isEmpty())
                        PreferencesUtil.PutAuthorValue(mContext, GlobleData.PREFERENCE_DETAIL_PAGE_LIBRARY_ID, String.valueOf(mLibraryId));
                    getDetailDataFromHttp(mLibraryId);
                    if(bOtherCall) {
                        setTimeData();
                    }
                }
            }
        }

        mMsgReceiver = new MsgReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(GlobleData.MESSAGE_UPDATE_TIME);
        intentFilter.addAction(GlobleData.MESSAGE_ORDER_FINISH);
        intentFilter.addAction(GlobleData.MESSAGE_UPDATE_DETAIL_DATA);
        registerReceiver(mMsgReceiver, intentFilter);

        initSelectClassList();
        initClassRecycler();

        initGuessLikeList();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        unregisterReceiver(mMsgReceiver);
        if(mTimer != null)
            mTimer.cancel();
    }

    @Override
    public void onBackPressed() {
        String first_library_id = PreferencesUtil.GetAuthorValue(mContext,GlobleData.PREFERENCE_DETAIL_PAGE_LIBRARY_ID, "");
        if(!first_library_id.equals(String.valueOf(mLibraryId)))
            fromGuessLikeToDetail(Integer.valueOf(first_library_id));
        else {
            PreferencesUtil.PutAuthorValue(mContext, GlobleData.PREFERENCE_DETAIL_PAGE_LIBRARY_ID, "");
            super.onBackPressed();
        }
    }

    private void getDetailDataFromHttp(int library_id) {
        // 保存当前知识点ID（重要）
        mLibraryId = library_id;

        String libraryID = library_id +"";
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put(HTTP_VERSION_KEY,HTTP_VERSION_VALUE);
        paramsMap.put(HTTP_PARAM_LIBRARYID,libraryID);
        String userID = PreferencesUtil.GetAuthorValue(mContext,GlobleData.PREFERENCE_AUTHOR_USER_ID, "");
        String mac = PreferencesUtil.GetAuthorValue(mContext, GlobleData.PREFERENCE_AUTHOR_STBMAC, "");
//        mac= "00:24:68:E8:AC:0E";
//        userID=83+"";
        paramsMap.put("Mac", mac);
        paramsMap.put("UserId", userID);
        Log.d("libraryID=",libraryID);

        OkhttpUtil.okHttpGet(HTTP_URL_CHANNEL_DETAIL, paramsMap, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                Log.d(TAG,"连接失败！");
                Toast.makeText(mContext, "连接失败！", Toast.LENGTH_SHORT).show();
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
                    mDetailJsonData = GsonUtil.fromJson(response, JsonVideoDetailData.class);
                    if(mDetailJsonData == null)
                        return;
                    if(mDetailJsonData.getResult() == 0) {
                        // 详情页左上角标题
                        mDetailPageTitle.setText(mDetailJsonData.getData().getUrl());

                        // 详情页标签
                        try {
                            if(!mDetailJsonData.getData().getTags().isEmpty()) {
                                String[] sTags = mDetailJsonData.getData().getTags().split("\\|");
                                if(sTags != null && sTags.length > 0) {
                                    mDetailTag1.setVisibility(View.VISIBLE);
                                    mDetailTag1.setText(sTags[0]);
                                }
                                if(sTags != null && sTags.length > 1) {
                                    mDetailTag2.setVisibility(View.VISIBLE);
                                    mDetailTag2.setText(sTags[1]);
                                }
                                if(sTags != null && sTags.length > 2) {
                                    mDetailTag3.setVisibility(View.VISIBLE);
                                    mDetailTag3.setText(sTags[2]);
                                }
                            }

                            mPerson.setText("主讲人："+ mDetailJsonData.getData().getMain_speaker());

                            setPvValue();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        // 详情页是否收藏，是否购买
                        int isCollect = mDetailJsonData.getData().getCollect();
                        if (isCollect == 1) {
                            mCollectID = mDetailJsonData.getData().getHistoryid();
                            mSaveButton.setText("已收藏");
                        }
                        else
                            mSaveButton.setText("收藏");

                        mDetail_page_name.setText(mDetailJsonData.getData().getTitle());
                        mDetailPageInfo.setText(mDetailJsonData.getData().getDescription());
                        Glide.with(mContext).load(mDetailJsonData.getData().getThumb_app_h()).centerCrop().into(mDetail_page_image);

                        // 处理订购（是否收费）
                        getProductList();

                        //选课
                        mTextList.clear();
                        mSelectDetailName.clear();
                        mUpdateSelectDetailName.clear();
                        for (int i = 0; i< mDetailJsonData.getData().getVideo_list().size(); i++){
                            HashMap<String, Object> map = new HashMap<String, Object>();
                            map.put("select_class_name", mDetailJsonData.getData().getVideo_list().get(i).getVideo_name());
                            map.put("select_class_durtion", mDetailJsonData.getData().getVideo_list().get(i).getVideo_duration());
                            map.put("select_class_library_id", mDetailJsonData.getData().getVideo_list().get(i).getLibrary_id());
                            map.put("select_class_code", mDetailJsonData.getData().getVideo_list().get(i).getCode());
                            map.put("select_class_library_detail_id", mDetailJsonData.getData().getVideo_list().get(i).getLibrary_detail_id());
                            map.put("select_class_playurl", mDetailJsonData.getData().getVideo_list().get(i).getPlayurl());
                            map.put("select_class_video", mDetailJsonData.getData().getVideo_list().get(i).getVideo());
                            map.put("select_class_isfree",mDetailJsonData.getData().getVideo_list().get(i).getVideo_is_free());
                            map.put("select_point_isfree",mDetailJsonData.getData().getIs_free());
                            mSelectDetailName.add(map);
                        }
                        if (mSelectDetailName.size()!=0){
                            addSelectClassData();
                        }

                        // 猜你喜欢
                        setGuessLikeData();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setPvValue() {
        int value = mDetailJsonData.getData().getPv();
        if (value  >= 10000){
            int b = value /10000;
            mTotalLessons.setText("已有" + b+ "万+观看");
        }else{
            int a = value /100;
            mTotalLessons.setText("已有" + a*100+ "+观看");
        }
    }

    private void addSelectClassData() {
        int Total = mSelectDetailName.size()/8 + (mSelectDetailName.size() % 8 == 0 ? 0:1);
        int left = 1;
        int right = Math.min(8,mSelectDetailName.size());

        for (int j = 0;j < Total;j++){
            mTextList.add(left +"-"+right);
            left = right+1;
            right = Math.min((j+2)*8,mSelectDetailName.size());
        }
        int minN = Math.min(8,mSelectDetailName.size());
        for (int i = 0; i< minN;i++){
            mUpdateSelectDetailName.add(mSelectDetailName.get(i));
        }
        mGeneralAdapter.notifyDataSetChanged();
        mClassGeneralAdapter.notifyDataSetChanged();
    }

    private void getLibraryIDByContentID(String content_id) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put(HTTP_VERSION_KEY,HTTP_VERSION_VALUE);
        paramsMap.put("ContentID",content_id);
        Log.d(TAG, content_id);

        OkhttpUtil.okHttpGet(GlobleData.HTTP_URL_LIBRARYID, paramsMap, new CallBackUtil.CallBackString() {
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
                        Toast.makeText(mContext, "获取资源失败！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (Exception e) {
                    Toast.makeText(mContext, "获取资源失败！", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    JsonLibraryIDData resultBean= GsonUtil.fromJson(response, JsonLibraryIDData.class);
                    if(resultBean == null)
                        return;
                    if(resultBean.getResult().equals("0")) {
                        mLibraryId = Integer.valueOf(resultBean.getData()).intValue();
                        PreferencesUtil.PutAuthorValue(mContext, GlobleData.PREFERENCE_DETAIL_PAGE_LIBRARY_ID, String.valueOf(mLibraryId));
                        getDetailDataFromHttp(mLibraryId);
                    } else
                        Toast.makeText(mContext, resultBean.getMessage(), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void switchNoDrawBridgeVersion() {
        float density = getResources().getDisplayMetrics().density;
        RectF rectf = new RectF(getDimension(R.dimen.w_10) * density, getDimension(R.dimen.h_10) * density,
                getDimension(R.dimen.w_9) * density, getDimension(R.dimen.h_9) * density);
        EffectNoDrawBridge effectNoDrawBridge = new EffectNoDrawBridge();
        effectNoDrawBridge.setTranDurAnimTime(200);
        mainUpView1.setEffectBridge(effectNoDrawBridge);
        mainUpView1.setUpRectResource(R.drawable.white_light_10);
        mainUpView1.setDrawUpRectPadding(rectf);
    }

    private void switchDrawBridgeVersion() {
        float density = getResources().getDisplayMetrics().density;
        RectF rectf = new RectF(getDimension(R.dimen.w_10) * density, getDimension(R.dimen.h_10) * density,
                getDimension(R.dimen.w_9) * density, getDimension(R.dimen.h_9) * density);
        EffectNoDrawBridge effectNoDrawBridge = new EffectNoDrawBridge();
        effectNoDrawBridge.setTranDurAnimTime(200);
        mainUpView1.setEffectBridge(effectNoDrawBridge);
        mainUpView1.setUpRectResource(R.drawable.yellow_white_border);
        mainUpView1.setDrawUpRectPadding(rectf);
    }

    private void initMainLayoutFocus() {
        mainUpView1 = (MainUpView) findViewById(R.id.mainUpView1);

        if (Utils.getSDKVersion() == 17) { // 测试 android 4.2版本.
            switchNoDrawBridgeVersion();
        } else {
            switchDrawBridgeVersion();
        }

        RelativeLayout ll = (RelativeLayout) findViewById(R.id.detail_image);
        ll.getViewTreeObserver().addOnGlobalFocusChangeListener(new ViewTreeObserver.OnGlobalFocusChangeListener() {
            @Override
            public void onGlobalFocusChanged(final View oldFocus, final View newFocus) {
                if (newFocus != null)
                    newFocus.bringToFront(); // 防止放大的view被压在下面. (建议使用MainLayout)

                float scale = 1.2f;

                if(mFirstRun) {
                    mFirstRun = false;
                    if(newFocus == mGuessLike0) {
                        mPlayButton.requestFocus();
                        mainUpView1.setFocusView(mPlayButton, mOldFocus, scale);
                        mOldFocus = mPlayButton;
                        return;
                    }
                }
                setTextUnSelected();

                if(newFocus == mPlayButton || newFocus == mBuyButton || newFocus == mSaveButton) {
                    mIsInSelectClass = false;
                    mGuessLikeLayout.setVisibility(View.VISIBLE);
                    mSelectLessonLayout.setVisibility(View.GONE);
                    mainUpView1.setUpRectResource(R.drawable.yellow_white_border);
                } else if(newFocus == mLessonsButton){
                    mIsInSelectClass = true;
                    mGuessLikeLayout.setVisibility(View.INVISIBLE);
                    mSelectLessonLayout.setVisibility(View.VISIBLE);
                    mainUpView1.setUpRectResource(R.drawable.yellow_white_border);
                } else if(newFocus == mGuessLike0) {
                    mIsInSelectClass = false;
                    mainUpView1.setUpRectResource(R.drawable.yellow_white_border);
                    mGuessLikeName0.setSelected(true);
                } else if(newFocus == mGuessLike1) {
                    mIsInSelectClass = false;
                    mainUpView1.setUpRectResource(R.drawable.yellow_white_border);
                    mGuessLikeName1.setSelected(true);
                }else if(newFocus == mGuessLike2) {
                    mIsInSelectClass = false;
                    mainUpView1.setUpRectResource(R.drawable.yellow_white_border);
                    mGuessLikeName2.setSelected(true);
                } else if(newFocus == mGuessLike3) {
                    mIsInSelectClass = false;
                    mainUpView1.setUpRectResource(R.drawable.yellow_white_border);
                    mGuessLikeName3.setSelected(true);
                } else if(newFocus == mGuessLike4) {
                    mIsInSelectClass = false;
                    mainUpView1.setUpRectResource(R.drawable.yellow_white_border);
                    mGuessLikeName4.setSelected(true);
                } else if(newFocus == mGuessLike5) {
                    mIsInSelectClass = false;
                    mainUpView1.setUpRectResource(R.drawable.yellow_white_border);
                    mGuessLikeName5.setSelected(true);
                } else if(newFocus == mGuessLike6) {
                    mIsInSelectClass = false;
                    mainUpView1.setUpRectResource(R.drawable.yellow_white_border);
                    mGuessLikeName6.setSelected(true);
                } else {
                    scale = 1.0f;
                    mainUpView1.setUpRectResource(R.color.transparent);
                }
                mButton_relativeLayout.setIsInSelect(mIsInSelectClass);
                mainUpView1.setFocusView(newFocus, mOldFocus, scale);
                mOldFocus = newFocus; // 4.3以下需要自己保存.
            }
        });

    }

    private void setTextUnSelected() {
        mGuessLikeName0.setSelected(false);
        mGuessLikeName1.setSelected(false);
        mGuessLikeName2.setSelected(false);
        mGuessLikeName3.setSelected(false);
        mGuessLikeName4.setSelected(false);
        mGuessLikeName5.setSelected(false);
        mGuessLikeName6.setSelected(false);
    }

    private void initGuessLikeList() {
        mPerson = (TextView)findViewById(R.id.person);
        mGuessLike0 = (RelativeLayout) findViewById(R.id.guess_like_layout_0);
        mGuessLikeImage0 = (RoundImageView) findViewById(R.id.guess_like_image_0);
        mGuessLikeName0 = (TextView) findViewById(R.id.guess_like_name_0);
        mGuessLikeFree0 = (ImageView) findViewById(R.id.guess_like_free_0);
        mGuessLike1 = (RelativeLayout) findViewById(R.id.guess_like_layout_1);
        mGuessLikeImage1 = (RoundImageView) findViewById(R.id.guess_like_image_1);
        mGuessLikeName1 = (TextView) findViewById(R.id.guess_like_name_1);
        mGuessLikeFree1 = (ImageView) findViewById(R.id.guess_like_free_1);
        mGuessLike2 = (RelativeLayout) findViewById(R.id.guess_like_layout_2);
        mGuessLikeImage2 = (RoundImageView) findViewById(R.id.guess_like_image_2);
        mGuessLikeName2 = (TextView) findViewById(R.id.guess_like_name_2);
        mGuessLikeFree2 = (ImageView) findViewById(R.id.guess_like_free_2);
        mGuessLike3 = (RelativeLayout) findViewById(R.id.guess_like_layout_3);
        mGuessLikeImage3 = (RoundImageView) findViewById(R.id.guess_like_image_3);
        mGuessLikeName3 = (TextView) findViewById(R.id.guess_like_name_3);
        mGuessLikeFree3 = (ImageView) findViewById(R.id.guess_like_free_3);
        mGuessLike4 = (RelativeLayout) findViewById(R.id.guess_like_layout_4);
        mGuessLikeImage4 = (RoundImageView) findViewById(R.id.guess_like_image_4);
        mGuessLikeName4 = (TextView) findViewById(R.id.guess_like_name_4);
        mGuessLikeFree4 = (ImageView) findViewById(R.id.guess_like_free_4);
        mGuessLike5 = (RelativeLayout) findViewById(R.id.guess_like_layout_5);
        mGuessLikeImage5 = (RoundImageView) findViewById(R.id.guess_like_image_5);
        mGuessLikeName5 = (TextView) findViewById(R.id.guess_like_name_5);
        mGuessLikeFree5 = (ImageView) findViewById(R.id.guess_like_free_5);
        mGuessLike6 = (RelativeLayout) findViewById(R.id.guess_like_layout_6);
        mGuessLikeImage6 = (RoundImageView) findViewById(R.id.guess_like_image_6);
        mGuessLikeName6 = (TextView) findViewById(R.id.guess_like_name_6);
        mGuessLikeFree6 = (ImageView) findViewById(R.id.guess_like_free_6);

        mGuessLike0.setVisibility(View.INVISIBLE);
        mGuessLike1.setVisibility(View.INVISIBLE);
        mGuessLike2.setVisibility(View.INVISIBLE);
        mGuessLike3.setVisibility(View.INVISIBLE);
        mGuessLike4.setVisibility(View.INVISIBLE);
        mGuessLike5.setVisibility(View.INVISIBLE);
        mGuessLike6.setVisibility(View.INVISIBLE);
        setTextUnSelected();
        guessLikeManage();
    }

    private void firstPositionSelected() {
        new Handler().postDelayed(new Runnable(){
            public void run() {
                mPlayButton.requestFocus();
            }
        }, 200);
    }

    private void initView(){
        mButton_relativeLayout = (ListPageLayout)findViewById(R.id.button_relativeLayout);
        mClass_detail_list = (RecyclerViewTV)findViewById(R.id.class_detail_list);
        mDetailPageTitle = (TextView) findViewById(R.id.detail_page_title);
        mDetailPageInfo = (TextView) findViewById(R.id.detail_page_info);
        mBuyButton = (Button) findViewById(R.id.detail_page_button_buy);
        mPlayButton = (Button) findViewById(R.id.detail_page_button_play);
        mSaveButton = (Button) findViewById(R.id.detail_page_button_save);
        mLessonsButton = (Button) findViewById(R.id.detail_page_button_lessons);
        mDetail_page_name = (TextView)findViewById(R.id.detail_page_name);
        mDetail_page_image = (RoundImageView)findViewById(R.id.detail_page_image);
        mWeekTextView = (TextView) findViewById(R.id.week);
        mTimeTextView = (TextView) findViewById(R.id.time);
        mDateTextView = (TextView) findViewById(R.id.date);
        mDetailTag1 = (TextView) findViewById(R.id.detail_lable_tag1);
        mDetailTag2 = (TextView) findViewById(R.id.detail_lable_tag2);
        mDetailTag3 = (TextView) findViewById(R.id.detail_lable_tag3);
        mTotalLessons = (TextView) findViewById(R.id.detail_page_total_lessons_text);
        mGuessLikeLayout = (RelativeLayout) findViewById(R.id.detail_page_guess_like_layout);
        mSelectLessonLayout = (RelativeLayout) findViewById(R.id.select_class_layout);
        mSelect_class_info_list = (RecyclerViewTV)findViewById(R.id.select_class_info_list);

        initMainLayoutFocus();
        initGuessLikeList();
        firstPositionSelected();

        mBuyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mBuyButton.getText().toString().equals("免费")) {
                    //Toast.makeText(mContext, "免费商品，可直接观看！", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    if(mGoodsListJsonData != null && mGoodsListJsonData.getResult().equals("0")) {
                        Intent intent;
                        if(mGoodsListJsonData.getData() != null && mGoodsListJsonData.getData().size() > 0) {
                            if (mGoodsListJsonData.getData().size() == 1) {
                                if(mGoodsListJsonData.getData().get(0) == null) {
                                    Toast.makeText(mContext, "获取商品信息失败！", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if(mGoodsListJsonData.getData().get(0).getGoodstype() == 2)
                                    intent = new Intent(mContext, BuyThemeActivity.class);
                                else
                                    intent = new Intent(mContext, BuyListActivity.class);
                            } else {
                                intent = new Intent(mContext, BuyListActivity.class);
                            }
                            intent.putExtra("productData", (Serializable) mGoodsListJsonData);
                            intent.putExtra("libraryId", mLibraryId);
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
        });

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSaveButton.getText().equals("收藏")) {
                    Map<String, String> paramsMap = new HashMap<String, String>();
                    paramsMap.put(GlobleData.HTTP_VERSION_KEY, GlobleData.HTTP_VERSION_VALUE);
                    paramsMap.put("collectId", Integer.toString(mLibraryId));
                    String mac = PreferencesUtil.GetAuthorValue(mContext, GlobleData.PREFERENCE_AUTHOR_STBMAC, "");
                    String userID = PreferencesUtil.GetAuthorValue(mContext,GlobleData.PREFERENCE_AUTHOR_USER_ID, "");
//                    mac= "00:24:68:E8:AC:0E";
//                    userID=83+"";
                    paramsMap.put("Mac", mac);
                    paramsMap.put("UserId", userID);
                    paramsMap.put("type", "1"); // 1: 知识点 2：专题
                    OkhttpUtil.okHttpPost(HTTP_URL_ADDCOLLECT, paramsMap, new CallBackUtil.CallBackString() {
                        @Override
                        public void onFailure(Call call, Exception e) {
                            Toast.makeText(mContext, "收藏失败！", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onResponse(String response) throws JSONException {
                            Log.d(TAG, response);
                            try {
                                JSONObject obj = new JSONObject(response);
                                if(obj == null) {
                                    Toast.makeText(mContext, "收藏失败！", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            } catch (Exception e) {
                                Toast.makeText(mContext, "收藏失败！", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            try {
                                JsonAddCollect resultBean = GsonUtil.fromJson(response, JsonAddCollect.class);
                                if(resultBean == null)
                                    return;
                                if(resultBean.getResult().equals("0")) {
                                    mCollectID = resultBean.getData().getHistoryid();
                                    mSaveButton.setText("已收藏");
                                }
                                Toast.makeText(mContext, resultBean.getMessage(), Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } else {
                    if(mCollectID == 0) {
                        Toast.makeText(mContext, "取消收藏失败！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Map<String, String> paramsMap = new HashMap<String, String>();
                    paramsMap.put(GlobleData.HTTP_VERSION_KEY, GlobleData.HTTP_VERSION_VALUE);
                    paramsMap.put("historyid", Integer.toString(mCollectID));
                    String mac = PreferencesUtil.GetAuthorValue(mContext, GlobleData.PREFERENCE_AUTHOR_STBMAC, "");
                    String userID = PreferencesUtil.GetAuthorValue(mContext,GlobleData.PREFERENCE_AUTHOR_USER_ID, "");
                    paramsMap.put("Mac", mac);
                    paramsMap.put("UserId", userID);
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
                                if(resultBean.getResult().equals("0"))
                                    mSaveButton.setText("收藏");
                                Toast.makeText(mContext, resultBean.getMessage(), Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });

        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int iContinueIndex = -1;
                if (mDetailJsonData != null && mDetailJsonData.getData().getVideo_list().size() > 0) {
                    for (int i = 0; i < mDetailJsonData.getData().getVideo_list().size(); ++i) {
                        if (mDetailJsonData.getData().getLibrary_detail_id() ==
                                mDetailJsonData.getData().getVideo_list().get(i).getLibrary_detail_id()) {
                            iContinueIndex = i;
                            break;
                        }
                    }
                    if(iContinueIndex == -1) { // 无观看记录，从第一集开始播

                        if (mHaveOrder != 1){
                            if (mDetailJsonData.getData().getVideo_list().get(0).getVideo_is_free() == 2){
                                start_video_activity_from_selectcalss(0,0);
                            }else {
                                sentToGoodlPage();
                                return;
                            }

                        }else{
                            start_video_activity_from_selectcalss(0,0);
                        }
                        return;
                    } else {

                        final Dialog builder = new Dialog(DetailActivity.this, R.style.dialogTransparent);
                        RelativeLayout loginDialog= (RelativeLayout) getLayoutInflater().inflate(R.layout.pop_dialog_continue_play,null);
                        Button mContinue = (Button)loginDialog.findViewById(R.id.pop_continue);
                        Button mRestart =  (Button)loginDialog.findViewById(R.id.pop_restart);
                        TextView mPlayTimeTextView = (TextView) loginDialog.findViewById(R.id.play_time_text);

                        if (mHaveOrder != 1){
                            if (mDetailJsonData.getData().getVideo_list().get(iContinueIndex).getVideo_is_free() == 2){
                                //免费
                                mPlayTimeTextView.setText("您已经观看到" + mDetailJsonData.getData().getTitle()+"知识点第"+(iContinueIndex+1)+"集的"+XESUtil.stringForTime(mDetailJsonData.getData().getLast_play_time() * 1000) + "分钟，是否继续观看？");
                                builder.setContentView(loginDialog);
                                builder.setCancelable(true);
                                builder.show();
                                mContinue.requestFocus();

                                final int iIndex = iContinueIndex;

                                mContinue.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        // 对剧集进行鉴权
                                        String durtion = mDetailJsonData.getData().getVideo_list().get(iIndex).getVideo_duration();
                                        int lastPlayTime = mDetailJsonData.getData().getLast_play_time();
                                        if (!durtion.equals("")){
                                            if (Integer.valueOf(durtion).intValue() - lastPlayTime < 5){
                                                if (iIndex != mDetailJsonData.getData().getVideo_list().size()-1){
                                                    if(mDetailJsonData.getData().getVideo_list().get(iIndex+1).getVideo_is_free() == 2){
                                                        playVideoAuthor(iIndex+1, 0);
                                                    }else {
                                                        //跳转到订购页面
                                                        sentToGoodlPage();
                                                        builder.dismiss();
                                                        return;
                                                    }
                                                }else{
                                                    if(mDetailJsonData.getData().getVideo_list().get(0).getVideo_is_free() == 2){
                                                        playVideoAuthor(0, 0);
                                                    }else{
                                                        //跳转到订购页面
                                                        sentToGoodlPage();
                                                    }
                                                    builder.dismiss();
                                                    return;
                                                }
                                            }else{
                                                playVideoAuthor(iIndex, Integer.valueOf(mDetailJsonData.getData().getLast_play_time()));
                                            }

                                        }else {
                                            playVideoAuthor(iIndex, Integer.valueOf(mDetailJsonData.getData().getLast_play_time()));
                                        }
                                        builder.dismiss();
                                    }
                                });

                                mRestart.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        // 对剧集进行鉴权
                                        playVideoAuthor(iIndex, 0);
                                        builder.dismiss();
                                    }
                                });

                            }else {
                                //不免费
                                //跳转到订购页面
                                sentToGoodlPage();
                                return;
                            }
                        }else {
                            mPlayTimeTextView.setText("您已经观看到" + mDetailJsonData.getData().getTitle()+"知识点第"+(iContinueIndex+1)+"集的"+XESUtil.stringForTime(mDetailJsonData.getData().getLast_play_time() * 1000) + "分钟，是否继续观看？");
                            builder.setContentView(loginDialog);
                            builder.setCancelable(true);
                            builder.show();
                            mContinue.requestFocus();

                            final int iIndex = iContinueIndex;

                            mContinue.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    String durtion = mDetailJsonData.getData().getVideo_list().get(iIndex).getVideo_duration();
                                    int lastPlayTime = mDetailJsonData.getData().getLast_play_time();
                                    // 对剧集进行鉴权
                                    if (!durtion.equals("")) {
                                        if (iIndex == mDetailJsonData.getData().getVideo_list().size() - 1) {
                                            if (Integer.valueOf(durtion).intValue() - lastPlayTime < 5){
                                                //播放下一集,从头开始播放
                                                playVideoAuthor(0, 0);
                                            }else {
                                                playVideoAuthor(iIndex,Integer.valueOf(mDetailJsonData.getData().getLast_play_time()));
                                            }

                                        }else {
                                            if (Integer.valueOf(durtion).intValue() - lastPlayTime < 5){
                                                //播放下一集
                                                playVideoAuthor(iIndex+1, 0);
                                            }else {
                                                playVideoAuthor(iIndex,Integer.valueOf(mDetailJsonData.getData().getLast_play_time()));
                                            }
                                        }
                                    }else {
                                        playVideoAuthor(iIndex,Integer.valueOf(mDetailJsonData.getData().getLast_play_time()));
                                    }
                                    builder.dismiss();
                                }
                            });

                            mRestart.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    // 对剧集进行鉴权
                                    playVideoAuthor(iIndex, 0);
                                    builder.dismiss();
                                }
                            });
                        }
                    }
                } else {
                    Toast.makeText(mContext, "获取播放信息失败！", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }

    private void sentToGoodlPage() {
        try {
            if(mGoodsListJsonData != null && mGoodsListJsonData.getResult().equals("0")) {
                Intent intent;
                if(mGoodsListJsonData.getData() != null && mGoodsListJsonData.getData().size() > 0) {
                    if (mGoodsListJsonData.getData().size() == 1) {
                        if(mGoodsListJsonData.getData().get(0) == null) {
                            Toast.makeText(mContext, "获取商品信息失败！", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(mGoodsListJsonData.getData().get(0).getGoodstype() == 2)
                            intent = new Intent(mContext, BuyThemeActivity.class);
                        else
                            intent = new Intent(mContext, BuyListActivity.class);
                    } else {
                        intent = new Intent(mContext, BuyListActivity.class);
                    }
                    intent.putExtra("productData", (Serializable) mGoodsListJsonData);
                    intent.putExtra("libraryId", mLibraryId);
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

    private void initSelectClassList() {
        SelectClassLinearLayoutManager layoutManager = new SelectClassLinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        layoutManager.setScrollEnabled(false);
        mSelect_class_info_list.setLayoutManager(layoutManager);
        mSelect_class_info_list.setFocusable(false);
        mGeneralAdapter = new GeneralSelectClassAdapter(this, mTextList,mSelect_class_info_list);
        mSelect_class_info_list.setAdapter(mGeneralAdapter);

        mButton_relativeLayout.setFocusFailedAdapter(mGeneralAdapter);
        layoutManager.setFocusFailedButton(mLessonsButton);

        mSelect_class_info_list.addItemDecoration(new LessonMenuSpacing(0, 0, (int)getDimension(R.dimen.w_20), 0));

        mGeneralAdapter.setOnItemSelectListener(new GeneralSelectClassAdapter.OnItemSelectListener() {
            @Override
            public void onItemSelect(View view, int position) {
                mUpdateSelectDetailName.clear();
                int minN = Math.min(8*(position+1),mSelectDetailName.size());
                for (int i = 8*position; i< minN;i++){
                    mUpdateSelectDetailName.add(mSelectDetailName.get(i));
                }
                mClassGeneralAdapter.notifyDataSetChanged();
            }
        });

        mSelect_class_info_list.setOnItemClickListener(new RecyclerViewTV.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerViewTV parent, View itemView, int position) {

            }
        });

        layoutManager.setDownKeyListener(new SelectClassLinearLayoutManager.OnDownKeyListener() {
            @Override
            public void onDownKey(View focused) {
                mGeneralAdapter.setSelectedBackground(focused);
            }
        });

        layoutManager.setUpKeyListener(new SelectClassLinearLayoutManager.OnUpKeyListener() {
            @Override
            public void onUpKey(View focused) {
                mGeneralAdapter.setSelectedBackground(focused);
            }
        });

    }

    private void initClassRecycler() {
        SelectClassGirdLayoutmanager gridlayoutManager = new SelectClassGirdLayoutmanager(mContext, 4); // 解决快速长按焦点丢失问题.
        gridlayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        mClass_detail_list.setLayoutManager(gridlayoutManager);
        mClass_detail_list.setFocusable(false);
        mClass_detail_list.setSelectedItemAtCentered(true); // 设置item在中间移动

        gridlayoutManager.setFocusFailedAdapter(mGeneralAdapter);

        selectCoursePresenter mRecyclerViewPresenter = new selectCoursePresenter(mContext, mUpdateSelectDetailName);
        mClassGeneralAdapter = new GeneralAdapter(mRecyclerViewPresenter);
        mClass_detail_list.setAdapter(mClassGeneralAdapter);

        mClass_detail_list.setItemAnimator(null);

        mClass_detail_list.addItemDecoration(new GuessLikeSpacing((int)getDimension(R.dimen.w_20) , (int)getDimension(R.dimen.w_20), (int)getDimension(R.dimen.w_21), (int)getDimension(R.dimen.w_20)));
        initClassHover();
    }

    private void initClassHover() {
        mClass_detail_upview = (MainUpView) findViewById(R.id.class_detail_upview);
        mClass_detail_upview.setEffectBridge(new RecyclerViewBridge());
        mClassHoverBridge = (RecyclerViewBridge) mClass_detail_upview.getEffectBridge();

        mClass_detail_list.setOnItemListener(new RecyclerViewTV.OnItemListener() {

            @Override
            public void onItemPreSelected(RecyclerViewTV parent, View itemView, int position) {
                if (!isClassListRowPresenter()) {
                    mClassHoverBridge.setUnFocusView(mOldView);
                    mClassHoverBridge.setVisibleWidget(true);
                    TextView mText = (TextView) itemView.findViewById(R.id.course_name);
                    mText.setSelected(false);
                    mText.setTextColor(0xffffffff);
                }
            }

            @Override
            public void onItemSelected(RecyclerViewTV parent, View itemView, int position) {

                if (!isClassListRowPresenter()) {
                    if(mClassHoverBridge.isVisibleWidget()) {
                        mClassHoverBridge.setVisibleWidget(false);
                    }
                    mClassHoverBridge.setFocusView(itemView, 1.1f);
                    TextView mText = (TextView) itemView.findViewById(R.id.course_name);
                    mText.setSelected(true);
                    mText.setTextColor(0xff000000);
                    mOldView = itemView;

                }
            }

            @Override
            public void onReviseFocusFollow(RecyclerViewTV parent, View itemView, int position) {

                if (!isClassListRowPresenter()) {
                    if(mClassHoverBridge.isVisibleWidget()) {
                        mClassHoverBridge.setVisibleWidget(false);
                    }
                    mClassHoverBridge.setFocusView(itemView, 1.1f);
                    TextView mText = (TextView) itemView.findViewById(R.id.course_name);
                    mText.setSelected(true);
                    mText.setTextColor(0xff000000);
                    mOldView = itemView;
                }

            }
        });

        // item 单击事件处理.
        mClass_detail_list.setOnItemClickListener(new RecyclerViewTV.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerViewTV parent, View itemView, int position) {
                if (mHaveOrder != 1){
                    if (Integer.valueOf(mUpdateSelectDetailName.get(position).get("select_class_isfree").toString()).intValue() == 2){
                        start_video_activity_from_selectcalss(position,0);
                    }else {
                        sentToGoodlPage();
                        return;
                    }

                }else{
                    start_video_activity_from_selectcalss(position,0);
                }

            }
        });
    }

    private boolean isClassListRowPresenter() {
        GeneralAdapter generalAdapter = (GeneralAdapter) mClass_detail_list.getAdapter();
        OpenPresenter openPresenter = generalAdapter.getPresenter();
        return (openPresenter instanceof ListRowPresenter);
    }

    private void guessLikeManage() {
        mGuessLike0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String library_id = (String) v.getTag();
                fromGuessLikeToDetail(Integer.valueOf(library_id));
            }
        });

        mGuessLike1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String library_id = (String) v.getTag();
                fromGuessLikeToDetail(Integer.valueOf(library_id));
            }
        });

        mGuessLike2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String library_id = (String) v.getTag();
                fromGuessLikeToDetail(Integer.valueOf(library_id));
            }
        });

        mGuessLike3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String library_id = (String) v.getTag();
                fromGuessLikeToDetail(Integer.valueOf(library_id));
            }
        });

        mGuessLike4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String library_id = (String) v.getTag();
                fromGuessLikeToDetail(Integer.valueOf(library_id));
            }
        });

        mGuessLike5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String library_id = (String) v.getTag();
                fromGuessLikeToDetail(Integer.valueOf(library_id));
            }
        });

        mGuessLike6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String library_id = (String) v.getTag();
                fromGuessLikeToDetail(Integer.valueOf(library_id));
            }
        });
    }

    private void setGuessLikeData() {
        if(mDetailJsonData.getData() != null && mDetailJsonData.getData().getGuess_list() != null) {
            for (int i = 0; i< mDetailJsonData.getData().getGuess_list().size(); i++) {
                if (i == 0) {
                    mGuessLike0.setVisibility(View.VISIBLE);
                    mGuessLike0.setTag(mDetailJsonData.getData().getGuess_list().get(i).getLibrary_id());
                    mGuessLikeName0.setText(mDetailJsonData.getData().getGuess_list().get(i).getTitle());
                    Glide.with(mContext).load(mDetailJsonData.getData().getGuess_list().get(i).getThumb_app_h()).placeholder(R.drawable.load_detail).into(mGuessLikeImage0);
                    if(mDetailJsonData.getData().getGuess_list().get(i).getIs_free() == 0)
                        mGuessLikeFree0.setVisibility(View.VISIBLE);
                    else
                        mGuessLikeFree0.setVisibility(View.INVISIBLE);
                } else if (i == 1) {
                    mGuessLike1.setVisibility(View.VISIBLE);
                    mGuessLike1.setTag(mDetailJsonData.getData().getGuess_list().get(i).getLibrary_id());
                    mGuessLikeName1.setText(mDetailJsonData.getData().getGuess_list().get(i).getTitle());
                    Glide.with(mContext).load(mDetailJsonData.getData().getGuess_list().get(i).getThumb_app_h()).placeholder(R.drawable.load_detail).into(mGuessLikeImage1);
                    if(mDetailJsonData.getData().getGuess_list().get(i).getIs_free() == 0)
                        mGuessLikeFree1.setVisibility(View.VISIBLE);
                    else
                        mGuessLikeFree1.setVisibility(View.INVISIBLE);
                } else if (i == 2) {
                    mGuessLike2.setVisibility(View.VISIBLE);
                    mGuessLike2.setTag(mDetailJsonData.getData().getGuess_list().get(i).getLibrary_id());
                    mGuessLikeName2.setText(mDetailJsonData.getData().getGuess_list().get(i).getTitle());
                    Glide.with(mContext).load(mDetailJsonData.getData().getGuess_list().get(i).getThumb_app_h()).placeholder(R.drawable.load_detail).into(mGuessLikeImage2);
                    if(mDetailJsonData.getData().getGuess_list().get(i).getIs_free() == 0)
                        mGuessLikeFree2.setVisibility(View.VISIBLE);
                    else
                        mGuessLikeFree2.setVisibility(View.INVISIBLE);
                } else if (i == 3) {
                    mGuessLike3.setVisibility(View.VISIBLE);
                    mGuessLike3.setTag(mDetailJsonData.getData().getGuess_list().get(i).getLibrary_id());
                    mGuessLikeName3.setText(mDetailJsonData.getData().getGuess_list().get(i).getTitle());
                    Glide.with(mContext).load(mDetailJsonData.getData().getGuess_list().get(i).getThumb_app_h()).placeholder(R.drawable.load_detail).into(mGuessLikeImage3);
                    if(mDetailJsonData.getData().getGuess_list().get(i).getIs_free() == 0)
                        mGuessLikeFree3.setVisibility(View.VISIBLE);
                    else
                        mGuessLikeFree3.setVisibility(View.INVISIBLE);
                } else if (i == 4) {
                    mGuessLike4.setVisibility(View.VISIBLE);
                    mGuessLike4.setTag(mDetailJsonData.getData().getGuess_list().get(i).getLibrary_id());
                    mGuessLikeName4.setText(mDetailJsonData.getData().getGuess_list().get(i).getTitle());
                    Glide.with(mContext).load(mDetailJsonData.getData().getGuess_list().get(i).getThumb_app_h()).placeholder(R.drawable.load_detail).into(mGuessLikeImage4);
                    if(mDetailJsonData.getData().getGuess_list().get(i).getIs_free() == 0)
                        mGuessLikeFree4.setVisibility(View.VISIBLE);
                    else
                        mGuessLikeFree4.setVisibility(View.INVISIBLE);
                } else if (i == 5) {
                    mGuessLike5.setVisibility(View.VISIBLE);
                    mGuessLike5.setTag(mDetailJsonData.getData().getGuess_list().get(i).getLibrary_id());
                    mGuessLikeName5.setText(mDetailJsonData.getData().getGuess_list().get(i).getTitle());
                    Glide.with(mContext).load(mDetailJsonData.getData().getGuess_list().get(i).getThumb_app_h()).placeholder(R.drawable.load_detail).into(mGuessLikeImage5);
                    if(mDetailJsonData.getData().getGuess_list().get(i).getIs_free() == 0)
                        mGuessLikeFree5.setVisibility(View.VISIBLE);
                    else
                        mGuessLikeFree5.setVisibility(View.INVISIBLE);
                } else if (i == 6) {
                    mGuessLike6.setVisibility(View.VISIBLE);
                    mGuessLike6.setTag(mDetailJsonData.getData().getGuess_list().get(i).getLibrary_id());
                    mGuessLikeName6.setText(mDetailJsonData.getData().getGuess_list().get(i).getTitle());
                    Glide.with(mContext).load(mDetailJsonData.getData().getGuess_list().get(i).getThumb_app_h()).placeholder(R.drawable.load_detail).into(mGuessLikeImage6);
                    if(mDetailJsonData.getData().getGuess_list().get(i).getIs_free() == 0)
                        mGuessLikeFree6.setVisibility(View.VISIBLE);
                    else
                        mGuessLikeFree6.setVisibility(View.INVISIBLE);
                } else {
                    break;
                }
            }

        }
    }

    private void fromGuessLikeToDetail(int library_id) {
        finish();
        Intent intent = new Intent(DetailActivity.this, DetailActivity.class);
        intent.putExtra("otherCall", mOtherCall);
        intent.putExtra("libraryId",library_id);
        startActivity(intent);
    }

    public float getDimension(int id) {
        return getResources().getDimension(id);
    }

    private void start_video_activity_from_selectcalss(int video_index,int play_time) {
        Intent intent = new Intent(mContext, PlayVideoActivity.class);
        intent.putExtra("play_video_json", (Serializable) mDetailJsonData);
        intent.putExtra("play_goodslist_json",(Serializable) mGoodsListJsonData);
        intent.putExtra("from",0);
        intent.putExtra("play_video_durtion",mUpdateSelectDetailName.get(video_index).get("select_class_durtion").toString());
        intent.putExtra("play_video_url", mUpdateSelectDetailName.get(video_index).get("select_class_playurl").toString());
        intent.putExtra("play_video_name", mUpdateSelectDetailName.get(video_index).get("select_class_name").toString());
        intent.putExtra("play_video_id", mUpdateSelectDetailName.get(video_index).get("select_class_library_detail_id").toString());
        intent.putExtra("play_video_playtime", play_time);
        startActivity(intent);
    }

    private void start_video_activity(int video_index,int play_time) {
        Intent intent = new Intent(mContext, PlayVideoActivity.class);
        intent.putExtra("play_video_json", (Serializable) mDetailJsonData);
        intent.putExtra("play_goodslist_json",(Serializable) mGoodsListJsonData);
        intent.putExtra("from",0);
        intent.putExtra("play_video_durtion",mDetailJsonData.getData().getVideo_list().get(video_index).getVideo_duration());
        intent.putExtra("play_video_url", mDetailJsonData.getData().getVideo_list().get(video_index).getPlayurl());
        intent.putExtra("play_video_name", mDetailJsonData.getData().getVideo_list().get(video_index).getVideo_name());
        intent.putExtra("play_video_id", mDetailJsonData.getData().getVideo_list().get(video_index).getLibrary_detail_id()+"");
        intent.putExtra("play_video_playtime", play_time);
        startActivity(intent);
    }

    private void playVideoAuthor(final int video_index, final int play_time) {
        start_video_activity(video_index,play_time);
        /* 暂时注释掉播放鉴权
        String sContentID = mDetailJsonData.getData().getList().get(video_index).getCode();
        String sSTB_MAC = PreferencesUtil.GetAuthorValue(mContext, GlobleData.PREFERENCE_AUTHOR_STBMAC, "");
        String sPhoneNumber = PreferencesUtil.GetAuthorValue(mContext, GlobleData.PREFERENCE_AUTHOR_PHONE, "");
        String sAccount = PreferencesUtil.GetAuthorValue(mContext, GlobleData.PREFERENCE_AUTHOR_ACCOUNT, "");
        String sSTB_SN = PreferencesUtil.GetAuthorValue(mContext, GlobleData.PREFERENCE_AUTHOR_STBSN, "");
        String sEPGAddress = PreferencesUtil.GetAuthorValue(mContext, GlobleData.PREFERENCE_AUTHOR_EPGADDRESS, "");
        String sTV_ID = PreferencesUtil.GetAuthorValue(mContext, GlobleData.PREFERENCE_AUTHOR_TVID, "");
        String sOTTToken = PreferencesUtil.GetAuthorValue(mContext, GlobleData.PREFERENCE_AUTHOR_USER_OTTTOKEN, "");

//        String sSTB_MAC = "00:24:68:E8:AC:0E";//devManager.getValue(DevInfoManager.STB_MAC);
//        String sPhoneNumber = "53402000349";//devManager.getValue(DevInfoManager.PHONE);
//        String sContentID = "58d8eff74f2a230bb54edd6f8b324768";
//        String sAccount = "";//devManager.getValue(DevInfoManager.ACCOUNT);
//        String sSTB_SN = "00410416210900901627002468E8ACOE";//devManager.getValue(DevInfoManager.STB_SN);
//        String sEPGAddress = "http://223.110.245.40:33200/EPG";//devManager.getValue(DevInfoManager.EPGAddress);
//        String sTV_ID = "";//devManager.getValue(DevInfoManager.TVID);
//        String sOTTToken = "53402000349-00:24:68:E8:AC:0E";

        String url = sEPGAddress + GlobleData.HTTP_PLAYVIDEO;
        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("UserID", sPhoneNumber.isEmpty() ? sAccount : sPhoneNumber);
        paramsMap.put("OTTUserToken", sOTTToken);
        paramsMap.put("ContentID", sContentID);
        paramsMap.put("TimeStamp", "");
        paramsMap.put("STBID", sTV_ID);
        paramsMap.put("SN", sSTB_SN);
        paramsMap.put("IP", "");
        paramsMap.put("MAC", sSTB_MAC);
        paramsMap.put("SPAuthResult", Integer.toString(mHaveOrder));
        OkhttpUtil.okHttpGet(url, paramsMap, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                Log.d(TAG, "play author failed!");
                Toast.makeText(mContext, "播放鉴权失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "play author response: " + response);
                try {
                    JSONObject obj = new JSONObject(response);
                    if(obj == null) {
                        Log.d(TAG, "play author failed!");
                        Toast.makeText(mContext, "播放鉴权失败: " + response, Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (Exception e) {
                    Log.d(TAG, "play author failed!");
                    Toast.makeText(mContext, "播放鉴权失败: " + response, Toast.LENGTH_SHORT).show();
                    return;
                }
                JsonVideoAuthorData resultBean = GsonUtil.fromJson(response, JsonVideoAuthorData.class);
                if(resultBean == null) {
                    Toast.makeText(mContext, "播放鉴权失败: " + response, Toast.LENGTH_SHORT).show();
                    return;
                }
                if(resultBean.getResult().equals("0")) {
                    Toast.makeText(mContext, "播放鉴权成功: " + response, Toast.LENGTH_SHORT).show();
                    start_video_activity(video_index, play_time);
                } else {
                    Toast.makeText(mContext, "播放鉴权失败: " + response, Toast.LENGTH_SHORT).show();
                }
            }
        });
        */
    }


    private void getProductList() {
        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put(GlobleData.HTTP_VERSION_KEY, GlobleData.HTTP_VERSION_VALUE);
        paramsMap.put(GlobleData.HTTP_PARAM_LIBRARYID, Integer.toString(mLibraryId));
        OkhttpUtil.okHttpGet(GlobleData.HTTP_URL_GOODSLIST, paramsMap, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                Toast.makeText(mContext, "获取商品信息失败！", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);
                try {
                    JSONObject obj = new JSONObject(response);
                    if(obj == null) {
                        Toast.makeText(mContext, "获取商品信息失败！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (Exception e) {
                    Toast.makeText(mContext, "获取商品信息失败！", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    mGoodsListJsonData = GsonUtil.fromJson(response, JsonGoodsListData.class);
                    if(mGoodsListJsonData == null)
                        return;
                    if(mGoodsListJsonData.getResult().equals("0")) {
                        if(mGoodsListJsonData.getData().size() > 0) {
                            int isOrder = mDetailJsonData.getData().getIsOrder();
                            int isFree = mDetailJsonData.getData().getIs_free();

                            // 是否免费
                            if (isFree == 0) {
                                mHaveOrder = 1;
                                mBuyButton.setText("免费");
                            } else {
                                // 是否订购
                                mHaveOrder = isOrder;
                                if (mHaveOrder == 1)
                                    mBuyButton.setText("已订购");
                                else
                                    mBuyButton.setText("订购");
                            }
                        }
                    } else {
                        int isOrder = mDetailJsonData.getData().getIsOrder();
                        int isFree = mDetailJsonData.getData().getIs_free();

                        // 是否免费
                        if (isFree == 0) {
                            mHaveOrder = 1;
                            mBuyButton.setText("免费");
                        } else {
                            // 是否订购
                            mHaveOrder = isOrder;
                            if (mHaveOrder == 1)
                                mBuyButton.setText("已订购");
                            else
                                mBuyButton.setText("订购");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setTimeData() {
        mTimer = new Timer();
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                Message msg = new Message();
                String sTimeData = XESUtil.getDateTime();
                Bundle bundle=new Bundle();
                bundle.putString("time", sTimeData);
                msg.setData(bundle);
                msg.what = 1;
                mHandler.sendMessage(msg);
            }
        };
        mTimer.schedule(mTimerTask, 0, 1000);
    }

    public class MsgReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if(GlobleData.MESSAGE_UPDATE_TIME.equals(intent.getAction())) {
                    String sTimeData = intent.getStringExtra("time");
                    mDateTextView.setText(sTimeData.split(" ")[0]);
                    mTimeTextView.setText(sTimeData.split(" ")[1]);
                    mWeekTextView.setText(sTimeData.split(" ")[2]);
                }
                if(GlobleData.MESSAGE_ORDER_FINISH.equals(intent.getAction())) {
                    int iResult = intent.getIntExtra("order_finish", 0);
                    String sGoodName = intent.getStringExtra("good_name");
                    if(iResult == 1 && sGoodName != null && !sGoodName.isEmpty()) {
                        mHaveOrder = 1;
                        mBuyButton.setText("已订购");

                        // 弹窗
                        final Dialog builder = new Dialog(DetailActivity.this, R.style.dialogTransparent);
                        LinearLayout orderSuccDialog= (LinearLayout) getLayoutInflater().inflate(R.layout.pop_dialog_order_success,null);
                        final Button mOk = (Button)orderSuccDialog.findViewById(R.id.popOK);
                        TextView mText =  (TextView) orderSuccDialog.findViewById(R.id.popText);
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
                if (GlobleData.MESSAGE_UPDATE_DETAIL_DATA.equals(intent.getAction())){
                    getDetailDataFromHttp(mLibraryId);
                }
            } catch (Exception e) {
                Log.e(TAG, "" + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
