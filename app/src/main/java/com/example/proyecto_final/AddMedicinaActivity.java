package com.example.proyecto_final;

import android.os.Bundle;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class AddMedicinaActivity extends AppCompatActivity {

    private TextInputEditText editText;
    private Button btnNextActivity;

    private CalendarView calendarView;
    private LinearLayout calendarContainer;
    private String selectFecha;
    private String selectHora;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_medicina);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializar vistas
        editText = findViewById(R.id.editText);
        calendarView = findViewById(R.id.calendarView);
        btnNextActivity = findViewById(R.id.btnNextActivity);
        int imageResource = R.drawable.baseline_calendar_month_24;

        //CALENDARIO
        calendarContainer = findViewById(R.id.calendarContainer);
        Button btnToggleCalendar = findViewById(R.id.btnToggleCalendar);

        calendarContainer.setVisibility(View.GONE);
        btnToggleCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (calendarContainer.getVisibility() == View.GONE) {
                    calendarContainer.setVisibility(View.VISIBLE);
                    btnToggleCalendar.setText("Ocultar Calendario"); // Actualizar el texto
                } else {
                    calendarContainer.setVisibility(View.GONE);
                    btnToggleCalendar.setText("Mostrar Calendario"); // Actualizar el texto
                }
            }
        });

        // Capturar fecha seleccionada
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            selectFecha = dayOfMonth + "/" + (month + 1) + "/" + year;
        });


        //RELOJ
        Button btnToggleClock = findViewById(R.id.btnToggleClock);

        // Mostrar el TimePickerDialog
        btnToggleClock.setOnClickListener(v -> {
            // Obtener la hora y minuto actual
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            // Crear un nuevo TimePickerDialog con formato de 12 horas
            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    AddMedicinaActivity.this,
                    android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                    (view, hourOfDay, minuteOfHour) -> {
                        // Formatear la hora en formato AM/PM
                        Calendar selectedTime = Calendar.getInstance();
                        selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        selectedTime.set(Calendar.MINUTE, minuteOfHour);
                        SimpleDateFormat format = new SimpleDateFormat("h:mm a", Locale.getDefault());
                        selectHora = format.format(selectedTime.getTime());

                        // Actualizar el botón para mostrar la hora seleccionada en formato AM/PM
                        btnToggleClock.setText("Hora: " + selectHora);
                    }, hour, minute, false); // false para formato de 12 horas

            // Mostrar el TimePickerDialog
            timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            timePickerDialog.show();
        });


        // Pasar datos a MainActivity
        btnNextActivity.setOnClickListener(v -> {
            String userInput = editText.getText().toString();
            Intent intent = new Intent(AddMedicinaActivity.this, CalendarioActivity.class);
            intent.putExtra("userInput", userInput);
            intent.putExtra("selectFecha", selectFecha); // Pasar la fecha seleccionada
            intent.putExtra("selectHora", selectHora); // Pasar la hora seleccionada
            intent.putExtra("imageResource", imageResource); // Pasar la imagen de calendario
            startActivity(intent);
        });
    }
}