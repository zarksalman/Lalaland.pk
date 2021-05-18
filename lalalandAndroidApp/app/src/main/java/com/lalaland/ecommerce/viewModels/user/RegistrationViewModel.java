package com.lalaland.ecommerce.viewModels.user;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.lalaland.ecommerce.data.models.registration.RegistrationContainer;
import com.lalaland.ecommerce.data.repository.UsersRepository;

import java.util.Map;

public class RegistrationViewModel extends AndroidViewModel {

    public RegistrationViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<RegistrationContainer> registerUser(String cart_session, Map<String, String> parameter, int signUpType) {
        return UsersRepository.getInstance().registerUser(cart_session, parameter, signUpType);
    }
}
