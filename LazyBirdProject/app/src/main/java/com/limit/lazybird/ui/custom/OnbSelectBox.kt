package com.limit.lazybird.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.limit.lazybird.R
import com.limit.lazybird.databinding.CustomOnbSelectBoxBinding

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

    // 이미지 삽입
    fun setImage(url: String) {
        Glide.with(this)
            .load(url)
            .centerCrop()
            .into(binding.customOnbImage)
    }

    // 답변문구 삽입
    fun setAnswer(answer: String) {
        binding.customOnbAnswer.text = answer
    }
}