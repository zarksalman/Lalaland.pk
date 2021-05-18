package com.lalaland.ecommerce.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.lalaland.ecommerce.data.models.productDetails.ProductMultimedium
import com.lalaland.ecommerce.helpers.AppConstants
import com.lalaland.ecommerce.views.fragments.ImageFragment


class ImageViewPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private val imageFragmentList = ArrayList<ImageFragment>()

    override fun getItemPosition(`object`: Any): Int {
        return super.getItemPosition(`object`)
    }

    override fun getItem(position: Int): Fragment {
        return imageFragmentList[position]
    }

    override fun getCount(): Int {
        return imageFragmentList.size
    }

    fun addAll(urlList: List<ProductMultimedium>, description: String?) {
        imageFragmentList.clear()
        urlList.forEach {
            ImageFragment().apply {
                imageUrl = AppConstants.PRODUCT_STORAGE_BASE_URL + it.source
                imageDescription = description
                imageFragmentList.add(this)
            }
        }
        notifyDataSetChanged()
    }
}