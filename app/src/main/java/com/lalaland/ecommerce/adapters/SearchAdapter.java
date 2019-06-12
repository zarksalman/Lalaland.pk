package com.lalaland.ecommerce.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.databinding.SearchItemBinding;

import java.util.ArrayList;
import java.util.List;


public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    private Context mContext;
    private List<String> mSearches, filteredSearchList = new ArrayList<>();
    private SearchItemBinding searchItemBinding;
    private LayoutInflater inflater;
    private SearchListener mSearchListener;

    public SearchAdapter(Context context, SearchListener searchListener) {
        mContext = context;
        inflater = LayoutInflater.from(context);
        mSearchListener = searchListener;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        searchItemBinding = DataBindingUtil.inflate(inflater, R.layout.search_item, parent, false);
        return new SearchViewHolder(searchItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        String searchItem = filteredSearchList.get(position);
        holder.bindHolder(searchItem);
    }

    @Override
    public int getItemCount() {
        return filteredSearchList.size();
    }

    public void setData(List<String> cityList) {

        mSearches = cityList;
        filteredSearchList.addAll(cityList);
        notifyDataSetChanged();
    }

    public void onSearchClicked(String search) {
        mSearchListener.onSearchClicked(search);
    }

    public void onSearchDelete(String search) {
        mSearchListener.onSearchDelete(search);
    }

    public void filter(String text) {
        filteredSearchList.clear();

        if (text.isEmpty()) {
            filteredSearchList.addAll(mSearches);
        } else {
            text = text.toLowerCase();
            for (String search : mSearches) {
                if (search.toLowerCase().contains(text)) {
                    filteredSearchList.add(search);
                }
            }
        }
        notifyDataSetChanged();
    }

    class SearchViewHolder extends RecyclerView.ViewHolder {

        SearchItemBinding mSearchItemBinding;

        SearchViewHolder(@NonNull SearchItemBinding searchItemBinding) {
            super(searchItemBinding.getRoot());

            mSearchItemBinding = searchItemBinding;
        }

        void bindHolder(String search) {

            mSearchItemBinding.ivSearchArrow.setVisibility(View.GONE);
            mSearchItemBinding.tvSearch.setText(search);

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
        void onSearchClicked(String search);

        void onSearchDelete(String search);
    }
}
