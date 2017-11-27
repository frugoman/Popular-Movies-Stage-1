package com.frusoft.movier.util;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.frusoft.movier.BuildConfig;
import com.frusoft.movier.model.Movie;
import com.frusoft.movier.model.MovieSortOrder;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by nfrugoni on 8/10/17.
 */

@SuppressWarnings("DefaultFileTemplate")
public class NetworkUtils {
    protected static final String API_KEY_PARAM = "api_key";
    protected static final String API_KEY_VALUE = BuildConfig.API_KEY;
    protected static final String API_SCHEME = "https";
    protected static final String API_BASE_URL = "api.themoviedb.org";
    protected static final String API_VERSION = "3";
    protected static final String API_IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w342";
    private static final String KEY_TAG = "NetworkUtils";


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
