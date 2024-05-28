package com.example.proyecto_final;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;

public class CalendarioActivity extends AppCompatActivity {
    private CalendarView calendarView;
    private TextView txtRecordatorio;
    private ImageView btnBack;
    private DatabaseReference databaseReference;
    private List<EventDay> eventDays;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario);

        calendarView = findViewById(R.id.calendar_view);
        txtRecordatorio = findViewById(R.id.txtRecordatorio);
        btnBack = findViewById(R.id.btnBack);
        //REGRESAR A PAGINA HOME
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CalendarioActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("Calendar");
        eventDays = new ArrayList<>();

        loadRecordatorios();

        calendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {
                SimpleDateFormat sdf = new SimpleDateFormat("d-M-yyyy", Locale.getDefault());
                String selectedDate = sdf.format(eventDay.getCalendar().getTime());
                loadRecordatoriosForDate(selectedDate);
            }
        });
    }

    private void loadRecordatorios() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                SimpleDateFormat sdf = new SimpleDateFormat("d-M-yyyy", Locale.getDefault());

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Recordatorio recordatorio = snapshot.getValue(Recordatorio.class);
                    try {
                        if (recordatorio != null) {
                            Date date = sdf.parse(recordatorio.getFecha());
                            if (date != null) {
                                Calendar calendar = Calendar.getInstance();
                                calendar.setTime(date);
                                Drawable drawable = getResources().getDrawable(R.drawable.baseline_event_note_24);
                                eventDays.add(new EventDay(calendar, drawable));
                                calendarView.setEvents(eventDays);
                            }
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(CalendarioActivity.this, "Error al cargar los datos.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadRecordatoriosForDate(String date) {
        databaseReference.orderByChild("fecha").equalTo(date).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Recordatorio> recordatorioList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Recordatorio recordatorio = snapshot.getValue(Recordatorio.class);
                    recordatorioList.add(recordatorio);
                }

                if (recordatorioList.isEmpty()) {
                    txtRecordatorio.setText("No hay recordatorios para esta fecha.");
                } else {
                    StringBuilder recordatoriosText = new StringBuilder();
                    for (Recordatorio recordatorio : recordatorioList) {
                        recordatoriosText.append("Hora: ").append(recordatorio.getHora())
                                .append("\nFecha: ").append(recordatorio.getFecha())
                                .append("\nMensaje: ").append(recordatorio.getMensaje())
                                .append("\n\n");
                    }
                    txtRecordatorio.setText(recordatoriosText.toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(CalendarioActivity.this, "Error al cargar los datos.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
