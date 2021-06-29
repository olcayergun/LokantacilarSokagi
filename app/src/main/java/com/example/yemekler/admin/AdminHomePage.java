package com.example.yemekler.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.yemekler.KayitGiris;
import com.example.yemekler.R;
import com.example.yemekler.asci.AsciHomePage;
import com.example.yemekler.asci.AsciProfil;

public class AdminHomePage extends AppCompatActivity {
    Button profil, cikis, uyeler, ascilar;
    String username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home_page);

        String ca = getIntent().getStringExtra("CALLINGACTIVITY");
        username = getIntent().getStringExtra("NAME");
        password = getIntent().getStringExtra("PASSWORD");

        profil = (Button) findViewById(R.id.bAdminProfil);
        profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomePage.this, AdminProfil.class);
                intent.putExtra("NAME", username);
                intent.putExtra("PASSWORD", password);
                startActivity(intent);
            }
        });
        cikis = findViewById(R.id.btnCikis);
        cikis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        uyeler = findViewById(R.id.btnUyeler);
        uyeler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomePage.this, Uyeler.class);
                intent.putExtra("NAME", username);
                intent.putExtra("PASSWORD", password);
                startActivity(intent);
            }
        });
        ascilar = findViewById(R.id.btnAscilar);
        ascilar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomePage.this, Ascilar.class);
                intent.putExtra("NAME", username);
                intent.putExtra("PASSWORD", password);
                startActivity(intent);
            }
        });
    }

    public void onBackPressed() {
        startActivity(new Intent(AdminHomePage.this, KayitGiris.class));
    }
}
