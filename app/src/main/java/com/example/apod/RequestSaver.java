package com.example.apod;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class RequestSaver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            File resDir = context.getDir("history", Context.MODE_PRIVATE);
            File resFile = new File(resDir, "hisN.txt");


            FileOutputStream os = new FileOutputStream(resFile, true);

            String data = intent.getExtras().getString("req");

            os.write((data+ "\n").getBytes());


            os.flush();
            os.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
