package com.example.proyecto_final;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Spinner;
import android.widget.Button;
import android.widget.ArrayAdapter;

public class RegistroEntrenamiento extends AppCompatActivity {
    Spinner spinner;
    Button submitButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_entrenamiento);

        spinner = findViewById(R.id.spinner);
        submitButton = findViewById(R.id.submit_button);

        // Define los elementos del ComboBox
        String[] items = new String[]{"Vigorosa", "Moderado", "Ligera"};

        // Crea un adaptador para el ComboBox
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);

        // Asigna el adaptador al ComboBox
        spinner.setAdapter(adapter);
    }
}