package com.example.ecosort;


import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.ecosort.data.AppDatabase;
import com.example.ecosort.data.RedemptionEntity;
import com.example.ecosort.data.UserEntity;
import com.example.ecosort.utils.SessionManager;
import com.google.android.material.button.MaterialButton;

import java.util.List;
import java.util.concurrent.Executors;

public class RewardsActivity extends AppCompatActivity {

    SessionManager session;
    TextView tvRewardPoints;
    TextView tvNoHistory;
    LinearLayout rewardsContainer;
    LinearLayout historyContainer;

    int currentPoints = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewards);

        session = new SessionManager(this);
        tvRewardPoints = findViewById(R.id.tvRewardPoints);
        tvNoHistory = findViewById(R.id.tvNoHistory);
        rewardsContainer = findViewById(R.id.rewardsContainer);
        historyContainer = findViewById(R.id.historyContainer);

        buildRewardCards();
        refresh();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    private void refresh() {
        Executors.newSingleThreadExecutor().execute(() -> {
            AppDatabase db = AppDatabase.getInstance(getApplicationContext());
            UserEntity user = db.userDao().findById(session.getUserId());
            List<RedemptionEntity> history = db.redemptionDao().historyForUser(session.getUserId());

            runOnUiThread(() -> {
                if (user != null) {
                    currentPoints = user.totalPoints;
                    tvRewardPoints.setText(String.valueOf(currentPoints));
                }
                updateRedeemButtons();
                renderHistory(history);
            });
        });
    }

    private void buildRewardCards() {
        for (Reward reward : Reward.catalog()) {
            rewardsContainer.addView(buildRewardCard(reward));
        }
    }

    private CardView buildRewardCard(Reward reward) {
        CardView card = new CardView(this);
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        cardParams.setMargins(0, 0, 0, dp(10));
        card.setLayoutParams(cardParams);
        card.setRadius(dp(14));
        card.setCardElevation(dp(2));
        card.setCardBackgroundColor(Color.WHITE);
        card.setContentPadding(dp(14), dp(14), dp(14), dp(14));
        card.setTag(reward);

        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER_VERTICAL);

        TextView iconView = new TextView(this);
        iconView.setText(reward.icon);
        iconView.setTextSize(24);
        LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(
                dp(44), LinearLayout.LayoutParams.WRAP_CONTENT);
        iconView.setLayoutParams(iconParams);

        LinearLayout textCol = new LinearLayout(this);
        textCol.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams textColParams = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        textColParams.setMarginStart(dp(6));
        textColParams.setMarginEnd(dp(10));
        textCol.setLayoutParams(textColParams);

        TextView titleView = new TextView(this);
        titleView.setText(reward.title);
        titleView.setTextSize(15);
        titleView.setTypeface(null, Typeface.BOLD);
        titleView.setTextColor(Color.parseColor("#212121"));

        TextView descView = new TextView(this);
        descView.setText(reward.description);
        descView.setTextSize(12);
        descView.setTextColor(Color.parseColor("#757575"));

        TextView costView = new TextView(this);
        costView.setText(reward.cost + " pts");
        costView.setTextSize(12);
        costView.setTypeface(null, Typeface.BOLD);
        costView.setTextColor(Color.parseColor("#2E7D32"));
        LinearLayout.LayoutParams costParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        costParams.topMargin = dp(2);
        costView.setLayoutParams(costParams);

        textCol.addView(titleView);
        textCol.addView(descView);
        textCol.addView(costView);

        MaterialButton redeemButton = new MaterialButton(this);
        redeemButton.setText("Redeem");
        redeemButton.setTextSize(12);
        redeemButton.setAllCaps(false);
        redeemButton.setCornerRadius(dp(10));
        redeemButton.setTag("redeemBtn");
        redeemButton.setOnClickListener(v -> redeem(reward, redeemButton));

        row.addView(iconView);
        row.addView(textCol);
        row.addView(redeemButton);
        card.addView(row);

        return card;
    }

    private void updateRedeemButtons() {
        for (int i = 0; i < rewardsContainer.getChildCount(); i++) {
            CardView card = (CardView) rewardsContainer.getChildAt(i);
            Reward reward = (Reward) card.getTag();
            MaterialButton button = findRedeemButton(card);
            if (button == null) continue;
            boolean canAfford = currentPoints >= reward.cost;
            button.setEnabled(canAfford);
            button.setBackgroundTintList(android.content.res.ColorStateList.valueOf(
                    canAfford ? Color.parseColor("#4CAF50") : Color.parseColor("#BDBDBD")));
        }
    }

    private MaterialButton findRedeemButton(CardView card) {
        LinearLayout row = (LinearLayout) card.getChildAt(0);
        for (int i = 0; i < row.getChildCount(); i++) {
            if ("redeemBtn".equals(row.getChildAt(i).getTag())) {
                return (MaterialButton) row.getChildAt(i);
            }
        }
        return null;
    }

    private void redeem(Reward reward, MaterialButton button) {
        if (currentPoints < reward.cost) {
            Toast.makeText(this, "Not enough points yet", Toast.LENGTH_SHORT).show();
            return;
        }
        button.setEnabled(false);

        Executors.newSingleThreadExecutor().execute(() -> {
            AppDatabase db = AppDatabase.getInstance(getApplicationContext());
            UserEntity user = db.userDao().findById(session.getUserId());
            if (user == null || user.totalPoints < reward.cost) {
                runOnUiThread(() -> {
                    Toast.makeText(this, "Not enough points yet", Toast.LENGTH_SHORT).show();
                    updateRedeemButtons();
                });
                return;
            }

            user.totalPoints -= reward.cost;
            db.userDao().update(user);

            RedemptionEntity redemption = new RedemptionEntity();
            redemption.userId = user.id;
            redemption.rewardTitle = reward.icon + " " + reward.title;
            redemption.pointsSpent = reward.cost;
            redemption.timestamp = System.currentTimeMillis();
            db.redemptionDao().insert(redemption);

            runOnUiThread(() -> {
                Toast.makeText(this, "Redeemed: " + reward.title, Toast.LENGTH_SHORT).show();
                refresh();
            });
        });
    }

    private void renderHistory(List<RedemptionEntity> history) {
        historyContainer.removeAllViews();
        tvNoHistory.setVisibility(history.isEmpty() ? android.view.View.VISIBLE : android.view.View.GONE);

        for (RedemptionEntity redemption : history) {
            LinearLayout row = new LinearLayout(this);
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setGravity(Gravity.CENTER_VERTICAL);
            LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            rowParams.setMargins(0, 0, 0, dp(10));
            row.setLayoutParams(rowParams);

            TextView titleView = new TextView(this);
            titleView.setText(redemption.rewardTitle);
            titleView.setTextSize(14);
            titleView.setTextColor(Color.parseColor("#212121"));
            LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
            titleView.setLayoutParams(titleParams);

            TextView metaView = new TextView(this);
            String date = DateFormat.format("MMM d", redemption.timestamp).toString();
            metaView.setText("-" + redemption.pointsSpent + " pts · " + date);
            metaView.setTextSize(12);
            metaView.setTextColor(Color.parseColor("#757575"));

            row.addView(titleView);
            row.addView(metaView);
            historyContainer.addView(row);
        }
    }

    private int dp(int value) {
        float density = getResources().getDisplayMetrics().density;
        return (int) (value * density);
    }
}