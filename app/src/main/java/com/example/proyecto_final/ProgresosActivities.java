package com.example.proyecto_final;
import java.util.List;

import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.formatter.ValueFormatter; // Importación necesaria
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import com.github.mikephil.charting.components.XAxis;
import android.content.Intent;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import java.util.ArrayList;
import java.util.List;
import android.graphics.Color;


public class ProgresosActivities extends AppCompatActivity {
    private BarChart barChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progresos_activities);
        LineChart lineChart = findViewById(R.id.lineChart);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.progresos);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        if (item.getItemId() == R.id.progresos) {
                            return true;
                        } else {
                            if (item.getItemId() == R.id.home) {
                                startActivity(new Intent(ProgresosActivities.this, HomeActivity.class));
                                return true;
                            } else if (item.getItemId() == R.id.perfil) {
                                startActivity(new Intent(ProgresosActivities.this, MainActivity.class));
                                return true;
                            } else {
                                return false;
                            }
                        }
                    }
                });

        barChart = findViewById(R.id.barChart);
        configurarGraficoBarras();
        mostrarGraficoBarras();
        // Datos de ejemplo: calorías al mes
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

        // Configurar el conjunto de datos
        LineDataSet dataSet = new LineDataSet(entries, "Calorías al mes");
        dataSet.setColor(Color.BLUE);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER); // Configurar el modo como CUBIC_BEZIER para una apariencia de montaña
        dataSet.setFillColor(Color.BLUE); // Color del relleno bajo la curva
        dataSet.setFillAlpha(50); // Transparencia del relleno (0-255)
        dataSet.setDrawFilled(true); // Habilitar el dibujo del área rellena

// Configurar la línea de datos
        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);

        // Configurar ejes
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        YAxis yAxisRight = lineChart.getAxisRight();
        yAxisRight.setEnabled(false); // Deshabilitar eje Y derecho

        // Configurar descripción
        Description description = new Description();
        description.setText("Meses");
        lineChart.setDescription(description);

        // Configurar leyenda
        Legend legend = lineChart.getLegend();
        legend.setEnabled(true);

// Animaciones
        lineChart.animateX(1000);
    }

    private void configurarGraficoBarras() {
        XAxis xAxis = barChart.getXAxis(); // Obtener el eje X desde el BarChart
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setDrawGridLines(false);
        xAxis.setValueFormatter(new ValueFormatter() {
            final String[] diasSemana = {"L", "M", "X", "J", "V", "S", "D"};

            @Override
            public String getFormattedValue(float value) {
                int index = (int) value;
                if (index >= 0 && index < diasSemana.length) {
                    return diasSemana[index];
                }
                return "";
            }
        });

        xAxis.setTextColor(Color.WHITE); // Color del texto del eje X

        YAxis leftAxis = barChart.getAxisLeft();
        YAxis rightAxis = barChart.getAxisRight();
        leftAxis.setDrawGridLines(true);
        rightAxis.setDrawGridLines(false);
    }

    private void configurarGraficoBarras(List<Float> valores) {
        ArrayList<BarEntry> entradas = new ArrayList<>();
        for (int i = 0; i < valores.size(); i++) {
            entradas.add(new BarEntry(i, valores.get(i)));
        }
        BarDataSet dataSet = new BarDataSet(entradas, "Ejercicio por día (minutos)");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS); // Colores predefinidos
        BarData data = new BarData(dataSet);
        barChart.setData(data);
        barChart.invalidate();
    }

    private void mostrarGraficoBarras() {
        List<Float> valores = obtenerValoresDeDatosPredefinidos();
        configurarGraficoBarras(valores);
    }

    private List<Float> obtenerValoresDeDatosPredefinidos() {
        // Definir los valores predefinidos para cada día de la semana
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



}
