package com.lalaland.ecommerce.data.pagging;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import com.lalaland.ecommerce.data.models.products.Product;

public class ProductDataSourceFactory extends DataSource.Factory {

    MutableLiveData<PageKeyedDataSource<Integer, Product>> productMutableLiveData = new MutableLiveData<>();

    @NonNull
    @Override

    public DataSource create() {

        ProductDataSource productDataSource = new ProductDataSource();
        productMutableLiveData.postValue(productDataSource);

        return productDataSource;
    }

    public MutableLiveData<PageKeyedDataSource<Integer, Product>> getItemsLiveDataSource() {
        return productMutableLiveData;
    }
}
