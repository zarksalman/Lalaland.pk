package com.lalaland.ecommerce.views.activities;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.data.models.order.details.OrderProduct;
import com.lalaland.ecommerce.databinding.ActivityReturnAndReplacementBinding;
import com.lalaland.ecommerce.helpers.AppUtils;
import com.lalaland.ecommerce.viewModels.returnAndReplacement.ReturnAndReplacementViewModel;

import java.util.ArrayList;
import java.util.List;

import static com.lalaland.ecommerce.helpers.AppConstants.CLAIM_TYPE;
import static com.lalaland.ecommerce.helpers.AppConstants.PRODUCT_STORAGE_BASE_URL;
import static com.lalaland.ecommerce.helpers.AppConstants.SUCCESS_CODE;

public class ReturnAndReplacementActivity extends AppCompatActivity {


    private ReturnAndReplacementViewModel returnAndReplacementViewModel;
    private ActivityReturnAndReplacementBinding activityReturnAndReplacementBinding;
    private String orderProductId = "";
    private List<String> returnReasons = new ArrayList<>();
    private List<String> replacementReasons = new ArrayList<>();
    private ArrayAdapter<String> adapter, repAdapter;

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
                .into(activityReturnAndReplacementBinding.ivProduct);

        activityReturnAndReplacementBinding.tvProductName.setText(orderProduct.getName());
        activityReturnAndReplacementBinding.tvPrice.setText(AppUtils.formatPriceString(orderProduct.getSalePrice()));
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

}
