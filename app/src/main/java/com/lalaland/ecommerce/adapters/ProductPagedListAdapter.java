package com.lalaland.ecommerce.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.data.models.products.Product;
import com.lalaland.ecommerce.databinding.ProductPagedItemBinding;

public class ProductPagedListAdapter extends PagedListAdapter<Product, ProductPagedListAdapter.ProductViewHolder> {

    private Context mContext;
    private ProductPagedItemBinding productItemBinding;
    private LayoutInflater inflater;
    private ProductListener mProductListener;

    public ProductPagedListAdapter(Context context, ProductListener productListener) {
        super(DIFF_CALLBACK);
        mContext = context;
        inflater = LayoutInflater.from(context);
        mProductListener = productListener;
    }


    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        productItemBinding = DataBindingUtil.inflate(inflater, R.layout.product_paged_item, parent, false);
        return new ProductViewHolder(productItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {

        Product product = getItem(position);

        if (product != null)
            holder.bindHolder(product);
    }

    private static DiffUtil.ItemCallback<Product> DIFF_CALLBACK = new DiffUtil.ItemCallback<Product>() {
        @Override
        public boolean areItemsTheSame(@NonNull Product oldItem, @NonNull Product newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Product oldItem, @NonNull Product newItem) {

            String oldObject, newObject;

            oldObject = new Gson().toJson(oldItem);
            newObject = new Gson().toJson(newItem);

            return oldObject.equals(newObject);
        }
    };

    public void onProductClicked(View view, Product product) {
        mProductListener.onProductProductClicked(product);
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {

        ProductPagedItemBinding mProductItemBinding;

        ProductViewHolder(@NonNull ProductPagedItemBinding productItemBinding) {
            super(productItemBinding.getRoot());

            mProductItemBinding = productItemBinding;
        }

        void bindHolder(Product product) {
            mProductItemBinding.setProduct(product);
            mProductItemBinding.setAdapter(ProductPagedListAdapter.this);
            mProductItemBinding.executePendingBindings();
        }
    }

    public interface ProductListener {
        void onProductProductClicked(Product product);
    }
}
