package com.example.map;

public class HelperTowingShop {
    String shopid, owner_name,owner_cnic,shop_name,shop_regno,owner_phone,owner_email,password,timage;

    public HelperTowingShop(String shopid, String owner_name, String owner_cnic, String shop_name, String shop_regno, String owner_phone, String owner_email, String password, String timage) {
        this.shopid = shopid;
        this.owner_name = owner_name;
        this.owner_cnic = owner_cnic;
        this.shop_name = shop_name;
        this.shop_regno = shop_regno;
        this.owner_phone = owner_phone;
        this.owner_email = owner_email;
        this.password = password;
        this.timage=timage;
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


    public String getTimage() {
        return timage;
    }

    public void setTimage(String timage) {
        this.timage = timage;
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
}
