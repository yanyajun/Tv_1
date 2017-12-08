package tv.dfyc.yckt.beans;

/**
 * Created by Administrator on 2017/8/3 0003.
 */

public class JsonHeartBeat {
    private String UserID;
    private String Result;
    private String Description;
    private String expiredTime;
    private String TimeOut;

    public String getTimeOut() {
        return TimeOut;
    }
    public void setTimeOut(String timeOut) {
        TimeOut = timeOut;
    }

    public String getExpiredTime() {
        return expiredTime;
    }
    public void setExpiredTime(String expiredTime) {
        this.expiredTime = expiredTime;
    }

    public String getDescription() {
        return Description;
    }
    public void setDescription(String description) {
        Description = description;
    }

    public String getUserID() {
        return UserID;
    }
    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getResult() {
        return Result;
    }
    public void setResult(String result) {
        Result = result;
    }
}
