package com.example.yemekler;

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

import com.example.yemekler.asci.AsciReg;
import com.example.yemekler.uye.UyeReg;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.yemekler.R.string.Hesap_Yaratildi;
import static com.example.yemekler.R.string.Isim_Girin;
import static com.example.yemekler.R.string.Sifre_Girin;
import static com.example.yemekler.R.string.Telefon_Girin;

public class KayitSayfasi extends AppCompatActivity {
    EditText etname, etphone, etpass, etadres;
    CheckBox cbasci;
    Button register;
    public static DatabaseReference databaseUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);
        databaseUsers = FirebaseDatabase.getInstance().getReference("memberReg");
        register = findViewById(R.id.btnregister);
        etname = findViewById(R.id.etName);
        etphone = findViewById(R.id.etPhone);
        etpass = findViewById(R.id.etPassword);
        cbasci = findViewById(R.id.cbAsci);
        etadres = findViewById(R.id.etAdres);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reg();
            }
        });
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
            if (cbasci.isChecked()) {
                DatabaseReference databaseStaff = FirebaseDatabase.getInstance().getReference("staffReg");
                String id = databaseStaff.push().getKey();
                AsciReg staffReg = new AsciReg(name, password, id, phone, adres);
                databaseStaff.child(id).setValue(staffReg);
            } else {
                String id = databaseUsers.push().getKey();
                UyeReg memberReg = new UyeReg(id, name, password, phone, adres);
                databaseUsers.child(id).setValue(memberReg).addOnFailureListener(new OnFailureListener() {

                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("OLcay", e.getLocalizedMessage());
                    }
                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("OLcay", "Yazıldı");
                    }
                });
            }
            Toast.makeText(this, Hesap_Yaratildi, Toast.LENGTH_SHORT).show();
        }
    }

    public static void getuser() {
        databaseUsers = FirebaseDatabase.getInstance().getReference("memberReg");
    }
}
