package tv.dfyc.yckt.beans;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/8/8.
 */

public class JsonGoodsListData implements Serializable {
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
        private String goods_id;
        private String price;
        private int goodstype;
        private String name;
        private String description;
        private String thumb;
        private String remarks; // 备注
        private String goods_endtime; // 过期提醒

        public String getGoods_id() {
            return goods_id;
        }
        public void setGoods_id(String goods_id) {
            this.goods_id = goods_id;
        }

        public String getPrice() {
            return price;
        }
        public void setPrice(String price) {
            this.price = price;
        }

        public int getGoodstype() {
            return goodstype;
        }
        public void setGoodstype(int goodstype) {
            this.goodstype = goodstype;
        }

        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }
        public void setDescription(String description) {
            this.description = description;
        }

        public String getThumb() {
            return thumb;
        }
        public void setThumb(String thumb) {
            this.thumb = thumb;
        }

        public String getRemarks() {
            return remarks;
        }
        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }

        public String getGoods_endtime() {
            return goods_endtime;
        }
        public void setGoods_endtime(String goods_endtime) {
            this.goods_endtime = goods_endtime;
        }
    }
}
