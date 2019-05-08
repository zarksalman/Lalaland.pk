package com.lalaland.ecommerce.views.fragments.homeFragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.adapters.CategoryAdapter;
import com.lalaland.ecommerce.data.models.category.Category;
import com.lalaland.ecommerce.databinding.FragmentCategoryBinding;
import com.lalaland.ecommerce.helpers.AppConstants;
import com.lalaland.ecommerce.viewModels.products.CategoryViewModel;
import com.lalaland.ecommerce.views.activities.RegistrationActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.LongStream;


public class CategoryFragment extends Fragment implements CategoryAdapter.CategoryClickListener {

    private FragmentCategoryBinding fragmentCategoryBinding;
    private CategoryViewModel categoryViewModel;
    private List<Category> categoryList = new ArrayList<>();
    private CategoryAdapter categoryAdapter;

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


//        initUi();
        return fragmentCategoryBinding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

        categoryViewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);

        Glide
                .with(getContext())
                .load(AppConstants.testImagesUrl.get(0))
                .into(fragmentCategoryBinding.ivCategoryHeader);
        
        setCategoryList();
    }

    private void setCategoryList() {


        categoryList = new ArrayList<>();
        categoryList = AppConstants.staticCategoryList;
        categoryAdapter = new CategoryAdapter(getContext(), this);
        fragmentCategoryBinding.rvCategory.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        fragmentCategoryBinding.rvCategory.setAdapter(categoryAdapter);
        fragmentCategoryBinding.rvCategory.setHasFixedSize(true);

        categoryAdapter.setData(categoryList);

    }


    @Override
    public void onCategoryClicked(Category category) {
        Toast.makeText(getContext(), category.getName(), Toast.LENGTH_SHORT).show();
    }
}
