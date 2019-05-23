package com.lalaland.ecommerce.data.pagging;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import com.lalaland.ecommerce.data.models.products.Product;
import com.lalaland.ecommerce.data.repository.ProductsRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.lalaland.ecommerce.helpers.AppConstants.LENGTH;
import static com.lalaland.ecommerce.helpers.AppConstants.START_INDEX;

public class ProductItemDataSource extends PageKeyedDataSource<Integer, Product> {

    public static int NUMBER_OF_PRODUCTS = 30;
    public static int START = 0;
    private Map<String, String> parameter = new HashMap<>();
    List<Product> list;

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, Product> callback) {
        parameter.clear();
        parameter.put(START_INDEX, String.valueOf(START));
        parameter.put(LENGTH, String.valueOf(NUMBER_OF_PRODUCTS));


        list = ProductsRepository.getInstance().getRangeProducts(parameter).getValue().getProductData().getProducts();
        callback.onResult(list, null, NUMBER_OF_PRODUCTS);

        START += NUMBER_OF_PRODUCTS;
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Product> callback) {

        parameter.clear();
        parameter.put(START_INDEX, String.valueOf(params.key));
        parameter.put(LENGTH, String.valueOf(NUMBER_OF_PRODUCTS));

        list = ProductsRepository.getInstance().getRangeProducts(parameter).getValue().getProductData().getProducts();

        Integer key = params.key > 0 ? params.key - 1 : null;
        callback.onResult(list, key);
    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Product> callback) {

        parameter.clear();
        parameter.put(START_INDEX, String.valueOf(params.key));
        parameter.put(LENGTH, String.valueOf(NUMBER_OF_PRODUCTS));

        list = ProductsRepository.getInstance().getRangeProducts(parameter).getValue().getProductData().getProducts();

        //Integer key = params.key. > 1 ? params.key + 1 : null;
        callback.onResult(list, params.key + 1);
    }
}
