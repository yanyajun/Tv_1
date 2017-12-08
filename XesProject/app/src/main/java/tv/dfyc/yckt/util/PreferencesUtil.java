package tv.dfyc.yckt.util;

import android.content.Context;
import android.content.SharedPreferences;

import tv.dfyc.yckt.util.GlobleData;

/**
 * Created by Administrator on 2017/8/2 0002.
 */

public class PreferencesUtil {

    public static void PutAuthorValue(Context context, String sKey, String sValue) {
        SharedPreferences settings = context.getSharedPreferences(GlobleData.PREFERENCE_AUTHOR, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(sKey, sValue);
        editor.commit();
    }
    public static String GetAuthorValue(Context context, String sKey, String sDefault) {
        SharedPreferences settings = context.getSharedPreferences(GlobleData.PREFERENCE_AUTHOR, Context.MODE_PRIVATE);
        return settings.getString(sKey, sDefault);
    }
}
