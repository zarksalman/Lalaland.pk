package com.lalaland.ecommerce.adapters;

public class SliderAdapterExample {/*extends SliderViewAdapter<SliderAdapterExample.SliderAdapterVH> {

    private Context context;
    private List<HomeBanner> img;

    public SliderAdapterExample(Context context, List<HomeBanner> img) {
        this.context = context;
        this.img = img;
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_item, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, int position) {

        String bannerImageUrl = BANNER_STORAGE_BASE_URL.concat(this.img.get(position).getBannerImage());
        Log.d("banner", bannerImageUrl);
        Glide.with(viewHolder.itemView)
                .load(bannerImageUrl)
                .placeholder(R.drawable.placeholder_products)
                .error(R.drawable.placeholder_products)
                .into(viewHolder.imageViewBackground);
    }

    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return img.size();
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        View itemView;
        ImageView imageViewBackground;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.iv_slider);

            this.itemView = itemView;
        }
    }*/
}