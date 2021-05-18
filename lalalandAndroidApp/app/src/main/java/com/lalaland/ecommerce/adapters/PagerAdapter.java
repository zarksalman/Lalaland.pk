package com.lalaland.ecommerce.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.lalaland.ecommerce.data.models.returnAndReplacement.claimListing.Claim;
import com.lalaland.ecommerce.views.fragments.returnAndReplacement.AllClaimsFragment;
import com.lalaland.ecommerce.views.fragments.returnAndReplacement.ReplacementFragment;
import com.lalaland.ecommerce.views.fragments.returnAndReplacement.ReturnsFragment;

import java.util.ArrayList;
import java.util.List;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    List<Claim> mClaimList;
    List<Claim> claimsReturn;
    List<Claim> claimsReplacement;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                AllClaimsFragment tab1 = new AllClaimsFragment();
                return tab1;
            case 1:
                ReturnsFragment tab2 = new ReturnsFragment();
                return tab2;
            case 2:
                ReplacementFragment tab3 = new ReplacementFragment();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

}