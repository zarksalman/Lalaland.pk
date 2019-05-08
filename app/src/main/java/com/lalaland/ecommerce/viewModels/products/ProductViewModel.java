package com.lalaland.ecommerce.viewModels.products;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.lalaland.ecommerce.data.models.actionProducs.ActionProductsContainer;
import com.lalaland.ecommerce.data.models.logout.BasicResponse;
import com.lalaland.ecommerce.data.models.productDetails.ProductDetailDataContainer;
import com.lalaland.ecommerce.data.models.products.ProductContainer;
import com.lalaland.ecommerce.data.repository.ProductsRepository;

import java.util.Map;

public class ProductViewModel extends AndroidViewModel {

    public ProductViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<ProductContainer> getRangeProducts(Map<String, String> parameters) {

        return ProductsRepository.getInstance().getRangeProducts(parameters);
    }

    public LiveData<ActionProductsContainer> getActionProducts(String action, Map<String, String> parameter) {

        return ProductsRepository.getInstance().getActionProducts(action, parameter);
    }

    public LiveData<ProductDetailDataContainer> getProductDetail(int product_id) {
        return ProductsRepository.getInstance().getProductDetail(product_id);
    }

    public LiveData<BasicResponse> addToCart(Map<String, String> parameter) {
        return ProductsRepository.getInstance().addToCart(parameter);
    }

    public LiveData<BasicResponse> addRemoveToWishList(Map<String, String> parameter) {
        return ProductsRepository.getInstance().addRemoveToWishList(parameter);
    }
}
