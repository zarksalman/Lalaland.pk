package com.lalaland.ecommerce.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.data.models.globalSearch.SearchCategory;
import com.lalaland.ecommerce.databinding.SearchItemBinding;

import java.util.ArrayList;
import java.util.List;


public class SearchProductAdapter extends RecyclerView.Adapter<SearchProductAdapter.SearchProductViewHolder> {

    private Context mContext;
    private List<SearchCategory> mSearches = new ArrayList<>();
    private SearchItemBinding searchItemBinding;
    private LayoutInflater inflater;
    private SearchListener mSearchListener;

    public SearchProductAdapter(Context context, SearchListener searchListener) {
        mContext = context;
        inflater = LayoutInflater.from(context);
        mSearchListener = searchListener;
    }

    @NonNull
    @Override
    public SearchProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        searchItemBinding = DataBindingUtil.inflate(inflater, R.layout.search_item, parent, false);
        return new SearchProductViewHolder(searchItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchProductViewHolder holder, int position) {
        SearchCategory searchItem = mSearches.get(position);
        holder.bindHolder(searchItem);
    }

    @Override
    public int getItemCount() {
        return mSearches.size();
    }

    public void setData(List<SearchCategory> searchProducts) {

        mSearches = searchProducts;
        notifyDataSetChanged();
    }

    public void onSearchClicked(SearchCategory search) {
        mSearchListener.onSearchClicked(search);
    }

    public void onSearchDelete(SearchCategory search) {
        mSearchListener.onSearchDelete(search);
    }

    class SearchProductViewHolder extends RecyclerView.ViewHolder {

        SearchItemBinding mSearchItemBinding;

        SearchProductViewHolder(@NonNull SearchItemBinding searchItemBinding) {
            super(searchItemBinding.getRoot());

            mSearchItemBinding = searchItemBinding;
        }

        void bindHolder(SearchCategory search) {

            mSearchItemBinding.ivSearchDelete.setVisibility(View.GONE);
            mSearchItemBinding.tvSearch.setText(search.getName());

            mSearchItemBinding.searchParent.setOnClickListener(v -> {
                onSearchClicked(mSearches.get(getAdapterPosition()));
            });

            mSearchItemBinding.ivSearchDelete.setOnClickListener(v -> {
                onSearchDelete(mSearches.get(getAdapterPosition()));
            });

            mSearchItemBinding.executePendingBindings();
        }
    }

    public interface SearchListener {
        void onSearchClicked(SearchCategory search);

        void onSearchDelete(SearchCategory search);
    }
}
