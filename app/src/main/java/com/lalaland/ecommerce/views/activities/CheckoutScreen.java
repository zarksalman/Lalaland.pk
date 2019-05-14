package com.lalaland.ecommerce.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import static com.lalaland.ecommerce.helpers.AppConstants.SIGNIN_TOKEN;
import static com.lalaland.ecommerce.helpers.AppConstants.SUCCESS_CODE;
import static com.lalaland.ecommerce.helpers.AppConstants.TAG;

public class CheckoutScreen extends AppCompatActivity {

    private ActivityCheckoutScreenBinding activityCheckoutScreenBinding;
    private UserAddresses userAddresses;

    private ProductViewModel productViewModel;
    private AppPreference appPreference;
    private List<CartItem> cartItemList = new ArrayList<>();
    private Map<String, String> parameter = new HashMap<>();
    private Map<String, String> headers = new HashMap<>();
    private String token, cart_session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityCheckoutScreenBinding = DataBindingUtil.setContentView(this, R.layout.activity_checkout_screen);

        appPreference = AppPreference.getInstance(this);
        userAddresses = AppConstants.userAddresses;
        activityCheckoutScreenBinding.setClickListener(this);

        productViewModel = ViewModelProviders.of(this).get(ProductViewModel.class);
        token = appPreference.getString(SIGNIN_TOKEN);

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
        setCartSelectedProducts();
        getCartItems();
    }

    private void setCartSelectedProducts() {

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


    private void getCartItems() {

        headers.clear();

        if (!token.isEmpty()) {

            headers.put(SIGNIN_TOKEN, token);

            productViewModel.getCart(headers).observe(this, cartContainer -> {

                if (cartContainer != null) {

                    if (cartContainer.getCode().equals(SUCCESS_CODE)) {
                        cartItemList = cartContainer.getData().getCartItems();
                        AppConstants.userAddresses = cartContainer.getData().getUserAddresses();
                        setCartAdapter();
                        activityCheckoutScreenBinding.tvTotalBalance.setText(AppUtils.formatPriceString(calculateTotalBill()));
                        Log.d(TAG, String.valueOf(cartContainer.getData().getCartItems().size()));
                    } else
                        Toast.makeText(this, GENERAL_ERROR, Toast.LENGTH_SHORT).show();
                }

            });
        }
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

    private String calculateTotalBill() {

        Double count = 0.0;

        for (CartItem cartItem : cartItemList) {
            count += Double.parseDouble(cartItem.getSalePrice());
        }

        return String.valueOf(count);
    }
}
