package com.lalaland.ecommerce.views.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Parcelable;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.adapters.CartIMerchantAdapter;
import com.lalaland.ecommerce.adapters.CartItemsAdapter;
import com.lalaland.ecommerce.data.models.DeliveryChargesData.DeliveryChargesOfMerchantItem;
import com.lalaland.ecommerce.data.models.cart.CartItem;
import com.lalaland.ecommerce.data.models.cartListingModel.CartListModel;
import com.lalaland.ecommerce.data.models.userAddressBook.UserAddresses;
import com.lalaland.ecommerce.databinding.ActivityCheckoutScreenBinding;
import com.lalaland.ecommerce.databinding.AddAddressDialogueBinding;
import com.lalaland.ecommerce.databinding.DeleteOutOfStockDialogueBinding;
import com.lalaland.ecommerce.databinding.OtpDialogueBinding;
import com.lalaland.ecommerce.databinding.PhoneNumberDialogueBinding;
import com.lalaland.ecommerce.databinding.VouhcerDialogueBinding;
import com.lalaland.ecommerce.helpers.AnalyticsManager;
import com.lalaland.ecommerce.helpers.AppConstants;
import com.lalaland.ecommerce.helpers.AppPreference;
import com.lalaland.ecommerce.helpers.AppUtils;
import com.lalaland.ecommerce.interfaces.NetworkInterface;
import com.lalaland.ecommerce.viewModels.order.OrderViewModel;
import com.lalaland.ecommerce.viewModels.products.ProductViewModel;
import com.lalaland.ecommerce.viewModels.user.UserViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.lalaland.ecommerce.helpers.AppConstants.CART_COUNTER;
import static com.lalaland.ecommerce.helpers.AppConstants.CASH_TRANSFER_TYPE;
import static com.lalaland.ecommerce.helpers.AppConstants.DATE_OF_BIRTH;
import static com.lalaland.ecommerce.helpers.AppConstants.GENDER;
import static com.lalaland.ecommerce.helpers.AppConstants.GENERAL_ERROR;
import static com.lalaland.ecommerce.helpers.AppConstants.IS_COUPON_APPLIED;
import static com.lalaland.ecommerce.helpers.AppConstants.ORDER_TOTAL;
import static com.lalaland.ecommerce.helpers.AppConstants.OUT_OF_STOCK_CODE;
import static com.lalaland.ecommerce.helpers.AppConstants.PAYMENT_LOWEST_LIMIT;
import static com.lalaland.ecommerce.helpers.AppConstants.PHONE_NUMBER;
import static com.lalaland.ecommerce.helpers.AppConstants.SIGNIN_TOKEN;
import static com.lalaland.ecommerce.helpers.AppConstants.SUCCESS_CODE;
import static com.lalaland.ecommerce.helpers.AppConstants.USER_NAME;
import static com.lalaland.ecommerce.helpers.AppConstants.VALIDATION_FAIL_CODE;
import static com.lalaland.ecommerce.helpers.AppConstants.VOUCHER_FAIL_CODE;

public class CheckoutScreen extends AppCompatActivity implements NetworkInterface {

    private ActivityCheckoutScreenBinding activityCheckoutScreenBinding;
    private UserAddresses userAddresses;

    private ProductViewModel productViewModel;
    private OrderViewModel orderViewModel;
    private AppPreference appPreference;
    private List<CartItem> cartItemList = new ArrayList<>();

    private List<CartListModel> cartListModelList = new ArrayList<>();
    private CartListModel cartListModel;
    private List<DeliveryChargesOfMerchantItem> merchantItems = new ArrayList<>();
    private CartIMerchantAdapter cartIMerchantAdapter;
    private List<CartItem> mCartProducts;

    private Map<String, String> parameter = new HashMap<>();
    private Map<String, String> header = new HashMap<>();
    private String token;

    String totalBill;
    Double totalBillWithShippingCharges = 0.0;
    Double totalAmount = 0.0;
    private boolean isUserAddressNull = false;
    Integer deliverCharges = 0;

    StringBuilder cartIds = new StringBuilder();
    String cartIdsStart = "cart_id[";
    String cartIdsEnd = "]";

