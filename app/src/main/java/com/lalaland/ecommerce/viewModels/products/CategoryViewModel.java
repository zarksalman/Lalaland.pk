package com.lalaland.ecommerce.viewModels.products;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.lalaland.ecommerce.data.models.category.CategoryContainer;
import com.lalaland.ecommerce.data.repository.ProductsRepository;

import java.util.Map;

public class CategoryViewModel extends AndroidViewModel {

    public CategoryViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<CategoryContainer> getActionProducts(Map<String, String> headers) {

        return ProductsRepository.getInstance().getCategoryGeneralData(headers);
    }
}
