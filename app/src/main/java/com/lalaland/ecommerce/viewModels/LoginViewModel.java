package com.lalaland.ecommerce.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.lalaland.ecommerce.data.models.login.Login;
import com.lalaland.ecommerce.data.models.login.LoginDataContainer;
import com.lalaland.ecommerce.data.models.logout.BasicResponse;
import com.lalaland.ecommerce.data.repository.UsersRepository;

import java.util.Map;

public class LoginViewModel extends AndroidViewModel {

    public LoginViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<LoginDataContainer> loginUser(String cart_session, Map<String, String> parameter) {
        return UsersRepository.getInstance().loginUser(cart_session, parameter);
    }

    public LiveData<BasicResponse> logoutUser() {
        return UsersRepository.getInstance().logoutUser();
    }
}
