package com.lalaland.ecommerce.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.data.models.home.Recommendation;
import com.lalaland.ecommerce.databinding.RecommendationItemBinding;

import java.util.List;


public class RecommendationAdapter extends RecyclerView.Adapter<RecommendationAdapter.RecommendationViewHolder> {

    private Context mContext;
    private List<Recommendation> mRecommendation;
    private RecommendationItemBinding recommendationItemBinding;
    protected RecommendationProductListener mRecommendationProductListener;
    private LayoutInflater inflater;

    public RecommendationAdapter(Context context, RecommendationProductListener recommendationProductListener) {
        mContext = context;
        mRecommendationProductListener = recommendationProductListener;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecommendationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        recommendationItemBinding = DataBindingUtil.inflate(inflater, R.layout.recommendation_item, parent, false);
        return new RecommendationViewHolder(recommendationItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecommendationViewHolder holder, int position) {
        Recommendation recommendation = mRecommendation.get(position);
        holder.bindHolder(recommendation);
    }

    @Override
    public int getItemCount() {
        return mRecommendation.size();
    }

    public void setData(List<Recommendation> recommendationList) {

        mRecommendation = recommendationList;
        notifyDataSetChanged();
    }

    public void onRProductClicked(View view, Recommendation recommendation) {
        mRecommendationProductListener.onRecommendationProductClicked(recommendation);
    }

    class RecommendationViewHolder extends RecyclerView.ViewHolder {

        RecommendationViewHolder(@NonNull RecommendationItemBinding recommendationItemBinding) {
            super(recommendationItemBinding.getRoot());
        }

        void bindHolder(Recommendation recommendation) {

            recommendationItemBinding.tvProductActualPrice.setPaintFlags(recommendationItemBinding.tvProductActualPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);  // making price for sales
            recommendationItemBinding.setRecommendation(recommendation);
            recommendationItemBinding.setAdapter(RecommendationAdapter.this);
            recommendationItemBinding.executePendingBindings();
        }
    }

    public interface RecommendationProductListener {
        void onRecommendationProductClicked(Recommendation recommendation);
    }
}
