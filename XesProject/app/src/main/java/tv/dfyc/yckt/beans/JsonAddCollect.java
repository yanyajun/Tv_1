package tv.dfyc.yckt.beans;

/**
 * Created by Administrator on 2017/11/30 0030.
 */

public class JsonAddCollect {
    private String SeqNo;
    private String Result;
    private String Message;
    private AddCollectData data;

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

    public AddCollectData getData() {
        return data;
    }
    public void setData(AddCollectData data) {
        this.data = data;
    }
    public static class AddCollectData {
        private int historyid;

        public int getHistoryid() {
            return historyid;
        }
        public void setHistoryid(int historyid) {
            this.historyid = historyid;
        }
    }
}
