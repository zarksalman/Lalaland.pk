package com.lalaland.ecommerce.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.data.models.home.Actions;
import com.lalaland.ecommerce.data.models.home.PicksOfTheWeek;
import com.lalaland.ecommerce.databinding.PickOfWeekLayoutBinding;

import java.util.List;


public class PickOfWeekAdapter extends RecyclerView.Adapter<PickOfWeekAdapter.PickPfWeekViewHolder> {

    private Context mContext;
    private List<PicksOfTheWeek> mPicks;
    private PickOfWeekLayoutBinding pickOfWeekLayoutBinding;
    private LayoutInflater inflater;
    private WeekProductClickListener mWeekProductClickListener;

    public PickOfWeekAdapter(Context context, WeekProductClickListener weekProductClickListener) {
        mContext = context;
        mWeekProductClickListener = weekProductClickListener;
        inflater = LayoutInflater.from(context);

    }

    @NonNull
    @Override
    public PickPfWeekViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        pickOfWeekLayoutBinding = DataBindingUtil.inflate(inflater, R.layout.pick_of_week_layout, parent, false);
        return new PickPfWeekViewHolder(pickOfWeekLayoutBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull PickPfWeekViewHolder holder, int position) {
        PicksOfTheWeek picksOfTheWeek = mPicks.get(position);
        holder.bindHolder(picksOfTheWeek);
    }

    @Override
    public int getItemCount() {
        return mPicks.size();
    }

    public void pickOfWeekClicked(View view, PicksOfTheWeek picksOfTheWeek) {
        mWeekProductClickListener.onWeekProductClicked(picksOfTheWeek);
    }

    public void setData(List<PicksOfTheWeek> picksOfTheWeekList) {

        mPicks = picksOfTheWeekList;
        notifyDataSetChanged();
    }

    class PickPfWeekViewHolder extends RecyclerView.ViewHolder {

        PickPfWeekViewHolder(@NonNull PickOfWeekLayoutBinding pickOfWeekLayoutBinding) {
            super(pickOfWeekLayoutBinding.getRoot());
        }

        void bindHolder(PicksOfTheWeek picksOfTheWeek) {
            pickOfWeekLayoutBinding.setPicks(picksOfTheWeek);
            pickOfWeekLayoutBinding.setAdapter(PickOfWeekAdapter.this);
            pickOfWeekLayoutBinding.executePendingBindings();
        }
    }

    public interface WeekProductClickListener {
        void onWeekProductClicked(PicksOfTheWeek picksOfTheWeek);
    }
}
