package com.lalaland.ecommerce.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.data.models.products.Product;
import com.lalaland.ecommerce.databinding.ProductLayoutBinding;

import java.util.List;


public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private Context mContext;
    private List<Product> mProductList;
    private ProductLayoutBinding productLayoutBinding;
    private LayoutInflater inflater;

    public ProductAdapter(Context context) {
        mContext = context;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        productLayoutBinding = DataBindingUtil.inflate(inflater, R.layout.product_layout, parent, false);
        return new ProductViewHolder(productLayoutBinding);
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

        ProductViewHolder(@NonNull ProductLayoutBinding productLayoutBinding) {
            super(productLayoutBinding.getRoot());
        }

        void bindHolder(Product product) {
            productLayoutBinding.setProducts(product);
            productLayoutBinding.setAdapter(ProductAdapter.this);
            productLayoutBinding.executePendingBindings();
        }
    }
}
