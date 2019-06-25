package com.lalaland.ecommerce.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.data.models.wishList.WishListProduct;
import com.lalaland.ecommerce.databinding.ProductItemBinding;
import com.lalaland.ecommerce.databinding.WishProductItemBinding;

import java.util.List;


public class WishlistProductAdapter extends RecyclerView.Adapter<WishlistProductAdapter.ProductViewHolder> {

    private Context mContext;
    private List<WishListProduct> mProductList;
    private ProductItemBinding productItemBinding;
    private WishProductItemBinding wishProductItemBinding;
    private LayoutInflater inflater;
    private ProductListener mProductListener;

    public WishlistProductAdapter(Context context, ProductListener productListener) {
        mContext = context;
        inflater = LayoutInflater.from(context);
        mProductListener = productListener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        wishProductItemBinding = DataBindingUtil.inflate(inflater, R.layout.wish_product_item, parent, false);
        return new ProductViewHolder(wishProductItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        WishListProduct product = mProductList.get(position);
        holder.bindHolder(product);
    }

    @Override
    public int getItemCount() {

        if (mProductList != null)
            return mProductList.size();

        return 0;
    }

    public void setData(List<WishListProduct> productList) {

        mProductList = productList;
        notifyDataSetChanged();
    }

    public void onProductClicked(View view, WishListProduct wishListProduct, boolean isDelete) {

        if (mProductList.indexOf(wishListProduct) != RecyclerView.NO_POSITION)
            mProductListener.onProductProductClicked(wishListProduct, isDelete);
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {

        WishProductItemBinding mWishProductItemBinding;

        ProductViewHolder(@NonNull WishProductItemBinding wishProductItemBinding) {
            super(wishProductItemBinding.getRoot());

            mWishProductItemBinding = wishProductItemBinding;
        }

        void bindHolder(WishListProduct wishListProduct) {
            mWishProductItemBinding.setProduct(wishListProduct);
            mWishProductItemBinding.setAdapter(WishlistProductAdapter.this);
            mWishProductItemBinding.executePendingBindings();
        }
    }

    public interface ProductListener {
        void onProductProductClicked(WishListProduct wishListProduct, boolean isDelete);
    }
}
