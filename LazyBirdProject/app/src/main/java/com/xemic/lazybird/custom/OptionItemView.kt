package com.xemic.lazybird.custom

import android.app.ActionBar
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.marginRight
import androidx.core.view.setPadding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.xemic.lazybird.R

class OptionItemView(
    context: Context,
    viewLifecycleOwner: LifecycleOwner,
    optionName: String,
    isSelected: Boolean = false
) : AppCompatTextView(context) {
    private val isSelectedLiveData: MutableLiveData<Boolean> by lazy {
        MutableLiveData(isSelected)
    }

    init {
        setPadding(30, 15, 30, 10) // px 단위
        val layoutParams = ViewGroup.MarginLayoutParams(
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        ).apply {
            rightMargin = 25
            bottomMargin = 25
        }
        setLayoutParams(layoutParams)

        // is_selected 가 변화할 때
        isSelectedLiveData.observe(viewLifecycleOwner) { is_selected ->
            if (is_selected) {
                setTextColor(resources.getColor(R.color.gray_400, null))
                background = resources.getDrawable(R.drawable.option_item_rounded, null)
                backgroundTintList = resources.getColorStateList(R.color.white, null)
            } else {
                setTextColor(resources.getColor(R.color.gray_1000, null))
                background = resources.getDrawable(R.drawable.option_item_rounded, null)
                backgroundTintList = resources.getColorStateList(R.color.gray_200, null)
            }
        }

        text = optionName
    }

    override fun isSelected(): Boolean = isSelectedLiveData.value!!

    override fun setSelected(selected: Boolean) {
        isSelectedLiveData.value = selected
    }
}