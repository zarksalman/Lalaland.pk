package com.lalaland.ecommerce.adapters;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.databinding.ProductImageRrItemBinding;
import com.lalaland.ecommerce.databinding.ProductImageRrItemUploaddBinding;

import java.util.ArrayList;
import java.util.List;


public class RRProductImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<String> mUris = new ArrayList<>();
    private ProductImageRrItemBinding rrItemBinding;
    private ProductImageRrItemUploaddBinding productImageRrItemUploaddBinding;
    private LayoutInflater inflater;
    private RRImageListener mRRImageListener;
    private float width = 0;

    public RRProductImageAdapter(Context context, RRImageListener rrImageListener) {
        mContext = context;
        inflater = LayoutInflater.from(context);
        mRRImageListener = rrImageListener;
        width = getScreenWidth();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        if (viewType == 0) {

            productImageRrItemUploaddBinding = DataBindingUtil.inflate(inflater, R.layout.product_image_rr_item_uploadd, parent, false);
            productImageRrItemUploaddBinding.parent.getLayoutParams().width = (int) (width / 5);
            productImageRrItemUploaddBinding.parent.getLayoutParams().height = (int) (width / 5);
            return new RRImageUploadViewHolder(productImageRrItemUploaddBinding);

        } else {

            rrItemBinding = DataBindingUtil.inflate(inflater, R.layout.product_image_rr_item, parent, false);
            rrItemBinding.parent.getLayoutParams().width = (int) (width / 5);
            rrItemBinding.parent.getLayoutParams().height = (int) (width / 5);
            return new RRImageViewHolder(rrItemBinding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        //City city = filteredCityList.get(position);

        if (position == 0) {

            RRImageUploadViewHolder rrImageUploadViewHolder = (RRImageUploadViewHolder) holder;
            rrImageUploadViewHolder.bind();

        } else {

            RRImageViewHolder rrImageViewHolder = (RRImageViewHolder) holder;
            String imgUri = mUris.get(position);
            rrImageViewHolder.bindHolder(imgUri);
        }
    }

    @Override
    public int getItemCount() {
        return mUris.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void setData(List<String> uris) {

        if (uris.size() == 0)
            uris.add(0, "upload");

        mUris = uris;
        notifyDataSetChanged();
    }

    private int getScreenWidth() {

        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        return size.x;
    }

    class RRImageViewHolder extends RecyclerView.ViewHolder {

        ProductImageRrItemBinding mRRItemBinding;

        RRImageViewHolder(@NonNull ProductImageRrItemBinding rrItemBinding) {
            super(rrItemBinding.getRoot());

            mRRItemBinding = rrItemBinding;
        }

        void bindHolder(String uri) {

            Glide
                    .with(mContext)
                    .load(uri)
                    .placeholder(R.drawable.placeholder_products)
                    .into(mRRItemBinding.ivClaim);

            mRRItemBinding.ivDeleteRrImg.setOnClickListener(v -> {

                if (getAdapterPosition() != RecyclerView.NO_POSITION)
                    mRRImageListener.onRRImageDeleteClicked(getAdapterPosition());

            });
        }
    }

    class RRImageUploadViewHolder extends RecyclerView.ViewHolder {

        ProductImageRrItemUploaddBinding mProductImageRrItemUploaddBinding;

        RRImageUploadViewHolder(@NonNull ProductImageRrItemUploaddBinding productImageRrItemUploaddBinding) {
            super(productImageRrItemUploaddBinding.getRoot());
            mProductImageRrItemUploaddBinding = productImageRrItemUploaddBinding;
        }

        void bind() {

            Glide
                    .with(mContext)
                    .load(R.drawable.upload_icon)
                    .into(mProductImageRrItemUploaddBinding.ivClaim);


            mProductImageRrItemUploaddBinding.ivClaim.setOnClickListener(v -> {
                mRRImageListener.onRRImageUploadClicked(0);
            });
        }
    }

    public interface RRImageListener {
        void onRRImageDeleteClicked(int position);

        void onRRImageUploadClicked(int position);
    }


}
