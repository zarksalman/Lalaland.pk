package com.lalaland.ecommerce.viewModels.products;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;

import com.lalaland.ecommerce.data.models.home.HomeDataContainer;
import com.lalaland.ecommerce.data.models.products.Product;
import com.lalaland.ecommerce.data.models.products.ProductContainer;
import com.lalaland.ecommerce.data.pagging.ProductDataSource;
import com.lalaland.ecommerce.data.pagging.ProductDataSourceFactory;
import com.lalaland.ecommerce.data.repository.ProductsRepository;

import java.util.Map;

public class HomeViewModel extends AndroidViewModel {

    private ProductsRepository productsRepository;

    public LiveData<PagedList<Product>> pagedListLiveData;
    public LiveData<PageKeyedDataSource<Integer, Product>> liveDataSource;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        productsRepository = ProductsRepository.getInstance();

        ProductDataSourceFactory productDataSourceFactory = new ProductDataSourceFactory();
        liveDataSource = productDataSourceFactory.getItemsLiveDataSource();

        PagedList.Config config =
                (new PagedList.Config.Builder())
                        .setEnablePlaceholders(true)
                        .setPageSize(ProductDataSource.NUMBER_OF_PRODUCTS)
                        .build();

        pagedListLiveData = new LivePagedListBuilder(productDataSourceFactory, config).build();
    }

    public LiveData<PagedList<Product>> getPagedList() {
        return pagedListLiveData;
    }
    public LiveData<HomeDataContainer> getHomeData() {
        return productsRepository.getHomeData();
    }

    public LiveData<ProductContainer> getRecommendations(Map<String, String> parameters) {

        return productsRepository.getRecommendations(parameters);
    }
}
