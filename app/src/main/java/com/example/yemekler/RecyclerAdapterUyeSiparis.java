package com.example.yemekler;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yemekler.uye.UyeReg;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class RecyclerAdapterUyeSiparis extends RecyclerView.Adapter<RecyclerAdapterUyeSiparis.ImageViewHolder> {
    String[] Id, asci, durumu, siparisId, yemekId, uyeId, zaman;
    int[] adedi, puan, teslimatsekli;
    String sna, spa, callingActivity;
    private final Context context;

    public RecyclerAdapterUyeSiparis(String[] Id, String[] asci, String[] durumu, String[] siparisId, String[] yemekId, int[] adedi, int[] puan, int[] teslimatsekli, Context context, String sna, String spa, String callingActivity, String[] uyeId, String[] zaman) {
        this.Id = Id;
        this.asci = asci;
        this.durumu = durumu;
        this.siparisId = siparisId;
        this.yemekId = yemekId;
        this.uyeId = uyeId;
        this.adedi = adedi;
        this.puan = puan;
        this.zaman = zaman;
        this.context = context;
        this.sna = sna;
        this.spa = spa;
        this.callingActivity = callingActivity;
        this.teslimatsekli = teslimatsekli;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.siparis_list_item, parent, false);
        return new ImageViewHolder(view, context, Id, asci, durumu, siparisId, yemekId, adedi, puan, teslimatsekli, sna, spa, callingActivity, uyeId, zaman);
    }

    @Override
    public void onBindViewHolder(@NonNull final ImageViewHolder holder, int position) {
        holder.tvSiparisNo.setText(Id[position]);
        String yID = yemekId[position];
        String uID = uyeId[position];
        if (null != uID) {
            Query queryUye = FirebaseDatabase.getInstance().getReference().child("memberReg").orderByChild("username").equalTo(uID);
            queryUye.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int count = (int) dataSnapshot.getChildrenCount();
                    if (1 == count) {
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            UyeReg uyeReg = child.getValue(UyeReg.class);
                            holder.tvUye.setText(uyeReg.getStaffname());
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d("Adaer", databaseError.getMessage());
                }
            });
        } else {
            holder.tvUye.setText("İsimsiz");
        }

        Query query = FirebaseDatabase.getInstance().getReference().child("YemeklerReg").orderByChild("id").equalTo(yID);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count = (int) dataSnapshot.getChildrenCount();
                if (1 == count) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        YemekReg yemekReg = child.getValue(YemekReg.class);
                        holder.tvYemek.setText(yemekReg.getItemName());
                        int fiyat = Integer.parseInt(yemekReg.getFiyat());
                        int adet = Integer.parseInt(holder.tvAdedi.getText().toString());
                        holder.tvTutar.setText(String.valueOf(adet * fiyat));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Adaer", databaseError.getMessage());
            }
        });
        holder.tvYemek.setText("");
        holder.tvAdedi.setText(String.valueOf(adedi[position]));
        holder.tvDurum.setText(durumu[position]);
        holder.tvPuan.setText(String.valueOf(puan[position]));
        holder.tvTeslimatSekli.setText(teslimatsekli[position] == 0 ? "Gel Al" : "Eve Teslim");
        holder.tvZaman.setText(zaman[position]);
    }

    @Override
    public int getItemCount() {
        return Id.length;
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Context context;
        TextView tvSiparisNo, tvYemek, tvUye, tvAdedi, tvDurum, tvPuan, tvTeslimatSekli, tvZaman, tvTutar;
        String[] Id, asci, durumu, siparisId, yemekId, uye, zaman;
        int[] adedi, puan, teslimatsekli;
        String sna, spa, callingActivity;
        int puanChoice = -1;

        public ImageViewHolder(@NonNull View itemView, Context context, String[] Id, String[] asci, String[] durumu, String[] siparisId, String[] yemekId, int[] adedi, int[] puan, int[] teslimatsekli, String sna, String spa, String callingActivity, String[] uye, String[] zaman) {
            super(itemView);
            this.context = context;
            tvSiparisNo = itemView.findViewById(R.id.tvSiparisNo);
            tvYemek = itemView.findViewById(R.id.tvYemek);
            tvAdedi = itemView.findViewById(R.id.tvAdedi);
            tvDurum = itemView.findViewById(R.id.tvDurumu);
            tvPuan = itemView.findViewById(R.id.tvPuan);
            tvUye = itemView.findViewById(R.id.tvUye);
            tvTeslimatSekli = itemView.findViewById(R.id.tvUyeSiparisTeslimanSekli);
            tvZaman = itemView.findViewById(R.id.tvZaman);
            tvTutar = itemView.findViewById(R.id.tvTutar);

            itemView.setOnClickListener(this);

            this.Id = Id;
            this.asci = asci;
            this.durumu = durumu;
            this.siparisId = siparisId;
            this.yemekId = yemekId;
            this.uye = uye;
            this.adedi = adedi;
            this.puan = puan;
            this.zaman = zaman;
            this.teslimatsekli = teslimatsekli;
            this.sna = sna;
            this.spa = spa;
            this.callingActivity = callingActivity;
        }

        @Override
        public void onClick(View v) {
            puanChoice = -1;
            showDialog(v.getContext(), "Puan verin.", new String[]{"Tamam"}, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(final DialogInterface dialog, int which) {
                    if (-1 == puanChoice) {
                        return;
                    }
                    int pos = getLayoutPosition();
                    Query query = FirebaseDatabase.getInstance().getReference().child("siparislerReg").orderByChild("siparisId").equalTo(siparisId[pos]);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            SiparisReg siparisReg = null;
                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                siparisReg = child.getValue(SiparisReg.class);
                                siparisReg.puan = puanChoice;
                            }
                            if (null != siparisReg) {
                                FirebaseDatabase.getInstance().getReference().child("siparislerReg").child(siparisId[pos]).setValue(siparisReg, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                        Toast.makeText(MainActivity.getAppContext(), "Puan eklendi.", Toast.LENGTH_LONG).show();
                                        tvPuan.setText(String.valueOf(puanChoice));
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.d("Adaer", databaseError.getMessage());
                        }
                    });
                }
            });
        }

        public void showDialog(Context context, String title, String[] btnText, DialogInterface.OnClickListener listener) {
            final CharSequence[] items = {"Çok İyi", "İyi", "Orta", "Kötü", "Çok Kötü"};
            if (listener == null) {
                listener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        paramDialogInterface.dismiss();
                    }
                };
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(title);
            builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    Log.d("Adaer", "items: " + String.valueOf(item));
                    puanChoice = 5 - item;
                }
            });
            builder.setPositiveButton(btnText[0], listener);
            if (btnText.length != 1) {
                builder.setNegativeButton(btnText[1], listener);
            }
            builder.show();
        }
    }
}
