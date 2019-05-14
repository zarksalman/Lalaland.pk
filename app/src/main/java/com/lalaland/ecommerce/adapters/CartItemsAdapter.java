package com.lalaland.ecommerce.adapters;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.data.models.cart.CartItem;
import com.lalaland.ecommerce.databinding.CartItemBinding;

import java.util.ArrayList;
import java.util.List;

import static com.lalaland.ecommerce.helpers.AppConstants.ITEM_SOLD_OUT;


public class CartItemsAdapter extends RecyclerView.Adapter<CartItemsAdapter.CartItemViewHolder> {

    private Context mContext;
    private List<CartItem> mCartItems;
    private CartItemBinding cartItemBinding;
    private LayoutInflater inflater;
    private CartClickListener mCartClickListener;
    private List<Integer> countList = new ArrayList<>();
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
        }

        return new CartItemViewHolder(cartItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemViewHolder holder, int position) {

        if (position != RecyclerView.NO_POSITION) {
            CartItem cartItem = mCartItems.get(position);
            holder.bindHolder(cartItem);

        }
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

    public void changeNumberOfCount(View view, CartItem cartItem, int value) {


        int quantity = cartItem.getItemQuantity();

        if (value > 0) {
            if (Integer.parseInt(cartItem.getRemainingQuantity()) > cartItem.getItemQuantity()) {
                quantity += 1;
                mCartClickListener.changeNumberOfCount(cartItem, quantity);
            } else
                Toast.makeText(mContext, ITEM_SOLD_OUT, Toast.LENGTH_SHORT).show();
        } else {
            quantity -= 1;
            mCartClickListener.changeNumberOfCount(cartItem, quantity);
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

            mCartItemBinding.cbAddToList.setChecked(itemStateArray.get(getAdapterPosition(), false));

            mCartItemBinding.ivDeleteItem.setOnClickListener(v -> {
                mCartClickListener.deleteFromCart(cartItem);
            });

            mCartItemBinding.cbAddToList.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    itemStateArray.put(getAdapterPosition(), isChecked);
                    mCartClickListener.addItemToList(cartItem);
                }
            });
        }
    }

    public interface CartClickListener {

        void addItemToList(CartItem cartItem);

        void deleteFromCart(CartItem cartItem);

        void changeNumberOfCount(CartItem cartItem, int quantity);
    }
}
