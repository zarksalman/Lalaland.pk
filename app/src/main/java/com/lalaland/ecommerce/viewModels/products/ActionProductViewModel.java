package com.lalaland.ecommerce.viewModels.products;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.lalaland.ecommerce.data.models.actionProducs.ActionProductsContainer;
import com.lalaland.ecommerce.data.repository.ProductsRepository;

import java.util.Map;

public class ActionProductViewModel extends AndroidViewModel {

    public ActionProductViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<ActionProductsContainer> getActionProducts(String action, Map<String, String> parameter) {

        return ProductsRepository.getInstance().getActionProducts(action, parameter);
    }
}
