package tv.dfyc.yckt;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.open.androidtvwidget.bridge.RecyclerViewBridge;
import com.open.androidtvwidget.leanback.adapter.GeneralAdapter;
import com.open.androidtvwidget.leanback.mode.ListRowPresenter;
import com.open.androidtvwidget.leanback.mode.OpenPresenter;
import com.open.androidtvwidget.leanback.recycle.RecyclerViewTV;
import com.open.androidtvwidget.view.MainUpView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import tv.dfyc.yckt.adapter.ClassListAdapter;
import tv.dfyc.yckt.adapter.LessonListAdapter;
import tv.dfyc.yckt.adapter.DetailPresenter;
import tv.dfyc.yckt.detail.DetailActivity;
import tv.dfyc.yckt.network.CallBackUtil;
import tv.dfyc.yckt.network.GsonUtil;
import tv.dfyc.yckt.network.OkhttpUtil;
import tv.dfyc.yckt.util.GlobleData;
import tv.dfyc.yckt.custom.GridLayoutManagerListPage;
import tv.dfyc.yckt.beans.JsonLessonData;
import tv.dfyc.yckt.util.LessonDetail;
import tv.dfyc.yckt.custom.LinearLayoutManagerClass;
import tv.dfyc.yckt.custom.LinearLayoutManagerLesson;
import tv.dfyc.yckt.beans.JsonListPage;
import tv.dfyc.yckt.custom.MarqueeTextView;
import tv.dfyc.yckt.custom.RecyclerItemSpacing;
import tv.dfyc.yckt.custom.VerticalSpacing;

public class ListPageActivity extends Activity implements RecyclerViewTV.OnItemListener {
    private final static String TAG = "ListPageActivity";
    private Context mContext;
    private int mGradeIndex = GlobleData.GRADE_TYPE_PRIMARY; // 小学;
    private JsonListPage mListPageJson = null;
    private JsonLessonData mListLessonsJson = null;
    private String mCurrentLibraryId;
    private int mCurrentClassId = 0;
    private List<String> mClassListData;
    private List<String> mLessonListData;
    private List<LessonDetail> mDetailListData;
    private RecyclerViewTV mClassListView;
    private RecyclerItemSpacing mClassDecoration;
    private RecyclerItemSpacing mLessonDecoration;
    private LinearLayoutManagerClass mClassLayoutManager;
    private RecyclerViewTV mLessonListView;
    private LinearLayoutManagerLesson mLessonLayoutManager;
    private RecyclerViewTV mDetailListView;
    private GridLayoutManagerListPage mGridlayoutManager;
    private GeneralAdapter mDetailListAdapter;
    private MainUpView mDetailHoverState;
    private RecyclerViewBridge mDetailHoverBridge;
    private ClassListAdapter mClassAdapter;
    private LessonListAdapter mLessonAdapter;
    private View mOldView;
    private LinearLayout mScrollBg;
    private View mScrollBar;
    private float mScrollBarDistance;
    private int mTotalPage;
    private int mTotalRows;
    private TextView mPageText;
    private MarqueeTextView mMessageText;

    private TextView mWeekTextView;
    private TextView mTimeTextView;
    private TextView mDateTextView;
    private MsgReceiver mMsgReceiver;

