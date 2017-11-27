package com.frusoft.movier.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.frusoft.movier.model.Movie;
import com.frusoft.movier.model.MovieContract;

import java.util.ArrayList;
import java.util.List;

import static com.frusoft.movier.model.MovieContract.*;

/**
 * Created by nfrugoni on 27/11/17.
 */

public class MoviesDBUtils {

    public static Uri addMovieToFavorites(Movie movie, Context context) {
        ContentValues values = new ContentValues();
        values.put(MovieEntry.COLUMN_MOVIE_NAME, String.valueOf(movie.getTitle()));
        values.put(MovieEntry.COLUMN_MOVIE_POSTER_URL, String.valueOf(movie.getPosterPathUrl()));
        values.put(MovieEntry.COLUMN_MOVIE_USER_RATING, String.valueOf(movie.getVoteAverage()));
        values.put(MovieEntry.COLUMN_MOVIE_ID, String.valueOf(movie.getId()));
        Uri insert = context.getContentResolver().insert(MovieEntry.CONTENT_URI, values);
        return insert;
    }

    public static int deleteMovieFromFavorites(Movie movie, Context context) {
        return context.getContentResolver().delete(MovieEntry.CONTENT_URI, MovieEntry.COLUMN_MOVIE_ID, new String[]{String.valueOf(movie.getId())});
    }

    public static boolean isMovieInFavorites(Movie movie, Context context) {
        Uri uri = MovieEntry.CONTENT_URI.buildUpon().appendPath(String.valueOf(movie.getId())).build();
        Cursor query = context.getContentResolver().query(uri, null, null, null, null);
        return (query != null && query.getCount() > 0);
    }

    public static List<Movie> getAllMoviesFromFavorites(Context context) {
        List<Movie> movies = new ArrayList<>();
        Uri uri = MovieEntry.CONTENT_URI;
        Cursor query = context.getContentResolver().query(uri, null, null, null, null);
        if (query != null && query.getCount() > 0) {
            while (query.moveToNext()) {
                Movie m = new Movie();
                m.setId(query.getInt(query.getColumnIndex(MovieEntry.COLUMN_MOVIE_ID)));
                m.setTitle(query.getString(query.getColumnIndex(MovieEntry.COLUMN_MOVIE_NAME)));
                m.setVoteAverage(query.getString(query.getColumnIndex(MovieEntry.COLUMN_MOVIE_USER_RATING)));
                m.setPosterPathUrl(query.getString(query.getColumnIndex(MovieEntry.COLUMN_MOVIE_POSTER_URL)));
                movies.add(m);
            }
        }
        return movies;
    }
}
