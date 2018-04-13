package com.example.usuario.pruebafile;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
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

    private String [] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};

    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
            }
        }

        b = findViewById(R.id.button);
        imageView = findViewById(R.id.imageView2);
        listView = findViewById(R.id.listView);

        loadList();



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
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    //finish();
                }
                return;
            }
        }
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
                loadPicture(Folder_pictures + items.get(position));
                Toast.makeText(getApplicationContext(), Folder_pictures + items.get(position), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void takePicture(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        String pictureName = PREFIJO + dateFormat.format(new Date());
        filepath = Folder_pictures + pictureName + ".jpg";
        Log.i(FOTOS, filepath);
        File myPicture = new File(filepath);

        try{
            myPicture.createNewFile();
            Log.i("PICTURE", filepath);
            Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                // versiones con android 6.0 o superior
                Uri fileUri = FileProvider.getUriForFile(getApplicationContext(),getResources().getString(R.string.file_provider_authority), myPicture);
                i.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                i.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            } else{
                // para versiones anteriores a android 6.0
                Uri uri = Uri.fromFile(myPicture);
                i.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            }
            startActivityForResult(i, REQUEST_CODE);
            loadPicture(filepath);
        } catch (IOException e) {
            Log.i(FOTOS, "No existe " + filepath);
            e.printStackTrace();
        }
    }
}
