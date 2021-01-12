package com.lalaland.ecommerce.viewModels.order;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.lalaland.ecommerce.data.models.DeliveryChargesData.DeliveryChargesContainer;
import com.lalaland.ecommerce.data.models.deliveryOption.DeliveryOptionDataContainer;
import com.lalaland.ecommerce.data.models.logout.BasicResponse;
import com.lalaland.ecommerce.data.models.order.details.OrderDetailContainer;
import com.lalaland.ecommerce.data.models.order.myOrders.OrderDataContainer;
import com.lalaland.ecommerce.data.repository.OrdersRepository;

import java.util.Map;

public class OrderViewModel extends AndroidViewModel {

    private OrdersRepository ordersRepository;

    public OrderViewModel(@NonNull Application application) {
        super(application);
        ordersRepository = OrdersRepository.getInstance();
    }

    public LiveData<DeliveryChargesContainer> getDeliveryCharges(String token, String cityId) {
        return ordersRepository.getDeliveryCharges(token, cityId);
    }

    public LiveData<DeliveryOptionDataContainer> getDeliveryOption(Map<String, String> parameter) {
        return ordersRepository.getDeliveryOption(parameter);
    }


    public LiveData<OrderDataContainer> getMyOrders(String token, String status) {
        return ordersRepository.getMyOrders(token, status);
    }

    public LiveData<OrderDetailContainer> getMyOrdersProducts(String token, String orderId) {
        return ordersRepository.getMyOrdersProducts(token, orderId);
    }

    public LiveData<BasicResponse> checkPayProPaymentStatus(String token, String orderId) {
        return ordersRepository.checkPayProPaymentStatus(token, orderId);
    }
}
