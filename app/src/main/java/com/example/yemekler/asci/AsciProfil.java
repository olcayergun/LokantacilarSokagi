package com.example.yemekler.asci;

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

import static com.example.yemekler.R.string.Hesap_Yaratildi;
import static com.example.yemekler.R.string.Sifre_Girin;
import static com.example.yemekler.R.string.Telefon_Girin;
import static com.example.yemekler.R.string.Isim_Girin;

public class AsciProfil extends AppCompatActivity {
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

        Query query = FirebaseDatabase.getInstance().getReference().child("staffReg").orderByChild("staffname").equalTo(username);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count = (int) dataSnapshot.getChildrenCount();
                if (1 == count) {
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

        Query queryTel = FirebaseDatabase.getInstance().getReference().child("staffReg").orderByChild("telno").equalTo(username);
        queryTel.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count = (int) dataSnapshot.getChildrenCount();
                if (1 == count) {
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

    private void fillDataSegments (DataSnapshot dataSnapshot) {
        for (DataSnapshot child : dataSnapshot.getChildren()) {
            AsciReg asciReg = child.getValue(AsciReg.class);
            id = asciReg.getId();
            etname.setText(asciReg.getStaffname());
            etphone.setText(asciReg.getTelno());
            etpass.setText(asciReg.getPassword());
            etadres.setText(asciReg.getAdres());
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
            DatabaseReference databaseStaff = FirebaseDatabase.getInstance().getReference("staffReg");
            AsciReg staffReg = new AsciReg(name, password, id, phone, adres);
            databaseStaff.child(id).setValue(staffReg);
            Toast.makeText(this, "Bilgiler güncellendi.", Toast.LENGTH_SHORT).show();
        }
    }
}
