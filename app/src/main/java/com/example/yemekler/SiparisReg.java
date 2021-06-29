package com.example.yemekler;

public class SiparisReg {
    String siparisId, yemekId, durumu, asci, uye, zaman;
    int adedi, puan, TeslimatSekli;
    float tutar;

    public SiparisReg() {

    }

    public float getTutar() {
        return tutar;
    }

    public void setTutar(float tutar) {
        this.tutar = tutar;
    }

    public SiparisReg(String siparisId, String yemekId, String durumu, int adedi, String asci, int puan, int teslimatsekli, String uye, String zaman, float tutar) {
        this.siparisId = siparisId;
        this.yemekId = yemekId;
        this.adedi = adedi;
        this.durumu = durumu;
        this.asci = asci;
        this.uye = uye;
        this.puan = puan;
        this.TeslimatSekli = teslimatsekli;
        this.zaman = zaman;
        this.tutar = tutar;
    }

    public String getSiparisId() {
        return siparisId;
    }

    public String getYemekId() {
        return yemekId;
    }

    public String getDurumu() {
        return durumu;
    }

    public int getAdedi() {
        return adedi;
    }

    public String getAsci() {
        return asci;
    }

    public String getUye() {
        return uye;
    }

    public int getPuan() {
        return puan;
    }

    public void setTeslimatSekli(int teslimatSekli) {
        TeslimatSekli = teslimatSekli;
    }

    public int getTeslimatSekli() {
        return TeslimatSekli;
    } //0:GelAl, 1:EveTeslim

    public String getZaman() {
        return zaman;
    }
}
