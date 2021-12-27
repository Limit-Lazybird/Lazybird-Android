package com.limit.lazybird.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.viewbinding.ViewBinding
import com.limit.lazybird.databinding.FragmentLoginBinding

/************* BaseFragment ***************
 * Fragment 에서 중복되는 코드들을 제거하기 위한 Fragment
 ********************************************** ***/
abstract class BaseFragment<VB: ViewBinding>(
    private val inflate: (LayoutInflater, ViewGroup?, Boolean) -> VB,
): Fragment() {

    val TAG = javaClass.simpleName

    lateinit var navController: NavController

    private var _binding: VB? = null
    val binding get() = _binding?: error("viewBinding 이 아직 초기화되지 않았습니다.")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = inflate.invoke(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = view.findNavController()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}