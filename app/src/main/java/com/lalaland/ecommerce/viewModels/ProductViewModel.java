package com.lalaland.ecommerce.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.lalaland.ecommerce.data.models.products.ProductContainer;
import com.lalaland.ecommerce.data.repository.Repository;

import java.util.Map;

public class ProductViewModel extends AndroidViewModel {

    public ProductViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<ProductContainer> getRangeProducts(Map<String, String> parameters) {

        return Repository.getInstance().getRangeProducts(parameters);
    }
}
