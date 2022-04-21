package com.example.norman_lee.recyclerview;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;

public class DataEntry extends AppCompatActivity {

    EditText editTextNameEntry;
    Button buttonSelectImage;
    Button buttonOK;
    ImageView imageViewSelected;
    Bitmap bitmap;
    final static int REQUEST_IMAGE_GET = 2000;
    final static String KEY_PATH = "Image";
    final static String KEY_NAME = "Name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_entry);

        editTextNameEntry = findViewById(R.id.editTextNameEntry);
        buttonSelectImage = findViewById(R.id.buttonSelectImage);
        imageViewSelected = findViewById(R.id.imageViewSelected);
        buttonOK = findViewById(R.id.buttonOK);

        //TODO 12.2 Set up an implicit intent to the image gallery (standard code)
        buttonSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, REQUEST_IMAGE_GET);
                }

            }
        });

        //TODO 12.4 When the OK button is clicked, set up an intent to go back to MainActivity
        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int resultCode = Activity.RESULT_OK;
                Intent resultIntent = new Intent();

                String name = editTextNameEntry.getText().toString();
                String path = Utils.saveToInternalStorage(bitmap,name,DataEntry.this);
                resultIntent.putExtra("name", name);
                resultIntent.putExtra("path", path);
                setResult(resultCode, resultIntent);
                finish();
            }
        });

        //TODO 12.5 --> Go back to MainActivity


    }

    //TODO 12.3 Write onActivityResult to get the image selected
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_GET && resultCode == RESULT_OK) {
            Uri fullPhotoUri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), fullPhotoUri);
                imageViewSelected.setImageURI(fullPhotoUri);
            } catch (FileNotFoundException e) {
                Toast.makeText(DataEntry.this,R.string.file_not_found, Toast.LENGTH_LONG);
            } catch (IOException e) {
                Toast.makeText(DataEntry.this,R.string.io_error, Toast.LENGTH_LONG);
            }
        } else {
            Toast.makeText(DataEntry.this,R.string.file_not_found, Toast.LENGTH_LONG);
        }
    }
}
