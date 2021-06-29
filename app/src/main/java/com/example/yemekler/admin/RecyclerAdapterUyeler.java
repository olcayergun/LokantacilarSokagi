package com.example.yemekler.admin;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yemekler.R;
import com.example.yemekler.uye.UyeReg;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RecyclerAdapterUyeler extends RecyclerView.Adapter<RecyclerAdapterUyeler.ImageViewHolder> {
    String[] Id, staffname, password, telno;
    String sna, spa, callingActivity;
    private final Context context;

    public RecyclerAdapterUyeler(String[] Id, String[] staffname, String[] password, String[] telno, Context context, String sna, String spa, String callingActivity) {
        this.Id = Id;
        this.staffname = staffname;
        this.password = password;
        this.telno = telno;
        this.context = context;
        this.sna = sna;
        this.spa = spa;
        this.callingActivity = callingActivity;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.uyeler_list_item, parent, false);
        return new ImageViewHolder(view, context, Id, staffname, password, telno, sna, spa, callingActivity);
    }

    @Override
    public void onBindViewHolder(@NonNull final ImageViewHolder holder, int position) {
        holder.tvId.setText(Id[position]);
        holder.tvStaffName.setText(staffname[position]);
        holder.tvPassword.setText(password[position]);
        holder.tvTelno.setText(telno[position]);
    }

    @Override
    public int getItemCount() {
        return Id.length;
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Context context;
        TextView tvId, tvStaffName, tvPassword, tvTelno;
        Button btnSil;
        String[] Id, staffname, password, telno;
        String sna, spa, callingActivity;

        public ImageViewHolder(@NonNull final View itemView, final Context context, String[] Id, final String[] staffname, final String[] password, final String[] telno, final String sna, final String spa, final String callingActivity) {
            super(itemView);
            this.context = context;
            tvId = itemView.findViewById(R.id.tvUyeId);
            tvStaffName = itemView.findViewById(R.id.tvStaffName);
            tvPassword = itemView.findViewById(R.id.tvPassword);
            tvTelno = itemView.findViewById(R.id.tvTelno);
            btnSil = itemView.findViewById(R.id.btnUyeSil);
            btnSil.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String Id = tvId.getText().toString();
                    Log.d("OLcay", "Sil düğmesine basıldı." + Id);

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("memberReg").child(Id);
                    reference.removeValue();

                    Toast.makeText(context, tvStaffName.getText().toString() + " adlı üye silindi.", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(context, UyeReg.class);
                    intent.putExtra("NAME", sna);
                    intent.putExtra("PASSWORD", spa);
                    context.startActivity(intent);
                }
            });

            itemView.setOnClickListener(this);

            this.Id = Id;
            this.staffname = staffname;
            this.password = password;
            this.telno = telno;
            this.sna = sna;
            this.spa = spa;
            this.callingActivity = callingActivity;
        }

        @Override
        public void onClick(View v) {

        }

    }
}
