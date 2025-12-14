package com.example.rec;

public class HelperMechanicRegistration {
    String mid,mname,mcnic,mage,mphone,memail,mpass,mimage,category,recimg,charges,cnicfrontpic,cnicbackpic;

    HelperMechanicRegistration(){

    }

    public HelperMechanicRegistration(String mid, String mname, String mcnic, String mage, String mphone, String memail, String mpass, String mimage, String category, String recimg, String charges, String cnicfrontpic, String cnicbackpic) {
        this.mid = mid;
        this.mname = mname;
        this.mcnic = mcnic;
        this.mage = mage;
        this.mphone = mphone;
        this.memail = memail;
        this.mpass = mpass;
        this.mimage = mimage;
        this.category = category;
        this.recimg = recimg;
        this.charges = charges;
        this.cnicfrontpic = cnicfrontpic;
        this.cnicbackpic = cnicbackpic;
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

    public String getRecimg() {
        return recimg;
    }

    public void setRecimg(String recimg) {
        this.recimg = recimg;
    }

    public String getCharges() {
        return charges;
    }

    public void setCharges(String charges) {
        this.charges = charges;
    }

    public String getCnicfrontpic() {
        return cnicfrontpic;
    }

    public void setCnicfrontpic(String cnicfrontpic) {
        this.cnicfrontpic = cnicfrontpic;
    }

    public String getCnicbackpic() {
        return cnicbackpic;
    }

    public void setCnicbackpic(String cnicbackpic) {
        this.cnicbackpic = cnicbackpic;
    }
}
