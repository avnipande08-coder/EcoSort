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
        spinnerCategory = findViewById(R.id.spinnerCategory);
        MaterialButton btnBrowseWaste = findViewById(R.id.btnBrowseWaste);
        MaterialButton btnLog = findViewById(R.id.btnLogWaste);
        MaterialButton btnLeaderboard = findViewById(R.id.btnLeaderboard);
        MaterialButton btnLogout = findViewById(R.id.btnLogout);

        tvWelcome.setText("Hi, " + session.getUsername() + " 👋");
        spinnerCategory.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categories));

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
                runOnUiThread(() -> tvPoints.setText(String.valueOf(user.totalPoints)));
            }
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
        });
    }
}