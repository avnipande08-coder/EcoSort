package com.example.ecosort;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ecosort.data.*;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import java.util.concurrent.Executors;

public class SignupActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        TextInputEditText etUsername = findViewById(R.id.etUsername);
        TextInputEditText etEmail = findViewById(R.id.etEmail);
        TextInputEditText etPassword = findViewById(R.id.etPassword);
        MaterialButton btnSignup = findViewById(R.id.btnSignup);

        btnSignup.setOnClickListener(v -> {
            String u = etUsername.getText().toString().trim();
            String e = etEmail.getText().toString().trim();
            String p = etPassword.getText().toString().trim();
            if (u.isEmpty() || e.isEmpty() || p.isEmpty()) {
                Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }
            Executors.newSingleThreadExecutor().execute(() -> {
                AppDatabase db = AppDatabase.getInstance(getApplicationContext());
                UserEntity existing = db.userDao().findByUsernameOrEmail(u, e);
                if (existing != null) {
                    runOnUiThread(() -> Toast.makeText(this, "Username/email taken", Toast.LENGTH_SHORT).show());
                    return;
                }
                UserEntity user = new UserEntity();
                user.username = u;
                user.email = e;
                user.passwordHash = PasswordUtils.hash(p);
                user.totalPoints = 0;
                db.userDao().insert(user);
                runOnUiThread(() -> {
                    Toast.makeText(this, "Signup successful, login now", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, LoginActivity.class));
                    finish();
                });
            });
        });
    }
}
