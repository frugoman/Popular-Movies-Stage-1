package com.frusoft.movier.util;

import android.content.Context;

import com.frusoft.movier.model.Movie;

import org.json.JSONException;

import java.io.IOException;

/**
 * Created by nfrugoni on 9/10/17.
 */

public class MovieDetailFetcher extends BaseFetcherTask<Integer,Void,Movie> {

    public MovieDetailFetcher(AsyncTaskCompletionListener<Movie> completionListener) {
        super(completionListener);
    }

    @Override
    protected Movie doInBackground(Integer... ids) {
        Movie movie = null;
        try {
            Integer id = ids[0];
            if (id == -1) return null;
            movie = MoviesNetworkUtils.getMovieWithId(id);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return movie;
    }
}
