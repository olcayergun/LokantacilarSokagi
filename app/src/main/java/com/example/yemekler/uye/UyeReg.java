package com.example.yemekler.uye;

public class UyeReg {

    String staffname, password, telno, adres;
    String id;
    public UyeReg(){

    }
    public UyeReg(String id, String username, String password, String telno, String adres) {
        this.id = id;
        this.staffname = username;
        this.password = password;
        this.telno = telno;
        this.adres = adres;
    }

    public String getStaffname() {
        return staffname;
    }

    public String getPassword() {
        return password;
    }

    public String getTelno() {
        return telno;
    }

    public String getId() {
        return id;
    }

    public String getAdres() {
        return adres;
    }
}
