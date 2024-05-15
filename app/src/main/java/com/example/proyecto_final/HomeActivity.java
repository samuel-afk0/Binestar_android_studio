package com.example.proyecto_final;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.clans.fab.FloatingActionButton;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnFailureListener;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    PieChart GraficoAnillo, GraficoAnilloKilometros;

    public int metaKilometros = 0;
    public double progresoKilometros = 0;

    public  int metaPasos=0, progresoPasos=0;
    public TextView txtvPasos, txtvKilometros;
    FloatingActionButton BtnAgregarRecordatorio,btnObjetivo, BtnAgregarEntrenamiento, btnAgregarObjeivo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Iniciar hilo para obtener datos de Firebase
        new Thread(new Runnable() {
            @Override
            public void run() {
                obtenerDatosFirebase();
            }
        }).start();

        // Configurar BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        if (item.getItemId() == R.id.progresos) {
                            startActivity(new Intent(HomeActivity.this, ProgresosActivities.class));
                            return true;
                        } else if (item.getItemId() == R.id.calendario) {
                            startActivity(new Intent(HomeActivity.this, CalendarioAnualActivities.class));
                            return true;
                        }
                        return false;
                    }
                });


        /////re direvion de los btonoes de la barra flotante del home activities

        BtnAgregarRecordatorio=findViewById(R.id.BtnAgregarRecordatorio);
        btnObjetivo=findViewById(R.id.btnObjetivo);
        BtnAgregarEntrenamiento = findViewById(R.id.BtnAgregarEntrenamiento);
        btnAgregarObjeivo = findViewById(R.id.btnAgregarObjetivos);
        BtnAgregarRecordatorio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, RecordatorioActivity.class);

                startActivity(intent);
            }
        });
        btnObjetivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, Registro_ActivadFisica.class);

                startActivity(intent);
            }
        });
        btnAgregarObjeivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, FormularioMetas.class);

                startActivity(intent);
            }
        });

        BtnAgregarEntrenamiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, RegistroEntrenamiento.class);

                startActivity(intent);
            }
        });



        // Configurar gráficos
        GraficoAnillo = findViewById(R.id.GraficoAnillo);
        GraficoAnilloKilometros = findViewById(R.id.GraficoAnilloKilometros);
    }

    private void obtenerDatosFirebase() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("progreso").document("BaBdwWCKVvzfNhCt7MLM");

        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String fecha = documentSnapshot.getString("fecha");
                    int metaKilogramos = documentSnapshot.getLong("metakilogramos").intValue();
                    double progresoKilogramos = documentSnapshot.getDouble("progresokilogramos");

                    metaKilometros = documentSnapshot.getLong("metakilometros").intValue();
                    progresoKilometros = documentSnapshot.getDouble("progresokilometros");

                     metaPasos = documentSnapshot.getLong("metapasos").intValue();
                     progresoPasos = documentSnapshot.getLong("progresopasos").intValue();

                    Log.d("TAG", "Fecha: " + fecha);
                    Log.d("TAG", "Meta Kilogramos: " + metaKilogramos);
                    Log.d("TAG", "Progreso Kilogramos: " + progresoKilogramos);
                    Log.d("TAG", "Meta Kilometros: " + metaKilometros);
                    Log.d("TAG", "Progreso Kilometros: " + progresoKilometros);
                    Log.d("TAG", "Meta Pasos: " + metaPasos);
                    Log.d("TAG", "Progreso Pasos: " + progresoPasos);

                    // Actualizar gráficos
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            configurarGraficoAnillo();
                            configurarGraficoAnilloKilometros();
                        }
                    });
                } else {
                    Log.d("TAG", "El documento no existe");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("TAG", "Error al obtener el documento", e);
            }
        });
    }
    ////////////////grafica para pasos

    private void configurarGraficoAnillo() {

        float pasosprogres = ((float) progresoPasos / metaPasos) * 100f;

        float porcentajeRestantePasos= 100f - pasosprogres;

        List<PieEntry> datos = new ArrayList<>();
        datos.add(new PieEntry(pasosprogres, ""));
        datos.add(new PieEntry(porcentajeRestantePasos, ""));

        List<Integer> colores = new ArrayList<>();
        colores.add(getResources().getColor(R.color.colorAccent)); // Color del progreso
        colores.add(getResources().getColor(android.R.color.transparent)); // Color transparente

        PieDataSet dataSet = new PieDataSet(datos, "");
        dataSet.setColors(colores);
        dataSet.setDrawValues(false);

        Legend leyenda = GraficoAnillo.getLegend();
        leyenda.setEnabled(false);

        PieData data = new PieData(dataSet);
        GraficoAnillo.animateY(2000);
        GraficoAnillo.setData(data);
        GraficoAnillo.invalidate();

        txtvPasos=findViewById(R.id.txtvPasos);
        txtvPasos.setText(progresoPasos+" de "+metaPasos);
    }

    private void configurarGraficoAnilloKilometros() {
        if (metaKilometros != 0) {
            float porcentajeProgresoKilometros = (float) (progresoKilometros / metaKilometros) * 100f;
            float porcentajeRestanteKilometros = 100f - porcentajeProgresoKilometros;

            List<PieEntry> datosKilometros = new ArrayList<>();
            datosKilometros.add(new PieEntry(porcentajeProgresoKilometros, "Progreso"));
            datosKilometros.add(new PieEntry(porcentajeRestanteKilometros, "Restante"));

            List<Integer> coloreskilometros = new ArrayList<>();
            coloreskilometros.add(getResources().getColor(R.color.bondy));
            coloreskilometros.add(getResources().getColor(android.R.color.transparent));

            PieDataSet dataSet = new PieDataSet(datosKilometros, "");
            dataSet.setColors(coloreskilometros);
            dataSet.setDrawValues(false);

            Legend leyenda = GraficoAnilloKilometros.getLegend();
            leyenda.setEnabled(false);

            PieData data = new PieData(dataSet);
            GraficoAnilloKilometros.animateY(2000);
            GraficoAnilloKilometros.setData(data);
            GraficoAnilloKilometros.invalidate();
            txtvKilometros=findViewById(R.id.txtvKilometros);
            txtvKilometros.setText(progresoKilometros+" de "+metaKilometros);
        } else {
            Log.d("TAG", "Error: metaKilometros es igual a cero");
        }
    }
}
