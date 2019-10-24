package com.lalaland.ecommerce.views.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;

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

        if (claimDetails.getApprovalStatus() != null && claimDetails.getApprovalStatus() == 1) {
            activityClaimDetailBinding.tvWaybill.setVisibility(View.VISIBLE);
            activityClaimDetailBinding.ivDownloadWaybill.setVisibility(View.VISIBLE);

            activityClaimDetailBinding.ivDownloadWaybill.setOnClickListener(v -> {
                askForPermission();
            });

            activityClaimDetailBinding.tvWaybill.setOnClickListener(v -> {
                askForPermission();
            });

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

    private void saveToDisk(ResponseBody body, String filename) {
        try {

            File destinationFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), filename);

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(destinationFile);
                byte data[] = new byte[4096];
                int count;
                int progress = 0;
                long fileSize = body.contentLength();
                Log.d("save_file", "File Size=" + fileSize);
                while ((count = inputStream.read(data)) != -1) {
                    outputStream.write(data, 0, count);
                }

                outputStream.flush();

                Toast.makeText(this, "Waybill Downloaded", Toast.LENGTH_SHORT).show();

                return;
            } catch (IOException e) {

                e.printStackTrace();

                Toast.makeText(this, "Failed to download (insufficient memory)", Toast.LENGTH_SHORT).show();
                return;
            } finally {

                if (inputStream != null) inputStream.close();
                if (outputStream != null) outputStream.close();

            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to download (insufficient memory)", Toast.LENGTH_SHORT).show();
            return;
        } finally {

            AppUtils.blockUi(this);
            activityClaimDetailBinding.pbLoading.setVisibility(View.GONE);
        }
    }

    private void askForPermission() {

        try {
            if (ActivityCompat.checkSelfPermission(ClaimDetail.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ClaimDetail.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
            } else {

                returnAndReplacementViewModel.getWayBill(claimDetails.getShipmentTrackingUrl()).observe(this, responseBody -> {

                    if (responseBody != null) {

                        activityClaimDetailBinding.pbLoading.setVisibility(View.VISIBLE);
                        AppUtils.blockUi(this);
                        saveToDisk(responseBody, "waybill_" + claimDetails.getFancyReturnId() + ".pdf");
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {

            case 100:

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();

                    returnAndReplacementViewModel.getWayBill(claimDetails.getShipmentTrackingUrl()).observe(this, responseBody -> {

                        if (responseBody != null) {

                            activityClaimDetailBinding.pbLoading.setVisibility(View.VISIBLE);
                            AppUtils.blockUi(this);
                            saveToDisk(responseBody, "waybill_" + claimDetails.getFancyReturnId() + ".pdf");
                        }
                    });

                } else {

                    showAltersDialogue("You need to give access to store waybill");
                    //do something like displaying a message that he didn`t allow the app to access gallery and you wont be able to let him select from gallery
                }
                break;
        }
    }

    private void showAltersDialogue(String msg) {

        new AlertDialog.Builder(this)
                .setTitle("Note")
                .setMessage(msg)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName())));
                })
                .setNegativeButton(android.R.string.cancel, (dialog, which) -> {
                    dialog.dismiss();
                })
                .show();
    }
}
