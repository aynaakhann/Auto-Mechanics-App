package com.example.rec;

public class HelperMech_compmessage {
    String mid,mname,mcnic,mage,mphone,memail,mpass,mimage,category,date,time,deducted_charges,current_charges;
    HelperMech_compmessage(){
    }

    public HelperMech_compmessage(String mid, String mname, String mcnic, String mage, String mphone, String memail, String mpass, String mimage, String category, String date, String time, String deducted_charges, String current_charges) {
        this.mid = mid;
        this.mname = mname;
        this.mcnic = mcnic;
        this.mage = mage;
        this.mphone = mphone;
        this.memail = memail;
        this.mpass = mpass;
        this.mimage = mimage;
        this.category = category;
        this.date = date;
        this.time = time;
        this.deducted_charges = deducted_charges;
        this.current_charges = current_charges;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
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

    public String getMage() {
        return mage;
    }

    public void setMage(String mage) {
        this.mage = mage;
    }

    public String getMphone() {
        return mphone;
    }

    public void setMphone(String mphone) {
        this.mphone = mphone;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public String getDeducted_charges() {
        return deducted_charges;
    }

    public void setDeducted_charges(String deducted_charges) {
        this.deducted_charges = deducted_charges;
    }

    public String getCurrent_charges() {
        return current_charges;
    }

    public void setCurrent_charges(String current_charges) {
        this.current_charges = current_charges;
    }
}
