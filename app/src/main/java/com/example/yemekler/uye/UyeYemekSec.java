package com.example.yemekler.uye;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yemekler.R;
import com.example.yemekler.RecyclerAdapterYemek;
import com.example.yemekler.SiparisReg;
import com.example.yemekler.YemekReg;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class UyeYemekSec extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerAdapterYemek adapter;
    private RecyclerView.LayoutManager layoutManager;
    private String sna;
    private String sph;
    private String spa;
    private java.util.HashMap<String, Float> yemekpuan = new HashMap<String, Float>();
    private java.util.HashMap<String, Integer> yemekadet = new HashMap<String, Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        recyclerView = findViewById(R.id.rvYemekler);
        sna = getIntent().getStringExtra("NAME");
        sph = getIntent().getStringExtra("PHONE");
        spa = getIntent().getStringExtra("PASSWORD");
        getYemekPuan();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_page, menu);
        return true;
    }

    public void onBackPressed() {
        Intent intent = new Intent(UyeYemekSec.this, UyeHomePage.class);
        intent.putExtra("NAME", sna);
        intent.putExtra("PASSWORD", spa);
        startActivity(intent);
    }

    private void getYemekPuan() {
        Query query = FirebaseDatabase.getInstance().getReference().child("siparislerReg");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    SiparisReg siparisReg = child.getValue(SiparisReg.class);
                    String yemekId = null;
                    if (siparisReg != null) {
                        yemekId = siparisReg.getYemekId();
                        if (yemekadet.containsKey(yemekId)) {
                            int adet = yemekadet.get(yemekId);
                            float avg = yemekpuan.get(yemekId);
                            float newavg = (adet * avg + siparisReg.getPuan()) / (adet + 1);
                            yemekadet.put(yemekId, adet + 1);
                            yemekpuan.put(yemekId, newavg);
                        } else {
                            yemekadet.put(yemekId, 1);
                            yemekpuan.put(yemekId, Float.valueOf(siparisReg.getPuan()));
                        }
                    }
                }
                getYemekler();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Adaer: HATAAAAA ", databaseError.getMessage());
                getYemekler();
            }
        });
    }

    private void getYemekler() {
        Query query = FirebaseDatabase.getInstance().getReference().child("YemeklerReg").orderByChild("asci");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("Adaer", String.valueOf(dataSnapshot.getChildrenCount()));
                int count = (int) dataSnapshot.getChildrenCount();
                String[] yemekId = new String[count];
                String[] details = new String[count];
                String[] names = new String[count];
                int[] prices = new int[count];
                String[] images = new String[count];
                float[] puans = new float[count];
                int i = 0;
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    YemekReg yemekReg = child.getValue(YemekReg.class);
                    images[i] = yemekReg.getBitmap();
                    yemekId[i] = yemekReg.getId();
                    details[i] = yemekReg.getIcerigi();
                    names[i] = yemekReg.getItemName();
                    prices[i] = Integer.parseInt(yemekReg.getFiyat());
                    if (yemekpuan.containsKey(yemekId[i])) {
                        puans[i] = yemekpuan.get(yemekId[i]);
                    } else {
                        puans[i] = Float.valueOf(0);
                    }
                    i++;
                }

                layoutManager = new GridLayoutManager(getApplicationContext(), 1);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                adapter = new RecyclerAdapterYemek(yemekId, images, names, details, prices, puans, getApplicationContext(), sna, sph, spa, this.getClass().getName());
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Adaer", databaseError.getMessage());
            }
        });
    }
}
