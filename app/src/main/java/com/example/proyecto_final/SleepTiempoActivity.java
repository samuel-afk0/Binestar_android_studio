package com.example.proyecto_final;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

import java.time.LocalTime;

import nl.joery.timerangepicker.TimeRangePicker;

public class SleepTiempoActivity extends AppCompatActivity {
    private String[] methods = {"16/8", "18/6", "20/4", "22/2"};

    private TextView startTime;
    private TextView endTime;
    private TextView startAt;
    private TextView endAt;
    private TextView duration;
    private TextView method;
    private TimeRangePicker picker;
    private AppCompatSeekBar seekbar;
    private ImageView btnBack, btnConsejo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.sleep_tiempo);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //Imagen gif consejos
        ImageView gifImageView = findViewById(R.id.btnConsejo);
        Glide.with(this).load(R.drawable.nutritionplan).into(gifImageView);
        btnBack = findViewById(R.id.btnBack);
        btnConsejo = findViewById(R.id.btnConsejo);
        picker = findViewById(R.id.trp_sleeping);
        picker.setStartTimeMinutes(22 * 60 + 30);
        picker.setEndTimeMinutes(8 * 60 + 30);

        startTime = findViewById(R.id.start_time);
        endTime = findViewById(R.id.end_time);
        startAt = findViewById(R.id.tv_breakfast_time);
        endAt = findViewById(R.id.tv_fast_time);
        duration = findViewById(R.id.tv_sleeping_duration);
        method = findViewById(R.id.tv_method_selected);
        seekbar = findViewById(R.id.sb_method);
        seekbar.setProgress(1);

        updateTimes();
        updateDuration();
        updateMethod();
        picker.setOnTimeChangeListener(new TimeRangePicker.OnTimeChangeListener() {
            @Override
            public void onStartTimeChange(TimeRangePicker.Time startTime) {
                updateTimes();
            }

            @Override
            public void onEndTimeChange(TimeRangePicker.Time endTime) {
                updateTimes();
            }

            @Override
            public void onDurationChange(TimeRangePicker.TimeDuration duration) {
                updateDuration();
            }
        });

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar sb, int progress, boolean fromUser) {
                updateMethod();
            }

            @Override
            public void onStartTrackingTouch(SeekBar sb) {}

            @Override
            public void onStopTrackingTouch(SeekBar sb) {}
        });
    }

    private void updateDuration() {
        duration.setText(getString(R.string.duracion, picker.getDuration()));
    }

    private void updateTimes() {
        endTime.setText(picker.getEndTime().toString());
        startTime.setText(picker.getStartTime().toString());
        calculatePeriod();
    }

    private void updateMethod() {
        method.setText(methods[seekbar.getProgress()]);
        calculatePeriod();
    }

    private void calculatePeriod() {
        long eatingHours = 8L - (seekbar.getProgress() * 2);

        TimeRangePicker.TimeDuration awakeDuration = new TimeRangePicker.TimeDuration(picker.getEndTime(), picker.getStartTime());
        LocalTime midEatingTime = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            midEatingTime = picker.getEndTime().getLocalTime().plusHours(awakeDuration.getHour() / 2);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startAt.setText(midEatingTime.minusHours(eatingHours / 2).toString());
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            endAt.setText(midEatingTime.plusHours(eatingHours / 2).toString());
        }
        //REGRESAR A PAGINA HOME
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SleepTiempoActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
        //REGRESAR A PAGINA HOME
        btnConsejo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SleepTiempoActivity.this, ConsejosActivity.class);
                startActivity(intent);
            }
        });
    }
}