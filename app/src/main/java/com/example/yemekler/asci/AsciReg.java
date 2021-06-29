package com.example.yemekler.asci;

public class AsciReg {
    String staffname, password, id, telno, adres;

    public AsciReg(){

    }

    public AsciReg(String staffname, String password, String id, String telno, String adres) {
        this.staffname = staffname;
        this.password = password;
        this.id = id;
        this.telno = telno;
        this.adres = adres;
    }

    public String getStaffname() {
        return staffname;
    }

    public String getPassword() {
        return password;
    }

    public String getId() {
        return id;
    }

    public String getTelno() {
        return telno;
    }

    public String getAdres() {
        return adres;
    }
}
