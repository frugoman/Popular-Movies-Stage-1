package com.frusoft.movier.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.frusoft.movier.R;
import com.frusoft.movier.adapter.MoviesAdapter;
import com.frusoft.movier.model.Movie;
import com.frusoft.movier.model.MovieSortOrder;
import com.frusoft.movier.util.AsyncTaskCompletionListener;
import com.frusoft.movier.util.MoviesArrayFetcher;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.frusoft.movier.util.MovieDetailFetcher.EXTRA_KEY_MOVIE_ID;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.MoviesAdapterOnClickHandler {

    @BindView(R.id.pb_loading_indicator)
    ProgressBar mLoadingIndicator;
    @BindView(R.id.tv_error_message)
    TextView mErrorMessageTextView;
    @BindView(R.id.rv_movies_container)
    RecyclerView mRecyclerView;

    private MoviesAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        GridLayoutManager moviesLayoutManager = new GridLayoutManager(this, numberOfComumns());
        mRecyclerView.setLayoutManager(moviesLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new MoviesAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        new MoviesArrayFetcher(new MoviesFetcherOnCompleteListener(),this).execute(MovieSortOrder.MOST_POPULAR);

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
        new MenuInflater(this).inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_sort_high_rating:
                new MoviesArrayFetcher(new MoviesFetcherOnCompleteListener(),this).execute(MovieSortOrder.HIGH_RATING);
                break;
            case R.id.action_sort_most_popular:
                new MoviesArrayFetcher(new MoviesFetcherOnCompleteListener(),this).execute(MovieSortOrder.MOST_POPULAR);
                break;
            case R.id.action_sort_favorite:
                new MoviesArrayFetcher(new MoviesFetcherOnCompleteListener(),this).execute(MovieSortOrder.FAVORITES);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private int numberOfComumns() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int widthDivider = 400;
        int width = metrics.widthPixels;
        int columnCount = width / widthDivider;
        if (columnCount < 2)
            columnCount = 2;
        return columnCount;
    }

    @Override
    public void onClick(Movie movie) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra(EXTRA_KEY_MOVIE_ID, movie.getId());
        startActivity(intent);
    }

    private class MoviesFetcherOnCompleteListener implements AsyncTaskCompletionListener<List<Movie>> {

        @Override
        public void OnFetchPreExecute() {
            mAdapter.setMoviesList(null);
            showResultsView();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        public void OnFetchCompleteWithResult(List<Movie> movies) {
            if (movies == null) {
                showErrorMessage();
            } else {
                mAdapter.setMoviesList(movies);
            }
            mLoadingIndicator.setVisibility(View.INVISIBLE);
        }
    }
}
