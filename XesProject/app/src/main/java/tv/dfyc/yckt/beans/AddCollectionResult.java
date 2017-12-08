package tv.dfyc.yckt.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by android on 2017/11/28.
 */

public class AddCollectionResult {
    private String SeqNo;
    private String Result;
    private String Message;
    private ADDColllection data;

    @Override
    public String toString() {
        return "AddCollectionResult{" +
                "SeqNo='" + SeqNo + '\'' +
                ", Result='" + Result + '\'' +
                ", Message='" + Message + '\'' +
                ", data=" + data +
                '}';
    }

    public ADDColllection getData() {
        return data;
    }

    public void setData(ADDColllection data) {
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

    public class ADDColllection {
        private int historyid;

        public int getHistoryid() {
            return historyid;
        }

        public void setHistoryid(int historyid) {
            this.historyid = historyid;
        }
    }
}
