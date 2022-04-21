package com.example.norman_lee.recyclerview;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    CharaAdapter charaAdapter;
    ImageView imageViewAdded;

    DataSource dataSource;

    final String KEY_DATA = "data";
    final String LOGCAT = "RV";
    final String PREF_FILE = "mainsharedpref";
    final int REQUEST_CODE_IMAGE = 1000;

    SharedPreferences mPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //TODO 11.1 Get references to the widgets
        recyclerView = findViewById(R.id.charaRecyclerView);
        imageViewAdded = findViewById(R.id.imageViewAdded);

        //TODO 12.7 Load the Json string from shared Preferences
        mPreferences = getSharedPreferences(PREF_FILE, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPreferences.getString(KEY_DATA, "");
        //TODO 12.8 Initialize your dataSource object with the Json string
        if (json.equals("")) {
            dataSource = new DataSource();
        } else {
            dataSource = gson.fromJson(json, DataSource.class);
        }

        //TODO 11.2 Create your dataSource object by calling Utils.firstLoadImages
        ArrayList<Integer> ids = new ArrayList<>();
        ids.add(R.drawable.bulbasaur);
        ids.add(R.drawable.eevee);
        ids.add(R.drawable.gyrados);
        ids.add(R.drawable.pikachu);
        ids.add(R.drawable.psyduck);
        ids.add(R.drawable.snorlax);
        ids.add(R.drawable.spearow);
        ids.add(R.drawable.squirtle);
        dataSource = Utils.firstLoadImages(MainActivity.this,ids);
        //TODO 11.3 --> Go to CharaAdapter
        //TODO 11.8 Complete the necessary code to initialize your RecyclerView
        charaAdapter = new CharaAdapter(MainActivity.this, dataSource);
        recyclerView.setAdapter(charaAdapter);
        // recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this,3));
        //TODO 12.9 [OPTIONAL] Add code to delete a RecyclerView item upon swiping. See notes for the code.


        //TODO 12.1 Set up an Explicit Intent to DataEntry Activity with startActivityForResult (no coding)
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, DataEntry.class);
                startActivityForResult(intent, REQUEST_CODE_IMAGE);

            }
        });

        Log.i("MainActivity", "onCreate");
    }

    //TODO 12.6 Complete onPause to store the DataSource object in SharedPreferences as a JSON string
    @Override
    protected void onPause(){
        super.onPause();
        SharedPreferences.Editor prefsEditor = mPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(dataSource);
        prefsEditor.putString(KEY_DATA, json);
        prefsEditor.apply();
        Log.i("MainActivity", "onPause");
    }


    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    //TODO 12.5 Write onActivityResult to get the data passed back from DataEntry and add to DataSource object
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if( requestCode == REQUEST_CODE_IMAGE && resultCode == Activity.RESULT_OK){
            String name = data.getStringExtra("name");
            String path = data.getStringExtra("path");
            dataSource.addData(name,path);
            charaAdapter.notifyDataSetChanged();
            imageViewAdded.setImageBitmap(dataSource.getImage(dataSource.getSize()-1));
            Toast.makeText(MainActivity.this, R.string.data_added, Toast.LENGTH_LONG);

        }
        Log.i("MainActivity", "onActivityResult");

    }
}
