package com.example.yemekler;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class Yemek extends AppCompatActivity {
    ImageView imageView;
    TextView imageDetails, imagePrice;
    Button degistir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_item);
        imageView = findViewById(R.id.item_display);
        imageDetails = findViewById(R.id.item_details_display);
        imagePrice = findViewById(R.id.item_price_display);
        degistir = findViewById(R.id.degistir);
        imageView.setImageResource(getIntent().getIntExtra("image_id", 0));
        imageDetails.setText(getIntent().getStringExtra("item_details"));
        imagePrice.setText(Integer.toString(getIntent().getIntExtra("item_price", 0)));
        final String sna = getIntent().getStringExtra("NAME");
        final String sph = getIntent().getStringExtra("PHONE");
        final String spa = getIntent().getStringExtra("PASSWORD");
        final String ca = getIntent().getStringExtra("CALLING_ACTIVITY");
        final int img_id = getIntent().getIntExtra("image_id", 00);
        final String item_details = getIntent().getStringExtra("item_details");
        final String yemekId = getIntent().getStringExtra("yemekId");
        final String name = getIntent().getStringExtra("name");
        final int item_price = getIntent().getIntExtra("item_price", 00);
        YemekEkle.getStocks();
        YemekEkle.databaseYemekler.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot stockSnapshot : dataSnapshot.getChildren()) {
                    YemekReg stockReg = stockSnapshot.getValue(YemekReg.class);
                    String item_det = stockReg.getItemName();
                    if (item_det.equals(item_details)) {
                        if (stockReg.getCurrentStockAvailaible() == 0) {
                            degistir.setEnabled(false);
                            degistir.setText("Bitti.");
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        degistir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
