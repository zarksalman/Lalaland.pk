package com.lalaland.ecommerce.views.fragments.homeFragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;

import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.adapters.ProductAdapter;
import com.lalaland.ecommerce.data.models.products.Product;
import com.lalaland.ecommerce.databinding.FragmentHomeBinding;
import com.lalaland.ecommerce.viewModels.ProductViewModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {

    private ProductViewModel productViewModel;
    private FragmentHomeBinding fragmentHomeBinding;
    private ProductAdapter productAdapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentHomeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        return fragmentHomeBinding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();


        Map<String, String> parameter = new HashMap<>();

        parameter.put("start", "0");
        parameter.put("length", "40");

/*        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        LalalandServiceApi lalalandServiceApi = retrofit.create(LalalandServiceApi.class);

        lalalandServiceApi.getRangeProducts(parameter).enqueue(new Callback<ProductContainer>() {
            @Override
            public void onResponse(Call<ProductContainer> call, Response<ProductContainer> response) {
                setProductRecyclerView(response.body().getProductData().getProducts());
                fragmentHomeBinding.pbLoading.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ProductContainer> call, Throwable t) {

            }
        });*/

        productViewModel = ViewModelProviders.of(this).get(ProductViewModel.class);
        productViewModel.getRangeProducts(parameter).observe(this, productContainer -> {

            if (productContainer != null) {

                setProductRecyclerView(productContainer.getProductData().getProducts());
                fragmentHomeBinding.pbLoading.setVisibility(View.GONE);
            }
        });
    }

    private void setProductRecyclerView(List<Product> productList) {

        productAdapter = new ProductAdapter(getContext());
        fragmentHomeBinding.rvProducts.setLayoutManager(new GridLayoutManager(getContext(), 2));
        fragmentHomeBinding.rvProducts.setAdapter(productAdapter);
        productAdapter.setData(productList);
    }
}
