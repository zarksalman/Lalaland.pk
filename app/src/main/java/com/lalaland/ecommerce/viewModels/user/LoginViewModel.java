package com.lalaland.ecommerce.viewModels.user;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.lalaland.ecommerce.data.models.login.LoginDataContainer;
import com.lalaland.ecommerce.data.models.logout.BasicResponse;
import com.lalaland.ecommerce.data.repository.UsersRepository;

import java.util.Map;

public class LoginViewModel extends AndroidViewModel {

    private UsersRepository usersRepository;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        usersRepository = UsersRepository.getInstance();
    }

    public LiveData<LoginDataContainer> loginUser(String cart_session, Map<String, String> parameter) {
        return usersRepository.loginUser(cart_session, parameter);
    }

    public LiveData<BasicResponse> logoutUser() {
        return usersRepository.logoutUser();
    }

    public LiveData<BasicResponse> forgotPassword(Map<String, String> parameter) {
        return usersRepository.forgotPassword(parameter);
    }

    public LiveData<BasicResponse> resetPassword(Map<String, String> parameter) {
        return usersRepository.resetPassword(parameter);
    }

}
