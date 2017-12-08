package tv.dfyc.yckt.beans;

import java.util.List;

/**
 * Created by admin on 2017-11-22.
 */

public class JsonViewRecordData {
    private String SeqNo;
    private String Result;
    private String Message;
    private List<ViewRecord> data;

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

    public List<ViewRecord> getData() {
        return data;
    }

    public void setData(List<ViewRecord> data) {
        this.data = data;
    }

    public class  ViewRecord{

        private int library_id;
        private String library_name;
        private int video_id;
        private String library_image;
        private int last_play_time;
        private String video_name;
        private String video_duration;
        private int video_is_free;
        private int is_free;
        private int historyid;

        public int getVideo_is_free() {
            return video_is_free;
        }

        public void setVideo_is_free(int video_is_free) {
            this.video_is_free = video_is_free;
        }

        public int getHistoryid() {
            return historyid;
        }

        public void setHistoryid(int historyid) {
            this.historyid = historyid;
        }

        public String getLibrary_name() {
            return library_name;
        }

        public void setLibrary_name(String library_name) {
            this.library_name = library_name;
        }

        public int getLibrary_id() {
            return library_id;
        }

        public void setLibrary_id(int library_id) {
            this.library_id = library_id;
        }

        public int getIs_free() {
            return is_free;
        }

        public void setIs_free(int is_free) {
            this.is_free = is_free;
        }

        public String getVideo_name() {
            return video_name;
        }

        public void setVideo_name(String video_name) {
            this.video_name = video_name;
        }

        public int getVideo_id() {
            return video_id;
        }

        public void setVideo_id(int video_id) {
            this.video_id = video_id;
        }

        public String getVideo_duration() {
            return video_duration;
        }

        public void setVideo_duration(String video_duration) {
            this.video_duration = video_duration;
        }

        public int getLast_play_time() {
            return last_play_time;
        }

        public void setLast_play_time(int last_play_time) {
            this.last_play_time = last_play_time;
        }

        public String getLibrary_image() {
            return library_image;
        }

        public void setLibrary_image(String library_image) {
            this.library_image = library_image;
        }
    }
}
