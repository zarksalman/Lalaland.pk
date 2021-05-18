package com.lalaland.ecommerce.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.data.models.filters.Filter;
import com.lalaland.ecommerce.databinding.FiltersItemBinding;

import java.util.ArrayList;
import java.util.List;


public class FiltersAdapter extends RecyclerView.Adapter<FiltersAdapter.FilterViewHolder> {

    private Context mContext;
    private List<Filter> mFilterList = new ArrayList<>();
    private FiltersItemBinding filterItemBinding;
    private LayoutInflater inflater;

    List<Filter> selectedFilters = new ArrayList<>();
    private SparseBooleanArray itemStateArray = new SparseBooleanArray();

    String filterName;
    String color;
    String[] colorRgb;
    Integer[] rgbColor = new Integer[3];
    Float percentFactor = 1.0f;
    Integer checkedPosition = -1;

    public FiltersAdapter(Context context, String filterName) {
        mContext = context;
        inflater = LayoutInflater.from(context);
        this.filterName = filterName;

    }

    @NonNull
    @Override
    public FilterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        filterItemBinding = DataBindingUtil.inflate(inflater, R.layout.filters_item, parent, false);
        return new FilterViewHolder(filterItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull FilterViewHolder holder, int position) {
        Filter filter = mFilterList.get(position);
        holder.bindHolder(filter);
    }

    @Override
    public int getItemCount() {
        return mFilterList.size();
    }

    public void setData(List<Filter> filterList) {

        mFilterList = filterList;
        notifyDataSetChanged();
    }

    public void setFirstFilter() {

        itemStateArray.put(0, true);
        checkedPosition = 0;
        notifyDataSetChanged();
    }

    public Filter getSelectedCategory() {

        if (checkedPosition != -1)
            return mFilterList.get(checkedPosition);
        else
            return null;
    }

    public List<Filter> getSelectedFilters() {

        selectedFilters.clear();

        if (itemStateArray.size() > 0) {

            for (int i = 0; i < mFilterList.size(); i++) {

                if (itemStateArray.get(i)) {
                    selectedFilters.add(mFilterList.get(i));
                }
            }
            return selectedFilters;
        }

        return null;
    }

    class FilterViewHolder extends RecyclerView.ViewHolder {

        FiltersItemBinding mFilterItemBinding;

        FilterViewHolder(@NonNull FiltersItemBinding filterItemBinding) {
            super(filterItemBinding.getRoot());

            mFilterItemBinding = filterItemBinding;
        }

        void bindHolder(Filter filter) {

            if (filterName.equals("Color")) {

                mFilterItemBinding.ivFilterColor.setVisibility(View.VISIBLE);
                mFilterItemBinding.ivFilterColor.setImageDrawable(null);

                if (filter.getDisplayName().toLowerCase().contains("multi"))
                    mFilterItemBinding.ivFilterColor.setImageDrawable(mContext.getResources().getDrawable(R.drawable.muli_color_icon));
                else if (filter.getFilterValue().toLowerCase().contains("#")) {
                    mFilterItemBinding.ivFilterColor.setBackgroundColor(Color.parseColor(filter.getFilterValue()));
                } else if (filter.getFilterValue().toLowerCase().contains("rgb")) {


                    color = filter.getFilterValue();
                    color = color.replace("rgb(", "");
                    color = color.replace(")", "");

                    if (color.contains("%")) {

                        color = color.replace("%", "");
                        percentFactor = 2.55f;
                    }

                    colorRgb = color.split(",");

                    rgbColor[0] = Math.round(Float.parseFloat(colorRgb[0]) * percentFactor);
                    rgbColor[1] = Math.round(Float.parseFloat(colorRgb[1]) * percentFactor);
                    rgbColor[2] = Math.round(Float.parseFloat(colorRgb[2]) * percentFactor);

                    mFilterItemBinding.ivFilterColor.setBackgroundColor(Color.rgb(rgbColor[0], rgbColor[1], rgbColor[2]));
                } else
                    mFilterItemBinding.ivFilterColor.setBackgroundColor(Color.rgb(255, 255, 255));
            }

            if (filterName.equals("Category")) {

/*                if (checkedPosition == -1) {
                    mFilterItemBinding.ivFilter.setVisibility(View.GONE);
                }*/

                if (checkedPosition == getAdapterPosition()) {
                    mFilterItemBinding.ivFilter.setVisibility(View.VISIBLE);
                } else {
                    mFilterItemBinding.ivFilter.setVisibility(View.GONE);
                }


                mFilterItemBinding.getRoot().setOnClickListener(view -> {
                    mFilterItemBinding.ivFilter.setVisibility(View.VISIBLE);

                    if (checkedPosition != getAdapterPosition()) {
                        notifyItemChanged(checkedPosition);
                        checkedPosition = getAdapterPosition();
                    }
                });
            } else {
                // use the sparse boolean array to check
                if (itemStateArray.get(getAdapterPosition(), false)) {

                    mFilterItemBinding.ivFilter.setVisibility(View.VISIBLE);
                } else {
                    mFilterItemBinding.ivFilter.setVisibility(View.GONE);
                }


                mFilterItemBinding.getRoot().setOnClickListener(v -> {

                    if (!itemStateArray.get(getAdapterPosition(), false)) {

                        if (getAdapterPosition() == 0) {
                            setAllUnCheck();
                        } else {
                            mFilterItemBinding.ivFilter.setVisibility(View.VISIBLE);
                            itemStateArray.put(getAdapterPosition(), true);
                            itemStateArray.put(0, false);
                            notifyDataSetChanged();
                        }
                    } else {

                        if (getAdapterPosition() != 0) {
                            mFilterItemBinding.ivFilter.setVisibility(View.GONE);
                            itemStateArray.put(getAdapterPosition(), false);

                            if (getSelectedFilters().size() == 0) {
                                itemStateArray.put(0, true);
                                notifyDataSetChanged();
                            }

                        }
                    }
                });
            }

            mFilterItemBinding.setFilter(filter);
            mFilterItemBinding.setAdapter(FiltersAdapter.this);
            mFilterItemBinding.executePendingBindings();
        }
    }

    private void setAllUnCheck() {

        for (int i = 0; i < itemStateArray.size(); i++) {
            itemStateArray.put(i, false);
        }

        itemStateArray.put(0, true);
        notifyDataSetChanged();
    }
}
