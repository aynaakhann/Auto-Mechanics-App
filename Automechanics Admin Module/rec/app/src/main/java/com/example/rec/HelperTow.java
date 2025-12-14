package com.example.rec;

public class HelperTow {
    String shopid, owner_name,owner_cnic,shop_name,shop_regno,owner_phone,owner_email,password,reg_img,rpayment,date_of_the_month,time,charges;
HelperTow(){

}

    public HelperTow(String shopid, String owner_name, String owner_cnic, String shop_name, String shop_regno, String owner_phone, String owner_email, String password, String reg_img, String rpayment, String date_of_the_month, String time, String charges) {
        this.shopid = shopid;
        this.owner_name = owner_name;
        this.owner_cnic = owner_cnic;
        this.shop_name = shop_name;
        this.shop_regno = shop_regno;
        this.owner_phone = owner_phone;
        this.owner_email = owner_email;
        this.password = password;
        this.reg_img = reg_img;
        this.rpayment = rpayment;
        this.date_of_the_month = date_of_the_month;
        this.time = time;
        this.charges = charges;
    }

    public String getShopid() {
        return shopid;
    }

    public void setShopid(String shopid) {
        this.shopid = shopid;
    }

    public String getOwner_name() {
        return owner_name;
    }

    public void setOwner_name(String owner_name) {
        this.owner_name = owner_name;
    }

    public String getOwner_cnic() {
        return owner_cnic;
    }

    public void setOwner_cnic(String owner_cnic) {
        this.owner_cnic = owner_cnic;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getShop_regno() {
        return shop_regno;
    }

    public void setShop_regno(String shop_regno) {
        this.shop_regno = shop_regno;
    }

    public String getOwner_phone() {
        return owner_phone;
    }

    public void setOwner_phone(String owner_phone) {
        this.owner_phone = owner_phone;
    }

    public String getOwner_email() {
        return owner_email;
    }

    public void setOwner_email(String owner_email) {
        this.owner_email = owner_email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getReg_img() {
        return reg_img;
    }

    public void setReg_img(String reg_img) {
        this.reg_img = reg_img;
    }

    public String getRpayment() {
        return rpayment;
    }

    public void setRpayment(String rpayment) {
        this.rpayment = rpayment;
    }

    public String getDate_of_the_month() {
        return date_of_the_month;
    }

    public void setDate_of_the_month(String date_of_the_month) {
        this.date_of_the_month = date_of_the_month;
    }

    public String time() {
        return time;
    }

    public void setDate(String time) {
        this.time = time;
    }

    public String getCharges() {
        return charges;
    }

    public void setCharges(String charges) {
        this.charges = charges;
    }
}
