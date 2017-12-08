package tv.dfyc.yckt.beans;

/**
 * Created by Administrator on 2017/8/2 0002.
 */

public class JsonUserLogin {
    private String SeqNo;
    private String Result;
    private String Message;
    private Data data;

    public String getSeqNo() {
        return SeqNo;
    }
    public void setSeqNo(String seqNo) {
        SeqNo = seqNo;
    }

    public String getResult() {
        return Result;
    }
    public void setResult(String result) {
        Result = result;
    }

    public String getMessage() {
        return Message;
    }
    public void setMessage(String message) {
        Message = message;
    }

    public Data getData() {
        return data;
    }
    public void setData(Data data) {
        this.data = data;
    }

    public class Data {
        private String User_id;
        private String phoneNum;
        private String apppId;
        private String userInfo;
        private String OTTUserToken;
        private String expiredTime;
        private String TimeOut;

        public String getUser_id() {
            return User_id;
        }
        public void setUser_id(String user_id) {
            User_id = user_id;
        }

        public String getPhoneNum() {
            return phoneNum;
        }
        public void setPhoneNum(String phoneNum) {
            this.phoneNum = phoneNum;
        }

        public String getApppId() {
            return apppId;
        }
        public void setApppId(String apppId) {
            this.apppId = apppId;
        }

        public String getUserInfo() {
            return userInfo;
        }
        public void setUserInfo(String userInfo) {
            this.userInfo = userInfo;
        }

        public String getOTTUserToken() {
            return OTTUserToken;
        }
        public void setOTTUserToken(String OTTUserToken) {
            this.OTTUserToken = OTTUserToken;
        }

        public String getExpiredTime() {
            return expiredTime;
        }
        public void setExpiredTime(String expiredTime) {
            this.expiredTime = expiredTime;
        }

        public String getTimeOut() {
            return TimeOut;
        }
        public void setTimeOut(String timeOut) {
            TimeOut = timeOut;
        }
    }
}
