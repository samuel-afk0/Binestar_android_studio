package com.example.proyecto_final;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.CarrierConfigManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class SplashScreen extends Activity {
    private static final long SPLASH_TIMEOUT = 10000;
    private MediaPlayer mediaPlayer;
    private Handler handler;
    private Runnable runnable;
    private long remainingTime = SPLASH_TIMEOUT;
    private long pauseTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ImageView imageView = findViewById(R.id.imageView);
        Glide.with(this).load(R.drawable.splashgif).into(imageView);
        mediaPlayer = MediaPlayer.create(this, R.raw.audio4);
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        };

        handler.postDelayed(runnable, remainingTime);
        mediaPlayer.start();
    }

    //CICLOS DE VIDA APLICADOS EN AUDIO DE INTRO DE LA APP
    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer.isPlaying()) {
            handler.removeCallbacks(runnable);
            pauseTime = System.currentTimeMillis();
            mediaPlayer.pause();
            //Toast.makeText(this, "PAUSE", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            long timePaused = System.currentTimeMillis() - pauseTime;
            remainingTime -= timePaused;
            handler.postDelayed(runnable, remainingTime);
            mediaPlayer.start();
            //Toast.makeText(this, "RESUME", Toast.LENGTH_SHORT).show();
        }
    }
}