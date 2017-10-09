package com.frusoft.movier.model;

import com.google.gson.annotations.SerializedName;

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
