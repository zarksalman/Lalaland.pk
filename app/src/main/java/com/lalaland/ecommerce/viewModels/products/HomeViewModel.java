package com.lalaland.ecommerce.viewModels.products;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.PagedList;

import com.lalaland.ecommerce.data.models.home.HomeDataContainer;
import com.lalaland.ecommerce.data.models.products.Product;
import com.lalaland.ecommerce.data.models.products.ProductContainer;

import com.lalaland.ecommerce.data.repository.ProductsRepository;

import java.util.Map;

public class HomeViewModel extends ViewModel {

    private ProductsRepository productsRepository;

    public LiveData<PagedList<Product>> pagedListLiveData;

    //    public HomeViewModel(@NonNull Application application)
    public HomeViewModel() {
        //   super(application);
        productsRepository = ProductsRepository.getInstance();

/*
        productDataSourceFactory = new ProductDataSourceFactory();

        PagedList.Config pagedListConfig =
                (new PagedList.Config.Builder())
                        .setEnablePlaceholders(false)
                        .setPageSize(ProductsDataSource.SIZE)
                        .setInitialLoadSizeHint(ProductsDataSource.totalCount)
                        .build();

        pagedListLiveData = (new LivePagedListBuilder(productDataSourceFactory, pagedListConfig)).build();

        liveDataSource = productDataSourceFactory.getItemsLiveDataSource();
*/

/*        ProductDataSourceFactory productDataSourceFactory = new ProductDataSourceFactory();
        liveDataSource = productDataSourceFactory.getItemsLiveDataSource();

        PagedList.Config config =
                (new PagedList.Config.Builder())
                        .setEnablePlaceholders(true)
                        .setPageSize(ProductDataSource.NUMBER_OF_PRODUCTS)
                        .build();

        pagedListLiveData = new LivePagedListBuilder(productDataSourceFactory, config).build();*/
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
