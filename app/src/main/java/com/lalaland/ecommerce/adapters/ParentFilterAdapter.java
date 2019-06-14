package com.lalaland.ecommerce.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.data.models.filters.ParentFilter;
import com.lalaland.ecommerce.databinding.FilterItemBinding;

import java.util.ArrayList;
import java.util.List;


public class ParentFilterAdapter extends RecyclerView.Adapter<ParentFilterAdapter.ParentFilterViewHolder> {

    private Context mContext;
    private List<ParentFilter> mParentFilterList, filteredCityList = new ArrayList<>();
    private FilterItemBinding filterItemBinding;
    private LayoutInflater inflater;
    private ParentFilterListener mparentFilterListener;

    public ParentFilterAdapter(Context context, ParentFilterListener parentFilterListener) {
        mContext = context;
        inflater = LayoutInflater.from(context);

        mparentFilterListener = parentFilterListener;
    }

    @NonNull
    @Override
    public ParentFilterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        filterItemBinding = DataBindingUtil.inflate(inflater, R.layout.filter_item, parent, false);
        return new ParentFilterViewHolder(filterItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ParentFilterViewHolder holder, int position) {
        ParentFilter parentFilter = mParentFilterList.get(position);
        holder.bindHolder(parentFilter);
    }

    @Override
    public int getItemCount() {
        return mParentFilterList.size();
    }

    public void setData(List<ParentFilter> parentFilters) {

        mParentFilterList = parentFilters;
        filteredCityList.addAll(mParentFilterList);

        notifyDataSetChanged();
    }


    class ParentFilterViewHolder extends RecyclerView.ViewHolder {

        FilterItemBinding mFilterItemBinding;

        ParentFilterViewHolder(@NonNull FilterItemBinding filterItemBinding) {
            super(filterItemBinding.getRoot());

            mFilterItemBinding = filterItemBinding;
        }

        void bindHolder(ParentFilter parentFilter) {

            mFilterItemBinding.getRoot().setOnClickListener(v -> {
                mparentFilterListener.onFilterClick(mParentFilterList.get(getAdapterPosition()));
            });

            mFilterItemBinding.setParentFilter(parentFilter);
            mFilterItemBinding.setAdapter(ParentFilterAdapter.this);
            mFilterItemBinding.executePendingBindings();
        }
    }

    public interface ParentFilterListener {
        void onFilterClick(ParentFilter filter);
    }

}
