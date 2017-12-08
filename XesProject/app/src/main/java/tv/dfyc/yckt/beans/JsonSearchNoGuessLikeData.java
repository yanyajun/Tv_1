package tv.dfyc.yckt.beans;

import java.util.List;

/**
 * Created by Administrator on 2017/8/11.
 */

public class JsonSearchNoGuessLikeData {

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
            private String title;
            private String library_id;
            private String num;

            public String getTitle() {
                return title;
            }
            public void setTitle(String title) {
                this.title = title;
            }

            public String getLibrary_id() {
                return library_id;
            }
            public void setLibrary_id(String library_id) {
                this.library_id = library_id;
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
