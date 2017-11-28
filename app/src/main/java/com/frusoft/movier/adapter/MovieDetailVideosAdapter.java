package com.frusoft.movier.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.frusoft.movier.R;
import com.frusoft.movier.databinding.MovieVideoButtonBinding;
import com.frusoft.movier.model.MovieVideo;

import java.util.List;

/**
 * Created by nfrugoni on 23/11/17.
 */

public class MovieDetailVideosAdapter extends RecyclerView.Adapter<MovieDetailVideosAdapter.MovieVideoViewHolder> {

    private final MovieVideoAdapterOnClickHandler onClickHandler;
    private List<MovieVideo> movieVideoList;

    @Override
    public MovieVideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_video_button, parent, false);
        return new MovieVideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MovieVideoViewHolder holder, int position) {
        holder.mBinding = DataBindingUtil.bind(holder.itemView);
        holder.mBinding.detailShareVideoIb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickHandler.onShareVideoButtonClick(movieVideoList.get(holder.getAdapterPosition()));
            }
        });
        holder.mBinding.videoTitleTv.setText(movieVideoList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        if (movieVideoList == null) return 0;
        return movieVideoList.size();
    }

    public void setMovieVideoList(List<MovieVideo> movieVideoList) {
        this.movieVideoList = movieVideoList;
        notifyDataSetChanged();
    }

    public interface MovieVideoAdapterOnClickHandler {
        void onMovieVideoButtonClick(MovieVideo movieVideo);

        void onShareVideoButtonClick(MovieVideo videoToShare);
    }

    public MovieDetailVideosAdapter(MovieDetailVideosAdapter.MovieVideoAdapterOnClickHandler onClickHandler) {
        this.onClickHandler = onClickHandler;
    }

    public class MovieVideoViewHolder extends RecyclerView.ViewHolder {

        MovieVideoButtonBinding mBinding;

        public MovieVideoViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int adapterPosition = getAdapterPosition();
                    MovieVideo movieVideo = movieVideoList.get(adapterPosition);
                    onClickHandler.onMovieVideoButtonClick(movieVideo);
                }
            });

        }
    }
}
