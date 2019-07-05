package com.lalaland.ecommerce.views.fragments.homeFragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.adapters.CartIMerchantAdapter;
import com.lalaland.ecommerce.adapters.CartItemsAdapter;
import com.lalaland.ecommerce.data.models.DeliveryChargesData.DeliveryChargesOfMerchantItem;
import com.lalaland.ecommerce.data.models.cart.CartItem;
import com.lalaland.ecommerce.data.models.cartListingModel.CartListModel;
import com.lalaland.ecommerce.databinding.FragmentCartBinding;
import com.lalaland.ecommerce.helpers.AppConstants;
import com.lalaland.ecommerce.helpers.AppPreference;
import com.lalaland.ecommerce.helpers.AppUtils;
import com.lalaland.ecommerce.viewModels.products.ProductViewModel;
import com.lalaland.ecommerce.views.activities.CheckoutScreen;
import com.lalaland.ecommerce.views.activities.RegistrationActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static com.lalaland.ecommerce.helpers.AppConstants.ADD_TO_READY_PRODUCT;
import static com.lalaland.ecommerce.helpers.AppConstants.CART_ID;
import static com.lalaland.ecommerce.helpers.AppConstants.CART_ITEM_ID;
import static com.lalaland.ecommerce.helpers.AppConstants.CART_SESSION_TOKEN;
import static com.lalaland.ecommerce.helpers.AppConstants.GENERAL_ERROR;
import static com.lalaland.ecommerce.helpers.AppConstants.QUANTITY;
import static com.lalaland.ecommerce.helpers.AppConstants.REMOVED_FROM_CART;
import static com.lalaland.ecommerce.helpers.AppConstants.REMOVE_FROM_READY_PRODUCT;
import static com.lalaland.ecommerce.helpers.AppConstants.SIGNIN_TOKEN;
import static com.lalaland.ecommerce.helpers.AppConstants.SUCCESS_CODE;
import static com.lalaland.ecommerce.helpers.AppConstants.TAG;
import static com.lalaland.ecommerce.helpers.AppConstants.VALIDATION_FAIL_CODE;

public class CartFragment extends Fragment implements View.OnClickListener, CartIMerchantAdapter.MerchantItemClickListener {

    private FragmentCartBinding fragmentCartBinding;

    private ProductViewModel productViewModel;

    private CartIMerchantAdapter cartIMerchantAdapter;
    private CartItemsAdapter cartItemsAdapter;
    private AppPreference appPreference;

    private List<CartItem> cartItemList = new ArrayList<>();
    private List<CartListModel> cartListModelList = new ArrayList<>();
    private List<CartListModel> mCartListModelList = new ArrayList<>();
    private List<CartItem> selectedCartItemList = new ArrayList<>();
    private List<DeliveryChargesOfMerchantItem> merchantItems = new ArrayList<>();

    private Map<String, String> parameter = new HashMap<>();
    private Map<String, String> headers = new HashMap<>();
    private String token, cart_session;

    private Double totalBill = 0.0;
    Double perItemBill;
    boolean isApiCalling = false;
    String[] cartIds = new String[2];

    public CartFragment() {
        // Required empty public constructor
    }

    public static CartFragment newInstance() {
        CartFragment fragment = new CartFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fragmentCartBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_cart, container, false);

        productViewModel = ViewModelProviders.of(this).get(ProductViewModel.class);

        appPreference = AppPreference.getInstance(getContext());

        token = appPreference.getString(SIGNIN_TOKEN);
        cart_session = appPreference.getString(CART_SESSION_TOKEN);

        getCartItems();

