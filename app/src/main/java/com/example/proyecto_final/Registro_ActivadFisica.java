package com.example.proyecto_final;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentReference;

public class Registro_ActivadFisica extends AppCompatActivity {
    EditText edtdistancia;
    Spinner SpOpcionescombo;
    Button btnEnviar, btnCancelar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_activad_fisica);

        edtdistancia = findViewById(R.id.edtdistancia);
        btnEnviar = findViewById(R.id.btnEnviar1);
        btnCancelar = findViewById(R.id.btnCancelar1);
        SpOpcionescombo = findViewById(R.id.SpOpcionescombo);

        String[] items = new String[]{"Kilometros recorridos", "Pasos realizados", ""};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        SpOpcionescombo.setAdapter(adapter);

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarDatosAFirestore();
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

    private void enviarDatosAFirestore() {
        String distancia = edtdistancia.getText().toString();

        if (distancia.isEmpty()) {
            Toast.makeText(this, "Por favor completa el campo de distancia", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("progreso").document("BaBdwWCKVvzfNhCt7MLM");

        docRef.update("progresokilometros", Double.parseDouble(distancia))
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(Registro_ActivadFisica.this, "Datos actualizados con éxito", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Registro_ActivadFisica.this, HomeActivity.class);
                    startActivity(intent);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(Registro_ActivadFisica.this, "Error al actualizar los datos", Toast.LENGTH_SHORT).show();
                });
    }
}
