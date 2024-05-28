package com.example.proyecto_final;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private ImageView btnBack;
    private LinearLayout chatLayout;
    private Spinner questionSpinner;
    private Button sendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        db = FirebaseFirestore.getInstance();
        chatLayout = findViewById(R.id.chat);
        questionSpinner = findViewById(R.id.question_spinner);
        sendButton = findViewById(R.id.send_button);
        btnBack = findViewById(R.id.btnBack);

        // Agregar preguntas predeterminadas
        List<String> questions = Arrays.asList(
                "Seleccione una pregunta...",
                "¿Cómo reducir lapresión sanguínea?",
                "¿Qué es la dieta“keto”?",
                "¿Cómo combatir elhipo?",
                "¿Qué causa elhipo?",
                "¿Cómo reducir elcolesterol?",
                "¿Cuántas caloríasdebo consumir diariamente?",
                "¿Qué causa la tensión arterial baja?",
                "¿Es bueno tomar una siesta durante el día?",
                "¿Qué factores ocasionan el insomnio?",
                "¿Cuánto sueño es suficiente?",
                "Horas recomendadas de sueño",
                "¿Por qué es importante el sueño?"
        );

        // Configurar el Spinner con las preguntas predeterminadas
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, questions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        questionSpinner.setAdapter(adapter);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String question = questionSpinner.getSelectedItem().toString();

                // Cuando el usuario envía una pregunta
                LinearLayout userMessageLayout = new LinearLayout(ChatActivity.this);
                userMessageLayout.setOrientation(LinearLayout.HORIZONTAL);
                userMessageLayout.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));

                ImageView userIcon = new ImageView(ChatActivity.this);
                userIcon.setLayoutParams(new LinearLayout.LayoutParams(
                        200, // Ancho en píxeles
                        200  // Alto en píxeles
                ));
                Glide.with(ChatActivity.this).asGif().load(R.drawable.chat).into(userIcon);
                userMessageLayout.addView(userIcon);

                TextView userMessage = new TextView(ChatActivity.this);
                userMessage.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                userMessage.setText(question + "\n");
                userMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                userMessageLayout.addView(userMessage);

                chatLayout.addView(userMessageLayout);


                // Obtener la respuesta de Firestore
                db.collection("chatSalud")
                        .whereEqualTo("pregunta", question)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String answer = document.getString("respuesta");
                                        if (answer != null) { // Verificar si la respuesta no es nula
                                            // Cuando recibe respuesta del bot
                                            LinearLayout botMessageLayout = new LinearLayout(ChatActivity.this);
                                            botMessageLayout.setOrientation(LinearLayout.HORIZONTAL);

                                            ImageView botIcon = new ImageView(ChatActivity.this);
                                            // Ajusta el tamaño de la imagen aquí
                                            LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(200, 200);
                                            botIcon.setLayoutParams(imageParams);
                                            Glide.with(ChatActivity.this).load(R.drawable.chatbot).into(botIcon);
                                            botMessageLayout.addView(botIcon);

                                            TextView botMessage = new TextView(ChatActivity.this);
                                            botMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                                            botMessage.setText(answer + "\n");
                                            LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                                    LinearLayout.LayoutParams.WRAP_CONTENT);
                                            textParams.gravity = Gravity.CENTER_VERTICAL;
                                            botMessage.setLayoutParams(textParams);
                                            botMessageLayout.addView(botMessage);

                                            chatLayout.addView(botMessageLayout);

                                        } else {
                                            Log.w("Firestore", "No se encontró una respuesta para la pregunta: " + question);
                                        }
                                    }
                                } else {
                                    Log.w("Firestore", "Error getting documents.", task.getException());
                                }
                            }
                        });
            }
        });
        //REGRESAR A HOME
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }
}
