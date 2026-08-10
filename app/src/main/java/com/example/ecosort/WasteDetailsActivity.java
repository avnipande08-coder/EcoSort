package com.example.ecosort;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class WasteDetailsActivity extends AppCompatActivity {

    ImageView wasteImageView;

    TextView nameTextView,
            tipsTextView,
            binColorTextView,
            categoryTextView,
            recyclableTextView,
            descriptionTextView,
            disposalMethodTextView,
            decompositionTimeTextView;

    Waste extractedWaste;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.waste_details_activity);

        wasteImageView = findViewById(R.id.wasteImageView);

        nameTextView = findViewById(R.id.nameTextView);
        tipsTextView = findViewById(R.id.tipsTextView);
        binColorTextView = findViewById(R.id.binColorTextView);
        categoryTextView = findViewById(R.id.categoryTextView);
        recyclableTextView = findViewById(R.id.recyclableTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);
        disposalMethodTextView = findViewById(R.id.disposalMethodTextView);
        decompositionTimeTextView = findViewById(R.id.decompositionTimeTextView);

        extractAndBindData();
    }

    public void extractAndBindData() {

        Intent i = getIntent();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            extractedWaste = i.getSerializableExtra("waste", Waste.class);
        } else {
            extractedWaste = (Waste) i.getSerializableExtra("waste");
        }

        nameTextView.setText(extractedWaste.getName());
        tipsTextView.setText(extractedWaste.getTips());
        binColorTextView.setText(extractedWaste.getBinColor());
        categoryTextView.setText(extractedWaste.getCategory());

        if (extractedWaste.isRecyclable()) {
            recyclableTextView.setText("Recyclable");
        } else {
            recyclableTextView.setText("Not Recyclable");
        }

        descriptionTextView.setText(extractedWaste.getDescription());
        disposalMethodTextView.setText(extractedWaste.getDisposalMethod());
        decompositionTimeTextView.setText(extractedWaste.getDecompositionTime());

        Glide.with(this)
                .load(extractedWaste.getImage())
                .placeholder(R.drawable.ic_launcher_background)
                .centerCrop()
                .into(wasteImageView);
    }
}