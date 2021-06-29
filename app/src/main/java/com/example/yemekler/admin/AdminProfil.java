package com.example.yemekler.admin;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.yemekler.R;
import com.example.yemekler.asci.AsciReg;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import static com.example.yemekler.R.string.Sifre_Girin;
import static com.example.yemekler.R.string.Telefon_Girin;
import static com.example.yemekler.R.string.Isim_Girin;

public class AdminProfil extends AppCompatActivity {
    EditText etname, etphone, etpass;
    CheckBox cbasci;
    Button register;
    String username, password, id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);
        register = findViewById(R.id.btnregister);
        register.setText("Güncelle");
        etname = findViewById(R.id.etName);
        etphone = findViewById(R.id.etPhone);
        etpass = findViewById(R.id.etPassword);
        cbasci = findViewById(R.id.cbAsci);
        cbasci.setVisibility(View.GONE);
        username = getIntent().getStringExtra("NAME");
        password = getIntent().getStringExtra("PASSWORD");
        etname.setText(username);
        etpass.setText(password);
    }
}
