package com.lalaland.ecommerce.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.lalaland.ecommerce.data.models.login.Login;
import com.lalaland.ecommerce.data.models.logout.BasicResponse;
import com.lalaland.ecommerce.data.repository.Repository;

import java.util.Map;

public class LoginViewModel extends AndroidViewModel {

    public LoginViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Login> loginUser(Map<String, String> parameter) {
        return Repository.getInstance().loginUser(parameter);
    }

    public LiveData<BasicResponse> logoutUser() {
        return Repository.getInstance().logoutUser();
    }
}
