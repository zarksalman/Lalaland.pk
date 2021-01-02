package com.lalaland.ecommerce.views.activities;

import android.Manifest;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.adapters.RRProductImageAdapter;
import com.lalaland.ecommerce.data.models.order.details.OrderProduct;
import com.lalaland.ecommerce.data.models.returnAndReplacement.createClaimDetail.LinkProduct;
import com.lalaland.ecommerce.data.models.returnAndReplacement.createClaimDetail.ProductAttribute;
import com.lalaland.ecommerce.data.models.returnAndReplacement.createClaimDetail.ReplacementReason;
import com.lalaland.ecommerce.data.models.returnAndReplacement.createClaimDetail.ReturnAndReplacementData;
import com.lalaland.ecommerce.data.models.returnAndReplacement.createClaimDetail.ReturnReason;
import com.lalaland.ecommerce.databinding.ActivityReturnAndReplacementBinding;
import com.lalaland.ecommerce.databinding.ClaimDialogueBinding;
import com.lalaland.ecommerce.helpers.AppConstants;
import com.lalaland.ecommerce.helpers.AppUtils;
import com.lalaland.ecommerce.viewModels.returnAndReplacement.ReturnAndReplacementViewModel;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static com.lalaland.ecommerce.helpers.AppConstants.CLAIM_TYPE;
import static com.lalaland.ecommerce.helpers.AppConstants.PRODUCT_STORAGE_BASE_URL;
import static com.lalaland.ecommerce.helpers.AppConstants.SUCCESS_CODE;
import static com.lalaland.ecommerce.helpers.AppConstants.VALIDATION_FAIL_CODE;
import static com.lalaland.ecommerce.helpers.AppUtils.getDataColumn;
import static com.lalaland.ecommerce.helpers.AppUtils.isDownloadsDocument;
import static com.lalaland.ecommerce.helpers.AppUtils.isExternalStorageDocument;
import static com.lalaland.ecommerce.helpers.AppUtils.isGooglePhotosUri;
import static com.lalaland.ecommerce.helpers.AppUtils.isMediaDocument;

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
    private String productId = "";
    private ReturnAndReplacementData returnAndReplacementData;
    private ClaimDialogueBinding claimDialogueBinding;
    private AlertDialog claimSuccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityReturnAndReplacementBinding = DataBindingUtil.setContentView(this, R.layout.activity_return_and_replacement);

        orderProductId = getIntent().getStringExtra("order_product_id");
        returnAndReplacementViewModel = new ViewModelProvider(this).get(ReturnAndReplacementViewModel.class);

        CLAIM_TYPE = 2;
        getRRData();

        activityReturnAndReplacementBinding.rgClaim.setOnCheckedChangeListener((group, checkedId) -> {

            activityReturnAndReplacementBinding.spColorContainer.setVisibility(View.GONE);
            activityReturnAndReplacementBinding.spSizeContainer.setVisibility(View.GONE);

            if (checkedId == R.id.rb_return) {
                CLAIM_TYPE = 2;
            } else {
                CLAIM_TYPE = 3;
            }

            initRRSpinner();

        });

        activityReturnAndReplacementBinding.btnBack.setOnClickListener(v -> onBackPressed());

        prepareClaimSuccess();
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
                        submitClaim();
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

        rrProductImageAdapter = new RRProductImageAdapter(this, false, this);
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

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            activityReturnAndReplacementBinding.spReturnReasons.setAdapter(adapter);

            activityReturnAndReplacementBinding.spReturnReasons.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    if (CLAIM_TYPE == 2)
                        return;

                    initColorSpinner();
                    initSizeSpinner();

                    if (position != 0) {

                        if (replacementReasonList.get(position).getShowColor() && replacementReasonList.get(position).getShowSize()) {

                            activityReturnAndReplacementBinding.spColorContainer.setVisibility(View.VISIBLE);
                            activityReturnAndReplacementBinding.spSizeContainer.setVisibility(View.VISIBLE);

                            activityReturnAndReplacementBinding.spSize.setEnabled(false);


                            isBothVisible = true;

                        } else if (replacementReasonList.get(position).getShowColor() && !replacementReasonList.get(position).getShowSize()) {

                            activityReturnAndReplacementBinding.spColorContainer.setVisibility(View.VISIBLE);
                            activityReturnAndReplacementBinding.spSizeContainer.setVisibility(View.GONE);

                            isBothVisible = false;
                        } else if (!replacementReasonList.get(position).getShowColor() && replacementReasonList.get(position).getShowSize()) {

                            activityReturnAndReplacementBinding.spSizeContainer.setVisibility(View.VISIBLE);
                            activityReturnAndReplacementBinding.spColorContainer.setVisibility(View.GONE);
                            activityReturnAndReplacementBinding.spSize.setEnabled(true);


                            isBothVisible = false;

                        } else {
                            activityReturnAndReplacementBinding.spColorContainer.setVisibility(View.GONE);
                            activityReturnAndReplacementBinding.spSizeContainer.setVisibility(View.GONE);
                            isBothVisible = false;
                        }

                    } else {
                        activityReturnAndReplacementBinding.spColorContainer.setVisibility(View.GONE);
                        activityReturnAndReplacementBinding.spSizeContainer.setVisibility(View.GONE);
                        isBothVisible = false;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    activityReturnAndReplacementBinding.spColorContainer.setVisibility(View.GONE);
                    activityReturnAndReplacementBinding.spSizeContainer.setVisibility(View.GONE);
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

        sizeAdapter = new ArrayAdapter<String>(this, R.layout.spiner_item, sizeList) {
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

    public void selectImage() {

        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
            } else {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Product Picture"), 200);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void submitClaim() {

        Map<String, String> parameters = new HashMap<>();
        int position;
        String claimType, reason, moreDetail, productId, attributeId;
        position = activityReturnAndReplacementBinding.spReturnReasons.getSelectedItemPosition();
        moreDetail = activityReturnAndReplacementBinding.etMoreDetail.getText().toString().trim();

        if (activityReturnAndReplacementBinding.spReturnReasons.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Select reason", Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedColorPosition = activityReturnAndReplacementBinding.spColor.getSelectedItemPosition();
        int selectedSizePosition = activityReturnAndReplacementBinding.spSize.getSelectedItemPosition();

        if (CLAIM_TYPE == 3) {

            // color selection parameter
            if (replacementReasonList.get(position).getShowColor() && !replacementReasonList.get(position).getShowSize()) {
                if (selectedColorPosition == 0) {
                    Toast.makeText(this, "Select color", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            // size selection parameter
            if (replacementReasonList.get(position).getShowSize() && !replacementReasonList.get(position).getShowColor()) {

                if (selectedSizePosition == 0) {
                    Toast.makeText(this, "Select size", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            if (replacementReasonList.get(position).getShowSize() && replacementReasonList.get(position).getShowColor()) {
                if (selectedColorPosition == 0 || selectedSizePosition == 0) {
                    Toast.makeText(this, "Select color and size", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            claimType = replacementReasonList.get(position).getClaimType().trim();
            reason = replacementReasonList.get(position).getText().trim();
        } else {
            claimType = returnReasonsList.get(position).getClaimType();
            reason = returnReasonsList.get(position).getText();
        }

        if (moreDetail.isEmpty()) {
            Toast.makeText(this, "Please enter the detail", Toast.LENGTH_SHORT).show();
            return;
        }

        if (uris.size() < 3) {
            Toast.makeText(this, "Please upload at least 2 images", Toast.LENGTH_SHORT).show();
            return;
        }

        parameters.put("reason", reason);

        parameters.put("more_detail", moreDetail);
        parameters.put("order_product_id", orderProductId);
        parameters.put("claim_type", claimType);


        if (selectedColorPosition == 0) {
            productId = String.valueOf(returnAndReplacementData.getOrderProduct().getId());

        } else {
            productId = String.valueOf(linkProducts.get(selectedColorPosition - 1).getProductId());
        }
        parameters.put("product_id", productId);

        AppUtils.blockUi(this);
        activityReturnAndReplacementBinding.pbLoading.setVisibility(View.VISIBLE);

        if (selectedSizePosition == 0) {
            attributeId = String.valueOf(returnAndReplacementData.getOrderedAttributeValueId());
        } else {
            attributeId = String.valueOf(productAttributes.get(selectedSizePosition - 1).getSizeId());
        }
        parameters.put("variation_attribute_value_id", attributeId);

        List<MultipartBody.Part> claimImages = new ArrayList<>();
        List<String> selectedImagesUris = new ArrayList<>();

        selectedImagesUris.addAll(uris);
        selectedImagesUris.remove(0);

        if (selectedImagesUris != null && selectedImagesUris.size() > 0) {
            for (int i = 0; i < selectedImagesUris.size(); i++) {
                String filePath = getRealPathFromUri(Uri.parse(selectedImagesUris.get(i)));

                File imageFile = new File(filePath);

                imageFile = AppUtils.saveBitmapToFile(imageFile);

                if (filePath != null && filePath.length() > 0) {
                    if (imageFile.exists()) {

                        RequestBody filePart = RequestBody.create(
                                MediaType.parse(getContentResolver().getType(Uri.parse(selectedImagesUris.get(i)))),
                                imageFile);


                        String filename = "images[" + i + "]"; //key for upload file like : imagePath0
                        MultipartBody.Part file = MultipartBody.Part.createFormData(filename, imageFile.getName(), filePart);

                        claimImages.add(i, file);
                    }
                }
            }
        }

        returnAndReplacementViewModel.newClaimPost(claimImages, parameters).observe(this, basicResponse -> {

            AppUtils.unBlockUi(this);
            activityReturnAndReplacementBinding.pbLoading.setVisibility(View.GONE);

            if (basicResponse != null) {

                if (basicResponse.getCode().equals(SUCCESS_CODE)) {

                    claimDialogueBinding.tvMessage.setText(basicResponse.getMsg());

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        claimDialogueBinding.tvMessage.setText(Html.fromHtml(basicResponse.getMsg(), Html.FROM_HTML_MODE_LEGACY));
                    } else {
                        claimDialogueBinding.tvMessage.setText(Html.fromHtml(basicResponse.getMsg()));
                    }

                    claimDialogueBinding.ivTick.setVisibility(View.VISIBLE);
                    claimDialogueBinding.btnApply.setBackgroundColor(getResources().getColor(R.color.colorGreen));
                    claimDialogueBinding.btnApply.setText("Continue");
                    claimDialogueBinding.btnApply.setTextColor(getResources().getColor(android.R.color.white));

                    claimSuccess.show();

                    claimDialogueBinding.btnApply.setOnClickListener(v -> {

                        claimSuccess.dismiss();

                        AppConstants.LOAD_HOME_FRAGMENT_INDEX = 4;
                        Intent intent = new Intent(this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();

                    });

                } else if (basicResponse.getCode().equals(VALIDATION_FAIL_CODE)) {

                    claimDialogueBinding.tvMessage.setText(basicResponse.getMsg());
                    claimDialogueBinding.ivTick.setVisibility(View.GONE);
                    claimDialogueBinding.btnApply.setBackgroundColor(getResources().getColor(R.color.colorMediumGray));
                    claimDialogueBinding.btnApply.setText("OK");
                    claimDialogueBinding.btnApply.setTextColor(getResources().getColor(R.color.colorDarkGray));
                    claimSuccess.show();

                    claimDialogueBinding.btnApply.setOnClickListener(v -> claimSuccess.dismiss());
                }
            }

        });
    }

    public String getRealPathFromUri(final Uri uri) { // function for file path from uri,
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(this, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(this, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(this, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(this, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {

            case 100:

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

/*                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, 1);*/

                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Product Picture"), 200);

                } else {

                    showAltersDialogue("You need to give access to upload image");
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200) {
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

                } else if (data.getData() != null) {
                    Uri imagePath = data.getData();

                    if (uris.size() <= 5)
                        uris.add(imagePath.toString());

                    rrProductImageAdapter.setData(uris);
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
        } else {
            selectImage();
        }
    }

    private void prepareClaimSuccess() {

        claimDialogueBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.claim_dialogue, null, false);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        claimSuccess = dialogBuilder.create();
        claimSuccess.setCanceledOnTouchOutside(false);
        claimSuccess.setCancelable(false);

        claimSuccess.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        claimSuccess.setView(claimDialogueBinding.getRoot());

    }
}
