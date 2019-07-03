package com.lalaland.ecommerce.views.activities;

import android.app.Activity;
import android.app.AlertDialog;
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
import com.lalaland.ecommerce.adapters.CartIMerchantAdapter;
import com.lalaland.ecommerce.adapters.CartItemsAdapter;
import com.lalaland.ecommerce.customDialogue.CustomListViewDialog;
import com.lalaland.ecommerce.data.models.DeliveryChargesData.DeliveryChargesOfMerchantItem;
import com.lalaland.ecommerce.data.models.cart.CartItem;
import com.lalaland.ecommerce.data.models.cartListingModel.CartListModel;
import com.lalaland.ecommerce.data.models.userAddressBook.UserAddresses;
import com.lalaland.ecommerce.databinding.ActivityCheckoutScreenBinding;
import com.lalaland.ecommerce.databinding.DeleteOutOfStockDialogueBinding;
import com.lalaland.ecommerce.helpers.AppConstants;
import com.lalaland.ecommerce.helpers.AppPreference;
import com.lalaland.ecommerce.helpers.AppUtils;
import com.lalaland.ecommerce.viewModels.order.OrderViewModel;
import com.lalaland.ecommerce.viewModels.products.ProductViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.lalaland.ecommerce.helpers.AppConstants.CASH_TRANSFER_TYPE;
import static com.lalaland.ecommerce.helpers.AppConstants.GENERAL_ERROR;
import static com.lalaland.ecommerce.helpers.AppConstants.ORDER_TOTAL;
import static com.lalaland.ecommerce.helpers.AppConstants.OUT_OF_STOCK_CODE;
import static com.lalaland.ecommerce.helpers.AppConstants.PAYMENT_LOWEST_LIMIT;
import static com.lalaland.ecommerce.helpers.AppConstants.SIGNIN_TOKEN;
import static com.lalaland.ecommerce.helpers.AppConstants.SUCCESS_CODE;
import static com.lalaland.ecommerce.helpers.AppConstants.VALIDATION_FAIL_CODE;

public class CheckoutScreen extends AppCompatActivity {

    private ActivityCheckoutScreenBinding activityCheckoutScreenBinding;
    private UserAddresses userAddresses;

    private ProductViewModel productViewModel;
    private OrderViewModel orderViewModel;
    private AppPreference appPreference;
    private List<CartItem> cartItemList = new ArrayList<>();

    private List<CartListModel> cartListModelList = new ArrayList<>();
    private List<DeliveryChargesOfMerchantItem> merchantItems = new ArrayList<>();
    private CartIMerchantAdapter cartIMerchantAdapter;
    private List<CartItem> selectedCartItemList = new ArrayList<>();

    private Map<String, String> parameter = new HashMap<>();
    private Map<String, String> headers = new HashMap<>();
    private String token, cart_session;
    String totalBill;
    Double totalBillWithShippingCharges = 0.0;
    Double totalAmount = 0.0;
    private boolean isUserAddressNull = false;
    Integer deliverCharges = 0;

    AlertDialog alertDialog;
    View dialogView;
    DeleteOutOfStockDialogueBinding outOfStockItemDialogueBinding;
    CartItemsAdapter outOfStockAdapter;

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
        isUserAddressExist();
        prepareDialogue();
    }

    private void prepareDialogue() {


      /*  outOfStockItemDialogueBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.delete_out_of_stock_dialogue, activityCheckoutScreenBinding.parent, false);

        dialogView = LayoutInflater.from(this).inflate(R.layout.delete_out_of_stock_dialogue, null);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(true);
        alertDialog.setView(dialogView);

        dialogView.findViewById(R.id.btn_delete_item).setOnClickListener(v -> {
            Toast.makeText(this, "Delete items", Toast.LENGTH_SHORT).show();
        });

        dialogView.findViewById(R.id.btn_cancel_delete_item).setOnClickListener(v -> {
            Toast.makeText(this, "Cancel Delete items", Toast.LENGTH_SHORT).show();
        });

*//*
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.setView(outOfStockItemDialogueBinding.getRoot());
*//*
         */
        outOfStockAdapter = new CartItemsAdapter(this, new CartItemsAdapter.CartClickListener() {
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

        //      outOfStockItemDialogueBinding.recyclerView.setAdapter(outOfStockAdapter);
        //     outOfStockItemDialogueBinding.recyclerView.setHasFixedSize(true);
        //    outOfStockItemDialogueBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
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

        activityCheckoutScreenBinding.btnAddAddress.setOnClickListener(v -> {
            startActivityForResult(new Intent(CheckoutScreen.this, ChangeShippingAddress.class), 202);
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
        });

        activityCheckoutScreenBinding.rvCartProducts.setHasFixedSize(true);
        activityCheckoutScreenBinding.rvCartProducts.setLayoutManager(new LinearLayoutManager(this));
        activityCheckoutScreenBinding.rvCartProducts.setAdapter(cartIMerchantAdapter);
        cartIMerchantAdapter.setData(cartListModelList);

        activityCheckoutScreenBinding.container.setVisibility(View.VISIBLE);
        activityCheckoutScreenBinding.pbLoading.setVisibility(View.GONE);
    }

    public void placeOrder() {

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
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();

                } else if (orderDataContainer.getCode().equals(OUT_OF_STOCK_CODE)) {

                    setOutOfStockItems(orderDataContainer.getData().getProducts());
                } else if (orderDataContainer.getCode().equals(VALIDATION_FAIL_CODE))
                    Toast.makeText(this, orderDataContainer.getMsg(), Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, GENERAL_ERROR, Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(this, GENERAL_ERROR, Toast.LENGTH_SHORT).show();


            activityCheckoutScreenBinding.pbLoading.setVisibility(View.GONE);
        });
    }

    private void setOutOfStockItems(List<CartItem> cartProducts) {
/*
        outOfStockAdapter.setData(cartProducts);
        outOfStockAdapter.notifyDataSetChanged();

        alertDialog.show();
        */

        outOfStockAdapter.setData(cartProducts);
        CustomListViewDialog customListViewDialog = new CustomListViewDialog(this, outOfStockAdapter);
        customListViewDialog.show();

/*        CustomDialogClass customDialogClass = new CustomDialogClass(this, cartProducts);
        customDialogClass.show();*/
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

            cartListModel.setCartItemList(tempCartItem);
            cartListModelList.add(cartListModel);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Integer.parseInt(SUCCESS_CODE)) {
                isUserAddressExist();
            } else if (requestCode == 1) {
                isUserAddressExist();
            } else if (requestCode == 201) {

                if (CASH_TRANSFER_TYPE == 1) {
                    activityCheckoutScreenBinding.tvPaymentType.setText(getResources().getString(R.string.cash_on_delivery));
                } else {
                    activityCheckoutScreenBinding.tvPaymentType.setText(getResources().getString(R.string.bank_transfer));
                }

                CASH_TRANSFER_TYPE = 1;

            } else if (requestCode == 202) {
                isUserAddressExist();
            }
        }
    }


}
