package tv.dfyc.yckt.beans;

/**
 * Created by Administrator on 2017/8/15.
 */

public class JsonBuyListData {
    private String SeqNo;
    private String Result;
    private String Message;
    private String data;

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

    public String getData() {
        return data;
    }
    public void setData(String data) {
        this.data = data;
    }
}
