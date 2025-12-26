package com.sadik.earntask;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SplashActivity extends AppCompatActivity {

    PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);

        View root = findViewById(R.id.main);
        if (root != null) {
            ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        prefManager = new PrefManager(this);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {

            if (prefManager.isLoggedIn()) {
                // ✅ Already logged in
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            } else {
                // ❌ Not logged in
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            }

            finish();

        }, 1200);
    }
}
