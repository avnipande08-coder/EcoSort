package com.example.ecosort;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class BusinessDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_detail);

        TextView emojiView = findViewById(R.id.detailEmojiView);
        TextView titleView = findViewById(R.id.detailTitleView);
        TextView categoryView = findViewById(R.id.detailCategoryView);
        TextView companyView = findViewById(R.id.detailCompanyTextView);
        TextView ideaView = findViewById(R.id.detailIdeaTextView);
        TextView howToStartView = findViewById(R.id.detailHowToStartTextView);
        TextView investmentView = findViewById(R.id.detailInvestmentTextView);
        TextView earningView = findViewById(R.id.detailEarningTextView);
        TextView difficultyView = findViewById(R.id.detailDifficultyTextView);

        String emoji = getIntent().getStringExtra("emoji");
        String category = getIntent().getStringExtra("category");
        String companyExample = getIntent().getStringExtra("companyExample");
        String idea = getIntent().getStringExtra("idea");
        String title = getIntent().getStringExtra("title");
        String howToStart = getIntent().getStringExtra("howToStart");
        String investment = getIntent().getStringExtra("investment");
        String earningPotential = getIntent().getStringExtra("earningPotential");
        String difficulty = getIntent().getStringExtra("difficulty");

        emojiView.setText(emoji);
        titleView.setText(title);
        categoryView.setText(category + " Waste");
        companyView.setText(companyExample);
        ideaView.setText(idea);
        howToStartView.setText(howToStart);
        investmentView.setText(investment);
        earningView.setText(earningPotential);
        difficultyView.setText(difficulty);
    }
}