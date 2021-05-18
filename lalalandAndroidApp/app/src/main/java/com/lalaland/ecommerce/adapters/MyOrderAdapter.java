package com.lalaland.ecommerce.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.data.models.order.myOrders.Order;
import com.lalaland.ecommerce.databinding.OrderItemBinding;

import java.util.ArrayList;
import java.util.List;


public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.MyOrderViewHolder> {

    private Context mContext;
    private List<Order> mOrders = new ArrayList<>();
    private OrderItemBinding orderItemBinding;
    private LayoutInflater inflater;
    private OrderListener mOrderListener;

    public MyOrderAdapter(Context context, OrderListener orderListener) {
        mContext = context;
        inflater = LayoutInflater.from(context);
        mOrderListener = orderListener;
    }

    @NonNull
    @Override
    public MyOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        orderItemBinding = DataBindingUtil.inflate(inflater, R.layout.order_item, parent, false);
        return new MyOrderViewHolder(orderItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyOrderViewHolder holder, int position) {
        Order order = mOrders.get(position);
        holder.bindHolder(order);
    }

    @Override
    public int getItemCount() {
        return mOrders.size();
    }

    public void setData(List<Order> orderList) {

        mOrders = orderList;
        notifyDataSetChanged();
    }

    public void onMyOrderClicked(View view, Order order) {
        mOrderListener.onOrderClicked(order);
    }


    class MyOrderViewHolder extends RecyclerView.ViewHolder {

        OrderItemBinding mOrderItemBinding;

        MyOrderViewHolder(@NonNull OrderItemBinding orderItemBinding) {
            super(orderItemBinding.getRoot());

            mOrderItemBinding = orderItemBinding;
        }

        void bindHolder(Order order) {

  /*          mOrderItemBinding.tvOrderId.setText(order.getOrderId());
            mOrderItemBinding.tvOrderDate.setText(order.getCreatedAt());
            mOrderItemBinding.tvOrderMerchant.setText(order.getMerchantName());
            mOrderItemBinding.tvOrderTotal.setText(order.getGrandTotal());*/

            Double discountAmount = Double.parseDouble(order.getDiscountAmount());
            Double orderTotal = Double.parseDouble(order.getOrderTotal());
            Double diffAmount = orderTotal - discountAmount;
            String orderBill;

            if (diffAmount <= 0) {
                diffAmount = 0.0;
            }

            diffAmount += Double.parseDouble(order.getShippingCharges());
            orderBill = String.valueOf(diffAmount);
            order.setGrandTotal(orderBill);

            mOrderItemBinding.setOrder(order);
            mOrderItemBinding.setAdapter(MyOrderAdapter.this);
            mOrderItemBinding.executePendingBindings();
        }
    }

    public interface OrderListener {
        void onOrderClicked(Order order);
    }
}
