package com.frusoft.movier.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.frusoft.movier.R;
import com.frusoft.movier.adapter.MovieDetailReviewsAdapter;
import com.frusoft.movier.adapter.MovieDetailVideosAdapter;
import com.frusoft.movier.databinding.ActivityMovieDetailBinding;
import com.frusoft.movier.model.Movie;
import com.frusoft.movier.model.MovieReview;
import com.frusoft.movier.model.MovieVideo;
import com.frusoft.movier.util.MovieDetailFetcher;
import com.frusoft.movier.util.MoviesDBUtils;
import com.frusoft.movier.util.MoviesNetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.frusoft.movier.util.MovieDetailFetcher.EXTRA_KEY_MOVIE_ID;

public class MovieDetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Movie>, MovieDetailReviewsAdapter.MovieReviewAdapterOnClickHandler, MovieDetailVideosAdapter.MovieVideoAdapterOnClickHandler, MovieDetailFetcher.MovieDetailsLoaderHandler {

    @BindView(R.id.pb_loading_indicator)
    ProgressBar mLoadingIndicator;
    @BindView(R.id.tv_error_message)
    TextView mErrorMessageTextView;

    ActivityMovieDetailBinding mBinding;

    private static final int MOVIE_DETAIL_LOADER_ID = 51;
    private int movieID = -1;
    private Movie mMovieCached;
    private MovieDetailReviewsAdapter reviewsAdapter;
    private MovieDetailVideosAdapter videosAdapter;
    private boolean isMovieFaved;

