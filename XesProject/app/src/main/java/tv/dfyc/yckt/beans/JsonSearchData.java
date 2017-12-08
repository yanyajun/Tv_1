package tv.dfyc.yckt.beans;

import java.util.List;

/**
 * Created by Administrator on 2017/8/11.
 */

public class JsonSearchData {
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

    public class SearchData{
        private List<Hotsearch_list> hotsearch_list;
        private List<Guess_list> guess_list;
        private List <Result_list> result_list;

        public List<Hotsearch_list> getHotsearch_list() {
            return hotsearch_list;
        }

        public void setHotsearch_list(List<Hotsearch_list> hotsearch_list) {
            this.hotsearch_list = hotsearch_list;
        }

        public List<Guess_list> getGuess_list() {
            return guess_list;
        }

        public void setGuess_list(List<Guess_list> guess_list) {
            this.guess_list = guess_list;
        }

        public List<Result_list> getResult_list() {
            return result_list;
        }

        public void setResult_list(List<Result_list> result_list) {
            this.result_list = result_list;
        }

        public class Hotsearch_list{
            private int library_id;
            private String title;

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
        }

        public class Guess_list{
            private int library_id;
            private String title;
            private int is_free;
            private String thumb_app_h;

            public int getLibrary_id() {
                return library_id;
            }

            public void setLibrary_id(int library_id) {
                this.library_id = library_id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
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

        public class Result_list{
            private int library_id;
            private String title;
            private int is_free;
            private String thumb_app_h;

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



}
