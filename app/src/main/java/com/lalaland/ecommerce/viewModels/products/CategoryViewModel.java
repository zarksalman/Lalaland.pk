package com.lalaland.ecommerce.viewModels.products;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.lalaland.ecommerce.data.models.category.CategoryContainer;
import com.lalaland.ecommerce.data.repository.ProductsRepository;
import com.lalaland.ecommerce.interfaces.NetworkInterface;

import java.util.Map;

public class CategoryViewModel extends AndroidViewModel {

    private NetworkInterface mNetworkInterface;

    public CategoryViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<CategoryContainer> getActionProducts(NetworkInterface networkInterface) {

        mNetworkInterface = networkInterface;
        return ProductsRepository.getInstance().getCategoryGeneralData(networkInterface);
    }
}
