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
import com.lalaland.ecommerce.data.models.products.Product;
import com.lalaland.ecommerce.databinding.ProductItemBinding;

import java.util.List;


public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private Context mContext;
    private List<Product> mProductList;
    private ProductItemBinding productItemBinding;
    private LayoutInflater inflater;
    private ProductListener mProductListener;

    public ProductAdapter(Context context, ProductListener productListener) {
        mContext = context;
        inflater = LayoutInflater.from(context);
        mProductListener = productListener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        productItemBinding = DataBindingUtil.inflate(inflater, R.layout.product_item, parent, false);
        return new ProductViewHolder(productItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = mProductList.get(position);
        holder.bindHolder(product);
    }

    @Override
    public int getItemCount() {

        if (mProductList != null)
            return mProductList.size();

        return 0;
    }

    public void setData(List<Product> productList) {

        mProductList = productList;
        notifyDataSetChanged();
    }

    public void onProductClicked(View view, Product product) {
        mProductListener.onProductProductClicked(product);
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {

        ProductItemBinding mProductItemBinding;

        ProductViewHolder(@NonNull ProductItemBinding productItemBinding) {
            super(productItemBinding.getRoot());

            mProductItemBinding = productItemBinding;
        }

        void bindHolder(Product product) {

            mProductItemBinding.tvProductActualPrice.setPaintFlags(mProductItemBinding.tvProductActualPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);  // making price for sales

            mProductItemBinding.setProduct(product);
            mProductItemBinding.setAdapter(ProductAdapter.this);
            mProductItemBinding.executePendingBindings();
        }
    }

    public interface ProductListener {
        void onProductProductClicked(Product product);
    }
}
