package com.lalaland.ecommerce.data.pagging;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import com.lalaland.ecommerce.data.models.products.Product;

public class ProductSourceFactory extends DataSource.Factory {

    MutableLiveData<PageKeyedDataSource<Integer, Product>> productMutableLiveData = new MutableLiveData<>();

    @NonNull
    @Override

    public DataSource create() {

        ProductItemDataSource productItemDataSource = new ProductItemDataSource();
        productMutableLiveData.postValue(productItemDataSource);

        return productItemDataSource;
    }

    public MutableLiveData<PageKeyedDataSource<Integer, Product>> getItemsLiveDataSource() {
        return productMutableLiveData;
    }
}
