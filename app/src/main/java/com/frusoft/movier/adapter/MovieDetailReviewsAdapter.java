package com.frusoft.movier.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.frusoft.movier.R;
import com.frusoft.movier.databinding.MovieReviewViewBinding;
import com.frusoft.movier.model.MovieReview;

import java.util.List;

/**
 * Created by nfrugoni on 23/11/17.
 */

public class MovieDetailReviewsAdapter extends RecyclerView.Adapter<MovieDetailReviewsAdapter.MovieReviewViewHolder> {

    private final MovieReviewAdapterOnClickHandler onClickHandler;
    private List<MovieReview> reviewList;

    @Override
    public MovieReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_review_view,parent,false);
        return new MovieReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieReviewViewHolder holder, int position) {
        holder.mBinding = DataBindingUtil.bind(holder.itemView);

        holder.mBinding.reviewAuthorTv.setText(reviewList.get(position).getAuthor());
        holder.mBinding.reviewContentTv.setText(reviewList.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        if(reviewList == null) return 0;
        return  reviewList.size();
    }

    public void setReviewList(List<MovieReview> reviewList){
        this.reviewList = reviewList;
        notifyDataSetChanged();
    }

    public interface MovieReviewAdapterOnClickHandler{
        void onClick(MovieReview movieReview);
    }

    public MovieDetailReviewsAdapter(MovieReviewAdapterOnClickHandler onClickHandler) {
        this.onClickHandler = onClickHandler;
    }

    public class MovieReviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        MovieReviewViewBinding mBinding;

        public MovieReviewViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            MovieReview review = reviewList.get(adapterPosition);
            onClickHandler.onClick(review);
        }
    }
}
