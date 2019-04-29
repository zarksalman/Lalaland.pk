package com.lalaland.ecommerce.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.lalaland.ecommerce.data.models.logout.BasicResponse;
import com.lalaland.ecommerce.data.models.registration.RegistrationContainer;
import com.lalaland.ecommerce.data.repository.Repository;

import java.util.Map;

public class RegistrationViewModel extends AndroidViewModel {

    public RegistrationViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<RegistrationContainer> registerUser(Map<String, String> parameter, int signUpType) {
        return Repository.getInstance().registerUser(parameter, signUpType);
    }

    public LiveData<BasicResponse> changePassword(Map<String, String> parameter) {
        return Repository.getInstance().changePassword(parameter);
    }
}
