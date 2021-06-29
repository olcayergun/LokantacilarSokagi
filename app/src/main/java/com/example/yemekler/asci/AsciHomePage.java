package com.example.yemekler.asci;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.yemekler.R;

public class AsciHomePage extends AppCompatActivity {
    Button siparisler, yemekler, profil, cikis, cuzdan;
    String sna, spa;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asci_home_page);
        sna = getIntent().getStringExtra("NAME");
        spa = getIntent().getStringExtra("PASSWORD");
        Toast.makeText(this, "Welcome, " + sna + "!", Toast.LENGTH_SHORT).show();

        siparisler = (Button) findViewById(R.id.bSiparisler);
        siparisler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AsciHomePage.this, AsciSiparisler.class);
                intent.putExtra("NAME", sna);
                intent.putExtra("PASSWORD", spa);
                startActivity(intent);
            }
        });

        cuzdan = (Button) findViewById(R.id.btnAsciCuzdan);
        cuzdan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AsciHomePage.this, AsciCuzdan.class);
                intent.putExtra("NAME", sna);
                intent.putExtra("PASSWORD", spa);
                startActivity(intent);
            }
        });

        yemekler = (Button) findViewById(R.id.bYemekler);
        yemekler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AsciHomePage.this, AsciYemekleri.class);
                intent.putExtra("NAME", sna);
                intent.putExtra("PASSWORD", spa);
                startActivity(intent);
            }
        });

        profil = (Button) findViewById(R.id.bProfil);
        profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AsciHomePage.this, AsciProfil.class);
                intent.putExtra("NAME", sna);
                intent.putExtra("PASSWORD", spa);
                startActivity(intent);
            }
        });

        cikis = findViewById(R.id.btnCikisAsci);
        cikis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void onBackPressed() {
        Intent intent = new Intent(AsciHomePage.this, AsciLogin.class);
        intent.putExtra("NAME", sna);
        intent.putExtra("PASSWORD", spa);
        startActivity(intent);
    }

}
