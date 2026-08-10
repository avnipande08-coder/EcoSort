package com.example.ecosort;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class BusinessIdeasActivity extends AppCompatActivity {

    static class BusinessIdea {
        String emoji, category, companyExample, idea;
        BusinessIdea(String emoji, String category, String companyExample, String idea) {
            this.emoji = emoji;
            this.category = category;
            this.companyExample = companyExample;
            this.idea = idea;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_ideas);

        LinearLayout container = findViewById(R.id.businessCardsContainer);

        BusinessIdea[] ideas = new BusinessIdea[]{
                new BusinessIdea("🛍️", "Plastic",
                        "Ecoware / Banyan Nation turn plastic waste into raw material for new products.",
                        "Startup idea: collect plastic bottles from your neighborhood and sell to local recyclers who pay per kg."),

                new BusinessIdea("📰", "Paper",
                        "Companies like Paperman recycle waste paper into notebooks and stationery.",
                        "Startup idea: make handmade recycled-paper greeting cards or notebooks to sell at school fairs."),

                new BusinessIdea("🍌", "Organic",
                        "Daily Dump converts kitchen waste into compost for home gardens.",
                        "Startup idea: sell homemade compost to local nurseries or gardeners using kitchen scraps."),

                new BusinessIdea("🔋", "E-Waste",
                        "Attero Recycling extracts precious metals from old electronics safely.",
                        "Startup idea: run a neighborhood e-waste collection drive and partner with a certified recycler for a share of proceeds."),

                new BusinessIdea("🥫", "Metal",
                        "Scrap metal dealers melt and resell metal for construction and manufacturing.",
                        "Startup idea: collect and sort metal cans/scraps, then sell in bulk to scrap dealers for a margin."),

                new BusinessIdea("🍾", "Glass",
                        "Glass recyclers crush and melt bottles into new glassware or construction tiles.",
                        "Startup idea: turn used glass bottles into painted lamps, vases, or planters and sell them online."),

                new BusinessIdea("🗑️", "General Waste",
                        "Waste management companies convert non-recyclable waste into energy (waste-to-energy plants).",
                        "Startup idea: partner with local municipalities on awareness campaigns about proper waste segregation for a fee."),
        };

        for (BusinessIdea item : ideas) {
            container.addView(buildCard(item));
        }
    }

    private CardView buildCard(BusinessIdea item) {
        CardView card = new CardView(this);
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        cardParams.setMargins(0, 0, 0, dp(14));
        card.setLayoutParams(cardParams);
        card.setRadius(dp(16));
        card.setCardElevation(dp(3));
        card.setCardBackgroundColor(Color.WHITE);
        card.setContentPadding(dp(16), dp(16), dp(16), dp(16));

        LinearLayout inner = new LinearLayout(this);
        inner.setOrientation(LinearLayout.VERTICAL);

        LinearLayout headerRow = new LinearLayout(this);
        headerRow.setOrientation(LinearLayout.HORIZONTAL);
        headerRow.setGravity(Gravity.CENTER_VERTICAL);

        TextView emojiView = new TextView(this);
        emojiView.setText(item.emoji);
        emojiView.setTextSize(26);

        TextView categoryView = new TextView(this);
        categoryView.setText(item.category);
        categoryView.setTextSize(18);
        categoryView.setTypeface(null, android.graphics.Typeface.BOLD);
        categoryView.setTextColor(Color.parseColor("#212121"));
        LinearLayout.LayoutParams catParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        catParams.setMargins(dp(10), 0, 0, 0);
        categoryView.setLayoutParams(catParams);

        headerRow.addView(emojiView);
        headerRow.addView(categoryView);

        TextView companyView = new TextView(this);
        companyView.setText("🏢 " + item.companyExample);
        companyView.setTextSize(13);
        companyView.setTextColor(Color.parseColor("#2E7D32"));
        LinearLayout.LayoutParams companyParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        companyParams.setMargins(0, dp(8), 0, 0);
        companyView.setLayoutParams(companyParams);

        TextView ideaView = new TextView(this);
        ideaView.setText("💡 " + item.idea);
        ideaView.setTextSize(13);
        ideaView.setTextColor(Color.parseColor("#424242"));
        LinearLayout.LayoutParams ideaParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ideaParams.setMargins(0, dp(6), 0, 0);
        ideaView.setLayoutParams(ideaParams);

        inner.addView(headerRow);
        inner.addView(companyView);
        inner.addView(ideaView);
        card.addView(inner);

        return card;
    }

    private int dp(int value) {
        float density = getResources().getDisplayMetrics().density;
        return (int) (value * density);
    }
}