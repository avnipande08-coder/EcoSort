package com.example.ecosort;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.ecosort.data.*;
import com.example.ecosort.utils.SessionManager;
import com.example.ecosort.utils.StreakCalculator;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class DashboardActivity_streak extends AppCompatActivity {
    SessionManager session;
    TextView tvPoints, tvWelcome, tvStreak;
    Spinner spinnerCategory;
    ViewPager2 ecoTipsPager;
    LinearLayout dotsIndicator;
    Handler sliderHandler = new Handler(Looper.getMainLooper());
    Runnable sliderRunnable;

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
        setupEcoTipsSlider();
        CardView cardUserProfile = findViewById(R.id.cardUserProfile);
        cardUserProfile.setOnClickListener(v -> startActivity(new Intent(this, ProfileActivity.class)));

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
    private void setupEcoTipsSlider() {
        ecoTipsPager = findViewById(R.id.ecoTipsPager);
        dotsIndicator = findViewById(R.id.dotsIndicator);

        List<EcoTipsAdapter.Tip> tips = new ArrayList<>();
        tips.add(new EcoTipsAdapter.Tip(R.drawable.img_tip_recycle, "Rinse before you recycle"));
        tips.add(new EcoTipsAdapter.Tip(R.drawable.img_tip_compost, "Compost food scraps daily"));
        tips.add(new EcoTipsAdapter.Tip(R.drawable.img_tip_ewaste, "Never bin e-waste"));
        tips.add(new EcoTipsAdapter.Tip(R.drawable.img_tip_water, "Save water while cleaning"));

        ecoTipsPager.setAdapter(new EcoTipsAdapter(tips));
        setupDots(tips.size());

        ecoTipsPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                updateDots(position);
            }
        });

        sliderRunnable = () -> {
            int next = (ecoTipsPager.getCurrentItem() + 1) % tips.size();
            ecoTipsPager.setCurrentItem(next, true);
            sliderHandler.postDelayed(sliderRunnable, 3000);
        };
        sliderHandler.postDelayed(sliderRunnable, 3000);
    }

    private void setupDots(int count) {
        dotsIndicator.removeAllViews();
        for (int i = 0; i < count; i++) {
            TextView dot = new TextView(this);
            dot.setText("●");
            dot.setTextSize(12);
            dot.setTextColor(i == 0
                    ? getResources().getColor(R.color.green_primary)
                    : getResources().getColor(R.color.green_light));
            dot.setPadding(6, 0, 6, 0);
            dotsIndicator.addView(dot);
        }
    }

    private void updateDots(int activePosition) {
        for (int i = 0; i < dotsIndicator.getChildCount(); i++) {
            TextView dot = (TextView) dotsIndicator.getChildAt(i);
            dot.setTextColor(i == activePosition
                    ? getResources().getColor(R.color.green_primary)
                    : getResources().getColor(R.color.green_light));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(sliderRunnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshPoints();
        refreshStreak();
        if (sliderRunnable != null) {
            sliderHandler.postDelayed(sliderRunnable, 3000);
        }
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