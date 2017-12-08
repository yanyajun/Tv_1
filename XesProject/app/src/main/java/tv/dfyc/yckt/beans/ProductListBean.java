package tv.dfyc.yckt.beans;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/8/8.
 */

public class ProductListBean implements Serializable {
    private String SeqNo;
    private String Result;
    private String Message;
    private List<GoodDetail> data;

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

    public List<GoodDetail> getData() {
        return data;
    }
    public void setData(List<GoodDetail> data) {
        this.data = data;
    }

    public static class GoodDetail  implements Serializable {
        private int goods_id;
        private String product_name;
        private String product_price;
        private String product_image;
        private String product_notice; // 备注
        private String goods_endtime; // 过期提醒

        public String getGoods_endtime() {
            return goods_endtime;
        }

        public void setGoods_endtime(String goods_endtime) {
            this.goods_endtime = goods_endtime;
        }

        public int getGoods_id() {
            return goods_id;
        }

        public void setGoods_id(int goods_id) {
            this.goods_id = goods_id;
        }

        public String getProduct_name() {
            return product_name;
        }

        public void setProduct_name(String product_name) {
            this.product_name = product_name;
        }

        public String getProduct_price() {
            return product_price;
        }

        public void setProduct_price(String product_price) {
            this.product_price = product_price;
        }

        public String getProduct_image() {
            return product_image;
        }

        public void setProduct_image(String product_image) {
            this.product_image = product_image;
        }

        public String getProduct_notice() {
            return product_notice;
        }

        public void setProduct_notice(String product_notice) {
            this.product_notice = product_notice;
        }
    }
}
