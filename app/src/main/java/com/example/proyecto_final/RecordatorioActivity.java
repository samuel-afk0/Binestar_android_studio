package com.example.proyecto_final;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RecordatorioActivity extends AppCompatActivity {
    private RecyclerView recordatorioRecyclerView;

    private ImageView buttonAddRecordatorio, btnClose;

    private List<Recordatorio> recordatorios;
    private RecordatorioAdapter adapter;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recordatorio);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        buttonAddRecordatorio = findViewById(R.id.buttonAddRecordatorio);
        btnClose = findViewById(R.id.btnClose);
        recordatorioRecyclerView = findViewById(R.id.recordatorioRecyclerView);
        recordatorioRecyclerView.setHasFixedSize(true);
        recordatorioRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Cargar la base de datos en el recyclerview
        recordatorios = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("Calendar");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                recordatorios.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Recordatorio recordatorio = postSnapshot.getValue(Recordatorio.class);
                    recordatorios.add(recordatorio);
                }
                RecordatorioAdapter adapter = new RecordatorioAdapter(RecordatorioActivity.this, recordatorios);
                recordatorioRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Manejo de errores
            }
        });
        // Boton abrir actividad agregar recordatorio
        buttonAddRecordatorio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecordatorioActivity.this, AddRecordatorio.class);
                startActivity(intent);
            }
        });
        //Boton cerrar actividad recordatorios
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecordatorioActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }
}