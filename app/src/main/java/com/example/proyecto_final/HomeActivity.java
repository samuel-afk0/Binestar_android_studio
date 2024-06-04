package com.example.proyecto_final;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionButton;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
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
    private Button btnLogout;
    private FirebaseAuth mAuth;
    public double progresoKilometros = 0;
    public  int metaPasos=0, progresoPasos=0;
    public TextView txtvPasos, txtvKilometros, txtEmail;
    public ImageView btnservicioWeb, btnSleep, btnChat, btnCalendar;
    FloatingActionButton BtnAgregarRecordatorio,btnObjetivo, BtnAgregarEntrenamiento, btnAgregarObjetivos, btnInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        txtEmail = findViewById(R.id.txtEmail);

        // Recupera el correo electrónico de las preferencias compartidas
        SharedPreferences sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String email = sharedPref.getString("email", "");

        // Muestra el correo electrónico en el TextView
        txtEmail.setText(email);
        btnLogout = findViewById(R.id.btnLogout);
        mAuth = FirebaseAuth.getInstance();

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // Cierra la actividad actual para que el usuario no pueda volver a ella presionando el botón de retroceso
                Toast.makeText(HomeActivity.this, "Sesion Cerrada", Toast.LENGTH_SHORT).show();
            }
        });

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
                        }else if (item.getItemId() == R.id.perfil) {
                            startActivity(new Intent(HomeActivity.this, PerfilActivity.class));
                            return true;
                        }
                        return false;
                    }
                });

        // Configurar gráficos
        GraficoAnillo = findViewById(R.id.GraficoAnillo);
        GraficoAnilloKilometros = findViewById(R.id.GraficoAnilloKilometros);

        // Iniciar hilo para obtener datos de Firebase
        new Thread(new Runnable() {
            @Override
            public void run() {
                obtenerDatosFirebase();
            }
        }).start();

        btnservicioWeb=findViewById(R.id.btnservicioWeb);
        btnSleep=findViewById(R.id.btnSleep);
        btnChat=findViewById(R.id.btnChat);
        btnCalendar=findViewById(R.id.btnCalendar);
        BtnAgregarRecordatorio=findViewById(R.id.BtnAgregarRecordatorio);
        btnObjetivo=findViewById(R.id.btnObjetivo);
        BtnAgregarEntrenamiento = findViewById(R.id.BtnAgregarEntrenamiento);
        btnAgregarObjetivos = findViewById(R.id.btnAgregarObjetivos);
        btnInfo = findViewById(R.id.btnInfo);
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

        btnAgregarObjetivos.setOnClickListener(new View.OnClickListener() {
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

        // Configurar acciones de los botones flotantes
        btnservicioWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, ImagenesActivity.class);
                startActivity(intent);
            }
        });

        btnSleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, SleepTiempoActivity.class);
                startActivity(intent);
            }
        });

        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, ChatActivity.class);
                startActivity(intent);
            }
        });

        btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, CalendarioActivity.class);
                startActivity(intent);
            }
        });

    }

    private void obtenerDatosFirebase() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("progreso").document("BaBdwWCKVvzfNhCt7MLM");

        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    metaKilometros = documentSnapshot.getLong("metakilometros").intValue();
                    progresoKilometros = documentSnapshot.getDouble("progresokilometros");
                    metaPasos = documentSnapshot.getLong("metapasos").intValue();
                    progresoPasos = documentSnapshot.getLong("progresopasos").intValue();

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

    private void configurarGraficoAnillo() {
        float pasosprogres = ((float) progresoPasos / metaPasos) * 100f;
        float porcentajeRestantePasos= 100f - pasosprogres;

        List<PieEntry> datos = new ArrayList<>();
        datos.add(new PieEntry(pasosprogres, ""));
        datos.add(new PieEntry(porcentajeRestantePasos, ""));

        List<Integer> colores = new ArrayList<>();
        colores.add(getResources().getColor(R.color.colorAccent));
        colores.add(getResources().getColor(android.R.color.transparent));

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
            datosKilometros.add(new PieEntry(porcentajeProgresoKilometros, ""));
            datosKilometros.add(new PieEntry(porcentajeRestanteKilometros, ""));

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
        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInfoDialog();
            }
        });
    }

    private void showInfoDialog() {
        // Crea un diálogo
        Dialog dialog = new Dialog(this);

        // Establece el layout del diálogo
        dialog.setContentView(R.layout.dialog_info);

        // Encuentra el botón de cierre en el layout del diálogo
        Button btnClose = dialog.findViewById(R.id.btnClose);

        // Cierra el diálogo cuando se hace clic en el botón de cierre
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        // Muestra el diálogo
        dialog.show();
    }
}
