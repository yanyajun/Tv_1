package tv.dfyc.yckt;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import tv.dfyc.yckt.adapter.FeedbackAdapter;
import tv.dfyc.yckt.network.CallBackUtil;
import tv.dfyc.yckt.network.GsonUtil;
import tv.dfyc.yckt.network.OkhttpUtil;
import tv.dfyc.yckt.util.PreferencesUtil;
import tv.dfyc.yckt.custom.FeedbackItemSpacing;
import tv.dfyc.yckt.util.GlobleData;
import tv.dfyc.yckt.custom.GridLayoutManagerFeedback;
import tv.dfyc.yckt.beans.JsonCommonResult;
import tv.dfyc.yckt.beans.JsonFeedBackData;

import static tv.dfyc.yckt.util.GlobleData.HTTP_URL_FEEDBACK;

/**
 * Created by admin on 2017-9-25.
 */

public class FeedBackActivity extends Activity {
    private final static String TAG="FeedBackActivity";

    private TextView mAsk_content;
    private RecyclerView mFeedback_recyclerview;
    private Context mContext;
    private LinearLayout mCommit;
    private LinearLayout mBack;
    private FeedbackAdapter mFeedbackAdapter;
    private TextView mAsk_phone;
    private ArrayList<String> mFeedBackList = new ArrayList<String>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        mContext = this;
        initView();
        getFeedBackDataFromHttp();
        initFeedbackRecyler();
        initClickListener();
    }

    private void initView() {
        mAsk_content = (TextView)findViewById(R.id.ask_content);
        mFeedback_recyclerview= (RecyclerView)findViewById(R.id.feedback_recyclerview);
        mCommit = (LinearLayout)findViewById(R.id.commit);
        mBack = (LinearLayout)findViewById(R.id.back);
        mAsk_phone = (TextView)findViewById(R.id.ask_phone);

        mAsk_content.setText("\u3000\u3000"+"您好，欢迎提出宝贵的建议！我们会用心倾听并改善产品，把更好的体验递给您，谢谢~！ (可多选)");
    }

    private void initClickListener(){
        mCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String paramString = mFeedbackAdapter.getSelectPosition();
                if (paramString.equals("")){
                    Toast.makeText(mContext, "您还没有选择内容哦！", Toast.LENGTH_SHORT).show();
                    return;
                }

                Map<String, String> paramsMap = new HashMap<String, String>();
                paramsMap.put(GlobleData.HTTP_VERSION_KEY, GlobleData.HTTP_VERSION_VALUE);
                String phone = PreferencesUtil.GetAuthorValue(mContext, GlobleData.PREFERENCE_AUTHOR_USERP_PONE, "");
                paramsMap.put("mobile", phone);
                paramsMap.put("suggestId", paramString);
                OkhttpUtil.okHttpPost(HTTP_URL_FEEDBACK, paramsMap, new CallBackUtil.CallBackString() {
                    @Override
                    public void onFailure(Call call, Exception e) {
                        Toast.makeText(mContext, "反馈提交失败！", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response) throws JSONException {
                        //Log.d(TAG, response);
                        try {
                            JSONObject obj = new JSONObject(response);
                            if(obj == null) {
                                Toast.makeText(mContext, "反馈提交失败！", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (Exception e) {
                            Toast.makeText(mContext, "反馈提交失败！", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        try {
                            JsonCommonResult resultBean = GsonUtil.fromJson(response, JsonCommonResult.class);
//                            if(resultBean == null)
//                                return;
//                            if(resultBean.getResult().equals("0")) {
                              //  if (resultBean.getMessage().equals("反馈成功")){
                                    final Dialog builder = new Dialog(FeedBackActivity.this, R.style.dialogTransparent);
                                    RelativeLayout loginDialog= (RelativeLayout) getLayoutInflater().inflate(R.layout.pop_dialog_feedback,null);
                                    Button mOk = (Button)loginDialog.findViewById(R.id.commitOK);
                                    mOk.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            builder.dismiss();
                                            finish();
                                        }
                                    });

                                    builder.setContentView(loginDialog);
                                    builder.setCancelable(false);
                                    builder.show();
                                    mOk.requestFocus();
//                                }else {
//                                    Toast.makeText(mContext, resultBean.getMessage(), Toast.LENGTH_SHORT).show();
//                                }

 //                           }
//                            else {
//                                Toast.makeText(mContext, resultBean.getMessage(), Toast.LENGTH_SHORT).show();
//                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getFeedBackDataFromHttp() {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put(GlobleData.HTTP_VERSION_KEY,GlobleData.HTTP_VERSION_VALUE);
        OkhttpUtil.okHttpGet(GlobleData.HTTP_URL_FEEDBACKINFO,paramsMap,new CallBackUtil.CallBackString() {
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
                        Toast.makeText(mContext, "获取反馈信息失败！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (Exception e) {
                    Toast.makeText(mContext, "获取反馈信息失败！", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    JsonFeedBackData jsonData = GsonUtil.fromJson(response, JsonFeedBackData.class);
                    if (jsonData == null)
                        return;
                    if(jsonData.getResult().equals("0")) {
                        mAsk_phone.setText("咨询热线："+jsonData.getData().getPhone());
                        for (int i = 0;i < jsonData.getData().getOpinion().size();i++){
                            mFeedBackList.add(jsonData.getData().getOpinion().get(i).toString());
                        }
                        mFeedbackAdapter.setSelectArr(mFeedBackList.size());
                        mFeedbackAdapter.notifyDataSetChanged();

                    } else {
                        Toast.makeText(mContext, jsonData.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(mContext, "获取反馈信息失败！", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
    }

    private void initFeedbackRecyler() {

        GridLayoutManagerFeedback gridlayoutManager = new GridLayoutManagerFeedback(mContext, 3);
        gridlayoutManager.setOrientation(GridLayoutManager.VERTICAL);

        mFeedback_recyclerview.setLayoutManager(gridlayoutManager);
        mFeedback_recyclerview.setFocusable(false);

        mFeedback_recyclerview.addItemDecoration(new FeedbackItemSpacing(0,0,(int)getDimension(R.dimen.w_48),(int)getDimension(R.dimen.w_48)));
        mFeedbackAdapter = new FeedbackAdapter(mContext,mFeedBackList);
        mFeedback_recyclerview.setAdapter(mFeedbackAdapter);


    }

    public float getDimension(int id) {
        return getResources().getDimension(id);
    }
}
