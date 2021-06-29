package com.example.yemekler;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.yemekler.asci.AsciReg;
import com.example.yemekler.uye.UyeYemekSec;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class YemekGoster extends AppCompatActivity {
    public static DatabaseReference databaseYemekler, databaseSiparisler;
    TextView isim, icerik, stockAdedi, fiyat, asci, adres, puani;
    Button btnUpdate;
    RadioButton rbGelAl, rbEveTeslim;
    ImageView ivYemekGoster;
    int yemekstockadedi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uye_yemek_sec);
        isim = findViewById(R.id.tv_uyeYemekSecAdi);
        icerik = findViewById(R.id.tv_uyeYemekSecIcerigi);
        fiyat = findViewById(R.id.tv_uyeYemekSecFiyat);
        stockAdedi = findViewById(R.id.tv_uyeYemekSecMiktari);
        asci = findViewById(R.id.tv_uyeYemekSecAsciAdi);
        adres = findViewById(R.id.tv_uyeYemekSecAsciAdresi);
        puani = findViewById(R.id.tv_uyeYemekSecPuani);
        rbEveTeslim = findViewById(R.id.rb_uyeYemekSecEveTeslim);
        rbGelAl = findViewById(R.id.rb_uyeYemekSecGelAl);
        btnUpdate = findViewById(R.id.btn_uyeYemekSec);
        ivYemekGoster = findViewById(R.id.iv_uyeYemekSecResim);

        final String sna = getIntent().getStringExtra("NAME");
        final String yemekId = getIntent().getStringExtra("yemekId");

        databaseYemekler = FirebaseDatabase.getInstance().getReference("YemeklerReg");
        if (null != yemekId && !"".equals(yemekId)) {
            Query query = databaseYemekler.orderByChild("id").equalTo(yemekId);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        YemekReg yemek = child.getValue(YemekReg.class);
                        isim.setText(yemek.getItemName());
                        icerik.setText(yemek.getIcerigi());
                        fiyat.setText(yemek.getFiyat());
                        yemekstockadedi = yemek.getCurrentStockAvailaible();
                        String asciIsim = yemek.getAsci();
                        getAsciAdres(asciIsim);
                        getYemekPuan(yemekId);
                        Bitmap bitmap = decodeFromFirebaseBase64(yemek.getBitmap());
                        if (null != bitmap) {
                            ivYemekGoster.setImageBitmap(bitmap);
                        }
                        stockAdedi.setText("");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d("Adaer", "Yemek bulunurken hata:" + databaseError.getMessage());
                }

            });
        } else {
            btnUpdate.setText(R.string.yemek_ekle);
        }

        btnUpdate.setOnClickListener(v -> {
            if (TextUtils.isEmpty((isim.getText()).toString()))
                itemIdNotEntered();
            else if (TextUtils.isEmpty((stockAdedi.getText()).toString()))
                quantityNotEntered();
            else if (TextUtils.isEmpty((icerik.getText()).toString()))
                icerikNotEntered();
            else {
                int TeslimatSekli = -1;
                if (rbGelAl.isChecked()) {
                    TeslimatSekli = 0;
                }
                if (rbEveTeslim.isChecked()) {
                    TeslimatSekli = 1;
                }
                if (TeslimatSekli == -1) {
                    noHandling();
                    return;
                }
                int siparisadedi = Integer.parseInt(stockAdedi.getText().toString());
                if (yemekstockadedi < siparisadedi) {
                    noStockLeft();
                    return;
                }

                int puan = 0;
                databaseSiparisler = FirebaseDatabase.getInstance().getReference("siparislerReg");
                String siparisId = databaseSiparisler.push().getKey();

                SimpleDateFormat s = new SimpleDateFormat("ddMMyyyyhhmmss");
                String timestamp = s.format(new Date());

                float f_fiyat = Float.valueOf(fiyat.getText().toString());
                float tutar = siparisadedi * f_fiyat;
                SiparisReg siparisReg = new SiparisReg(
                        siparisId,
                        yemekId,
                        "Yeni",
                        siparisadedi,
                        asci.getText().toString(),
                        puan,
                        TeslimatSekli,
                        sna,
                        timestamp,
                        tutar);
                databaseSiparisler.child(siparisId).setValue(siparisReg, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        showSuccess();

                        databaseYemekler = FirebaseDatabase.getInstance().getReference("YemeklerReg");
                        if (null != yemekId && !"".equals(yemekId)) {
                            Query query = databaseYemekler.orderByChild("id").equalTo(yemekId);
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    YemekReg yemek = null;
                                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                                        yemek = child.getValue(YemekReg.class);
                                        yemek.currentStockAvailaible = yemekstockadedi - siparisadedi;
                                        Log.d("Adaer", "Yemek kalan sipariş adedi:" + yemek.getCurrentStockAvailaible());
                                    }
                                    if ( null != yemek) {
                                        FirebaseDatabase.getInstance().getReference("YemeklerReg").child(yemekId).setValue(yemek);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Log.d("Adaer", "Yemek bulunurken hata:" + databaseError.getMessage());
                                }

                            });
                        }
                    }
                });
            }
        });
    }

    private void getAsciAdres(String asciIsim) {
        DatabaseReference databaseYemekler = FirebaseDatabase.getInstance().getReference("staffReg");
        if (null != asciIsim && !"".equals(asciIsim)) {
            Query query = databaseYemekler.orderByChild("staffname").equalTo(asciIsim);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    int i = 0;
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        AsciReg asciReg = child.getValue(AsciReg.class);
                        adres.setText(asciReg.getAdres());
                        asci.setText(asciReg.getStaffname());
                        i++;
                    }

                    if (0 == i) {
                        getAsciAdresByTelNo(asciIsim);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d("Adaer", "Yemek bulunurken hata:" + databaseError.getMessage());
                    adres.setText("Adresi yok.");
                }

            });
        } else {
            adres.setText("Adresi yok.");
        }
    }

    private void getAsciAdresByTelNo(String asciIsim) {
        DatabaseReference databaseYemekler = FirebaseDatabase.getInstance().getReference("staffReg");
        if (null != asciIsim && !"".equals(asciIsim)) {
            Query query = databaseYemekler.orderByChild("telno").equalTo(asciIsim);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    int i = 0;
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        AsciReg asciReg = child.getValue(AsciReg.class);
                        adres.setText(asciReg.getAdres());
                        i++;
                    }

                    if (0 == i) {
                        adres.setText("Aşçinın adresi yok.");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d("Adaer", "Yemek bulunurken hata:" + databaseError.getMessage());
                    adres.setText("Adresi yok.");
                }

            });
        } else {
            adres.setText("Adresi yok.");
        }
    }

    private void getYemekPuan(String yemekId) {
        Query query = FirebaseDatabase.getInstance().getReference().child("siparislerReg").orderByChild("yemekId").equalTo(yemekId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                float toplam = 0;
                int adet = 0;
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    SiparisReg siparisReg = child.getValue(SiparisReg.class);
                    toplam += siparisReg.getPuan();
                    adet++;
                }
                if (0 == adet) {
                    puani.setText("0");
                } else {
                    float ortPuani = toplam / adet;
                    puani.setText(String.valueOf(ortPuani));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Adaer: HATAAAAA ", databaseError.getMessage());
                puani.setText("--");
            }
        });
    }

    public Bitmap decodeFromFirebaseBase64(String image) {
        try {
            byte[] decodedByteArray = android.util.Base64.decode(image, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
        } catch (Exception e) {
            Log.d("Adaer", "", e);
        }
        return null;
    }

    public void itemIdNotEntered() {
        Toast.makeText(this, "Yemek adını girin.", Toast.LENGTH_SHORT).show();
    }

    public void quantityNotEntered() {
        Toast.makeText(this, "Yemek adedini girin.", Toast.LENGTH_SHORT).show();
    }

    public void icerikNotEntered() {
        Toast.makeText(this, "Lütfen, yemek iceriğini girin.", Toast.LENGTH_SHORT).show();
    }

    public void showSuccess() {
        Toast.makeText(this, "Siparişiniz alındı.", Toast.LENGTH_SHORT).show();
    }

    public void noStockLeft() {
        Toast.makeText(this, "Elimizde kalmadı.", Toast.LENGTH_SHORT).show();
    }

    public void noHandling() {
        Toast.makeText(this, "Teslimat şeklini seçin.", Toast.LENGTH_SHORT).show();
    }

    public static void getStocks() {
        databaseYemekler = FirebaseDatabase.getInstance().getReference("siparislerReg");
    }

    public void onDestroy() {
        super.onDestroy();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    public void onBackPressed() {
        startActivity(new Intent(YemekGoster.this, UyeYemekSec.class));
        super.onBackPressed();
    }
}
