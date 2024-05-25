package com.example.proyecto_final;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class ImagenesActivity extends AppCompatActivity {
    ListView listView;
    private List<ImagenItem> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_imagenes);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ImageView btnBack = (ImageView) findViewById(R.id.btnBack);
        listView = (ListView) findViewById(R.id.listView);
        items = new ArrayList<>();
        // Agregar imágenes  texto y url
        items.add(new ImagenItem(R.drawable.image1, "CONSEJOS DE SALUD", "https://azsalud.com/"));
        items.add(new ImagenItem(R.drawable.image2, "PLANES DE DIETAS", "https://dimequecomes.com/"));
        items.add(new ImagenItem(R.drawable.image3, "NUTRICION Y PESAS", "https://nutricionypesas.com/"));
        items.add(new ImagenItem(R.drawable.image4, "CONSEJOS FIT", "https://www.mundofitness.com/"));
        items.add(new ImagenItem(R.drawable.image5, "SUPLEMENTOS FIT", "https://www.colosofit.com/"));
        items.add(new ImagenItem(R.drawable.image6, "VIDA FIT", "https://miguelcamarenasalud.com/blog/"));
        items.add(new ImagenItem(R.drawable.image7, "ENTRENA SANO", "https://guiafitness.com/"));
        items.add(new ImagenItem(R.drawable.image8, "MUJER SALUDABLE", "https://www.womenshealthmag.com/es/"));
        items.add(new ImagenItem(R.drawable.image9, "GUIA MUSCULAR", "https://www.musculaciontotal.com/"));
        items.add(new ImagenItem(R.drawable.image10, "ENTRENA CUERPO Y MENTE", "https://www.nationalgeographicla.com/ciencia/2022/06/como-el-ejercicio-fisico-beneficia-al-cerebro"));
        items.add(new ImagenItem(R.drawable.image11, "MEDICAMENTOS Y EJERCICIOS", "https://www.hsnstore.com/blog/salud-y-belleza/buenos-habitos/medicamentos-y-ejercicio/"));
        items.add(new ImagenItem(R.drawable.image12, "HOMBRE SALUDABLE", "https://www.menshealth.com/es/"));

        ImagenAdapter adapter = new ImagenAdapter(this, items);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ImagenesActivity.this, ServicioWebActivity.class);
                intent.putExtra("url", items.get(position).getUrl());
                startActivity(intent);
            }
        });
        //REGRESAR A PAGINA HOME
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ImagenesActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }
}