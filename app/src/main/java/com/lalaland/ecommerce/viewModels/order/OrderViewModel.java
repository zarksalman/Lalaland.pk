package com.lalaland.ecommerce.viewModels.order;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.lalaland.ecommerce.data.models.DeliveryChargesData.DeliveryChargesContainer;
import com.lalaland.ecommerce.data.models.order.details.OrderDetailContainer;
import com.lalaland.ecommerce.data.models.order.myOrders.OrderDataContainer;
import com.lalaland.ecommerce.data.repository.OrdersRepository;

public class OrderViewModel extends AndroidViewModel {

    private OrdersRepository ordersRepository;

    public OrderViewModel(@NonNull Application application) {
        super(application);
        ordersRepository = OrdersRepository.getInstance();
    }

    public LiveData<DeliveryChargesContainer> getDeliveryCharges(String token, String cityId) {
        return ordersRepository.getDeliveryCharges(token, cityId);
    }

    public LiveData<OrderDataContainer> getMyOrders(String token, String status) {
        return ordersRepository.getMyOrders(token, status);
    }

    public LiveData<OrderDetailContainer> getMyOrdersProducts(String token, String orderId) {
        return ordersRepository.getMyOrdersProducts(token, orderId);
    }
}
