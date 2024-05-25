package com.example.proyecto_final;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
public class ConsejosActivity extends AppCompatActivity {

    private ImageView btnBack;
    private RecyclerView recview;
    private ConsejoAdapter adapter;
    private List<ConsejoModelo> consejoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_consejos);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btnBack = findViewById(R.id.btnBack);
        //REGRESAR A PAGINA HOME
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ConsejosActivity.this, SleepTiempoActivity.class);
                startActivity(intent);
            }
        });
        recview = findViewById(R.id.recview);
        recview.setLayoutManager(new LinearLayoutManager(this));
        consejoList = new ArrayList<>();
        adapter = new ConsejoAdapter(consejoList);
        recview.setAdapter(adapter);

        //OBTENER DATOS DE FIRESTORE CLOUD
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("consejo").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        ConsejoModelo item = document.toObject(ConsejoModelo.class);
                        consejoList.add(item);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Log.d("MainActivity", "Error obteniendo documentos: ", task.getException());
                }
            }
        });
    }
}