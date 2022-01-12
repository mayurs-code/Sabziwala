package com.example.sabziwala.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.sabziwala.R;
import com.example.sabziwala.Utilities.AppSettings;

public class SplashActivity extends AppCompatActivity {
    RelativeLayout r1;
    Handler handler;
    private long secondsRemaining;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        checkPermissions();
        handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (AppSettings.isLogin(SplashActivity.this))
                    gotoHomeActivity();
                else gotoPreLoginActivity();
            }
        }, 1000);


    }

    private void checkPermissions() {
        boolean permissionGranted = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        if (permissionGranted) {
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
        }

    }
    private void gotoHomeActivity() {
        Intent mainIntent = new Intent(SplashActivity.this, HomeActivity.class);
        startActivity(mainIntent);
        finish();
    }

    private void gotoPreLoginActivity() {
        Intent mainIntent = new Intent(SplashActivity.this, PreLoginActivity.class);
        startActivity(mainIntent);
        finish();
    }
}
