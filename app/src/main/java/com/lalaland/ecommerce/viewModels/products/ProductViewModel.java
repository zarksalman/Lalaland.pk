package com.lalaland.ecommerce.viewModels.products;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.lalaland.ecommerce.data.models.actionProducs.ActionProductsContainer;
import com.lalaland.ecommerce.data.models.cart.CartContainer;
import com.lalaland.ecommerce.data.models.logout.BasicResponse;
import com.lalaland.ecommerce.data.models.order.OrderDataContainer;
import com.lalaland.ecommerce.data.models.productDetails.ProductDetailDataContainer;
import com.lalaland.ecommerce.data.models.products.ProductContainer;
import com.lalaland.ecommerce.data.repository.ProductsRepository;

import java.util.Map;

public class ProductViewModel extends AndroidViewModel {

    private static ProductsRepository productsRepository;
    private MutableLiveData<BasicResponse> basicResponse;

    public ProductViewModel(@NonNull Application application) {
        super(application);

        productsRepository = ProductsRepository.getInstance();
    }

    public LiveData<ProductContainer> getRangeProducts(Map<String, String> parameters) {

        return productsRepository.getRangeProducts(parameters);
    }


    public LiveData<ActionProductsContainer> getActionProducts(String action, Map<String, String> parameter) {

        return productsRepository.getActionProducts(action, parameter);
    }

    public LiveData<ProductDetailDataContainer> getProductDetail(int product_id) {
        return productsRepository.getProductDetail(product_id);
    }

    public LiveData<BasicResponse> addToCart(Map<String, String> headers, Map<String, String> parameter) {

        //basicResponse = productsRepository.addToCart(headers, parameter);
        return productsRepository.addToCart(headers, parameter);
    }

    public MutableLiveData<BasicResponse> getBasicResponse() {
        return basicResponse;
    }

    public LiveData<BasicResponse> addRemoveToWishList(Map<String, String> headers, Map<String, String> parameter) {
        //basicResponse = productsRepository.addRemoveToWishList(headers, parameter);
        return productsRepository.addRemoveToWishList(headers, parameter);
    }

    public LiveData<CartContainer> getCart(Map<String, String> headers) {
        return productsRepository.getCart(headers);
    }

    public LiveData<BasicResponse> addToReadyCartList(Map<String, String> header, Map<String, String> parameter) {
        return productsRepository.addToReadyCartList(header, parameter);
    }

    public LiveData<BasicResponse> deleteCartItem(Map<String, String> header, Map<String, String> parameter) {
        return productsRepository.deleteCartItem(header, parameter);
    }

    public LiveData<BasicResponse> changeCartProductQuantity(Map<String, String> parameter) {
        return productsRepository.changeCartProductQuantity(parameter);
    }

    public LiveData<OrderDataContainer> confirmOrder(String header, Map<String, String> parameter) {
        return productsRepository.confirmOrder(header, parameter);
    }
}
