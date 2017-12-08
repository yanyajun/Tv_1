package tv.dfyc.yckt.beans;

import java.util.List;

/**
 * Created by admin on 2017-8-3.
 */

public class JsonLessonData {
    private String SeqNo;
    private int Result;
    private String Message;
    private DetailData data;

    public String getSeqNo() {
        return SeqNo;
    }
    public void setSeqNo(String seqNo) {
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

    public DetailData getData() {
        return data;
    }
    public void setData(DetailData data) {
        this.data = data;
    }

    public class DetailData {
        private String notice;
        private List<Lesson> list;

        public String getNotice() {
            return notice;
        }
        public void setNotice(String notice) {
            this.notice = notice;
        }

        public List<Lesson> getList() {
            return list;
        }
        public void setList(List<Lesson> list) {
            this.list = list;
        }

        public class Lesson {
            private int library_id;
            private String title;
            private String thumb_app_h;
            private int is_free;

            public String getTitle() {
                return title;
            }
            public void setTitle(String title) {
                this.title = title;
            }

            public int getLibrary_id() {
                return library_id;
            }
            public void setLibrary_id(int library_id) {
                this.library_id = library_id;
            }

            public String getThumb_app_h() {
                return thumb_app_h;
            }
            public void setThumb_app_h(String thumb_app_h) {
                this.thumb_app_h = thumb_app_h;
            }

            public int getIs_free() {
                return is_free;
            }
            public void setIs_free(int is_free) {
                this.is_free = is_free;
            }
        }
    }
}
