package com.example.proyecto_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private EditText edtCorreo;
    private EditText edtContra;
    private Button btnlogin;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtCorreo = findViewById(R.id.edtCorreo);
        edtContra = findViewById(R.id.edtContra);
        btnlogin = findViewById(R.id.btnlogin);
        mAuth = FirebaseAuth.getInstance();

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
    }

    private void loginUser() {
        String email = edtCorreo.getText().toString().trim();
        String password = edtContra.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            edtCorreo.setError("Email es requerido");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            edtContra.setError("La contraseña es requerida");
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                    } else {
                        Toast.makeText(MainActivity.this, "Autentificacion fallida",
                                Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            // Almacena el correo electrónico en las preferencias compartidas
            SharedPreferences sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("email", user.getEmail());
            editor.apply();
            // Navegar a HomeActivity
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            //intent.putExtra("email", user.getEmail()); // Agrega el correo electrónico al Intent
            startActivity(intent);
            finish(); // Cierra la actividad actual para que el usuario no pueda volver a ella presionando el botón de retroceso
            Toast.makeText(MainActivity.this, "Inicio exitoso.", Toast.LENGTH_SHORT).show();
        } else {
            // Mostrar un mensaje de error
            Toast.makeText(MainActivity.this, "La autentificacion fallo, intentelo de nuevo.", Toast.LENGTH_SHORT).show();
        }
    }
}
