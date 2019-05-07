package com.lalaland.ecommerce.adapters;

import android.content.Context;
import android.view.LayoutInflater;
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

    public ProductAdapter(Context context) {
        mContext = context;
        inflater = LayoutInflater.from(context);
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
        return mProductList.size();
    }

    public void setData(List<Product> productList) {

        mProductList = productList;
        notifyDataSetChanged();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {

        ProductItemBinding mProductItemBinding;

        ProductViewHolder(@NonNull ProductItemBinding productItemBinding) {
            super(productItemBinding.getRoot());

            mProductItemBinding = productItemBinding;
        }

        void bindHolder(Product product) {
            mProductItemBinding.setProducts(product);
            mProductItemBinding.setAdapter(ProductAdapter.this);
            mProductItemBinding.executePendingBindings();
        }
    }
}
