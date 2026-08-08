package com.example.ecosort;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ecosort.data.*;
import com.example.ecosort.utils.SessionManager;
import com.google.android.material.button.MaterialButton;
import java.util.concurrent.Executors;

public class DashboardActivity extends AppCompatActivity {
    SessionManager session;
    TextView tvPoints, tvWelcome;

    static final String DEFAULT_CATEGORY = "general";
    static final int DEFAULT_POINTS = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        session = new SessionManager(this);

        tvWelcome = findViewById(R.id.tvWelcome);
        tvPoints = findViewById(R.id.tvPoints);
        MaterialButton btnBrowseWaste = findViewById(R.id.btnBrowseWaste);
        MaterialButton btnLog = findViewById(R.id.btnLogWaste);
        MaterialButton btnLeaderboard = findViewById(R.id.btnLeaderboard);
        MaterialButton btnLogout = findViewById(R.id.btnLogout);

        tvWelcome.setText("Hi, " + session.getUsername() + " 👋");

        refreshPoints();

        btnBrowseWaste.setOnClickListener(v -> startActivity(new Intent(this, MainActivity.class)));
        btnLog.setOnClickListener(v -> logWaste());
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
    }

    private void refreshPoints() {
        Executors.newSingleThreadExecutor().execute(() -> {
            AppDatabase db = AppDatabase.getInstance(getApplicationContext());
            UserEntity user = db.userDao().findById(session.getUserId());
            if (user != null) {
                runOnUiThread(() -> tvPoints.setText("Total Points: " + user.totalPoints));
            }
        });
    }

    private void logWaste() {
        Executors.newSingleThreadExecutor().execute(() -> {
            AppDatabase db = AppDatabase.getInstance(getApplicationContext());
            UserEntity user = db.userDao().findById(session.getUserId());
            if (user == null) return;

            WasteLogEntity log = new WasteLogEntity();
            log.userId = user.id;
            log.category = DEFAULT_CATEGORY;
            log.points = DEFAULT_POINTS;
            log.timestamp = System.currentTimeMillis();
            db.wasteLogDao().insert(log);

            user.totalPoints += DEFAULT_POINTS;
            db.userDao().update(user);

            runOnUiThread(() -> {
                Toast.makeText(this, "+" + DEFAULT_POINTS + " points!", Toast.LENGTH_SHORT).show();
                tvPoints.setText("Total Points: " + user.totalPoints);
            });
        });
    }
}