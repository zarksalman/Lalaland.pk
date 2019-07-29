package com.lalaland.ecommerce.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.data.models.userAddressBook.UserAddresses;
import com.lalaland.ecommerce.databinding.AddressItemBinding;

import java.util.ArrayList;
import java.util.List;


public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder> {

    private Context mContext;
    private List<UserAddresses> mUserAddresses = new ArrayList<>();
    private AddressItemBinding addressItemBinding;
    private LayoutInflater inflater;
    private AddressListener mAddressListener;

    public AddressAdapter(Context context, AddressListener addressListener) {
        mContext = context;
        inflater = LayoutInflater.from(context);
        mAddressListener = addressListener;
    }

    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        addressItemBinding = DataBindingUtil.inflate(inflater, R.layout.address_item, parent, false);
        return new AddressViewHolder(addressItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder, int position) {
        UserAddresses userAddresses = mUserAddresses.get(position);
        holder.bindHolder(userAddresses);
    }

    @Override
    public int getItemCount() {
        return mUserAddresses.size();
    }

    public void setData(List<UserAddresses> userAddresses) {

        mUserAddresses = userAddresses;

        notifyDataSetChanged();
    }

    public void onSetDefaultAddressClicked(View view, UserAddresses userAddresses) {
        mAddressListener.onSetDefaultAddressClicked(userAddresses);
    }

    public void onAddressClicked(View view, UserAddresses userAddresses) {
        mAddressListener.onAddressClicked(userAddresses);
    }

    public void onEditAddressClicked(View view, UserAddresses userAddresses) {
        mAddressListener.onEditAddressClicked(userAddresses);
    }

    class AddressViewHolder extends RecyclerView.ViewHolder {

        AddressItemBinding mAddressItemBinding;

        AddressViewHolder(@NonNull AddressItemBinding addressItemBinding) {
            super(addressItemBinding.getRoot());

            mAddressItemBinding = addressItemBinding;
        }

        void bindHolder(UserAddresses userAddresses) {
            mAddressItemBinding.setUser(userAddresses);
            mAddressItemBinding.setAdapter(AddressAdapter.this);
            mAddressItemBinding.executePendingBindings();
        }
    }

    public interface AddressListener {
        void onAddressClicked(UserAddresses userAddresses);

        void onSetDefaultAddressClicked(UserAddresses userAddresses);

        void onEditAddressClicked(UserAddresses userAddresses);
    }
}
