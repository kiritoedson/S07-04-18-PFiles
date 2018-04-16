package com.example.usuario.pruebafile;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Actividad_Tarea extends AppCompatActivity {
    private final int REQUEST_CODE = 0;
    private final String FOTOS = "misfotos";
    private final String Folder_pictures = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" + FOTOS + "/";
    private File folder = new File(Folder_pictures);
    private String filepath;

    private ImageView imageView;
    private ListView listView;

    private static final String PREFIJO = "pic_";
    //private static File mFilename = null;
    private List<String> items;

    private String [] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};

    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1 ;

    public static final String NOTIFICACION = "NOTIFICACION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad__tarea);

        imageView = findViewById(R.id.imageViewT);
        listView = findViewById(R.id.listviewT);

        loadList();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    private void loadPicture(String f) {
        File imgFile = new File(f);
        if (imgFile.exists()) {
            Bitmap bm = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            imageView.setImageBitmap(bm);
        }
        Bitmap bm = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        imageView.setImageBitmap(bm);
    }

    private void loadList() {
        items = new ArrayList<String>();
        File[] files = folder.listFiles();
        if (files.length > 0) {
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                if (file.isFile()) {
                    if (file.getName().contains(PREFIJO)) {
                        items.add(file.getName());
                    }
                }
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String dato=Folder_pictures+items.get(position);
                loadPicture(Folder_pictures + items.get(position));
                Toast.makeText(getApplicationContext(), Folder_pictures + items.get(position), Toast.LENGTH_SHORT).show();
                Intent i=new Intent(getApplicationContext(), Actividad_Imagen.class);
                i.putExtra(NOTIFICACION,dato);
                startActivity(i);

            }
        });
    }
}
