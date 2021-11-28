package com.xemic.lazybird.custom

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.xemic.lazybird.R
import com.xemic.lazybird.databinding.CustomOnbSelectBoxBinding

/****************** OnbSelectBox *********************
 * Onboarding 화면에서 사용하는 Custom Select Box
 * 이미지와 답변문구로 이루어져있다.
 ********************************************** ***/
class OnbSelectBox(context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs) {
    companion object{
        private lateinit var binding: CustomOnbSelectBoxBinding
    }

    init {
        val view = inflate(context, R.layout.custom_onb_select_box, this)
        binding = CustomOnbSelectBoxBinding.bind(view)
    }

    fun setImage(url: String) {
        // 이미지 삽입
        Glide.with(this)
            .load(url)
            .centerCrop()
            .into(binding.customOnbImage)
    }

    fun setAnswer(answer: String) {
        // 답변문구 삽입
        binding.customOnbAnswer.text = answer
    }
}