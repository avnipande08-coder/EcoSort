package com.example.ecosort;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.button.MaterialButton;

import com.example.ecosort.data.AppDatabase;
import com.example.ecosort.data.UserEntity;
import com.example.ecosort.utils.SessionManager;
import com.example.ecosort.utils.StreakCalculator;

import java.util.concurrent.Executors;

public class ProfileActivity extends AppCompatActivity {

    SessionManager session;
    TextView tvAvatarInitial, tvUsername, tvEmail, tvPointsValue, tvStreakValue, tvItemsLoggedValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        session = new SessionManager(this);

        Toolbar toolbar = findViewById(R.id.toolbarProfile);
        setSupportActionBar(toolbar);

        tvAvatarInitial = findViewById(R.id.tvAvatarInitialProfile);
        tvUsername = findViewById(R.id.tvUsernameProfile);
        tvEmail = findViewById(R.id.tvEmailProfile);
        tvPointsValue = findViewById(R.id.tvPointsValue);
        tvStreakValue = findViewById(R.id.tvStreakValue);
        tvItemsLoggedValue = findViewById(R.id.tvItemsLoggedValue);

        MaterialButton btnLogout = findViewById(R.id.btnLogoutProfile);
        btnLogout.setOnClickListener(v -> {
            session.logout();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        loadProfile();
    }

    private void loadProfile() {
        Executors.newSingleThreadExecutor().execute(() -> {
            AppDatabase db = AppDatabase.getInstance(getApplicationContext());
            UserEntity user = db.userDao().findById(session.getUserId());
            int streak = StreakCalculator.computeCurrentStreak(
                    db.wasteLogDao().historyForUser(session.getUserId()));
            int itemsLogged = db.wasteLogDao().historyForUser(session.getUserId()).size();

            runOnUiThread(() -> {
                if (user != null) {
                    tvUsername.setText(user.username);
                    tvEmail.setText(user.email);
                    tvAvatarInitial.setText(user.username.substring(0, 1).toUpperCase());
                    tvPointsValue.setText(String.valueOf(user.totalPoints));
                }
                tvStreakValue.setText(streak + (streak == 1 ? " day" : " days"));
                tvItemsLoggedValue.setText(String.valueOf(itemsLogged));
            });
        });
    }
}