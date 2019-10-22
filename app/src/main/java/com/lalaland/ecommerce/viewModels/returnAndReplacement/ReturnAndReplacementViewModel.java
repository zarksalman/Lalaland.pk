package com.lalaland.ecommerce.viewModels.returnAndReplacement;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.lalaland.ecommerce.data.models.logout.BasicResponse;
import com.lalaland.ecommerce.data.models.returnAndReplacement.claimListing.ClaimListingDataContainer;
import com.lalaland.ecommerce.data.models.returnAndReplacement.createClaimDetail.ReturnAndReplacementDataContainer;
import com.lalaland.ecommerce.data.repository.ReturnAndReplacementRepository;
import com.lalaland.ecommerce.interfaces.NetworkInterface;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;

public class ReturnAndReplacementViewModel extends AndroidViewModel {

    private NetworkInterface mNetworkInterface;

    public ReturnAndReplacementViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<ReturnAndReplacementDataContainer> createNewClaim(String orderProductId) {

        return ReturnAndReplacementRepository.getInstance().createNewClaim(orderProductId);
    }

    public LiveData<BasicResponse> newClaimPost(List<MultipartBody.Part> claimImages , Map<String, String> parameter) {
        return ReturnAndReplacementRepository.getInstance().newClaimPost(claimImages, parameter);
    }

    public LiveData<ClaimListingDataContainer> getClaimsList() {
        return ReturnAndReplacementRepository.getInstance().getClaimList();
    }
}
