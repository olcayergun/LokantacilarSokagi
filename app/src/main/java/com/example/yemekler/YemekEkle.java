package com.example.yemekler;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.yemekler.asci.AsciYemekleri;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class YemekEkle extends AppCompatActivity {
    public static DatabaseReference databaseYemekler;
    EditText isim, icerik, stockAdedi, fiyat;
    Button btnUpdate, btnResimSec, btnYemekSil;
    int PICK_IMAGE_REQUEST = 111;
    ImageView imgView;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yemek_ekle);
        isim = findViewById(R.id.etIsim);
        icerik = findViewById(R.id.etIcerigi);
        fiyat = findViewById(R.id.etFiyat);
        stockAdedi = findViewById(R.id.etAdedi);
        btnUpdate = findViewById(R.id.btnUpdateStock);
        btnResimSec = findViewById(R.id.bChooseImage);
        btnYemekSil = findViewById(R.id.btnYemekSil);
        imgView = findViewById(R.id.ivYemek);
        final String sna = getIntent().getStringExtra("NAME");
        final String yemekId = getIntent().getStringExtra("yemekId");

        databaseYemekler = FirebaseDatabase.getInstance().getReference("YemeklerReg");
        if (null != yemekId && !"".equals(yemekId)) {
            btnUpdate.setText("Değiştir");
            Query query = databaseYemekler.orderByChild("id").equalTo(yemekId);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        YemekReg yemek = child.getValue(YemekReg.class);
                        isim.setText(yemek.getItemName());
                        icerik.setText(yemek.getIcerigi());
                        fiyat.setText(yemek.getFiyat());
                        stockAdedi.setText(String.valueOf(yemek.getCurrentStockAvailaible()));
                        Bitmap bitmap = decodeFromFirebaseBase64(yemek.getBitmap());
                        imgView.setImageBitmap(bitmap);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d("Adaer", "Yemek bulunurken hata:" + databaseError.getMessage());
                }

            });
        } else {
            btnUpdate.setText(R.string.yemek_ekle);
            btnYemekSil.setVisibility(View.GONE);
        }

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty((isim.getText()).toString()))
                    itemIdNotEntered();
                else if (TextUtils.isEmpty((stockAdedi.getText()).toString()))
                    quantityNotEntered();
                else if (TextUtils.isEmpty((icerik.getText()).toString()))
                    icerikNotEntered();
                else {
                    String id;
                    if (null != yemekId && !"".equals(yemekId)) {
                        id = yemekId;
                    } else {
                        id = databaseYemekler.push().getKey();
                    }

                    String imageEncoded = "";
                    if (null != bitmap) {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                        imageEncoded = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
                    }

                    YemekReg yemekReg = new YemekReg(
                            isim.getText().toString(),
                            id,
                            icerik.getText().toString(),
                            sna,
                            Integer.parseInt(stockAdedi.getText().toString()),
                            fiyat.getText().toString(),
                            imageEncoded,
                            0
                    );
                    databaseYemekler.child(id).setValue(yemekReg, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                            showSuccess();
                        }
                    });
                }
            }
        });

        btnYemekSil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("OLcay", "Sil düğmesine basıldı." + yemekId);
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("YemeklerReg").child(yemekId);
                reference.removeValue();

                Toast.makeText(YemekEkle.this, isim.getText().toString() + " adlı yemek silindi.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(YemekEkle.this, AsciYemekleri.class);
                intent.putExtra("NAME", sna);
                startActivity(intent);
            }
        });

        btnResimSec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(intent, "Yemek resmini seçin."), PICK_IMAGE_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && null != data) {
            Uri imageUri = data.getData();
            imgView.setImageURI(imageUri);
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
            } catch (IOException e) {

            }
        } else {
            Toast.makeText(this, "Resim seçilemedi.", Toast.LENGTH_SHORT).show();
        }
    }

    public Bitmap decodeFromFirebaseBase64(String image) {
        try {
            byte[] decodedByteArray = android.util.Base64.decode(image, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
        } catch (Exception e) {
            Log.d("Adaer", "", e);
        }
        return null;
    }

    public void itemIdNotEntered() {
        Toast.makeText(this, "Yemek adını girin.", Toast.LENGTH_SHORT).show();
    }

    public void quantityNotEntered() {
        Toast.makeText(this, "Yemek adedini girin.", Toast.LENGTH_SHORT).show();
    }

    public void icerikNotEntered() {
        Toast.makeText(this, "Lütfen, yemek iceriğini girin.", Toast.LENGTH_SHORT).show();
    }


    public void showSuccess() {
        Toast.makeText(this, "Yemek eklendi / güncellendi.", Toast.LENGTH_SHORT).show();
    }

    public static void getStocks() {
        databaseYemekler = FirebaseDatabase.getInstance().getReference("YemeklerReg");
    }

    public void onDestroy() {
        super.onDestroy();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    public void onBackPressed() {
        startActivity(new Intent(YemekEkle.this, AsciYemekleri.class));
        super.onBackPressed();
    }
}
