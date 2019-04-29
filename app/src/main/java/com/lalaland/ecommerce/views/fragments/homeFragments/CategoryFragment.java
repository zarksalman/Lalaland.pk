package com.lalaland.ecommerce.views.fragments.homeFragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.databinding.FragmentCategoryBinding;
import com.lalaland.ecommerce.views.activities.RegistrationActivity;


public class CategoryFragment extends Fragment {

    private FragmentCategoryBinding fragmentCategoryBinding;

    public CategoryFragment() {
        // Required empty public constructor
    }


    public static CategoryFragment newInstance() {
        CategoryFragment fragment = new CategoryFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fragmentCategoryBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_category, container, false);

        fragmentCategoryBinding.btnRegistration.setOnClickListener(v -> startActivity(new Intent(getContext(), RegistrationActivity.class)));

        return fragmentCategoryBinding.getRoot();
    }

}
