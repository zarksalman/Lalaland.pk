package com.lalaland.ecommerce.data.pagging;

import androidx.annotation.NonNull;
import androidx.paging.PositionalDataSource;

import com.lalaland.ecommerce.data.models.products.Product;
import com.lalaland.ecommerce.data.models.products.ProductContainer;
import com.lalaland.ecommerce.data.retrofit.RetrofitClient;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.lalaland.ecommerce.helpers.AppConstants.LENGTH;
import static com.lalaland.ecommerce.helpers.AppConstants.START_INDEX;

public class ProductsDataSource extends PositionalDataSource<Product> {

    public static int SIZE = 30;

    public static int totalCount;
    public static int position;
    public static int loadSize;

    private Map<String, String> parameter = new HashMap<>();

    public ProductsDataSource() {

    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams params, @NonNull LoadInitialCallback<Product> callback) {


        totalCount = SIZE;
        position = computeInitialLoadPosition(params, totalCount);
        loadSize = computeInitialLoadSize(params, position, totalCount);

        parameter.clear();
        parameter.put(START_INDEX, String.valueOf(position));
        parameter.put(LENGTH, String.valueOf(SIZE));


        RetrofitClient.getInstance().createClient().getRecommendations(parameter).enqueue(new Callback<ProductContainer>() {
            @Override
            public void onResponse(Call<ProductContainer> call, Response<ProductContainer> response) {

                if (response.isSuccessful()) {
                    callback.onResult(response.body().getProductData().getProducts(), position, totalCount);
                }
            }

            @Override
            public void onFailure(Call<ProductContainer> call, Throwable t) {

//                callback.onResult(null, position, 0);
            }
        });

    }

    @Override
    public void loadRange(@NonNull LoadRangeParams params, @NonNull LoadRangeCallback<Product> callback) {

        parameter.clear();
        parameter.put(START_INDEX, String.valueOf(params.startPosition));
        parameter.put(LENGTH, String.valueOf(params.loadSize));

        RetrofitClient.getInstance().createClient().getRecommendations(parameter).enqueue(new Callback<ProductContainer>() {
            @Override
            public void onResponse(Call<ProductContainer> call, Response<ProductContainer> response) {

            }

            @Override
            public void onFailure(Call<ProductContainer> call, Throwable t) {

            }
        });
    }
}
