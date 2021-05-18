package com.lalaland.ecommerce.views.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.lalaland.ecommerce.R
import com.lalaland.ecommerce.databinding.FragmentImageBinding
import com.lalaland.ecommerce.views.activities.ZoomInZoomOutActivity


class ImageFragment : Fragment() {

    var imageUrl: String? = null
    var imageDescription: String? = null
    private lateinit var binding: FragmentImageBinding

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_image, container, false);
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.imageView.setOnClickListener {
            Intent(requireContext(), ZoomInZoomOutActivity::class.java).apply {
                putExtra("size_chart", imageUrl)
                putExtra("title", "Zoom in/out")
                startActivity(this)
            }
        }

        Glide.with(this)
                .load(imageUrl)
                .error(R.drawable.placeholder_products)
                .placeholder(R.drawable.placeholder_products)
                .into(binding.imageView)

        imageDescription?.let {
            binding.tvImageDetail.apply {
                text = it
                visibility = View.VISIBLE
            }
        }
    }
}