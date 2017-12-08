package tv.dfyc.yckt.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by android on 2017/11/23.
 */

public class ThemeContent {
    private String background_img;
    private int is_order;//是否订购  0 未订购 1订购
    private int is_collection;//是否收藏 0 未收藏 1已收藏
    private List<ThemeList> list = new ArrayList<>();
    private int goods_id;//专题商品id
    private String goods_name;//专题商品名称
    private String price;//价格
    private String thumb;//图片
    private String remarks;//有效期
    private String description;//简介
    private int historyid;//删除收藏的参数
    private String goods_endtime;//是否过期

    @Override
    public String toString() {
        return "ThemeContent{" +
                "background_img='" + background_img + '\'' +
                ", is_order=" + is_order +
                ", is_collection=" + is_collection +
                ", list=" + list +
                ", goods_id=" + goods_id +
                ", goods_name='" + goods_name + '\'' +
                ", price='" + price + '\'' +
                ", thumb='" + thumb + '\'' +
                ", remarks='" + remarks + '\'' +
                ", description='" + description + '\'' +
                ", historyid=" + historyid +
                ", goods_endtime=" + goods_endtime +
                '}';
    }

    public String getGoods_endtime() {
        return goods_endtime;
    }

    public void setGoods_endtime(String goods_endtime) {
        this.goods_endtime = goods_endtime;
    }

    public int getHistoryid() {
        return historyid;
    }

    public void setHistoryid(int historyid) {
        this.historyid = historyid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(int goods_id) {
        this.goods_id = goods_id;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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

    public String getBackground_img() {
        return background_img;
    }

    public void setBackground_img(String background_img) {
        this.background_img = background_img;
    }

    public int getIs_order() {
        return is_order;
    }

    public void setIs_order(int is_order) {
        this.is_order = is_order;
    }

    public int getIs_collection() {
        return is_collection;
    }

    public void setIs_collection(int is_collection) {
        this.is_collection = is_collection;
    }

    public List<ThemeList> getList() {
        return list;
    }

    public void setList(List<ThemeList> list) {
        this.list = list;
    }
}
