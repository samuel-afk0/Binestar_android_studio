package com.example.proyecto_final;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ProgresosActivities extends AppCompatActivity {
    private BarChart barChart;
    private LineChart lineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progresos_activities);

        lineChart = findViewById(R.id.lineChart);
        barChart = findViewById(R.id.barChart);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.progresos);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        if (item.getItemId() == R.id.home) {
                            startActivity(new Intent(ProgresosActivities.this, HomeActivity.class));
                            return true;
                        } else if (item.getItemId() == R.id.calendario) {
                            startActivity(new Intent(ProgresosActivities.this, CalendarioAnualActivities.class));
                            return true;
                        }
                        return false;
                    }
                });

        obtenerDatosFirebase();
        configurarGraficoLineas();
        configurarGraficoBarras();
    }

    private void configurarGraficoLineas() {
        // Configurar el gráfico de líneas
        List<Entry> entries = new ArrayList<>();
        entries.add(new Entry(1, 1500));
        entries.add(new Entry(2, 800));
        entries.add(new Entry(3, 1900));
        entries.add(new Entry(5, 50));
        entries.add(new Entry(6, 300));
        entries.add(new Entry(7, 2100));
        entries.add(new Entry(8, 400));
        entries.add(new Entry(9, 2000));
        entries.add(new Entry(10, 2300));
        entries.add(new Entry(11, 2700));
        entries.add(new Entry(12, 50));

        LineDataSet dataSet = new LineDataSet(entries, "Calorías al mes");
        dataSet.setColor(Color.BLUE);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setFillColor(Color.BLUE);
        dataSet.setFillAlpha(50);
        dataSet.setDrawFilled(true);

        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        YAxis yAxisRight = lineChart.getAxisRight();
        yAxisRight.setEnabled(false);

        Description description = new Description();
        description.setText("Meses");
        lineChart.setDescription(description);

        Legend legend = lineChart.getLegend();
        legend.setEnabled(true);

        lineChart.animateX(1000);
    }

    private void configurarGraficoBarras() {
        // Configurar el gráfico de barras
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setDrawGridLines(false);
        xAxis.setTextColor(Color.WHITE);

        YAxis leftAxis = barChart.getAxisLeft();
        YAxis rightAxis = barChart.getAxisRight();
        leftAxis.setDrawGridLines(true);
        rightAxis.setDrawGridLines(false);

        List<Float> valores = obtenerValoresDeDatosPredefinidos();
        ArrayList<BarEntry> entradas = new ArrayList<>();
        for (int i = 0; i < valores.size(); i++) {
            entradas.add(new BarEntry(i, valores.get(i)));
        }

        BarDataSet dataSet = new BarDataSet(entradas, "Ejercicio por día (minutos)");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        BarData data = new BarData(dataSet);
        barChart.setData(data);
        barChart.invalidate();
    }

    private List<Float> obtenerValoresDeDatosPredefinidos() {
        // Valores predefinidos para cada día de la semana
        List<Float> valores = new ArrayList<>();
        valores.add(120f); // Lunes
        valores.add(90f);  // Martes
        valores.add(150f); // Miércoles
        valores.add(80f);  // Jueves
        valores.add(100f); // Viernes
        valores.add(130f); // Sábado
        valores.add(110f); // Domingo
        return valores;
    }

    private void obtenerDatosFirebase() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference progresoRef = db.collection("progreso").document("BaBdwWCKVvzfNhCt7MLM");

        progresoRef.collection("ejercicios").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<Integer> pasosList = new ArrayList<>();
                List<Double> caloriasList = new ArrayList<>();
                List<Double> kilometrosList = new ArrayList<>();

                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    Double calorias = document.getDouble("calorias");
                    Long pasosLong = document.getLong("pasos");
                    Double kilometros = document.getDouble("kilometros");

                    // Verificar si el valor de pasosLong es nulo antes de llamar a intValue()
                    int pasos = pasosLong != null ? pasosLong.intValue() : 0;

                    pasosList.add(pasos);
                    caloriasList.add(calorias);
                    kilometrosList.add(kilometros);
                }
                Log.d("TAG", "Pasos: " + pasosList.toString());
                Log.d("TAG", "Calorias: " + caloriasList.toString());
                Log.d("TAG", "Kilometros: " + kilometrosList.toString());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("TAG", "Error al obtener los documentos de la subcolección 'ejercicios'", e);
            }
        });
    }
}
