package tv.dfyc.yckt.beans;

/**
 * Created by admin on 2017-11-22.
 */

public class JsonAboutUsData {
    private String SeqNo;
    private String Result;
    private String Message;
    private imageUrl data;

    public imageUrl getData() {
        return data;
    }

    public void setData(imageUrl data) {
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

    public class imageUrl{
        String image_url;

        public String getImage_url() {
            return image_url;
        }

        public void setImage_url(String image_url) {
            this.image_url = image_url;
        }
    }

}
