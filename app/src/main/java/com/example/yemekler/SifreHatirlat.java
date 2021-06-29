package com.example.yemekler;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.yemekler.uye.UyeReg;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import static com.example.yemekler.R.string.Telefon_Girin;

public class SifreHatirlat extends AppCompatActivity {
    Button btnSifreHatir;
    EditText etTelefonNo;
    RadioButton rbUye, rbAsci;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sifre_hatirlat);
        btnSifreHatir = findViewById(R.id.btnSifreHatir);
        etTelefonNo = findViewById(R.id.etSifreHatirTelefonNo);
        rbAsci = findViewById(R.id.rb_SifreHatirAsci);
        rbUye = findViewById(R.id.rb_SifreHatirUye);

        btnSifreHatir.setOnClickListener(v -> sifrehatirlat());
    }

    public void sifrehatirlat() {
        String telefonno = etTelefonNo.getText().toString();
        if (!rbUye.isChecked() && !rbAsci.isChecked()) {
            Toast.makeText(this, "Üye tipini girin.", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(telefonno)) {
            Toast.makeText(this, Telefon_Girin, Toast.LENGTH_LONG).show();
        } else {
            if (rbUye.isChecked()) {
                Query query = FirebaseDatabase.getInstance().getReference("memberReg").orderByChild("telno").equalTo(telefonno);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (0 == (int) dataSnapshot.getChildrenCount()) {
                            Toast.makeText(getBaseContext(), "Kullanıcı bulunumadı.", Toast.LENGTH_LONG).show();
                        } else {
                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                UyeReg uyeReg = child.getValue(UyeReg.class);
                                String msg = "Üye Adı : " + uyeReg.getStaffname() + "\n";
                                msg += "Şifre : " + uyeReg.getPassword();
                                Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.d("Adaer", databaseError.getMessage());
                    }
                });
            } else if (rbAsci.isChecked()) {
                Query query = FirebaseDatabase.getInstance().getReference("staffReg").orderByChild("telno").equalTo(telefonno);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (0 == (int) dataSnapshot.getChildrenCount()) {
                            Toast.makeText(getBaseContext(), "Kullanıcı bulunumadı.", Toast.LENGTH_LONG).show();
                        } else {
                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                UyeReg uyeReg = child.getValue(UyeReg.class);
                                String msg = "Aşçı Adı : " + uyeReg.getStaffname() + "\n";
                                msg += "Şifre : " + uyeReg.getPassword();
                                Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.d("Adaer", databaseError.getMessage());
                    }
                });
            } else {
                Log.d("Adaer", "Ne Üye ne de aşçı düğmesi işaretlenmiş....");
            }
        }
    }

}
