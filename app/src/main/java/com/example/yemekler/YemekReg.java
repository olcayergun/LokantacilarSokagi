package com.example.yemekler;

public class YemekReg {
    String itemName, id, icerigi, asci, fiyat;
    int currentStockAvailaible;
    float puan;
    String bitmap;

    public YemekReg() {
    }

    public YemekReg(String itemName, String id, String icerigi, String asci, int currentStockAvailaible, String fiyat, String bitmap, float puan) {
        this.itemName = itemName;
        this.id = id;
        this.icerigi = icerigi;
        this.asci = asci;
        this.fiyat = fiyat;
        this.currentStockAvailaible = currentStockAvailaible;
        this.bitmap = bitmap;
        this.puan = puan;
    }

    public String getItemName() {
        return itemName;
    }

    public String getId() {
        return id;
    }

    public String getIcerigi() {
        return icerigi;
    }

    public String getAsci() {
        return asci;
    }

    public String getFiyat() {
        return fiyat;
    }

    public int getCurrentStockAvailaible() {
        return currentStockAvailaible;
    }

    public String getBitmap() {
        return bitmap;
    }

    public float getPuan() {
        return puan;
    }
}
