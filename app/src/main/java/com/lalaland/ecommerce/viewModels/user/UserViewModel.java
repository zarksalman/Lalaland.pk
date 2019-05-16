package com.lalaland.ecommerce.viewModels.user;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.lalaland.ecommerce.data.models.userAddressBook.AddressDataContainer;
import com.lalaland.ecommerce.data.repository.UsersRepository;

import java.util.Map;

public class UserViewModel extends AndroidViewModel {

    private UsersRepository usersRepository;

    public UserViewModel(@NonNull Application application) {
        super(application);

        usersRepository = UsersRepository.getInstance();
    }

    public LiveData<AddressDataContainer> addNewAddress(String token, Map<String, String> parameter) {
        return usersRepository.addNewAddress(token, parameter);
    }
}
