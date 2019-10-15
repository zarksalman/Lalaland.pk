package com.lalaland.ecommerce.viewModels.returnAndReplacement;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.lalaland.ecommerce.data.models.returnAndReplacement.ReturnAndReplacementDataContainer;
import com.lalaland.ecommerce.data.repository.ReturnAndReplacementRepository;
import com.lalaland.ecommerce.interfaces.NetworkInterface;

public class ReturnAndReplacementViewModel extends AndroidViewModel {

    private NetworkInterface mNetworkInterface;

    public ReturnAndReplacementViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<ReturnAndReplacementDataContainer> createNewClaim(String orderProductId) {

        return ReturnAndReplacementRepository.getInstance().createNewClaim(orderProductId);
    }
}
