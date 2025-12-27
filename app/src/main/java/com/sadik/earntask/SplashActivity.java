package com.sadik.earntask;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    PrefManager pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        pref = new PrefManager(this);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {

            if (pref.getToken() != null) {
                // User already logged in
                startActivity(new Intent(this, MainActivity.class));
            } else {
                // Not logged in
                startActivity(new Intent(this, LoginActivity.class));
            }
            finish();

        }, 1200);
    }
}
