package com.lalaland.ecommerce.data.pagging;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

public class ProductDataSourceFactory extends DataSource.Factory {

    MutableLiveData<ProductsDataSource> productMutableLiveData = new MutableLiveData<>();

    @NonNull
    @Override

    public DataSource create() {

        ProductsDataSource productDataSource = new ProductsDataSource();
        productMutableLiveData.postValue(productDataSource);

        return productDataSource;
    }

    public MutableLiveData<ProductsDataSource> getItemsLiveDataSource() {
        return productMutableLiveData;
    }
}
