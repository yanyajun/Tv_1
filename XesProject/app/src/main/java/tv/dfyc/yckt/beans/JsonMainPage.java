package tv.dfyc.yckt.beans;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yyj on 2017-11-17.
 */

public class JsonMainPage implements Serializable {
    private String SeqNo;
    private int Result;
    private String Message;
    private MainData data;

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

    public MainData getData() {
        return data;
    }
    public void setData(MainData data) {
        this.data = data;
    }

    /* 网站首页 */
    public class MainData {
        private String image;
        private String notice;
        private List<RecommendData> xiaoxue;
        private List<RecommendData> chuzhong;
        private List<RecommendData> gaozhong;

        public String getImage() {
            return image;
        }
        public void setImage(String image) {
            this.image = image;
        }

        public String getNotice() {
            return notice;
        }
        public void setNotice(String notice) {
            this.notice = notice;
        }

        public List<RecommendData> getXiaoxue() {
            return xiaoxue;
        }
        public void setXiaoxue(List<RecommendData> xiaoxue) {
            this.xiaoxue = xiaoxue;
        }

        public List<RecommendData> getChuzhong() {
            return chuzhong;
        }
        public void setChuzhong(List<RecommendData> chuzhong) {
            this.chuzhong = chuzhong;
        }

        public List<RecommendData> getGaozhong() {
            return gaozhong;
        }
        public void setGaozhong(List<RecommendData> gaozhong) {
            this.gaozhong = gaozhong;
        }

        public class RecommendData {
            private String tuijian_type;
            private String tuijian_number_image;
            private String name;
            private int id;

            public String getTuijian_type() {
                return tuijian_type;
            }
            public void setTuijian_type(String tuijian_type) {
                this.tuijian_type = tuijian_type;
            }

            public String getTuijian_number_image() {
                return tuijian_number_image;
            }
            public void setTuijian_number_image(String tuijian_number_image) {
                this.tuijian_number_image = tuijian_number_image;
            }

            public String getName() {
                return name;
            }
            public void setName(String name) {
                this.name = name;
            }
            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }
        }
    }

}
