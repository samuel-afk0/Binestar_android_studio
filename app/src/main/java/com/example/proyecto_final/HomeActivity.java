package com.example.proyecto_final;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import java.util.ArrayList;
import java.util.List;
import com.airbnb.lottie.LottieAnimationView;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.MotionLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.graphics.Color;
import android.content.res.ColorStateList;
import android.graphics.Color;
import androidx.core.content.ContextCompat;
public class HomeActivity extends AppCompatActivity {
    PieChart GraficoAnillo,GraficoAnilloKilometros;
    Button MenuFlota;
    private com.github.clans.fab.FloatingActionButton button1, button2, button3, BtnAgregarRecordatorio;
    private com.github.clans.fab.FloatingActionButton menuFlota;


    private boolean areButtonsVisible = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        if (item.getItemId() == R.id.progresos) {
                            startActivity(new Intent(HomeActivity.this, ProgresosActivities.class));
                            return true;
                        } else {

                            return false;
                        }
                    }

                });
        GraficoAnillo = findViewById(R.id.GraficoAnillo);
        configurarGraficoAnillo();
        GraficoAnilloKilometros=findViewById(R.id.GraficoAnilloKilometros);
        configurarGraficoAnilloKilometros();

        ///menu flotannte
        // Referencias a los botones en el layout
        menuFlota = findViewById(R.id.MenuFlota);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        BtnAgregarRecordatorio = findViewById(R.id.BtnAgregarRecordatorio);

        // Configuración del OnClickListener para el botón flotante principal
        menuFlota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cambiar la visibilidad de los botones adicionales
                if (areButtonsVisible) {
                    button1.setVisibility(View.INVISIBLE);
                    button2.setVisibility(View.INVISIBLE);
                    button3.setVisibility(View.INVISIBLE);
                    BtnAgregarRecordatorio = findViewById(R.id.BtnAgregarRecordatorio);
                } else {
                    button1.setVisibility(View.VISIBLE);
                    button2.setVisibility(View.VISIBLE);
                    button3.setVisibility(View.VISIBLE);
                    BtnAgregarRecordatorio = findViewById(R.id.BtnAgregarRecordatorio);
                }
                // Cambiar el estado de visibilidad de los botones
                areButtonsVisible = !areButtonsVisible;
            }
        });
        menuFlota=findViewById(R.id.MenuFlota);
        menuFlota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, FormularioMetas.class);

                startActivity(intent);

            }
        });
        //ABRIR ACTIVIDAD RECORDATORIO
        BtnAgregarRecordatorio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, RecordatorioActivity.class);

                startActivity(intent);
            }
        });
    }
    private void configurarGraficoAnillo() {
        // Crear datos para el gráfico de dona
        List<PieEntry> datos = new ArrayList<>();
        datos.add(new PieEntry(75f, "")); // Progreso (porcentaje)
        datos.add(new PieEntry(25f, "")); // Espacio en blanco para el resto del círculo

        // Configurar colores
        List<Integer> colores = new ArrayList<>();
        colores.add(getResources().getColor(R.color.colorAccent)); // Color del progreso
        colores.add(getResources().getColor(android.R.color.transparent)); // Color transparente para el espacio en blanco

        // Crear conjunto de datos
        PieDataSet dataSet = new PieDataSet(datos, "");
        dataSet.setColors(colores);
        dataSet.setDrawValues(false); // No mostrar valores en las partes del círculo

        // Configurar leyenda
        Legend leyenda = GraficoAnillo.getLegend();
        leyenda.setEnabled(false); // Deshabilitar la leyenda

        // Crear objeto PieData
        PieData data = new PieData(dataSet);

        // Configurar animación
        GraficoAnillo.animateY(2000);

        // Establecer datos en el gráfico
        GraficoAnillo.setData(data);

        // Actualizar el gráfico
        GraficoAnillo.invalidate();
    }
    private void configurarGraficoAnilloKilometros() {
        // Crear datos para el gráfico de dona
        List<PieEntry> datosKilometros = new ArrayList<>();
        datosKilometros.add(new PieEntry(20f, "")); // Progreso (porcentaje)
        datosKilometros.add(new PieEntry(80f, "")); // Espacio en blanco para el resto del círculo

        // Configurar colores
        List<Integer> coloreskilometros = new ArrayList<>();
        coloreskilometros.add(getResources().getColor(R.color.bondy)); // Color del progreso
        coloreskilometros.add(getResources().getColor(android.R.color.transparent)); // Color transparente para el espacio en blanco

        // Crear conjunto de datos
        PieDataSet dataSet = new PieDataSet(datosKilometros, "");
        dataSet.setColors(coloreskilometros);
        dataSet.setDrawValues(false); // No mostrar valores en las partes del círculo

        // Configurar leyenda
        Legend leyenda = GraficoAnillo.getLegend();
        leyenda.setEnabled(false); // Deshabilitar la leyenda

        // Crear objeto PieData
        PieData data = new PieData(dataSet);

        // Configurar animación
        GraficoAnilloKilometros.animateY(2000);

        // Establecer datos en el gráfico
        GraficoAnilloKilometros.setData(data);

        // Actualizar el gráfico
        GraficoAnilloKilometros.invalidate();
    }

}

