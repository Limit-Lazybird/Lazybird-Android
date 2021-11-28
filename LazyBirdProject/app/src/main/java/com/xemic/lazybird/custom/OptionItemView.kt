package com.xemic.lazybird.custom

import android.content.Context
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.xemic.lazybird.R

/****************** OptionItemView *********************
 * Filtering 할 때 사용하는 하나의 item view
 * isSelected 가 true인지, false인지에 따라 view의 모양이 변경된다.
 * padding값, 기본 design, 클릭 시 변경되는 design은 모두 고정되어있다.
 * Todo : 시간있으면 기본 design, 클릭 시 변경되는 design 모두 변경가능하게 업데이트하기
 ********************************************** ***/

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
        setPadding(30, 15, 30, 10) // px 단위로 여백 padding 설정
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

        isSelectedLiveData.observe(viewLifecycleOwner) { is_selected ->
            if (is_selected) {
                // view 가 선택 되었을 때 
                setTextColor(resources.getColor(R.color.gray_400, null))
                background = resources.getDrawable(R.drawable.option_item_rounded, null)
                backgroundTintList = resources.getColorStateList(R.color.white, null)
            } else {
                // view 가 선택 되지 않았을 때
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