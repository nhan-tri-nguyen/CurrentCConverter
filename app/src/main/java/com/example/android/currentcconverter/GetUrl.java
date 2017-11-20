package com.example.android.currentcconverter;

import android.os.AsyncTask;
import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by ngtrnhan1205 on 10/24/17.
 */

abstract class GetUrl extends AsyncTask<String, Void, String> {
    OkHttpClient okHttpClient = new OkHttpClient.Builder().build();

    @Override
    final protected String doInBackground(String... urls) {
        Request request = new Request.Builder().url(urls[0]).build();

        try {
            Response response = okHttpClient.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}