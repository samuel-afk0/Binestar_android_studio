package com.example.proyecto_final;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Spinner;
import android.widget.Button;
import android.widget.ArrayAdapter;

public class FormularioMetas extends AppCompatActivity {
    Spinner spinner;
    Button submitButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_metas);

        spinner = findViewById(R.id.spinner);
        submitButton = findViewById(R.id.submit_button);


        String[] items = new String[]{"Perder peso,", "Aumentar la masa muscular", "Mejorar su condición física"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        spinner.setAdapter(adapter);


    }
}