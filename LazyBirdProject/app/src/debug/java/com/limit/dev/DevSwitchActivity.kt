package com.limit.dev

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.limit.lazybird.databinding.ActivityDevSwitchBinding

class DevSwitchActivity : AppCompatActivity() {

    private val IS_DEV_MODE = "is_dev_mode"
    private val preferences by lazy { getSharedPreferences("dev_mode", MODE_PRIVATE) }
    lateinit var binding: ActivityDevSwitchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDevSwitchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.devSwitch.apply {
            isChecked = preferences.getBoolean(IS_DEV_MODE, false)
            setOnClickListener {
                preferences.edit { putBoolean(IS_DEV_MODE, isChecked) }
            }
        }
    }
}