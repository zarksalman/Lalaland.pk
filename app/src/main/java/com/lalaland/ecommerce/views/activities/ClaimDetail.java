package com.lalaland.ecommerce.views.activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.adapters.RRProductImageAdapter;
import com.lalaland.ecommerce.customBinding.CustomBinding;
import com.lalaland.ecommerce.data.models.returnAndReplacement.claimListingDetail.ClaimDetails;
import com.lalaland.ecommerce.data.models.returnAndReplacement.claimListingDetail.ClaimImage;
import com.lalaland.ecommerce.databinding.ActivityClaimDetailBinding;
import com.lalaland.ecommerce.helpers.AppUtils;
import com.lalaland.ecommerce.viewModels.returnAndReplacement.ReturnAndReplacementViewModel;

import java.util.ArrayList;
import java.util.List;

import static com.lalaland.ecommerce.helpers.AppConstants.CLAIM_STORAGE_BASE_URL;
import static com.lalaland.ecommerce.helpers.AppConstants.SUCCESS_CODE;

public class ClaimDetail extends AppCompatActivity {

    private ActivityClaimDetailBinding activityClaimDetailBinding;
    private ReturnAndReplacementViewModel returnAndReplacementViewModel;
    private String claimId;
    private ClaimDetails claimDetails;
    private List<ClaimImage> claimImage;
    String pVariation;
    String[] variation;
    String[] variationTitle;
    String[] variationValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityClaimDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_claim_detail);

        claimId = getIntent().getStringExtra("claim_id");
        getClaimDetails();

        activityClaimDetailBinding.btnBack.setOnClickListener(v -> onBackPressed());
    }

    private void getClaimDetails() {

        returnAndReplacementViewModel = ViewModelProviders.of(this).get(ReturnAndReplacementViewModel.class);

        returnAndReplacementViewModel.getClaimDetails(claimId).observe(this, claimDataContainer -> {

            if (claimDataContainer != null) {
                if (claimDataContainer.getCode().equals(SUCCESS_CODE)) {

                    claimDetails = claimDataContainer.getData().getClaimDetails();
                    claimImage = claimDataContainer.getData().getClaimImages();
                    setDetails();
                    setClaimImages();
                }
            }
        });
    }

    private void setDetails() {

        CustomBinding.setImageFromServer(activityClaimDetailBinding.ivProduct, claimDetails.getPrimaryImage());

        String title = "Claim " + AppUtils.getClaimType(claimDetails.getApprovalStatus());
        activityClaimDetailBinding.tvTitle.setText(title);

        activityClaimDetailBinding.tvProductName.setText(claimDetails.getProductName());


        pVariation = claimDetails.getProductVariationDescription();

        variation = pVariation.split("[|]", 6);
        variationTitle = variation[0].split(",");

        if (variation.length > 0)
            variationValue = variation[variation.length - 1].split(",");

        if (variationTitle[0].toLowerCase().contains("size")) {
            activityClaimDetailBinding.tvProductSize.setText(variationValue[0]);
        } else {
            activityClaimDetailBinding.tvProductSize.setText(variationValue[1]);
        }

        activityClaimDetailBinding.tvProductQty.setText(String.valueOf(claimDetails.getItemQuantity()));
        activityClaimDetailBinding.tvPrice.setText(AppUtils.formatPriceString(claimDetails.getSalePrice()));

        activityClaimDetailBinding.tvClaimType.setText(claimDetails.getDisplayName());
        activityClaimDetailBinding.tvReason.setText(claimDetails.getCustomerReason());

        if (claimDetails.getApprovalStatus() != null) {
            if (claimDetails.getApprovalStatus() == 0) {
                if (claimDetails.getRejectReason() != null) {
                    activityClaimDetailBinding.tvRejectionReason.setText(claimDetails.getRejectReason().toString());
                    activityClaimDetailBinding.rejectionReasonContainer.setVisibility(View.VISIBLE);
                }
            }
        }

        activityClaimDetailBinding.tvDetail.setText(claimDetails.getCustomerReasonDetail());


    }

    private void setClaimImages() {

        RRProductImageAdapter rrProductImageAdapter = new RRProductImageAdapter(this, true, new RRProductImageAdapter.RRImageListener() {
            @Override
            public void onRRImageDeleteClicked(int position) {

            }

            @Override
            public void onRRImageUploadClicked(int position) {

            }
        });

        List<String> urls = new ArrayList<>();
        String completeUrl;

        for (ClaimImage claimImage : claimImage) {

            completeUrl = CLAIM_STORAGE_BASE_URL + claimImage.getClaimImage();
            urls.add(completeUrl);
        }

        activityClaimDetailBinding.rvClaimImages.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        rrProductImageAdapter.setData(urls);
        activityClaimDetailBinding.rvClaimImages.setAdapter(rrProductImageAdapter);

        activityClaimDetailBinding.pbLoading.setVisibility(View.GONE);
        activityClaimDetailBinding.productDetail.setVisibility(View.VISIBLE);
        activityClaimDetailBinding.claimDetail.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
