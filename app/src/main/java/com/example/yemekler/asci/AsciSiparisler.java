package com.example.yemekler.asci;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yemekler.R;
import com.example.yemekler.RecyclerAdapterSiparis;
import com.example.yemekler.SiparisReg;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class AsciSiparisler extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerAdapterSiparis adapter;
    private RecyclerView.LayoutManager layoutManager;
    private String sna;
    private String sph;
    private String spa;

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

        getSiparisler();
    }

    private void getSiparisler() {
        Query query = FirebaseDatabase.getInstance().getReference().child("siparislerReg").orderByChild("asci").equalTo(sna);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count = (int) dataSnapshot.getChildrenCount();
                String[] Id = new String[count];
                int[] adedi = new int[count];
                String[] asci = new String[count];
                String[] uye = new String[count];
                String[] durumu = new String[count];
                int[] puan = new int[count];
                String[] siparisId = new String[count];
                String[] yemekId = new String[count];
                String[] zaman = new String[count];
                int[] teslimatsekli = new int[count];
                float[] tutar = new float[count];

                int i = 0;
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    SiparisReg siparisReg = child.getValue(SiparisReg.class);
                    Id[i] = child.getKey();
                    siparisId[i] = siparisReg.getSiparisId();
                    yemekId[i] = siparisReg.getYemekId();
                    asci[i] = siparisReg.getAsci();
                    uye[i] = siparisReg.getUye();
                    adedi[i] = siparisReg.getAdedi();
                    durumu[i] = siparisReg.getDurumu();
                    puan[i] = siparisReg.getPuan();
                    teslimatsekli[i] = siparisReg.getTeslimatSekli();
                    zaman[i] = siparisReg.getZaman();
                    tutar[i] = siparisReg.getTutar();
                    i++;
                }

                layoutManager = new GridLayoutManager(getApplicationContext(), 1);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                adapter = new RecyclerAdapterSiparis(Id, asci, durumu, siparisId, yemekId, adedi, puan, teslimatsekli, tutar, getApplicationContext(), sna, sph, spa, this.getClass().getName(), uye, zaman);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Adaer", databaseError.getMessage());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getSiparisler();
    }

    public void onBackPressed() {
        Intent intent = new Intent(AsciSiparisler.this, AsciHomePage.class);
        intent.putExtra("NAME", sna);
        intent.putExtra("PASSWORD", spa);
        startActivity(intent);
    }
}
