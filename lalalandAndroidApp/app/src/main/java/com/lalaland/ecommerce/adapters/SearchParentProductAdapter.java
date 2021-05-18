package com.lalaland.ecommerce.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.data.models.globalSearch.SearchParentCategory;
import com.lalaland.ecommerce.databinding.SearchParentItemBinding;

import java.util.ArrayList;
import java.util.List;


public class SearchParentProductAdapter extends RecyclerView.Adapter<SearchParentProductAdapter.SearchProductViewHolder> implements SearchProductAdapter.SearchListener {

    private Context mContext;
    private List<SearchParentCategory> mSearches = new ArrayList<>();
    private SearchParentItemBinding searchItemBinding;
    private LayoutInflater inflater;
    private SearchListener mSearchListener;
    private boolean mIsHistory;
    private SearchProductAdapter searchProductAdapter;

    public SearchParentProductAdapter(Context context, SearchListener searchListener, boolean isHistory) {
        mContext = context;
        inflater = LayoutInflater.from(context);
        mSearchListener = searchListener;
        mIsHistory = isHistory;
    }

    @NonNull
    @Override
    public SearchProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        searchItemBinding = DataBindingUtil.inflate(inflater, R.layout.search_parent_item, parent, false);

        return new SearchProductViewHolder(searchItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchProductViewHolder holder, int position) {
        SearchParentCategory searchItem = mSearches.get(position);

        searchProductAdapter = new SearchProductAdapter(mContext, this, mIsHistory);
        searchItemBinding.rvSearchProducts.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false));
        searchItemBinding.rvSearchProducts.setAdapter(searchProductAdapter);
        searchProductAdapter.setData(searchItem.getSearchCategories());
        searchProductAdapter.notifyDataSetChanged();

        holder.bindHolder(searchItem);
    }

    @Override
    public int getItemCount() {
        return mSearches.size();
    }

    public void setData(List<SearchParentCategory> searchProducts) {

        mSearches = searchProducts;
        notifyDataSetChanged();
    }

    @Override
    public void onSearchProductClicked(int parentId, int position, boolean isHistory) {
        mSearchListener.onSearchParentProductClicked(parentId, position, mIsHistory);
    }

    @Override
    public void onSearchProductDelete(int parentId, int position, boolean isHistory) {
        
        mSearchListener.onSearchParentProductDelete(parentId, position, mIsHistory);
    }

    class SearchProductViewHolder extends RecyclerView.ViewHolder {

        SearchParentItemBinding mSearchItemBinding;

        SearchProductViewHolder(@NonNull SearchParentItemBinding searchItemBinding) {
            super(searchItemBinding.getRoot());

            mSearchItemBinding = searchItemBinding;
        }

        void bindHolder(SearchParentCategory search) {

            mSearchItemBinding.tvSearchHeader.setText(search.getParentName());
            mSearchItemBinding.executePendingBindings();
        }
    }

    public interface SearchListener {
        void onSearchParentProductClicked(int parentId, int position, boolean isHistory);

        void onSearchParentProductDelete(int parentId, int position, boolean isHistory);
    }
}
