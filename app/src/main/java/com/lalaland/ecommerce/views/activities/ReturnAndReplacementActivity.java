package com.lalaland.ecommerce.views.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.adapters.RRProductImageAdapter;
import com.lalaland.ecommerce.data.models.order.details.OrderProduct;
import com.lalaland.ecommerce.databinding.ActivityReturnAndReplacementBinding;
import com.lalaland.ecommerce.helpers.AppUtils;
import com.lalaland.ecommerce.viewModels.returnAndReplacement.ReturnAndReplacementViewModel;

import java.util.ArrayList;
import java.util.List;

import static com.lalaland.ecommerce.helpers.AppConstants.CLAIM_TYPE;
import static com.lalaland.ecommerce.helpers.AppConstants.PRODUCT_STORAGE_BASE_URL;
import static com.lalaland.ecommerce.helpers.AppConstants.SUCCESS_CODE;

public class ReturnAndReplacementActivity extends AppCompatActivity implements RRProductImageAdapter.RRImageListener {


    private ReturnAndReplacementViewModel returnAndReplacementViewModel;
    private ActivityReturnAndReplacementBinding activityReturnAndReplacementBinding;
    private String orderProductId = "";
    private List<String> returnReasons = new ArrayList<>();
    private List<String> replacementReasons = new ArrayList<>();
    private List<String> uris = new ArrayList<>();
    private ArrayAdapter<String> adapter, repAdapter;
    RRProductImageAdapter rrProductImageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityReturnAndReplacementBinding = DataBindingUtil.setContentView(this, R.layout.activity_return_and_replacement);

        orderProductId = getIntent().getStringExtra("order_product_id");
        returnAndReplacementViewModel = ViewModelProviders.of(this).get(ReturnAndReplacementViewModel.class);

        getRRData();

        activityReturnAndReplacementBinding.rgClaim.setOnCheckedChangeListener((group, checkedId) -> {

            if (checkedId == R.id.rb_return) {
                CLAIM_TYPE = 2;
            } else {
                CLAIM_TYPE = 3;
            }

            initRRSpinner();

        });
    }

    private void getRRData() {

        returnAndReplacementViewModel.createNewClaim(orderProductId).observe(this, returnAndReplacementDataContainer -> {

            if (returnAndReplacementDataContainer != null) {

                if (returnAndReplacementDataContainer.getCode().equals(SUCCESS_CODE)) {

                    showProductDetail(returnAndReplacementDataContainer.getData().getOrderProduct());
                    setRRImageAdapter();

                    returnReasons = returnAndReplacementDataContainer.getData().getReturnReasons();
                    for (int i = 0; i < returnAndReplacementDataContainer.getData().getReplacementReasons().size(); i++) {
                        replacementReasons.add(returnAndReplacementDataContainer.getData().getReplacementReasons().get(i).getText());
                    }
                    initRRSpinner();

                } else {
                    Toast.makeText(this, returnAndReplacementDataContainer.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showProductDetail(OrderProduct orderProduct) {

        String imgUrl = PRODUCT_STORAGE_BASE_URL.concat(orderProduct.getPrimaryImage());
        Glide
                .with(this)
                .load(imgUrl)
                .placeholder(R.drawable.placeholder_products)
                .into(activityReturnAndReplacementBinding.ivProduct);

        activityReturnAndReplacementBinding.tvProductName.setText(orderProduct.getName());
        activityReturnAndReplacementBinding.tvPrice.setText(AppUtils.formatPriceString(orderProduct.getSalePrice()));

    }

    private void setRRImageAdapter() {

        rrProductImageAdapter = new RRProductImageAdapter(this, this);
        rrProductImageAdapter.setData(uris);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        activityReturnAndReplacementBinding.rvProductImages.setLayoutManager(linearLayoutManager);
        activityReturnAndReplacementBinding.rvProductImages.setAdapter(rrProductImageAdapter);

    }

    private void initRRSpinner() {

        activityReturnAndReplacementBinding.spReturnReasons.setPrompt("Return");

        if (CLAIM_TYPE == 2)
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, returnReasons);
        else
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, replacementReasons);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activityReturnAndReplacementBinding.spReturnReasons.setAdapter(adapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                if (data.getClipData() != null) {
                    int count = data.getClipData().getItemCount(); //evaluate the count before the for loop --- otherwise, the count is evaluated every loop.
                    Uri imageUri;

                    for (int i = 0; i < count; i++) {
                        imageUri = data.getClipData().getItemAt(i).getUri();

                        if (uris.size() <= 5)
                            uris.add(imageUri.toString());
                    }

                    rrProductImageAdapter.setData(uris);

                    //do something with the image (save it to some directory or whatever you need to do with it here)
                } else if (data.getData() != null) {
                    Uri imagePath = data.getData();

                    if (uris.size() <= 5)
                        uris.add(imagePath.toString());

                    rrProductImageAdapter.setData(uris);
                    //do something with the image (save it to some directory or whatever you need to do with it here)
                }
            }
        }
    }

    @Override
    public void onRRImageDeleteClicked(int position) {
        uris.remove(position);
        rrProductImageAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRRImageUploadClicked(int position) {

        if (uris.size() >= 6) {
            Toast.makeText(this, "You cannot upload more than 5 images", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Product Picture"), 1);
    }
}
