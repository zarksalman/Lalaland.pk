package com.lalaland.ecommerce.viewModels.products;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.lalaland.ecommerce.data.models.home.HomeDataContainer;
import com.lalaland.ecommerce.data.models.products.ProductContainer;
import com.lalaland.ecommerce.data.repository.ProductsRepository;

import java.util.Map;

public class HomeViewModel extends AndroidViewModel {

    private ProductsRepository productsRepository;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        productsRepository = ProductsRepository.getInstance();
    }

    public LiveData<HomeDataContainer> getHomeData() {
        return productsRepository.getHomeData();
    }

    public LiveData<ProductContainer> getRecommendations(Map<String, String> parameters) {

        return productsRepository.getRecommendations(parameters);
    }
}
