package com.limit.lazybird.util

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.limit.lazybird.R

fun FragmentManager.replaceChildFragment(
    fragment: Fragment                 // 변경할 Fragment
) {
    // fragmentContainer 를 다른 fragment 로 변경하기
    beginTransaction()
        .replace(R.id.fragment_child_container_view, fragment)
        .commit()
}