    AlertDialog alertDialog, phoneNumberDialogue, addAddressDialogue, voucherDialogue, otpDialogue;
    DeleteOutOfStockDialogueBinding outOfStockItemDialogueBinding;
    PhoneNumberDialogueBinding phoneNumberDialogueBinding;
    VouhcerDialogueBinding vouhcerDialogueBinding;
    AddAddressDialogueBinding addAddressDialogueBinding;
    OtpDialogueBinding otpDialogueBinding;

    CartItemsAdapter cartItemsAdapter;
    private String phoneNumber;
    private Bundle bundle = new Bundle();
    String disTotal, tCharges, discount = "-";
    Double discountedTotal, totalCharges, discountedBill, billBeforeDiscount;
    int mMerchantIndex;
    String mId, mCoupon;
    String userOtpCode = "";
    StringBuilder otpCode = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityCheckoutScreenBinding = DataBindingUtil.setContentView(this, R.layout.activity_checkout_screen);

        appPreference = AppPreference.getInstance(this);
        activityCheckoutScreenBinding.setClickListener(this);

        productViewModel = ViewModelProviders.of(this).get(ProductViewModel.class);
        orderViewModel = ViewModelProviders.of(this).get(OrderViewModel.class);

        token = appPreference.getString(SIGNIN_TOKEN);

        cartItemList = getIntent().getParcelableArrayListExtra("ready_cart_items");

