package tv.dfyc.yckt.info.search;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.open.androidtvwidget.bridge.RecyclerViewBridge;
import com.open.androidtvwidget.keyboard.SkbContainer;
import com.open.androidtvwidget.keyboard.SoftKey;
import com.open.androidtvwidget.keyboard.SoftKeyBoardListener;
import com.open.androidtvwidget.leanback.adapter.GeneralAdapter;
import com.open.androidtvwidget.leanback.mode.ListRowPresenter;
import com.open.androidtvwidget.leanback.mode.OpenPresenter;
import com.open.androidtvwidget.leanback.recycle.GridLayoutManagerTV;
import com.open.androidtvwidget.leanback.recycle.RecyclerViewTV;
import com.open.androidtvwidget.view.MainUpView;

import tv.dfyc.yckt.R;
import tv.dfyc.yckt.adapter.GuessLikePresenter;
import tv.dfyc.yckt.adapter.HotSearchPresenter;
import tv.dfyc.yckt.adapter.SearchResultPresenter;
import tv.dfyc.yckt.detail.DetailActivity;
import tv.dfyc.yckt.network.CallBackUtil;
import tv.dfyc.yckt.network.GsonUtil;
import tv.dfyc.yckt.network.OkhttpUtil;
import tv.dfyc.yckt.custom.CourseRecyclerItemSpace;
import tv.dfyc.yckt.util.GlobleData;
import tv.dfyc.yckt.custom.GuessLikeSpacing;
import tv.dfyc.yckt.beans.JsonSearchData;
import tv.dfyc.yckt.util.LessonDetail;
import tv.dfyc.yckt.util.XESUtil;

import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by admin on 2017-7-14.
 */

public class searchPageActivity extends Activity {
    private static final String TAG = "searchPageActivity";
    private Context mContext;
    private SkbContainer skbContainer;
    private EditText input_tv;
    SoftKey mOldSoftKey =null;
    private TextView mSearch_note;
    private TextView weekTextView;
    private TextView timeTextView;
    private TextView dateTextView;
    private MsgReceiver msgReceiver;
    private String sInput;
    private MainUpView mGuessLikeHoverState;
    private RecyclerViewBridge mGuessLikeHoverBridge;
    private RecyclerViewTV mGuessLikeRecyclerList;
    private List<LessonDetail> mGuessLikeList;
    private GeneralAdapter mGuessLikeAdapter;
    private View mGuessLikeOldView;
    private MainUpView mHotSearchHoverState;
    private RecyclerViewBridge mHotSearchHoverBridge;
    private RecyclerViewTV mHotSearchRecyclerList;
    private View mHotSearchOldView;
    private ArrayList<JsonSearchData.SearchData.Hotsearch_list> mHotSearchList;
    private RelativeLayout mHot_search;
    private TextView mSearch_title;
    private LinearLayout mNo_result;
    private RelativeLayout mGuess_like;
    private Handler mHandler;
    private int mCurrentSelectPosition;
    private ArrayList<LessonDetail> mSearchResultList;
    private ArrayList<LessonDetail> mUpdateSearchList;
    private GeneralAdapter mSearchResultAdapter;
    private RelativeLayout mSearch_result;
    private RecyclerViewTV mSearchResultRecyclerList;
    private MainUpView mSearchResultHoverState;
    private RecyclerViewBridge mSearchResultHoverBridge;
    private View mSearchResultOldView;
    private static final int DEFAULT_SEARCH  = 1;
    private static final int NORESULT_SEARCH = 2;
    private static final int SEARCH_RESULT = 3;
    private int search_mode = 1;
    private TextView mPageText;
    private int mTotalPage;
    private int mTotalRows;
    private int mCurrentPage;
    private int mKeyBoardMode = SEARCH_LETTER;
    private static final int SEARCH_NUMBER = 2;
    private static final int SEARCH_LETTER = 1;
    private GeneralAdapter mHotSearchGeneralAdapter;
    private LinearLayout mSearchScrollBg;
    private View mSearchScrollBar;
    private float mSearchScrollBarDistance;
    private RelativeLayout mTmp_layout;
    private boolean isTop = true;


