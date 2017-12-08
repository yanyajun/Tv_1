package tv.dfyc.yckt.beans;

import java.util.List;

/**
 * Created by admin on 2017-11-22.
 */

public class JsonPurchaseRecordData {
    private String SeqNo;
    private String Result;
    private String Message;
    private List<PurchaseRecord> data;

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

    public List<PurchaseRecord> getData() {
        return data;
    }

    public void setData(List<PurchaseRecord> data) {
        this.data = data;
    }

    public class PurchaseRecord{
        private String name;
        private String postage;
        private String status;
        private String endtime;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPostage() {
            return postage;
        }

        public void setPostage(String postage) {
            this.postage = postage;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getEndtime() {
            return endtime;
        }

        public void setEndtime(String endtime) {
            this.endtime = endtime;
        }
    }
}
