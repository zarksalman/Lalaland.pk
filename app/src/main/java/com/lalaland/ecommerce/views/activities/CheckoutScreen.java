package com.lalaland.ecommerce.views.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
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
import static com.lalaland.ecommerce.helpers.AppConstants.ORDER_TOTAL;
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
    private boolean isUserAddressNull = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityCheckoutScreenBinding = DataBindingUtil.setContentView(this, R.layout.activity_checkout_screen);

        appPreference = AppPreference.getInstance(this);
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

        isUserAddressExist();

        setListeners();
        setCartAdapter();
    }


    void setListeners() {

        activityCheckoutScreenBinding.rgPaymentType.setOnCheckedChangeListener((group, checkedId) -> {

            if (R.id.rb_cash_on_delivery == checkedId && Double.parseDouble(totalBill) <= PAYMENT_LOWEST_LIMIT)
                CASH_TRANSFER_TYPE = 1;
            else {
                CASH_TRANSFER_TYPE = 2;
                activityCheckoutScreenBinding.rbCashOnDelivery.setChecked(false);
                activityCheckoutScreenBinding.rbBankTransfer.setChecked(true);
            }
        });

        activityCheckoutScreenBinding.ivCloseCheckoutScreen.setOnClickListener(v -> {
            finish();
        });
    }

    void isUserAddressExist() {
        userAddresses = AppConstants.userAddresses;

        if (userAddresses == null) {
            activityCheckoutScreenBinding.addUserAddress.setVisibility(View.VISIBLE);
            activityCheckoutScreenBinding.userDetail.setVisibility(View.GONE);
            activityCheckoutScreenBinding.btnCheckout.setBackgroundColor(getResources().getColor(R.color.colorLightGray));
            isUserAddressNull = true;
        } else {

            activityCheckoutScreenBinding.tvUserName.setText(userAddresses.getUserNameAddress());
            activityCheckoutScreenBinding.tvUserAddress.setText(userAddresses.getShippingAddress());
            activityCheckoutScreenBinding.tvUserCityPostalCode.setText(String.valueOf(userAddresses.getPostalCode()));
            activityCheckoutScreenBinding.tvUserMobile.setText(userAddresses.getPhone());

            activityCheckoutScreenBinding.btnCheckout.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            activityCheckoutScreenBinding.addUserAddress.setVisibility(View.GONE);
            activityCheckoutScreenBinding.userDetail.setVisibility(View.VISIBLE);


            isUserAddressNull = false;
        }
    }
    public void addNewAddress(View view) {
        startActivityForResult(new Intent(this, AddressCreationActivity.class), 1);
    }

    void setCartAdapter() {


        activityCheckoutScreenBinding.rvCartProducts.setHasFixedSize(true);
        CartItemsAdapter cartItemsAdapter = new CartItemsAdapter(this, new CartItemsAdapter.CartClickListener() {
            @Override
            public void addItemToList(int merchantId, int position) {

            }

            @Override
            public void deleteFromCart(int merchantId, int position) {

            }

            @Override
            public void changeNumberOfCount(int merchantId, int position, int quantity) {

            }
        });

        activityCheckoutScreenBinding.rvCartProducts.setLayoutManager(new LinearLayoutManager(this));
        activityCheckoutScreenBinding.rvCartProducts.setAdapter(cartItemsAdapter);
        cartItemsAdapter.setData(cartItemList);
    }

    public void placeOrder() {

       /* if (Double.parseDouble(totalBill) >= PAYMENT_LOWEST_LIMIT || CASH_TRANSFER_TYPE == 2) {
            Toast.makeText(this, "You have to pay by bank", Toast.LENGTH_SHORT).show();
            return;
        }*/

        if (isUserAddressNull) {
            Toast.makeText(this, "Please add address to place order", Toast.LENGTH_SHORT).show();
            return;
        }

        if (userAddresses.getPhone() == null || userAddresses.getPhone().isEmpty()) {
            Intent intent = new Intent(this, AccountInformationActivity.class);
            startActivityForResult(intent, Integer.parseInt(SUCCESS_CODE));
            activityCheckoutScreenBinding.pbLoading.setVisibility(View.GONE);
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

                if (orderDataContainer.getCode().equals(SUCCESS_CODE)) {
                    Toast.makeText(this, orderDataContainer.getMsg(), Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(this, OrderReceivedActivity.class);
                    intent.putExtra(ORDER_TOTAL, String.valueOf(totalBill));
                    CASH_TRANSFER_TYPE = 1;
                    intent.putParcelableArrayListExtra("recommended_products", (ArrayList<? extends Parcelable>) orderDataContainer.getData().getRecommendation());
                    startActivity(intent);
                    finish();
                } else if (orderDataContainer.getCode().equals(VALIDATION_FAIL_CODE) || orderDataContainer.getCode().equals("403"))
                    Toast.makeText(this, orderDataContainer.getMsg(), Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, GENERAL_ERROR, Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(this, GENERAL_ERROR, Toast.LENGTH_SHORT).show();


            activityCheckoutScreenBinding.pbLoading.setVisibility(View.GONE);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Integer.parseInt(SUCCESS_CODE)) {
                isUserAddressExist();
            } else if (requestCode == 1) {
                isUserAddressExist();
            }
        } else {
            Toast.makeText(this, "You should fill required fields, to place order", Toast.LENGTH_SHORT).show();
        }
    }
}
