package com.lalaland.ecommerce.adapters;

import android.content.Context;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.data.models.cart.CartItem;
import com.lalaland.ecommerce.databinding.CartHeaderItemBinding;
import com.lalaland.ecommerce.databinding.CartItemBinding;

import java.util.List;

import static com.lalaland.ecommerce.helpers.AppConstants.ITEM_SOLD_OUT;
import static com.lalaland.ecommerce.helpers.AppConstants.TAG;


public class CartItemsAdapter extends RecyclerView.Adapter<CartItemsAdapter.CartItemViewHolder> {

    private Context mContext;
    private List<CartItem> mCartItems;
    private CartItemBinding cartItemBinding;
    private CartHeaderItemBinding cartHeaderItemBinding;

    private LayoutInflater inflater;
    private CartClickListener mCartClickListener;
    private SparseBooleanArray itemStateArray = new SparseBooleanArray();

    public CartItemsAdapter(Context context, CartClickListener cartClickListener) {
        mContext = context;
        inflater = LayoutInflater.from(context);
        mCartClickListener = cartClickListener;
    }

    @NonNull
    @Override
    public CartItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        cartItemBinding = DataBindingUtil.inflate(inflater, R.layout.cart_item, parent, false);

        // for testing now
        if (mContext.getClass().getSimpleName().equals("CheckoutScreen")) {
            cartItemBinding.ivDeleteItem.setVisibility(View.GONE);
            cartItemBinding.cbAddToList.setVisibility(View.GONE);
            cartItemBinding.counterContainer.setVisibility(View.GONE);
            cartItemBinding.tvQuantityDetail.setVisibility(View.VISIBLE);
        }

        return new CartItemViewHolder(cartItemBinding);


/*        if (viewType == R.layout.cart_item) {
            cartItemBinding = DataBindingUtil.inflate(inflater, R.layout.cart_item, parent, false);

            // for testing now
            if (mContext.getClass().getSimpleName().equals("CheckoutScreen")) {
                cartItemBinding.ivDeleteItem.setVisibility(View.GONE);
                cartItemBinding.cbAddToList.setVisibility(View.GONE);
                cartItemBinding.counterContainer.setVisibility(View.GONE);
                cartItemBinding.tvQuantityDetail.setVisibility(View.VISIBLE);
            }

            return new CartItemViewHolder(cartItemBinding);
        } else if (viewType == R.layout.cart_header_item) {

            cartHeaderItemBinding = DataBindingUtil.inflate(inflater, R.layout.cart_header_item, parent, false);
            return new CartHeaderViewHolder(cartHeaderItemBinding);
        } else {
            cartHeaderItemBinding = DataBindingUtil.inflate(inflater, R.layout.cart_header_item, parent, false);
            return new CartHeaderViewHolder(cartHeaderItemBinding);
        }*/
    }

/*    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

*//*        CartItem cartItem = mCartItems.get(position);

        if (holder instanceof CartItemViewHolder) {
            ((CartItemViewHolder) holder).bindHolder(cartItem);
        } else if (holder instanceof CartHeaderViewHolder) {
            ((CartHeaderViewHolder) holder).bindHolder(cartItem);
        }*//*
    }*/

    @Override
    public void onBindViewHolder(@NonNull CartItemViewHolder holder, int position) {

        CartItem cartItem = mCartItems.get(position);
        holder.bindHolder(cartItem);
    }

    @Override
    public int getItemCount() {

        if (mCartItems == null)
            return 0;

        return mCartItems.size();
    }


    public void setData(List<CartItem> cartItemList) {

        mCartItems = cartItemList;
        notifyDataSetChanged();
    }

    public void changeNumberOfCount(int position, int value) {


        int quantity = mCartItems.get(position).getItemQuantity();
        int remainingQuantity = Integer.parseInt(mCartItems.get(position).getRemainingQuantity());


        if (value > 0) {
            if (remainingQuantity > quantity) {
                quantity += 1;
                mCartClickListener.changeNumberOfCount(position, quantity);
            } else
                Toast.makeText(mContext, ITEM_SOLD_OUT, Toast.LENGTH_SHORT).show();
        } else {

            if (quantity > 0) {
                quantity -= 1;
                mCartClickListener.changeNumberOfCount(position, quantity);
            }
        }
    }

    public interface CartClickListener {

        void addItemToList(int position);

        void deleteFromCart(int position);

        void changeNumberOfCount(int position, int quantity);
    }

    class CartHeaderViewHolder extends RecyclerView.ViewHolder {

        CartHeaderItemBinding mCartHeaderItemBinding;

        public CartHeaderViewHolder(CartHeaderItemBinding cartHeaderItemBinding) {
            super(cartHeaderItemBinding.getRoot());
            mCartHeaderItemBinding = cartHeaderItemBinding;
        }

        void bindHolder(CartItem cartItem) {

            mCartHeaderItemBinding.tvBrandName.setText(cartItem.getMerchantName());
            mCartHeaderItemBinding.setAdapter(CartItemsAdapter.this);
            mCartHeaderItemBinding.executePendingBindings();
        }

    }

    class CartItemViewHolder extends RecyclerView.ViewHolder {

        CartItemBinding mCartItemBinding;

        CartItemViewHolder(@NonNull CartItemBinding cartItemBinding) {
            super(cartItemBinding.getRoot());

            mCartItemBinding = cartItemBinding;
        }

        void bindHolder(CartItem cartItem) {
            mCartItemBinding.setCartItem(cartItem);
            mCartItemBinding.setAdapter(CartItemsAdapter.this);
            mCartItemBinding.executePendingBindings();

            //mCartItemBinding.cbAddToList.setChecked(itemStateArray.get(getAdapterPosition(), false));

            if (cartItem.getCartStatus() == 1)
                mCartItemBinding.cbAddToList.setChecked(false);
            else if (cartItem.getCartStatus() == 3)
                mCartItemBinding.cbAddToList.setChecked(true);
            else
                mCartItemBinding.cbAddToList.setChecked(true);

            mCartItemBinding.ivDeleteItem.setOnClickListener(v -> {
                if (getAdapterPosition() != RecyclerView.NO_POSITION)

                    Log.d(TAG, "DELETE_ITEMS_ADAPTER #" + getAdapterPosition());

                mCartClickListener.deleteFromCart(getAdapterPosition());
            });

            mCartItemBinding.cbAddToList.setOnClickListener(v -> {

                mCartClickListener.addItemToList(getAdapterPosition());
            });

            mCartItemBinding.btnAdd.setOnClickListener(v -> changeNumberOfCount(getAdapterPosition(), 1));
            mCartItemBinding.btnSun.setOnClickListener(v -> changeNumberOfCount(getAdapterPosition(), -1));
        }
    }

    @Override
    public int getItemViewType(int position) {

        if (position != 0
                && !mCartItems.get(position).getMerchantId().equals(mCartItems.get(position - 1).getMerchantId())) {

            return R.layout.cart_item;
        } else
            return R.layout.cart_header_item;
    }
}
