package com.lalaland.ecommerce.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.data.models.actionProducs.ActionProducts;
import com.lalaland.ecommerce.data.models.home.Recommendation;
import com.lalaland.ecommerce.databinding.ActionProductsItemBinding;
import com.lalaland.ecommerce.databinding.RecommendationItemBinding;

import java.util.List;


public class ActionProductsAdapter extends RecyclerView.Adapter<ActionProductsAdapter.ActionProductsViewHolder> {

    private Context mContext;
    private List<ActionProducts> mActionProducts;
    private ActionProductsItemBinding actionProductsItemBinding;
    protected ActionProductsListener mActionProductListener;
    private LayoutInflater inflater;

    public ActionProductsAdapter(Context context, ActionProductsListener actionProductListener) {
        mContext = context;
        mActionProductListener = actionProductListener;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ActionProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        actionProductsItemBinding = DataBindingUtil.inflate(inflater, R.layout.action_products_item, parent, false);
        return new ActionProductsViewHolder(actionProductsItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ActionProductsViewHolder holder, int position) {
        ActionProducts actionProducts = mActionProducts.get(position);
        holder.bindHolder(actionProducts);
    }

    @Override
    public int getItemCount() {
        return mActionProducts.size();
    }

    public void setData(List<ActionProducts> actionProducts) {

        mActionProducts = actionProducts;
        notifyDataSetChanged();
    }

    public void onActionProductClicked(View view, ActionProducts actionProducts) {
        mActionProductListener.onActionProductClicked(actionProducts);
    }

    class ActionProductsViewHolder extends RecyclerView.ViewHolder {

        ActionProductsItemBinding mActionProductsItemBinding;

        ActionProductsViewHolder(@NonNull ActionProductsItemBinding actionProductsItemBinding) {
            super(actionProductsItemBinding.getRoot());

            mActionProductsItemBinding = actionProductsItemBinding; // do this otherwise some items of adapter will change its content
        }

        void bindHolder(ActionProducts actionProducts) {

            mActionProductsItemBinding.setActionProduct(actionProducts);
            mActionProductsItemBinding.setAdapter(ActionProductsAdapter.this);
            mActionProductsItemBinding.executePendingBindings();
        }
    }

    public interface ActionProductsListener {
        void onActionProductClicked(ActionProducts actionProducts);
    }
}
