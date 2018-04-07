package com.example.usuario.pruebafile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PictureActivity extends AppCompatActivity {
    private final int REQUEST_CODE = 0;
    private final String FOTOS = "misfotos";
    private final String Folder_pictures = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" + FOTOS + "/";
    private File folder = new File(Folder_pictures);
    private String filepath;

    private Button b;
    private ImageView imageView;
    private ListView listView;

    private static final String PREFIJO = "pic_";
    //private static File mFilename = null;
    private List<String> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);

        b = findViewById(R.id.button);
        imageView = findViewById(R.id.imageView2);
        listView = findViewById(R.id.listView);


        if (folder.mkdirs()) {
            loadList();
        }

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == REQUEST_CODE) {
                loadPicture(filepath);
                loadList();
            } else {
                Toast.makeText(getApplicationContext(), "Error al tomar la Foto", Toast.LENGTH_SHORT).show();
            }
        } catch (NullPointerException e) {

        }
    }

    private void loadPicture(String f) {
        File imgFile = new File(f);
        if (imgFile.exists()) {
            Bitmap bm = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            imageView.setImageBitmap(bm);
        }
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
                loadPicture(Folder_pictures + items.get(position));
                Toast.makeText(getApplicationContext(), Folder_pictures + items.get(position), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void takePicture() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHMMSS");
        String pictureName = PREFIJO + dateFormat.format(new Date());
        filepath = Folder_pictures + pictureName + ".jpg";
        File myPicture = new File(filepath);
        try {
            Log.i("Imagen",myPicture.toString());
            myPicture.createNewFile();
            Log.i("PICTURE", filepath);
            Uri uri = Uri.fromFile(myPicture);
            Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            i.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(i, REQUEST_CODE);
            loadPicture(filepath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
