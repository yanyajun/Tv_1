package tv.dfyc.yckt.beans;

/**
 * Created by android on 2017/11/23.
 */

public class ThemeBean {
    private int SeqNo;
    private int Result;
    private String Message;
    private ThemeContent data;

    @Override
    public String toString() {
        return "ThemeBean{" +
                "SeqNo=" + SeqNo +
                ", Result=" + Result +
                ", Message='" + Message + '\'' +
                ", data=" + data +
                '}';
    }

    public int getSeqNo() {
        return SeqNo;
    }

    public void setSeqNo(int seqNo) {
        SeqNo = seqNo;
    }

    public int getResult() {
        return Result;
    }

    public void setResult(int result) {
        Result = result;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public ThemeContent getData() {
        return data;
    }

    public void setData(ThemeContent data) {
        this.data = data;
    }
}
