package com.lalaland.ecommerce.views.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.adapters.MyOrderProductsAdapter;
import com.lalaland.ecommerce.data.models.order.details.OrderProduct;
import com.lalaland.ecommerce.databinding.ActivityOrderDetailBinding;
import com.lalaland.ecommerce.helpers.AppPreference;
import com.lalaland.ecommerce.helpers.AppUtils;
import com.lalaland.ecommerce.viewModels.order.OrderViewModel;

import java.util.ArrayList;
import java.util.List;

import static com.lalaland.ecommerce.helpers.AppConstants.ORDER_ADDRESS;
import static com.lalaland.ecommerce.helpers.AppConstants.ORDER_DATE;
import static com.lalaland.ecommerce.helpers.AppConstants.ORDER_ID;
import static com.lalaland.ecommerce.helpers.AppConstants.ORDER_MERCHANT;
import static com.lalaland.ecommerce.helpers.AppConstants.ORDER_TOTAL;
import static com.lalaland.ecommerce.helpers.AppConstants.PHONE_NUMBER;
import static com.lalaland.ecommerce.helpers.AppConstants.SIGNIN_TOKEN;
import static com.lalaland.ecommerce.helpers.AppConstants.SUCCESS_CODE;
import static com.lalaland.ecommerce.helpers.AppConstants.USER_NAME;

public class OrderDetailActivity extends AppCompatActivity {

    private ActivityOrderDetailBinding activityOrderDetailBinding;
    private String orderId, orderDate, orderMerchant, orderAddress, orderTotal, token;
    private OrderViewModel orderViewModel;
    private List<OrderProduct> orderProductList = new ArrayList<>();
    private MyOrderProductsAdapter myOrderProductsAdapter;
    private AppPreference appPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityOrderDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_order_detail);

        if (getIntent().getExtras() != null) {
            orderId = getIntent().getStringExtra(ORDER_ID);
            orderDate = getIntent().getStringExtra(ORDER_DATE);
            orderMerchant = getIntent().getStringExtra(ORDER_MERCHANT);
            orderAddress = getIntent().getStringExtra(ORDER_ADDRESS);
            orderTotal = getIntent().getStringExtra(ORDER_TOTAL);
        }

        appPreference = AppPreference.getInstance(this);
        token = AppPreference.getInstance(this).getString(SIGNIN_TOKEN);
        orderViewModel = ViewModelProviders.of(this).get(OrderViewModel.class);

        setInitValues();
        setAdapter();
        getOrderProducts();
    }

    private void setInitValues() {

        activityOrderDetailBinding.tvUserName.setText(appPreference.getString(USER_NAME));
        activityOrderDetailBinding.tvUserPhone.setText(appPreference.getString(PHONE_NUMBER));
        activityOrderDetailBinding.tvUserAddress.setText(orderAddress);

        activityOrderDetailBinding.tvOrderId.setText(String.format(getResources().getString(R.string.order_id_format), orderId));
        activityOrderDetailBinding.tvOrderDate.setText(String.format(getResources().getString(R.string.order_date_format), orderDate));
        activityOrderDetailBinding.tvOrderMerchant.setText(String.format(getResources().getString(R.string.order_merchant_format), orderMerchant));
        activityOrderDetailBinding.tvOrderTotal.setText(AppUtils.formatPriceString(orderTotal));
    }

    private void setAdapter() {
        myOrderProductsAdapter = new MyOrderProductsAdapter(this);
        myOrderProductsAdapter.setData(orderProductList);

        activityOrderDetailBinding.rvOrderProducts.setHasFixedSize(true);
        activityOrderDetailBinding.rvOrderProducts.setAdapter(myOrderProductsAdapter);
        activityOrderDetailBinding.rvOrderProducts.setLayoutManager(new LinearLayoutManager(this));

    }

    private void getOrderProducts() {

        orderViewModel.getMyOrdersProducts(token, orderId).observe(this, orderDetailContainer -> {

            if (orderDetailContainer != null) {

                if (orderDetailContainer.getCode().equals(SUCCESS_CODE)) {
                    orderProductList.addAll(orderDetailContainer.getData().getOrderProducts());
                    myOrderProductsAdapter.notifyItemRangeInserted(0, orderProductList.size());
                }
            }
        });
    }
}
