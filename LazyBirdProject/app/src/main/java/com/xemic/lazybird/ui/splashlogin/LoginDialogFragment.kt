package com.xemic.lazybird.ui.splashlogin

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.model.KakaoSdkError
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.user.model.User
import com.xemic.lazybird.R

/*** deprecated Fragment ***/

class LoginDialogFragment : DialogFragment() {

    companion object {
        const val TAG = "LoginDialogFragment"
        const val RESULT_CANCLED = "RESULT_CANCLED"
        const val RESULT_KAKAO_OK = "RESULT_KAKAO_OK"
        const val RESULT_GOOGLE_OK = "RESULT_GOOGLE_OK"
        const val RESULT_SIGN_IN_DIALOG = "RESULT_SIGN_IN_DIALOG"
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Kakao Init
        KakaoSdk.init(requireContext(), getString(R.string.native_app_key))
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e(LoginDialogFragment.TAG, "로그인 실패 : ${error}")
            } else if (token != null) {
                Log.e(LoginDialogFragment.TAG, "로그인 성공")
                UserApiClient.instance.me { user, error ->
                    if (error != null) {
                        Log.e(LoginDialogFragment.TAG, "사용자 정보 요청 실패: ${error}")
                    } else if (user != null) {
                        successKakaoLogin(user, token) // 로그인 성공
                    } else {
                        Log.e(LoginDialogFragment.TAG, "user is null")
                    }
                }
            }
        }

        // GoogleSignInClient 개체 구성
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestServerAuthCode(getString(R.string.google_server_client_id))
            .requestEmail()
            .build()
        val mGoogleSignInClient = GoogleSignIn.getClient(context, gso)
        val registerResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    // Result OK 일 때, SignIn 한 결과에서 account 가져오기
                    val data = result.data
                    val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                    try {
                        // success
                        val account = task.getResult(ApiException::class.java)
                        successGoogleLogin(account)
                    } catch (e: ApiException) {
                        // fail
                        Log.w(LoginDialogFragment.TAG, "signInResult:failed code= " + e.statusCode)
                    }
                }
            }

        // alertDialog 생성
        val layout = layoutInflater.inflate(R.layout.custom_login_dialog, null).apply {
            findViewById<RelativeLayout>(R.id.login_background).setOnClickListener {
                // 바탕화면 클릭시 out
                cancelDialog()
            }
            findViewById<ImageButton>(R.id.login_google_btn).setOnClickListener {
                // 구글로 로그인하기 버튼 클릭
                var account = GoogleSignIn.getLastSignedInAccount(context)
                if (account == null) {
                    // 기존 로그인 사용자가 아닌 경우
                    // 회원가입 진행
                    Log.e("test", "기존사용자 아님")
                    val intent = mGoogleSignInClient.signInIntent
                    registerResult.launch(intent)
                } else {
                    // 기존 사용자
                    Log.e("test", "기존사용자")
                    successGoogleLogin(account)
                }
            }
            findViewById<ImageButton>(R.id.login_kakao_btn).setOnClickListener {
                // 카카오로 로그인하기 버튼 클릭
                if (AuthApiClient.instance.hasToken()) {
                    // 이미 로그인하여 토큰을 발급받았었음
                    UserApiClient.instance.accessTokenInfo { _, error ->
                        if (error != null) {
                            if (error is KakaoSdkError && error.isInvalidTokenError()) {
                                // 로그인 필요
                                UserApiClient.instance.loginWithKakaoAccount(
                                    context,
                                    callback = callback
                                )
                            } else {
                                // 기타 에러
                                Log.e("LoginActivity", "error occured")
                                error.printStackTrace()
                            }
                        } else {
                            // 토큰 유효성 체크 성공
                            UserApiClient.instance.me { user, error ->
                                if (error != null) {
                                    Log.e(LoginDialogFragment.TAG, "사용자 정보 요청 실패: $error")
                                } else if (user != null) {
                                    // MainActivity 로 이동
                                    successKakaoLogin(user, AuthApiClient.instance.tokenManagerProvider.manager.getToken()!!)
                                } else {
                                    Log.e(LoginDialogFragment.TAG, "user is null")
                                }
                            }
                        }
                    }
                } else {
                    // 로그인 필요
                    UserApiClient.instance.loginWithKakaoAccount(
                        context,
                        callback = callback
                    )
                }
            }
            findViewById<TextView>(R.id.login_sign_in_button).setOnClickListener {
                // 회원가입 버튼
                openSignInDialog()
            }
        }

        return AlertDialog.Builder(requireContext(), R.style.DialogThemeFullScreen)
            .setOnKeyListener(object : DialogInterface.OnKeyListener {
                override fun onKey(
                    dialogInterface: DialogInterface?,
                    keyCode: Int,
                    event: KeyEvent
                ): Boolean {
                    // 뒤로가기 눌렀을 때
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        cancelDialog()
                        return true
                    }
                    return false
                }
            })
            .setView(layout)
            .create()
            .apply {
                window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 배경 투명하게 하기
                window?.setWindowAnimations(R.style.DialogPopUp) // slide_up_in, slide_down_out 애니메이션 적용
                window?.statusBarColor = Color.TRANSPARENT // statusBar 투명하게 하기
            }
    }

    private fun successGoogleLogin(account: GoogleSignInAccount) {
        // 구글 로그인 성공
        setFragmentResult(
            LoginDialogFragment.TAG, bundleOf(
                "resultCode" to LoginDialogFragment.RESULT_GOOGLE_OK,
                "accountName" to account.account.name,
                "accountType" to account.account.type,
                "displayName" to account.displayName,
                "email" to account.email,
                "givenName" to account.givenName,
                "id" to account.id,
                "photoUrl" to account.photoUrl,
                "idToken" to account.idToken,
                "isExpired" to account.isExpired
            )
        )
        dismiss()
    }

    private fun successKakaoLogin(user: User, token: OAuthToken) {
        // 카카오 로그인 성공
        setFragmentResult(
            LoginDialogFragment.TAG, bundleOf(
                "resultCode" to LoginDialogFragment.RESULT_KAKAO_OK,
                "id" to user?.id.toString(),
                "profileNickname" to user?.kakaoAccount?.profile?.nickname.toString(),
                "accountEmail" to user?.kakaoAccount?.gender.toString(),
                "birthday" to user?.kakaoAccount?.ageRange.toString(),
                "accessToken" to token.accessToken
            )
        )
        dismiss()
    }

    private fun openSignInDialog() {
        // 회원가입 dialog 열기
        setFragmentResult(
            LoginDialogFragment.TAG, bundleOf(
                "resultCode" to LoginDialogFragment.RESULT_SIGN_IN_DIALOG
            )
        )
        dismiss()
    }

    private fun cancelDialog() {
        // dialog 제거
        setFragmentResult(
            LoginDialogFragment.TAG, bundleOf(
                "resultCode" to LoginDialogFragment.RESULT_CANCLED
            )
        )
        dismiss()
    }

}