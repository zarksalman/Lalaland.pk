package com.lalaland.ecommerce.viewModels.filter;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.lalaland.ecommerce.data.models.actionProducs.ActionProductsContainer;
import com.lalaland.ecommerce.data.models.categories.CategoriesContainer;
import com.lalaland.ecommerce.data.models.filters.FilterDataContainer;
import com.lalaland.ecommerce.data.models.logout.BasicResponse;
import com.lalaland.ecommerce.data.repository.ProductsRepository;

import java.util.Map;

public class FilterViewModel extends AndroidViewModel {

    private static ProductsRepository productsRepository;
    private MutableLiveData<BasicResponse> basicResponse;
    private LiveData<CategoriesContainer> categoriesContainerLiveData;

    public FilterViewModel(@NonNull Application application) {
        super(application);

        productsRepository = ProductsRepository.getInstance();
    }

    public LiveData<FilterDataContainer> getCategories(Map<String, String> parameter) {

        return productsRepository.getFilters(parameter);
    }

    public LiveData<ActionProductsContainer> applyFilters(Map<String, String> parameter) {
        return productsRepository.applyFilters(parameter);
    }
}
