package tv.dfyc.yckt.beans;

/**
 * Created by Administrator on 2017/8/7.
 */

public class JsonCommonResult {
    private String SeqNo;
    private String Result;
    private String Message;

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
}
