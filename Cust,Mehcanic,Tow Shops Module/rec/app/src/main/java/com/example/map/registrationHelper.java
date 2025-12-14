package com.example.map;

public class registrationHelper {
    String uid,username,email,phone,cnic,pimage,pass;

    public registrationHelper(String uid, String username, String email, String phone, String cnic,String pass, String pimage) {
        this.uid = uid;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.cnic = cnic;
        this.pimage=pimage;
        this.pass=pass;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
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

    public String getPimage() {
        return pimage;
    }

    public void setPimage(String pimage) {
        this.pimage = pimage;
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
}
