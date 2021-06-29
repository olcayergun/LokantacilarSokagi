package com.example.yemekler.puanlama;

public class PuanReg {
    String yemekId;
    String uyeId;
    String puan;

    public PuanReg() {
    }

    public PuanReg(String yemekId, String uyeId, String puan) {
        this.yemekId = yemekId;
        this.uyeId = uyeId;
        this.puan = puan;
    }

    public String getYemekId() {
        return yemekId;
    }

    public String getUyeId() {
        return uyeId;
    }

    public String getPuan() {
        return puan;
    }
}
