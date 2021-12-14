package com.limit.lazybird.custom

import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import com.limit.lazybird.R
import com.limit.lazybird.databinding.CustomSnackbarBinding

/********** CustomSnackbar **********
 * GetEarlycardFragmewnt 에서 쓰기 위한 custom 된 snackbar
 * Code Ref : https://black-jin0427.tistory.com/294
 ********************************************** ***/
class CustomSnackBar(
    view:View,
    private val message:String,
    private val actionName:String
) {
    interface OnClickListener {
        fun onClick(view: View)
    }

    companion object {
        fun make(view:View, message: String, actionName: String) = CustomSnackBar(view, message, actionName)
    }

    private val context = view.context
    private val snackbar = Snackbar.make(view, "", 10000)
    private val snackbarLayout = snackbar.view as Snackbar.SnackbarLayout

    private val inflater = LayoutInflater.from(context)
    private val snackbarBinding: CustomSnackbarBinding = DataBindingUtil.inflate(inflater, R.layout.custom_snackbar, null, false)

    var clickListener: OnClickListener? = null

    init {
        initView()
        initData()
    }

    private fun initView() {
        with(snackbarLayout){
            removeAllViews()
            setPadding(0,0,0,0)
            setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent))
            addView(snackbarBinding.root, 0)
        }
    }

    private fun initData() {
        snackbarBinding.snackbarMainText.text = message
        snackbarBinding.snackbarActionText.text = actionName
        snackbarBinding.snackbarContainer.setOnClickListener {
            clickListener?.onClick(it)
        }
    }

    fun show() {
        snackbar.show()
    }

    fun dismiss() {
        snackbar.dismiss()
    }
}