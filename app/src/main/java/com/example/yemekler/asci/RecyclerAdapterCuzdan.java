package com.example.yemekler.asci;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yemekler.R;

import java.util.ArrayList;

public class RecyclerAdapterCuzdan extends RecyclerView.Adapter<RecyclerAdapterCuzdan.ImageViewHolder> {
    private final Context context;
    String sna;
    ArrayList<ArrayList<String>> yemeksatisler;

    public RecyclerAdapterCuzdan(Context context, String sna, ArrayList<ArrayList<String>> yemeksatisler) {
        this.context = context;
        this.sna = sna;
        this.yemeksatisler = yemeksatisler;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cuzdan_list_item, parent, false);
        return new ImageViewHolder(view, context, sna, yemeksatisler);
    }

    @Override
    public void onBindViewHolder(@NonNull final ImageViewHolder holder, int position) {
        Log.d("Adaer", "Position:" + position);
        ArrayList<String> yemeksatis = yemeksatisler.get(position);
        holder.tvCuzdanYemekAdi.setText(yemeksatis.get(0));
        holder.tvCuzdanYemekFiyati.setText(yemeksatis.get(1));
        holder.tvCuzdanToplamAdet.setText(yemeksatis.get(2));
        float tutar = Float.valueOf(yemeksatis.get(1)) * Float.valueOf(yemeksatis.get(2));
        holder.tvCuzdanToplamTutar.setText(String.valueOf(tutar));
    }

    @Override
    public int getItemCount() {
        return yemeksatisler.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Context context;
        TextView tvCuzdanYemekAdi, tvCuzdanYemekFiyati, tvCuzdanToplamAdet, tvCuzdanToplamTutar;
        String sna;
        ArrayList<ArrayList<String>> yemeksatisler;

        public ImageViewHolder(@NonNull View itemView, Context context, String sna, ArrayList<ArrayList<String>> yemeksatisler) {
            super(itemView);
            this.context = context;
            tvCuzdanYemekAdi = itemView.findViewById(R.id.tvCuzdanYemekAdi);
            tvCuzdanYemekFiyati = itemView.findViewById(R.id.tvCuzdanYemekFiyati);
            tvCuzdanToplamAdet = itemView.findViewById(R.id.tvCuzdanToplamAdet);
            tvCuzdanToplamTutar = itemView.findViewById(R.id.tvCuzdanToplamTutar);

            itemView.setOnClickListener(this);

            this.sna = sna;
            this.yemeksatisler = yemeksatisler;
        }

        @Override
        public void onClick(View v) {

        }
    }
}
