package com.lalaland.ecommerce.viewModels.productsViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.lalaland.ecommerce.data.models.home.HomeDataContainer;
import com.lalaland.ecommerce.data.models.products.ProductContainer;
import com.lalaland.ecommerce.data.repository.ProductsRepository;
import com.lalaland.ecommerce.data.repository.Repository;

import java.util.Map;

public class HomeViewModel extends AndroidViewModel {

    public HomeViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<HomeDataContainer> getHomeData() {

        return ProductsRepository.getInstance().getHomeData();
    }
}
