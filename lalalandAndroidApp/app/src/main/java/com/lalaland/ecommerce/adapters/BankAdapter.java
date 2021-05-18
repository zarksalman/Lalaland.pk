package com.lalaland.ecommerce.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.databinding.BankItemBinding;

import java.util.ArrayList;
import java.util.List;


public class BankAdapter extends RecyclerView.Adapter<BankAdapter.BankViewHolder> {

    private Context mContext;
    private List<String> mBankItem = new ArrayList<>();
    private List<String> mFilteredBankItem = new ArrayList<>();
    private BankItemBinding bankLayoutBinding;
    private LayoutInflater inflater;

    public BankAdapter(Context context) {
        mContext = context;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public BankViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        bankLayoutBinding = DataBindingUtil.inflate(inflater, R.layout.bank_item, parent, false);
        return new BankViewHolder(bankLayoutBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull BankViewHolder holder, int position) {
        String bank = mFilteredBankItem.get(position);
        holder.bindHolder(bank);
    }

    @Override
    public int getItemCount() {
        return mFilteredBankItem.size();
    }

    public void setData(List<String> payProData) {

        mBankItem = payProData;
        mFilteredBankItem = mBankItem;
        notifyDataSetChanged();
    }


    public void filter(String text) {
        mFilteredBankItem.clear();

        if (text.isEmpty()) {
            mFilteredBankItem.addAll(mBankItem);
        } else {
            text = text.toLowerCase();
            for (String bankItem : mBankItem) {
                if (bankItem.toLowerCase().contains(text)) {
                    mFilteredBankItem.add(bankItem);
                }
            }
        }

        // selected default value i.e select internet banking
        if (mFilteredBankItem.size() == 0)
            mFilteredBankItem.addAll(mBankItem);

        notifyDataSetChanged();
    }

    class BankViewHolder extends RecyclerView.ViewHolder {

        BankItemBinding mbankLayoutBinding;

        BankViewHolder(@NonNull BankItemBinding bankLayoutBinding) {
            super(bankLayoutBinding.getRoot());
            mbankLayoutBinding = bankLayoutBinding;
        }

        void bindHolder(String bank) {
            Glide.with(mContext)
                    .load(bank)
                    .into(mbankLayoutBinding.ivBank);
        }
    }
}
