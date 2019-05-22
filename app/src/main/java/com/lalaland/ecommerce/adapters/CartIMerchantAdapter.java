package com.lalaland.ecommerce.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.data.models.cartListingModel.CartListModel;
import com.lalaland.ecommerce.databinding.CartMerchantItemBinding;

import java.util.List;


public class CartIMerchantAdapter extends RecyclerView.Adapter<CartIMerchantAdapter.MerchantItemViewHolder> implements CartItemsAdapter.CartClickListener {

    private Context mContext;
    private List<CartListModel> mCartListModelList;
    private CartMerchantItemBinding cartMerchantItemBinding;
    private MerchantItemClickListener mMerchantItemClickListener;
    private LayoutInflater inflater;

    public CartIMerchantAdapter(Context context, MerchantItemClickListener merchantItemClickListener) {
        mContext = context;
        inflater = LayoutInflater.from(context);

        mMerchantItemClickListener = merchantItemClickListener;
    }

    @NonNull
    @Override
    public CartIMerchantAdapter.MerchantItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        cartMerchantItemBinding = DataBindingUtil.inflate(inflater, R.layout.cart_merchant_item, parent, false);
        return new MerchantItemViewHolder(cartMerchantItemBinding);
    }


    @Override
    public void onBindViewHolder(@NonNull CartIMerchantAdapter.MerchantItemViewHolder holder, int position) {

        CartListModel cartListModel = mCartListModelList.get(position);


        CartItemsAdapter cartItemsAdapter = new CartItemsAdapter(mContext, CartIMerchantAdapter.this);

        cartMerchantItemBinding.rvCartProducts.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false));
        cartMerchantItemBinding.rvCartProducts.setAdapter(cartItemsAdapter);

        cartItemsAdapter.setData(cartListModel.getCartItemList());

        holder.bindHolder(cartListModel);
    }

    @Override
    public int getItemCount() {

        if (mCartListModelList == null)
            return 0;

        return mCartListModelList.size();
    }


    public void setData(List<CartListModel> cartListModels) {

        mCartListModelList = cartListModels;
        notifyDataSetChanged();
    }

    @Override
    public void addItemToList(int position) {
        mMerchantItemClickListener.addItemToList(position);
    }

    @Override
    public void deleteFromCart(int position) {
        mMerchantItemClickListener.deleteFromCart(position);
    }

    @Override
    public void changeNumberOfCount(int position, int quantity) {
        mMerchantItemClickListener.changeNumberOfCount(position, quantity);
    }

    class MerchantItemViewHolder extends RecyclerView.ViewHolder implements CartItemsAdapter.CartClickListener {

        CartMerchantItemBinding mCartMerchantItemBinding;

        MerchantItemViewHolder(@NonNull CartMerchantItemBinding cartMerchantItemBinding) {
            super(cartMerchantItemBinding.getRoot());

            mCartMerchantItemBinding = cartMerchantItemBinding;
        }

        void bindHolder(CartListModel cartListModel) {

            mCartMerchantItemBinding.setCartListItem(cartListModel);
            mCartMerchantItemBinding.setAdapter(CartIMerchantAdapter.this);
            mCartMerchantItemBinding.executePendingBindings();
        }

        @Override
        public void addItemToList(int position) {
            mMerchantItemClickListener.addItemToList(position);
        }

        @Override
        public void deleteFromCart(int position) {
            mMerchantItemClickListener.deleteFromCart(position);
        }

        @Override
        public void changeNumberOfCount(int position, int quantity) {
            mMerchantItemClickListener.changeNumberOfCount(position, quantity);
        }
    }

    public interface MerchantItemClickListener {

        void addItemToList(int position);

        void deleteFromCart(int position);

        void changeNumberOfCount(int position, int quantity);
    }
}
