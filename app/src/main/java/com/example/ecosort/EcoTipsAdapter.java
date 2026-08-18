package com.example.ecosort;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EcoTipsAdapter extends RecyclerView.Adapter<EcoTipsAdapter.SlideViewHolder> {

    public static class Tip {
        public final int imageRes;
        public final String caption;

        public Tip(int imageRes, String caption) {
            this.imageRes = imageRes;
            this.caption = caption;
        }
    }

    private final List<Tip> tips;

    public EcoTipsAdapter(List<Tip> tips) {
        this.tips = tips;
    }

    @NonNull
    @Override
    public SlideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_eco_tip_slide, parent, false);
        return new SlideViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SlideViewHolder holder, int position) {
        Tip tip = tips.get(position);
        holder.ivSlide.setImageResource(tip.imageRes);
        holder.tvCaption.setText(tip.caption);
    }

    @Override
    public int getItemCount() {
        return tips.size();
    }

    static class SlideViewHolder extends RecyclerView.ViewHolder {
        ImageView ivSlide;
        TextView tvCaption;

        SlideViewHolder(View itemView) {
            super(itemView);
            ivSlide = itemView.findViewById(R.id.ivSlide);
            tvCaption = itemView.findViewById(R.id.tvSlideCaption);
        }
    }
}