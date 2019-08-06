package com.lalaland.ecommerce.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.data.models.home.CustomProductsFive;
import com.lalaland.ecommerce.data.models.home.FeaturedBrand;
import com.lalaland.ecommerce.databinding.GetTheLookItemBinding;

import java.util.List;


public class GetTheLooksAdapter extends RecyclerView.Adapter<GetTheLooksAdapter.BrandsFocusViewHolder> {

    private Context mContext;
    private List<CustomProductsFive> mCustomProductsFives;
    private GetTheLookItemBinding getTheLookItemBinding;
    private LayoutInflater inflater;
    private GetTheLooksClickListener mGetTheLooksClickListener;

    public GetTheLooksAdapter(Context context, GetTheLooksClickListener getTheLooksClickListener) {
        mContext = context;
        mGetTheLooksClickListener = getTheLooksClickListener;
        inflater = LayoutInflater.from(context);

    }

    @NonNull
    @Override
    public BrandsFocusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        getTheLookItemBinding = DataBindingUtil.inflate(inflater, R.layout.get_the_look_item, parent, false);
        return new BrandsFocusViewHolder(getTheLookItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull BrandsFocusViewHolder holder, int position) {
        CustomProductsFive customProductsFive = mCustomProductsFives.get(position);
        holder.bindHolder(customProductsFive);
    }

    @Override
    public int getItemCount() {
        return mCustomProductsFives.size();
    }

    public void getTheLooksClicked(View view, CustomProductsFive customProductsFive) {
        mGetTheLooksClickListener.onGetTheLookClicked(customProductsFive);
    }

    public void setData(List<CustomProductsFive> customProductsFives) {

        mCustomProductsFives = customProductsFives;
        notifyDataSetChanged();
    }

    class BrandsFocusViewHolder extends RecyclerView.ViewHolder {

        GetTheLookItemBinding mGetTheLookItemBinding;

        BrandsFocusViewHolder(@NonNull GetTheLookItemBinding getTheLookItemBinding) {
            super(getTheLookItemBinding.getRoot());

            mGetTheLookItemBinding = getTheLookItemBinding;
        }

        void bindHolder(CustomProductsFive customProductsFive) {
           mGetTheLookItemBinding.setCustomProduct(customProductsFive);
           mGetTheLookItemBinding.setAdapter(GetTheLooksAdapter.this);
           mGetTheLookItemBinding.executePendingBindings();
        }
    }

    public interface GetTheLooksClickListener {
        void onGetTheLookClicked(CustomProductsFive customProductsFive);
    }
}
