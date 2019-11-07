package com.danchua.codechallenge.home;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.danchua.codechallenge.R;
import com.danchua.codechallenge.db.entity.Result;

import java.util.ArrayList;
import java.util.List;

public class HomeResultListAdapter extends RecyclerView.Adapter<HomeResultListAdapter.ResultViewHolder> {

    private Context mContext;
    private List<Result> resultList;
    private View.OnClickListener mOnClickListener;

    /**
     *
     * Initialize needed components
     * @param context Application Context
     *
     * */
    public HomeResultListAdapter(Context context, View.OnClickListener onClickListener) {
        this.mContext = context;
        this.mOnClickListener = onClickListener;
        resultList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ResultViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.result_list_item, viewGroup, false);
        return new ResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultViewHolder resultViewHolder, int i) {
        resultViewHolder.bindTo(resultList.get(i));
    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }

    /**
     *
     * Set list for recyclerView
     * @param results List from the API response
     *
     * */
    public void setResultList(List<Result> results) {
        this.resultList = results;
    }

    class ResultViewHolder extends RecyclerView.ViewHolder {

        private CardView cardViewContainer;
        private ImageView imageViewArtWork;
        private TextView textViewTrackName;
        private TextView textViewPrice;
        private TextView textViewGenre;

        public ResultViewHolder(@NonNull View itemView) {
            super(itemView);
            cardViewContainer = itemView.findViewById(R.id.result_list_item_cardivew_container);
            imageViewArtWork = itemView.findViewById(R.id.result_list_item_imageview_artwork);
            textViewTrackName = itemView.findViewById(R.id.result_list_item_textview_trackname);
            textViewPrice = itemView.findViewById(R.id.result_list_item_textview_price);
            textViewGenre = itemView.findViewById(R.id.result_list_item_textview_genre);
        }

        /**
         *
         * Bind value to every item Result
         * @param result Item from result list
         *
         * */
        void bindTo(Result result) {
            Glide.with(mContext)
                    .load(result.getArtworkUrl100())
                    .apply(new RequestOptions().fitCenter())
                    .placeholder(R.drawable.poster_placeholder)
                    .into(imageViewArtWork);

            cardViewContainer.setTag(result);
            cardViewContainer.setOnClickListener(mOnClickListener);

            textViewTrackName.setText(result.getTrackName());
            textViewPrice.setText(String.valueOf(result.getTrackPrice()));
            textViewGenre.setText(result.getPrimaryGenreName());
        }

    }

}
