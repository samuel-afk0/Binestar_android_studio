package com.example.proyecto_final;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class CalendarioAnualActivities extends AppCompatActivity {

    private TextView customMessageTextView;
    private CalendarView calendarView;
    private PieChart graficoAnillo1;
    private PieChart graficoAnillo2;
    private PieChart graficoAnillo3;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario_anual_activities);

        customMessageTextView = findViewById(R.id.customMessageTextView);
        calendarView = findViewById(R.id.calendarView);
        graficoAnillo1 = findViewById(R.id.GraficoAnillo1);
        graficoAnillo2 = findViewById(R.id.GraficoAnillo2);
        graficoAnillo3 = findViewById(R.id.GraficoAnillo3);
        btnBack = findViewById(R.id.btnBack);

        // Configurar el listener para el evento de selección de fecha
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                String mensaje = "Has seleccionado: " + dayOfMonth + "/" + (month + 1) + "/" + year;
                customMessageTextView.setText(mensaje);

                actualizarDatosGraficos();
            }
        });


        actualizarDatosGraficos();
    }

    // Método para actualizar los datos
    private void actualizarDatosGraficos() {
        Random random = new Random();
        float progreso1 = random.nextInt(100);
        float progreso2 = random.nextInt(100);
        float progreso3 = random.nextInt(100);

        // Crear lista de datos para los gráficos
        List<PieEntry> entries1 = new ArrayList<>();
        List<PieEntry> entries2 = new ArrayList<>();
        List<PieEntry> entries3 = new ArrayList<>();
        entries1.add(new PieEntry(progreso1, ""));
        entries1.add(new PieEntry(100 - progreso1, ""));
        entries2.add(new PieEntry(progreso2, ""));
        entries2.add(new PieEntry(100 - progreso2, ""));
        entries3.add(new PieEntry(progreso3, ""));
        entries3.add(new PieEntry(100 - progreso3, ""));

        PieDataSet dataSet1 = new PieDataSet(entries1, "");
        PieDataSet dataSet2 = new PieDataSet(entries2, "");
        PieDataSet dataSet3 = new PieDataSet(entries3, "");

        // Establecer colores para el progreso y el resto
        dataSet1.setColors(Color.BLUE, Color.TRANSPARENT);
        dataSet2.setColors(Color.GREEN, Color.TRANSPARENT);
        dataSet3.setColors(Color.RED, Color.TRANSPARENT);
        graficoAnillo1.getLegend().setEnabled(false);
        graficoAnillo1.getDescription().setEnabled(false);
        graficoAnillo2.getLegend().setEnabled(false);
        graficoAnillo2.getDescription().setEnabled(false);
        graficoAnillo3.getLegend().setEnabled(false);
        graficoAnillo3.getDescription().setEnabled(false);

        PieData data1 = new PieData(dataSet1);
        PieData data2 = new PieData(dataSet2);
        PieData data3 = new PieData(dataSet3);

        graficoAnillo1.setData(data1);
        graficoAnillo2.setData(data2);
        graficoAnillo3.setData(data3);

        graficoAnillo1.invalidate();
        graficoAnillo2.invalidate();
        graficoAnillo3.invalidate();
        //REGRESAR A PAGINA HOME
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CalendarioAnualActivities.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }
}
