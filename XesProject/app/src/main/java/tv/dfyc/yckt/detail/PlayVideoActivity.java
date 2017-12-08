package tv.dfyc.yckt.detail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import okhttp3.Call;
import tv.danmaku.ijk.media.player.IMediaPlayer;

import com.dou361.ijkplayer.listener.OnPlayerBackListener;
import com.dou361.ijkplayer.widget.PlayerView;
import com.open.androidtvwidget.leanback.recycle.RecyclerViewTV;

import tv.dfyc.yckt.AppManager;
import tv.dfyc.yckt.R;
import tv.dfyc.yckt.adapter.PlayLessonsAdapter;
import tv.dfyc.yckt.network.CallBackUtil;
import tv.dfyc.yckt.network.GsonUtil;
import tv.dfyc.yckt.network.OkhttpUtil;
import tv.dfyc.yckt.util.PreferencesUtil;
import tv.dfyc.yckt.util.GlobleData;
import tv.dfyc.yckt.beans.JsonCommonResult;
import tv.dfyc.yckt.beans.JsonGoodsListData;
import tv.dfyc.yckt.beans.JsonVideoDetailData;
import tv.dfyc.yckt.custom.LessonMenuSpacing;
import tv.dfyc.yckt.custom.LinearLayoutManagerPlayLessons;
import tv.dfyc.yckt.util.XESUtil;

import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/25 0025.
 */

public class PlayVideoActivity extends Activity {
    private static final String TAG = "PlayVideoActivity";
    private Context mContext;
    private TextView mLessonsName;
    private RecyclerViewTV mLessonsList;
    private LinearLayout mLessonsView;
    private PlayLessonsAdapter mLessonsAdapter;
    private LinearLayoutManagerPlayLessons mLessonsLayoutManager;
    private PlayerView player;
    private View rootView;
    private List<JsonVideoDetailData.detailData.videoDetail> mLessonsDataList;
    private int libraryDetailID;
    private int library_id;
    private String mPlayName;
    private Handler mHandler;
    private int mProgress;
    private  CountDownTimer countDownTimer;
    private int number = 0;
    private String startTime;
    private Long time = 0L;
    private AutoListenerRunnable mAutoListenerRunnable = new AutoListenerRunnable();
    private int iContinueIndex;
    private  JsonVideoDetailData data;

    private JsonGoodsListData mGoodsListData;
    private int mHaveOrder = 0;
    int from = -1;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        AppManager.getAppManager().addActivity(this);
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        data = (JsonVideoDetailData) bundle.getSerializable("play_video_json");
        mGoodsListData = (JsonGoodsListData)bundle.getSerializable("play_goodslist_json");
        String id = bundle.getString("play_video_id");
        libraryDetailID = Integer.valueOf(id).intValue();
        int iPlayTime = bundle.getInt("play_video_playtime");

        String durtion = bundle.getString("play_video_durtion");
        String sVideoUrl = bundle.getString("play_video_url");
        mPlayName = bundle.getString("play_video_name");
        from = bundle.getInt("from");
        library_id = data.getData().getLibrary_id();

        int isOrder = data.getData().getIsOrder();
        int isFree = data.getData().getIs_free();

        // 是否免费
        if (isFree == 0) {
            mHaveOrder = 1;
        } else {
            // 是否订购
            mHaveOrder = isOrder;
        }

        if (data != null && data.getData().getVideo_list().size() > 0) {
            for (int i = 0; i < data.getData().getVideo_list().size(); ++i) {
                if (data.getData().getLibrary_detail_id() == data.getData().getVideo_list().get(i).getLibrary_detail_id()) {
                    iContinueIndex = i;
                    break;
                }
            }
        }

        mHandler = new Handler();

        sVideoUrl = GetCdnPlayUrl(sVideoUrl);
        if(sVideoUrl.isEmpty()) {
            Toast.makeText(mContext, "获取播放地址失败！", Toast.LENGTH_SHORT).show();
            return;
        }

