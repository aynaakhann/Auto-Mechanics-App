package com.example.rec;

public class HelperMech_complains {
    String mid,cid,mname,mcnic,mphone,address,statuscomplain,memail,mpass,mimage,date,time,charges;

    HelperMech_complains(){}

    public HelperMech_complains(String mid, String cid, String mname, String mcnic, String mphone, String address, String statuscomplain, String memail, String mpass, String mimage, String date, String time, String charges) {
        this.mid = mid;
        this.cid = cid;
        this.mname = mname;
        this.mcnic = mcnic;
        this.mphone = mphone;
        this.address = address;
        this.statuscomplain = statuscomplain;
        this.memail = memail;
        this.mpass = mpass;
        this.mimage = mimage;
        this.date = date;
        this.time = time;
        this.charges = charges;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getMname() {
        return mname;
    }

    public void setMname(String mname) {
        this.mname = mname;
    }

    public String getMcnic() {
        return mcnic;
    }

    public void setMcnic(String mcnic) {
        this.mcnic = mcnic;
    }

    public String getMphone() {
        return mphone;
    }

    public void setMphone(String mphone) {
        this.mphone = mphone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatuscomplain() {
        return statuscomplain;
    }

    public void setStatuscomplain(String statuscomplain) {
        this.statuscomplain = statuscomplain;
    }

    public String getMemail() {
        return memail;
    }

    public void setMemail(String memail) {
        this.memail = memail;
    }

    public String getMpass() {
        return mpass;
    }

    public void setMpass(String mpass) {
        this.mpass = mpass;
    }

    public String getMimage() {
        return mimage;
    }

    public void setMimage(String mimage) {
        this.mimage = mimage;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCharges() {
        return charges;
    }

    public void setCharges(String charges) {
        this.charges = charges;
    }
}
