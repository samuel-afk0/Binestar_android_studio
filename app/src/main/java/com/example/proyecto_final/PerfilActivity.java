package com.example.proyecto_final;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class PerfilActivity extends AppCompatActivity {
    private TextView txtEmail;
    private Button btnBack;
    private TextView textviewNOMBRE, textviewOCUPACION, textviewEDAD, textviewGENERO, textviewCM, textviewKG, textviewIMC;
    private ImageView user_imageview;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_perfil);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        txtEmail = findViewById(R.id.txtEmail);

        // Recupera el correo electrónico de las preferencias compartidas
        SharedPreferences sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String email = sharedPref.getString("email", "");

        // Muestra el correo electrónico en el TextView
        txtEmail.setText(email);
        // Recupera los datos del usuario desde la base de datos Firestore
        textviewNOMBRE = findViewById(R.id.textviewNOMBRE);
        user_imageview = findViewById(R.id.user_imageview);
        textviewOCUPACION = findViewById(R.id.textviewOCUPACION);
        textviewEDAD = findViewById(R.id.textviewEDAD);
        textviewGENERO = findViewById(R.id.textviewGENERO);
        textviewCM = findViewById(R.id.textviewCM);
        textviewKG = findViewById(R.id.textviewKG);
        textviewIMC = findViewById(R.id.textviewIMC);
        btnBack = findViewById(R.id.btnBack);
        // Consulta a Firestore para obtener los datos del usuario
        db.collection("perfiles").document(email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                // Actualiza los TextViews con los datos del usuario
                                textviewNOMBRE.setText(document.getString("nombre"));
                                textviewOCUPACION.setText(document.getString("ocupacion"));
                                textviewEDAD.setText(document.getString("edad"));
                                textviewGENERO.setText(document.getString("genero"));
                                textviewCM.setText(document.getString("cm"));
                                textviewKG.setText(document.getString("kg"));
                                textviewIMC.setText(document.getString("imc"));

                                // Carga la imagen del usuario desde la URL almacenada en Firestore
                                String imageUrl = document.getString("imagen");
                                if (imageUrl != null && !imageUrl.isEmpty()) {
                                    Glide.with(PerfilActivity.this)
                                            .load(imageUrl)
                                            .into(user_imageview);
                                }
                            } else {
                                Log.d("Firestore", "No hay datos del usuario");
                            }
                        } else {
                            Log.d("Firestore", "error al obtener datos ", task.getException());
                        }
                    }
                });
        //REGRESAR A PAGINA HOME
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PerfilActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }
}