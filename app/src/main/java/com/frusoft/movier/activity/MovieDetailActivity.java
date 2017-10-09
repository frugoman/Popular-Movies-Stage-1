package com.frusoft.movier.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.frusoft.movier.R;
import com.frusoft.movier.model.Movie;
import com.frusoft.movier.util.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;

public class MovieDetailActivity extends AppCompatActivity {

    public static final String EXTRA_KEY_MOVIE_ID = "movieID";
    private ProgressBar mLoadingIndicator;
    private TextView mTitleTextView;
    private TextView mOriginalTitleTextView;
    private TextView mUserRatingTextView;
    private TextView mReleaseDateTextView;
    private TextView mOverviewTextView;
    private ImageView mPosterImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        int movieID = -1;
        if (getIntent().hasExtra(EXTRA_KEY_MOVIE_ID)) {
            movieID = getIntent().getIntExtra(EXTRA_KEY_MOVIE_ID, -1);
        }

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        mTitleTextView = (TextView) findViewById(R.id.tv_details_title);
        mOriginalTitleTextView = (TextView) findViewById(R.id.tv_details_original_title);
        mUserRatingTextView = (TextView) findViewById(R.id.tv_details_user_rating);
        mReleaseDateTextView = (TextView) findViewById(R.id.tv_details_release_date);
        mOverviewTextView = (TextView) findViewById(R.id.tv_details_overview);
        mPosterImageView = (ImageView) findViewById(R.id.iv_details_preview_poster);

        new MovieDetailFetcher().execute(movieID);
    }

    private void setMovieDataIntoViews(Movie movie) {
        Picasso.with(this).load(movie.getPosterPathUrl()).into(mPosterImageView);
        mTitleTextView.setText(movie.getTitle());
        mOriginalTitleTextView.setText(movie.getOriginalTitle());
        mUserRatingTextView.setText(movie.getVoteAverage());
        mReleaseDateTextView.setText(movie.getReleaseDate());
        mOverviewTextView.setText(movie.getOverview());
    }

    private class MovieDetailFetcher extends AsyncTask<Integer, Void, Movie> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected Movie doInBackground(Integer... ids) {
            Movie movie = null;
            try {
                Integer id = ids[0];
                if (id == -1) return null;
                movie = NetworkUtils.getMovieWithId(id);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return movie;
        }

        @Override
        protected void onPostExecute(Movie movie) {
            super.onPostExecute(movie);
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            setMovieDataIntoViews(movie);
        }

    }
}
