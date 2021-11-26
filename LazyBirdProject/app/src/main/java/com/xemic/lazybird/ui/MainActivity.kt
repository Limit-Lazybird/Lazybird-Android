package com.xemic.lazybird.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.xemic.lazybird.databinding.ActivityMainBinding
import com.xemic.lazybird.ui.splashlogin.SplashFragment
import com.xemic.lazybird.util.calculateDateDiff
import com.xemic.lazybird.util.replaceFragment
import com.xemic.lazybird.util.toDate
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

// TOdo : ===== 수정해야하는 것들 =====
// TOdo : 상세페이지 더보기 누르기 전의 이미지가 조금 이상한데? 한번 체크해보기
// TOdo : 로딩 안되었을 때 뺑글뺑글 돌아가는거 흰색/검은색/회색 계열이면 ok

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.replaceFragment(
            SplashFragment(),
            false
        ) // 첫 fragment 화면은 splashFragment 으로 설정
    }
}