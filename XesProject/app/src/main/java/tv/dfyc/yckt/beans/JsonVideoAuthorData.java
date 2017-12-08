package tv.dfyc.yckt.beans;

import java.util.List;

/**
 * Created by Administrator on 2017/8/16.
 */

public class JsonVideoAuthorData {
    private String Result;
    private String UserID;
    private List<ProductData> ProductList;
    private int Balance;
    private String AuthCode;

    public String getResult() {
        return Result;
    }
    public void setResult(String result) {
        Result = result;
    }

    public String getUserID() {
        return UserID;
    }
    public void setUserID(String userID) {
        UserID = userID;
    }

    public List<ProductData> getProductList() {
        return ProductList;
    }
    public void setProductList(List<ProductData> productList) {
        ProductList = productList;
    }

    public int getBalance() {
        return Balance;
    }
    public void setBalance(int balance) {
        Balance = balance;
    }

    public String getAuthCode() {
        return AuthCode;
    }
    public void setAuthCode(String authCode) {
        AuthCode = authCode;
    }

    public class ProductData {
        private String ProductCode;
        private String Name;
        private int Type;
        private int BizType;
        private int Price;
        private String ValidStartTime;
        private String ValidEndTime;

        public String getProductCode() {
            return ProductCode;
        }
        public void setProductCode(String productCode) {
            ProductCode = productCode;
        }

        public String getName() {
            return Name;
        }
        public void setName(String name) {
            Name = name;
        }

        public int getType() {
            return Type;
        }
        public void setType(int type) {
            Type = type;
        }

        public int getBizType() {
            return BizType;
        }
        public void setBizType(int bizType) {
            BizType = bizType;
        }

        public int getPrice() {
            return Price;
        }
        public void setPrice(int price) {
            Price = price;
        }

        public String getValidStartTime() {
            return ValidStartTime;
        }
        public void setValidStartTime(String validStartTime) {
            ValidStartTime = validStartTime;
        }

        public String getValidEndTime() {
            return ValidEndTime;
        }
        public void setValidEndTime(String validEndTime) {
            ValidEndTime = validEndTime;
        }
    }
}
