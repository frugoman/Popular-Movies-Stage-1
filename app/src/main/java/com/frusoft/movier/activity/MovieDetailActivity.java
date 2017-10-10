package com.frusoft.movier.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.frusoft.movier.R;
import com.frusoft.movier.model.Movie;
import com.frusoft.movier.util.AsyncTaskCompletionListener;
import com.frusoft.movier.util.MovieDetailFetcher;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailActivity extends AppCompatActivity {

    public static final String EXTRA_KEY_MOVIE_ID = "movieID";
    @BindView(R.id.pb_loading_indicator)
    ProgressBar mLoadingIndicator;
    @BindView(R.id.tv_details_title)
    TextView mTitleTextView;
    @BindView(R.id.tv_details_original_title)
    TextView mOriginalTitleTextView;
    @BindView(R.id.tv_details_user_rating)
    TextView mUserRatingTextView;
    @BindView(R.id.tv_details_release_date)
    TextView mReleaseDateTextView;
    @BindView(R.id.tv_details_overview)
    TextView mOverviewTextView;
    @BindView(R.id.iv_details_preview_poster)
    ImageView mPosterImageView;
    @BindView(R.id.ll_detail_wrapper)
    ScrollView mLinearLayoutWraper;
    @BindView(R.id.tv_error_message)
    TextView mErrorMessageTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        int movieID = -1;
        if (getIntent().hasExtra(EXTRA_KEY_MOVIE_ID)) {
            movieID = getIntent().getIntExtra(EXTRA_KEY_MOVIE_ID, -1);
        }

        new MovieDetailFetcher(new MovieDetailFetcherOnCompleteListener()).execute(movieID);
    }

    private void setMovieDataIntoViews(Movie movie) {
        String posterPathUrl = movie.getPosterPathUrl();
        Picasso.with(this).load(posterPathUrl).into(mPosterImageView);
        mTitleTextView.setText(movie.getTitle());
        mOriginalTitleTextView.setText(movie.getOriginalTitle());
        mUserRatingTextView.setText(movie.getVoteAverage());
        mReleaseDateTextView.setText(movie.getReleaseDate());
        mOverviewTextView.setText(movie.getOverview());
    }

    private void showErrorMessage() {
        mLinearLayoutWraper.setVisibility(View.INVISIBLE);
        mErrorMessageTextView.setVisibility(View.VISIBLE);
    }

    private void showResultsView() {
        mLinearLayoutWraper.setVisibility(View.VISIBLE);
        mErrorMessageTextView.setVisibility(View.INVISIBLE);
    }

    private class MovieDetailFetcherOnCompleteListener implements AsyncTaskCompletionListener<Movie> {

        @Override
        public void OnFetchPreExecute() {
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        public void OnFetchCompleteWithResult(Movie movie) {
            if (movie == null) {
                showErrorMessage();
            } else {
                showResultsView();
                setMovieDataIntoViews(movie);
            }
            mLoadingIndicator.setVisibility(View.INVISIBLE);
        }
    }
}
