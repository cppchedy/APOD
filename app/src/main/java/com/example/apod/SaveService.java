package com.example.apod;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Random;

public class SaveService extends Service {
    public SaveService() {
    }

    private final IBinder binder = new LocalBinder();


    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        SaveService getService() {
            // Return this instance of LocalService so clients can call public methods
            return SaveService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    /** method for clients */
    public int saveAPODData(Bitmap bmp) {

        String filename = "" + new Date();
        FileOutputStream out;
        try  {
            out = openFileOutput(filename, Context.MODE_PRIVATE);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
            File[] files =  getFilesDir().listFiles();
            for(File fl: files)
                Log.i("info", fl.getPath());
            out.flush();
            out.close();
            return 1;
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
