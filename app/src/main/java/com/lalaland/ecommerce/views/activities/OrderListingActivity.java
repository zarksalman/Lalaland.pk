package com.lalaland.ecommerce.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.adapters.MyOrderAdapter;
import com.lalaland.ecommerce.data.models.order.myOrders.Order;
import com.lalaland.ecommerce.databinding.ActivityOrderListingBinding;
import com.lalaland.ecommerce.helpers.AppPreference;
import com.lalaland.ecommerce.helpers.AppUtils;
import com.lalaland.ecommerce.viewModels.order.OrderViewModel;

import java.util.ArrayList;
import java.util.List;

import static com.lalaland.ecommerce.helpers.AppConstants.GENERAL_ERROR;
import static com.lalaland.ecommerce.helpers.AppConstants.ORDER_ADDRESS;
import static com.lalaland.ecommerce.helpers.AppConstants.ORDER_DATE;
import static com.lalaland.ecommerce.helpers.AppConstants.ORDER_ID;
import static com.lalaland.ecommerce.helpers.AppConstants.ORDER_MERCHANT;
import static com.lalaland.ecommerce.helpers.AppConstants.ORDER_STATUS;
import static com.lalaland.ecommerce.helpers.AppConstants.ORDER_TOTAL;
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

            activityOrderListingBinding.tvTitle.setText(AppUtils.toLowerCase(orderListType.concat(" ")).concat("Orders"));
            loginToken = AppPreference.getInstance(this).getString(SIGNIN_TOKEN);

            setInitialValues();
            setAdapter();
            getOrdersList();
        }

        activityOrderListingBinding.btnBack.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    private void setInitialValues() {
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
                    myOrderAdapter.notifyItemRangeInserted(0, ordersList.size());

                } else {
                    Toast.makeText(this, GENERAL_ERROR, Toast.LENGTH_SHORT).show();
                }
            }

            activityOrderListingBinding.progressBar.setVisibility(View.GONE);
        });
    }

    @Override
    public void onOrderClicked(Order order) {

        Intent intent = new Intent(this, OrderDetailActivity.class);
        intent.putExtra(ORDER_ID, String.valueOf(order.getOrderId()));
        intent.putExtra(ORDER_DATE, order.getCreatedAt());
        intent.putExtra(ORDER_MERCHANT, order.getMerchantName());
        intent.putExtra(ORDER_MERCHANT, order.getMerchantName());
        intent.putExtra(ORDER_ADDRESS, order.getDeliveryAddress());
        intent.putExtra(ORDER_TOTAL, order.getGrandTotal());

        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