        mLessonsDataList = data.getData().getVideo_list();
        rootView = getLayoutInflater().from(this).inflate(R.layout.simple_player_view_player, null);
        setContentView(rootView);
        mLessonsName = (TextView) findViewById(R.id.play_page_lessons_name);
        mLessonsList = (RecyclerViewTV) findViewById(R.id.play_page_lessons_list);
        mLessonsView = (LinearLayout) findViewById(R.id.play_page_lessons_layout);
        initLessonsView();
        mLessonsName.setText(data.getData().getTitle());
        mLessonsName.setSelected(true);
        player = new PlayerView(this, rootView);

        player.setTitle(mPlayName);
        player.setPlaySource(sVideoUrl);
        player.setPlayerBackListener(new OnPlayerBackListener() {
            @Override
            public void onPlayerBack() {
                player.removeHandler();
                player.onBackPressed();
                player.onDestroy();
                finish();
            }
        });
        player.startPlay();
        player.setCurrentPosition(iPlayTime*1000);

        startTime = XESUtil.getCurrentFormatTime("yyyyMMddHHmmss");

        try {
            Calendar c = Calendar.getInstance();
            c.setTime(new SimpleDateFormat("yyyyMMddHHmmss").parse(startTime));
            time = c.getTimeInMillis();

        } catch (ParseException e) {
            e.printStackTrace();
        }

        player.setOnAutoCompleteListener(new PlayerView.OnAutoCompleteListener() {
            @Override
            public void onAutoComplete() {
                player.removeHandler();
                    if (isLessonsViewVisible()){
                        closewLessonsView();
                    }

                    if (mHaveOrder != 1){
                        //未订购
                        if (iContinueIndex != data.getData().getVideo_list().size()-1){
                            //Log.e("iContinueIndex=",iContinueIndex+"");
                            if (mLessonsDataList.get(iContinueIndex+1).getVideo_is_free() == 2){
                                //免费,播放下一集
                                player.getPlayerView().setVisibility(View.INVISIBLE);
                                mPlayName = mLessonsDataList.get(iContinueIndex+1).getVideo_name();
                                player.setTitle(mPlayName);
                                String url = mLessonsDataList.get(iContinueIndex+1).getVideo();
                                url = GetCdnPlayUrl(url);
                                player.setPlaySource(url);
                                player.setCurrentPosition(0);
                                player.startPlay();
                                libraryDetailID = mLessonsDataList.get(iContinueIndex+1).getLibrary_detail_id();
                                iContinueIndex++;
                            }else {
                                //不免费
                                sentToGoodPage();
                                player.removeHandler();
                                player.onBackPressed();
                                player.onNoRecordDestory();
                                finish();
                            }
                        }else {
                            if (from == 0){
                                player.removeHandler();
                                player.onBackPressed();
                                player.onNoRecordDestory();
                                 finish();
                            }else {
                                Intent intent = new Intent(PlayVideoActivity.this, DetailActivity.class);
                                intent.putExtra("libraryId",Integer.valueOf(library_id).intValue());
                                startActivity(intent);
                                stopTimer();
                                player.removeHandler();
                                player.onBackPressed();
                                player.onNoRecordDestory();
                                 finish();
                            }
                        }
                    }else{
                        //已经订购
                        if (iContinueIndex != data.getData().getVideo_list().size()-1){
                            player.getPlayerView().setVisibility(View.INVISIBLE);
                            mPlayName = mLessonsDataList.get(iContinueIndex+1).getVideo_name();
                            player.setTitle(mPlayName);
                            String url = mLessonsDataList.get(iContinueIndex+1).getVideo();
                            url = GetCdnPlayUrl(url);
                            player.setPlaySource(url);
                            player.setCurrentPosition(0);
                            player.startPlay();
                            libraryDetailID = mLessonsDataList.get(iContinueIndex+1).getLibrary_detail_id();
                            iContinueIndex++;
                        }else {
                            if (from == 0){
                                player.removeHandler();
                                player.onBackPressed();
                                player.onNoRecordDestory();
                                finish();
                            }else {
                                Intent intent = new Intent(PlayVideoActivity.this, DetailActivity.class);
                                intent.putExtra("libraryId",Integer.valueOf(library_id).intValue());
                                startActivity(intent);
                                stopTimer();
                                finish();
                            }
                        }
                    }
                }
        });

