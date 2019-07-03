package com.lalaland.ecommerce.customDialogue;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.adapters.CartItemsAdapter;
import com.lalaland.ecommerce.databinding.DeleteOutOfStockDialogueBinding;

public class CustomListViewDialog extends Dialog implements View.OnClickListener {


    public CustomListViewDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public CustomListViewDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


    public Activity activity;
    public Dialog dialog;
    public Button yes, no;
    TextView title;
    RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    CartItemsAdapter adapter;

    DeleteOutOfStockDialogueBinding deleteOutOfStockDialogueBinding;


    public CustomListViewDialog(Activity activity, CartItemsAdapter adapter) {
        super(activity);
        this.activity = activity;
        this.adapter = adapter;
    }

    private void setItems() {

        deleteOutOfStockDialogueBinding.recyclerView.setAdapter(adapter);
        deleteOutOfStockDialogueBinding.recyclerView.setHasFixedSize(true);
        deleteOutOfStockDialogueBinding.recyclerView.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.VERTICAL, false));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        deleteOutOfStockDialogueBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.delete_out_of_stock_dialogue, null, false);

        mLayoutManager = new LinearLayoutManager(activity);
        deleteOutOfStockDialogueBinding.recyclerView.setLayoutManager(mLayoutManager);


        deleteOutOfStockDialogueBinding.recyclerView.setAdapter(adapter);
        deleteOutOfStockDialogueBinding.btnDeleteItem.setOnClickListener(this);
        deleteOutOfStockDialogueBinding.btnCancelDeleteItem.setOnClickListener(this);

        setItems();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_delete_item:

                break;
            case R.id.btn_cancel_delete_item:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }
}