package com.example.yemekler.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.yemekler.KayitGiris;
import com.example.yemekler.R;

public class AdminLogin extends AppCompatActivity {
    EditText admuser, admpass;
    Button admlog;
    TextView admstatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        admuser = (EditText) findViewById(R.id.admuser);
        admpass = (EditText) findViewById(R.id.admpass);
        admlog = (Button) findViewById(R.id.admlogin);
        admstatus = (TextView) findViewById(R.id.admstatus);
        admstatus.setText("");
        admlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = admuser.getText().toString();
                String password = admpass.getText().toString();
                if (username.equals("admin") && password.equals("a")) {
                    Intent intent = new Intent(AdminLogin.this, AdminHomePage.class);
                    intent.putExtra("CALLINGACTIVITY", "AdminLogin");
                    intent.putExtra("NAME", username);
                    intent.putExtra("PASSWORD", password);
                    startActivity(intent);
                } else {
                    admstatus.setText("Hatalı giriş denemesi...");
                }
            }
        });

    }

    public void onBackPressed() {
        startActivity(new Intent(AdminLogin.this, KayitGiris.class));
    }
}
