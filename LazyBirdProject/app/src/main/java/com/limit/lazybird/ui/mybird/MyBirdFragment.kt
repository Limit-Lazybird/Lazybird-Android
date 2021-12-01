package com.limit.lazybird.ui.mybird

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.limit.lazybird.R
import com.limit.lazybird.databinding.FragmentMybirdBinding
import com.limit.lazybird.ui.MainActivity
import com.limit.lazybird.ui.setting.SettingFragment
import com.limit.lazybird.util.*
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

/************* MyBirdFragment ***************
 * 메인화면(마이버드 탭) (Fragment)
 * 마이버드 화면 (내 정보 보기)
 ********************************************** ***/
@AndroidEntryPoint
class MyBirdFragment : Fragment(R.layout.fragment_mybird) {

    companion object {
        const val TAG = "MyBirdFragment"
    }

    lateinit var binding: FragmentMybirdBinding
    private val viewModel: MyBirdViewModel by viewModels()
    private val parentActivity: MainActivity by lazy {
        activity as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentMybirdBinding.bind(view)

        viewModel.userInfo.observe(viewLifecycleOwner) { userInfo ->
            binding.mybirdEmail.text = userInfo.email
            binding.mybirdName.text = userInfo.name
        }

        viewModel.likeExhbtList.observe(viewLifecycleOwner) { exhbtList ->
            if (exhbtList.isNullOrEmpty()){
                binding.mybirdNoLikeText.visibility = View.VISIBLE
            } else {
                binding.mybirdNoLikeText.visibility = View.INVISIBLE
            }

            if (!exhbtList.isNullOrEmpty() && exhbtList.size >= 1) {
                binding.mybirdLike1Title.visibility = View.VISIBLE
                binding.mybirdLike1Date.visibility = View.VISIBLE
                binding.mybirdLike1Image.visibility = View.VISIBLE

                val exhbt = exhbtList[0]
                binding.mybirdLike1Title.text = exhbt.exhbt_nm
                binding.mybirdLike1Date.text =
                    "${exhbt.exhbt_from_dt.dateFormatted()} ~ ${exhbt.exhbt_to_dt.dateFormatted()}"
                Glide.with(view)
                    .load(exhbt.exhbt_sn)
                    .apply(
                        RequestOptions().transform(
                            CenterCrop(),
                            RoundedCorners(
                                15f.toDp(view)
                            )
                        )
                    )
                    .into(binding.mybirdLike1Image)
            }
            
            if (!exhbtList.isNullOrEmpty() && exhbtList.size >= 2) {
                binding.mybirdLike2Title.visibility = View.VISIBLE
                binding.mybirdLike2Date.visibility = View.VISIBLE
                binding.mybirdLike2Image.visibility = View.VISIBLE

                val exhbt = exhbtList[1]
                binding.mybirdLike2Title.text = exhbt.exhbt_nm
                binding.mybirdLike2Date.text =
                    "${exhbt.exhbt_from_dt.dateFormatted()} ~ ${exhbt.exhbt_to_dt.dateFormatted()}"
                Glide.with(view)
                    .load(exhbt.exhbt_sn)
                    .apply(
                        RequestOptions().transform(
                            CenterCrop(),
                            RoundedCorners(
                                15f.toDp(view)
                            )
                        )
                    )
                    .into(binding.mybirdLike2Image)
            }
        }
        
        viewModel.reservationExhbtList.observe(viewLifecycleOwner) { exhbtList ->
            if (exhbtList.isNullOrEmpty()){
                binding.mybirdNoReservationText.visibility = View.VISIBLE
            } else {
                binding.mybirdNoReservationText.visibility = View.INVISIBLE
            }

            if (!exhbtList.isNullOrEmpty() && exhbtList.size >= 1) {
                binding.mybirdReservation1Title.visibility = View.VISIBLE
                binding.mybirdReservation1Date.visibility = View.VISIBLE
                binding.mybirdReservation1Dday.visibility = View.VISIBLE
                binding.mybirdReservation1Image.visibility = View.VISIBLE

                val exhbt = exhbtList[0]
                binding.mybirdReservation1Title.text = exhbt.exhbt_nm
                binding.mybirdReservation1Date.text =
                    "${exhbt.exhbt_from_dt.dateFormatted()} ~ ${exhbt.exhbt_to_dt.dateFormatted()}"
                binding.mybirdReservation1Dday.text =
                    "D - ${calculateDateDiff(exhbt.exhbt_to_dt.toDate(), Calendar.getInstance().time)}"
                Glide.with(view)
                    .load(exhbt.exhbt_sn)
                    .apply(
                        RequestOptions().transform(
                            CenterCrop(),
                            RoundedCorners(
                                15f.toDp(view)
                            )
                        )
                    )
                    .into(binding.mybirdReservation1Image)
            }
            
            if (!exhbtList.isNullOrEmpty() && exhbtList.size >= 2) {
                binding.mybirdReservation2Title.visibility = View.VISIBLE
                binding.mybirdReservation2Date.visibility = View.VISIBLE
                binding.mybirdReservation2Dday.visibility = View.VISIBLE
                binding.mybirdReservation2Image.visibility = View.VISIBLE

                val exhbt = exhbtList[1]
                binding.mybirdReservation2Title.text = exhbt.exhbt_nm
                binding.mybirdReservation2Date.text =
                    "${exhbt.exhbt_from_dt.dateFormatted()} ~ ${exhbt.exhbt_to_dt.dateFormatted()}"
                binding.mybirdReservation2Dday.text =
                    "D - ${calculateDateDiff(exhbt.exhbt_to_dt.toDate(), Calendar.getInstance().time)}"
                Glide.with(view)
                    .load(exhbt.exhbt_sn)
                    .apply(
                        RequestOptions().transform(
                            CenterCrop(),
                            RoundedCorners(
                                15f.toDp(view)
                            )
                        )
                    )
                    .into(binding.mybirdReservation2Image)
            }
        }

        binding.mybirdReservationContainer.setOnClickListener {
            // 예매한 전시 클릭 >> 내가 예매한 전시 화면으로 넘어감
            parentActivity.supportFragmentManager.replaceFragment(ReservationDetailFragment())
        }

        binding.mybirdLikeContainer.setOnClickListener {
            // 찜한 전시 클릭 >> 내가 찜한 전시 화면으로 넘어감
            parentActivity.supportFragmentManager.replaceFragment(LikeDetailFragment())
        }

        binding.mybirdSetting.setOnClickListener {
            // 옵션 버튼 클릭 >> 세팅 화면으로 넘어감
            parentActivity.supportFragmentManager.replaceFragment(SettingFragment())
        }
    }
}