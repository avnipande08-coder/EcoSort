package com.example.ecosort;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.ecosort.data.AppDatabase;
import com.example.ecosort.data.UserEntity;

import java.util.List;
import java.util.concurrent.Executors;

public class LeaderboardActivity extends AppCompatActivity {

    LinearLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ScrollView scrollView = new ScrollView(this);
        scrollView.setBackgroundColor(Color.WHITE);

        container = new LinearLayout(this);
        container.setOrientation(LinearLayout.VERTICAL);
        container.setPadding(dp(16), dp(16), dp(16), dp(16));

        TextView title = new TextView(this);
        title.setText("🏆 Leaderboard");
        title.setTextSize(24);
        title.setTypeface(null, Typeface.BOLD);
        title.setTextColor(Color.parseColor("#2E7D32"));
        LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        titleParams.setMargins(0, 0, 0, dp(16));
        title.setLayoutParams(titleParams);

        container.addView(title);
        scrollView.addView(container);
        setContentView(scrollView);

        Executors.newSingleThreadExecutor().execute(() -> {
            AppDatabase db = AppDatabase.getInstance(getApplicationContext());
            List<UserEntity> users = db.userDao().topUsers();

            runOnUiThread(() -> {
                int rank = 1;
                for (UserEntity u : users) {
                    container.addView(buildRankCard(rank, u.username, u.totalPoints));
                    rank++;
                }
            });
        });
    }

    private CardView buildRankCard(int rank, String username, int points) {
        CardView card = new CardView(this);
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        cardParams.setMargins(0, 0, 0, dp(10));
        card.setLayoutParams(cardParams);
        card.setRadius(dp(14));
        card.setCardElevation(dp(2));
        card.setCardBackgroundColor(rank == 1 ? Color.parseColor("#FFF9C4") : Color.WHITE);
        card.setContentPadding(dp(14), dp(14), dp(14), dp(14));

        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER_VERTICAL);

        TextView rankView = new TextView(this);
        rankView.setText(medalFor(rank));
        rankView.setTextSize(20);
        LinearLayout.LayoutParams rankParams = new LinearLayout.LayoutParams(
                dp(40), LinearLayout.LayoutParams.WRAP_CONTENT);
        rankView.setLayoutParams(rankParams);

        TextView nameView = new TextView(this);
        nameView.setText(username);
        nameView.setTextSize(16);
        nameView.setTypeface(null, Typeface.BOLD);
        nameView.setTextColor(Color.parseColor("#212121"));
        LinearLayout.LayoutParams nameParams = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        nameView.setLayoutParams(nameParams);

        TextView pointsView = new TextView(this);
        pointsView.setText(points + " pts");
        pointsView.setTextSize(15);
        pointsView.setTypeface(null, Typeface.BOLD);
        pointsView.setTextColor(Color.parseColor("#2E7D32"));

        row.addView(rankView);
        row.addView(nameView);
        row.addView(pointsView);
        card.addView(row);

        return card;
    }

    private String medalFor(int rank) {
        if (rank == 1) return "🥇";
        if (rank == 2) return "🥈";
        if (rank == 3) return "🥉";
        return rank + ".";
    }

    private int dp(int value) {
        float density = getResources().getDisplayMetrics().density;
        return (int) (value * density);
    }
}