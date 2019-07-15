package com.lalaland.ecommerce.views.fragments.homeFragments;


import android.content.Intent;
import android.os.Bundle;
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
import com.lalaland.ecommerce.adapters.WishlistProductAdapter;
import com.lalaland.ecommerce.data.models.wishList.WishListProduct;
import com.lalaland.ecommerce.databinding.FragmentWishBinding;
import com.lalaland.ecommerce.helpers.AppPreference;
import com.lalaland.ecommerce.viewModels.products.ProductViewModel;
import com.lalaland.ecommerce.views.activities.ProductDetailActivity;
import com.lalaland.ecommerce.views.activities.RegistrationActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static com.lalaland.ecommerce.helpers.AppConstants.GENERAL_ERROR;
import static com.lalaland.ecommerce.helpers.AppConstants.IS_WISH_LIST;
import static com.lalaland.ecommerce.helpers.AppConstants.PRODUCT_ID;
import static com.lalaland.ecommerce.helpers.AppConstants.SIGNIN_TOKEN;
import static com.lalaland.ecommerce.helpers.AppConstants.SUCCESS_CODE;
import static com.lalaland.ecommerce.helpers.AppConstants.TAG;


public class WishFragment extends Fragment implements WishlistProductAdapter.ProductListener {

    private ProductViewModel productViewModel;
    private FragmentWishBinding fragmentWishBinding;
    private String token;
    private AppPreference appPreference;
    private List<WishListProduct> wishListProductList;
    private WishlistProductAdapter wishlistProductAdapter;
    private Map<String, String> parameter, header;

    public WishFragment() {
        // Required empty public constructor
    }


    public static WishFragment newInstance() {
        WishFragment fragment = new WishFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fragmentWishBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_wish, container, false);

        appPreference = AppPreference.getInstance(getContext());
        token = appPreference.getString(SIGNIN_TOKEN);

        fragmentWishBinding.btnLogin.setOnClickListener(v -> {
            startActivityForResult(new Intent(getContext(), RegistrationActivity.class), 100);
        });

        productViewModel = ViewModelProviders.of(this).get(ProductViewModel.class);
        return fragmentWishBinding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

        if (token.isEmpty()) {
            fragmentWishBinding.ivEmptyState.setVisibility(View.VISIBLE);
            fragmentWishBinding.loginContainer.setVisibility(View.VISIBLE);
        } else {
            fragmentWishBinding.pbLoading.setVisibility(View.VISIBLE);
            getWishListProducts();
        }
    }

    void getWishListProducts() {

        wishListProductList = new ArrayList<>();

        productViewModel.getWishListProducts(token).observe(this, wishListContainer -> {

            if (wishListContainer != null) {

                if (wishListContainer.getCode().equals(SUCCESS_CODE)) {

                    wishListProductList.addAll(wishListContainer.getData().getWishListProducts());

                    if (wishListProductList.size() > 0) {
                        fragmentWishBinding.rvWishlist.setVisibility(View.VISIBLE);
                        setAdapter();
                    }
                    else {
                        fragmentWishBinding.ivEmptyState.setVisibility(View.VISIBLE);
                        fragmentWishBinding.tvEmptyState.setVisibility(View.VISIBLE);
                        fragmentWishBinding.rvWishlist.setVisibility(View.GONE);
                        fragmentWishBinding.pbLoading.setVisibility(View.GONE);
                    }
                    Log.d(TAG, "getWishListProducts:" + wishListContainer.getData().getWishListProducts().size());
                }
            } else
                Toast.makeText(getContext(), GENERAL_ERROR, Toast.LENGTH_SHORT).show();
        });
    }

    void setAdapter() {

        wishlistProductAdapter = new WishlistProductAdapter(getContext(), this);
        fragmentWishBinding.rvWishlist.setLayoutManager(new LinearLayoutManager(getContext()));
        fragmentWishBinding.rvWishlist.setAdapter(wishlistProductAdapter);
        wishlistProductAdapter.setData(wishListProductList);

        fragmentWishBinding.pbLoading.setVisibility(View.GONE);
    }

    @Override
    public void onProductProductClicked(WishListProduct wishListProduct, boolean isDelete) {

        if (isDelete) {

            parameter = new HashMap<>();
            header = new HashMap<>();

            parameter.clear();
            header.clear();

            parameter.put(PRODUCT_ID, String.valueOf(wishListProduct.getId()));
            parameter.put(IS_WISH_LIST, String.valueOf(0));

            header.put(SIGNIN_TOKEN, token);

            productViewModel.addRemoveToWishList(header, parameter).observe(this, basicResponse -> {

                if (basicResponse != null) {


                    if (!wishListProductList.contains(wishListProduct))
                        return;

                    int position = wishListProductList.indexOf(wishListProduct);
                    wishListProductList.remove(position);
                    wishlistProductAdapter.notifyItemRemoved(position);
                    
                    Toast.makeText(getContext(), basicResponse.getMsg(), Toast.LENGTH_SHORT).show();

                    if (wishListProductList.size() <= 0) {

                        fragmentWishBinding.ivEmptyState.setVisibility(View.VISIBLE);
                        fragmentWishBinding.tvEmptyState.setVisibility(View.VISIBLE);
                    }
                }
            });
        } else {

            Intent intent = new Intent(getContext(), ProductDetailActivity.class);
            intent.putExtra(PRODUCT_ID, wishListProduct.getId());
            startActivity(intent);

        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == 100) {

                token = appPreference.getString(SIGNIN_TOKEN);

                fragmentWishBinding.pbLoading.setVisibility(View.VISIBLE);
                fragmentWishBinding.loginContainer.setVisibility(View.GONE);
                fragmentWishBinding.ivEmptyState.setVisibility(View.GONE);

                getWishListProducts();
            }
        }
    }
}
