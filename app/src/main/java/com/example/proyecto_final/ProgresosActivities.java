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
import com.github.mikephil.charting.charts.PieChart;
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
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.github.mikephil.charting.charts.PieChart;

import java.util.ArrayList;
import java.util.List;

public class ProgresosActivities extends AppCompatActivity {
    private BarChart barChart, barChart2, barChart3;
    private LineChart lineChart, lineChart2, lineChart3;

    private PieChart pieChart,pieChart2,pieChart3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progresos_activities);

        lineChart = findViewById(R.id.lineChart);
        lineChart2 = findViewById(R.id.lineChart2);
        lineChart3 = findViewById(R.id.lineChart3);
        barChart = findViewById(R.id.barChart);
        barChart2 = findViewById(R.id.barChart2);
        barChart3 = findViewById(R.id.barChart3);
        pieChart = findViewById(R.id.pieChart);
        pieChart2 = findViewById(R.id.pieChart2);
        pieChart3 = findViewById(R.id.pieChart3);

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
    }

    private void configurarGraficoLineas(List<Double> caloriasList, LineChart lineChart) {
        // Configurar el gráfico de líneas
        List<Entry> entries = new ArrayList<>();
        int cantidadDeDatos = lineChart == lineChart3 ? caloriasList.size() : lineChart == lineChart2 ? 15 : 8;
        for (int i = 0; i < cantidadDeDatos; i++) {
            entries.add(new Entry(i, caloriasList.get(i).floatValue()));
        }

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
        lineChart.invalidate();
    }

    private void configurarGraficoBarras(List<Integer> pasosList, BarChart barChart) {
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

        ArrayList<BarEntry> entradas = new ArrayList<>();
        int cantidadDeDatos = barChart == barChart3 ? pasosList.size() : barChart == barChart2 ? 15 : 8;
        for (int i = 0; i < cantidadDeDatos; i++) {
            entradas.add(new BarEntry(i, pasosList.get(i)));
        }

        BarDataSet dataSet = new BarDataSet(entradas, "Ejercicio por día (minutos)");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        BarData data = new BarData(dataSet);
        barChart.setData(data);
        barChart.invalidate();
    }

    private void configurarGraficoPastel(List<Double> kilometrosList, PieChart pieChart) {
        // Calcular el total de kilómetros
        double totalKilometros = 0;
        for (Double kilometros : kilometrosList) {
            totalKilometros += kilometros;
        }

        // Configurar los datos para el gráfico de pastel
        ArrayList<PieEntry> entries = new ArrayList<>();
        int cantidadDeDatos = 0;
        if (pieChart == pieChart2) {
            cantidadDeDatos = 15;
        } else if (pieChart == pieChart3) {
            cantidadDeDatos = kilometrosList.size();
        } else {
            cantidadDeDatos = 7;
        }

        for (int i = 0; i < cantidadDeDatos; i++) {
            double porcentaje = (kilometrosList.get(i) / totalKilometros) * 100;
            entries.add(new PieEntry((float) porcentaje, "Día " + (i + 1)));
        }

        PieDataSet dataSet = new PieDataSet(entries, "Kilómetros por día");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData data = new PieData(dataSet);

        // Configurar el gráfico
        pieChart.setData(data);
        pieChart.setDrawEntryLabels(false); // Deshabilitar etiquetas en las entradas
        pieChart.getDescription().setEnabled(false); // Deshabilitar descripción
        pieChart.animateY(1000);
        pieChart.invalidate();
    }

    private void obtenerDatosFirebase() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference progresoRef = db.collection("progreso").document("BaBdwWCKVvzfNhCt7MLM");

        progresoRef.collection("ejercicios").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<Integer> pasosList = new ArrayList<>();
                List<Double> caloriasList = new ArrayList<>();
                List<Double> kilometrosList = new ArrayList<>(); // Agregar lista para los kilómetros

                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    Double calorias = document.getDouble("calorias");
                    Long pasosLong = document.getLong("pasos");
                    Double kilometros = document.getDouble("kilometros"); // Recoger los kilómetros

                    int pasos = pasosLong != null ? pasosLong.intValue() : 0;

                    pasosList.add(pasos);
                    caloriasList.add(calorias);
                    kilometrosList.add(kilometros);
                }

                Log.d("TAG", "Pasos: " + pasosList.toString());
                Log.d("TAG", "Calorias: " + caloriasList.toString());
                Log.d("TAG", "Kilometros: " + kilometrosList.toString());

                configurarGraficoLineas(caloriasList, lineChart);
                configurarGraficoLineas(caloriasList, lineChart2);
                configurarGraficoLineas(caloriasList, lineChart3);
                configurarGraficoBarras(pasosList, barChart);
                configurarGraficoBarras(pasosList, barChart2);
                configurarGraficoBarras(pasosList, barChart3);
                configurarGraficoPastel(kilometrosList, pieChart);
                configurarGraficoPastel(kilometrosList, pieChart2);
                configurarGraficoPastel(kilometrosList, pieChart3);

                // Puedes agregar configuración de gráfico para los kilómetros aquí si es necesario
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("TAG", "Error al obtener los documentos de la subcolección 'ejercicios'", e);
            }
        });
    }

}