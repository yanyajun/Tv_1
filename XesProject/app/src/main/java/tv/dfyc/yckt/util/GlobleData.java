package tv.dfyc.yckt.util;

/**
 * Created by Administrator on 2017/8/2 0002.
 */

public class GlobleData {

//    public final static String HTTP_URL = "http://api.nmop.jr3d.cn/api/app/";       // online environment
//    public final static String HTTP_URL = "http://112.25.69.39/api/app/";           // middle online environment
    public final static String HTTP_URL = "http://39.106.9.12/api/app/";            // test environment
    public final static String HTTP_URL_HOME = HTTP_URL + "homepage";
    public final static String HTTP_URL_CHANNEL = HTTP_URL + "channel";
    public final static String HTTP_URL_CHANNEL_SELECTION = HTTP_URL + "columncontent";
    public final static String HTTP_URL_CHANNEL_DETAIL = HTTP_URL + "pointdetails";
    public final static String HTTP_URL_ADDCOLLECT = HTTP_URL + "addcollectrecord";
    public final static String HTTP_URL_DELCOLLECT = HTTP_URL + "delcollectrecord";
    public final static String HTTP_URL_FEEDBACK = HTTP_URL + "addsuggest";
    public final static String HTTP_URL_MYCOLLECT = HTTP_URL + "collectrecord";
    public final static String HTTP_URL_CREATEORDER = HTTP_URL + "createorder";
    public final static String HTTP_URL_PAYQUERT = HTTP_URL + "payquery";
    public final static String HTTP_URL_ORDERLIST = HTTP_URL + "orderrecord";
    public final static String HTTP_URL_ABOUTUS= HTTP_URL + "aboutus";
    public final static String HTTP_URL_QUIT= HTTP_URL + "quit";
    public final static String HTTP_URL_FEEDBACKINFO= HTTP_URL + "feedback";
    public final static String HTTP_URL_GOODSLIST = HTTP_URL + "buygoods";
    public final static String HTTP_URL_LOGIN = HTTP_URL + "login";
    public final static String HTTP_URL_ADDHISTORY = HTTP_URL + "addhistory";
    public final static String HTTP_URL_SOURCEHISTORY = HTTP_URL + "watchrecord";
    public final static String HTTP_URL_DELHISTORY = HTTP_URL + "delhistory";
    public final static String HTTP_URL_LIBRARYID = HTTP_URL + "getlibraryid";
    public final static String HTTP_URL_ADCOLLECT = HTTP_URL + "addcollectrecord";

    public final static String HTTP_URL_ALLGOODS = HTTP_URL + "allgoods";
    public final static String HTTP_URL_SPECIAL = HTTP_URL + "specialdetails";
    public final static String HTTP_URL_SEARCH = HTTP_URL + "newsearch";
    public final static String HTTP_VERSION_KEY ="version_id";
    public final static String HTTP_VERSION_VALUE ="21";

    public final static String HTTP_PARAM ="Catid";
    public final static String HTTP_PARAM_LIBRARYID ="libraryId";

    public final static String HTTP_HEARTBEAT = "/Ott/jsp/HeartBeat.jsp";
    public final static String HTTP_PLAYVIDEO = "/Ott/jsp/Auth.jsp";

    public final static String PREFERENCE_AUTHOR = "preference_anthor";
    public final static String PREFERENCE_AUTHOR_USERTOKEN = "preference_anthor_usertoken";
    public final static String PREFERENCE_AUTHOR_STBMAC = "preference_anthor_stbmac";
    public final static String PREFERENCE_AUTHOR_PHONE = "preference_anthor_phone";
    public final static String PREFERENCE_AUTHOR_ACCOUNT = "preference_anthor_account";
    public final static String PREFERENCE_AUTHOR_STBSN = "preference_anthor_stbsn";
    public final static String PREFERENCE_AUTHOR_EPGADDRESS = "preference_anthor_epgaddress";
    public final static String PREFERENCE_AUTHOR_TVID = "preference_anthor_tvid";
    public final static String PREFERENCE_AUTHOR_DEVICETYPE = "preference_anthor_devicetype";
    public final static String PREFERENCE_AUTHOR_FIRMWAREVERSION = "preference_anthor_firmwareversion";
    public final static String PREFERENCE_AUTHOR_CDNADDRESS = "preference_anthor_cdnaddress";
    public final static String PREFERENCE_AUTHOR_BACKUPCDNADDRESS = "preference_anthor_backup_cdnaddress";

    public final static String PREFERENCE_AUTHOR_USER_ID = "preference_anthor_user_id";
    public final static String PREFERENCE_AUTHOR_USERP_PONE = "preference_anthor_user_phone";
    public final static String PREFERENCE_AUTHOR_USERP_APPID = "preference_anthor_user_appid";
    public final static String PREFERENCE_AUTHOR_USER_OTTTOKEN = "preference_anthor_user_otttoken";
    public final static String PREFERENCE_AUTHOR_USER_EXPIREDTIME = "preference_anthor_user_expiredtime";
    public final static String PREFERENCE_AUTHOR_USER_TIMEOUT = "preference_anthor_user_timeout";

    public final static String PREFERENCE_DETAIL_PAGE_LIBRARY_ID = "preference_detail_page_library_id";

    public final static String MESSAGE_HEART_FAILED = "message_heart_failed";
    public final static String MESSAGE_HEART_SUCCESS = "message_heart_successed";

    public final static String MESSAGE_GET_INFO = "message_get_info";

    public final static String MESSAGE_UPDATE_TIME = "message_update_time";
    public final static String MESSAGE_UPDATE_DETAIL_DATA = "message_update_detail_data";

    public final static String MESSAGE_ORDER_FINISH = "message_order_finish";
    public final static String MESSAGE_ORDER_UNFINISH = "message_order_finish";

    public final static int GRADE_TYPE_PRIMARY = 0; // 小学
    public final static int GRADE_TYPE_JUNIOR = 1;  // 初中
    public final static int GRADE_TYPE_SENIOR = 2;  // 高中

    public final static String PNG_SPLASH_PICTURE_NAME = "splash.png";
    public final static String PNG_CONNECT_US_NAME = "connect_us.png";
}
