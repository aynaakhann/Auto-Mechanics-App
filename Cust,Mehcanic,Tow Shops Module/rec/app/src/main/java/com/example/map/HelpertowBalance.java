package com.example.map;

public class HelpertowBalance {
    String complain,key, statuscomplain,cid,shopid;
    HelpertowBalance(){}

    public HelpertowBalance(String complain, String key, String statuscomplain, String cid, String shopid) {
        this.complain = complain;
        this.key = key;
        this.statuscomplain = statuscomplain;
        this.cid = cid;
        this.shopid = shopid;
    }

    public String getComplain() {
        return complain;
    }

    public void setComplain(String complain) {
        this.complain = complain;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getStatuscomplain() {
        return statuscomplain;
    }

    public void setStatuscomplain(String statuscomplain) {
        this.statuscomplain = statuscomplain;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getShopid() {
        return shopid;
    }

    public void setShopid(String shopid) {
        this.shopid = shopid;
    }
}
