package com.lalaland.ecommerce.data.pagging;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import com.lalaland.ecommerce.data.models.products.Product;
import com.lalaland.ecommerce.data.models.products.ProductContainer;
import com.lalaland.ecommerce.data.retrofit.RetrofitClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.lalaland.ecommerce.helpers.AppConstants.LENGTH;
import static com.lalaland.ecommerce.helpers.AppConstants.START_INDEX;

public class ProductDataSource extends PageKeyedDataSource<Integer, Product> {

    public static final int SIZE = 30;
    public static int NUMBER_OF_PRODUCTS = 30;
    public static int START = 0;

    private Map<String, String> parameter = new HashMap<>();
    List<Product> list;

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, Product> callback) {
        parameter.clear();
        parameter.put(START_INDEX, String.valueOf(START));
        parameter.put(LENGTH, String.valueOf(SIZE));


        RetrofitClient.getInstance().createClient().getRecommendations(parameter).enqueue(new Callback<ProductContainer>() {
            @Override
            public void onResponse(Call<ProductContainer> call, Response<ProductContainer> response) {

                if (response.isSuccessful()) {
                    START = SIZE + 1;
                    callback.onResult(response.body().getProductData().getProducts(), null, START);
                }
            }

            @Override
            public void onFailure(Call<ProductContainer> call, Throwable t) {

            }
        });
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Product> callback) {

        /*
        parameter.clear();
        parameter.put(START_INDEX, String.valueOf(params.key));
        parameter.put(LENGTH, String.valueOf(NUMBER_OF_PRODUCTS));

        list = ProductsRepository.getInstance().getRangeProducts(parameter).getValue().getProductData().getProducts();

        Integer key = params.key > 0 ? params.key - 1 : null;
        callback.onResult(list, key);*/
    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Product> callback) {

        parameter.clear();
        parameter.put(START_INDEX, String.valueOf(params.key));
        parameter.put(LENGTH, String.valueOf(SIZE));

        RetrofitClient.getInstance().createClient().getRecommendations(parameter).enqueue(new Callback<ProductContainer>() {
            @Override
            public void onResponse(Call<ProductContainer> call, Response<ProductContainer> response) {

                if (response.isSuccessful()) {

                    if (response.body().getProductData().getProducts().size() > 0) {

                        START = SIZE + 1;
                        callback.onResult(response.body().getProductData().getProducts(), START);
                    }
                }
            }

            @Override
            public void onFailure(Call<ProductContainer> call, Throwable t) {

            }
        });
    }
}
