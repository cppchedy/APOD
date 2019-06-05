package com.example.apod;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    Button apodBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        apodBtn = (Button) findViewById(R.id.APODbtn);
        apodBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent apodInten = new Intent(MainActivity.this, APODActivity.class);
                startActivity(apodInten);
            }
        });

        Intent saveServiceIntent = new Intent(MainActivity.this, SaveService.class);
        startService(saveServiceIntent);
    }
}
