package tv.dfyc.yckt.beans;

/**
 * Created by android on 2017/12/4.
 */

public class DeleteCollectionResult {
    private String SeqNo;
    private String Result;
    private String Message;
    private boolean data;

    @Override
    public String toString() {
        return "DeleteCollectionResult{" +
                "SeqNo='" + SeqNo + '\'' +
                ", Result='" + Result + '\'' +
                ", Message='" + Message + '\'' +
                ", data=" + data +
                '}';
    }

    public boolean isData() {
        return data;
    }

    public void setData(boolean data) {
        this.data = data;
    }

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
