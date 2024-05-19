package com.example.proyecto_final;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddRecordatorio extends AppCompatActivity {
    private CalendarView calendarView;
    private TimePicker timePicker;
    private EditText edtMensaje;
    private ImageView btnGuardar, btnClose;
    private DatabaseReference databaseReference;
    private String stringDateSelected;
    private String stringHourSelected;
    private List<String> dataList = new ArrayList<>();
    private int jam, menit;
    private static final int YOUR_REQUEST_CODE = 123; // Puedes usar cualquier número aquí


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
        // Solicitar permiso para mostrar notificaciones
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, YOUR_REQUEST_CODE);
        }



/*
        // Crear una instancia de NotificationHelper
        NotificacionesHelper notificationHelper = new NotificacionesHelper(this);

        // Llamar a createNotification para mostrar una notificación
        notificationHelper.createNotification("Título de la notificación", "Mensaje de la notificación");
*/


        calendarView = findViewById(R.id.calendarView);
        timePicker = findViewById(R.id.timePicker);
        edtMensaje = findViewById(R.id.edtMensaje);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnClose = findViewById(R.id.btnClose);

        //OBTENER FECHA
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            stringDateSelected = dayOfMonth + "-" + (month + 1) + "-" + year;
            /*calendarClicked();*/
        });
        // OBTENER HORA Y MINUTOS
        Calendar now = Calendar.getInstance();
        jam = now.get(Calendar.HOUR_OF_DAY);
        menit = now.get(Calendar.MINUTE);
        stringHourSelected = String.valueOf(jam) + ":" + String.valueOf(menit);

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                jam = hourOfDay;
                menit = minute;
                stringHourSelected = String.valueOf(jam) + ":" + String.valueOf(menit);

                // Validar que la hora seleccionada no esté en el pasado
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                try {
                    Date selectedDateTime = sdf.parse(stringDateSelected + " " + stringHourSelected);
                    Date currentDateTime = new Date();
                    if (selectedDateTime.before(currentDateTime)) {
                        Toast.makeText(AddRecordatorio.this, "Por favor, Seleccione una Hora Futura", Toast.LENGTH_LONG).show();
                        stringHourSelected = ""; // Resetear la hora seleccionada
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        //BASE DE DATOS FIREBASE
        databaseReference = FirebaseDatabase.getInstance().getReference("Calendar");


        //GUARDAR DATOS
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mensaje = edtMensaje.getText().toString();
                if (mensaje.isEmpty()) {
                    Toast.makeText(AddRecordatorio.this, "Por favor, Ingrese un Recordatorio", Toast.LENGTH_LONG).show();
                    return;
                }
                if (stringDateSelected == null || stringDateSelected.isEmpty()) {
                    Toast.makeText(AddRecordatorio.this, "Por favor, Seleccione una Fecha", Toast.LENGTH_LONG).show();
                    return;
                }
                if (stringHourSelected == null || stringHourSelected.isEmpty()) {
                    Toast.makeText(AddRecordatorio.this, "Por favor, Seleccione una Hora", Toast.LENGTH_LONG).show();
                    return;
                }

                // Validar que la fecha y hora no estén en el pasado
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                try {
                    Date selectedDateTime = sdf.parse(stringDateSelected + " " + stringHourSelected);
                    Date currentDateTime = new Date();
                    if (selectedDateTime.before(currentDateTime)) {
                        Toast.makeText(AddRecordatorio.this, "Por favor, Seleccione una Fecha y Hora Futuras", Toast.LENGTH_LONG).show();
                        return;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                // Si todas las validaciones son exitosas, proceder a guardar los datos
                String id = databaseReference.push().getKey();
                String fecha = stringDateSelected;
                String hora = stringHourSelected;

                Recordatorio recordatorio = new Recordatorio(id, fecha, hora, mensaje);
                databaseReference.child(id).setValue(recordatorio);
                Toast.makeText(AddRecordatorio.this, "Recordatorio Guardado: " + mensaje, Toast.LENGTH_SHORT).show();
                edtMensaje.setText("");

                //Recordatorio activado
                onStart();
                Toast.makeText(AddRecordatorio.this, "Recordatorio Activado: " +  "Fecha: " + fecha + " " + "Hora: " + hora, Toast.LENGTH_LONG).show();
            }
        });

//CAMBIAR DE ACTIVIDAD
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddRecordatorio.this, RecordatorioActivity.class);
                startActivity(intent);
            }
        });



    }

    //MOSTRAR NOTIFICON
    @Override
    protected void onStart() {
        super.onStart();

        // Obtener una referencia a la base de datos firebase
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Calendar");

        // Agrega un listener para obtener los datos
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Recordatorio recordatorio = postSnapshot.getValue(Recordatorio.class);

                    // Aquí tienes los datos del recordatorio
                    String id = recordatorio.getId();
                    String fecha = recordatorio.getFecha();
                    String hora = recordatorio.getHora();
                    String mensaje = recordatorio.getMensaje();

                    // Aquí se puede configurar la alarma con estos datos
                    setAlarm(fecha, hora, mensaje);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Aquí se manejan los errores
            }
        });
    }

//CREAR NOTIFICACION
    private void setAlarm(String fecha, String hora, String mensaje) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        // Crea un Intent para iniciar el BroadcastReceiver cuando se dispare la alarma
        Intent intent = new Intent(this, MyBroadcastReceiver.class);
        intent.putExtra("mensaje", mensaje);
        intent.putExtra("fecha", fecha);
        intent.putExtra("hora", hora);

        // Parseo la fecha y la hora en un objeto Date
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        Date date = null; // Declara 'date' aquí

        try {
            date = sdf.parse(fecha + " " + hora); // Se asigna un valor a 'date' aquí
        } catch (ParseException e) {
            e.printStackTrace();
            // Manejo del error...
        }

        // Comprobar si 'date' es null antes de configurar la alarma
        if (date == null) {
            // Manejo del error...
            return;
        }

        // Crea un PendingIntent
        PendingIntent pendingIntent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
        } else {
            pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }

        // Configura la alarma
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, date.getTime(), pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, date.getTime(), pendingIntent);
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == YOUR_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Los permisos fueron concedidos, puedes continuar con la configuración de la alarma
            } else {
                // Los permisos fueron denegados, debes manejar esta situación
                Toast.makeText(this, "Permiso de notificaciones denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }


   /* private void calendarClicked() {
        databaseReference.child(stringDateSelected).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    String value = snapshot.getValue().toString();
                   // txtViewMensaje.setText(snapshot.getValue().toString());
                    edtMensaje.setText(value);
                    dataList.add(value);


                } else {
                    edtMensaje.setText("no hay recordatorios");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }*/




}