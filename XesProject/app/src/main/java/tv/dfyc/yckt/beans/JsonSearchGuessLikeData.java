package tv.dfyc.yckt.beans;

import java.util.List;

/**
 * Created by Administrator on 2017/8/11.
 */

public class JsonSearchGuessLikeData {

    private String SeqNo;
    private String Result;
    private String Message;
    private SearchData data;

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

    public SearchData getData() {
        return data;
    }
    public void setData(SearchData data) {
        this.data = data;
    }

    public class SearchData {
        private int page;
        private int allcount;
        private int total;
        private int count;
        private List<DataDetail> list;

        public int getPage() {
            return page;
        }
        public void setPage(int page) {
            this.page = page;
        }

        public int getAllcount() {
            return allcount;
        }
        public void setAllcount(int allcount) {
            this.allcount = allcount;
        }

        public int getTotal() {
            return total;
        }
        public void setTotal(int total) {
            this.total = total;
        }

        public int getCount() {
            return count;
        }
        public void setCount(int count) {
            this.count = count;
        }

        public List<DataDetail> getList() {
            return list;
        }
        public void setList(List<DataDetail> list) {
            this.list = list;
        }

        public class DataDetail {
            private String category_id;
            private String category_name;
            private String category_names;
            private String num;

            public String getCategory_id() {
                return category_id;
            }
            public void setCategory_id(String category_id) {
                this.category_id = category_id;
            }

            public String getCategory_name() {
                return category_name;
            }
            public void setCategory_name(String category_name) {
                this.category_name = category_name;
            }

            public String getCategory_names() {
                return category_names;
            }
            public void setCategory_names(String category_names) {
                this.category_names = category_names;
            }

            public String getNum() {
                return num;
            }
            public void setNum(String num) {
                this.num = num;
            }
        }
    }
}
