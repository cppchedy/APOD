package com.example.apod;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

public class APODActivity extends AppCompatActivity {
    Button updateBtn;
    Button saveBtn;

    ImageView imgView;

    ProgressDialog progressDoalog;

    BroadcastReceiver receiver;

    SaveService mService;
    boolean mBound = false;
    private Date lastRequestDate;

    private APODRepository apodrepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apod);

        IntentFilter filter = new IntentFilter();
        filter.addAction("com.example.apod");
        receiver = new RequestSaver();
        registerReceiver(receiver, filter);

        imgView = findViewById(R.id.imgAPOD);
        updateBtn = (Button) findViewById(R.id.updateBtn);
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = new Date();
                if(date.getDay() != lastRequestDate.getDay()) {
                    lastRequestDate = date;
                    apodrepo.grabImage(APODActivity.this);
                } else {
                    Log.i("update", "image is the same");
                }
            }
        });
        saveBtn = (Button) findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bmp = ((BitmapDrawable)imgView.getDrawable()).getBitmap();
                new SaveFileTask(mService).execute(bmp);
            }
        });
        /*
        progressDoalog = new ProgressDialog(APODActivity.this);
        progressDoalog.setMessage("Getting url....");
        progressDoalog.show();
        */

        apodrepo = new APODRepository((ImageView)findViewById(R.id.imgAPOD), (TextView)findViewById(R.id.descTxtView));
        apodrepo.grabImage(this);
        lastRequestDate = new Date();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, SaveService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(connection);
        mBound = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            SaveService.LocalBinder binder = (SaveService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    private class SaveFileTask extends AsyncTask<Bitmap, Void, Integer> {
        SaveService mServ;

        public SaveFileTask(SaveService s) {
            this.mServ = s;

        }

        protected Integer doInBackground(Bitmap... urls) {
            Bitmap bmp = urls[0];

            return mServ.saveAPODData(bmp);
        }

        protected void onPostExecute(Integer result) {
            if(result == 0)
                Log.e("info", "error saving file");
            else
                Log.e("info", "file saved successfully");
        }
    }


}

