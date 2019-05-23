package com.lalaland.ecommerce.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.data.models.cart.CartItem;
import com.lalaland.ecommerce.databinding.CartItemBinding;

import java.util.List;

import static com.lalaland.ecommerce.helpers.AppConstants.ITEM_SOLD_OUT;
import static com.lalaland.ecommerce.helpers.AppConstants.TAG;


public class CartItemsAdapter extends RecyclerView.Adapter<CartItemsAdapter.CartItemViewHolder> {

    private Context mContext;
    private List<CartItem> mCartItems;
    private CartItemBinding cartItemBinding;

    private LayoutInflater inflater;
    private CartClickListener mCartClickListener;

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
    }


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

    public void changeNumberOfCount(int merchantId, int position, int value) {


        int quantity = mCartItems.get(position).getItemQuantity();
        int remainingQuantity = Integer.parseInt(mCartItems.get(position).getRemainingQuantity());


        if (value > 0) {
            if (remainingQuantity > quantity) {
                quantity += 1;
                mCartClickListener.changeNumberOfCount(merchantId, position, quantity);
            } else
                Toast.makeText(mContext, ITEM_SOLD_OUT, Toast.LENGTH_SHORT).show();
        } else {

            if (quantity > 0) {
                quantity -= 1;
                mCartClickListener.changeNumberOfCount(merchantId, position, quantity);
            }
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

            if (cartItem.getCartStatus() == 1)
                mCartItemBinding.cbAddToList.setChecked(false);
            else if (cartItem.getCartStatus() == 3)
                mCartItemBinding.cbAddToList.setChecked(true);
            else
                mCartItemBinding.cbAddToList.setChecked(true);

            mCartItemBinding.ivDeleteItem.setOnClickListener(v -> {
                if (getAdapterPosition() != RecyclerView.NO_POSITION)

                    Log.d(TAG, "DELETE_ITEMS_ADAPTER #" + getAdapterPosition());

                mCartClickListener.deleteFromCart(cartItem.getMerchantId(), getAdapterPosition());
            });

            mCartItemBinding.cbAddToList.setOnClickListener(v -> {

                mCartClickListener.addItemToList(cartItem.getMerchantId(), getAdapterPosition());
            });

            mCartItemBinding.btnAdd.setOnClickListener(v -> changeNumberOfCount(cartItem.getMerchantId(), getAdapterPosition(), 1));
            mCartItemBinding.btnSub.setOnClickListener(v -> changeNumberOfCount(cartItem.getMerchantId(), getAdapterPosition(), -1));
        }
    }

    public interface CartClickListener {

        void addItemToList(int merchantId, int position);

        void deleteFromCart(int merchantId, int position);

        void changeNumberOfCount(int merchantId, int position, int quantity);
    }
}