        player.getVideoView().setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(IMediaPlayer iMediaPlayer) {

                Map<String, String> paramsMap = new HashMap<String, String>();
                String mac = PreferencesUtil.GetAuthorValue(mContext, GlobleData.PREFERENCE_AUTHOR_STBMAC, "");
                String userID = PreferencesUtil.GetAuthorValue(mContext,GlobleData.PREFERENCE_AUTHOR_USER_ID, "");
                paramsMap.put(GlobleData.HTTP_VERSION_KEY, GlobleData.HTTP_VERSION_VALUE);
                paramsMap.put("Contentid", Integer.toString(libraryDetailID));
                paramsMap.put("playtime", Integer.toString((int)iMediaPlayer.getCurrentPosition()/1000));
//                mac= "00:24:68:E8:AC:0E";
//                userID=83+"";
                paramsMap.put("begintime", startTime);
                if ((int)iMediaPlayer.getCurrentPosition()/1000 == 0){
                    return;
                }
                long endtime = time+iMediaPlayer.getCurrentPosition();
                Date date = new Date(endtime);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                paramsMap.put("endtime", sdf.format(date));
                paramsMap.put("Mac", mac);
                paramsMap.put("UserID", userID);
                Log.e("libraryDetailID",libraryDetailID+"");
                Log.e("playtime",Integer.toString((int)iMediaPlayer.getCurrentPosition()/1000)+"");
                OkhttpUtil.okHttpGet(GlobleData.HTTP_URL_ADDHISTORY, paramsMap, new CallBackUtil.CallBackString() {
                    @Override
                    public void onFailure(Call call, Exception e) {
                        Toast.makeText(mContext, "添加播放记录失败！", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        try {
                            JSONObject obj = new JSONObject(response);
                            if(obj == null) {
                                Toast.makeText(mContext, "添加播放记录失败！", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (Exception e) {
                            Toast.makeText(mContext, "添加播放记录失败！", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        try {
                            JsonCommonResult resultBean = GsonUtil.fromJson(response, JsonCommonResult.class);
                            if(resultBean == null){
                                return;
                            }
                            //Toast.makeText(mContext, resultBean.getMessage(), Toast.LENGTH_SHORT).show();
                            sendBroadcastUpdateData();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        mLessonsList.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    if (mLessonsList.getChildCount() > 0) {
                        mLessonsList.setDefaultSelect(mLessonsAdapter.getSelectPos());
                    }
                }
            }
        });
    }

    private void sentToGoodPage() {
        if(mHaveOrder != 1) {
            //Toast.makeText(mContext, "请先订购此商品！", Toast.LENGTH_SHORT).show();
            // 直接跳到订购页面
            try {
                if(mGoodsListData != null && mGoodsListData.getResult().equals("0")) {
                    Intent intent;
                    if(mGoodsListData.getData() != null && mGoodsListData.getData().size() > 0) {
                        if (mGoodsListData.getData().size() == 1) {
                            if(mGoodsListData.getData().get(0) == null) {
                                Toast.makeText(mContext, "获取商品信息失败！", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if(mGoodsListData.getData().get(0).getGoodstype() == 2)
                                intent = new Intent(mContext, BuyThemeActivity.class);
                            else
                                intent = new Intent(mContext, BuyListActivity.class);
                        } else {
                            intent = new Intent(mContext, BuyListActivity.class);
                        }
                        intent.putExtra("productData", (Serializable) mGoodsListData);
                        intent.putExtra("libraryId", library_id);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(mContext, "获取商品信息失败！", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(mContext, "获取商品信息失败！", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            return;
        }
    }

    private void sendBroadcastUpdateData() {
        Intent intent = new Intent(GlobleData.MESSAGE_UPDATE_DETAIL_DATA);
        this.sendBroadcast(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if (resultCode == RESULT_OK){
                    int index = 0;
                    if (data.getIntExtra("index",0) != mLessonsDataList.size()-1){
                        index = data.getIntExtra("index",0)+1;
                    }else {
                        index = 0;
                    }
                    player.getPlayerView().setVisibility(View.INVISIBLE);
                    mPlayName = mLessonsDataList.get(index).getVideo_name();
                    player.setTitle(mPlayName);
                    String url = mLessonsDataList.get(index).getVideo();
                    url = GetCdnPlayUrl(url);
                    player.setPlaySource(url);
                    player.setCurrentPosition(0);
                    player.startPlay();
                    libraryDetailID = mLessonsDataList.get(index).getLibrary_detail_id();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (player != null) {
            player.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (player != null) {
            player.onResume();
        }
    }

    @Override
    protected void onDestroy() {
        if (player != null) {
//            player.removeHandler();
//            player.onBackPressed();
            player.onDestroy();
        }
        if(AppManager.getAppManager().getActivity(PlayVideoActivity.class) != null)
            AppManager.getAppManager().removeActivity(this);
        super.onDestroy();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (player != null) {
            player.onConfigurationChanged(newConfig);
        }
    }

    @Override
    public void onBackPressed() {
//        player.removeHandler();
//        player.onBackPressed();
//        player.onDestroy();
//        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mAutoListenerRunnable != null) {
            mAutoListenerRunnable.stop();
            mAutoListenerRunnable.start();
        }
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_ENTER:
            case KeyEvent.KEYCODE_NUMPAD_ENTER:
                if (!isLessonsViewVisible()){
                    if (player.getVideoView().isPlaying()) {
                        player.getVideoView().pause();
                        player.getPlayerView().setVisibility(View.VISIBLE);
                    }else {
                        player.getPlayerView().setVisibility(View.INVISIBLE);
                        player.getVideoView().start();
                    }
                }
                break;

            case KeyEvent.KEYCODE_DPAD_LEFT:
                if (!isLessonsViewVisible()) {
                    if(!player.getIsShowControlPanl()) {
                        player.operatorPanl();
                    }
                    player.setFastUpAndDown(true);
                    player.getVideoView().pause();
                    int progress = player.getSeekBarView().getProgress();
                    int currentTime = player.getVideoView().getCurrentPosition();
                    if (progress <= ((int)(1000*10000/player.getDuration()))) {
                        progress = (int)(1000*10000/player.getDuration());
                        player.getSeekBarView().setProgress(progress);
                    }else {
                        player.getSeekBarView().setProgress(progress-(int)(1000*10000/player.getDuration()));
                    }
                }
                break;

            case KeyEvent.KEYCODE_DPAD_RIGHT:
                if (!isLessonsViewVisible()) {
                    if(!player.getIsShowControlPanl()) {
                        player.operatorPanl();
                    }
                    player.setFastUpAndDown(true);
                    player.getVideoView().pause();
                    int progress = player.getSeekBarView().getProgress();
                    int currentTime = player.getVideoView().getCurrentPosition();
                    if (progress >= (1000 -(int)(1000*10000/player.getDuration()))) {
                        progress = 1000 - (int)(1000*10000/player.getDuration());
                        player.getSeekBarView().setProgress(progress);
                    }else {
                        player.getSeekBarView().setProgress(progress+(int)(1000*10000/player.getDuration()));
                     }
                }

                break;

            case KeyEvent.KEYCODE_DPAD_UP:

                if (!isLessonsViewVisible()) {
                        showLessonsView();
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mLessonsList.setFocusable(true);
                                //mLessonsList.requestFocus();
                            }
                        }, 500);
                    mAutoListenerRunnable.start();
                }
                break;

            case KeyEvent.KEYCODE_DPAD_DOWN:

                if (!isLessonsViewVisible()) {
                    if(!player.getIsShowControlPanl()) {
                        player.operatorPanl();
                    }
                }
                break;
            case KeyEvent.KEYCODE_BACK:
                if (!isLessonsViewVisible()) {
                    if (player != null) {
                        if (number == 0){
                            Toast.makeText(mContext, "再按一次退出播放", Toast.LENGTH_SHORT).show();
                            startTimer();
                        }
                        number++;
                        //return false;
                    }
                }else {
                    closewLessonsView();
                    //return false;
                }
                break;

            case KeyEvent.KEYCODE_MENU:

                if(!isLessonsViewVisible()) {
                    showLessonsView();

                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mLessonsList.setFocusable(true);
                            //mLessonsList.requestFocus();
                        }
                    }, 500);
                    mAutoListenerRunnable.start();

                } else {
                    closewLessonsView();
                }
                break;
            default:
                break;

        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                if (!isLessonsViewVisible()) {
                    int progress = player.getSeekBarView().getProgress();
                    player.seekTo((int)(progress * player.getDuration() / 1000));
                    player.setFastUpAndDown(false);
                    player.showLoadImage(true);
                    player.getVideoView().start();
                }else {
                    closewLessonsView();
                }
            }
        return super.onKeyUp(keyCode, event);
    }




    private void initLessonsView() {
        mLessonsLayoutManager = new LinearLayoutManagerPlayLessons(mContext);
        mLessonsLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mLessonsLayoutManager.setScrollEnabled(true);
        mLessonsList.setLayoutManager(mLessonsLayoutManager);
        mLessonsList.setFocusable(false);
        mLessonsAdapter = new PlayLessonsAdapter(mContext, mLessonsList);
        mLessonsList.setAdapter(mLessonsAdapter);
        mLessonsList.addItemDecoration(new LessonMenuSpacing(0, (int)getDimension(R.dimen.h_42), 0, (int)getDimension(R.dimen.h_42)));


        mLessonsAdapter.setOnItemClickListener(new PlayLessonsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                JsonVideoDetailData.detailData.videoDetail video_detail = (JsonVideoDetailData.detailData.videoDetail) view.getTag();
                if (mHaveOrder != 1){
                    if (video_detail.getVideo_is_free() == 2){
                        iContinueIndex = position;
                        player.stopPlay();
                        player.setTitle(video_detail.getVideo_name());
                        mPlayName = video_detail.getVideo_name();
                        String url = video_detail.getVideo();
                        url = GetCdnPlayUrl(url);
                        player.setPlaySource(url);
                        player.startPlay();
                        player.setCurrentPosition(0);
                        libraryDetailID = video_detail.getLibrary_detail_id();
                        refrushLessonsView(true);
                    }else {
                        sentToGoodPage();
                        player.removeHandler();
                        player.onBackPressed();
                        player.onDestroy();
                        finish();
                    }
                }else {
                    iContinueIndex = position;
                    player.stopPlay();
                    player.setTitle(video_detail.getVideo_name());
                    mPlayName = video_detail.getVideo_name();
                    String url = video_detail.getVideo();
                    url = GetCdnPlayUrl(url);
                    player.setPlaySource(url);
                    player.startPlay();
                    player.setCurrentPosition(0);
                    libraryDetailID = video_detail.getLibrary_detail_id();
                    refrushLessonsView(true);
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
    }

    private class AutoListenerRunnable implements Runnable {
        private int AUTO_PLAY_INTERVAL = 3000
                ;
        private boolean mShouldAutoPlay;

        /**
         * 三秒无操作，收起控制面板
         */
        public AutoListenerRunnable() {
            mShouldAutoPlay = false;
        }

        public void start() {
            if (!mShouldAutoPlay) {
                mShouldAutoPlay = true;
                mHandler.removeCallbacks(this);
                mHandler.postDelayed(this, AUTO_PLAY_INTERVAL);
            }
        }

        public void stop() {
            if (mShouldAutoPlay) {
                mHandler.removeCallbacks(this);
                mShouldAutoPlay = false;
            }
        }

        @Override
        public void run() {
            if (mShouldAutoPlay) {
                mHandler.removeCallbacks(this);
                if (isLessonsViewVisible())
                    closewLessonsView();
            }
        }
    }

    private void startTimer(){
        /** * 参数1：倒计时总时间 * 参数2：倒计时间隔时间 */
        countDownTimer = new CountDownTimer(2000,10) {
            @Override
            public void onTick(long l) {
                //倒计时每秒的回调
                if (number == 2){
                    stopTimer();
                    player.removeHandler();
                    player.onBackPressed();
                    player.onDestroy();
                    finish();
                }
            }
            @Override
            public void onFinish() {
                //倒计时结束
                number = 0;
            }
        };
        countDownTimer.start();
    }

    private void stopTimer() {
        if (countDownTimer != null)
            countDownTimer.cancel();
        number = 0;
    }


    public void showLessonsView() {
        refrushLessonsView(false);

        TranslateAnimation translate = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                1.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        translate.setDuration(300);//动画时间300毫秒
        translate.setFillAfter(false);//动画出来控件可以点击
        mLessonsView.setAnimation(translate);
        mLessonsView.setVisibility(View.VISIBLE);
    }

    public void closewLessonsView() {
        TranslateAnimation translate = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF,
                1.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 0.0f);

        translate.setDuration(500);//动画时间500毫秒
        translate.setFillAfter(false);//动画出来控件可以点击
        mLessonsView.startAnimation(translate);
        mLessonsView.setVisibility(View.INVISIBLE);
    }

    private void refrushLessonsView(boolean add_pos) {
        mLessonsList.removeAllViews();
        int cur_position = getPositionByName(mPlayName, mLessonsDataList);
        int scroll_positon = cur_position;
        /* 去掉循环列表
        if(mLessonsDataList.size() > 8) {
            cur_position = cur_position + mLessonsDataList.size()*1000;
            scroll_positon = cur_position - 3;
            mLessonsAdapter.setMultiFlag(true);
            mLessonsLayoutManager.setMultiFlag(true);
        } else {
            mLessonsAdapter.setMultiFlag(false);
            mLessonsLayoutManager.setMultiFlag(false);
        }
        */
        mLessonsAdapter.UpdateData(mPlayName, cur_position, mLessonsDataList);
        if(add_pos)
            mLessonsList.scrollToPosition(scroll_positon);
        else
            mLessonsList.scrollToPosition(cur_position);
    }

    private boolean isLessonsViewVisible() {
        if(mLessonsView == null) {
            return false;
        }
        if(mLessonsView != null && (mLessonsView.getVisibility() == View.INVISIBLE)) {
            return false;
        }
        return true;
    }

    private int getPositionByName(String name, List<JsonVideoDetailData.detailData.videoDetail> list) {
        for(int i = 0; i < list.size(); ++i) {
            if(list.get(i).getVideo_name().equals(name)) {
                return i;
            }
        }
        return -1;
    }

    private String GetCdnPlayUrl(String url) {
        // cdn: "http://183.207.249.71:80"
        String CdnAddress = PreferencesUtil.GetAuthorValue(mContext,GlobleData.PREFERENCE_AUTHOR_CDNADDRESS, "");
        String CdnAddressBackup = PreferencesUtil.GetAuthorValue(mContext,GlobleData.PREFERENCE_AUTHOR_BACKUPCDNADDRESS, "");
        CdnAddress = "http://183.207.249.71:80";
        if(!CdnAddress.isEmpty() || !CdnAddressBackup.isEmpty()) {
            String cdn = CdnAddress.isEmpty() ? CdnAddressBackup : CdnAddress;
            int iIndex = url.indexOf("/", 8);
            String leftStr = url.substring(0, iIndex);
            url = url.replaceAll(leftStr, cdn);
            return url;
        }
        return "";

    }

    public float getDimension(int id) {
        return getResources().getDimension(id);
    }
}
