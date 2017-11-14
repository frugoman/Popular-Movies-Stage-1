package com.frusoft.movier.util;

import com.frusoft.movier.model.Movie;
import com.frusoft.movier.model.MovieSortOrder;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

/**
 * Created by nfrugoni on 9/10/17.
 */

public class MoviesArrayFetcher extends BaseFetcherTask<MovieSortOrder, String, List<Movie>> {

    public MoviesArrayFetcher(AsyncTaskCompletionListener<List<Movie>> completionListener) {
        super(completionListener);
    }

    @Override
    protected List<Movie> doInBackground(MovieSortOrder... params) {
        List<Movie> popularMovies = null;
        try {
            MovieSortOrder sort = params[0];
            if(sort == null)
                sort = MovieSortOrder.MOST_POPULAR;
            popularMovies = MoviesNetworkUtils.getMovies(sort);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return popularMovies;
    }

}
