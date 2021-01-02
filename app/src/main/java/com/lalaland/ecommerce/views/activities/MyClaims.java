package com.lalaland.ecommerce.views.activities;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.adapters.ClaimAdapter;
import com.lalaland.ecommerce.data.models.returnAndReplacement.claimListing.Claim;
import com.lalaland.ecommerce.databinding.ActivityMyClaimsBinding;
import com.lalaland.ecommerce.helpers.AppConstants;
import com.lalaland.ecommerce.viewModels.returnAndReplacement.ReturnAndReplacementViewModel;

import java.util.ArrayList;
import java.util.List;

public class MyClaims extends AppCompatActivity {

    private ActivityMyClaimsBinding activityMyClaimsBinding;
    private ReturnAndReplacementViewModel returnAndReplacementViewModel;
    List<Claim> claimList = new ArrayList<>();
    List<Claim> claimsReturn = new ArrayList<>();
    List<Claim> claimsReplacement = new ArrayList<>();
    ClaimAdapter claimAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMyClaimsBinding = DataBindingUtil.setContentView(this, R.layout.activity_my_claims);

        setTablayout();
        getClaimList();

        activityMyClaimsBinding.btnBack.setOnClickListener(v -> onBackPressed());
    }

    private void setTablayout() {

        activityMyClaimsBinding.tabBar.addTab(activityMyClaimsBinding.tabBar.newTab().setText("All Claims"));
        activityMyClaimsBinding.tabBar.addTab(activityMyClaimsBinding.tabBar.newTab().setText("Returns"));
        activityMyClaimsBinding.tabBar.addTab(activityMyClaimsBinding.tabBar.newTab().setText("Replacements"));

        activityMyClaimsBinding.tabBar.setTabGravity(TabLayout.GRAVITY_FILL);

        View root = activityMyClaimsBinding.tabBar.getChildAt(0);
        if (root instanceof LinearLayout) {
            ((LinearLayout) root).setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
            GradientDrawable drawable = new GradientDrawable();
            drawable.setColor(getResources().getColor(R.color.colorMediumGray));
            drawable.setSize(1, 1);

            ((LinearLayout) root).setDividerDrawable(drawable);
        }

        activityMyClaimsBinding.tabBar.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setAdapter(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

    }

    private void getClaimList() {

        ReturnAndReplacementViewModel returnAndReplacementViewModel = new ViewModelProvider(this).get(ReturnAndReplacementViewModel.class);

        returnAndReplacementViewModel.getClaimsList().observe(this, claimListingDataContainer -> {

            if (claimListingDataContainer != null) {

                if (claimListingDataContainer.getCode().equals(AppConstants.SUCCESS_CODE)) {
                    claimList = claimListingDataContainer.getData().getClaims();
                    setAdapter(0);

                    createReturnList();
                    createRePlacementList();

                    claimAdapter.notifyDataSetChanged();
                }
            }
        });
    }


    private void setAdapter(int claimType) {

        activityMyClaimsBinding.pbLoading.setVisibility(View.VISIBLE);

        Intent intent = new Intent(this, ClaimDetail.class);

        claimAdapter = new ClaimAdapter(this, claim -> {

            intent.putExtra("claim_id", String.valueOf(claim.getId()));
            startActivity(intent);
        });

        activityMyClaimsBinding.rvAllClaims.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        activityMyClaimsBinding.rvAllClaims.setAdapter(claimAdapter);

        if (claimType == 0) {
            claimAdapter.setData(claimList);
        } else if (claimType == 1) {
            claimAdapter.setData(claimsReturn);
        } else if (claimType == 2) {
            claimAdapter.setData(claimsReplacement);
        }

        activityMyClaimsBinding.pbLoading.setVisibility(View.GONE);
    }

    private void createReturnList() {

        claimsReturn = new ArrayList<>();

        for (Claim claim : claimList) {
            if (claim.getReturnType() == 2 || claim.getReturnType() == 4)
                claimsReturn.add(claim);
        }
    }

    private void createRePlacementList() {

        claimsReplacement = new ArrayList<>();

        for (Claim claim : claimList) {
            if (claim.getReturnType() == 3 || claim.getReturnType() == 5)
                claimsReplacement.add(claim);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}