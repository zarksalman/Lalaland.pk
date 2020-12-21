package com.lalaland.ecommerce.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.adapters.MyOrderProductsAdapter;
import com.lalaland.ecommerce.data.models.order.details.OrderProduct;
import com.lalaland.ecommerce.databinding.ActivityOrderDetailBinding;
import com.lalaland.ecommerce.helpers.AppPreference;
import com.lalaland.ecommerce.helpers.AppUtils;
import com.lalaland.ecommerce.viewModels.order.OrderViewModel;
import com.lalaland.ecommerce.viewModels.returnAndReplacement.ReturnAndReplacementViewModel;

import java.util.ArrayList;
import java.util.List;

import static com.lalaland.ecommerce.helpers.AppConstants.FANCY_ORDER_ID;
import static com.lalaland.ecommerce.helpers.AppConstants.ORDER_ADDRESS;
import static com.lalaland.ecommerce.helpers.AppConstants.ORDER_DATE;
import static com.lalaland.ecommerce.helpers.AppConstants.ORDER_ID;
import static com.lalaland.ecommerce.helpers.AppConstants.ORDER_MERCHANT;
import static com.lalaland.ecommerce.helpers.AppConstants.ORDER_TOTAL;
import static com.lalaland.ecommerce.helpers.AppConstants.PHONE_NUMBER;
import static com.lalaland.ecommerce.helpers.AppConstants.SIGNIN_TOKEN;
import static com.lalaland.ecommerce.helpers.AppConstants.SUCCESS_CODE;
import static com.lalaland.ecommerce.helpers.AppConstants.USER_NAME;

public class OrderDetailActivity extends AppCompatActivity implements MyOrderProductsAdapter.ClickClaim {

    private ActivityOrderDetailBinding activityOrderDetailBinding;
    private String fancyOrderId, orderId, orderDate, orderMerchant, orderAddress, orderTotal, orderTotal1, token, discountAmount, shippingCharges;
    private OrderViewModel orderViewModel;
    private List<OrderProduct> orderProductList = new ArrayList<>();
    private MyOrderProductsAdapter myOrderProductsAdapter;
    private AppPreference appPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityOrderDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_order_detail);

        if (getIntent().getExtras() != null) {
            fancyOrderId = getIntent().getStringExtra(FANCY_ORDER_ID);
            orderId = getIntent().getStringExtra(ORDER_ID);
            orderDate = getIntent().getStringExtra(ORDER_DATE);
            orderMerchant = getIntent().getStringExtra(ORDER_MERCHANT);
            orderAddress = getIntent().getStringExtra(ORDER_ADDRESS);
            orderTotal = getIntent().getStringExtra(ORDER_TOTAL);
        }

        appPreference = AppPreference.getInstance(this);
        token = AppPreference.getInstance(this).getString(SIGNIN_TOKEN);
        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);

        activityOrderDetailBinding.btnBack.setOnClickListener(v -> {
            onBackPressed();
        });

        //  setInitValues();
        setAdapter();
        getOrderProducts();
    }

    private void setInitValues() {
        activityOrderDetailBinding.tvUserName.setText(appPreference.getString(USER_NAME));
        activityOrderDetailBinding.tvUserPhone.setText(appPreference.getString(PHONE_NUMBER));
        activityOrderDetailBinding.tvUserAddress.setText(orderAddress);

        activityOrderDetailBinding.tvOrderId.setText(String.format(getResources().getString(R.string.order_id_format), fancyOrderId));
        activityOrderDetailBinding.tvOrderDate.setText(String.format(getResources().getString(R.string.order_date_format), orderDate));
        activityOrderDetailBinding.tvOrderMerchant.setText(String.format(getResources().getString(R.string.order_merchant_format), orderMerchant));
        activityOrderDetailBinding.tvOrderTotal.setText(AppUtils.formatPriceString(orderTotal));
    }

    private void setAdapter() {
        myOrderProductsAdapter = new MyOrderProductsAdapter(this, this);
        myOrderProductsAdapter.setData(orderProductList);

        activityOrderDetailBinding.rvOrderProducts.setHasFixedSize(true);
        activityOrderDetailBinding.rvOrderProducts.setAdapter(myOrderProductsAdapter);
        activityOrderDetailBinding.rvOrderProducts.setLayoutManager(new LinearLayoutManager(this));

    }

    private void getOrderProducts() {

        orderViewModel.getMyOrdersProducts(token, orderId).observe(this, orderDetailContainer -> {

            if (orderDetailContainer != null) {

                if (orderDetailContainer.getCode().equals(SUCCESS_CODE)) {

                    String orderBill, disAmount = "-";
                    Double orderTotal = Double.parseDouble(orderDetailContainer.getData().getOrderTotal());
                    Double discountAmount = Double.parseDouble(orderDetailContainer.getData().getDiscountAmount());
                    disAmount = disAmount + discountAmount;

                    shippingCharges = orderDetailContainer.getData().getShippingCharges();
                    Double diffAmount = orderTotal - discountAmount;


                    if (diffAmount <= 0)
                        diffAmount = 0.0;

                    diffAmount += Double.parseDouble(shippingCharges);
                    orderBill = String.valueOf(diffAmount);

                    orderProductList.addAll(orderDetailContainer.getData().getOrderProducts());
                    myOrderProductsAdapter.notifyItemRangeInserted(0, orderProductList.size());

/*                    discountAmount = orderDetailContainer.getData().getDiscountAmount();
                    activityOrderDetailBinding.tvMerchantDiscount.setText(discountAmount);
                    activityOrderDetailBinding.discountRow.setVisibility(View.VISIBLE);*/

                    activityOrderDetailBinding.tvOrderTotal.setText(AppUtils.formatPriceString(orderBill));
                    activityOrderDetailBinding.tvMerchantOrderTotal.setText(AppUtils.formatPriceString(String.valueOf(orderTotal)));
                    activityOrderDetailBinding.tvMerchantDeliveryCharges.setText(AppUtils.formatPriceString(shippingCharges));
                    disAmount = AppUtils.formatPriceString(disAmount);

                    activityOrderDetailBinding.tvMerchantDiscount.setText(disAmount);
                    activityOrderDetailBinding.tvMerchantTotal.setText(AppUtils.formatPriceString(orderBill));


                    if (discountAmount > 0)
                        activityOrderDetailBinding.discountRow.setVisibility(View.VISIBLE);


                    setInitValues();
                    activityOrderDetailBinding.nsvParent.setVisibility(View.VISIBLE);
                }
            }

            activityOrderDetailBinding.pbLoading.setVisibility(View.GONE);
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void claimClicked(String orderProductId) {

        Intent claimIntent = new Intent(this, ReturnAndReplacementActivity.class);
        claimIntent.putExtra("order_product_id", orderProductId);
        startActivity(claimIntent);
    }
}
