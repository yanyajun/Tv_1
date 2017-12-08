package tv.dfyc.yckt.beans;

import java.util.List;

/**
 * Created by admin on 2017-11-25.
 */

public class JsonFeedBackData {
    private String SeqNo;
    private String Result;
    private String Message;
    private FeedBackData data;

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

    public FeedBackData getData() {
        return data;
    }

    public void setData(FeedBackData data) {
        this.data = data;
    }

    public class FeedBackData{
        private String phone;
        private List<String> opinion;

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public List<String> getOpinion() {
            return opinion;
        }

        public void setOpinion(List<String> opinion) {
            this.opinion = opinion;
        }
    }
}