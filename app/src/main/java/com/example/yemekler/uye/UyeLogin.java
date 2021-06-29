package com.example.yemekler.uye;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.yemekler.KayitGiris;
import com.example.yemekler.KayitSayfasi;
import com.example.yemekler.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UyeLogin extends AppCompatActivity {
    EditText phoneno, pass;
    Button login;
    TextView status;
    String ph, pa;
    static String tmpname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        phoneno = (EditText) findViewById(R.id.logphone);
        pass = (EditText) findViewById(R.id.logpass);
        login = (Button) findViewById(R.id.btnlogin);
        status = (TextView) findViewById(R.id.tvstatus);
        status.setText("");

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KayitSayfasi.getuser();
                ph = phoneno.getText().toString();
                pa = pass.getText().toString();
                DatabaseReference databaseUye = FirebaseDatabase.getInstance().getReference("memberReg");
                databaseUye.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int x = 0;
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            UyeReg memberReg = userSnapshot.getValue(UyeReg.class);
                            String dpn = memberReg.getStaffname();
                            String dph = memberReg.getTelno();
                            String dpa = memberReg.getPassword();
                            if (dph.equals(ph) && dpa.equals(pa)) {
                                Intent i = new Intent(UyeLogin.this, UyeHomePage.class);
                                i.putExtra("NAME", dpn);
                                i.putExtra("PHONE", dph);
                                i.putExtra("PASSWORD", dpa);
                                i.putExtra("CALLINGACTIVITY", "UyeLogin");
                                startActivity(i);
                                x = 1;
                                break;
                            }
                        }
                        if (x == 0)
                            status.setText(R.string.Hatali_Giris);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });
    }

    public void onBackPressed() {
        startActivity(new Intent(UyeLogin.this, KayitGiris.class));
    }
}