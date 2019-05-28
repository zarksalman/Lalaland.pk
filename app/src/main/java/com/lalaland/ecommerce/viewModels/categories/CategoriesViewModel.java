package com.lalaland.ecommerce.viewModels.categories;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PagedList;

import com.lalaland.ecommerce.data.models.categories.CategoriesContainer;
import com.lalaland.ecommerce.data.models.logout.BasicResponse;
import com.lalaland.ecommerce.data.models.products.Product;
import com.lalaland.ecommerce.data.repository.ProductsRepository;

public class CategoriesViewModel extends AndroidViewModel {

    private static ProductsRepository productsRepository;
    private MutableLiveData<BasicResponse> basicResponse;
    private LiveData<PagedList<Product>> productPageList;
    private LiveData<CategoriesContainer> categoriesContainerLiveData;

    public CategoriesViewModel(@NonNull Application application) {
        super(application);

        productsRepository = ProductsRepository.getInstance();
    }

    public LiveData<CategoriesContainer> getCategories(String id) {

        return productsRepository.getCategories(id);
    }
}
