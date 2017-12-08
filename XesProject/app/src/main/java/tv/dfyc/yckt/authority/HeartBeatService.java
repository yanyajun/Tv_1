package tv.dfyc.yckt.authority;

import android.app.DevInfoManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.chinamobile.authclient.AuthClient;
import com.chinamobile.authclient.Constants;
import tv.dfyc.yckt.network.CallBackUtil;
import tv.dfyc.yckt.network.GsonUtil;
import tv.dfyc.yckt.network.OkhttpUtil;
import tv.dfyc.yckt.util.PreferencesUtil;
import tv.dfyc.yckt.util.GlobleData;
import tv.dfyc.yckt.beans.JsonHeartBeat;
import tv.dfyc.yckt.beans.JsonUserLogin;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by Administrator on 2017/8/1 0001.
 */

public class HeartBeatService extends Service {
    private final static String TAG="HeartBeatService";
    private Context mContext;
    private long mTime = 1000 * 60 * 10;
    private boolean bRun = true;
    private AuthClient mAuthClient;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG , "onCreate");
        mContext = this;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG , "onDestroy");
        bRun = false;
    }

    @Override
    public int onStartCommand(Intent intent,int flags, int startId) {
        Log.d(TAG , "onStartCommand");
        new Thread(new Runnable() {
            @Override
            public void run() {
                sendHeartToRemote();
            }
        }).start();
        return super.onStartCommand(intent, flags, startId);
    }

    private void sendHeartToRemote() {
        //"http://223.110.245.40:33200/EPG";//53402000349
        if(mAuthClient == null)
            mAuthClient = AuthClient.getIntance(mContext);
        mAuthClient.getToken(new AuthClient.CallBack() {
            @Override
            public void onResult(final JSONObject json) {
                try {
                    final int resultCode = json.getInt(Constants.VALUNE_KEY_RESULT_CODE);
                    if(resultCode == Constants.RESULT_OK){

                        ///////////////////////////////////获取UserToken////////////////////////////////////
                        final String sUserToken = json.getString(Constants.VALUNE_KEY_TOKEN);
                        Log.d(TAG,"token="+sUserToken);
                        sendGetInfoMsg("认证成功: token="+sUserToken, true);
                        PreferencesUtil.PutAuthorValue(mContext, GlobleData.PREFERENCE_AUTHOR_USERTOKEN, sUserToken);

                        ////////////////////////////////////获取DevInfo////////////////////////////////////
                        final Map<String, String> paramsMap = new HashMap<String, String>();
                        try {
                            //noinspection ResourceType
                            DevInfoManager devManager = (DevInfoManager) getSystemService(DevInfoManager.DATA_SERVER);
                            if(devManager == null) {
                                Log.d(TAG,"获取设备信息失败: devManager == null");
                                sendGetInfoMsg("获取设备信息失败: devManager == null", false);
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

                            //String sPASSWORD = devManager.getValue(DevInfoManager.SPASSWORD);
                            //devManager.update(DevInfoManager.PHONE, sPhoneNumber,  DevInfoManager.Default_Attribute);
                            //devManager.update(DevInfoManager.SPASSWORD, sPASSWORD, DevInfoManager.Default_Attribute);

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

                            Log.d(TAG,"获取设备信息成功: "+sResult);
                            sendGetInfoMsg("获取设备信息成功: "+sResult, true);

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
                            Log.d(TAG,"获取设备信息失败: have exception");
                            sendGetInfoMsg("获取设备信息失败: have exception", false);
                            return;
                        }

                        ////////////////////////////////////获取OTTToken////////////////////////////////////
                        OkhttpUtil.okHttpGet(GlobleData.HTTP_URL_LOGIN, paramsMap, new CallBackUtil.CallBackString() {
                            @Override
                            public void onFailure(Call call, Exception e) {
                                Log.d(TAG, "get user data failed!");
                                sendGetInfoMsg("获取用户信息失败", false);
                            }

                            @Override
                            public void onResponse(String response) {
                                Log.d(TAG, response);
                                try {
                                    JSONObject obj = new JSONObject(response);
                                    if(obj == null) {
                                        Log.d(TAG, "get user data failed!");
                                        sendGetInfoMsg("获取用户信息失败", false);
                                        return;
                                    }
                                } catch (Exception e) {
                                    Log.d(TAG, "get user data failed!");
                                    sendGetInfoMsg("获取用户信息失败", false);
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

                                    sResult += " UserToken: " + sUserToken;

                                    Log.d(TAG, "获取用户信息成功: " + sResult);
                                    sendGetInfoMsg("获取用户信息成功: " + sResult, true);
                                    heartHttp(loginBean.getData().getOTTUserToken(), paramsMap.get("UserID"), paramsMap.get("EPGAddress"));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Log.d(TAG, "get user data failed!");
                                    sendGetInfoMsg("获取用户信息失败", false);
                                }

                            }
                        });


                    }else{
                        Log.d(TAG,"认证失败: resultCode = "+resultCode);
                        sendGetInfoMsg("认证失败: resultCode = "+resultCode, false);
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
        try {
            Thread.currentThread().sleep(mTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        sendHeartToRemote();
    }

    private void heartHttp(String ottToken, String userID, String sEPGAddress) {
        if(!sEPGAddress.isEmpty() && !ottToken.isEmpty() && !ottToken.isEmpty()) {
            String url = sEPGAddress + GlobleData.HTTP_HEARTBEAT;
            Map<String, String> paramsMap = new HashMap<String, String>();
            paramsMap.put("OTTUserToken", ottToken);
            paramsMap.put("UserID", userID);
            OkhttpUtil.okHttpGet(url, paramsMap, new CallBackUtil.CallBackString() {
                @Override
                public void onFailure(Call call, Exception e) {
                    sendHeartFailedMsg("http get failed");
                }

                @Override
                public void onResponse(String response) {
                    Log.d(TAG, response);
                    try {
                        JSONObject obj = new JSONObject(response);
                        if(obj == null) {
                            sendHeartFailedMsg(response);
                            return;
                        }
                    } catch (Exception e) {
                        sendHeartFailedMsg(response);
                        return;
                    }
                    try {
                        JsonHeartBeat heartBean = GsonUtil.fromJson(response, JsonHeartBeat.class);
                        if(heartBean == null)
                            return;
                        if(heartBean.getResult().equals("0")) {
                            String sResult = "UserID: " + heartBean.getUserID() +
                                    " Result: " + heartBean.getResult() +
                                    " Description: " + heartBean.getDescription() +
                                    " expiredTime: " + heartBean.getExpiredTime() +
                                    " TimeOut: " + heartBean.getTimeOut();
                            sendHeartSuccessMsg(sResult);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        sendHeartFailedMsg(response);
                    }
                }
            });
        } else {
            sendHeartFailedMsg("have empty values");
        }
    }

    private void sendHeartFailedMsg(String result) {
        Intent intentParam = new Intent(GlobleData.MESSAGE_HEART_FAILED);
        intentParam.putExtra("Result", result);
        this.sendBroadcast(intentParam);
    }

    private void sendHeartSuccessMsg(String result) {
        Intent intentParam = new Intent(GlobleData.MESSAGE_HEART_SUCCESS);
        intentParam.putExtra("Result", result);
        this.sendBroadcast(intentParam);
    }

    private void sendGetInfoMsg(String result, boolean isSucc) {
        Intent intentParam = new Intent(GlobleData.MESSAGE_GET_INFO);
        intentParam.putExtra("Result", result);
        intentParam.putExtra("Successed", isSucc);
        this.sendBroadcast(intentParam);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG , "onBind");
        return null;
    }
}
