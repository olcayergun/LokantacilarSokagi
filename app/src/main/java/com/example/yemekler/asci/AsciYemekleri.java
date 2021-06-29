package com.example.yemekler.asci;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yemekler.R;
import com.example.yemekler.RecyclerAdapterYemek;
import com.example.yemekler.YemekEkle;
import com.example.yemekler.YemekReg;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class AsciYemekleri extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerAdapterYemek adapter;
    private RecyclerView.LayoutManager layoutManager;
    private String sna;
    private String sph;
    private String spa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asci_yemekleri);
        recyclerView = findViewById(R.id.rvYemekler);
        Button bYemekEkle = findViewById(R.id.bYemekEkle);
        final String fsna = getIntent().getStringExtra("NAME");
        final String fsph = getIntent().getStringExtra("PHONE");
        final String fspa = getIntent().getStringExtra("PASSWORD");
        sna = fsna;
        sph = fsph;
        spa = fspa;

        bYemekEkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AsciYemekleri.this, YemekEkle.class);
                intent.putExtra("NAME", fsna);
                intent.putExtra("PHONE", fsph);
                intent.putExtra("PASSWORD", fspa);
                startActivity(intent);
            }
        });

        getYemekler();
    }

    private void getYemekler()  {
        Query query = FirebaseDatabase.getInstance().getReference().child("YemeklerReg").orderByChild("asci").equalTo(sna);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("Adaer", String.valueOf(dataSnapshot.getChildrenCount()));
                int count = (int) dataSnapshot.getChildrenCount();
                String[] yemekId = new String[count];
                String[] details = new String[count];
                String[] names = new String[count];
                int[] prices = new int[count];
                float[] puans = new float[count];
                String[] images = new String[count];
                int i = 0;
                for (DataSnapshot child: dataSnapshot.getChildren()){
                    YemekReg yemekReg = child.getValue(YemekReg.class);
                    images[i] = yemekReg.getBitmap();
                    yemekId[i] = yemekReg.getId();
                    details[i] = yemekReg.getIcerigi();
                    names[i] = yemekReg.getItemName();
                    prices[i] = Integer.parseInt(yemekReg.getFiyat());
                    puans[i] = Float.valueOf(0);
                    i++;
                }

                layoutManager = new GridLayoutManager(getApplicationContext(), 1);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                adapter = new RecyclerAdapterYemek(yemekId, images, names, details, prices, puans, getApplicationContext(), sna, sph, spa, this.getClass().getName());
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("Adaer", databaseError.getMessage());
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        getYemekler();
    }

    public void onBackPressed() {
        Intent intent = new Intent(AsciYemekleri.this, AsciHomePage.class);
        intent.putExtra("NAME", sna);
        intent.putExtra("PASSWORD", spa);
        startActivity(intent);
    }
}
