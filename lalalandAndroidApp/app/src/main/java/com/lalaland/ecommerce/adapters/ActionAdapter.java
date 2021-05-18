package com.lalaland.ecommerce.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.data.models.home.Actions;
import com.lalaland.ecommerce.databinding.ActionLayoutBinding;

import java.util.List;


public class ActionAdapter extends RecyclerView.Adapter<ActionAdapter.ActionViewHolder> {

    private Context mContext;
    private List<Actions> mActions;
    private ActionLayoutBinding actionLayoutBinding;
    private LayoutInflater inflater;
    private ActionClickListener mActionClickListener;

    public ActionAdapter(Context context, ActionClickListener actionClickListener) {
        mContext = context;
        mActionClickListener = actionClickListener;
        inflater = LayoutInflater.from(context);

    }

    @NonNull
    @Override
    public ActionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        actionLayoutBinding = DataBindingUtil.inflate(inflater, R.layout.action_layout, parent, false);
        return new ActionViewHolder(actionLayoutBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ActionViewHolder holder, int position) {
        Actions action = mActions.get(position);
        holder.bindHolder(action);
    }

    @Override
    public int getItemCount() {
        return mActions.size();
    }

    public void actionClicked(View view, Actions actions) {
        mActionClickListener.onActionClicked(actions);
    }

    public void setData(List<Actions> actionsList) {

        mActions = actionsList;
        notifyDataSetChanged();
    }

    class ActionViewHolder extends RecyclerView.ViewHolder {

        ActionLayoutBinding mActionLayoutBinding;

        ActionViewHolder(@NonNull ActionLayoutBinding actionLayoutBinding) {
            super(actionLayoutBinding.getRoot());
            mActionLayoutBinding = actionLayoutBinding;
        }

        void bindHolder(Actions actions) {
            mActionLayoutBinding.setAction(actions);
            mActionLayoutBinding.setAdapter(ActionAdapter.this);
            mActionLayoutBinding.executePendingBindings();
        }
    }

    public interface ActionClickListener {
        void onActionClicked(Actions actions);
    }
}
