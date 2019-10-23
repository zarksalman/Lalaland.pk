package com.lalaland.ecommerce.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.data.models.returnAndReplacement.claimListing.Claim;
import com.lalaland.ecommerce.databinding.ClaimItemBinding;

import java.util.ArrayList;
import java.util.List;


public class ClaimAdapter extends RecyclerView.Adapter<ClaimAdapter.ClaimViewHolder> {

    private Context mContext;
    private List<Claim> mClaimList = new ArrayList<>();
    private ClaimItemBinding claimItemBinding;
    private LayoutInflater inflater;
    private ClaimListener mClaimListener;

    public ClaimAdapter(Context context, ClaimListener claimListener) {
        mContext = context;
        inflater = LayoutInflater.from(context);
        mClaimListener = claimListener;

    }

    @NonNull
    @Override
    public ClaimViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        claimItemBinding = DataBindingUtil.inflate(inflater, R.layout.claim_item, parent, false);
        return new ClaimViewHolder(claimItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ClaimViewHolder holder, int position) {
        Claim claim = mClaimList.get(position);
        holder.bindHolder(claim);
    }

    @Override
    public int getItemCount() {
        return mClaimList.size();
    }

    public void setData(List<Claim> claimList) {

        mClaimList = claimList;
        notifyDataSetChanged();
    }

    public void claimClicked(View view, Claim claim) {
        mClaimListener.onClaimClicked(claim);
    }

    class ClaimViewHolder extends RecyclerView.ViewHolder {

        ClaimItemBinding mClaimItemBinding;

        ClaimViewHolder(@NonNull ClaimItemBinding claimItemBinding) {
            super(claimItemBinding.getRoot());

            mClaimItemBinding = claimItemBinding;
        }

        void bindHolder(Claim claim) {
            mClaimItemBinding.setClaim(claim);
            mClaimItemBinding.setAdapter(ClaimAdapter.this);
            mClaimItemBinding.executePendingBindings();
        }
    }

    public interface ClaimListener {
        void onClaimClicked(Claim claim);
    }
}
