package com.lalaland.ecommerce.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.data.models.productDetails.ProductVariation;
import com.lalaland.ecommerce.databinding.ProductVariationItemBinding;
import com.lalaland.ecommerce.helpers.AppUtils;

import java.util.List;


public class ProductVariationAdapter extends RecyclerView.Adapter<ProductVariationAdapter.ProductVariationViewHolder> {

    private Context mContext;
    private List<ProductVariation> mProductVariations;
    private ProductVariationItemBinding productVariationItemBinding;
    private LayoutInflater inflater;
    private ProductVariationListener mProductVariationListener;
    private int selectedPosition;

    public ProductVariationAdapter(Context context, ProductVariationListener productVariationListener, int firstSelected) {
        mContext = context;
        inflater = LayoutInflater.from(context);
        mProductVariationListener = productVariationListener;

        selectedPosition = firstSelected;
    }

    @NonNull
    @Override
    public ProductVariationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        productVariationItemBinding = DataBindingUtil.inflate(inflater, R.layout.product_variation_item, parent, false);
        return new ProductVariationViewHolder(productVariationItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductVariationViewHolder holder, int position) {
        ProductVariation productVariation = mProductVariations.get(position);
        holder.bindHolder(productVariation);
    }

    @Override
    public int getItemCount() {
        return mProductVariations.size();
    }

    public void setData(List<ProductVariation> productVariations) {

        mProductVariations = productVariations;
        notifyDataSetChanged();
    }

    public void onVariationClicked(ProductVariation productVariation) {

        if (AppUtils.toInteger(productVariation.getRemainingQuantity()) > 0) {
            selectedPosition = mProductVariations.indexOf(productVariation);
            mProductVariationListener.onProductVariationClicked(productVariation);
        }

        notifyDataSetChanged();
    }

    class ProductVariationViewHolder extends RecyclerView.ViewHolder {

        ProductVariationItemBinding mProductVariationItemBinding;

        ProductVariationViewHolder(@NonNull ProductVariationItemBinding productVariationItemBinding) {
            super(productVariationItemBinding.getRoot());

            mProductVariationItemBinding = productVariationItemBinding;
        }

        void bindHolder(ProductVariation productVariation) {

            if (AppUtils.toInteger(productVariation.getRemainingQuantity()) > 0) {

                if (selectedPosition == getAdapterPosition()) {

                    mProductVariationItemBinding.tvVariation.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
                    mProductVariationItemBinding.variationContainer.setBackground(mContext.getResources().getDrawable(R.drawable.bg_round_corner_transparent_accent));

                } else {
                    mProductVariationItemBinding.tvVariation.setTextColor(mContext.getResources().getColor(android.R.color.black));
                    mProductVariationItemBinding.variationContainer.setBackground(mContext.getResources().getDrawable(R.drawable.bg_round_corner_transparent));
                }
            } else {

                mProductVariationItemBinding.tvVariation.setTextColor(mContext.getResources().getColor(android.R.color.white));
                mProductVariationItemBinding.variationContainer.setBackground(mContext.getResources().getDrawable(R.drawable.bg_circle_gray));
            }

            mProductVariationItemBinding.setProductVariation(productVariation);
            mProductVariationItemBinding.setAdapter(ProductVariationAdapter.this);
            mProductVariationItemBinding.executePendingBindings();
        }
    }

    public interface ProductVariationListener {
        void onProductVariationClicked(ProductVariation productVariation);
    }
}
