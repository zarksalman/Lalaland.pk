package com.lalaland.ecommerce.adapters;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.data.models.productDetails.LinkedProduct;
import com.lalaland.ecommerce.databinding.ProductColorItemBinding;

import java.util.ArrayList;
import java.util.List;


public class ProductColorAdapter extends RecyclerView.Adapter<ProductColorAdapter.ProductColorViewHolder> {

    private Context mContext;
    private List<LinkedProduct> mLinkedProducts = new ArrayList<>();
    private ProductColorItemBinding mProductColorItemBinding;
    private LayoutInflater inflater;
    private ProductColorListener mProductColorListener;
    float width = 0;

    public ProductColorAdapter(Context context, ProductColorListener productColorListener) {
        mContext = context;
        inflater = LayoutInflater.from(context);
        mProductColorListener = productColorListener;
        width = getScreenWidth();
    }

    @NonNull
    @Override
    public ProductColorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        mProductColorItemBinding = DataBindingUtil.inflate(inflater, R.layout.product_color_item, parent, false);

        mProductColorItemBinding.innerCatParent.getLayoutParams().width = (int) (width / 4.8);
        return new ProductColorViewHolder(mProductColorItemBinding);
    }

    public int getScreenWidth() {

        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        return size.x;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductColorViewHolder holder, int position) {
        LinkedProduct linkedProduct = mLinkedProducts.get(position);
        holder.bindHolder(linkedProduct);
    }

    @Override
    public int getItemCount() {
        return mLinkedProducts.size();
    }

    public void setData(List<LinkedProduct> linkedProducts) {

        mLinkedProducts = linkedProducts;
        notifyDataSetChanged();
    }

    public void onProductColorClicked(LinkedProduct linkedProduct) {
        mProductColorListener.onProductColorClicked(linkedProduct);
    }


    class ProductColorViewHolder extends RecyclerView.ViewHolder {

        ProductColorItemBinding mProductColorItemBinding;

        ProductColorViewHolder(@NonNull ProductColorItemBinding productColorItemBinding) {
            super(productColorItemBinding.getRoot());
            mProductColorItemBinding = productColorItemBinding;
        }

        void bindHolder(LinkedProduct linkedProduct) {

            mProductColorItemBinding.setLinkedProduct(linkedProduct);
            mProductColorItemBinding.setAdapter(ProductColorAdapter.this);
            mProductColorItemBinding.executePendingBindings();
        }
    }

    public interface ProductColorListener {
        void onProductColorClicked(LinkedProduct linkedProduct);
    }
}
