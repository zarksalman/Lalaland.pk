package com.lalaland.ecommerce.views.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.adapters.RRProductImageAdapter;
import com.lalaland.ecommerce.data.models.order.details.OrderProduct;
import com.lalaland.ecommerce.data.models.returnAndReplacement.LinkProduct;
import com.lalaland.ecommerce.data.models.returnAndReplacement.ProductAttribute;
import com.lalaland.ecommerce.data.models.returnAndReplacement.ReplacementReason;
import com.lalaland.ecommerce.data.models.returnAndReplacement.ReturnAndReplacementData;
import com.lalaland.ecommerce.data.models.returnAndReplacement.ReturnReason;
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
    private List<String> colorList = new ArrayList<>();
    private List<String> sizeList = new ArrayList<>();
    private List<String> uris = new ArrayList<>();
    private List<ReplacementReason> replacementReasonList = new ArrayList<>();
    private List<ReturnReason> returnReasonsList = new ArrayList<>();
    private List<LinkProduct> linkProducts = new ArrayList<>();
    private List<ProductAttribute> productAttributes = new ArrayList<>();
    private ArrayAdapter<String> adapter, colorAdapter, sizeAdapter;
    private RRProductImageAdapter rrProductImageAdapter;
    private String pVariation;
    private String[] variation;
    private String[] variationTitle;
    private String[] variationValue;
    private boolean isBothVisible = false;
    private boolean isSameColorSelected = false;
    private String sizeDisable = "-1";
    private ReturnAndReplacementData returnAndReplacementData;

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

        activityReturnAndReplacementBinding.btnBack.setOnClickListener(v -> onBackPressed());
    }

    private void getRRData() {

        returnAndReplacementViewModel.createNewClaim(orderProductId).observe(this, returnAndReplacementDataContainer -> {

            if (returnAndReplacementDataContainer != null) {

                if (returnAndReplacementDataContainer.getCode().equals(SUCCESS_CODE)) {

                    returnAndReplacementData = returnAndReplacementDataContainer.getData();

                    showProductDetail(returnAndReplacementData.getOrderProduct());
                    setRRImageAdapter();

                    // return
                    returnReasonsList = returnAndReplacementData.getReturnReasons();
                    ReturnReason returnReason = new ReturnReason();
                    returnReason.setClaimType("0");
                    returnReason.setShowColor(false);
                    returnReason.setShowSize(false);
                    returnReason.setText("Select Reason");
                    returnReasonsList.add(0, returnReason);

                    for (int i = 0; i < returnReasonsList.size(); i++) {
                        returnReasons.add(returnReasonsList.get(i).getText());
                    }

                    // replacement
                    replacementReasonList = returnAndReplacementData.getReplacementReasons();
                    ReplacementReason replacementReason = new ReplacementReason();
                    replacementReason.setShowColor(false);
                    replacementReason.setShowSize(false);
                    replacementReason.setText("Select Reason");
                    replacementReasonList.add(0, replacementReason);

                    for (int i = 0; i < replacementReasonList.size(); i++) {
                        replacementReasons.add(replacementReasonList.get(i).getText());
                    }

                    initRRSpinner();

                    // color
                    linkProducts = returnAndReplacementData.getLinkProducts();
                    for (int i = 0; i < linkProducts.size(); i++) {
                        colorList.add(linkProducts.get(i).getColorName());
                    }
                    colorList.add(0, "Color");
                    initColorSpinner();

                    // size
                    productAttributes = returnAndReplacementData.getProductAttributes();
                    for (int i = 0; i < productAttributes.size(); i++) {
                        sizeList.add(productAttributes.get(i).getSizeName());
                    }

                    sizeList.add(0, "Size");
                    initSizeSpinner();

                    activityReturnAndReplacementBinding.svClaim.setVisibility(View.VISIBLE);
                    activityReturnAndReplacementBinding.pbLoading.setVisibility(View.GONE);

                    activityReturnAndReplacementBinding.btnSubmitClaim.setOnClickListener(v -> {
                        Toast.makeText(this, "submit claim", Toast.LENGTH_SHORT).show();
                    });
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
        activityReturnAndReplacementBinding.tvProductQty.setText(AppUtils.toString(orderProduct.getItemQuantity()));

        pVariation = orderProduct.getProductVariationDescription();
        variation = pVariation.split("[|]", 6);

        variationTitle = variation[0].split(",");

        if (variation.length > 0)
            variationValue = variation[variation.length - 1].split(",");

        if (variationTitle[0].toLowerCase().contains("size")) {
            activityReturnAndReplacementBinding.tvProductSize.setText(variationValue[0]);
        } else {
            activityReturnAndReplacementBinding.tvProductSize.setText(variationValue[1]);
        }

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

        if (CLAIM_TYPE == 2) {
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, returnReasons);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            activityReturnAndReplacementBinding.spReturnReasons.setAdapter(adapter);

        } else {

            adapter = new ArrayAdapter<String>(this,
                    R.layout.spiner_item, replacementReasons) {
                // Disable click item < month current
                @Override
                public boolean isEnabled(int position) {
                    // TODO Auto-generated method stub
                    if (replacementReasons.get(position).toLowerCase().contains("color") && linkProducts.size() <= 1) {
                        return false;
                    } else if (replacementReasons.get(position).toLowerCase().contains("size") && productAttributes.size() <= 1) {
                        return false;
                    } else {
                        return true;
                    }
                }

                // Change color item
                @Override
                public View getDropDownView(int position, View convertView,
                                            ViewGroup parent) {
                    // TODO Auto-generated method stub
                    View mView = super.getDropDownView(position, convertView, parent);
                    TextView mTextView = (TextView) mView;

                    if (replacementReasons.get(position).toLowerCase().contains("color") && linkProducts.size() <= 1) {
                        mTextView.setTextColor(Color.GRAY);

                    } else if (replacementReasons.get(position).toLowerCase().contains("color") && linkProducts.size() > 1) {
                        mTextView.setTextColor(Color.BLACK);
                    }

                    if (replacementReasons.get(position).toLowerCase().contains("size") && productAttributes.size() <= 1)
                        mTextView.setTextColor(Color.GRAY);
                    else if (replacementReasons.get(position).toLowerCase().contains("size") && productAttributes.size() > 1)
                        mTextView.setTextColor(Color.BLACK);

                    if (replacementReasons.get(position).toLowerCase().contains("color & size")) {
                        if (linkProducts.size() <= 1 || productAttributes.size() <= 1)
                            mTextView.setTextColor(Color.GRAY);
                    }
                    return mView;
                }
            };


            //adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, replacementReasons);

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            activityReturnAndReplacementBinding.spReturnReasons.setAdapter(adapter);

            activityReturnAndReplacementBinding.spReturnReasons.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    initColorSpinner();
                    initSizeSpinner();

                    if (position != 0) {

                        if (replacementReasonList.get(position).getShowColor() && replacementReasonList.get(position).getShowSize()) {

                            activityReturnAndReplacementBinding.spColor.setVisibility(View.VISIBLE);
                            activityReturnAndReplacementBinding.spSize.setVisibility(View.VISIBLE);

                            activityReturnAndReplacementBinding.spSize.setEnabled(false);


                            isBothVisible = true;

                        } else if (replacementReasonList.get(position).getShowColor() && !replacementReasonList.get(position).getShowSize()) {

                            activityReturnAndReplacementBinding.spColor.setVisibility(View.VISIBLE);
                            activityReturnAndReplacementBinding.spSize.setVisibility(View.GONE);

                            isBothVisible = false;
                        } else if (!replacementReasonList.get(position).getShowColor() && replacementReasonList.get(position).getShowSize()) {

                            activityReturnAndReplacementBinding.spSize.setVisibility(View.VISIBLE);
                            activityReturnAndReplacementBinding.spColor.setVisibility(View.GONE);
                            activityReturnAndReplacementBinding.spSize.setEnabled(true);


                            isBothVisible = false;

                        } else {
                            activityReturnAndReplacementBinding.spColor.setVisibility(View.GONE);
                            activityReturnAndReplacementBinding.spSize.setVisibility(View.GONE);
                            isBothVisible = false;

                        }

                    } else {
                        activityReturnAndReplacementBinding.spColor.setVisibility(View.GONE);
                        activityReturnAndReplacementBinding.spSize.setVisibility(View.GONE);
                        isBothVisible = false;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    activityReturnAndReplacementBinding.spColor.setVisibility(View.GONE);
                    activityReturnAndReplacementBinding.spSize.setVisibility(View.GONE);
                    isBothVisible = false;
                }
            });
        }

    }

    private void initColorSpinner() {

        activityReturnAndReplacementBinding.spColor.setPrompt("Colors");

        colorAdapter = new ArrayAdapter<String>(this, R.layout.spiner_item, colorList) {
            @Override
            public boolean isEnabled(int position) {

                if (position != 0) {

                    if (isBothVisible)
                        return true;
                    else {

                        if (linkProducts.get(position - 1).getColorKey().equals(returnAndReplacementData.getOrderedAttributeValuePrimaryKey()))
                            return false;
                        else
                            return true;
                    }
                } else
                    return true;

            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

                View mView = super.getDropDownView(position, convertView, parent);
                TextView mTextView = (TextView) mView;

                if (position != 0) {
                    if (isBothVisible)
                        mTextView.setTextColor(Color.BLACK);
                    else {

                        // because we have added color object
                        if (linkProducts.get(position - 1).getColorKey().equals(returnAndReplacementData.getOrderedAttributeValuePrimaryKey()))
                            mTextView.setTextColor(Color.GRAY);
                        else
                            mTextView.setTextColor(Color.BLACK);
                    }

                } else
                    mTextView.setTextColor(Color.BLACK);


                return mView;

            }
        };

        colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activityReturnAndReplacementBinding.spColor.setAdapter(colorAdapter);

        activityReturnAndReplacementBinding.spColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                isSameColorSelected = false;

                if (position > 0)
                    activityReturnAndReplacementBinding.spSize.setEnabled(true);
                else
                    activityReturnAndReplacementBinding.spSize.setEnabled(false);

                if (isBothVisible) {

                    String colorName = colorList.get(position);
                    Integer selectedColorId = -1;

                    for (int i = 0; i < linkProducts.size(); i++) {
                        if (linkProducts.get(i).getColorName().equals(colorName))
                            selectedColorId = linkProducts.get(i).getColorKey();
                    }

                    if (selectedColorId.equals(returnAndReplacementData.getOrderedAttributeValuePrimaryKey()))
                        isSameColorSelected = true;
                }

                initSizeSpinner();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initSizeSpinner() {

        activityReturnAndReplacementBinding.spSize.setPrompt("Sizes");

        sizeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sizeList) {
            @Override
            public boolean isEnabled(int position) {

                if (position != 0) {
                    if (isBothVisible) {

                        if (isSameColorSelected) {
                            return !productAttributes.get(position - 1).getSizeId().equals(returnAndReplacementData.getOrderedAttributeValueId());
                        }

                        return true;
                    } else {

                        if (productAttributes.get(position - 1).getSizeId().equals(returnAndReplacementData.getOrderedAttributeValueId()))
                            return false;
                    }
                } else
                    return true;


                return true;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View mView = super.getDropDownView(position, convertView, parent);
                TextView mTextView = (TextView) mView;

                if (position != 0) {

                    if (isBothVisible) {

                        if (isSameColorSelected) {
                            if (productAttributes.get(position - 1).getSizeId().equals(returnAndReplacementData.getOrderedAttributeValueId()))
                                mTextView.setTextColor(Color.GRAY);
                            else
                                mTextView.setTextColor(Color.BLACK);
                        } else
                            mTextView.setTextColor(Color.BLACK);


                    } else {
                        if (productAttributes.get(position - 1).getSizeId().equals(returnAndReplacementData.getOrderedAttributeValueId())) {
                            mTextView.setTextColor(Color.GRAY);
                        } else {
                            mTextView.setTextColor(Color.BLACK);
                        }
                    }
                }
                return mView;
            }
        };

        sizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activityReturnAndReplacementBinding.spSize.setAdapter(sizeAdapter);

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
