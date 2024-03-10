package com.example.letslearnjava;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.letslearnjava.ui.home.HomeFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class SplashActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private static final int SPLASH_TIMER = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mAuth = FirebaseAuth.getInstance();
        DbQuery.g_firestore = FirebaseFirestore.getInstance();

        new Handler().postDelayed(() -> {
            boolean isUserLoggedIn = checkUserLoggedIn();

            if (mAuth.getCurrentUser() != null) {
                DbQuery.loadData(new MyCompletedListener() {
                    @Override
                    public void onSuccess() {
                        navigateToMainActivity();
                    }

                    @Override
                    public void onFailure() {
                        showErrorToast();
                    }
                });
            } else {
                navigateToLoginActivity();
            }
            finish();
        }, SPLASH_TIMER);
    }

    private boolean checkUserLoggedIn() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        return sharedPreferences.contains("user_id");
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    private void navigateToLoginActivity() {
        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void showErrorToast() {
        Toast.makeText(SplashActivity.this, "Error", Toast.LENGTH_SHORT).show();
    }
}