        return fragmentCartBinding.getRoot();
    }


    void setCartAdapter() {

        if (cartItemList.size() == 0) {
            fragmentCartBinding.tvCartEmptyState.setVisibility(View.VISIBLE);
            fragmentCartBinding.tvEmptyState.setVisibility(View.VISIBLE);

            fragmentCartBinding.btnCheckout.setOnClickListener(null);
        } else {
            fragmentCartBinding.tvCartEmptyState.setVisibility(View.GONE);
            fragmentCartBinding.tvEmptyState.setVisibility(View.GONE);
            fragmentCartBinding.btnCheckout.setOnClickListener(this);
        }


        cartIMerchantAdapter = new CartIMerchantAdapter(getContext(), this);

        fragmentCartBinding.rvCartItems.setLayoutManager(new LinearLayoutManager(getContext()));
        fragmentCartBinding.rvCartItems.setAdapter(cartIMerchantAdapter);
        cartIMerchantAdapter.setData(cartListModelList);

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

                        getMerchantList();
                        addMerchantProductList();
                        setCartAdapter();
                        setSelectedCartItemList();

                        Log.d(TAG, String.valueOf(cartContainer.getData().getCartItems().size()));
                    } else if (cartContainer.getCode().equals(VALIDATION_FAIL_CODE))
                        Toast.makeText(getContext(), cartContainer.getMsg(), Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(getContext(), GENERAL_ERROR, Toast.LENGTH_SHORT).show();
                }

                fragmentCartBinding.pbLoading.setVisibility(View.GONE);
            });
        } else {

            headers.put(CART_SESSION_TOKEN, cart_session);

            productViewModel.getCart(headers).observe(this, cartContainer -> {

                if (cartContainer != null) {

                    if (cartContainer.getCode().equals(SUCCESS_CODE)) {
                        cartItemList.addAll(cartContainer.getData().getCartItems());
                        AppConstants.userAddresses = cartContainer.getData().getUserAddresses();

                        getMerchantList();
                        addMerchantProductList();
                        setCartAdapter();
                        setSelectedCartItemList();

                        Log.d(TAG, String.valueOf(cartContainer.getData().getCartItems().size()));
                    } else
                        Toast.makeText(getContext(), GENERAL_ERROR, Toast.LENGTH_SHORT).show();
                }

                fragmentCartBinding.pbLoading.setVisibility(View.GONE);
            });
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

        // creating model list for adapter to display products merchant wise {merchantId, merchantName, List of Cart Products}
        for (int i = 0; i < merchantItems.size(); i++) {

            tempCartItem = new ArrayList<>();
            for (int j = 0; j < cartItemList.size(); j++) {

                if (merchantItems.get(i).getMerchantId().equals(cartItemList.get(j).getMerchantId())) {
                    tempCartItem.add(cartItemList.get(j));
                    totalMerchant += Double.parseDouble(cartItemList.get(j).getSalePrice());
                }
            }

            cartListModel = new CartListModel();
            cartListModel.setMerchantId(merchantItems.get(i).getMerchantId());
            cartListModel.setMerchantName(merchantItems.get(i).getMerchantName());
            cartListModel.setTotalAmount(String.valueOf(totalMerchant));
            cartListModel.setMerchantShippingRate("0.0");
            cartListModel.setTotalCharges("0.0");
            cartListModel.setCartItemList(tempCartItem);
            cartListModelList.add(cartListModel);
        }
    }


    void setSelectedCartItemList() {

        for (CartItem cartItem : cartItemList) {
            if (cartItem.getCartStatus() == 3)
                selectedCartItemList.add(cartItem);
            else
                selectedCartItemList.remove(cartItem);
        }

        calCalculateBill();
    }

    @Override
    public void addItemToList(int merchantId, int position) {

        // if response has not received of previous api
        if (isApiCalling)
            return;

        Integer merchantIndex = getMerchantModelIndex(merchantId);
        fragmentCartBinding.pbLoading.setVisibility(View.VISIBLE);
        CartItem cartItem = cartListModelList.get(merchantIndex).getCartItemList().get(position);

        parameter.clear();
        parameter.put(CART_ITEM_ID, String.valueOf(cartItem.getCartId()));

        if (cartItem.getCartStatus() == 1) {
            cartListModelList.get(merchantIndex).getCartItemList().get(position).setCartStatus(3);
            selectedCartItemList.add(cartItem);
        } else if (cartItem.getCartStatus() == 3) {
            cartListModelList.get(merchantIndex).getCartItemList().get(position).setCartStatus(1);
            selectedCartItemList.remove(cartItem); //delete item if exists
        }

        calCalculateBill();

        productViewModel.addToReadyCartList(headers, parameter).observe(this, basicResponse ->
        {
            isApiCalling = true;

            if (basicResponse != null) {
                if (basicResponse.getCode().equals(SUCCESS_CODE)) {

                    if (cartListModelList.get(merchantIndex).getCartItemList().get(position).getCartStatus() == 1) // its status was 1
                        Toast.makeText(getContext(), REMOVE_FROM_READY_PRODUCT, Toast.LENGTH_SHORT).show();
                    else if (cartListModelList.get(merchantIndex).getCartItemList().get(position).getCartStatus() == 3) // its status was 3
                        Toast.makeText(getContext(), ADD_TO_READY_PRODUCT, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), GENERAL_ERROR, Toast.LENGTH_SHORT).show();
                }
            } else
                Toast.makeText(getContext(), GENERAL_ERROR, Toast.LENGTH_SHORT).show();

            isApiCalling = false;
            fragmentCartBinding.pbLoading.setVisibility(View.GONE);
        });
    }

    @Override
    public void deleteFromCart(int merchantId, int position) {

        // if response has not received of previous api
        if (isApiCalling)
            return;

        Integer merchantIndex = getMerchantModelIndex(merchantId);
        fragmentCartBinding.pbLoading.setVisibility(View.VISIBLE);
        CartItem cartItem = cartListModelList.get(merchantIndex).getCartItemList().get(position);

        parameter.clear();

        parameter.put(CART_ID, String.valueOf(cartItem.getCartId()));

        parameter.put("is_delete_all", String.valueOf(0));

        productViewModel.deleteCartItem(headers, parameter).observe(this, basicResponse ->
        {
            isApiCalling = true;
            if (basicResponse != null) {
                if (basicResponse.getCode().equals(SUCCESS_CODE)) {

                    cartItemList.remove(cartItem);
                    selectedCartItemList.remove(cartItem);
                    getMerchantList();
                    addMerchantProductList();
                    setCartAdapter();
                    calCalculateBill();

                    if (cartListModelList.size() < 1) {
                        fragmentCartBinding.tvCartEmptyState.setVisibility(View.VISIBLE);
                        fragmentCartBinding.tvEmptyState.setVisibility(View.VISIBLE);
                        AppConstants.CART_COUNTER = 0;
                    } else {
                        fragmentCartBinding.tvCartEmptyState.setVisibility(View.GONE);
                        fragmentCartBinding.tvEmptyState.setVisibility(View.GONE);
                    }

                    Toast.makeText(getContext(), REMOVED_FROM_CART, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), GENERAL_ERROR, Toast.LENGTH_SHORT).show();
                }
            } else
                Toast.makeText(getContext(), GENERAL_ERROR, Toast.LENGTH_SHORT).show();

            isApiCalling = false;
            fragmentCartBinding.pbLoading.setVisibility(View.GONE);
        });
    }

    @Override
    public void changeNumberOfCount(int merchantId, int position, int quantity) {

        // if response has not received of previous api
        if (isApiCalling)
            return;

        Integer merchantIndex = getMerchantModelIndex(merchantId);
        fragmentCartBinding.pbLoading.setVisibility(View.VISIBLE);

        CartItem cartItem = cartListModelList.get(merchantIndex).getCartItemList().get(position);

        parameter.clear();
        parameter.put(CART_ITEM_ID, String.valueOf(cartItem.getCartId()));
        parameter.put(QUANTITY, String.valueOf(quantity));

        productViewModel.changeCartProductQuantity(parameter).observe(this, basicResponse ->
        {
            isApiCalling = true;

            if (basicResponse != null) {
                if (basicResponse.getCode().equals(SUCCESS_CODE)) {

                    cartListModelList.get(merchantIndex).getCartItemList().get(position).setItemQuantity(quantity);

//                    cartIMerchantAdapter.setData(cartListModelList);
                    setCartAdapter();

                    if (selectedCartItemList.contains(cartItemList.get(position)))
                        calCalculateBill();

                    Toast.makeText(getContext(), basicResponse.getMsg(), Toast.LENGTH_SHORT).show();

                } else if (basicResponse.getCode().equals(VALIDATION_FAIL_CODE)) {
                    Toast.makeText(getContext(), basicResponse.getMsg(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), GENERAL_ERROR, Toast.LENGTH_SHORT).show();
                }
            } else
                Toast.makeText(getContext(), GENERAL_ERROR, Toast.LENGTH_SHORT).show();

            isApiCalling = false;
            fragmentCartBinding.pbLoading.setVisibility(View.GONE);
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_checkout:

                if (getContext() != null) {

                    Intent intent;

                    token = appPreference.getString(SIGNIN_TOKEN);
                    if (token != null && !token.isEmpty()) {

                        if (!selectedCartItemList.isEmpty()) {
                            intent = new Intent(getContext(), CheckoutScreen.class);
                            intent.putExtra("total_bill", String.valueOf(totalBill));
                            intent.putParcelableArrayListExtra("ready_cart_items", (ArrayList<? extends Parcelable>) selectedCartItemList);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getContext(), "Please select some items to proceed", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } else {
                        intent = new Intent(getContext(), RegistrationActivity.class);
                        startActivityForResult(intent, 100);
                    }


                }
                break;
        }
    }

    private Integer getMerchantModelIndex(int merchantId) {

        for (int i = 0; i < cartListModelList.size(); i++) {

            if (cartListModelList.get(i).getMerchantId() == merchantId)
                return i;
        }

        return null; //
    }

    private void calCalculateBill() {

        perItemBill = 0.0;
        totalBill = 0.0;

        for (CartItem mCartItem : selectedCartItemList) {

            perItemBill = mCartItem.getItemQuantity() * Double.parseDouble(mCartItem.getSalePrice());
            totalBill += perItemBill;
        }

        if (selectedCartItemList.size() < 1 || String.valueOf(totalBill).equals("0.0"))
            fragmentCartBinding.tvTotalBalance.setText("");
        else {
            fragmentCartBinding.tvTotalBalance.setText(AppUtils.formatPriceString(String.valueOf(totalBill)));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        token = appPreference.getString(SIGNIN_TOKEN);

        if (resultCode == RESULT_OK) {
            if (requestCode == 100) {

                token = appPreference.getString(SIGNIN_TOKEN);
                cart_session = appPreference.getString(CART_SESSION_TOKEN);
                getCartItems();

                //        fragmentCartBinding.btnCheckout.performClick();
            }
        }
    }


}
