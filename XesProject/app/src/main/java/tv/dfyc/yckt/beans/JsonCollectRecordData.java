package tv.dfyc.yckt.beans;


import java.util.List;

/**
 * Created by admin on 2017-11-22.
 */

public class JsonCollectRecordData {
    private String SeqNo;
    private String Result;
    private String Message;
    private List<CollectData> data;

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

    public List<CollectData> getData() {
        return data;
    }

    public void setData(List<CollectData> data) {
        this.data = data;
    }

    public class CollectData{


        private String title;
        private int id;
        private String thumb_app_h;
        private int type;
        private int is_free;
        private int historyid;


        public int getHistoryid() {
            return historyid;
        }

        public void setHistoryid(int historyid) {
            this.historyid = historyid;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getIs_free() {
            return is_free;
        }

        public void setIs_free(int is_free) {
            this.is_free = is_free;
        }

        public String getThumb_app_h() {
            return thumb_app_h;
        }

        public void setThumb_app_h(String thumb_app_h) {
            this.thumb_app_h = thumb_app_h;
        }
    }
}
