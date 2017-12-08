package tv.dfyc.yckt.detail;

import android.app.Activity;
import android.app.DevInfoManager;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chinamobile.authclient.AuthClient;
import com.chinamobile.authclient.Constants;
import com.open.androidtvwidget.bridge.EffectNoDrawBridge;
import com.open.androidtvwidget.utils.Utils;
import com.open.androidtvwidget.view.LinearMainLayout;
import com.open.androidtvwidget.view.MainUpView;

import tv.dfyc.yckt.R;
import tv.dfyc.yckt.network.CallBackUtil;
import tv.dfyc.yckt.network.GsonUtil;
import tv.dfyc.yckt.network.OkhttpUtil;
import tv.dfyc.yckt.util.PreferencesUtil;
import tv.dfyc.yckt.util.GlobleData;
import tv.dfyc.yckt.beans.JsonCreateOrderData;
import tv.dfyc.yckt.beans.JsonGoodsListData;
import tv.dfyc.yckt.beans.JsonUserLogin;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by Administrator on 2017/7/24 0024.
 */

public class BuyListActivity extends Activity {
    private static final String TAG = "BuyListActivity";
    private Context mContext;
    private LinearLayout mScrollLayout;
    private MainUpView mProductHoverView;
    private View mOldFocus; // 4.3以下版本需要自己保存.
    private int Id;
    private JsonCreateOrderData mOrderData;

