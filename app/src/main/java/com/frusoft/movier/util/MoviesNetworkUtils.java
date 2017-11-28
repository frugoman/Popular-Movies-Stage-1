package com.frusoft.movier.util;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.frusoft.movier.model.Movie;
import com.frusoft.movier.model.MovieReview;
import com.frusoft.movier.model.MovieSortOrder;
import com.frusoft.movier.model.MovieVideo;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nfrugoni on 13/11/17.
 */

public class MoviesNetworkUtils extends NetworkUtils {

    private static final String KEY_TAG = "MoviesNetworkUtils";

    private static final String API_RESOURCE_MOVIE = "movie";
    private static final String API_RESOURCE_POPULAR = "popular";
    private static final String API_RESOURCE_TOP_RATED = "top_rated";
    private static final String API_RESOURCE_MOVIE_REVIEW = "reviews";
    private static final String API_RESOURCE_MOVIE_TRAILERS = "videos";
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

    @Nullable
    private static URL getMovieVideosUrl(String videoId) {
        final Uri.Builder baseUrl = new Uri.Builder().scheme(API_SCHEME).authority(API_BASE_URL).appendPath(API_VERSION);
        Uri uri = baseUrl.appendPath(API_RESOURCE_MOVIE).appendPath(videoId).appendPath(API_RESOURCE_MOVIE_TRAILERS).appendQueryParameter(API_KEY_PARAM, API_KEY_VALUE).build();
        Log.i(KEY_TAG, uri.toString());
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    private static URL getMovieReviewsUrl(String videoId) {
        final Uri.Builder baseUrl = new Uri.Builder().scheme(API_SCHEME).authority(API_BASE_URL).appendPath(API_VERSION);
        Uri uri = baseUrl.appendPath(API_RESOURCE_MOVIE).appendPath(videoId).appendPath(API_RESOURCE_MOVIE_REVIEW).appendQueryParameter(API_KEY_PARAM, API_KEY_VALUE).build();
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


    private static MovieVideo generateMovieVideoFromJson(JSONObject jsonObject) {
        return new Gson().fromJson(jsonObject.toString(), MovieVideo.class);
    }

    private static MovieReview generateMovieReviewFromJson(JSONObject jsonObject) {
        return new Gson().fromJson(jsonObject.toString(), MovieReview.class);
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

    public static List<MovieVideo> getMovieVideosFormMovie(Movie movie) throws IOException, JSONException {
        List<MovieVideo> videos = new ArrayList<>();
        String responseFromURL = getResponseFromURL(getMovieVideosUrl(String.valueOf(movie.getId())));
        JSONArray jsonArray = new JSONObject(responseFromURL).getJSONArray(RESULT_VARIABLE_NAME_FROM_JSON);
        for (int i = 0; i < jsonArray.length(); i++) {
            MovieVideo video = generateMovieVideoFromJson(jsonArray.getJSONObject(i));
            videos.add(video);
        }
        return videos;
    }

    public static List<MovieReview> getMovieReviewFormMovie(Movie movie) throws IOException, JSONException {
        List<MovieReview> reviews = new ArrayList<>();
        String responseFromURL = getResponseFromURL(getMovieReviewsUrl(String.valueOf(movie.getId())));
        JSONArray jsonArray = new JSONObject(responseFromURL).getJSONArray(RESULT_VARIABLE_NAME_FROM_JSON);
        for (int i = 0; i < jsonArray.length(); i++) {
            MovieReview review = generateMovieReviewFromJson(jsonArray.getJSONObject(i));
            reviews.add(review);
        }
        return reviews;
    }

    public static Movie getMovieWithId(Integer id) throws IOException, JSONException {
        Movie movie;
        String responseFromURL = getResponseFromURL(getMovieURL(id));
        JSONObject jsonObject = new JSONObject(responseFromURL);
        movie = generateMovieFromJson(jsonObject);
        return movie;
    }
}
