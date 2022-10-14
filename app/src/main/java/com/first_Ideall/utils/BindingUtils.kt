package com.first_Ideall.utils

import androidx.databinding.BindingAdapter
import com.facebook.drawee.view.SimpleDraweeView

class BindingUtils {

    @BindingAdapter("image")
    fun loadImage(view: SimpleDraweeView, url: String) {
        view.setImageURI(url)
    }
}