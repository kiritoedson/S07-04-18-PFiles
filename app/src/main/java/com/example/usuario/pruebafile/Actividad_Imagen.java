package com.example.usuario.pruebafile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

public class Actividad_Imagen extends AppCompatActivity {
    private ImageView imagen;
    Actividad_Tarea tarea;
    String cadena;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad__imagen);

        imagen=findViewById(R.id.imageViewLanzada);

        Intent i=getIntent();
        cadena=i.getStringExtra(Actividad_Tarea.NOTIFICACION);
        Toast.makeText(getApplicationContext(), cadena, Toast.LENGTH_SHORT).show();

        File imgFile = new File(cadena);
        Bitmap bm = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        imagen.setImageBitmap(bm);

    }
}
