package com.example.proyecto_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.Button;
import android.widget.ArrayAdapter;

public class FormularioMetas extends AppCompatActivity {
    Spinner spinner;
    Button btnEnviar, btnCancelar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_metas);

        spinner = findViewById(R.id.spinner);
     btnEnviar = findViewById(R.id.btnEnviar);
        btnCancelar = findViewById(R.id.btnCancelar);

        String[] items = new String[]{"Perder peso,", "Aumentar la masa muscular"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        spinner.setAdapter(adapter);


        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FormularioMetas.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }
}