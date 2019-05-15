package com.lalaland.ecommerce.views.fragments.homeFragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.adapters.CartItemsAdapter;
import com.lalaland.ecommerce.data.models.cart.CartItem;
import com.lalaland.ecommerce.databinding.FragmentCartBinding;
import com.lalaland.ecommerce.helpers.AppConstants;
import com.lalaland.ecommerce.helpers.AppPreference;
import com.lalaland.ecommerce.viewModels.products.ProductViewModel;
import com.lalaland.ecommerce.views.activities.CheckoutScreen;
import com.lalaland.ecommerce.views.activities.RegistrationActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.lalaland.ecommerce.helpers.AppConstants.ADD_TO_READY_PRODUCT;
import static com.lalaland.ecommerce.helpers.AppConstants.CART_ID;
import static com.lalaland.ecommerce.helpers.AppConstants.CART_SESSION_TOKEN;
import static com.lalaland.ecommerce.helpers.AppConstants.GENERAL_ERROR;
import static com.lalaland.ecommerce.helpers.AppConstants.QUANTITY;
import static com.lalaland.ecommerce.helpers.AppConstants.REMOVED_FROM_CART;
import static com.lalaland.ecommerce.helpers.AppConstants.REMOVE_FROM_READY_PRODUCT;
import static com.lalaland.ecommerce.helpers.AppConstants.SIGNIN_TOKEN;
import static com.lalaland.ecommerce.helpers.AppConstants.SUCCESS_CODE;
import static com.lalaland.ecommerce.helpers.AppConstants.TAG;
import static com.lalaland.ecommerce.helpers.AppConstants.VALIDATION_FAIL_CODE;

public class CartFragment extends Fragment implements View.OnClickListener, CartItemsAdapter.CartClickListener {

    private FragmentCartBinding fragmentCartBinding;
    private ProductViewModel productViewModel;
    CartItemsAdapter cartItemsAdapter;
    private AppPreference appPreference;
    private List<CartItem> cartItemList = new ArrayList<>();
    private Map<String, String> parameter = new HashMap<>();
    private Map<String, String> headers = new HashMap<>();
    private String token, cart_session;
    private Double totalBill = 0.0;
    private List<CartItem> selectedCartItemList = new ArrayList<>();
    boolean isSelected;
    Double perItemBill;

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

    private void getCartItems() {

        headers.clear();

        if (!token.isEmpty()) {

            headers.put(SIGNIN_TOKEN, token);

            productViewModel.getCart(headers).observe(this, cartContainer -> {

                if (cartContainer != null) {

                    if (cartContainer.getCode().equals(SUCCESS_CODE)) {
                        cartItemList = cartContainer.getData().getCartItems();
                        AppConstants.userAddresses = cartContainer.getData().getUserAddresses();
                        //userAddress = cartContainer.getData().getUserAddresses();
                        setCartAdapter();
                        Log.d(TAG, String.valueOf(cartContainer.getData().getCartItems().size()));
                    } else
                        Toast.makeText(getContext(), GENERAL_ERROR, Toast.LENGTH_SHORT).show();
                }

                fragmentCartBinding.pbLoading.setVisibility(View.GONE);
            });
        } else {

            headers.put(CART_SESSION_TOKEN, cart_session);

            productViewModel.getCart(headers).observe(this, cartContainer -> {

                if (cartContainer != null) {

                    if (cartContainer.getCode().equals(SUCCESS_CODE)) {
                        cartItemList = cartContainer.getData().getCartItems();
                        AppConstants.userAddresses = cartContainer.getData().getUserAddresses();

                        setCartAdapter();
                        Log.d(TAG, String.valueOf(cartContainer.getData().getCartItems().size()));
                    } else
                        Toast.makeText(getContext(), GENERAL_ERROR, Toast.LENGTH_SHORT).show();
                }

                fragmentCartBinding.pbLoading.setVisibility(View.GONE);
            });
        }
    }

    void setCartAdapter() {

        if (cartItemList.size() == 0) {
            fragmentCartBinding.tvCartEmptyState.setVisibility(View.VISIBLE);
            fragmentCartBinding.btnCheckout.setOnClickListener(null);
            fragmentCartBinding.btnCheckout.setBackground(getResources().getDrawable(R.drawable.btn_bg_round_corner_dark_gray));
        } else {


            fragmentCartBinding.tvCartEmptyState.setVisibility(View.GONE);
            fragmentCartBinding.btnCheckout.setBackground(getResources().getDrawable(R.drawable.btn_bg_round_corner_accent));
            fragmentCartBinding.btnCheckout.setOnClickListener(this);
        }

        cartItemsAdapter = new CartItemsAdapter(getContext(), this);
        fragmentCartBinding.rvCartItems.setLayoutManager(new LinearLayoutManager(getContext()));
        fragmentCartBinding.rvCartItems.setAdapter(cartItemsAdapter);
        cartItemsAdapter.setData(cartItemList);

    }

