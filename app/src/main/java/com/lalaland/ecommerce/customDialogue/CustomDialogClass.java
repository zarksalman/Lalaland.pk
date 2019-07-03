package com.lalaland.ecommerce.customDialogue;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.adapters.CartItemsAdapter;
import com.lalaland.ecommerce.data.models.cart.CartItem;
import com.lalaland.ecommerce.databinding.DeleteOutOfStockDialogueBinding;

import java.util.List;

public class CustomDialogClass extends Dialog {

    public Activity mActivity;
    List<CartItem> outOfStockItems;
    CartItemsAdapter outOfStockAdapter;
    DeleteOutOfStockDialogueBinding deleteOutOfStockDialogueBinding;

    public CustomDialogClass(Activity activity, List<CartItem> cartItemList) {
        super(activity);
        mActivity = activity;
        outOfStockItems = cartItemList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        deleteOutOfStockDialogueBinding = DataBindingUtil.setContentView(mActivity, R.layout.delete_out_of_stock_dialogue);

        deleteOutOfStockDialogueBinding.btnDeleteItem.setOnClickListener(v -> {
            Toast.makeText(mActivity, "Deleting Items", Toast.LENGTH_SHORT).show();
        });
        deleteOutOfStockDialogueBinding.btnCancelDeleteItem.setOnClickListener(v -> {
            Toast.makeText(mActivity, "Cancel Deleting Items", Toast.LENGTH_SHORT).show();
        });

        setItems();
    }

    private void setItems() {

        outOfStockAdapter = new CartItemsAdapter(mActivity, new CartItemsAdapter.CartClickListener() {
            @Override
            public void addItemToList(int merchantId, int position) {

            }

            @Override
            public void deleteFromCart(int merchantId, int position) {

            }

            @Override
            public void changeNumberOfCount(int merchantId, int position, int quantity) {

            }
        }, 2);

        deleteOutOfStockDialogueBinding.recyclerView.setAdapter(outOfStockAdapter);
        deleteOutOfStockDialogueBinding.recyclerView.setHasFixedSize(true);
        deleteOutOfStockDialogueBinding.recyclerView.setLayoutManager(new LinearLayoutManager(mActivity, RecyclerView.VERTICAL, false));
        outOfStockAdapter.setData(outOfStockItems);
    }
}