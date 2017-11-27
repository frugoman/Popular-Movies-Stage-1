package com.frusoft.movier.model;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by nfrugoni on 18/11/17.
 */

public class MovieContract {
    private MovieContract(){}

    public static final String AUTHORITY = "com.frusoft.movier.activity";
    public static final Uri     BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_MOVIES = "movies";

    public static final class MovieEntry implements BaseColumns{
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();
        public static final String MOVIES_TABLE_NAME = "Movies";
        public static final String COLUMN_FAV_DATE = "movie_dateFaved";
        public static final String COLUMN_MOVIE_ID = "movie_ID";
        public static final String COLUMN_MOVIE_USER_RATING = "user_rating";
        public static final String COLUMN_MOVIE_NAME = "movie_name";
        public static final String COLUMN_MOVIE_POSTER_URL = "movie_poster";
    }
}
