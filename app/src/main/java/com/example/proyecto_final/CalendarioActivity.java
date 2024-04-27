package com.example.proyecto_final;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import androidx.appcompat.widget.Toolbar;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CalendarioActivity extends AppCompatActivity {
    private CalendarView calendarView;
    private LinearLayout calendarContainer;
    private Button btnToggleCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_calendario);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //TOOLBAR
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button btnOpenActivity = findViewById(R.id.btnOpenActivity);
        TextView textViewResult = findViewById(R.id.textViewResult);

        // Obtener medicina ingresada desde AddMedicinaActivity
        String userInput = getIntent().getStringExtra("userInput");
        //textViewResult.setText("Texto ingresado: " + userInput);
        // Verificar si el valor es nulo o vacío
        if (userInput != null && !userInput.isEmpty()) {
            textViewResult.setText("Medicina agregada: " + userInput);
        } else {
            textViewResult.setText("No se ha agregado medicina");
        }

        TextView textView = findViewById(R.id.textView);
        //Obtener fecha ingresada desde AddMedicinaActivity
        String selectFecha = getIntent().getStringExtra("selectFecha");
        if (selectFecha != null && !userInput.isEmpty()) {
            textView.setText("Fecha seleccionada: " + selectFecha);
        }else {
            textView.setText("No se ha agregado fecha");
        }

        TextView textViewH = findViewById(R.id.textViewH);
        //Obtener hora ingresada desde AddMedicinaActivity
        String selectHora = getIntent().getStringExtra("selectHora");
        if (selectHora != null && !userInput.isEmpty()) {
            textViewH.setText("Hora seleccionada: " + selectHora);
        }else {
            textViewH.setText("No se ha agregado hora");
        }
        // Obtener la referencia de la imagen del Intent
        int imageResource = getIntent().getIntExtra("imageResource", 0);

        // Configurar la vista de imagen para mostrar la imagen
        ImageView imageView = findViewById(R.id.imageView);
        if (imageResource != 0) {
            imageView.setImageResource(imageResource);
        }

        btnOpenActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CalendarioActivity.this, AddMedicinaActivity.class);
                startActivity(intent);
            }
        });
    }
}