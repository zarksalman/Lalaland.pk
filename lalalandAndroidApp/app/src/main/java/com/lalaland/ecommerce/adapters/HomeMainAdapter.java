package com.lalaland.ecommerce.adapters;

/*
 * Created by SalmanHameed on 10/10/2019.
 */

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.lalaland.ecommerce.R;
import com.lalaland.ecommerce.data.models.home.Actions;
import com.lalaland.ecommerce.data.models.home.HomeData;
import com.lalaland.ecommerce.data.models.home.HomeDataContainer;
import com.lalaland.ecommerce.data.models.home.PicksOfTheWeek;
import com.lalaland.ecommerce.databinding.ActionItemContainerItemBinding;
import com.lalaland.ecommerce.databinding.ActionLayoutBinding;
import com.lalaland.ecommerce.databinding.BannerItemBinding;
import com.lalaland.ecommerce.databinding.PickOfTheWeekItemContainerItemBinding;
import com.lalaland.ecommerce.databinding.PickOfWeekItemBinding;
import com.lalaland.ecommerce.views.activities.ActionProductListingActivity;
import com.lalaland.ecommerce.views.activities.ProductDetailActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.lalaland.ecommerce.helpers.AppConstants.ACTION_ID;
import static com.lalaland.ecommerce.helpers.AppConstants.ACTION_NAME;
import static com.lalaland.ecommerce.helpers.AppConstants.PICK_OF_THE_WEEK_PRODUCTS;
import static com.lalaland.ecommerce.helpers.AppConstants.PRODUCT_ID;
import static com.lalaland.ecommerce.helpers.AppConstants.PRODUCT_TYPE;

