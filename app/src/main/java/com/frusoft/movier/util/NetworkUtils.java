package com.frusoft.movier.util;

import android.util.Log;

import com.frusoft.movier.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by nfrugoni on 8/10/17.
 */

@SuppressWarnings("DefaultFileTemplate")
public class NetworkUtils {
    public static final String API_KEY_PARAM = "api_key";
    public static final String API_KEY_VALUE = BuildConfig.API_KEY;
    public static final String API_SCHEME = "https";
    public static final String API_BASE_URL = "api.themoviedb.org";
    public static final String API_VERSION = "3";
    public static final String API_IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w342";
    public static final String KEY_TAG = "NetworkUtils";


    protected static String getResponseFromURL(URL popularMoviesURL) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) popularMoviesURL.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                String response = scanner.next();
                Log.i(KEY_TAG, response);
                return response;
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
