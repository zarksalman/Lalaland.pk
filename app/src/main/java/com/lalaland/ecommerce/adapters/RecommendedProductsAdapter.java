package com.lalaland.ecommerce.adapters;

public class RecommendedProductsAdapter {
}
/*
public class RecommendedProductsAdapter extends RecyclerView.Adapter<RecommendedProductsAdapter.ProductViewHolder> {

    private Context mContext;
    private List<Product> mProductList;
  //  private RecietRecommendationItemBinding productItemBinding;
    private LayoutInflater inflater;
    private ProductListener mProductListener;

    public RecommendedProductsAdapter(Context context, ProductListener productListener) {
        mContext = context;
        inflater = LayoutInflater.from(context);
        mProductListener = productListener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

      *//*  productItemBinding = DataBindingUtil.inflate(inflater, R.layout.product_item, parent, false);
        return new ProductViewHolder(productItemBinding);*//*
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = mProductList.get(position);
        holder.bindHolder(product);
    }

    @Override
    public int getItemCount() {

        if (mProductList != null)
            return mProductList.size();

        return 0;
    }

    public void setData(List<Product> productList) {

        mProductList = productList;
        notifyDataSetChanged();
    }

    public void onProductClicked(View view, Product product) {
        mProductListener.onProductProductClicked(product);
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {

        RecietRecommendationItemBinding mProductItemBinding;

        ProductViewHolder(@NonNull RecietRecommendationItemBinding productItemBinding) {
            super(productItemBinding.getRoot());

            mProductItemBinding = productItemBinding;
        }

        void bindHolder(Product product) {

            mProductItemBinding.tvProductActualPrice.setPaintFlags(mProductItemBinding.tvProductActualPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);  // making price for sales

            mProductItemBinding.setProduct(product);
            mProductItemBinding.setAdapter(RecommendedProductsAdapter.this);
            mProductItemBinding.executePendingBindings();
        }
    }

    public interface ProductListener {
        void onProductProductClicked(Product product);
    }
}
*/