package com.frusoft.movier.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.sax.StartElementListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.frusoft.movier.R;
import com.frusoft.movier.adapter.MoviesAdapter;
import com.frusoft.movier.model.Movie;
import com.frusoft.movier.model.MovieSortOrder;
import com.frusoft.movier.util.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.MoviesAdapterOnClickHandler{

    ProgressBar mLoadingIndicator;
    TextView mErrorMessageTextView;
    RecyclerView mRecyclerView;
    MoviesAdapter mAdapter;
    MovieSortOrder sortOrderSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_movies_container);
        mErrorMessageTextView = (TextView) findViewById(R.id.tv_error_message);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);


        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new MoviesAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        sortOrderSelected = MovieSortOrder.MOST_POPULAR;
        new MovieFetcher().execute();

    }

    private void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageTextView.setVisibility(View.VISIBLE);
    }

    private void showResultsView() {
        mRecyclerView.setVisibility(View.VISIBLE);
        mErrorMessageTextView.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_sort_high_rating:
                sortOrderSelected = MovieSortOrder.HIGH_RATING;
                new MovieFetcher().execute();
                break;
            case R.id.action_sort_most_popular:
                sortOrderSelected = MovieSortOrder.MOST_POPULAR;
                new MovieFetcher().execute();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(Movie movie) {
        Intent intent = new Intent(this,MovieDetailActivity.class);
        intent.putExtra(MovieDetailActivity.EXTRA_KEY_MOVIE_ID,movie.getId());
        startActivity(intent);
    }

    private class MovieFetcher extends AsyncTask<Void, Void, List<Movie>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mAdapter.setMoviesList(null);
            showResultsView();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Movie> doInBackground(Void... voids) {
            List<Movie> popularMovies = null;
            try {
                popularMovies = NetworkUtils.getMovies(sortOrderSelected);
            } catch (IOException e) {
                e.printStackTrace();
                showErrorMessage();
            } catch (JSONException e) {
                e.printStackTrace();
                showErrorMessage();
            }
            return popularMovies;
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            super.onPostExecute(movies);

            mAdapter.setMoviesList(movies);
            mLoadingIndicator.setVisibility(View.INVISIBLE);
        }
    }
}
