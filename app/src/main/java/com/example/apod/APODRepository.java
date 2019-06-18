package com.example.apod;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class APODRepository {
    AstronomyPODApi apodService = RetrofitClientInstance.getRetrofitInstance().create(AstronomyPODApi.class);
    ImageView imageView;
    TextView textView;

    APODRepository(ImageView imageView, TextView txtVw) {
        this.imageView = imageView;
        textView = txtVw;
    }

    void grabImage(Context c) {

        Intent intent = new Intent();
        intent.setAction("com.example.apod");
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        intent.putExtra("req", "Request API:  ressource planetary/apod at "+ new Date());
        c.sendBroadcast(intent);

        Call<PictureOfTheDay> call = apodService.getPictureOFtheDay(BuildConfig.API_KEY);
        call.enqueue(new Callback<PictureOfTheDay>() {
            @Override
            public void onResponse(Call<PictureOfTheDay> call, Response<PictureOfTheDay> response) {
                if (!response.isSuccessful()) {
                    Log.e("info", response + "");
                    Log.e("info", "" + response.code());
                    return;
                }

                PictureOfTheDay pic = response.body();
                downloadImage(pic.getUrl());
                textView.setText(pic.getTitle());
            }

            @Override
            public void onFailure(Call<PictureOfTheDay> call, Throwable t) {
                Log.e("erreur", "failed");
            }
        });
    }

    void downloadImage(String url) {
        new DownloadImageFromInternet(imageView)
                .execute(url);
    }

    private class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        public DownloadImageFromInternet(ImageView imageView) {
            this.imageView = imageView;

        }

        protected Bitmap doInBackground(String... urls) {
            String imageURL = urls[0];
            Bitmap bimage = null;
            try {
                InputStream in = new java.net.URL(imageURL).openStream();
                bimage = BitmapFactory.decodeStream(in);

            } catch (Exception e) {
                Log.e("Error Message", e.getMessage());
                e.printStackTrace();
            }
            return bimage;
        }

        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }
}
