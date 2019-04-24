package com.lalaland.ecommerce.views.fragments.homeFragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lalaland.ecommerce.R;


public class WishFragment extends Fragment {

    public WishFragment() {
        // Required empty public constructor
    }


    public static WishFragment newInstance() {
        WishFragment fragment = new WishFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wish, container, false);
    }

}
