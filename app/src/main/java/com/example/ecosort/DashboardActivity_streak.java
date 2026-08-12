package com.example.ecosort;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ecosort.data.*;
import com.example.ecosort.utils.SessionManager;
import com.example.ecosort.utils.StreakCalculator;
import com.google.android.material.button.MaterialButton;

import java.util.concurrent.Executors;

public class DashboardActivity_streak extends AppCompatActivity {
    SessionManager session;
    TextView tvPoints, tvWelcome, tvStreak;
    Spinner spinnerCategory;

    String[] categories = {"plastic", "paper", "organic", "ewaste", "metal", "glass", "general"};
    int[] pointsFor = {10, 5, 8, 15, 12, 10, 2};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        session = new SessionManager(this);

        tvWelcome = findViewById(R.id.tvWelcome);
        tvPoints = findViewById(R.id.tvPoints);
        tvStreak = findViewById(R.id.tvStreak);
        TextView tvAvatarInitial = findViewById(R.id.tvAvatarInitial);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        MaterialButton btnBrowseWaste = findViewById(R.id.btnBrowseWaste);
        MaterialButton btnSortingGame = findViewById(R.id.btnSortingGame);
        MaterialButton btnQuiz = findViewById(R.id.btnQuiz);
        MaterialButton btnBusinessIdeas = findViewById(R.id.btnBusinessIdeas);
        MaterialButton btnLog = findViewById(R.id.btnLogWaste);
        MaterialButton btnRewards = findViewById(R.id.btnRewards);
        MaterialButton btnLeaderboard = findViewById(R.id.btnLeaderboard);
        MaterialButton btnLogout = findViewById(R.id.btnLogout);

        String username = session.getUsername();
        tvWelcome.setText("Hi, " + username + " 👋");
        if (username != null && !username.isEmpty()) {
            tvAvatarInitial.setText(username.substring(0, 1).toUpperCase());
        }
        spinnerCategory.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categories));

        refreshPoints();
        refreshStreak();

        btnBrowseWaste.setOnClickListener(v -> startActivity(new Intent(this, MainActivity.class)));
        btnSortingGame.setOnClickListener(v -> startActivity(new Intent(this, SortingGameActivity.class)));
        btnQuiz.setOnClickListener(v -> startActivity(new Intent(this, QuizActivity.class)));
        btnBusinessIdeas.setOnClickListener(v -> startActivity(new Intent(this, BusinessIdeasActivity.class)));
        btnLog.setOnClickListener(v -> logWaste());
        btnRewards.setOnClickListener(v -> startActivity(new Intent(this, RewardsActivity.class)));
        btnLeaderboard.setOnClickListener(v -> startActivity(new Intent(this, LeaderboardActivity.class)));
        btnLogout.setOnClickListener(v -> {
            session.logout();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshPoints();
        refreshStreak();
    }

    private void refreshPoints() {
        Executors.newSingleThreadExecutor().execute(() -> {
            AppDatabase db = AppDatabase.getInstance(getApplicationContext());
            UserEntity user = db.userDao().findById(session.getUserId());
            if (user != null) {
                runOnUiThread(() -> tvPoints.setText(String.valueOf(user.totalPoints)));
            }
        });
    }

    private void refreshStreak() {
        Executors.newSingleThreadExecutor().execute(() -> {
            AppDatabase db = AppDatabase.getInstance(getApplicationContext());
            int streak = StreakCalculator.computeCurrentStreak(
                    db.wasteLogDao().historyForUser(session.getUserId()));
            runOnUiThread(() -> {
                if (streak <= 0) {
                    tvStreak.setText("🔥 No streak yet — log waste today to start one!");
                } else if (streak == 1) {
                    tvStreak.setText("🔥 1-day streak — keep it going!");
                } else {
                    tvStreak.setText("🔥 " + streak + "-day streak!");
                }
            });
        });
    }

    private void logWaste() {
        int index = spinnerCategory.getSelectedItemPosition();
        String category = categories[index];
        int pts = pointsFor[index];

        Executors.newSingleThreadExecutor().execute(() -> {
            AppDatabase db = AppDatabase.getInstance(getApplicationContext());
            UserEntity user = db.userDao().findById(session.getUserId());
            if (user == null) return;

            WasteLogEntity log = new WasteLogEntity();
            log.userId = user.id;
            log.category = category;
            log.points = pts;
            log.timestamp = System.currentTimeMillis();
            db.wasteLogDao().insert(log);

            user.totalPoints += pts;
            db.userDao().update(user);

            runOnUiThread(() -> tvPoints.setText(String.valueOf(user.totalPoints)));
            refreshStreak();
        });
    }
}