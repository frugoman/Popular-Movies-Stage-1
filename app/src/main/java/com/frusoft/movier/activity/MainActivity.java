package com.frusoft.movier.activity;

import android.content.Intent;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
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
import com.frusoft.movier.util.MoviesArrayAsyncTaskLoader;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.frusoft.movier.util.MovieDetailAsyncTaskLoader.EXTRA_KEY_MOVIE_ID;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.MoviesAdapterOnClickHandler, LoaderManager.LoaderCallbacks<List<Movie>> {

    @BindView(R.id.pb_loading_indicator)
    ProgressBar mLoadingIndicator;
    @BindView(R.id.tv_error_message)
    TextView mErrorMessageTextView;
    @BindView(R.id.rv_movies_container)
    RecyclerView mRecyclerView;

    private MoviesAdapter mAdapter;
    private static final int MOVIES_ARRAY_LOADER_ID = 951;
    MovieSortOrder selectedSortOrder;

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
        selectedSortOrder = MovieSortOrder.MOST_POPULAR;
    }

    @Override
    protected void onResume() {
        super.onResume();

        Bundle bundle = new Bundle();
        bundle.putString(MoviesArrayAsyncTaskLoader.EXTRA_KEY_MOVIES_SORT_ORDER, selectedSortOrder.toString());
        getSupportLoaderManager().restartLoader(MOVIES_ARRAY_LOADER_ID, bundle, this);
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
        MovieSortOrder sortOrder = null;
        switch (id) {
            case R.id.action_sort_high_rating:
                sortOrder = MovieSortOrder.HIGH_RATING;
                break;
            case R.id.action_sort_most_popular:
                sortOrder = MovieSortOrder.MOST_POPULAR;
                break;
            case R.id.action_sort_favorite:
                sortOrder = MovieSortOrder.FAVORITES;
                break;
        }
        selectedSortOrder = sortOrder;
        Bundle bundle = new Bundle();
        bundle.putString(MoviesArrayAsyncTaskLoader.EXTRA_KEY_MOVIES_SORT_ORDER, selectedSortOrder.toString());
        getSupportLoaderManager().restartLoader(MOVIES_ARRAY_LOADER_ID, bundle, this);

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

    @Override
    public Loader<List<Movie>> onCreateLoader(int id, final Bundle args) {
        return new MoviesArrayAsyncTaskLoader(this, args) {
            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                if (args == null) return;

                mAdapter.setMoviesList(null);
                showResultsView();
                mLoadingIndicator.setVisibility(View.VISIBLE);
                forceLoad();
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> movies) {
        if (movies == null) {
            showErrorMessage();
        } else {
            mAdapter.setMoviesList(movies);
        }
        mLoadingIndicator.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {
        mAdapter.setMoviesList(null);
    }
}
