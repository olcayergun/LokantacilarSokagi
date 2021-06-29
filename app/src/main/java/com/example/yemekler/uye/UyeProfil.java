package com.example.yemekler.uye;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import static com.example.yemekler.R.string.Isim_Girin;
import static com.example.yemekler.R.string.Sifre_Girin;
import static com.example.yemekler.R.string.Telefon_Girin;

public class UyeProfil extends AppCompatActivity {
    EditText etname, etphone, etpass, etadres;
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
        etadres = findViewById(R.id.etAdres);
        cbasci = findViewById(R.id.cbAsci);
        cbasci.setVisibility(View.GONE);
        username = getIntent().getStringExtra("NAME");
        password = getIntent().getStringExtra("PASSWORD");

        Query query = FirebaseDatabase.getInstance().getReference().child("memberReg").orderByChild("staffname").equalTo(username);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count = (int) dataSnapshot.getChildrenCount();
                if (0 < count) {
                    fillDataSegments(dataSnapshot);
                } else {
                    Log.d("Adaer", "Doyuran yok. " + username);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Adaer", databaseError.getMessage());
            }
        });

        Query queryTel = FirebaseDatabase.getInstance().getReference().child("memberReg").orderByChild("telno").equalTo(username);
        queryTel.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count = (int) dataSnapshot.getChildrenCount();
                if (0 < count) {
                    fillDataSegments(dataSnapshot);
                } else {
                    Log.d("Adaer", "Doyuran yok. " + username);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Adaer", databaseError.getMessage());
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reg();
            }
        });
    }

    private void fillDataSegments(DataSnapshot dataSnapshot) {
        for (DataSnapshot child : dataSnapshot.getChildren()) {
            UyeReg memberReg = child.getValue(UyeReg.class);
            id = memberReg.getId();
            etname.setText(memberReg.getStaffname());
            etphone.setText(memberReg.getTelno());
            etpass.setText(memberReg.getPassword());
            etadres.setText(memberReg.getAdres());
        }
    }

    public void reg() {
        String name = etname.getText().toString();
        String phone = etphone.getText().toString();
        String password = etpass.getText().toString();
        String adres = etadres.getText().toString();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, Isim_Girin, Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, Telefon_Girin, Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, Sifre_Girin, Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(adres)) {
            Toast.makeText(this, "Adresinizi girin.", Toast.LENGTH_SHORT).show();
        } else {
            DatabaseReference databaseStaff = FirebaseDatabase.getInstance().getReference("memberReg");
            UyeReg uyeReg = new UyeReg(id, name, password, phone, adres);
            databaseStaff.child(id).setValue(uyeReg);
            Toast.makeText(this, "Bilgiler güncellendi.", Toast.LENGTH_SHORT).show();
        }
    }
}
