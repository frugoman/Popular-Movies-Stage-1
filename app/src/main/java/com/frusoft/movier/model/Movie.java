package com.frusoft.movier.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by nfrugoni on 8/10/17.
 */

@SuppressWarnings({"DefaultFileTemplate", "unused", "CanBeFinal"})
public class Movie {
    private int id;
    private String title;
    @SerializedName("original_title")
    private String originalTitle;
    @SerializedName("release_date")
    private String releaseDate;
    @SerializedName("vote_average")
    private String voteAverage;
    private String overview;
    @SerializedName("poster_path")
    private String posterPathUrl;
    private Double popularity;
    private List<MovieVideo> movieVideo;
    private List<MovieReview> movieReview;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public List<MovieVideo> getMovieVideos() {
        return movieVideo;
    }

    public void setMovieVideos(List<MovieVideo> movieVideo) {
        this.movieVideo = movieVideo;
    }

    public List<MovieReview> getMovieReview() {
        return movieReview;
    }

    public void setMovieReviews(List<MovieReview> movieReview) {
        this.movieReview = movieReview;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPathUrl() {
        return posterPathUrl;
    }

    public void setPosterPathUrl(String posterPathUrl) {
        this.posterPathUrl = posterPathUrl;
    }
}
