package tv.dfyc.yckt.util;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by yyj on 2017/8/12.
 */

public class XESUtil {
    private final static String TAG = "XESUtil";
    private static String[] weeks = {"星期日","星期一","星期二","星期三","星期四","星期五","星期六"};
    private final static int[] li_SecPosValue = { 1601, 1637, 1833, 2078, 2274,
            2302, 2433, 2594, 2787, 3106, 3212, 3472, 3635, 3722, 3730, 3858,
            4027, 4086, 4390, 4558, 4684, 4925, 5249, 5590 };
    private final static String[] lc_FirstLetter = { "a", "b", "c", "d", "e",
            "f", "g", "h", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
            "t", "w", "x", "y", "z" };

    public static String getDateTime() {
        Date now = new Date();
        SimpleDateFormat dateformat = new SimpleDateFormat("MM/dd HH:mm");
        dateformat.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        String sDateTime = dateformat.format(now);

        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if(week_index<0){
            week_index = 0;
        }
        String sWeek = weeks[week_index];
        return sDateTime + " " + sWeek;
    }

    public static String stringForTime(int timeMs) {
        if (timeMs <= 0 || timeMs >= 24 * 60 * 60 * 1000) {
            return "00:00";
        }
        int totalSeconds = timeMs / 1000;
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;
        StringBuilder stringBuilder = new StringBuilder();
        Formatter mFormatter = new Formatter(stringBuilder, Locale.getDefault());
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

    /**
     * 比较两个日期的大小，日期格式为yyyy-MM-dd
     *
     * @param str1 the first date
     * @param str2 the second date
     * @return true <br/>false
     */
    public static boolean isDateOneBigger(String str1, String str2) {
        boolean isBigger = false;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dt1 = null;
        Date dt2 = null;
        try {
            dt1 = sdf.parse(str1);
            dt2 = sdf.parse(str2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (dt1.getTime() > dt2.getTime()) {
            isBigger = true;
        } else if (dt1.getTime() < dt2.getTime()) {
            isBigger = false;
        }
        return isBigger;
    }


    public static boolean isDateAndTimeOneBigger(String str1, String str2) {
        boolean isBigger = false;
        // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dt1 = null;
        Date dt2 = null;
        try {
            dt1 = sdf.parse(str1);
            dt2 = sdf.parse(str2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (dt1.getTime() > dt2.getTime()) {
            isBigger = true;
        } else if (dt1.getTime() < dt2.getTime()) {
            isBigger = false;
        }
        return isBigger;
    }

    public static String getCurrentFormatTime(String format) {
        Date now = new Date();
        SimpleDateFormat dateformat = new SimpleDateFormat(format);
        dateformat.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        String sDateTime = dateformat.format(now);
        return sDateTime;
    }

    public static String getEndFormatTime(String format, long msecs) {
        Date end = new Date();
        end.setTime(end.getTime() + msecs);
        SimpleDateFormat dateformat = new SimpleDateFormat(format);
        dateformat.setTimeZone(TimeZone.getTimeZone("GMT+08"));

        String sDateTime = dateformat.format(end);
        return sDateTime;
    }

    /**
     * 取得给定汉字串的首字母串,即声母串
     * @param str 给定汉字串
     * @return 声母串
     */
    public static String getAllFirstLetter(String str) {
        if (str == null || str.trim().length() == 0) {
            return "";
        }
        String _str = "";
        for (int i = 0; i < str.length(); i++) {
            _str = _str + XESUtil.getFirstLetter(str.substring(i, i + 1));
        }
        return _str;
    }

    /**
     * 取得给定汉字的首字母,即声母
     * @param chinese 给定的汉字
     * @return 给定汉字的声母
     */
    public static String getFirstLetter(String chinese) {
        if (chinese == null || chinese.trim().length() == 0) {
            return "";
        }
        chinese = XESUtil.conversionStr(chinese, "GB2312", "ISO8859-1");

        if (chinese.length() > 1) // 判断是不是汉字
        {
            int li_SectorCode = (int) chinese.charAt(0); // 汉字区码
            int li_PositionCode = (int) chinese.charAt(1); // 汉字位码
            li_SectorCode = li_SectorCode - 160;
            li_PositionCode = li_PositionCode - 160;
            int li_SecPosCode = li_SectorCode * 100 + li_PositionCode; // 汉字区位码
            if (li_SecPosCode > 1600 && li_SecPosCode < 5590) {
                for (int i = 0; i < 23; i++) {
                    if (li_SecPosCode >= li_SecPosValue[i]  && li_SecPosCode < li_SecPosValue[i + 1]) {
                        chinese = lc_FirstLetter[i];
                        break;
                    }
                }
            } else // 非汉字字符,如图形符号或ASCII码
            {
                chinese = XESUtil.conversionStr(chinese, "ISO8859-1", "GB2312");
                chinese = chinese.substring(0, 1);
            }
        }
        return chinese;
    }
    /**
     * 字符串编码转换
     * @param str 要转换编码的字符串
     * @param charsetName 原来的编码
     * @param toCharsetName 转换后的编码
     * @return 经过编码转换后的字符串
     */
    private static String conversionStr(String str, String charsetName,String toCharsetName) {
        try {
            str = new String(str.getBytes(charsetName), toCharsetName);
        } catch (UnsupportedEncodingException ex) {
            System.out.println("字符串编码转换异常：" + ex.getMessage());
        }
        return str;
    }

    public static void savePicture(Context context, final String file_name, String url) {
        Log.d(TAG, "savePicture: " + file_name + " url: " + url);
        Glide.with(context).load(url).asBitmap().toBytes().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(new SimpleTarget<byte[]>() {
            @Override
            public void onResourceReady(byte[] bytes, GlideAnimation<? super byte[]> glideAnimation) {
                try {
                    savaFileToSD(file_name,bytes);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static boolean isPictureExist(String file_name) throws Exception {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String filePath = Environment.getExternalStorageDirectory().getCanonicalPath()+"/XueErSi/" + file_name;
            File file = new File(filePath);
            if (file.exists()){
                return true;
            }
        }
        return false;
    }

    public static String getPicturePath(String file_name) throws Exception{
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String filePath = Environment.getExternalStorageDirectory().getCanonicalPath()+"/XueErSi/" + file_name;
            File file = new File(filePath);
            if (file.exists()){
                return filePath;
            }
        }
        return "";
    }

    //往SD卡写入文件的方法
    public static void savaFileToSD(String filename, byte[] bytes) throws Exception {
        //如果手机已插入sd卡,且app具有读写sd卡的权限
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String filePath = Environment.getExternalStorageDirectory().getCanonicalPath()+"/XueErSi";
            File dir1 = new File(filePath);
            if (!dir1.exists()){
                dir1.mkdirs();
            }
            filename = filePath+ "/" + filename;
            File file = new File(filename);
            if(file.exists())
                file.delete();
            FileOutputStream output = new FileOutputStream(filename);
            output.write(bytes);
            output.close();
        }
    }
}
