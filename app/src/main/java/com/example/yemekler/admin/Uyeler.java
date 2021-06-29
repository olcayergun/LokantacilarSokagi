package com.example.yemekler.admin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yemekler.R;
import com.example.yemekler.uye.UyeReg;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Uyeler extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerAdapterUyeler adapter;
    private RecyclerView.LayoutManager layoutManager;
    private String sna;
    private String spa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asci_siparisler);
        recyclerView = findViewById(R.id.rvYemekler);
        sna = getIntent().getStringExtra("NAME");
        spa = getIntent().getStringExtra("PASSWORD");

        getUyeler();
    }

    private void getUyeler() {
        Query query = FirebaseDatabase.getInstance().getReference().child("memberReg").orderByChild("staffname");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count = (int) dataSnapshot.getChildrenCount();
                String[] Id = new String[count];
                String[] staffname = new String[count];
                String[] password = new String[count];
                String[] telno = new String[count];
                int i = 0;
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    UyeReg uyeReg = child.getValue(UyeReg.class);
                    Id[i] = child.getKey();
                    staffname[i] = uyeReg.getStaffname();
                    password[i] = uyeReg.getPassword();
                    telno[i] = uyeReg.getTelno();
                    i++;
                }

                layoutManager = new GridLayoutManager(getApplicationContext(), 1);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                adapter = new RecyclerAdapterUyeler(Id, staffname, password, telno, getApplicationContext(), sna, spa, this.getClass().getName());
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
        getUyeler();
    }

    public void onBackPressed() {
        Intent intent = new Intent(Uyeler.this, AdminHomePage.class);
        intent.putExtra("NAME", sna);
        intent.putExtra("PASSWORD", spa);
        startActivity(intent);
    }
}