    private TextView mWeekTextView;
    private TextView mTimeTextView;
    private TextView mDateTextView;
    private MsgReceiver mMsgReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_buy_page_commenly);
        mScrollLayout = (LinearLayout) findViewById(R.id.buy_page_scroll_layout);
        mProductHoverView = (MainUpView) findViewById(R.id.buy_page_product_upview);

        mWeekTextView = (TextView) findViewById(R.id.week);
        mTimeTextView = (TextView) findViewById(R.id.time);
        mDateTextView = (TextView) findViewById(R.id.date);

        if (Utils.getSDKVersion() == 17) { // 测试 android 4.2版本.
            switchNoDrawBridgeVersion();
        } else { // 其它版本（android 4.3以上）.
            switchDrawBridgeVersion();
        }

        mScrollLayout.getViewTreeObserver().addOnGlobalFocusChangeListener(new ViewTreeObserver.OnGlobalFocusChangeListener() {
            @Override
            public void onGlobalFocusChanged(final View oldFocus, final View newFocus) {
                float scale = 1.10f;
                mProductHoverView.setFocusView(newFocus, mOldFocus, scale);
                mOldFocus = newFocus; // 4.3以下需要自己保存.

                if(oldFocus != null) {
                    ImageView old_topImage = (ImageView) oldFocus.findViewById(R.id.top_image);
                    old_topImage.setBackground(getResources().getDrawable(R.drawable.buy_page_commen_unselect));
                    oldFocus.setBackground(getResources().getDrawable(R.drawable.buy_page_item_unselect_shape));
                }
                if(newFocus != null) {
                    ImageView new_topImage = (ImageView) newFocus.findViewById(R.id.top_image);
                    new_topImage.setBackground(getResources().getDrawable(R.drawable.buy_page_commen_select));
                    newFocus.setBackground(getResources().getDrawable(R.drawable.buy_page_item_select_shape));
                }
            }
        });

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        Id = bundle.getInt("libraryId");
        JsonGoodsListData data = (JsonGoodsListData) bundle.getSerializable("productData");

        setProductView(data);

        mMsgReceiver = new MsgReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(GlobleData.MESSAGE_UPDATE_TIME);
        intentFilter.addAction(GlobleData.MESSAGE_ORDER_UNFINISH);
        intentFilter.addAction(GlobleData.MESSAGE_ORDER_FINISH);
        registerReceiver(mMsgReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mMsgReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initUserAuth();
    }

    public float getDimension(int id) {
        return getResources().getDimension(id);
    }

    private void switchNoDrawBridgeVersion() {
        float density = getResources().getDisplayMetrics().density;
        RectF rectf = new RectF(getDimension(R.dimen.w_10) * density, getDimension(R.dimen.h_10) * density,
                getDimension(R.dimen.w_9) * density, getDimension(R.dimen.h_9) * density);
        EffectNoDrawBridge effectNoDrawBridge = new EffectNoDrawBridge();
        effectNoDrawBridge.setTranDurAnimTime(200);
        mProductHoverView.setEffectBridge(effectNoDrawBridge); // 4.3以下版本边框移动.
        mProductHoverView.setUpRectResource(R.drawable.buy_page_commen_border_shape); // 设置移动边框的图片.
        mProductHoverView.setDrawUpRectPadding(rectf); // 边框图片设置间距.
    }

    private void switchDrawBridgeVersion() {
        float density = getResources().getDisplayMetrics().density;
        RectF rectf = new RectF(getDimension(R.dimen.w_35) * density, getDimension(R.dimen.w_35) * density,
                getDimension(R.dimen.w_35) * density, getDimension(R.dimen.h_35) * density);
        EffectNoDrawBridge effectNoDrawBridge = new EffectNoDrawBridge();
        effectNoDrawBridge.setTranDurAnimTime(200);
        mProductHoverView.setEffectBridge(effectNoDrawBridge);
        mProductHoverView.setUpRectResource(R.drawable.buy_page_commen_border);
//        mProductHoverView.setUpRectResource(R.drawable.buy_page_commen_border_shape);
        mProductHoverView.setDrawUpRectPadding(rectf); // 边框图片设置间距.
    }

    private void setProductView(JsonGoodsListData data) {
        for(int pos = 0; pos < data.getData().size(); ++pos) {
            if(data.getData().get(pos) == null)
                continue;
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_buy_product, mScrollLayout, false);
            view.setClickable(true);
            setProductData(view, pos, data);
            final int iIndex = pos;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final JsonGoodsListData.GoodDetail goods_detail = (JsonGoodsListData.GoodDetail) v.getTag();
                    if(goods_detail == null)
                        return;
                    buyProduct(goods_detail);
                }
            });

            if (pos == 0) {
                LinearMainLayout.LayoutParams paras = new LinearMainLayout.LayoutParams((int) getDimension(R.dimen.w_376), (int) getDimension(R.dimen.h_691));
                paras.setMargins((int) getDimension(R.dimen.w_30), (int) getDimension(R.dimen.w_30), 0, (int) getDimension(R.dimen.w_30));
                mScrollLayout.addView(view, paras);
            } else if (pos == 1) {
                LinearMainLayout.LayoutParams paras = new LinearMainLayout.LayoutParams((int) getDimension(R.dimen.w_376), (int) getDimension(R.dimen.h_691));
                paras.setMargins((int) getDimension(R.dimen.w_140), (int) getDimension(R.dimen.h_30), (int) getDimension(R.dimen.h_30), (int) getDimension(R.dimen.w_30));
                mScrollLayout.addView(view, paras);
            } else {
                LinearMainLayout.LayoutParams paras = new LinearMainLayout.LayoutParams((int) getDimension(R.dimen.w_376), (int) getDimension(R.dimen.h_691));
                paras.setMargins((int) getDimension(R.dimen.w_140), (int) getDimension(R.dimen.h_30), (int) getDimension(R.dimen.h_30), (int) getDimension(R.dimen.w_30));
                mScrollLayout.addView(view, paras);
            }
        }
    }

    private void buyProduct(final JsonGoodsListData.GoodDetail goods_detail) {
        String sEndDate = goods_detail.getGoods_endtime();
        if(sEndDate != null && !sEndDate.isEmpty()) {
            Toast.makeText(mContext, sEndDate, Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put(GlobleData.HTTP_VERSION_KEY,GlobleData.HTTP_VERSION_VALUE);
        paramsMap.put("productType", goods_detail.getGoods_id());
        //paramsMap.put(GlobleData.HTTP_PARAM_LIBRARYID, Integer.toString(Id));
        String userID = PreferencesUtil.GetAuthorValue(mContext,GlobleData.PREFERENCE_AUTHOR_USER_ID, "");
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
                    if(mOrderData.getResult().equals("0")) {
                        String sOrderUrl = mOrderData.getData();
                        Intent intent = new Intent(mContext, OrderActivity.class);
                        intent.putExtra("product_good_info", (Serializable) goods_detail);
                        intent.putExtra("product_order_url", sOrderUrl);
                        startActivity(intent);
                    } else {
                        if(mOrderData.getMessage().equals("您已购买过此商品")){
                            final Dialog builder = new Dialog(BuyListActivity.this, R.style.dialogTransparent);
                            RelativeLayout quitDialog= (RelativeLayout) getLayoutInflater().inflate(R.layout.pop_dialog_reorder,null);
                            final Button mOk = (Button)quitDialog.findViewById(R.id.popOK);

                            mOk.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    builder.dismiss();
                                }
                            });
                            builder.setContentView(quitDialog);
                            builder.show();
                            builder.dismiss();
                            builder.setCancelable(true);
                            builder.show();
                            mOk.requestFocus();
                        } else {
                            Toast.makeText(mContext, mOrderData.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(TAG, "json exception");
                }
            }
        });
    }

    private void setProductData(View itemView, int pos, JsonGoodsListData resultBean) {
        TextView productName = (TextView) itemView.findViewById(R.id.buy_product_reflect_name);
        TextView prodectDetail = (TextView) itemView.findViewById(R.id.buy_product_reflect_detail);
        TextView prodectMoney = (TextView) itemView.findViewById(R.id.buy_product_reflect_money);
        TextView prodectWords = (TextView) itemView.findViewById(R.id.buy_product_reflect_words);

        itemView.setTag(resultBean.getData().get(pos));

        String product_name = resultBean.getData().get(pos).getName();
        if(product_name.contains("（")) {
            product_name = product_name.split("（")[0] + "\n" + "（" + product_name.split("（")[1];
        }
        productName.setText(product_name);

        prodectDetail.setText(resultBean.getData().get(pos).getDescription());

        String sPrice = resultBean.getData().get(pos).getPrice();
        if(sPrice.split("元").length > 0)
            prodectMoney.setText(sPrice.split("元")[0]);

        String product_words = resultBean.getData().get(pos).getRemarks();
        if(product_words.contains("（")) {
            product_words = product_words.split("（")[0] + "\n" + "（" + product_words.split("（")[1];
        }
        prodectWords.setText(product_words);
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
                if(GlobleData.MESSAGE_ORDER_UNFINISH.equals(intent.getAction())) {
                    Bundle bundle = intent.getExtras();
                    final JsonGoodsListData.GoodDetail goodsDetail = (JsonGoodsListData.GoodDetail) bundle.getSerializable("product_good_info");
                    if(goodsDetail != null) {
                        final Dialog builder = new Dialog(BuyListActivity.this, R.style.dialogTransparent);
                        RelativeLayout orderDialog= (RelativeLayout) getLayoutInflater().inflate(R.layout.pop_dialog_order_failed,null);
                        Button mOk = (Button)orderDialog.findViewById(R.id.popOK);
                        Button mCancel =  (Button)orderDialog.findViewById(R.id.popCancel);
                        TextView mText =  (TextView) orderDialog.findViewById(R.id.popText);
                        mText.setText("对不起，" + goodsDetail.getName() + "购买失败，请重新购买");

                        mCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                builder.dismiss();
                            }
                        });
                        mOk.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                builder.dismiss();
                                buyProduct(goodsDetail);
                            }
                        });
                        builder.setContentView(orderDialog);
                        builder.setCancelable(true);
                        builder.show();
                        mOk.requestFocus();
                    }
                }
                if(GlobleData.MESSAGE_ORDER_FINISH.equals(intent.getAction())) {
                    int iResult = intent.getIntExtra("order_finish", 0);
                    if(iResult == 1) {
                        finish();
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "" + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void initUserAuth() {
        AuthClient mAuthClient = AuthClient.getIntance(BuyListActivity.this);
        mAuthClient.getToken(new AuthClient.CallBack() {
            @Override
            public void onResult(final JSONObject json) {
                try {
                    final int resultCode = json.getInt(Constants.VALUNE_KEY_RESULT_CODE);
                    if(resultCode == Constants.RESULT_OK){

                        ///////////////////////////////////获取UserToken////////////////////////////////////
                        final String sUserToken = json.getString(Constants.VALUNE_KEY_TOKEN);
                        Log.d(TAG,"token="+sUserToken);
                        PreferencesUtil.PutAuthorValue(mContext, GlobleData.PREFERENCE_AUTHOR_USERTOKEN, sUserToken);

                        ////////////////////////////////////获取DevInfo////////////////////////////////////

                        Map<String, String> paramsMap = new HashMap<String, String>();
                        try {
                            //noinspection ResourceType
                            DevInfoManager devManager = (DevInfoManager) getSystemService(DevInfoManager.DATA_SERVER);
                            if(devManager == null) {
                                return;
                            }
                            String sSTB_MAC = devManager.getValue(DevInfoManager.STB_MAC);
                            String sPhoneNumber = devManager.getValue(DevInfoManager.PHONE);
                            String sAccount = devManager.getValue(DevInfoManager.ACCOUNT);
                            String sSTB_SN = devManager.getValue(DevInfoManager.STB_SN);
                            String sEPGAddress = devManager.getValue(DevInfoManager.EPGAddress);
                            String sTV_ID = devManager.getValue(DevInfoManager.TVID);
                            String sDeviceType = devManager.getValue(DevInfoManager.DeviceType);
                            String sFirmwareVersion = devManager.getValue(DevInfoManager.FirmwareVersion);
                            String sCDNAddress = devManager.getValue(DevInfoManager.CDNAddress);
                            String sCDNAddressBackup = devManager.getValue(DevInfoManager.BackupCDNAddress);

                            PreferencesUtil.PutAuthorValue(mContext, GlobleData.PREFERENCE_AUTHOR_STBMAC, sSTB_MAC);
                            PreferencesUtil.PutAuthorValue(mContext, GlobleData.PREFERENCE_AUTHOR_PHONE, sPhoneNumber);
                            PreferencesUtil.PutAuthorValue(mContext, GlobleData.PREFERENCE_AUTHOR_ACCOUNT, sAccount);
                            PreferencesUtil.PutAuthorValue(mContext, GlobleData.PREFERENCE_AUTHOR_STBSN, sSTB_SN);
                            PreferencesUtil.PutAuthorValue(mContext, GlobleData.PREFERENCE_AUTHOR_EPGADDRESS, sEPGAddress);
                            PreferencesUtil.PutAuthorValue(mContext, GlobleData.PREFERENCE_AUTHOR_TVID, sTV_ID);
                            PreferencesUtil.PutAuthorValue(mContext, GlobleData.PREFERENCE_AUTHOR_DEVICETYPE, sDeviceType);
                            PreferencesUtil.PutAuthorValue(mContext, GlobleData.PREFERENCE_AUTHOR_FIRMWAREVERSION, sFirmwareVersion);
                            PreferencesUtil.PutAuthorValue(mContext, GlobleData.PREFERENCE_AUTHOR_CDNADDRESS, sCDNAddress);
                            PreferencesUtil.PutAuthorValue(mContext, GlobleData.PREFERENCE_AUTHOR_BACKUPCDNADDRESS, sCDNAddressBackup);


                            final String sResult = "STB_MAC: " + sSTB_MAC + " PHONE: " + sPhoneNumber + " ACCOUNT: " +  sAccount + " STB_SN: " + sSTB_SN + " EPGAddress: " +
                                    sEPGAddress + " TVID: " + sTV_ID + " DeviceType: " + sDeviceType + " FirmwareVersion: " + sFirmwareVersion +
                                    " CDNAddress: " + sCDNAddress + " BackupCDNAddress: " + sCDNAddressBackup;

                            Log.d(TAG,"resultCode="+sResult);

                            paramsMap.put(GlobleData.HTTP_VERSION_KEY, GlobleData.HTTP_VERSION_VALUE);
                            paramsMap.put("STBSN", sSTB_SN);
                            paramsMap.put("EPGAddress", sEPGAddress);
                            paramsMap.put("TVID", sTV_ID);
                            paramsMap.put("DeviceType", sDeviceType);
                            paramsMap.put("FirmwareVersion", sFirmwareVersion);
                            paramsMap.put("UserToken", sUserToken);
                            paramsMap.put("MAC", sSTB_MAC);
                            if(sPhoneNumber.isEmpty()) {
                                paramsMap.put("UserID", sAccount);
                            } else {
                                paramsMap.put("UserID", sPhoneNumber);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            return;
                        }

                        ////////////////////////////////////获取OTTToken////////////////////////////////////
                        OkhttpUtil.okHttpGet(GlobleData.HTTP_URL_LOGIN, paramsMap, new CallBackUtil.CallBackString() {
                            @Override
                            public void onFailure(Call call, Exception e) {
                                Log.d(TAG, "get user data failed!");
                            }

                            @Override
                            public void onResponse(String response) {
                                Log.d(TAG, response);
                                try {
                                    JSONObject obj = new JSONObject(response);
                                    if(obj == null) {
                                        Log.d(TAG, "get user data failed!");
                                        return;
                                    }
                                } catch (Exception e) {
                                    Log.d(TAG, "get user data failed!");
                                    return;
                                }
                                try {
                                    JsonUserLogin loginBean = GsonUtil.fromJson(response, JsonUserLogin.class);
                                    if(loginBean == null)
                                        return;
                                    if(loginBean.getResult().equals("1")) {
                                        Toast.makeText(mContext, loginBean.getMessage(), Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    PreferencesUtil.PutAuthorValue(mContext, GlobleData.PREFERENCE_AUTHOR_USER_ID, loginBean.getData().getUser_id());
                                    PreferencesUtil.PutAuthorValue(mContext, GlobleData.PREFERENCE_AUTHOR_USER_OTTTOKEN, loginBean.getData().getOTTUserToken());
                                    PreferencesUtil.PutAuthorValue(mContext, GlobleData.PREFERENCE_AUTHOR_USERP_PONE, loginBean.getData().getPhoneNum());
                                    PreferencesUtil.PutAuthorValue(mContext, GlobleData.PREFERENCE_AUTHOR_USERP_APPID, loginBean.getData().getApppId());
                                    PreferencesUtil.PutAuthorValue(mContext, GlobleData.PREFERENCE_AUTHOR_USER_EXPIREDTIME, loginBean.getData().getExpiredTime());
                                    PreferencesUtil.PutAuthorValue(mContext, GlobleData.PREFERENCE_AUTHOR_USER_TIMEOUT, loginBean.getData().getTimeOut());

                                    String sResult =
                                            "Result: " + loginBean.getResult() +
                                                    " Message: " + loginBean.getMessage() +
                                                    " phoneNum: " + loginBean.getData().getPhoneNum() +
                                                    " OTTUserToken: " + loginBean.getData().getOTTUserToken() +
                                                    " expiredTime: " + loginBean.getData().getExpiredTime() +
                                                    " UserID: " + loginBean.getData().getUser_id() +
                                                    " TimeOut: " + loginBean.getData().getTimeOut();

                                    Log.d(TAG, "获取用户信息成功: " + sResult);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Log.d(TAG, "get user data failed!");
                                }
                            }
                        });


                    }else{
                        Log.d(TAG,"resultCode="+resultCode);
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
    }
}
