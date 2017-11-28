package com.frusoft.movier.util;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;

import com.frusoft.movier.model.Movie;
import com.frusoft.movier.model.MovieSortOrder;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

/**
 * Created by nfrugoni on 9/10/17.
 */

public class MoviesArrayAsyncTaskLoader extends AsyncTaskLoader<List<Movie>> {

    public static final String EXTRA_KEY_MOVIES_SORT_ORDER = "movieSortOrder";
    private Bundle aBundle;

    public MoviesArrayAsyncTaskLoader(Context context, Bundle bundle) {
        super(context);
        aBundle = bundle;
    }

    @Override
    public List<Movie> loadInBackground() {
        List<Movie> popularMovies = null;
        try {

            MovieSortOrder sort = MovieSortOrder.valueOf(aBundle.getString(EXTRA_KEY_MOVIES_SORT_ORDER));
            if (sort == null)
                sort = MovieSortOrder.MOST_POPULAR;

            if (sort == MovieSortOrder.FAVORITES) {
                popularMovies = MoviesDBUtils.getAllMoviesFromFavorites(getContext());
            } else {
                popularMovies = MoviesNetworkUtils.getMovies(sort);
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return popularMovies;
    }
}
