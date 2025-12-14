package com.example.rec;

public class Helpercust {
    String uid,username,email,phone,cnic,password,image,receipt;

    Helpercust(){

    }

    public Helpercust(String uid, String username, String email, String phone, String cnic, String password, String image, String receipt) {
        this.uid = uid;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.cnic = cnic;
        this.password = password;
        this.image = image;
        this.receipt = receipt;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getReceipt() {
        return receipt;
    }

    public void setReceipt(String receipt) {
        this.receipt = receipt;
    }
}
