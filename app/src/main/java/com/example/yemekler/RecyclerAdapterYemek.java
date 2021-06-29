package com.example.yemekler;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerAdapterYemek extends RecyclerView.Adapter<RecyclerAdapterYemek.ImageViewHolder> {
    private final String[] yemekIds;
    private final String[] details;
    private final String[] names;
    private final String[] images;
    int[] prices;
    float[] puans;
    String sna, sph, spa, callingActivity;
    private final Context context;

    public RecyclerAdapterYemek(String[] yemekId, String[] images, String[] names, String[] details, int[] prices, float[] puans, Context context, String sna, String sph, String spa, String callingActivity) {
        this.images = images;
        this.yemekIds = yemekId;
        this.details = details;
        this.names = names;
        this.prices = prices;
        this.puans = puans;
        this.context = context;
        this.sna = sna;
        this.sph = sph;
        this.spa = spa;
        this.callingActivity = callingActivity;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ImageViewHolder(view, context, yemekIds, images, names, details, prices, puans, sna, sph, spa, callingActivity);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        if (null == images[position]) {

        } else {
            Bitmap bitmap = decodeFromFirebaseBase64(images[position]);
            holder.img.setImageBitmap(bitmap);
        }
        holder.img_det.setText(details[position]);
        holder.itemname.setText(names[position]);
        holder.img_price.setText("Fiyatı :"+String.valueOf(prices[position]));
        holder.yemekpuan.setText("Puan :" + String.valueOf(puans[position]));
    }

    public Bitmap decodeFromFirebaseBase64(String image)  {
        try {
            byte[] decodedByteArray = android.util.Base64.decode(image, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
        } catch (Exception e) {
            Log.d("Adaer", "", e);
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return images.length;
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView img;
        TextView itemname, img_det, img_price, yemekpuan;
        Context context;
        String[] yemekIds, details, names, images;
        int[] prices;
        float[] puans;
        String sna, sph, spa, callingActivity;

        public ImageViewHolder(@NonNull View itemView, final Context context, String[] yemekIds, String[] images, String[] names, String[] details, int[] prices, float[] puans, final String sna, String sph, final String spa, String callingActivity) {
            super(itemView);
            img = itemView.findViewById(R.id.item_image);
            img_det = itemView.findViewById(R.id.item_details);
            img_price = itemView.findViewById(R.id.item_price);
            yemekpuan = itemView.findViewById(R.id.tv_yemekpuan);
            itemname = itemView.findViewById(R.id.tvItemName);
            itemView.setOnClickListener(this);
            this.context = context;
            this.yemekIds = yemekIds;
            this.images = images;
            this.details = details;
            this.names = names;
            this.prices = prices;
            this.puans = puans;
            this.sna = sna;
            this.sph = sph;
            this.spa = spa;
            this.callingActivity = callingActivity;
        }

        @Override
        public void onClick(View v) {
            Intent intent;
            if (callingActivity.contains("UyeYemekSec")) {
                intent = new Intent(context, YemekGoster.class);
            } else {
                intent = new Intent(context, YemekEkle.class);
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //intent.putExtra("image_id", images[getAdapterPosition()]);
            intent.putExtra("yemekId", yemekIds[getAdapterPosition()]);
            //intent.putExtra("item_details", details[getAdapterPosition()]);
            //intent.putExtra("name", names[getAdapterPosition()]);
            //intent.putExtra("item_price", prices[getAdapterPosition()]);
            intent.putExtra("NAME", sna);
            intent.putExtra("PHONE", sph);
            intent.putExtra("PASSWORD", spa);
            intent.putExtra("CALLING_ACTIVITY", callingActivity);
            context.startActivity(intent);

        }
    }
}
