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
import com.lalaland.ecommerce.helpers.AppUtils;

import java.util.ArrayList;
import java.util.List;


public class SearchProductAdapter extends RecyclerView.Adapter<SearchProductAdapter.SearchProductViewHolder> {

    private Context mContext;
    private List<SearchCategory> mSearches = new ArrayList<>();
    private SearchItemBinding searchItemBinding;
    private LayoutInflater inflater;
    private SearchListener mSearchListener;
    private boolean mIsHistory;

    public SearchProductAdapter(Context context, SearchListener searchListener, boolean isHistory) {
        mContext = context;
        inflater = LayoutInflater.from(context);
        mSearchListener = searchListener;
        mIsHistory = isHistory;
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

    public void onSearchClicked(int position) {
        mSearchListener.onSearchProductClicked(position, mIsHistory);
    }

    public void onSearchDelete(int position) {
        mSearchListener.onSearchProductDelete(position, mIsHistory);
    }

    class SearchProductViewHolder extends RecyclerView.ViewHolder {

        SearchItemBinding mSearchItemBinding;

        SearchProductViewHolder(@NonNull SearchItemBinding searchItemBinding) {
            super(searchItemBinding.getRoot());

            mSearchItemBinding = searchItemBinding;
        }

        void bindHolder(SearchCategory search) {

            if (mIsHistory)
                mSearchItemBinding.ivSearchDelete.setVisibility(View.VISIBLE);
            else
                mSearchItemBinding.ivSearchArrow.setVisibility(View.VISIBLE);

            mSearchItemBinding.tvSearch.setText(AppUtils.formatSearchUrl(search.getUrlName()));

            mSearchItemBinding.searchParent.setOnClickListener(v -> {
                onSearchClicked(getAdapterPosition());
            });

            // if search product are listing
            mSearchItemBinding.ivSearchArrow.setOnClickListener(v -> {
                onSearchClicked(getAdapterPosition());
            });

            mSearchItemBinding.ivSearchDelete.setOnClickListener(v -> {

                if (getAdapterPosition() != RecyclerView.NO_POSITION)
                    onSearchDelete(getAdapterPosition());
            });

            mSearchItemBinding.executePendingBindings();
        }
    }

    public interface SearchListener {
        void onSearchProductClicked(int position, boolean isHistory);

        void onSearchProductDelete(int position, boolean isHistory);
    }
}