    @Override
    public void addItemToList(CartItem cartItem) {

        fragmentCartBinding.pbLoading.setVisibility(View.VISIBLE);
        parameter.clear();
        parameter.put(CART_ID, String.valueOf(cartItem.getCartId()));

        //isSelected = cartItem.getSelected();

        cartItem.setSelected(!cartItem.getSelected());

        cartItemList.get(cartItemList.indexOf(cartItem)).setSelected(cartItem.getSelected());

        if (cartItem.getSelected()) {
            selectedCartItemList.add(cartItem);
        } else
            selectedCartItemList.remove(cartItem);

        calCalculateBill();

        productViewModel.addToReadyCartList(headers, parameter).observe(this, basicResponse ->
        {
            if (basicResponse != null) {
                if (basicResponse.getCode().equals(SUCCESS_CODE)) {

                    if (cartItem.getSelected())
                        Toast.makeText(getContext(), ADD_TO_READY_PRODUCT, Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(getContext(), REMOVE_FROM_READY_PRODUCT, Toast.LENGTH_SHORT).show();

                    cartItemsAdapter.setData(cartItemList);

                } else {
                    Toast.makeText(getContext(), GENERAL_ERROR, Toast.LENGTH_SHORT).show();
                }
            } else
                Toast.makeText(getContext(), GENERAL_ERROR, Toast.LENGTH_SHORT).show();

            fragmentCartBinding.pbLoading.setVisibility(View.GONE);

        });
    }

    @Override
    public void deleteFromCart(CartItem cartItem) {

        fragmentCartBinding.pbLoading.setVisibility(View.VISIBLE);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        parameter.clear();
        parameter.put(CART_ID, String.valueOf(cartItem.getCartId()));
        parameter.put("is_delete_all", String.valueOf(0));

        fragmentCartBinding.pbLoading.setVisibility(View.VISIBLE);

        productViewModel.deleteCartItem(headers, parameter).observe(this, basicResponse ->
        {
            if (basicResponse != null) {
                if (basicResponse.getCode().equals(SUCCESS_CODE)) {

                    getCartItems();
                    selectedCartItemList.clear();
                    calCalculateBill();
                    /*if (selectedCartItemList.contains(cartItem)) {
                        selectedCartItemList.remove(cartItem);
                        calCalculateBill();
                    }

                    cartItemList.remove(cartItem);
                    cartItemsAdapter.setData(cartItemList);
                    *///    cartItemsAdapter.notifyItemRemoved(cartItemList.indexOf(cartItem));

                    Toast.makeText(getContext(), REMOVED_FROM_CART, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), GENERAL_ERROR, Toast.LENGTH_SHORT).show();
                }
            } else
                Toast.makeText(getContext(), GENERAL_ERROR, Toast.LENGTH_SHORT).show();

            fragmentCartBinding.pbLoading.setVisibility(View.GONE);
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        });
    }

    @Override
    public void changeNumberOfCount(CartItem cartItem, int quantity) {

        fragmentCartBinding.pbLoading.setVisibility(View.VISIBLE);
        parameter.clear();
        parameter.put(CART_ID, String.valueOf(cartItem.getCartId()));
        parameter.put(QUANTITY, String.valueOf(quantity));

        productViewModel.changeCartProductQuantity(parameter).observe(this, basicResponse ->
        {
            if (basicResponse != null) {
                if (basicResponse.getCode().equals(SUCCESS_CODE)) {

                    cartItemList.get(cartItemList.indexOf(cartItem)).setItemQuantity(quantity);
                    cartItemsAdapter.setData(cartItemList);

                    cartItem.setItemQuantity(quantity);

                    if (selectedCartItemList.contains(cartItem))
                        calCalculateBill();

                    Toast.makeText(getContext(), basicResponse.getMsg(), Toast.LENGTH_SHORT).show();
                } else if (basicResponse.getCode().equals(VALIDATION_FAIL_CODE)) {
                    Toast.makeText(getContext(), basicResponse.getMsg(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), GENERAL_ERROR, Toast.LENGTH_SHORT).show();
                }
            } else
                Toast.makeText(getContext(), GENERAL_ERROR, Toast.LENGTH_SHORT).show();
        });

        fragmentCartBinding.pbLoading.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_checkout:

                Log.d(TAG, "Checkout");

                if (getContext() != null) {

                    Intent intent;

                    if (token != null && !token.isEmpty()) {

                        if (!selectedCartItemList.isEmpty()) {
                            intent = new Intent(getContext(), CheckoutScreen.class);
                            intent.putExtra("total_bill", String.valueOf(totalBill));
                            intent.putParcelableArrayListExtra("ready_cart_items", (ArrayList<? extends Parcelable>) selectedCartItemList);
                        } else {
                            Toast.makeText(getContext(), "Please select some items to proceed", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    else
                        intent = new Intent(getContext(), RegistrationActivity.class);

                    getContext().startActivity(intent);

                }
                break;
        }
    }

    private void calCalculateBill() {


      /*  Double perItemBill = cartItem.getItemQuantity() * Double.parseDouble(cartItem.getSalePrice());

        if (!cartItem.getSelected()) {

            totalBill = totalBill + perItemBill;
            selectedCartItemList.add(cartItem);
        } else {
            totalBill = totalBill - perItemBill;
            selectedCartItemList.remove(cartItem);
        }*/

        perItemBill = 0.0;
        totalBill = 0.0;

        for (CartItem mCartItem : selectedCartItemList) {

            perItemBill = mCartItem.getItemQuantity() * Double.parseDouble(mCartItem.getSalePrice());
            totalBill += perItemBill;
        }

        fragmentCartBinding.tvTotalBalance.setText(String.valueOf(totalBill));
    }
}
