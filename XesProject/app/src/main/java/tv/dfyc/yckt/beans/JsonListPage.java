package tv.dfyc.yckt.beans;

import java.io.Serializable;
import java.util.List;

public class JsonListPage implements Serializable {

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

    public pageData getData() {
        return data;
    }

    public void setData(pageData data) {
        this.data = data;
    }

    private String SeqNo;
    private int Result;
    private String Message;
    private pageData data;


    /* 网站首页 */
    public class pageData implements Serializable{
        public int getNode_id() {
            return node_id;
        }

        public void setNode_id(int node_id) {
            this.node_id = node_id;
        }

        public String getNode_name() {
            return node_name;
        }

        public void setNode_name(String node_name) {
            this.node_name = node_name;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public int getNode_type() {
            return node_type;
        }

        public void setNode_type(int node_type) {
            this.node_type = node_type;
        }

        public List<pageDataDetail> getLists() {
            return lists;
        }

        public void setLists(List<pageDataDetail> lists) {
            this.lists = lists;
        }

        private int node_id ;
        private String node_name;
        private int sort;
        private int node_type;
        private List<pageDataDetail> lists;


        /*首页,小学....*/
        public class pageDataDetail implements Serializable{

            public int getNode_id() {
                return node_id;
            }

            public void setNode_id(int node_id) {
                this.node_id = node_id;
            }

            public String getNode_name() {
                return node_name;
            }

            public void setNode_name(String node_name) {
                this.node_name = node_name;
            }

            public int getSort() {
                return sort;
            }

            public void setSort(int sort) {
                this.sort = sort;
            }

            public int getNode_type() {
                return node_type;
            }

            public void setNode_type(int node_type) {
                this.node_type = node_type;
            }

            public List<grade> getLists() {
                return lists;
            }

            public void setLists(List<grade> lists) {
                this.lists = lists;
            }

            private int node_id ;
            private String node_name;
            private int sort;
            private int node_type;
            private List<grade> lists;

                /* 年级 */
                public class grade implements Serializable{

                    public int getNode_id() {
                        return node_id;
                    }

                    public void setNode_id(int node_id) {
                        this.node_id = node_id;
                    }

                    public String getNode_name() {
                        return node_name;
                    }

                    public void setNode_name(String node_name) {
                        this.node_name = node_name;
                    }

                    public int getSort() {
                        return sort;
                    }

                    public void setSort(int sort) {
                        this.sort = sort;
                    }

                    public int getNode_type() {
                        return node_type;
                    }

                    public void setNode_type(int node_type) {
                        this.node_type = node_type;
                    }

                    public List<course> getLists() {
                        return lists;
                    }

                    public void setLists(List<course> lists) {
                        this.lists = lists;
                    }

                    private int node_id ;
                    private String node_name;
                    private int sort;
                    private int node_type;
                    private List<course> lists;

                    /*课程*/
                    public class course implements Serializable{
                        public int getNode_id() {
                            return node_id;
                        }

                        public void setNode_id(int node_id) {
                            this.node_id = node_id;
                        }

                        public String getNode_name() {
                            return node_name;
                        }

                        public void setNode_name(String node_name) {
                            this.node_name = node_name;
                        }

                        public int getSort() {
                            return sort;
                        }

                        public void setSort(int sort) {
                            this.sort = sort;
                        }

                        public int getNode_type() {
                            return node_type;
                        }

                        public void setNode_type(int node_type) {
                            this.node_type = node_type;
                        }

                        public List getLists() {
                            return lists;
                        }

                        public void setLists(List lists) {
                            this.lists = lists;
                        }

                        public String getNode_thumb() {
                            return node_thumb;
                        }

                        public void setNode_thumb(String node_thumb) {
                            this.node_thumb = node_thumb;
                        }

                        private int node_id ;
                        private String node_name;
                        private int sort;
                        private int node_type;
                        private String node_thumb;
                        private List lists;
                    }

                }

            }
        }

    //}

}
