package com.lalaland.ecommerce.views.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.adapters.MyOrderAdapter;
import com.lalaland.ecommerce.data.models.order.Order;
import com.lalaland.ecommerce.databinding.ActivityOrderListingBinding;
import com.lalaland.ecommerce.helpers.AppPreference;
import com.lalaland.ecommerce.viewModels.order.OrderViewModel;

import java.util.ArrayList;
import java.util.List;

import static com.lalaland.ecommerce.helpers.AppConstants.GENERAL_ERROR;
import static com.lalaland.ecommerce.helpers.AppConstants.ORDER_STATUS;
import static com.lalaland.ecommerce.helpers.AppConstants.SIGNIN_TOKEN;
import static com.lalaland.ecommerce.helpers.AppConstants.SUCCESS_CODE;

public class OrderListingActivity extends AppCompatActivity implements MyOrderAdapter.OrderListener {

    private ActivityOrderListingBinding activityOrderListingBinding;
    private OrderViewModel orderViewModel;
    private String orderListType, loginToken;
    private List<Order> ordersList = new ArrayList<>();
    private MyOrderAdapter myOrderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityOrderListingBinding = DataBindingUtil.setContentView(this, R.layout.activity_order_listing);
        orderViewModel = ViewModelProviders.of(this).get(OrderViewModel.class);

        if (getIntent().getExtras() != null) {

            orderListType = getIntent().getStringExtra(ORDER_STATUS);
            loginToken = AppPreference.getInstance(this).getString(SIGNIN_TOKEN);
            setAdapter();
            getOrdersList();
        }
    }

    private void setAdapter() {

        myOrderAdapter = new MyOrderAdapter(this, this);
        activityOrderListingBinding.rvOrder.setHasFixedSize(true);
        activityOrderListingBinding.rvOrder.setLayoutManager(new LinearLayoutManager(this));
        activityOrderListingBinding.rvOrder.setAdapter(myOrderAdapter);
        myOrderAdapter.setData(ordersList);
    }

    private void getOrdersList() {

        orderViewModel.getMyOrders(loginToken, orderListType).observe(this, orderDataContainer -> {

            if (orderDataContainer != null) {
                if (orderDataContainer.getCode().equals(SUCCESS_CODE)) {
                    ordersList.addAll(orderDataContainer.getData().getOrders());
                    myOrderAdapter.notifyDataSetChanged();
                    //myOrderAdapter.notifyItemRangeChanged(0, ordersList.size());
                } else {
                    Toast.makeText(this, GENERAL_ERROR, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onOrderClicked(Order order) {

    }
}