public class HomeMainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private static final int LAYOUT_ONE = 0;
    private static final int LAYOUT_TWO = 1;
    private LayoutInflater inflater;
    private HomeDataContainer mHomeDataContainer;
    private ActionItemContainerItemBinding actionItemContainerItemBinding;
    private HomeData homeData;

    public HomeMainAdapter(Context context, HomeDataContainer homeDataContainer) {
        mContext = context;
        inflater = LayoutInflater.from(context);
        mHomeDataContainer = homeDataContainer;
        homeData = mHomeDataContainer.getHomeData();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == 0) {

            BannerItemBinding bannerItemBinding = DataBindingUtil.inflate(inflater, R.layout.banner_item, parent, false);
            return new BannerViewHolder(bannerItemBinding);

        } else if (viewType == 1) {
            ActionItemContainerItemBinding actionItemContainerItemBinding = DataBindingUtil.inflate(inflater, R.layout.action_item_container_item, parent, false);
            return new ActionViewHolder(actionItemContainerItemBinding);
        } else if (viewType == 2) {
            PickOfTheWeekItemContainerItemBinding pickOfTheWeekItemContainerItemBinding = DataBindingUtil.inflate(inflater, R.layout.pick_of_the_week_item_container_item, parent, false);
            return new PickViewHolder(pickOfTheWeekItemContainerItemBinding);
        }

        PickOfTheWeekItemContainerItemBinding pickOfTheWeekItemContainerItemBinding = DataBindingUtil.inflate(inflater, R.layout.pick_of_the_week_item_container_item, parent, false);
        actionItemContainerItemBinding.getRoot().setVisibility(View.GONE);
        return new PickViewHolder(pickOfTheWeekItemContainerItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }


    @Override
    public int getItemCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {

        switch (position) {

            case 0:
                return 0;

            case 1:
                return 1;

            case 2:
                return 2;

            default:
                return 2;
        }

    }

    class BannerViewHolder extends RecyclerView.ViewHolder {

        private BannerItemBinding mView;
        private int currentPage = 0;
        private List<ImageView> dots = new ArrayList<>();

        private BannerViewHolder(@NonNull BannerItemBinding itemView) {
            super(itemView.getRoot());
            mView = itemView;
            setBannerSlider();
        }

        void setBannerSlider() {

            if (mView.dots.getChildCount() > 0)
                mView.dots.removeAllViews();

            BannerImageAdapter bannerImageAdapter = new BannerImageAdapter(mContext, homeData.getHomeBanners());
            mView.vpImages.setAdapter(bannerImageAdapter);


            //setting viewpagger height because in scrollview wrap/match does not calculate their height correctly
            android.view.Display display = ((android.view.WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            mView.vpImages.getLayoutParams().height = ((int) (display.getWidth() * 0.6));
            mView.vpImages.getLayoutParams().width = ((int) (display.getWidth() * 1.0));

            currentPage = 0;
            addDots();
            setupAutoPager();

        }

        private void addDots() {

            dots.clear();

            if (homeData.getHomeBanners().size() < 2) {
                mView.dots.setVisibility(View.GONE);
                return;
            }

            for (int i = 0; i <= homeData.getHomeBanners().size() - 1; i++) {
                ImageView dot = new ImageView(mContext);
                dot.setImageDrawable(mContext.getResources().getDrawable(R.drawable.empty_circle));

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );


                mView.dots.addView(dot, params);

                dot.setTag(R.string.banner_circle_tag, i);

                dot.setOnClickListener(v -> {

                    onBannerCircleClicked(Integer.parseInt(v.getTag(R.string.banner_circle_tag).toString()));
                });

                dots.add(dot);
            }

            selectDot(0);

            mView.vpImages.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {

                    currentPage = position;
                    selectDot(position);
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            });
        }

        private void onBannerCircleClicked(int parseInt) {
            currentPage = parseInt;
            mView.vpImages.setCurrentItem(parseInt);
        }

        private void selectDot(int idx) {

            Resources res = mContext.getResources();
            for (int i = 0; i <= homeData.getHomeBanners().size() - 1; i++) {
                int drawableId = (i == idx) ? (R.drawable.filled_circle) : (R.drawable.empty_circle);
                Drawable drawable = res.getDrawable(drawableId);
                dots.get(i).setImageDrawable(drawable);
            }
        }

        private void setupAutoPager() {
            final Handler handler = new Handler();

            final Runnable update = () -> {

                mView.vpImages.setCurrentItem(currentPage, true);
                if (currentPage == homeData.getHomeBanners().size()) {
                    currentPage = 0;
                } else {

                    ++currentPage;
                }
            };


            Timer timer = new Timer();
            timer.schedule(new TimerTask() {

                @Override
                public void run() {
                    handler.post(update);
                }
            }, 1000, 3500);
        }

    }

    class ActionViewHolder extends RecyclerView.ViewHolder {

        private ActionItemContainerItemBinding mActionItemContainerItemBinding;

        private ActionViewHolder(@NonNull ActionItemContainerItemBinding itemView) {
            super(itemView.getRoot());
            mActionItemContainerItemBinding = itemView;
            setActions();
        }

        //******************************************* Action starts here **************************************************

        /*
         *   Action adapter code is also available
         *   to stop scrolling that's why this is implemented
         */

        private void setActions() {

            if (mActionItemContainerItemBinding.rvActionContainer.getChildCount() > 0)
                mActionItemContainerItemBinding.rvActionContainer.removeAllViews();

            Integer weight = (100 / homeData.getActions().size());

            for (Integer i = 0; i < homeData.getActions().size(); i++) {

                ActionLayoutBinding layout = DataBindingUtil.inflate(inflater, R.layout.action_layout, null, false);
                layout.ivAction.setTag(R.string.action_tag, i);

                if (layout.actionParent.getParent() != null)
                    layout.actionParent.removeView(layout.actionParent);

                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        weight
                );

                layout.setAction(homeData.getActions().get(i));

                layout.actionParent.setClickable(true);
                layout.ivAction.setOnClickListener(v -> onActionClicked(Integer.parseInt(v.getTag(R.string.action_tag).toString())));

                mActionItemContainerItemBinding.rvActionContainer.addView(layout.getRoot(), param);


            }
        }

        private void onActionClicked(Integer index) {

            Actions actions = homeData.getActions().get(index);
            Intent intent = new Intent(mContext, ActionProductListingActivity.class);

            intent.putExtra(ACTION_NAME, actions.getName());
            intent.putExtra(ACTION_ID, String.valueOf(actions.getActionId()));
            intent.putExtra(PRODUCT_TYPE, actions.getActionName());
            mContext.startActivity(intent);
        }

        //******************************************* Action starts here **************************************************

    }

    class PickViewHolder extends RecyclerView.ViewHolder {

        private PickOfTheWeekItemContainerItemBinding mPickOfTheWeekItemContainerItemBinding;

        private PickViewHolder(@NonNull PickOfTheWeekItemContainerItemBinding itemView) {
            super(itemView.getRoot());
            mPickOfTheWeekItemContainerItemBinding = itemView;
            setPickOfTheWeek();
        }

        //******************************************* Pick of the wee starts here **************************************************

        /*
         *   Action adapter code is also available
         *   to stop scrolling that's why this is implemented
         */

        private void setPickOfTheWeek() {

            if (mPickOfTheWeekItemContainerItemBinding.pickOfWeekContainerParent.rvPicksOfWeekContainer.getChildCount() > 0)
                mPickOfTheWeekItemContainerItemBinding.pickOfWeekContainerParent.rvPicksOfWeekContainer.removeAllViews();

            Integer weight = (90 / 3);

            for (int i = 0; i < 3; i++) {

                PickOfWeekItemBinding layout = DataBindingUtil.inflate(inflater, R.layout.pick_of_week_item, null, false);
                layout.ivAction.setTag(R.string.pick_of_week_tag, i);

                if (layout.actionParent.getParent() != null)
                    layout.actionParent.removeView(layout.actionParent);

                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        weight
                );

                try {

                    layout.setPicks(homeData.getPicksOfTheWeek().get(i));
                    layout.ivAction.setOnClickListener(v -> onWeekProductClicked(Integer.parseInt(v.getTag(R.string.pick_of_week_tag).toString())));
                    mPickOfTheWeekItemContainerItemBinding.pickOfWeekContainerParent.rvPicksOfWeekContainer.addView(layout.getRoot(), param);
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }

            }
        }

        public void showAllProductsWeekProducts() {

            Intent intent = new Intent(mContext, ActionProductListingActivity.class);
            intent.putExtra(ACTION_NAME, mContext.getResources().getString(R.string.picks_of_the_week));
            intent.putExtra(PRODUCT_TYPE, PICK_OF_THE_WEEK_PRODUCTS);
            mContext.startActivity(intent);
        }

        public void onWeekProductClicked(Integer index) {

            PicksOfTheWeek picksOfTheWeek = homeData.getPicksOfTheWeek().get(index);
            Intent intent = new Intent(mContext, ProductDetailActivity.class);
            intent.putExtra(PRODUCT_ID, picksOfTheWeek.getId());
            mContext.startActivity(intent);
        }

        //******************************************* pick of the week ends here **************************************************

    }
}
