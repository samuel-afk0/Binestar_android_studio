package com.example.proyecto_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Registro_ActivadFisica extends AppCompatActivity {
    EditText edtdistancia, edtcalorias;

    Button btnEnviar, btnCancelar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_activad_fisica);

        btnEnviar = findViewById(R.id.btnEnviar1);
        btnCancelar = findViewById(R.id.btnCancelar1);

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Registro_ActivadFisica.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }

}