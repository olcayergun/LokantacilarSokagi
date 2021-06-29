package com.example.yemekler;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.yemekler.admin.AdminLogin;
import com.example.yemekler.asci.AsciLogin;
import com.example.yemekler.uye.UyeLogin;

public class KayitGiris extends AppCompatActivity {
    Button reg, log, sifrehatirlat;
    TextView admlog, ascilog, about;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_log_choice);
        reg = (Button) findViewById(R.id.btnreg);
        log = (Button) findViewById(R.id.btnlog);
        admlog = (TextView) findViewById(R.id.admlog);
        ascilog = (TextView) findViewById(R.id.ascilog);
        about = (TextView) findViewById(R.id.tvAbout);
        sifrehatirlat = (Button) findViewById(R.id.btnSifreHatirlat);

        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(KayitGiris.this, UyeLogin.class);
                startActivity(intent);
            }
        });
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(KayitGiris.this, KayitSayfasi.class);
                startActivity(intent);
            }
        });
        admlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(KayitGiris.this, AdminLogin.class));
            }
        });
        ascilog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(KayitGiris.this, AsciLogin.class));
            }
        });
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(KayitGiris.this, About.class));
            }
        });

        sifrehatirlat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(KayitGiris.this, SifreHatirlat.class));
            }
        });
    }

    public void onBackPressed() {
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }
}
