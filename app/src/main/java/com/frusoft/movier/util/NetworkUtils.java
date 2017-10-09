package com.frusoft.movier.util;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

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
    private static final String API_KEY_PARAM = "api_key";
    private static final String API_KEY_VALUE = "APIKEYHERE";
    private static final String API_SCHEME = "https";
    private static final String API_BASE_URL = "api.themoviedb.org";
    private static final String API_VERSION = "3";
    private static final String API_RESOURCE_MOVIE = "movie";
    private static final String API_RESOURCE_POPULAR = "popular";
    private static final String API_RESOURCE_TOP_RATED = "top_rated";
    private static final String API_IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w342";
    private static final String KEY_TAG = "NetworkUtils";
    private static final String RESULT_VARIABLE_NAME_FROM_JSON = "results";

    private static URL getPopularMoviesURL() {
        return getMoviesUrl(API_RESOURCE_POPULAR);
    }

    private static URL getTopRatedMoviesURL() {
        return getMoviesUrl(API_RESOURCE_TOP_RATED);
    }

    @Nullable
    private static URL getMoviesUrl(String path) {
        final Uri.Builder baseUrl = new Uri.Builder().scheme(API_SCHEME).authority(API_BASE_URL).appendPath(API_VERSION);
        Uri uri = baseUrl.appendPath(API_RESOURCE_MOVIE).appendPath(path).appendQueryParameter(API_KEY_PARAM, API_KEY_VALUE).build();
        Log.i(KEY_TAG, uri.toString());
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    private static URL getMovieURL(int movieId) {
        return getMoviesUrl(String.valueOf(movieId));
    }

    private static Movie generateMovieFromJson(JSONObject json) {
        Movie movie = new Gson().fromJson(json.toString(), Movie.class);
        String fullPosterPath = API_IMAGE_BASE_URL.concat(movie.getPosterPathUrl());
        movie.setPosterPathUrl(fullPosterPath);
        return movie;
    }

    public static List<Movie> getMovies(MovieSortOrder sortOrder) throws IOException, JSONException {
        List<Movie> movies;
        String jsonResponse;
        URL moviesURL = null;
        switch (sortOrder) {
            case MOST_POPULAR:
                moviesURL = getPopularMoviesURL();
                break;
            case HIGH_RATING:
                moviesURL = getTopRatedMoviesURL();
                break;
        }
        jsonResponse = getResponseFromURL(moviesURL);
        JSONArray jsonArray = new JSONObject(jsonResponse).getJSONArray(RESULT_VARIABLE_NAME_FROM_JSON);

        movies = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            Movie movie = generateMovieFromJson(jsonArray.getJSONObject(i));
            movies.add(movie);
        }
        return movies;
    }

    public static Movie getMovieWithId(Integer id) throws IOException, JSONException {
        Movie movie;
        String responseFromURL = getResponseFromURL(getMovieURL(id));
        JSONObject jsonObject = new JSONObject(responseFromURL);
        movie = generateMovieFromJson(jsonObject);
        return movie;
    }

    private static String getResponseFromURL(URL popularMoviesURL) throws IOException {
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
