package com.example.yemekler.asci;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yemekler.R;
import com.example.yemekler.SiparisReg;
import com.example.yemekler.YemekReg;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class AsciCuzdan extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerAdapterCuzdan adapter;
    private RecyclerView.LayoutManager layoutManager;
    private String sna;
    private String sph;
    private String spa;
    private String ya = "";
    private float yf = 0;
    private Map<String, SiparisReg> siparisler = new HashMap<>();
    private Map<String, YemekReg> yemekler = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asci_siparisler);
        recyclerView = findViewById(R.id.rvYemekler);
        final String fsna = getIntent().getStringExtra("NAME");
        final String fsph = getIntent().getStringExtra("PHONE");
        final String fspa = getIntent().getStringExtra("PASSWORD");
        sna = fsna;
        sph = fsph;
        spa = fspa;

        getSiparisAsci();
    }

    private void getSiparisAsci() {
        Query query = FirebaseDatabase.getInstance().getReference().child("siparislerReg").orderByChild("asci").equalTo(sna);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                siparisler.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    SiparisReg siparisReg = child.getValue(SiparisReg.class);
                    siparisler.put(siparisReg.getSiparisId(), siparisReg);
                }
                getYemekAsci();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Adaer", databaseError.getMessage());
            }
        });
    }

    private void getYemekAsci() {
        Query query = FirebaseDatabase.getInstance().getReference().child("YemeklerReg").orderByChild("asci").equalTo(sna);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                yemekler.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    YemekReg yemekReg = child.getValue(YemekReg.class);
                    yemekler.put(yemekReg.getId(), yemekReg);
                }
                goNextActivity();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Adaer", databaseError.getMessage());
            }
        });


    }

    private void goNextActivity() {
        //grouping siparis according to yemek
        ArrayList<ArrayList<String>> yemeksatisler = new ArrayList<>();
        Iterator<String> it = yemekler.keySet().iterator();
        while (it.hasNext()) {
            YemekReg yemekReg = yemekler.get(it.next());
            String key = yemekReg.getId();
            ArrayList<String> satisdetay = new ArrayList<>();
            satisdetay.add(yemekReg.getItemName());
            satisdetay.add(yemekReg.getFiyat());

            //scanning siparisler
            int toplamsiparisadedi = 0;
            Iterator<String> it_siparis = siparisler.keySet().iterator();
            while (it_siparis.hasNext()) {
                SiparisReg siparisReg = siparisler.get(it_siparis.next());
                if (key.equals(siparisReg.getYemekId())) {
                    toplamsiparisadedi += siparisReg.getAdedi();
                }
            }
            satisdetay.add(Integer.toString(toplamsiparisadedi));

            yemeksatisler.add(satisdetay);
        }

        layoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerAdapterCuzdan(getApplicationContext(), sna, yemeksatisler);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        getSiparisAsci();
    }

    public void onBackPressed() {
        Intent intent = new Intent(AsciCuzdan.this, AsciHomePage.class);
        intent.putExtra("NAME", sna);
        intent.putExtra("PASSWORD", spa);
        startActivity(intent);
    }
}
