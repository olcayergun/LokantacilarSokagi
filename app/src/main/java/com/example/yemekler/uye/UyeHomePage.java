package com.example.yemekler.uye;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.yemekler.R;

public class UyeHomePage extends AppCompatActivity {
    Button siparisler, yemekler, profil, cikis;
    String sna, dpa;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uye_home_page);
        sna = getIntent().getStringExtra("NAME");
        dpa = getIntent().getStringExtra("PASSWORD");
        Toast.makeText(this, "Welcome, " + sna + "!", Toast.LENGTH_SHORT).show();

        siparisler = (Button) findViewById(R.id.bSiparisler);
        siparisler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UyeHomePage.this, UyeSiparisler.class);
                intent.putExtra("NAME", sna);
                intent.putExtra("PASSWORD", dpa);
                startActivity(intent);
            }
        });

        yemekler = (Button) findViewById(R.id.bYemekler);
        yemekler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UyeHomePage.this, UyeYemekSec.class);
                intent.putExtra("NAME", sna);
                intent.putExtra("PASSWORD", dpa);
                startActivity(intent);
            }
        });

        profil = (Button) findViewById(R.id.bUyeProfil);
        profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UyeHomePage.this, UyeProfil.class);
                intent.putExtra("NAME", sna);
                intent.putExtra("PASSWORD", dpa);
                startActivity(intent);
            }
        });

        cikis = findViewById(R.id.btnCikisUye);
        cikis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    public void onBackPressed() {
        startActivity(new Intent(UyeHomePage.this, UyeLogin.class));
    }

}
