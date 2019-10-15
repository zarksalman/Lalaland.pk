package com.lalaland.ecommerce.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.data.models.order.details.OrderProduct;
import com.lalaland.ecommerce.databinding.OrderProductItemBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MyOrderProductsAdapter extends RecyclerView.Adapter<MyOrderProductsAdapter.MyOrderProductViewHolder> {

    private Context mContext;
    private List<OrderProduct> mOrdersProducts = new ArrayList<>();
    private OrderProductItemBinding orderProductItemBinding;
    private LayoutInflater inflater;
    private ClickClaim mClickClaim;

    public MyOrderProductsAdapter(Context context, ClickClaim clickClaim) {
        mContext = context;
        inflater = LayoutInflater.from(context);
        mClickClaim = clickClaim;
    }

    @NonNull
    @Override
    public MyOrderProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        orderProductItemBinding = DataBindingUtil.inflate(inflater, R.layout.order_product_item, parent, false);
        return new MyOrderProductViewHolder(orderProductItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyOrderProductViewHolder holder, int position) {
        OrderProduct orderProduct = mOrdersProducts.get(position);
        holder.bindHolder(orderProduct);
    }

    @Override
    public int getItemCount() {
        return mOrdersProducts.size();
    }

    public void setData(List<OrderProduct> orderProducts) {

        mOrdersProducts = orderProducts;
        notifyDataSetChanged();
    }


    class MyOrderProductViewHolder extends RecyclerView.ViewHolder {

        OrderProductItemBinding mOrderProductItemBinding;
        String pVariation;
        String[] variation;
        String[] variationTitle;
        String[] variationValue;

        Pattern pattern = Pattern.compile("(||---||)");
        Matcher matcher;

        MyOrderProductViewHolder(@NonNull OrderProductItemBinding orderProductItemBinding) {
            super(orderProductItemBinding.getRoot());

            mOrderProductItemBinding = orderProductItemBinding;
        }

        void bindHolder(OrderProduct orderProduct) {

            pVariation = orderProduct.getProductVariationDescription();
            variation = pVariation.split("[|]", 6);

            variationTitle = variation[0].split(",");

            if (variation.length > 0)
                variationValue = variation[variation.length - 1].split(",");

            if (variationTitle[0].equalsIgnoreCase("Size")) {
                mOrderProductItemBinding.tvProductSize.setText(variationValue[0]);
            } else {
                mOrderProductItemBinding.tvProductSize.setText(variationValue[1]);
            }

            if (orderProduct.getIsClaim().equals("1")) {

                mOrderProductItemBinding.btnClaim.setVisibility(View.VISIBLE);

                mOrderProductItemBinding.btnClaim.setOnClickListener(v -> {
                    mClickClaim.claimClicked(String.valueOf(orderProduct.getOrderProductId()));
                });

            } else if (orderProduct.getIsClaim().equals("2")) {
                mOrderProductItemBinding.btnClaimed.setVisibility(View.VISIBLE);
            } else {
                mOrderProductItemBinding.btnClaim.setVisibility(View.GONE);
                mOrderProductItemBinding.btnClaimed.setVisibility(View.GONE);
            }

            mOrderProductItemBinding.setOrderproduct(orderProduct);
            mOrderProductItemBinding.setAdapter(MyOrderProductsAdapter.this);
            mOrderProductItemBinding.executePendingBindings();
        }
    }

    public interface ClickClaim {
        void claimClicked(String orderProductId);
    }
}
