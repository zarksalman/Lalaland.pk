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
import com.lalaland.ecommerce.helpers.AppUtils;
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

                        setCartAdapter();
                        setSelectedCartItemList();

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


            for (int i = 0; i < cartItemList.size(); i++) {
                Log.d(TAG, "adapter_items" + cartItemList.get(i).getProductName());
            }
            fragmentCartBinding.tvCartEmptyState.setVisibility(View.GONE);
            fragmentCartBinding.btnCheckout.setBackground(getResources().getDrawable(R.drawable.btn_bg_round_corner_accent));
            fragmentCartBinding.btnCheckout.setOnClickListener(this);
        }

        cartItemsAdapter = new CartItemsAdapter(getContext(), this);
        fragmentCartBinding.rvCartItems.setLayoutManager(new LinearLayoutManager(getContext()));
        fragmentCartBinding.rvCartItems.setAdapter(cartItemsAdapter);
        cartItemsAdapter.setData(cartItemList);

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
    public void addItemToList(int position) {

        fragmentCartBinding.pbLoading.setVisibility(View.VISIBLE);

        CartItem cartItem = cartItemList.get(position);

        parameter.clear();
        parameter.put(CART_ID, String.valueOf(cartItem.getCartId()));

        //isSelected = cartItem.getSelected();

        //cartItem.setSelected(!cartItem.getSelected());

        if (cartItem.getCartStatus() == 1) {
            cartItemList.get(position).setCartStatus(3);
            selectedCartItemList.add(cartItem);
        } else if (cartItem.getCartStatus() == 3) {
            cartItemList.get(position).setCartStatus(1);
            selectedCartItemList.remove(cartItem); //delete item if exists
        }

        calCalculateBill();

        productViewModel.addToReadyCartList(headers, parameter).observe(this, basicResponse ->
        {
            if (basicResponse != null) {
                if (basicResponse.getCode().equals(SUCCESS_CODE)) {

                    if (cartItemList.get(position).getCartStatus() == 1) // its status was 1
                        Toast.makeText(getContext(), REMOVE_FROM_READY_PRODUCT, Toast.LENGTH_SHORT).show();
                    else if (cartItemList.get(position).getCartStatus() == 3) // its status was 3
                        Toast.makeText(getContext(), ADD_TO_READY_PRODUCT, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), GENERAL_ERROR, Toast.LENGTH_SHORT).show();
                }
            } else
                Toast.makeText(getContext(), GENERAL_ERROR, Toast.LENGTH_SHORT).show();

            fragmentCartBinding.pbLoading.setVisibility(View.GONE);

        });
    }

    @Override
    public void deleteFromCart(int position) {

        fragmentCartBinding.pbLoading.setVisibility(View.VISIBLE);

        for (int i = 0; i < cartItemList.size(); i++) {
            Log.d(TAG, "adapter_items" + cartItemList.get(i).getProductName());
        }

        CartItem cartItem = cartItemList.get(position);
        parameter.clear();
        parameter.put(CART_ID, String.valueOf(cartItem.getCartId()));
        parameter.put("is_delete_all", String.valueOf(0));

        fragmentCartBinding.pbLoading.setVisibility(View.VISIBLE);

        productViewModel.deleteCartItem(headers, parameter).observe(this, basicResponse ->
        {
            if (basicResponse != null) {
                if (basicResponse.getCode().equals(SUCCESS_CODE)) {

                    Log.d(TAG, "DELETE_ITEMS #" + cartItemList.indexOf(cartItem));

                    if (selectedCartItemList.size() > position) {
                        selectedCartItemList.remove(position);
                        calCalculateBill();
                    }

                    cartItemList.remove(position);
                    cartItemsAdapter.notifyItemRemoved(position);

                    if (cartItemList.size() < 1) {
                        fragmentCartBinding.tvCartEmptyState.setVisibility(View.VISIBLE);
                        fragmentCartBinding.btnCheckout.setBackground(getResources().getDrawable(R.drawable.btn_bg_round_corner_dark_gray));
                    } else
                        fragmentCartBinding.tvCartEmptyState.setVisibility(View.GONE);

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
    public void changeNumberOfCount(int position, int quantity) {

        fragmentCartBinding.pbLoading.setVisibility(View.VISIBLE);

        CartItem cartItem = cartItemList.get(position);

        parameter.clear();
        parameter.put(CART_ID, String.valueOf(cartItem.getCartId()));
        parameter.put(QUANTITY, String.valueOf(quantity));

        productViewModel.changeCartProductQuantity(parameter).observe(this, basicResponse ->
        {
            if (basicResponse != null) {
                if (basicResponse.getCode().equals(SUCCESS_CODE)) {

                    cartItemList.get(position).setItemQuantity(quantity);
                    cartItemsAdapter.notifyDataSetChanged();

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
}
