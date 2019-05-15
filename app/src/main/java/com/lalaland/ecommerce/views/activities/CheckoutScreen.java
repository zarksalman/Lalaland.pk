package com.lalaland.ecommerce.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.adapters.CartItemsAdapter;
import com.lalaland.ecommerce.data.models.cart.CartItem;
import com.lalaland.ecommerce.data.models.userAddressBook.UserAddresses;
import com.lalaland.ecommerce.databinding.ActivityCheckoutScreenBinding;
import com.lalaland.ecommerce.helpers.AppConstants;
import com.lalaland.ecommerce.helpers.AppPreference;
import com.lalaland.ecommerce.helpers.AppUtils;
import com.lalaland.ecommerce.viewModels.products.ProductViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.lalaland.ecommerce.helpers.AppConstants.CASH_TRANSFER_TYPE;
import static com.lalaland.ecommerce.helpers.AppConstants.GENERAL_ERROR;
import static com.lalaland.ecommerce.helpers.AppConstants.PAYMENT_LOWEST_LIMIT;
import static com.lalaland.ecommerce.helpers.AppConstants.SIGNIN_TOKEN;
import static com.lalaland.ecommerce.helpers.AppConstants.SUCCESS_CODE;
import static com.lalaland.ecommerce.helpers.AppConstants.VALIDATION_FAIL_CODE;

public class CheckoutScreen extends AppCompatActivity {

    private ActivityCheckoutScreenBinding activityCheckoutScreenBinding;
    private UserAddresses userAddresses;

    private ProductViewModel productViewModel;
    private AppPreference appPreference;
    private List<CartItem> cartItemList = new ArrayList<>();
    private Map<String, String> parameter = new HashMap<>();
    private Map<String, String> headers = new HashMap<>();
    private String token, cart_session;
    String totalBill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityCheckoutScreenBinding = DataBindingUtil.setContentView(this, R.layout.activity_checkout_screen);

        appPreference = AppPreference.getInstance(this);
        userAddresses = AppConstants.userAddresses;
        activityCheckoutScreenBinding.setClickListener(this);

        productViewModel = ViewModelProviders.of(this).get(ProductViewModel.class);
        token = appPreference.getString(SIGNIN_TOKEN);

        cartItemList = getIntent().getParcelableArrayListExtra("ready_cart_items");
        totalBill = getIntent().getStringExtra("total_bill");

        activityCheckoutScreenBinding.tvTotalBalance.setText(AppUtils.formatPriceString(totalBill));

        if (Double.parseDouble(totalBill) >= PAYMENT_LOWEST_LIMIT) {
            activityCheckoutScreenBinding.rbBankTransfer.setChecked(true);
            activityCheckoutScreenBinding.rgPaymentType.setOnCheckedChangeListener(null);
        }


        if (userAddresses == null) {
            activityCheckoutScreenBinding.addUserAddress.setVisibility(View.VISIBLE);
            activityCheckoutScreenBinding.userDetail.setVisibility(View.GONE);
        } else {

            activityCheckoutScreenBinding.tvUserName.setText(userAddresses.getUserName());
            activityCheckoutScreenBinding.tvUserAddress.setText(userAddresses.getShippingAddress());
            activityCheckoutScreenBinding.tvUserCityPostalCode.setText(String.valueOf(userAddresses.getPostalCode()));
            activityCheckoutScreenBinding.tvUserMobile.setText(userAddresses.getPhone());

            activityCheckoutScreenBinding.addUserAddress.setVisibility(View.GONE);
            activityCheckoutScreenBinding.userDetail.setVisibility(View.VISIBLE);
        }

        setListeners();
        setCartAdapter();
    }


    void setListeners() {

        activityCheckoutScreenBinding.rgPaymentType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (R.id.rb_cash_on_delivery == checkedId)
                    CASH_TRANSFER_TYPE = 1;
                else
                    CASH_TRANSFER_TYPE = 2;
            }
        });
    }

    public void addNewAddress(View view) {
        startActivity(new Intent(this, AddressCreationActivity.class));
    }

    void setCartAdapter() {


        activityCheckoutScreenBinding.rvCartProducts.setHasFixedSize(true);
        CartItemsAdapter cartItemsAdapter = new CartItemsAdapter(this, new CartItemsAdapter.CartClickListener() {
            @Override
            public void addItemToList(CartItem cartItem) {

            }

            @Override
            public void deleteFromCart(CartItem cartItem) {

            }

            @Override
            public void changeNumberOfCount(CartItem cartItem, int quantity) {

            }
        });

        activityCheckoutScreenBinding.rvCartProducts.setLayoutManager(new LinearLayoutManager(this));
        activityCheckoutScreenBinding.rvCartProducts.setAdapter(cartItemsAdapter);
        cartItemsAdapter.setData(cartItemList);
    }

    public void placeOrder(View view) {

        if (Double.parseDouble(totalBill) >= PAYMENT_LOWEST_LIMIT) {
            Toast.makeText(this, "You have to pay by bank", Toast.LENGTH_SHORT).show();
            return;
        }

        activityCheckoutScreenBinding.pbLoading.setVisibility(View.VISIBLE);
        parameter.clear();
        parameter.put("name", userAddresses.getUserName());
        parameter.put("phone_no", userAddresses.getPhone());
        parameter.put("email", userAddresses.getEmail());
        parameter.put("city_id", String.valueOf(userAddresses.getCityId()));
        parameter.put("delivery_address", String.valueOf(userAddresses.getShippingAddress()));
        parameter.put("postal_code", String.valueOf(userAddresses.getPostalCode()));
        parameter.put("shipping_method", String.valueOf(0));
        parameter.put("payment_gateway", String.valueOf(CASH_TRANSFER_TYPE));

        productViewModel.confirmOrder(token, parameter).observe(this, orderDataContainer -> {

            if (orderDataContainer != null) {

                if (orderDataContainer.getCode().equals(SUCCESS_CODE))
                    Toast.makeText(this, orderDataContainer.getMsg(), Toast.LENGTH_SHORT).show();
                else if (orderDataContainer.getCode().equals(VALIDATION_FAIL_CODE))
                    Toast.makeText(this, orderDataContainer.getMsg(), Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, GENERAL_ERROR, Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(this, GENERAL_ERROR, Toast.LENGTH_SHORT).show();


            activityCheckoutScreenBinding.pbLoading.setVisibility(View.GONE);
        });
    }
}
