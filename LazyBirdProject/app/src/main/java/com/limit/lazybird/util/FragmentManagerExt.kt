package com.limit.lazybird.util

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.limit.lazybird.R

fun FragmentManager.replaceFragment(
    fragment: Fragment,                 // 변경할 Fragment
    is_add_backstack: Boolean = true    // backstack 을 추가 할 지
) {
    // fragmentContainer 를 다른 fragment 로 변경하기
    beginTransaction()
        .replace(R.id.fragment_container_view, fragment)
        .apply {
            if (is_add_backstack)
                addToBackStack(null)
        }
        .commit()
}

fun FragmentManager.replaceChildFragment(
    fragment: Fragment                 // 변경할 Fragment
) {
    // fragmentContainer 를 다른 fragment 로 변경하기
    beginTransaction()
        .replace(R.id.fragment_child_container_view, fragment)
        .commit()
}

fun FragmentManager.removeAllBackStack(){
    // 모든 backStack 제거하기
    repeat(backStackEntryCount){
        popBackStack()
    }
}