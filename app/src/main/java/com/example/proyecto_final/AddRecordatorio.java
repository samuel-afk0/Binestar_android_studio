package com.example.proyecto_final;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageView;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;

public class AddRecordatorio extends AppCompatActivity {
    private CalendarView calendarView;
    private TimePicker timePicker;
    private EditText edtMensaje;
    //private TextView txtViewMensaje;
    private ImageView btnGuardar;
    private DatabaseReference databaseReference;
    private String stringDateSelected;
    private List<String> dataList = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_recordatorio);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        calendarView = findViewById(R.id.calendarView);
        timePicker = findViewById(R.id.timePicker);
        edtMensaje = findViewById(R.id.edtMensaje);
        //txtViewMensaje = findViewById(R.id.txtViewMensaje);
        btnGuardar = findViewById(R.id.btnGuardar);



        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            stringDateSelected = dayOfMonth + "-" + (month + 1) + "-" + year;
            calendarClicked();
        });




    databaseReference = FirebaseDatabase.getInstance().getReference("Calendar");

//GUARDAR DATOS
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = databaseReference.push().getKey();
                //String fecha = DateFormat.format("dd-MM-yyyy", calendarView.getDate()).toString();
                String fecha = stringDateSelected;
                String hora = timePicker.getHour() + ":" + timePicker.getMinute();
                String mensaje = edtMensaje.getText().toString();

                Recordatorio recordatorio = new Recordatorio(id, fecha, hora, mensaje);

                databaseReference.child(id).setValue(recordatorio);
                Toast.makeText(AddRecordatorio.this, "Recordatorio guardado", Toast.LENGTH_SHORT).show();
                edtMensaje.setText("");
            }
        });


    }

    private void calendarClicked() {
        databaseReference.child(stringDateSelected).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    String value = snapshot.getValue().toString();
                   // txtViewMensaje.setText(snapshot.getValue().toString());
                    edtMensaje.setText(value);
                    dataList.add(value);


                } else {
                    //txtViewMensaje.setText("no hay recordatorios");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }




}