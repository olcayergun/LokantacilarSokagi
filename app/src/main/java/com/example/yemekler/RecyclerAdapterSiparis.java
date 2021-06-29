package com.example.yemekler;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yemekler.uye.UyeReg;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class RecyclerAdapterSiparis extends RecyclerView.Adapter<RecyclerAdapterSiparis.ImageViewHolder> {
    String[] Id, asci, durumu, siparisId, yemekId, uyeId, zaman;
    int[] adedi, puan, teslimatsekli;
    float[] tutar;
    String sna, sph, spa, callingActivity;
    private final Context context;

    public RecyclerAdapterSiparis(String[] Id, String[] asci, String[] durumu, String[] siparisId, String[] yemekId, int[] adedi, int[] puan, int[] teslimatsekli, float[] tutar, Context context, String sna, String sph, String spa, String callingActivity, String[] uyeId, String[] zaman) {
        this.Id = Id;
        this.asci = asci;
        this.durumu = durumu;
        this.siparisId = siparisId;
        this.uyeId = uyeId;
        this.yemekId = yemekId;
        this.adedi = adedi;
        this.puan = puan;
        this.zaman = zaman;
        this.context = context;
        this.sna = sna;
        this.sph = sph;
        this.spa = spa;
        this.callingActivity = callingActivity;
        this.teslimatsekli = teslimatsekli;
        this.tutar = tutar;
        Log.d("Adaer", "List size:" + Id.length);
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.siparis_list_item, parent, false);
        return new ImageViewHolder(view, context, Id, asci, durumu, siparisId, yemekId, adedi, puan, teslimatsekli, tutar, sna, sph, spa, callingActivity, uyeId, zaman);
    }

    @Override
    public void onBindViewHolder(@NonNull final ImageViewHolder holder, int position) {
        Log.d("Adaer", "Position:" + position);
        holder.tvSiparisNo.setText(Id[position]);
        String uID = uyeId[position];
        if (null != uID) {
            Query queryUye = FirebaseDatabase.getInstance().getReference().child("memberReg").orderByChild("staffname").equalTo(uID);
            queryUye.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int count = (int) dataSnapshot.getChildrenCount();
                    if (1 == count) {
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            UyeReg uyeReg = child.getValue(UyeReg.class);
                            holder.tvUye.setText(uyeReg.getStaffname());
                            if (teslimatsekli[position] == 1) {
                                holder.tvUyeAdres.setText(uyeReg.getAdres());
                            } else {
                                holder.tvUyeAdres.setText("");
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d("Adaer", databaseError.getMessage());
                }
            });
        } else {
            holder.tvUye.setText(R.string.Isimsiz);
        }
        String yID = yemekId[position];
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
        holder.tvZaman.setText(zaman[position]);
        holder.tvTeslimatSekli.setText(teslimatsekli[position] == 0 ? "Gel Al" : "Eve Teslim");
        holder.tvTutar.setText(String.valueOf(tutar[position]));
    }

    @Override
    public int getItemCount() {
        return Id.length;
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Context context;
        TextView tvSiparisNo, tvYemek, tvAdedi, tvDurum, tvPuan, tvTeslimatSekli, tvUye, tvUyeAdres, tvZaman, tvTutar, tvPuanVer;
        String[] Id, asci, durumu, siparisId, yemekId, uyeId, zaman;
        int[] adedi, puan, teslimatsekli;
        float[] tutar;
        String sna, sph, spa, callingActivity;

        public ImageViewHolder(@NonNull View itemView, Context context, String[] Id, String[] asci, String[] durumu, String[] siparisId, String[] yemekId, int[] adedi, int[] puan, int[] teslimatsekli, float[] tutar, String sna, String sph, String spa, String callingActivity, String[] uyeId, String[] zaman) {
            super(itemView);
            this.context = context;
            tvSiparisNo = itemView.findViewById(R.id.tvSiparisNo);
            tvUye = itemView.findViewById(R.id.tvUye);
            tvUyeAdres = itemView.findViewById(R.id.tvUyeAdres);
            tvYemek = itemView.findViewById(R.id.tvYemek);
            tvAdedi = itemView.findViewById(R.id.tvAdedi);
            tvDurum = itemView.findViewById(R.id.tvDurumu);
            tvPuan = itemView.findViewById(R.id.tvPuan);
            tvZaman = itemView.findViewById(R.id.tvZaman);
            tvTeslimatSekli = itemView.findViewById(R.id.tvUyeSiparisTeslimanSekli);
            tvTutar = itemView.findViewById(R.id.tvTutar);
            tvPuanVer = itemView.findViewById(R.id.tvPuanVer);

            itemView.setOnClickListener(this);

            this.Id = Id;
            this.asci = asci;
            this.durumu = durumu;
            this.siparisId = siparisId;
            this.uyeId = uyeId;
            this.yemekId = yemekId;
            this.adedi = adedi;
            this.puan = puan;
            this.teslimatsekli = teslimatsekli;
            this.tutar = tutar;
            this.zaman = zaman;
            this.sna = sna;
            this.sph = sph;
            this.spa = spa;
            this.callingActivity = callingActivity;
            tvPuanVer.setText("");
        }

        @Override
        public void onClick(View v) {

        }

    }
}
