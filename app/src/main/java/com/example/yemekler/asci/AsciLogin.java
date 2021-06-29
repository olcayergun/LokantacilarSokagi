package com.example.yemekler.asci;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.yemekler.KayitGiris;
import com.example.yemekler.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AsciLogin extends AppCompatActivity {
    EditText stfname, stfpass;
    TextView stfstatus;
    Button stflogin;
    String sna, spa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asci_login);
        stfname = (EditText) findViewById(R.id.stfname);
        stfpass = (EditText) findViewById(R.id.stfpass);
        stfstatus = (TextView) findViewById(R.id.stfstatus);
        stflogin = (Button) findViewById(R.id.stfAscilogin);
        stfstatus.setText("");
        stflogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference databaseStaff = FirebaseDatabase.getInstance().getReference("staffReg");
                sna = stfname.getText().toString();
                spa = stfpass.getText().toString();
                if (TextUtils.isEmpty(sna))
                    stfstatus.setText(R.string.Isim_Girin);
                else if (TextUtils.isEmpty(spa))
                    stfstatus.setText(R.string.Sifre_Girin);
                else {
                    databaseStaff.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            int success = 0;
                            for (DataSnapshot staffSnapshot : dataSnapshot.getChildren()) {
                                AsciReg staffReg = staffSnapshot.getValue(AsciReg.class);
                                final String dpn = staffReg.getStaffname();
                                final String dpa = staffReg.getPassword();
                                final String tel = staffReg.getTelno();
                                if (tel.equals(sna) && dpa.equals(spa)) {
                                    Intent intent = new Intent(AsciLogin.this, AsciHomePage.class);
                                    intent.putExtra("NAME", sna);
                                    intent.putExtra("PASSWORD", dpa);
                                    startActivity(intent);
                                    success = 1;
                                    break;
                                }

                            }
                            if (success == 0)
                                stfstatus.setText("Hatalı giriş!!!");
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });
    }

    public void onBackPressed() {
        startActivity(new Intent(AsciLogin.this, KayitGiris.class));
    }
}
