package com.lalaland.ecommerce.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.data.models.productDetails.ProductReview;
import com.lalaland.ecommerce.databinding.ReviewItemBinding;
import com.lalaland.ecommerce.helpers.AppConstants;

import java.util.ArrayList;
import java.util.List;

import static com.lalaland.ecommerce.helpers.AppConstants.USER_STORAGE_BASE_URL;


public class ReviewRatingAdapter extends RecyclerView.Adapter<ReviewRatingAdapter.ProductreviewViewHolder> {

    private Context mContext;
    private List<ProductReview> mProductReviews = new ArrayList<>();
    private ReviewItemBinding reviewItemBinding;
    private LayoutInflater inflater;

    public ReviewRatingAdapter(Context context) {
        mContext = context;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ProductreviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        reviewItemBinding = DataBindingUtil.inflate(inflater, R.layout.review_item, parent, false);
        return new ProductreviewViewHolder(reviewItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductreviewViewHolder holder, int position) {
        ProductReview productReview = mProductReviews.get(position);
        holder.bindHolder(productReview);
    }

    @Override
    public int getItemCount() {
        return mProductReviews.size();
    }

    public void setData(List<ProductReview> productReviews) {

        mProductReviews = productReviews;

        notifyDataSetChanged();
    }


    class ProductreviewViewHolder extends RecyclerView.ViewHolder {

        ReviewItemBinding mReviewItemBinding;

        ProductreviewViewHolder(@NonNull ReviewItemBinding reviewItemBinding) {
            super(reviewItemBinding.getRoot());

            mReviewItemBinding = reviewItemBinding;
        }

        void bindHolder(ProductReview productReview) {

            String imageSrc = USER_STORAGE_BASE_URL.concat(productReview.getAvatar());

            Glide.with(AppConstants.mContext)
                    .load(imageSrc)
                    .placeholder(R.drawable.placeholder_products)
                    .into(mReviewItemBinding.ivDisplayPicture);


            mReviewItemBinding.setReview(productReview);
            mReviewItemBinding.setAdapter(ReviewRatingAdapter.this);
            mReviewItemBinding.executePendingBindings();
        }
    }
}
