package com.lalaland.ecommerce.viewModels.user;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.lalaland.ecommerce.data.models.userAddressBook.AddressDataContainer;
import com.lalaland.ecommerce.data.repository.UserRepository;

import java.util.Map;

public class UserViewModel extends AndroidViewModel {

    private UserRepository userRepository;

    public UserViewModel(@NonNull Application application) {
        super(application);

        userRepository = UserRepository.getInstance();
    }

    public LiveData<AddressDataContainer> addNewAddress(String token, Map<String, String> parameter) {
        return userRepository.addNewAddress(token, parameter);
    }
}