        getMerchantList();
        addMerchantProductList();
        prepareOutOfStockDialogue();
        preparePhoneNumberDialogue();
        prepareAddAddressDialogue();
        prepareVoucherCodeDialogue();
        prepareOTPDialogue();
        isUserAddressExist();

    }

    private void prepareOutOfStockDialogue() {

        outOfStockItemDialogueBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.delete_out_of_stock_dialogue, null, false);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.setView(outOfStockItemDialogueBinding.getRoot());

        outOfStockItemDialogueBinding.btnDeleteItem.setOnClickListener(v -> {
            deleteOutOfStockItems();
        });

        outOfStockItemDialogueBinding.btnCancelDeleteItem.setOnClickListener(v -> {
            alertDialog.dismiss();
        });

        cartItemsAdapter = new CartItemsAdapter(this, new CartItemsAdapter.CartClickListener() {
            @Override
            public void addItemToList(int merchantId, int position) {

            }

            @Override
            public void deleteFromCart(int merchantId, int position) {

            }

            @Override
            public void changeNumberOfCount(int merchantId, int position, int quantity) {

            }
        }, 2);
        outOfStockItemDialogueBinding.recyclerView.setAdapter(cartItemsAdapter);
        outOfStockItemDialogueBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void preparePhoneNumberDialogue() {

        phoneNumberDialogueBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.phone_number_dialogue, null, false);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        phoneNumberDialogue = dialogBuilder.create();
        phoneNumberDialogue.setCanceledOnTouchOutside(false);
        phoneNumberDialogue.setView(phoneNumberDialogueBinding.getRoot());

        phoneNumberDialogueBinding.btnSave.setOnClickListener(v -> {

            if (validatePhoneNumber()) {

                activityCheckoutScreenBinding.pbLoading.setVisibility(View.VISIBLE);

                parameter.clear();
                parameter.put(PHONE_NUMBER, phoneNumber);

                UserViewModel userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
                userViewModel.updateUserDetails(token, parameter).observe(this, updateUserDataContainer -> {

                    if (updateUserDataContainer != null) {

                        if (updateUserDataContainer.getCode().equals(SUCCESS_CODE)) {

                            phoneNumberDialogue.dismiss();
                            
                            appPreference.setString(USER_NAME, updateUserDataContainer.getData().getUser().getName());
                            appPreference.setString(PHONE_NUMBER, updateUserDataContainer.getData().getUser().getPhone());
                            appPreference.setString(DATE_OF_BIRTH, updateUserDataContainer.getData().getUser().getDateOfBirth());
                            appPreference.setString(GENDER, updateUserDataContainer.getData().getUser().getGender());

                            AppConstants.userAddresses.setPhone(updateUserDataContainer.getData().getUser().getPhone());
                            activityCheckoutScreenBinding.tvUserName.setText(appPreference.getString(PHONE_NUMBER));

                            activityCheckoutScreenBinding.rvCartProducts.setVisibility(View.GONE);
                            isUserAddressExist();
                            activityCheckoutScreenBinding.rvCartProducts.setVisibility(View.VISIBLE);

                            Toast.makeText(this, updateUserDataContainer.getMsg(), Toast.LENGTH_SHORT).show();


                        } else if (updateUserDataContainer.getCode().equals(VALIDATION_FAIL_CODE)) {
                            Toast.makeText(this, updateUserDataContainer.getMsg(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, GENERAL_ERROR, Toast.LENGTH_SHORT).show();
                        }
                    } else {

                        Toast.makeText(this, GENERAL_ERROR, Toast.LENGTH_SHORT).show();
                    }

                    activityCheckoutScreenBinding.pbLoading.setVisibility(View.VISIBLE);

                });

            }
        });
    }

    private void prepareVoucherCodeDialogue() {

        vouhcerDialogueBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.vouhcer_dialogue, null, false);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        voucherDialogue = dialogBuilder.create();
        voucherDialogue.setCanceledOnTouchOutside(true);
        voucherDialogue.setView(vouhcerDialogueBinding.getRoot());

        vouhcerDialogueBinding.btnApply.setOnClickListener(v -> {

            if (!vouhcerDialogueBinding.etVoucher.getText().toString().trim().isEmpty()) {

                String coupon = vouhcerDialogueBinding.etVoucher.getText().toString();
                applyMyVoucher(mMerchantIndex, coupon);
            } else
                Toast.makeText(this, "Voucher code could not be empty", Toast.LENGTH_SHORT).show();
        });
    }

    private void prepareOTPDialogue() {

        otpDialogueBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.otp_dialogue, null, false);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        otpDialogue = dialogBuilder.create();
        otpDialogue.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        otpDialogue.setCanceledOnTouchOutside(true);
        otpDialogue.setView(otpDialogueBinding.getRoot());

        otpDialogueBinding.btnApply.setOnClickListener(v -> {

            if (!otpDialogueBinding.etPin1.getText().toString().trim().isEmpty()) {

                String pin = vouhcerDialogueBinding.etVoucher.getText().toString();

            } else
                Toast.makeText(this, "PIN could not be empty", Toast.LENGTH_SHORT).show();
        });

        changeFocusEdittext();
    }

    private void startCounter() {

        otpDialogueBinding.tvOtpNotReceive.setOnClickListener(null);

        SpannableString spannableString = new SpannableString(getString(R.string.otp_not_receivedd_clicable));
        ForegroundColorSpan foregroundColorSpanRed = new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary));
        ForegroundColorSpan foregroundColorSpanGray = new ForegroundColorSpan(getResources().getColor(R.color.colorLightGray));
        spannableString.setSpan(foregroundColorSpanGray, 29, 35, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                otpDialogueBinding.tvOtpNotReceive.setText(getString(R.string.otp_not_receivedd, (millisUntilFinished / 1000)));
            }

            public void onFinish() {

                spannableString.setSpan(foregroundColorSpanRed, 29, 35, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                otpDialogueBinding.tvOtpNotReceive.setText(spannableString);

                otpDialogueBinding.tvOtpNotReceive.setOnClickListener(v -> {
                    Toast.makeText(CheckoutScreen.this, "Resend pin", Toast.LENGTH_SHORT).show();
                });

            }

        }.start();

        otpDialogueBinding.btnApply.setOnClickListener(v -> {
            placeOrder();
        });
    }

    private void changeFocusEdittext() {

        otpDialogueBinding.etPin1.requestFocus();

        otpDialogueBinding.etPin1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() == 1) {
                    otpDialogueBinding.etPin2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        otpDialogueBinding.etPin2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() == 0)
                    otpDialogueBinding.etPin1.requestFocus();
                else if (s.length() == 1) {
                    otpDialogueBinding.etPin3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        otpDialogueBinding.etPin3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() == 0)
                    otpDialogueBinding.etPin2.requestFocus();
                else if (s.length() == 1) {
                    otpDialogueBinding.etPin4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        otpDialogueBinding.etPin4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() == 0)
                    otpDialogueBinding.etPin3.requestFocus();
                else if (s.length() == 1) {
                    otpDialogueBinding.etPin5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        otpDialogueBinding.etPin5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() == 0)
                    otpDialogueBinding.etPin4.requestFocus();
                else if (s.length() == 1) {
                    otpDialogueBinding.etPin6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        otpDialogueBinding.etPin6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() == 0)
                    otpDialogueBinding.etPin5.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    private void prepareAddAddressDialogue() {

        addAddressDialogueBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.add_address_dialogue, null, false);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        addAddressDialogue = dialogBuilder.create();
        addAddressDialogue.setCancelable(true);
        addAddressDialogue.setView(addAddressDialogueBinding.getRoot());

        addAddressDialogueBinding.btnOk.setOnClickListener(v -> {
            addAddressDialogue.dismiss();
            startActivityForResult(new Intent(CheckoutScreen.this, AddressCreationActivity.class), 202);
        });
    }


    private boolean validatePhoneNumber() {

        phoneNumber = phoneNumberDialogueBinding.etContact.getText().toString().trim();

        if (phoneNumber.length() == 11) {
            return true;
        }

        Toast.makeText(this, "Phone number should be 11 digit (03**-*******)", Toast.LENGTH_SHORT).show();
        return false;
    }

    void setBill() {
        totalBill = getIntent().getStringExtra("total_bill");
        totalAmount = Double.parseDouble(totalBill);
        totalAmount += deliverCharges;
        totalBill = String.valueOf(totalAmount);

        activityCheckoutScreenBinding.tvTotalBalance.setText(AppUtils.formatPriceString(totalBill));

        if (Double.parseDouble(totalBill) >= PAYMENT_LOWEST_LIMIT) {
            activityCheckoutScreenBinding.tvPaymentType.setText(getResources().getString(R.string.bank_transfer));
            activityCheckoutScreenBinding.rbBankTransfer.setChecked(true);
            activityCheckoutScreenBinding.rgPaymentType.setOnCheckedChangeListener(null);
        }
    }

    private void getDeliveryCharges() {

        orderViewModel.getDeliveryCharges(token, String.valueOf(AppConstants.userAddresses.getCityId())).observe(this, deliveryChargesContainer -> {

            if (deliveryChargesContainer != null) {

                setMerchantShippingRate(deliveryChargesContainer.getData().getDeliveryChargesOfMerchantItems());
                setBill();
                setListeners();
                setCartAdapter();
            }
        });
    }

    private void setMerchantShippingRate(List<DeliveryChargesOfMerchantItem> deliveryChargesOfMerchantItems) {

        Double billWithoutShippingCharges;
        Integer shippingCharges;

        for (int i = 0; i < deliveryChargesOfMerchantItems.size(); i++) {

            for (int j = 0; j < cartListModelList.size(); j++) {

                if (deliveryChargesOfMerchantItems.get(i).getMerchantId().equals(cartListModelList.get(j).getMerchantId())) {

                    shippingCharges = deliveryChargesOfMerchantItems.get(i).getShippingRate();
                    cartListModelList.get(j).setMerchantShippingRate(String.valueOf(shippingCharges));

                    billWithoutShippingCharges = Double.parseDouble(cartListModelList.get(j).getTotalAmount());
                    totalBillWithShippingCharges = billWithoutShippingCharges + shippingCharges;
                    cartListModelList.get(j).setTotalCharges(String.valueOf(totalBillWithShippingCharges));

                    deliverCharges += shippingCharges;
                }
            }
        }
    }

    private void setMerchantShippingRate() {

        for (int j = 0; j < cartListModelList.size(); j++) {

            cartListModelList.get(j).setMerchantShippingRate(String.valueOf(0));
            cartListModelList.get(j).setTotalCharges(cartListModelList.get(j).getTotalAmount());
        }
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

        activityCheckoutScreenBinding.btnChangePaymentType.setOnClickListener(v -> {

            Intent intent = new Intent(CheckoutScreen.this, ChangePaymentActivity.class);
            intent.putExtra("total_bill", totalBill);
            startActivityForResult(intent, 201);
        });

        activityCheckoutScreenBinding.paymentDetail.setOnClickListener(v -> {

            Intent intent = new Intent(CheckoutScreen.this, ChangePaymentActivity.class);
            intent.putExtra("total_bill", totalBill);
            startActivityForResult(intent, 201);
        });

        activityCheckoutScreenBinding.btnAddAddress.setOnClickListener(v -> {
            startActivityForResult(new Intent(CheckoutScreen.this, ChangeShippingAddress.class), 202);
        });

        activityCheckoutScreenBinding.userDetail.setOnClickListener(p -> {
            startActivityForResult(new Intent(CheckoutScreen.this, ChangeShippingAddress.class), 202);
        });
        
        activityCheckoutScreenBinding.ivCloseCheckoutScreen.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    @Override
    public void onBackPressed() {

        IS_COUPON_APPLIED = false;
        finish();
    }

    void isUserAddressExist() {

        userAddresses = AppConstants.userAddresses;

        if (userAddresses == null) {

            addAddressDialogue.show();
            activityCheckoutScreenBinding.addUserAddress.setVisibility(View.VISIBLE);
            activityCheckoutScreenBinding.userDetail.setVisibility(View.GONE);
            isUserAddressNull = true;

            setMerchantShippingRate();
            setBill();
            setListeners();
            setCartAdapter();

            activityCheckoutScreenBinding.container.setVisibility(View.VISIBLE);
            activityCheckoutScreenBinding.pbLoading.setVisibility(View.GONE);

        } else {

            activityCheckoutScreenBinding.tvUserName.setText(userAddresses.getUserNameAddress());
            activityCheckoutScreenBinding.tvUserCity.setText(userAddresses.getCityName());
            activityCheckoutScreenBinding.tvUserAddress.setText(userAddresses.getShippingAddress().concat(" ").concat(String.valueOf(userAddresses.getPostalCode())));
            activityCheckoutScreenBinding.tvUserMobile.setText(userAddresses.getPhone());
            activityCheckoutScreenBinding.btnCheckout.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            activityCheckoutScreenBinding.addUserAddress.setVisibility(View.GONE);
            activityCheckoutScreenBinding.userDetail.setVisibility(View.VISIBLE);
            isUserAddressNull = false;

            if (userAddresses.getPhone() == null || userAddresses.getPhone().isEmpty()) {
                phoneNumberDialogue.show();
            }

            getDeliveryCharges();
        }
    }

    public void addNewAddress(View view) {
        Intent intent = new Intent(this, AddressCreationActivity.class);
        intent.putExtra("is_edit_address", false);
        startActivityForResult(intent, 1);
    }

    void setCartAdapter() {

        cartIMerchantAdapter = new CartIMerchantAdapter(this, new CartIMerchantAdapter.MerchantItemClickListener() {

            @Override
            public void addItemToList(int merchantId, int position) {

            }

            @Override
            public void deleteFromCart(int merchantId, int position) {

            }

            @Override
            public void changeNumberOfCount(int merchantId, int position, int quantity) {

            }

            @Override
            public void applyVoucher(int merchantIndex) {

                if (IS_COUPON_APPLIED) {
                    Toast.makeText(CheckoutScreen.this, "Coupon applied", Toast.LENGTH_SHORT).show();
                } else {

                    vouhcerDialogueBinding.etVoucher.requestFocus();
                    voucherDialogue.show();
                    mMerchantIndex = merchantIndex;
                }


                //applyMyVoucher(merchantIndex);
            }
        });

        activityCheckoutScreenBinding.rvCartProducts.setHasFixedSize(true);
        activityCheckoutScreenBinding.rvCartProducts.setLayoutManager(new LinearLayoutManager(this));
        activityCheckoutScreenBinding.rvCartProducts.setAdapter(cartIMerchantAdapter);
        cartIMerchantAdapter.setData(cartListModelList);

        activityCheckoutScreenBinding.container.setVisibility(View.VISIBLE);
        activityCheckoutScreenBinding.rvCartProducts.setVisibility(View.VISIBLE);
        activityCheckoutScreenBinding.pbLoading.setVisibility(View.GONE);
    }

    public void getOtp() {

        activityCheckoutScreenBinding.pbLoading.setVisibility(View.VISIBLE);

        otpDialogue.show();
        startCounter();

        productViewModel.generateOtpToConfirmOrder().observe(this, otpDataContainer -> {

            activityCheckoutScreenBinding.pbLoading.setVisibility(View.GONE);

            if (otpDataContainer.getCode().equals(SUCCESS_CODE)) {
                userOtpCode = String.valueOf(otpDataContainer.getData().getUserOtpId());
            }
        });
        //prepareOTPDialogue();
    }

    public void placeOrder() {

        if (isUserAddressNull) {
            addAddressDialogue.show();
            return;
        }

        if (userAddresses.getPhone() == null || userAddresses.getPhone().isEmpty()) {
            phoneNumberDialogue.show();
            return;
        }

        getOtpCode();

        if (otpCode.toString().length() != 6) {
            Toast.makeText(this, "You need to enter complete PIN", Toast.LENGTH_SHORT).show();
            return;
        }

        activityCheckoutScreenBinding.pbLoading.setVisibility(View.VISIBLE);
        parameter.clear();
        parameter.put("name", userAddresses.getUserNameAddress());
        parameter.put("phone_no", userAddresses.getPhone());
        parameter.put("email", userAddresses.getEmail());
        parameter.put("city_id", String.valueOf(userAddresses.getCityId()));
        parameter.put("delivery_address", String.valueOf(userAddresses.getShippingAddress()));
        parameter.put("postal_code", String.valueOf(userAddresses.getPostalCode()));

        parameter.put("shipping_method", String.valueOf(0));
        parameter.put("payment_gateway", String.valueOf(CASH_TRANSFER_TYPE));

        parameter.put("user_otp_id", userOtpCode);
        parameter.put("otp", otpCode.toString());

        if (IS_COUPON_APPLIED) {
            parameter.put("verified_coupon", mCoupon);
            parameter.put("verified_coupon_for_merchant_id", mId);
        }

        productViewModel.confirmOrder(parameter, this).observe(this, orderDataContainer -> {

            if (orderDataContainer != null) {

                if (orderDataContainer.getCode().equals(SUCCESS_CODE)) {
                    Toast.makeText(this, orderDataContainer.getMsg(), Toast.LENGTH_SHORT).show();

                    bundle.putString("price", String.valueOf(totalBill));
                    AnalyticsManager.getInstance().sendAnalytics("confirm_order", bundle);
                    AnalyticsManager.getInstance().sendFacebookAnalytics("confirm_order", bundle);

                    Intent intent = new Intent(this, OrderReceivedActivity.class);
                    intent.putExtra(ORDER_TOTAL, String.valueOf(totalBill));
                    CASH_TRANSFER_TYPE = 1;
                    CART_COUNTER = 0;
                    IS_COUPON_APPLIED = false;
                    intent.putParcelableArrayListExtra("recommended_products", (ArrayList<? extends Parcelable>) orderDataContainer.getData().getRecommendation());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();

                } else if (orderDataContainer.getCode().equals(OUT_OF_STOCK_CODE)) {

                    mCartProducts = orderDataContainer.getData().getProducts();
                    setOutOfStockItems(mCartProducts);

                } else if (orderDataContainer.getCode().equals(VALIDATION_FAIL_CODE))
                    Toast.makeText(this, orderDataContainer.getMsg(), Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, GENERAL_ERROR, Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(this, GENERAL_ERROR, Toast.LENGTH_SHORT).show();


            activityCheckoutScreenBinding.pbLoading.setVisibility(View.GONE);
        });
    }

    private void getOtpCode() {

        otpCode.append(otpDialogueBinding.etPin1.getText().toString());
        otpCode.append(otpDialogueBinding.etPin2.getText().toString());
        otpCode.append(otpDialogueBinding.etPin3.getText().toString());
        otpCode.append(otpDialogueBinding.etPin4.getText().toString());
        otpCode.append(otpDialogueBinding.etPin5.getText().toString());
        otpCode.append(otpDialogueBinding.etPin6.getText().toString());

    }

    public void applyMyVoucher(int merchantIndex, String coupon) {

        if (IS_COUPON_APPLIED) {
            Toast.makeText(this, "Coupon applied", Toast.LENGTH_SHORT).show();
            return;
        }

        vouhcerDialogueBinding.pbLoading.setVisibility(View.VISIBLE);
        AppUtils.blockUi(this);
        AppUtils.hideKeyboard(this);

        cartListModel = cartListModelList.get(merchantIndex);

        discount = "-";
//        Integer merchantId = 62;
        // coupon = "LL123";

        Integer merchantId = cartListModel.getMerchantId();
        mId = String.valueOf(merchantId);
        mCoupon = coupon;

        parameter.clear();
        parameter.put("voucher_code", mCoupon);
        parameter.put("merchant_id", mId);
        parameter.put("sub_total", cartListModel.getTotalAmount());

        productViewModel.isVoucherValid(parameter).observe(this, voucherDataContainer -> {

            if (voucherDataContainer != null) {

                Toast.makeText(this, voucherDataContainer.getMsg(), Toast.LENGTH_SHORT).show();

                if (voucherDataContainer.getCode().equals(SUCCESS_CODE)) {

                    voucherDialogue.dismiss();
                    vouhcerDialogueBinding.tvErrorMessage.setVisibility(View.GONE);

                    discountedTotal = voucherDataContainer.getData().getDiscountedTotal();

                    discount = discount + voucherDataContainer.getData().getDiscountAmount();
                    discountedBill = Double.parseDouble(voucherDataContainer.getData().getDiscountAmount());
                    billBeforeDiscount = Double.parseDouble(totalBill);

                    discountedBill = Math.abs(billBeforeDiscount - discountedBill);

                    totalCharges = discountedTotal + Double.parseDouble(cartListModel.getMerchantShippingRate());
                    disTotal = String.valueOf(discountedTotal);
                    tCharges = String.valueOf(totalCharges);

                    totalAmount = discountedBill;
                    totalBill = String.valueOf(totalAmount);

                    activityCheckoutScreenBinding.tvTotalBalance.setText(AppUtils.formatPriceString(totalBill));

                    if (totalAmount >= PAYMENT_LOWEST_LIMIT) {
                        activityCheckoutScreenBinding.tvPaymentType.setText(getResources().getString(R.string.bank_transfer));
                        activityCheckoutScreenBinding.rbBankTransfer.setChecked(true);
                        activityCheckoutScreenBinding.rgPaymentType.setOnCheckedChangeListener(null);
                    }

                    cartListModelList.get(merchantIndex).setTotalCharges(tCharges);
                    cartListModelList.get(merchantIndex).setDiscountApplied(true);
                    cartListModelList.get(merchantIndex).setDiscount(discount);
                    cartListModelList.get(merchantIndex).setCoupon(coupon);

                    cartIMerchantAdapter.notifyDataSetChanged();
                    IS_COUPON_APPLIED = true;
                } else if (voucherDataContainer.getCode().equals(VOUCHER_FAIL_CODE)) {
                    vouhcerDialogueBinding.tvErrorMessage.setText(voucherDataContainer.getMsg());
                    vouhcerDialogueBinding.tvErrorMessage.setVisibility(View.VISIBLE);
                }
            }

            vouhcerDialogueBinding.pbLoading.setVisibility(View.GONE);
            AppUtils.unBlockUi(this);

        });
    }

    private void setOutOfStockItems(List<CartItem> cartProducts) {

        setParameters(cartProducts);

        if (alertDialog != null) {

            cartItemsAdapter.setData(cartProducts);
            alertDialog.show();
        }
    }

    private void setParameters(List<CartItem> cartItemList) {

        StringBuilder parameterKey = new StringBuilder();
        parameter.clear();

        for (int i = 0; i < cartItemList.size(); i++) {
            parameterKey.append(cartIdsStart);
            parameterKey.append(i);
            parameterKey.append(cartIdsEnd);
            parameter.put(parameterKey.toString(), String.valueOf(cartItemList.get(i).getCartId()));

            parameterKey = new StringBuilder();
        }
    }

    private void deleteOutOfStockItems() {

        alertDialog.dismiss();
        activityCheckoutScreenBinding.pbLoading.setVisibility(View.VISIBLE);
        header.put(SIGNIN_TOKEN, token);

        productViewModel.deleteCartItem(header, parameter).observe(this, basicResponse -> {

            if (basicResponse != null) {
                if (basicResponse.getCode().equals(SUCCESS_CODE)) {

                    deleteFromList();
                    Toast.makeText(this, basicResponse.getMsg(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, basicResponse.getMsg(), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, GENERAL_ERROR, Toast.LENGTH_SHORT).show();
            }

            activityCheckoutScreenBinding.pbLoading.setVisibility(View.VISIBLE);
        });
    }

    private void deleteFromList() {

        Log.d(AppConstants.TAG, "deleteFromList:");
        for (int i = 0; i < cartItemList.size(); i++) {
            for (int j = 0; j < mCartProducts.size(); j++) {

                if (cartItemList.get(i).getCartId().equals(mCartProducts.get(j).getCartId())) {
                    cartItemList.remove(i);
                    i--;
                    break;
                }
            }
        }

        if (cartItemList.size() > 0) {
            getMerchantList();
            addMerchantProductList();
            isUserAddressExist();
        } else {

            AppConstants.LOAD_HOME_FRAGMENT_INDEX = 2;
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }

    }

    private void getMerchantList() {

        DeliveryChargesOfMerchantItem merchantItem;
        merchantItems.clear();

        int mId = -1;

        for (int i = 0; i < cartItemList.size(); i++) {

            merchantItem = new DeliveryChargesOfMerchantItem();

            merchantItem.setMerchantName(cartItemList.get(i).getMerchantName());
            merchantItem.setMerchantId(cartItemList.get(i).getMerchantId());

            merchantItems.add(merchantItem);
        }


        for (int i = 0; i < merchantItems.size(); i++) {

            if (merchantItems.get(i).getMerchantId() == mId) {
                merchantItems.remove(merchantItems.get(i));
                i--;
            }

            mId = merchantItems.get(i).getMerchantId();
        }

    }

    private void addMerchantProductList() {

        CartListModel cartListModel;
        List<CartItem> tempCartItem;
        cartListModelList = new ArrayList<>();
        Double totalMerchant = 0.0;
        Double totalTemp = 0.0;

        // creating model list for adapter to display products merchant wise {merchantId, merchantName, List of Cart Products}
        for (int i = 0; i < merchantItems.size(); i++) {

            tempCartItem = new ArrayList<>();
            totalMerchant = 0.0;

            for (int j = 0; j < cartItemList.size(); j++) {

                if (merchantItems.get(i).getMerchantId().equals(cartItemList.get(j).getMerchantId())) {
                    tempCartItem.add(cartItemList.get(j));
                    totalTemp = Double.parseDouble(cartItemList.get(j).getSalePrice()) * cartItemList.get(j).getItemQuantity();
                    totalMerchant += totalTemp;
                }
            }


            cartListModel = new CartListModel();
            cartListModel.setMerchantId(merchantItems.get(i).getMerchantId());
            cartListModel.setMerchantName(merchantItems.get(i).getMerchantName());
            cartListModel.setTotalAmount(String.valueOf(totalMerchant));
            cartListModel.setDiscountApplied(false);
            cartListModel.setDiscount("0");

            cartListModel.setCartItemList(tempCartItem);
            cartListModelList.add(cartListModel);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        activityCheckoutScreenBinding.container.fullScroll(ScrollView.FOCUS_UP);

        if (resultCode == Activity.RESULT_OK) {

            activityCheckoutScreenBinding.pbLoading.setVisibility(View.VISIBLE);

            if (requestCode == Integer.parseInt(SUCCESS_CODE)) {
                activityCheckoutScreenBinding.rvCartProducts.setVisibility(View.GONE);
                isUserAddressExist();
            } else if (requestCode == 1) {
                activityCheckoutScreenBinding.rvCartProducts.setVisibility(View.GONE);
                isUserAddressExist();
            } else if (requestCode == 201) {

                if (CASH_TRANSFER_TYPE == 1) {
                    activityCheckoutScreenBinding.tvPaymentType.setText(getResources().getString(R.string.cash_on_delivery));
                    CASH_TRANSFER_TYPE = 1;
                } else {
                    activityCheckoutScreenBinding.tvPaymentType.setText(getResources().getString(R.string.bank_transfer));
                    CASH_TRANSFER_TYPE = 2;
                }

                activityCheckoutScreenBinding.pbLoading.setVisibility(View.GONE);

            } else if (requestCode == 202) {

                activityCheckoutScreenBinding.rvCartProducts.setVisibility(View.GONE);
                isUserAddressExist();
            }
        } else {
            activityCheckoutScreenBinding.rvCartProducts.setVisibility(View.GONE);
            isUserAddressExist();
        }
    }


    @Override
    public void onFailure(boolean isFailed) {

        if (isFailed)
            activityCheckoutScreenBinding.pbLoading.setVisibility(View.GONE);
    }
}