    private int mTotalItemHeight;
    private int mEachItemHeight;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_list_page);
        mClassListData = new ArrayList<>();
        mLessonListData = new ArrayList<>();
        mDetailListData = new ArrayList<>();
        mTotalItemHeight = (int) getDimension(R.dimen.h_776);
        mEachItemHeight = (int) getDimension(R.dimen.h_56);

        initView();
        initClassRecyclerView(mClassListData);
        initLessonRecyclerView(mLessonListData);
        initDetailRecycler();

        initIntent();

        mMsgReceiver = new MsgReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(GlobleData.MESSAGE_UPDATE_TIME);
        registerReceiver(mMsgReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mMsgReceiver);
    }

    private void initIntent() {
        // 年级数据
        Intent intent = this.getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                mListPageJson = (JsonListPage) bundle.getSerializable("class_list");

                // 小学、初中、高中索引
                mGradeIndex = bundle.getInt("grade_index", GlobleData.GRADE_TYPE_PRIMARY);

                // 年级
                mCurrentClassId = bundle.getInt("class_index", 0);

                if (mListPageJson != null && mListPageJson.getResult() == 0 ) {

                    // 默认选中某个年级
                    mClassAdapter.setSelectedPositon(mCurrentClassId);

                    // 年级列表
                    resetClassData();

                    // 默认显示某年级的课程列表
                    resetLessonData(mCurrentClassId);

                    // 默认显示一年级语文的课程列表
                    mCurrentLibraryId = mListPageJson.getData().getLists().get(mGradeIndex).getLists().get(mCurrentClassId).getLists().get(0).getNode_id()+"";
                    if (mDetailListData != null) {
                        new Handler().postDelayed(new Runnable(){
                            public void run() {
                                getDetailDataFromHttp(mCurrentLibraryId);
                            }
                        }, 300);
                    }
                }
            }
        }
    }

    public void resetClassData() {
        if (mClassListData != null && mLessonListData != null) {
            mClassListData.clear();

            for (int i = 0; i< mListPageJson.getData().getLists().get(mGradeIndex).getLists().size() - 1;i++){
                String sNodeName = mListPageJson.getData().getLists().get(mGradeIndex).getLists().get(i).getNode_name();
                mClassListData.add(sNodeName);
            }
            mClassAdapter.notifyDataSetChanged();
            dynamicLayoutClassItems();
        }
    }

    public void resetLessonData(int pos) {
        if (mLessonListData != null) {
            mLessonListData.clear();

            for(int j = 0; j < mListPageJson.getData().getLists().get(mGradeIndex).getLists().get(pos).getLists().size();j++){
                mLessonListData.add(mListPageJson.getData().getLists().get(mGradeIndex).getLists().get(pos).getLists().get(j).getNode_name());
            }
            new Handler().postDelayed(new Runnable(){
                public void run() {
                    mLessonAdapter.notifyDataSetChanged();
                    dynamicLayoutLessonItems();
                }
            }, 200);
        }
    }

    public void resetDetailData() {
        mDetailListAdapter.notifyDataSetChanged();
        updateDetailListData();
        mGridlayoutManager.scrollToPosition(0);
    }

    private void updateDetailListData() {
        // 知识点总数
        mPageText.setText("共" + mDetailListData.size() + "部");

        // 总页数
        if(mDetailListData.size() / 8 == 0)
            mTotalPage = 1;
        else
            mTotalPage = mDetailListData.size() / 8 + (mDetailListData.size() % 8 == 0 ? 0:1);

        // 总行数
        if(mDetailListData.size() / 4 == 0)
            mTotalRows = 1;
        else
            mTotalRows = mDetailListData.size() / 4 + (mDetailListData.size() % 4 == 0 ? 0:1);

        // 处理进度条
        if(mTotalPage == 1) {
            mScrollBg.setVisibility(View.INVISIBLE);
        } else {
            mScrollBg.setVisibility(View.VISIBLE);
            new Handler().postDelayed(new Runnable(){
                public void run() {
                    ViewGroup.LayoutParams para1;
                    para1 = mScrollBar.getLayoutParams();
                    mScrollBarDistance = mScrollBg.getHeight() / mTotalRows;
                    para1.height = mScrollBg.getHeight() / mTotalRows;

                    mScrollBar.setLayoutParams(para1);
                    mScrollBar.setTranslationY(0);
                }
            }, 200);
        }
    }

    private void getDetailDataFromHttp(String node_id) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put(GlobleData.HTTP_VERSION_KEY,GlobleData.HTTP_VERSION_VALUE);
        paramsMap.put(GlobleData.HTTP_PARAM,node_id);

        OkhttpUtil.okHttpGet(GlobleData.HTTP_URL_CHANNEL_SELECTION, paramsMap, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                Log.d(TAG,"连接失败！");
            }
            @Override
            public void onResponse(String response) {
                try {
                    mListLessonsJson = GsonUtil.fromJson(response, JsonLessonData.class);
                    if (mListLessonsJson.getResult() == 0) {
                        mDetailListData.clear();
                        if(mListLessonsJson.getData() == null || mListLessonsJson.getData().getList() == null)
                            return;
                        // 跑马灯
                        mMessageText.setText(mListLessonsJson.getData().getNotice());

                        // 列表数据
                        for (int i = 0 ; i< mListLessonsJson.getData().getList().size();i++) {
                            LessonDetail one = new LessonDetail();
                            one.setmLessonImageUrl(mListLessonsJson.getData().getList().get(i).getThumb_app_h());
                            one.setmLessonName(mListLessonsJson.getData().getList().get(i).getTitle());
                            one.setLibraryID(mListLessonsJson.getData().getList().get(i).getLibrary_id());
                            one.setmLessonImageID(R.drawable.load_list);
                            one.setIsCharge(mListLessonsJson.getData().getList().get(i).getIs_free());
                            mDetailListData.add(one);
                        }
                        resetDetailData();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initView() {
        mClassListView = (RecyclerViewTV) findViewById(R.id.list_page_grade_recycler);
        mLessonListView = (RecyclerViewTV) findViewById(R.id.list_page_lesson_recycler);
        mDetailListView = (RecyclerViewTV) findViewById(R.id.list_page_detail_recycler);
        mWeekTextView = (TextView) findViewById(R.id.week);
        mTimeTextView = (TextView) findViewById(R.id.time);
        mDateTextView = (TextView) findViewById(R.id.date);
        mScrollBg = (LinearLayout) findViewById(R.id.scroll_bg);
        mScrollBar = (View) findViewById(R.id.scroll_bar);
        mPageText = (TextView) findViewById(R.id.page_text);
        mMessageText = (MarqueeTextView) findViewById(R.id.message_text);
    }

    private void initClassRecyclerView(List<String> list) {
        mClassLayoutManager = new LinearLayoutManagerClass(mContext);
        mClassLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mClassLayoutManager.setScrollEnabled(false);
        mClassListView.setLayoutManager(mClassLayoutManager);
        mClassListView.setFocusable(false);
        mClassAdapter = new ClassListAdapter(mContext, list, mClassListView);
        mClassListView.setAdapter(mClassAdapter);
        dynamicLayoutClassItems();

        mClassAdapter.setOnItemSelectListener(new ClassListAdapter.OnItemSelectListener() {
            @Override
            public void onItemSelect(View view, int position) {
                // 选择了一个年级
                mLessonAdapter.SetClassClicked(true);

                mCurrentClassId = position;
                resetLessonData(position);
            }
        });

        mClassLayoutManager.setRightKeyListener(new LinearLayoutManagerClass.OnRightKeyListener() {
            @Override
            public void onRightKey(View focused) {
                mClassAdapter.setSelectedBackground(focused);
            }
        });
    }

    private void initLessonRecyclerView(List<String> list) {
        mLessonLayoutManager = new LinearLayoutManagerLesson(mContext);
        mLessonLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mLessonLayoutManager.setScrollEnabled(false);
        mLessonListView.setLayoutManager(mLessonLayoutManager);
        mLessonListView.setFocusable(false);
        mLessonAdapter = new LessonListAdapter(mContext, list, mLessonListView);
        mLessonListView.setAdapter(mLessonAdapter);
        mClassLayoutManager.setFocusFailedAdapter(mLessonAdapter);
        dynamicLayoutLessonItems();

        mLessonAdapter.setOnItemSelectListener(new LessonListAdapter.OnItemSelectListener() {
            @Override
            public void onItemSelect(View view, int position) {
                String new_library_id = mListPageJson.getData().getLists().get(mGradeIndex).getLists().get(mCurrentClassId).getLists().get(position).getNode_id()+"";
                if(mCurrentLibraryId != null && !mCurrentLibraryId.equals(new_library_id)) {
                    mCurrentLibraryId = new_library_id;
                    getDetailDataFromHttp(mCurrentLibraryId);
                }
            }
        });

        mLessonLayoutManager.setLeftKeyListener(new LinearLayoutManagerLesson.OnLeftKeyListener() {
            @Override
            public void onLeftKey(View focused) {
                mLessonAdapter.setSelectedBackground(focused);
            }
        });

        mLessonLayoutManager.setRightKeyListener(new LinearLayoutManagerLesson.OnRightKeyListener() {
            @Override
            public void onRightKey(View focused) {
                mLessonAdapter.setSelectedBackground(focused);
            }
        });
    }

    private void initDetailRecycler() {
        mGridlayoutManager = new GridLayoutManagerListPage(mContext, 4); // 解决快速长按焦点丢失问题.
        mGridlayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        mDetailListView.setLayoutManager(mGridlayoutManager);
        mDetailListView.setFocusable(false);
        //mDetailListView.setSelectedItemAtCentered(true); // 设置item在中间移动(第二行左右移动时晃动)
        mDetailListView.setSelectedItemOffset(0,(int) getDimension(R.dimen.h_200));
        mGridlayoutManager.setFocusFailedAdapter(null, mLessonAdapter);
        mLessonLayoutManager.setFocusFailedLayoutManager(mGridlayoutManager);

        DetailPresenter mRecyclerViewPresenter = new DetailPresenter(mContext, mDetailListData);
        mDetailListAdapter = new GeneralAdapter(mRecyclerViewPresenter);
        mDetailListView.setAdapter(mDetailListAdapter);
        mDetailListView.addItemDecoration(new VerticalSpacing((int)getDimension(R.dimen.w_20) ,(int)getDimension(R.dimen.w_20), (int)getDimension(R.dimen.w_20), (int)getDimension(R.dimen.w_20)));

        mGridlayoutManager.setUpKeyListener(new GridLayoutManagerListPage.OnUpKeyListener() {
            @Override
            public void onUpKey(int position) {
                scrollBarUp(position);
            }
        });

        mGridlayoutManager.setDownKeyListener(new GridLayoutManagerListPage.OnDownKeyListener() {
            @Override
            public void onDownKey(int position) {
                scrollBarDown(position);
            }
        });

        mGridlayoutManager.setLeftKeyListener(new GridLayoutManagerListPage.OnLeftKeyListener() {
            @Override
            public void onLeftKey(int position) {
            }
        });

        updateDetailListData();
        initDetailHover();
    }

    private void initDetailHover() {
        mDetailHoverState = (MainUpView) findViewById(R.id.primary_detail_upview);
        mDetailHoverState.setEffectBridge(new RecyclerViewBridge());
        mDetailHoverBridge = (RecyclerViewBridge) mDetailHoverState.getEffectBridge();
        mDetailHoverBridge.setUpRectResource(R.drawable.yellow_white_border);
        float density = getResources().getDisplayMetrics().density;
        RectF rectf = new RectF(getDimension(R.dimen.w_13) * density, getDimension(R.dimen.h_11) * density,
                getDimension(R.dimen.w_12) * density, getDimension(R.dimen.h_11) * density);
        mDetailHoverBridge.setDrawUpRectPadding(rectf);

        mDetailListView.setOnItemListener(this);
        // item 单击事件处理.
        mDetailListView.setOnItemClickListener(new RecyclerViewTV.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerViewTV parent, View itemView, int position) {
                if (mListPageJson != null && mDetailListData.size() != 0) {
                    startDetailActivity(mDetailListData.get(position).getLibraryID());
                }
            }
        });
    }

    private void startDetailActivity(int library_id) {
        Intent intent = new Intent(ListPageActivity.this, DetailActivity.class);
        intent.putExtra("libraryId",library_id);
        startActivity(intent);
    }

    /**
     * 排除 Leanback demo的RecyclerView.
     */
    private boolean isListRowPresenter() {
        GeneralAdapter generalAdapter = (GeneralAdapter) mDetailListView.getAdapter();
        OpenPresenter openPresenter = generalAdapter.getPresenter();
        return (openPresenter instanceof ListRowPresenter);
    }

    @Override
    public void onItemPreSelected(RecyclerViewTV parent, View itemView, int position) {
        if (!isListRowPresenter()) {
            mDetailHoverBridge.setUnFocusView(mOldView);
            mDetailHoverBridge.setVisibleWidget(true);
            if(mOldView != null) {
                TextView oldTitleText = (TextView) mOldView.findViewById(R.id.primary_detail_name);
                oldTitleText.setMaxLines(1);
            }
        }
    }

    @Override
    public void onItemSelected(RecyclerViewTV parent, View itemView, int position) {
        if (!isListRowPresenter()) {
            if(mDetailHoverBridge.isVisibleWidget()) {
                mDetailHoverBridge.setVisibleWidget(false);
            }
            mDetailHoverBridge.setFocusView(itemView, 1.1f);
            if(mOldView != null) {
                TextView oldTitleText = (TextView) mOldView.findViewById(R.id.primary_detail_name);
                oldTitleText.setMaxLines(1);
            }
            mOldView = itemView;
            TextView titleText = (TextView) itemView.findViewById(R.id.primary_detail_name);
            titleText.setMaxLines(2);
        }
    }

    @Override
    public void onReviseFocusFollow(RecyclerViewTV parent, View itemView, int position) {
        if (!isListRowPresenter()) {
            if(mDetailHoverBridge.isVisibleWidget()) {
                mDetailHoverBridge.setVisibleWidget(false);
            }
            mDetailHoverBridge.setFocusView(itemView, 1.1f);
            if(mOldView != null) {
                TextView oldTitleText = (TextView) mOldView.findViewById(R.id.primary_detail_name);
                oldTitleText.setMaxLines(1);
            }
            mOldView = itemView;
            TextView titleText = (TextView) itemView.findViewById(R.id.primary_detail_name);
            titleText.setMaxLines(2);
        }
    }

    private void scrollBarDown(int position) {
        if(mDetailListData.size() % 4 == 0) {
            if(position >= (mDetailListData.size() / 4 - 1) * 4) {
                mScrollBar.setTranslationY(mScrollBg.getHeight() - mScrollBar.getHeight());
                return;
            }
        } else {
            if(position >= (mDetailListData.size() / 4) * 4) {
                mScrollBar.setTranslationY(mScrollBg.getHeight() - mScrollBar.getHeight());
                return;
            }
        }
        int iTranslation = (int)(mScrollBar.getTranslationY() + mScrollBarDistance);
        if(iTranslation > mScrollBg.getHeight() - mScrollBar.getHeight()) {
            return;
        }
        mScrollBar.setTranslationY(iTranslation);
    }

    private void scrollBarUp(int position) {
        if(position < 4) {
            mScrollBar.setTranslationY(0);
            return;
        }
        int iTranslation = (int)(mScrollBar.getTranslationY() - mScrollBarDistance);
        if(iTranslation < 0) {
            return;
        }
        mScrollBar.setTranslationY(iTranslation);
    }

    public void dynamicLayoutClassItems() {
        if(mClassListData.size() == 0)
            return;
        if(mClassDecoration != null)
            mClassListView.removeItemDecoration(mClassDecoration);
        int iItemsCalc = mClassListData.size();
        int iTotalCalc = iItemsCalc * mEachItemHeight;
        if(iTotalCalc > mTotalItemHeight)
            iTotalCalc = mTotalItemHeight;
        int iSpan = mTotalItemHeight - iTotalCalc;

        if(iItemsCalc >= 8) {
            int iOneSpan = iSpan / (iItemsCalc*2);
            mClassDecoration = new RecyclerItemSpacing(0, iOneSpan, 0, 2 * iOneSpan);
        } else {
            int iOneSpan = (iItemsCalc - 1) * (int) getDimension(R.dimen.h_30) * 2;
            iOneSpan = (iSpan - iOneSpan) / 2;
            if(iOneSpan < 0)
                iOneSpan = 0;
            mClassDecoration = new RecyclerItemSpacing(0, iOneSpan, 0, (int) getDimension(R.dimen.h_60));
        }
        mClassListView.addItemDecoration(mClassDecoration);
    }

    public void dynamicLayoutLessonItems() {
        if(mLessonListData.size() == 0)
            return;
        if(mLessonDecoration != null)
            mLessonListView.removeItemDecoration(mLessonDecoration);
        int iItemsCalc = mLessonListData.size();
        int iTotalCalc = iItemsCalc * mEachItemHeight;
        if(iTotalCalc > mTotalItemHeight)
            iTotalCalc = mTotalItemHeight;
        int iSpan = mTotalItemHeight - iTotalCalc;

        if(iItemsCalc >= 8) {
            int iOneSpan = iSpan / (iItemsCalc*2);
            mLessonDecoration = new RecyclerItemSpacing(0, iOneSpan, 0, 2 * iOneSpan);
        } else {
            int iOneSpan = (iItemsCalc - 1) * (int) getDimension(R.dimen.h_30) * 2;
            iOneSpan = (iSpan - iOneSpan) / 2;
            if(iOneSpan < 0)
                iOneSpan = 0;
            mLessonDecoration = new RecyclerItemSpacing(0, iOneSpan, 0, (int) getDimension(R.dimen.h_60));
        }
        mLessonListView.addItemDecoration(mLessonDecoration);
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
                    mDateTextView.setText(sTimeData.split(" ")[0]);
                    mTimeTextView.setText(sTimeData.split(" ")[1]);
                    mWeekTextView.setText(sTimeData.split(" ")[2]);
                }
            } catch (Exception e) {
                Log.e(TAG, "" + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