    @Override
    protected void onResume() {
        super.onResume();

        Bundle aBundle = new Bundle();
        aBundle.putInt(EXTRA_KEY_MOVIE_ID, movieID);

        getSupportLoaderManager().restartLoader(MOVIE_DETAIL_LOADER_ID, aBundle, this);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        if (getIntent().hasExtra(EXTRA_KEY_MOVIE_ID)) {
            movieID = getIntent().getIntExtra(EXTRA_KEY_MOVIE_ID, -1);
        }

        mBinding.fabFavouriteMovieButton.bringToFront();

        mBinding.fabFavouriteMovieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFabButtonClick();
            }

        });

        reviewsAdapter = new MovieDetailReviewsAdapter(this);
        videosAdapter = new MovieDetailVideosAdapter(this);
    }

    private void setMovieDataIntoViews(Movie movie) {
        String posterPathUrl = movie.getPosterPathUrl();
        Picasso.with(this).load(posterPathUrl).into(mBinding.ivDetailsPreviewPoster);
        mBinding.tvDetailsTitle.setText(movie.getTitle());
        mBinding.tvDetailsOriginalTitle.setText(movie.getOriginalTitle());
        mBinding.tvDetailsUserRating.setText(movie.getVoteAverage());
        String releaseDate = movie.getReleaseDate();
        String formatedDate;
        try {
            Date parsedDate = new SimpleDateFormat("yyyy-MM-dd").parse(releaseDate);
            formatedDate = new SimpleDateFormat("MM/dd/yyyy").format(parsedDate);
        } catch (ParseException e) {
            e.printStackTrace();
            formatedDate = releaseDate;
        }
        mBinding.tvDetailsReleaseDate.setText(formatedDate);
        mBinding.tvDetailsOverview.setText(movie.getOverview());

    }

    private void setFavFabState(final boolean isMovieFaved) {
        this.isMovieFaved = isMovieFaved;
        if (isMovieFaved) {

            mBinding.fabFavouriteMovieButton.setImageResource(R.drawable.ic_star_black_24dp);
        } else {
            mBinding.fabFavouriteMovieButton.setImageResource(R.drawable.ic_star_border_black_24dp);
        }
    }

    private void onFabButtonClick() {

        if (mMovieCached != null) {
            if (!isMovieFaved) {
                saveMovieAsFavorite(mMovieCached);
            } else {
                deleteMovieFromFavorites(mMovieCached);
            }
        }
    }

    private void deleteMovieFromFavorites(Movie movie) {
        int delete = MoviesDBUtils.deleteMovieFromFavorites(movie, this);
        if (delete > 0) {
            mBinding.fabFavouriteMovieButton.setImageResource(R.drawable.ic_star_border_black_24dp);
            isMovieFaved = false;
            Toast.makeText(this, R.string.fab_fav_movie_detail_action_delete_toast, Toast.LENGTH_SHORT).show();
        }
    }

    private void saveMovieAsFavorite(Movie movie) {
        Uri insert = MoviesDBUtils.addMovieToFavorites(movie, this);
        if (insert != null) {
            mBinding.fabFavouriteMovieButton.setImageResource(R.drawable.ic_star_black_24dp);
            isMovieFaved = true;
            Toast.makeText(this, R.string.fab_fav_movie_detail_action_add_toast, Toast.LENGTH_SHORT).show();
        }
    }

    private void showReviewsView(List<MovieReview> reviews) {
        LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        if (reviews.size() > 0) {
            mBinding.reviewsTitleLabel.setVisibility(View.VISIBLE);
            mBinding.movieDetailsReviewRv.setVisibility(View.VISIBLE);
            mBinding.movieDetailsReviewRv.setLayoutManager(llm);
            mBinding.movieDetailsReviewRv.setHasFixedSize(true);
            mBinding.movieDetailsReviewRv.setAdapter(reviewsAdapter);
        } else {
            mBinding.reviewsTitleLabel.setVisibility(View.GONE);
            mBinding.movieDetailsReviewRv.setVisibility(View.GONE);
        }
    }

    private void showTrailersView(List<MovieVideo> videos) {
        LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        if (videos.size() > 0) {
            mBinding.videosTitleLabel.setVisibility(View.VISIBLE);
            mBinding.movieDetailsVideosRv.setVisibility(View.VISIBLE);
            mBinding.movieDetailsVideosRv.setLayoutManager(llm);
            mBinding.movieDetailsVideosRv.setHasFixedSize(true);
            mBinding.movieDetailsVideosRv.setAdapter(videosAdapter);
        } else {
            mBinding.videosTitleLabel.setVisibility(View.GONE);
            mBinding.movieDetailsVideosRv.setVisibility(View.GONE);
        }
    }

    private void showErrorMessage() {
        mBinding.llDetailWrapper.setVisibility(View.INVISIBLE);
        mErrorMessageTextView.setVisibility(View.VISIBLE);
    }

    private void showResultsView() {
        mBinding.llDetailWrapper.setVisibility(View.VISIBLE);
        mErrorMessageTextView.setVisibility(View.INVISIBLE);
    }

    @Override
    public Loader<Movie> onCreateLoader(int id, final Bundle args) {

        return new MovieDetailFetcher(this, args, this) {
            @Override
            protected void onStartLoading() {
                super.onStartLoading();

                if (args == null)
                    return;

                reviewsAdapter.setReviewList(null);
                videosAdapter.setMovieVideoList(null);
                mLoadingIndicator.setVisibility(View.VISIBLE);
                mBinding.llDetailWrapper.setVisibility(View.GONE);
                if (mMovieCached != null) {
                    deliverResult(mMovieCached);
                } else {
                    forceLoad();
                }
            }

            @Override
            public void deliverResult(Movie data) {
                if (data != null) {
                    mMovieCached = data;
                }
                super.deliverResult(data);
            }
        };
    }


    @Override
    public void onLoadFinished(Loader<Movie> loader, Movie data) {
        if (data == null) {
            showErrorMessage();
        } else {
            reviewsAdapter.setReviewList(data.getMovieReview());
            videosAdapter.setMovieVideoList(data.getMovieVideos());
            showResultsView();
            setMovieDataIntoViews(data);

        }
        mLoadingIndicator.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<Movie> loader) {
    }


    @Override
    public void onClick(MovieReview movieReview) {

    }

    @Override
    public void onClick(MovieVideo movieVideo) {
        if (movieVideo.getSite().equalsIgnoreCase("youtube")) {
            try {
                Intent ytAppIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + movieVideo.getKey()));
                startActivity(ytAppIntent);
            } catch (ActivityNotFoundException e) {
                Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + movieVideo.getKey()));
                startActivity(webIntent);
            }
        }
    }

    @Override
    public void onMovieReviewsFetched(List<MovieReview> reviews) {
        showReviewsView(reviews);
    }

    @Override
    public void onMovieVideosFetched(List<MovieVideo> videos) {

        showTrailersView(videos);
    }

    @Override
    public void onIsMovieSavedInFavorites(boolean isInFavorites) {

        setFavFabState(isInFavorites);
    }
}
