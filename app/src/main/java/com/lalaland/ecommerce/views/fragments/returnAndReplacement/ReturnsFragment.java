package com.lalaland.ecommerce.views.fragments.returnAndReplacement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.adapters.ClaimAdapter;
import com.lalaland.ecommerce.data.models.returnAndReplacement.claimListing.Claim;
import com.lalaland.ecommerce.databinding.FragmentClaimsBinding;
import com.lalaland.ecommerce.helpers.AppConstants;
import com.lalaland.ecommerce.viewModels.returnAndReplacement.ReturnAndReplacementViewModel;

import java.util.List;


public class ReturnsFragment extends Fragment {

    List<Claim> mReturnList;
    FragmentClaimsBinding fragmentClaimsBinding;
    boolean isViewShown = false;

    public ReturnsFragment() {
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        isViewShown = isVisibleToUser;

        if (isViewShown) {
            getClaimList();
        }
    }

    // TODO: Rename and change types and number of parameters
    public static ReturnsFragment newInstance() {
        ReturnsFragment fragment = new ReturnsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fragmentClaimsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_claims, container, false);

        if (isViewShown) {
            getClaimList();
        }
        return fragmentClaimsBinding.getRoot();
    }

    private void getClaimList() {

        ReturnAndReplacementViewModel returnAndReplacementViewModel = ViewModelProviders.of(this).get(ReturnAndReplacementViewModel.class);

        returnAndReplacementViewModel.getClaimsList().observe(this, claimListingDataContainer -> {

            if (claimListingDataContainer != null) {

                if (claimListingDataContainer.getCode().equals(AppConstants.SUCCESS_CODE)) {
                    mReturnList = claimListingDataContainer.getData().getClaims();
                    setAdapter();
                }
            }
        });
    }


    private void setAdapter() {

        fragmentClaimsBinding.rvAllClaims.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        fragmentClaimsBinding.rvAllClaims.setHasFixedSize(true);
    }
}