    private Handler mUIHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:    // 图片加载完成
                    mSearchResultRecyclerList.setDefaultSelect(0);
                    break;
                case 2:
                    mSearchResultRecyclerList.setDefaultSelect(mCurrentSelectPosition);
                    break;
                case 3:
                    backToKEyboard();
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.search_page);
        input_tv = (EditText) findViewById(R.id.left_editText);

        skbContainer = (SkbContainer) findViewById(R.id.skbContainer);
        skbContainer.setSkbLayout(R.xml.search_keyboard);

        weekTextView = (TextView) findViewById(R.id.week);
        timeTextView = (TextView) findViewById(R.id.time);
        dateTextView = (TextView) findViewById(R.id.date);
        mSearch_note = (TextView)findViewById(R.id.search_note);
        mSearch_note.setText("搜索提示:如<<高考冲刺>>，输入GKCC");
        mHotSearchRecyclerList = (RecyclerViewTV)findViewById(R.id.hot_list) ;
        mGuessLikeRecyclerList = (RecyclerViewTV)findViewById(R.id.guess_like_list);
        mSearchScrollBg = (LinearLayout) findViewById(R.id.search_scroll_bg);
        mSearchScrollBar = (View) findViewById(R.id.search_scroll_bar);

        mSearch_result = (RelativeLayout)findViewById(R.id.search_result);
        mSearchResultRecyclerList = (RecyclerViewTV)findViewById(R.id.search_result_list);

        mHot_search = (RelativeLayout)findViewById(R.id.hot_search);
        mSearch_title = (TextView)findViewById(R.id.search_title);
        mNo_result = (LinearLayout)findViewById(R.id.no_result);
        mGuess_like = (RelativeLayout)findViewById(R.id.guess_like);
        mPageText = (TextView)findViewById(R.id.pageText);
        mTmp_layout = (RelativeLayout)findViewById(R.id.tmp_layout);

        mGuessLikeList = new ArrayList<>();
        mSearchResultList = new ArrayList<>();
        mUpdateSearchList = new ArrayList<>();
        mHotSearchList = new ArrayList<JsonSearchData.SearchData.Hotsearch_list>();
        search_mode = DEFAULT_SEARCH;

        skbContainer.setFocusable(true);
        skbContainer.setFocusableInTouchMode(true);
        skbContainer.requestFocus();
        setSkbContainerMove();
        //skbContainer.setSelectSofkKeyFront(true); // 设置选中边框最前面.
        skbContainer.setDefualtSelectKey(3,2);

        msgReceiver = new MsgReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(GlobleData.MESSAGE_UPDATE_TIME);
        registerReceiver(msgReceiver, intentFilter);
        getDefaultSearchHttpData(null);

        // 监听键盘事件.
        skbContainer.setOnSoftKeyBoardListener(new SoftKeyBoardListener() {
            @Override
            public void onCommitText(SoftKey softKey) {
                int keyCode = softKey.getKeyCode();
                String keyLabel = softKey.getKeyLabel();

                if (keyCode == KeyEvent.KEYCODE_CLEAR) {
                    input_tv.setText("");
                    mSearchResultList.clear();
                    mSearchResultAdapter.notifyDataSetChanged();
                    //resetListData();
                }else if (keyCode == 250) { //切换数字键盘
                    mKeyBoardMode = SEARCH_NUMBER;
                    setSkbContainerOther();
                    skbContainer.setSkbLayout(R.xml.search_number);
                    setSkbContainerMove();
                } else if (keyCode == 251) { //切换字母键盘
                    mKeyBoardMode = SEARCH_LETTER;
                    setSkbContainerOther();
                    skbContainer.setSkbLayout(R.xml.search_keyboard);
                    setSkbContainerMove();
                }else if(keyCode == KeyEvent.KEYCODE_DEL) {
                    String text = input_tv.getText().toString();
                    if (TextUtils.isEmpty(text)) {
                        Toast.makeText(getApplicationContext(), "文本已空", Toast.LENGTH_LONG).show();
                    } else {
                        input_tv.setText(text.substring(0, text.length() - 1));
                        if (input_tv.getText().length() == 0){
                        }else {
                            sInput = input_tv.getText().toString();
                        }

                    }
                }else{
                    if (!TextUtils.isEmpty(keyLabel)) { // 输入文字.
                        input_tv.setText(input_tv.getText() + softKey.getKeyLabel());
                    }
                }
            }

            @Override
            public void onBack(SoftKey key) {
                finish();
            }


            @Override
            public void onDelete(SoftKey key) {
                String text = input_tv.getText().toString();
                input_tv.setText(text.substring(0, text.length() - 1));
            }

        });

        skbContainer.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (mOldSoftKey != null)
                        skbContainer.setKeySelected(mOldSoftKey);
                    else
                        skbContainer.setDefualtSelectKey(0, 0);
                    mOldSoftKey = skbContainer.getSelectKey();
                    skbContainer.setKeySelected(null);
                } else {
                    if (search_mode == DEFAULT_SEARCH){
                        mHotSearchRecyclerList.setDefaultSelect(0);
                    }else if (search_mode == NORESULT_SEARCH){
                        mGuessLikeRecyclerList.setDefaultSelect(0);
                    }else if (search_mode == SEARCH_RESULT){
                        if (isTop){
                            updateRecyclyerView(1);
                        }
                    }
                    skbContainer.setKeySelected(null);
                }
            }
        });

        input_tv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
               sInput = s.toString();
                if(sInput.isEmpty()){
                    mSearchResultList.clear();
                    mSearchResultAdapter.notifyDataSetChanged();
                    mNo_result.setVisibility(View.GONE);
                    search_mode = DEFAULT_SEARCH;
                    mSearch_title.setText("热门搜索");
                    mHot_search.setVisibility(View.VISIBLE);
                    mGuess_like.setVisibility(View.VISIBLE);
                    mSearch_result.setVisibility(View.INVISIBLE);
                    return;
                }
                search_mode = SEARCH_RESULT;
                mHot_search.setVisibility(View.GONE);
                mNo_result.setVisibility(View.GONE);
                mGuess_like.setVisibility(View.GONE);
                mSearch_result.setVisibility(View.INVISIBLE);
                mSearch_title.setText("搜索结果");
                getSearchHttpData(sInput);
            }
        });

        initHotSearchRecycler();
        initGuessLikeRecycler();
        initSearchResultRecycler();
    }

    private void initSearchListViewItems() {

        // 知识点总数
        mPageText.setText("共" + mSearchResultList.size() + "部");

        // 总页数
        if(mSearchResultList.size() / 8 == 0)
            mTotalPage = 1;
        else
            mTotalPage = mSearchResultList.size() / 8 + (mSearchResultList.size() % 8 == 0 ? 0:1);

        // 总行数
        if(mSearchResultList.size() / 4 == 0)
            mTotalRows = 1;
        else
            mTotalRows = mSearchResultList.size() / 4 + (mSearchResultList.size() % 4 == 0 ? 0:1);

        // 处理进度条
        if(mTotalPage == 1) {
            mSearchScrollBg.setVisibility(View.INVISIBLE);
        } else {
            mSearchScrollBg.setVisibility(View.VISIBLE);
            new Handler().postDelayed(new Runnable(){
                public void run() {
                    ViewGroup.LayoutParams para1;
                    para1 = mSearchScrollBar.getLayoutParams();
                    mSearchScrollBarDistance = mSearchScrollBg.getHeight() / mTotalRows;
                    para1.height = mSearchScrollBg.getHeight() / mTotalRows;

                    mSearchScrollBar.setLayoutParams(para1);
                    mSearchScrollBar.setTranslationY(0);
                }
            }, 200);
        }
    }

    private void getSearchHttpData(String name_key) {
        final String key = XESUtil.getAllFirstLetter(name_key).toUpperCase();

        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put(GlobleData.HTTP_VERSION_KEY, GlobleData.HTTP_VERSION_VALUE);
        paramsMap.put("key", key);
        OkhttpUtil.okHttpGet(GlobleData.HTTP_URL_SEARCH, paramsMap, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                Toast.makeText(mContext, "搜索失败1！", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response) {
                Log.e(TAG, response);
                try {
                    JSONObject obj = new JSONObject(response);
                    if(obj == null) {
                        Toast.makeText(mContext, "搜索失败2！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (Exception e) {
                    Toast.makeText(mContext, "搜索失败3！", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    JsonSearchData resultBean = GsonUtil.fromJson(response, JsonSearchData.class);
                    if(resultBean == null)
                        return;
                    if(resultBean.getResult().equals("0")) {
                        mSearchResultList.clear();
                        if (resultBean.getData().getResult_list().size() == 0 && !key.equals("")){
                            mHot_search.setVisibility(View.GONE);
                            search_mode = NORESULT_SEARCH;
                            mNo_result.setVisibility(View.VISIBLE);
                            mSearch_title.setText("搜索结果");
                            mSearch_result.setVisibility(View.INVISIBLE);
                            mGuess_like.setVisibility(View.VISIBLE);
                        } else if (resultBean.getData().getResult_list().size() != 0){
                            search_mode = SEARCH_RESULT;
                            mHot_search.setVisibility(View.GONE);
                            mNo_result.setVisibility(View.GONE);
                            mGuess_like.setVisibility(View.GONE);
                            mSearch_title.setText("搜索结果");
                            mSearch_result.setVisibility(View.VISIBLE);

                            for (int i = 0;i < resultBean.getData().getResult_list().size();i++){
                                LessonDetail one = new LessonDetail();
                                one.setmLessonImageUrl(resultBean.getData().getResult_list().get(i).getThumb_app_h());
                                one.setmLessonName(resultBean.getData().getResult_list().get(i).getTitle());
                                one.setLibraryID(resultBean.getData().getResult_list().get(i).getLibrary_id());
                                one.setIsCharge(resultBean.getData().getResult_list().get(i).getIs_free());
                                one.setmLessonImageID(R.drawable.load_list);
                                mSearchResultList.add(one);
                           }
                            mPageText.setText("共"+mSearchResultList.size()+"课");
                        }
                        resetData();
                    } else {
                        Toast.makeText(mContext, resultBean.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void resetData(){
        isTop = true;
        mTmp_layout.setVisibility(View.VISIBLE);
        mSearchResultAdapter.notifyDataSetChanged();
        mSearchResultRecyclerList.setAdapter(mSearchResultAdapter);
        initSearchListViewItems();
    }

    private void getDefaultSearchHttpData(final String name_key) {
        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put(GlobleData.HTTP_VERSION_KEY, GlobleData.HTTP_VERSION_VALUE);
        paramsMap.put("key", name_key);
        OkhttpUtil.okHttpGet(GlobleData.HTTP_URL_SEARCH, paramsMap, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                Toast.makeText(mContext, "搜索失败！", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response) {
                Log.e(TAG, response);
                try {
                    JSONObject obj = new JSONObject(response);
                    if(obj == null) {
                        Toast.makeText(mContext, "搜索失败！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (Exception e) {
                    Toast.makeText(mContext, "搜索失败！", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    JsonSearchData resultBean = GsonUtil.fromJson(response, JsonSearchData.class);
                    if(resultBean == null)
                        return;
                    if(resultBean.getResult().equals("0")) {
                        mHotSearchList.clear();
                        for(int i = 0; i < resultBean.getData().getHotsearch_list().size(); ++i) {
                            mHotSearchList.add(resultBean.getData().getHotsearch_list().get(i));
                        }

                        mGuessLikeList.clear();
                        for (int i = 0;i <resultBean.getData().getGuess_list().size();i++){
                            LessonDetail one = new LessonDetail();
                            one.setmLessonImageUrl(resultBean.getData().getGuess_list().get(i).getThumb_app_h());
                            one.setmLessonName(resultBean.getData().getGuess_list().get(i).getTitle());
                            one.setLibraryID(resultBean.getData().getGuess_list().get(i).getLibrary_id());
                            one.setIsCharge(resultBean.getData().getGuess_list().get(i).getIs_free());
                            one.setmLessonImageID(R.drawable.load_list);
                            mGuessLikeList.add(one);
                        }

                        mHotSearchGeneralAdapter.notifyDataSetChanged();
                        mGuessLikeAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(mContext, resultBean.getMessage(), Toast.LENGTH_SHORT).show();
                        mHotSearchGeneralAdapter.notifyDataSetChanged();
                        mGuessLikeAdapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setSkbContainerOther() {
        mOldSoftKey = null;
        skbContainer.setMoveSoftKey(false);
        skbContainer.setSoftKeySelectPadding(0);
        skbContainer.setSelectSofkKeyFront(false);
    }

    private boolean isHotSearchListRowPresenter() {
        GeneralAdapter generalAdapter = (GeneralAdapter) mHotSearchRecyclerList.getAdapter();
        OpenPresenter openPresenter = generalAdapter.getPresenter();
        return (openPresenter instanceof ListRowPresenter);
    }
    private boolean isGuessLikeListRowPresenter() {
        GeneralAdapter generalAdapter = (GeneralAdapter) mGuessLikeRecyclerList.getAdapter();
        OpenPresenter openPresenter = generalAdapter.getPresenter();
        return (openPresenter instanceof ListRowPresenter);
    }

    private boolean isSearchResultListRowPresenter() {
        GeneralAdapter generalAdapter = (GeneralAdapter) mSearchResultRecyclerList.getAdapter();
        OpenPresenter openPresenter = generalAdapter.getPresenter();
        return (openPresenter instanceof ListRowPresenter);
    }

    /*热门搜索*/
    private void initHotSearchRecycler() {
        HotSearchGirdLayoutManager gridlayoutManager = new HotSearchGirdLayoutManager(mContext, 3); // 解决快速长按焦点丢失问题.
        gridlayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        mHotSearchRecyclerList.setLayoutManager(gridlayoutManager);
        mHotSearchRecyclerList.setFocusable(false);
        mHotSearchRecyclerList.setSelectedItemAtCentered(true); // 设置item在中间移动

        HotSearchPresenter mRecyclerViewPresenter = new HotSearchPresenter(mContext, mHotSearchList);
        mHotSearchGeneralAdapter = new GeneralAdapter(mRecyclerViewPresenter);
        mHotSearchRecyclerList.setAdapter(mHotSearchGeneralAdapter);

        mHotSearchRecyclerList.addItemDecoration(new CourseRecyclerItemSpace((int)getDimension(R.dimen.w_20) , (int)getDimension(R.dimen.w_10), (int)getDimension(R.dimen.w_21), (int)getDimension(R.dimen.w_51)));

        gridlayoutManager.setLeftKeyListener(new HotSearchGirdLayoutManager.OnLeftKeyListener() {
            @Override
            public void onLeftKey() {
                //backToKEyboard();
                updateRecyclyerView(3);
            }
        });

        gridlayoutManager.setDownKeyListener(new HotSearchGirdLayoutManager.OnDownKeyListener() {
            @Override
            public void onDownKey() {
            }
        });

        initHotSearchHover();
    }

    private void initHotSearchHover() {
        mHotSearchHoverState = (MainUpView) findViewById(R.id.guess_search_upview);
        mHotSearchHoverState.setEffectBridge(new RecyclerViewBridge());
        mHotSearchHoverBridge = (RecyclerViewBridge) mHotSearchHoverState.getEffectBridge();

        mHotSearchRecyclerList.setOnItemListener(new RecyclerViewTV.OnItemListener() {

            @Override
            public void onItemPreSelected(RecyclerViewTV parent, View itemView, int position) {
                if (!isHotSearchListRowPresenter()) {
                    mHotSearchHoverBridge.setUnFocusView(mHotSearchOldView);
                    mHotSearchHoverBridge.setVisibleWidget(true);
                    TextView mText = (TextView) itemView.findViewById(R.id.course_name);
                    mText.setSelected(false);
                    mText.setTextColor(0xfff1f1f1);
                }
            }

            @Override
            public void onItemSelected(RecyclerViewTV parent, View itemView, int position) {

                if (!isHotSearchListRowPresenter()) {
                    if(mHotSearchHoverBridge.isVisibleWidget()) {
                        mHotSearchHoverBridge.setVisibleWidget(false);
                    }
                    mHotSearchHoverBridge.setFocusView(itemView, 1.1f);
                    TextView mText = (TextView) itemView.findViewById(R.id.course_name);
                    mText.setSelected(true);
                    mText.setTextColor(0xff0e1933);
                    mHotSearchOldView = itemView;

                }
            }

            @Override
            public void onReviseFocusFollow(RecyclerViewTV parent, View itemView, int position) {

                if (!isHotSearchListRowPresenter()) {
                    if(mHotSearchHoverBridge.isVisibleWidget()) {
                        mHotSearchHoverBridge.setVisibleWidget(false);
                    }
                    mHotSearchHoverBridge.setFocusView(itemView, 1.1f);
                    TextView mText = (TextView) itemView.findViewById(R.id.course_name);
                    mText.setSelected(true);
                    mText.setTextColor(0xff0e1933);
                    mHotSearchOldView = itemView;
                }

            }
        });

        mHotSearchRecyclerList.setOnItemClickListener(new RecyclerViewTV.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerViewTV parent, View itemView, int position) {
                Intent intent = new Intent(searchPageActivity.this, DetailActivity.class);
                intent.putExtra("libraryId",Integer.valueOf(mHotSearchList.get(position).getLibrary_id()).intValue());
                startActivity(intent);
            }
        });
    }

    /*猜你喜欢*/
    private void initGuessLikeRecycler() {
        GuessLikeGridLayoutManger gridlayoutManager = new GuessLikeGridLayoutManger(mContext,1); // 解决快速长按焦点丢失问题.
        gridlayoutManager.setOrientation(GridLayoutManager.HORIZONTAL);
        mGuessLikeRecyclerList.setLayoutManager(gridlayoutManager);
        mGuessLikeRecyclerList.setFocusable(false);
        mGuessLikeRecyclerList.setSelectedItemAtCentered(true); // 设置item在中间移动

        GuessLikePresenter mRecyclerViewPresenter = new GuessLikePresenter(mContext, mGuessLikeList);
        mGuessLikeAdapter = new GeneralAdapter(mRecyclerViewPresenter);
        mGuessLikeRecyclerList.setAdapter(mGuessLikeAdapter);
        mGuessLikeRecyclerList.addItemDecoration(new GuessLikeSpacing((int)getDimension(R.dimen.w_20),(int)getDimension(R.dimen.w_20), (int)getDimension(R.dimen.w_20), (int)getDimension(R.dimen.w_25)));
        gridlayoutManager.setLeftKeyListener(new GuessLikeGridLayoutManger.OnLeftKeyListener() {
            @Override
            public void onLeftKey() {
                backToKEyboard();
            }
        });
        initGuessLikeHover();
    }

    private void initGuessLikeHover() {
        mGuessLikeHoverState = (MainUpView) findViewById(R.id.guess_like_upview);
        mGuessLikeHoverState.setEffectBridge(new RecyclerViewBridge());
        mGuessLikeHoverBridge = (RecyclerViewBridge) mGuessLikeHoverState.getEffectBridge();
        mGuessLikeHoverBridge.setUpRectResource(R.drawable.yellow_white_border);
        float density = getResources().getDisplayMetrics().density;
        RectF rectf = new RectF(getDimension(R.dimen.w_13) * density, getDimension(R.dimen.h_11) * density,
                getDimension(R.dimen.w_12) * density, getDimension(R.dimen.h_11) * density);
        mGuessLikeHoverBridge.setDrawUpRectPadding(rectf);

        mGuessLikeRecyclerList.setOnItemListener(new RecyclerViewTV.OnItemListener() {

            @Override
            public void onItemPreSelected(RecyclerViewTV parent, View itemView, int position) {
                if (!isGuessLikeListRowPresenter()) {
                    mGuessLikeHoverBridge.setUnFocusView(mGuessLikeOldView);
                    mGuessLikeHoverBridge.setVisibleWidget(true);
                }
            }

            @Override
            public void onItemSelected(RecyclerViewTV parent, View itemView, int position) {
                if (!isGuessLikeListRowPresenter()) {
                    if(mGuessLikeHoverBridge.isVisibleWidget()) {
                        mGuessLikeHoverBridge.setVisibleWidget(false);
                    }
                    mGuessLikeHoverBridge.setFocusView(itemView, 1.1f);
                    mGuessLikeOldView = itemView;

                }
            }

            @Override
            public void onReviseFocusFollow(RecyclerViewTV parent, View itemView, int position) {

                if (!isGuessLikeListRowPresenter()) {
                    if(mGuessLikeHoverBridge.isVisibleWidget()) {
                        mGuessLikeHoverBridge.setVisibleWidget(false);
                    }
                    mGuessLikeHoverBridge.setFocusView(itemView, 1.1f);
                    mGuessLikeOldView = itemView;
                }

            }
        });

        mGuessLikeRecyclerList.setOnItemClickListener(new RecyclerViewTV.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerViewTV parent, View itemView, int position) {
                Intent intent = new Intent(searchPageActivity.this, DetailActivity.class);
                intent.putExtra("libraryId",Integer.valueOf(mGuessLikeList.get(position).getLibraryID()).intValue());
                startActivity(intent);
            }
        });
    }

    /*搜索结果*/
    private void initSearchResultRecycler() {
        GridLayoutManagerTV gridlayoutManager = new GridLayoutManagerTV(mContext,4); // 解决快速长按焦点丢失问题.
        gridlayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        mSearchResultRecyclerList.setLayoutManager(gridlayoutManager);
        mSearchResultRecyclerList.setFocusable(false);
        //mSearchResultRecyclerList.setSelectedItemAtCentered(true); // 设置item在中间移动
        mSearchResultRecyclerList.setSelectedItemOffset(0,(int)getDimension(R.dimen.h_200));

        SearchResultPresenter mRecyclerViewPresenter = new SearchResultPresenter(mContext, mSearchResultList);
        mSearchResultAdapter = new GeneralAdapter(mRecyclerViewPresenter);

        mSearchResultRecyclerList.setAdapter(mSearchResultAdapter);
        mSearchResultRecyclerList.addItemDecoration(new GuessLikeSpacing((int)getDimension(R.dimen.w_20),(int)getDimension(R.dimen.w_20), (int)getDimension(R.dimen.w_20), (int)getDimension(R.dimen.w_30)));

        gridlayoutManager.setUpKeyListener(new GridLayoutManagerTV.OnUpKeyListener() {
            @Override
            public void onUpKey(int position) {
                input_tv.setFocusable(false);
                if(position < 4) {
                    isTop  = true;
                    mTmp_layout.setVisibility(View.VISIBLE);
                    mSearchScrollBar.setTranslationY(0);
                    return;
                }
                int iTranslation = (int)(mSearchScrollBar.getTranslationY() - mSearchScrollBarDistance);
                if(iTranslation < 0) {
                    return;
                }
                mSearchScrollBar.setTranslationY(iTranslation);
            }
        });
        gridlayoutManager.setDownKeyListener(new GridLayoutManagerTV.OnDownKeyListener() {
            @Override
            public void onDownKey(int position) {
                isTop = false;
                mTmp_layout.setVisibility(View.GONE);
                if(mSearchResultList.size() % 4 == 0) {
                    if(position >= (mSearchResultList.size() / 4 - 1) * 4) {
                        mSearchScrollBar.setTranslationY(mSearchScrollBg.getHeight() - mSearchScrollBar.getHeight());
                        return;
                    }
                } else {
                    if(position >= (mSearchResultList.size() / 4) * 4) {
                        mSearchScrollBar.setTranslationY(mSearchScrollBg.getHeight() - mSearchScrollBar.getHeight());
                        return;
                    }
                }
                int iTranslation = (int)(mSearchScrollBar.getTranslationY() + mSearchScrollBarDistance);
                if(iTranslation > mSearchScrollBg.getHeight() - mSearchScrollBar.getHeight()) {
                    return;
                }
                mSearchScrollBar.setTranslationY(iTranslation);

            }
        });
        gridlayoutManager.setLeftKeyListener(new GridLayoutManagerTV.OnLeftKeyListener() {
            @Override
            public void onLeftKey() {
                input_tv.setFocusable(true);
                //backToKEyboard();
                updateRecyclyerView(3);
            }
        });

        initSearchResultHover();
    }

    private void initSearchResultHover() {
        mSearchResultHoverState = (MainUpView) findViewById(R.id.search_result_upview);
        mSearchResultHoverState.setEffectBridge(new RecyclerViewBridge());
        mSearchResultHoverBridge = (RecyclerViewBridge) mSearchResultHoverState.getEffectBridge();
        mSearchResultHoverBridge.setUpRectResource(R.drawable.yellow_white_border);
        float density = getResources().getDisplayMetrics().density;
        RectF rectf = new RectF(getDimension(R.dimen.w_13) * density, getDimension(R.dimen.h_11) * density,
                getDimension(R.dimen.w_12) * density, getDimension(R.dimen.h_11) * density);
        mSearchResultHoverBridge.setDrawUpRectPadding(rectf);

        mSearchResultRecyclerList.setOnItemClickListener(new RecyclerViewTV.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerViewTV parent, View itemView, int position) {
                Intent intent = new Intent(searchPageActivity.this, DetailActivity.class);
                intent.putExtra("libraryId",Integer.valueOf(mSearchResultList.get(position).getLibraryID()).intValue());
                startActivity(intent);
            }
        });

        mSearchResultRecyclerList.setOnItemListener(new RecyclerViewTV.OnItemListener() {

            @Override
            public void onItemPreSelected(RecyclerViewTV parent, View itemView, int position) {
                if (!isSearchResultListRowPresenter()) {
                    mSearchResultHoverBridge.setUnFocusView(mSearchResultOldView);
                    mSearchResultHoverBridge.setVisibleWidget(true);
                }

                TextView mText = (TextView) itemView.findViewById(R.id.search_result_detail_name);
                mText.setSelected(true);
                mText.setMaxLines(1);
            }

            @Override
            public void onItemSelected(RecyclerViewTV parent, View itemView, int position) {
                mCurrentSelectPosition  = position;
                if (!isSearchResultListRowPresenter()) {
                    if(mSearchResultHoverBridge.isVisibleWidget()) {
                        mSearchResultHoverBridge.setVisibleWidget(false);
                    }
                    mSearchResultHoverBridge.setFocusView(itemView, 1.1f);

                    TextView mText = (TextView) itemView.findViewById(R.id.search_result_detail_name);
                    mText.setSelected(true);
                    mText.setMaxLines(2);
                    mSearchResultOldView = itemView;

                }
            }

            @Override
            public void onReviseFocusFollow(RecyclerViewTV parent, View itemView, int position) {
                if (!isSearchResultListRowPresenter()) {
                    if(mSearchResultHoverBridge.isVisibleWidget()) {
                        mSearchResultHoverBridge.setVisibleWidget(false);
                    }
                    TextView mText = (TextView) itemView.findViewById(R.id.search_result_detail_name);
                    mText.setSelected(true);
                    mText.setMaxLines(2);
                    mSearchResultHoverBridge.setFocusView(itemView, 1.1f);
                    mSearchResultOldView = itemView;
                }
            }
        });
    }

    private void backToKEyboard() {
        skbContainer.setFocusable(true);
        skbContainer.setFocusableInTouchMode(true);
        skbContainer.requestFocus();
        setSkbContainerMove();
        if (mKeyBoardMode == SEARCH_NUMBER){
            skbContainer.setDefualtSelectKey(1,2);
        }else
            skbContainer.setDefualtSelectKey(3,2);
    }

    private void updateRecyclyerView(final int position){
        HandlerThread handlerThread = new HandlerThread("updateview");//创建一个handlerThread线程
        handlerThread.start();//启动该线程
        mHandler =  new Handler(handlerThread.getLooper()) {
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                mUIHandler.sendEmptyMessage(position);
            }
        };

        Message msg = new Message();
        mHandler.sendMessage(msg);
    }

    private void setSkbContainerMove() {
        mOldSoftKey = null;
        skbContainer.setMoveSoftKey(true); // 设置是否移动按键边框.
        skbContainer.setMoveDuration(200); // 设置移动边框的时间(默认:300)
        skbContainer.setSelectSofkKeyFront(true); // 设置选中边框在最前面.
    }

    public float getDimension(int id) {
        return getResources().getDimension(id);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (skbContainer.onSoftKeyDown(keyCode, event))
            return true;
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (skbContainer.onSoftKeyUp(keyCode, event))
            return true;
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
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

            } catch (Exception e) {
                Log.e(TAG, "" + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
