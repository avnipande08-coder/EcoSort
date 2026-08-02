package com.example.ecosort;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText editTextUsername, editTextPassword;
    Button buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        editTextUsername =
                findViewById(R.id.editTextUsername);

        editTextPassword =
                findViewById(R.id.editTextPassword);

        buttonLogin =
                findViewById(R.id.buttonLogin);


        buttonLogin.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        String username =
                                editTextUsername
                                        .getText()
                                        .toString()
                                        .trim();

                        String password =
                                editTextPassword
                                        .getText()
                                        .toString()
                                        .trim();

                        LoginRequest loginRequest =
                                new LoginRequest(
                                        username,
                                        password
                                );

                        AuthService authService =
                                AuthService.getInstance();

                        authService.login(loginRequest).enqueue(

                                new Callback<LoginResponse>() {

                                    @Override
                                    public void onResponse(
                                            Call<LoginResponse> call,
                                            Response<LoginResponse> response) {

                                        if (response.isSuccessful()
                                                && response.body() != null) {

                                            Toast.makeText(
                                                    LoginActivity.this,
                                                    "Login Successful",
                                                    Toast.LENGTH_SHORT
                                            ).show();

                                            Intent intent =
                                                    new Intent(
                                                            LoginActivity.this,
                                                            MainActivity.class
                                                    );

                                            startActivity(intent);

                                            finish();
                                        }
                                        else {

                                            Toast.makeText(
                                                    LoginActivity.this,
                                                    "Invalid Username or Password",
                                                    Toast.LENGTH_SHORT
                                            ).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(
                                            Call<LoginResponse> call,
                                            Throwable t) {

                                        Toast.makeText(
                                                LoginActivity.this,
                                                t.getMessage(),
                                                Toast.LENGTH_LONG
                                        ).show();
                                    }
                                }
                        );
                    }
                }
        );
    }
}