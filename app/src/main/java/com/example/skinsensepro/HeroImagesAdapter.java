package com.example.skinsensepro;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.skinsensepro.R;

import java.util.List;

public class HeroImagesAdapter extends RecyclerView.Adapter<HeroImagesAdapter.HeroImageViewHolder> {

    private Context context;
    private List<String> imageUrls;

    public HeroImagesAdapter(Context context, List<String> imageUrls) {
        this.context = context;
        this.imageUrls = imageUrls;
    }

    @NonNull
    @Override
    public HeroImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_hero_image, parent, false);
        return new HeroImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HeroImageViewHolder holder, int position) {
        String imageUrl = imageUrls.get(position);
        Glide.with(context).load(imageUrl).into(holder.heroImageView);
    }

    @Override
    public int getItemCount() {
        return imageUrls.size();
    }

    public static class HeroImageViewHolder extends RecyclerView.ViewHolder {
        ImageView heroImageView;

        public HeroImageViewHolder(@NonNull View itemView) {
            super(itemView);
            heroImageView = itemView.findViewById(R.id.heroImageView);
        }
    }
}
