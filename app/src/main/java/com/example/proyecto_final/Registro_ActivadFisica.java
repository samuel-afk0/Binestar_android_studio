package com.example.proyecto_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Registro_ActivadFisica extends AppCompatActivity {
    EditText edtdistancia, edtcalorias;

    Button submit_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtdistancia = findViewById(R.id.edtcalorias);


        submit_button = findViewById(R.id.submit_button);
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String distancia = edtdistancia.getText().toString();
                String calorias = caloriasEditText.getText().toString();

                // Crear un Intent para pasar los datos al otro Activity
                Intent intent = new Intent(Registro_ActivadFisica.this, HomeActivity.class);
                intent.putExtra("distancia", distancia);
                intent.putExtra("calorias", calorias);
                startActivity(intent);
            }
        });
    }

}