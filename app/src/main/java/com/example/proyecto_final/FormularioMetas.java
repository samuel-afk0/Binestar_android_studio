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

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class FormularioMetas extends AppCompatActivity {
    Spinner SpOpcionescombo;
    Button btnEnviar, btnCancelar;
    EditText edtdistancia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_metas);

        SpOpcionescombo = findViewById(R.id.SpOpcionescombo);
        edtdistancia = findViewById(R.id.edtdistancia);
        btnEnviar = findViewById(R.id.btnEnviar);
        btnCancelar = findViewById(R.id.btnCancelar);

        String[] items = new String[]{"Meta de pasos", "Meta de kilometros"};
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
                Intent intent = new Intent(FormularioMetas.this, HomeActivity.class);
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

        String seleccion = SpOpcionescombo.getSelectedItem().toString();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("progreso").document("BaBdwWCKVvzfNhCt7MLM");

        if (seleccion.equals("Meta de kilometros")) {
            actualizarKilometros(docRef, Double.parseDouble(distancia));
        } else if (seleccion.equals("Meta de pasos")) {
            actualizarPasos(docRef, Double.parseDouble(distancia));
        }
    }

    private void actualizarKilometros(DocumentReference docRef, double distancia) {
        docRef.update("metakilometros", distancia)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(FormularioMetas.this, "Datos de kilómetros actualizados con éxito", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(FormularioMetas.this, HomeActivity.class);
                    startActivity(intent);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(FormularioMetas.this, "Error al actualizar los datos de kilómetros", Toast.LENGTH_SHORT).show();
                });
    }

    private void actualizarPasos(DocumentReference docRef, double pasos) {
        int pasosRedondeados = (int) Math.round(pasos);

        docRef.update("metapasos", pasosRedondeados)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(FormularioMetas.this, "Datos de pasos actualizados con éxito", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(FormularioMetas.this, HomeActivity.class);
                    startActivity(intent);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(FormularioMetas.this, "Error al actualizar los datos de pasos", Toast.LENGTH_SHORT).show();
                });
    }
}
