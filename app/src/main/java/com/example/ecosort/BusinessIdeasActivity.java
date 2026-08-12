package com.example.ecosort;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class BusinessIdeasActivity extends AppCompatActivity {

    static class BusinessIdea {
        String emoji, category, companyExample, idea;
        String title, howToStart, investment, earningPotential, difficulty;

        BusinessIdea(String emoji, String category, String companyExample, String idea,
                     String title, String howToStart, String investment,
                     String earningPotential, String difficulty) {
            this.emoji = emoji;
            this.category = category;
            this.companyExample = companyExample;
            this.idea = idea;
            this.title = title;
            this.howToStart = howToStart;
            this.investment = investment;
            this.earningPotential = earningPotential;
            this.difficulty = difficulty;
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
                        "Startup idea: collect plastic bottles from your neighborhood and sell to local recyclers who pay per kg.",
                        "Plastic Bottle Collection Drive",
                        "1. Get bins and bags for collection.\n2. Partner with 2-3 local recyclers to fix a rate per kg.\n3. Set up drop points in your society or school.\n4. Weigh and sell collected plastic weekly.",
                        "Low (₹1,000–3,000 for bins, bags, weighing scale)",
                        "₹2,000–8,000/month depending on volume collected",
                        "Easy"),

                new BusinessIdea("📰", "Paper",
                        "Companies like Paperman recycle waste paper into notebooks and stationery.",
                        "Startup idea: make handmade recycled-paper greeting cards or notebooks to sell at school fairs.",
                        "Recycled Paper Stationery",
                        "1. Collect waste paper (newspapers, old notebooks).\n2. Learn basic paper pulping/recycling technique.\n3. Make notebooks, cards, or gift bags.\n4. Sell at school fairs, online, or local stores.",
                        "Low (₹500–2,000 for basic tools and material)",
                        "₹1,500–6,000/month depending on sales volume",
                        "Easy"),

                new BusinessIdea("🍌", "Organic",
                        "Daily Dump converts kitchen waste into compost for home gardens.",
                        "Startup idea: sell homemade compost to local nurseries or gardeners using kitchen scraps.",
                        "Kitchen-Waste Composting",
                        "1. Set up a compost bin at home or society level.\n2. Collect kitchen scraps from a few households.\n3. Let it compost for 4-6 weeks.\n4. Package and sell to nurseries or gardeners.",
                        "Low-Medium (₹1,500–5,000 for compost bins)",
                        "₹2,000–7,000/month",
                        "Medium"),

                new BusinessIdea("🔋", "E-Waste",
                        "Attero Recycling extracts precious metals from old electronics safely.",
                        "Startup idea: run a neighborhood e-waste collection drive and partner with a certified recycler for a share of proceeds.",
                        "E-Waste Collection Partnership",
                        "1. Identify a certified e-waste recycler to partner with.\n2. Organize a collection drive (phones, cables, batteries).\n3. Hand over collected items to the recycler.\n4. Earn a commission or share of proceeds.",
                        "Low (mainly promotion/collection costs, ₹1,000–3,000)",
                        "₹3,000–10,000 per drive",
                        "Medium"),

                new BusinessIdea("🥫", "Metal",
                        "Scrap metal dealers melt and resell metal for construction and manufacturing.",
                        "Startup idea: collect and sort metal cans/scraps, then sell in bulk to scrap dealers for a margin.",
                        "Scrap Metal Sorting & Resale",
                        "1. Collect metal scraps/cans from households or local shops.\n2. Sort by metal type (aluminium, iron, copper).\n3. Store safely until you have bulk quantity.\n4. Sell in bulk to scrap dealers for better rates.",
                        "Medium (₹2,000–6,000 for storage and transport)",
                        "₹3,000–12,000/month",
                        "Medium"),

                new BusinessIdea("🍾", "Glass",
                        "Glass recyclers crush and melt bottles into new glassware or construction tiles.",
                        "Startup idea: turn used glass bottles into painted lamps, vases, or planters and sell them online.",
                        "Upcycled Glass Decor",
                        "1. Collect clean used glass bottles.\n2. Learn simple painting/decoupage techniques.\n3. Turn bottles into lamps, vases, or planters.\n4. Sell on Instagram, Etsy, or local craft fairs.",
                        "Low (₹500–2,500 for paints and tools)",
                        "₹1,500–5,000/month",
                        "Easy"),

                new BusinessIdea("🗑️", "General Waste",
                        "Waste management companies convert non-recyclable waste into energy (waste-to-energy plants).",
                        "Startup idea: partner with local municipalities on awareness campaigns about proper waste segregation for a fee.",
                        "Waste Segregation Awareness Drives",
                        "1. Prepare simple, engaging material on waste segregation.\n2. Approach local municipal bodies, schools, or RWAs.\n3. Conduct workshops or awareness campaigns.\n4. Charge a fee per session or get sponsorship.",
                        "Low (₹1,000–3,000 for printed material)",
                        "₹2,000–8,000 per campaign",
                        "Medium"),
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

        // Make the card behave like a button
        card.setClickable(true);
        card.setFocusable(true);
        TypedValue outValue = new TypedValue();
        getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
        card.setForeground(getResources().getDrawable(outValue.resourceId));
        card.setOnClickListener(v -> openDetails(item));

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

        TextView tapHintView = new TextView(this);
        tapHintView.setText("Tap for details ›");
        tapHintView.setTextSize(12);
        tapHintView.setTypeface(null, android.graphics.Typeface.BOLD);
        tapHintView.setTextColor(Color.parseColor("#4CAF50"));
        tapHintView.setGravity(Gravity.END);
        LinearLayout.LayoutParams hintParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        hintParams.setMargins(0, dp(10), 0, 0);
        tapHintView.setLayoutParams(hintParams);

        inner.addView(headerRow);
        inner.addView(companyView);
        inner.addView(ideaView);
        inner.addView(tapHintView);
        card.addView(inner);

        return card;
    }

    private void openDetails(BusinessIdea item) {
        Intent intent = new Intent(this, BusinessDetailActivity.class);
        intent.putExtra("emoji", item.emoji);
        intent.putExtra("category", item.category);
        intent.putExtra("companyExample", item.companyExample);
        intent.putExtra("idea", item.idea);
        intent.putExtra("title", item.title);
        intent.putExtra("howToStart", item.howToStart);
        intent.putExtra("investment", item.investment);
        intent.putExtra("earningPotential", item.earningPotential);
        intent.putExtra("difficulty", item.difficulty);
        startActivity(intent);
    }

    private int dp(int value) {
        float density = getResources().getDisplayMetrics().density;
        return (int) (value * density);
    }
}