package com.lalaland.ecommerce.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.lalaland.ecommerce.R
import com.lalaland.ecommerce.data.models.productDetails.ProductMultimedium
import com.lalaland.ecommerce.databinding.ProductImageItemBinding
import com.lalaland.ecommerce.helpers.AppConstants
import com.squareup.picasso.Picasso

class ProductImageAdapter(var context: Context, private val productMultimedia: List<ProductMultimedium>, private val mediaDescription: String?) : PagerAdapter() {
    private lateinit var productImageItemBinding: ProductImageItemBinding
    var layoutInflater: LayoutInflater? = null
    override fun getCount(): Int {
        return productMultimedia.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        productImageItemBinding = DataBindingUtil.inflate(layoutInflater!!, R.layout.product_image_item, container, false)
        val imgUrl = AppConstants.PRODUCT_STORAGE_BASE_URL + productMultimedia[position].source
        Picasso
                .get()
                .load(imgUrl)
                .placeholder(R.drawable.placeholder_products)
                .into(productImageItemBinding.ivProduct)
        productImageItemBinding.tvImageDetail.text = mediaDescription
        if (mediaDescription == null || mediaDescription.isEmpty()) productImageItemBinding.tvImageDetail.visibility = View.GONE else productImageItemBinding.tvImageDetail.visibility = View.VISIBLE
        container.addView(productImageItemBinding.root)
        return productImageItemBinding.root
    }

    override fun destroyItem(container: View, position: Int, `object`: Any) {
        (container as ViewPager).removeView(`object` as View)
    }
}