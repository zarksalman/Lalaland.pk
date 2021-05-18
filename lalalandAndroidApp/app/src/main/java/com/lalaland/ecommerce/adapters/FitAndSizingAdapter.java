package com.lalaland.ecommerce.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.data.models.productDetails.FitAndSizing;
import com.lalaland.ecommerce.databinding.FitAndSizingItemBinding;

import java.util.List;


public class FitAndSizingAdapter extends RecyclerView.Adapter<FitAndSizingAdapter.FitAndSizingViewHolder> {

    private Context mContext;
    private List<FitAndSizing> mFitAndSizing;
    private FitAndSizingItemBinding mFitAndSizingItemBinding;
    private LayoutInflater inflater;

    public FitAndSizingAdapter(Context context) {
        mContext = context;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public FitAndSizingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        mFitAndSizingItemBinding = DataBindingUtil.inflate(inflater, R.layout.fit_and_sizing_item, parent, false);
        return new FitAndSizingViewHolder(mFitAndSizingItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull FitAndSizingViewHolder holder, int position) {
        FitAndSizing fitAndSizing = mFitAndSizing.get(position);
        holder.bindHolder(fitAndSizing);
    }

    @Override
    public int getItemCount() {
        return mFitAndSizing.size();
    }

    public void setData(List<FitAndSizing> fitAndSizing) {

        mFitAndSizing = fitAndSizing;
        notifyDataSetChanged();
    }


    class FitAndSizingViewHolder extends RecyclerView.ViewHolder {

        FitAndSizingItemBinding mFitAndSizingItemBinding;

        FitAndSizingViewHolder(@NonNull FitAndSizingItemBinding fitAndSizingItemBinding) {
            super(fitAndSizingItemBinding.getRoot());
            mFitAndSizingItemBinding = fitAndSizingItemBinding; // do this otherwise some items of adapter will change its content
        }

        void bindHolder(FitAndSizing fitAndSizing) {

            mFitAndSizingItemBinding.wvFitAndSize.loadData(fitAndSizing.getDescription().toString(), "text/html", "UTF-8");
            mFitAndSizingItemBinding.setFitAndSizing(fitAndSizing);
            mFitAndSizingItemBinding.setAdapter(FitAndSizingAdapter.this);
            mFitAndSizingItemBinding.executePendingBindings();
        }
    }

}
