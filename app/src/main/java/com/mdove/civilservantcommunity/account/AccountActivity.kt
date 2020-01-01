package com.mdove.civilservantcommunity.account

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.base.BaseActivity
import androidx.transition.Fade
import androidx.transition.TransitionInflater
import androidx.transition.TransitionSet
import com.mdove.civilservantcommunity.account.AccountActivity.Companion.INTENT_PARAMS
import com.mdove.civilservantcommunity.account.bean.AccountResult
import com.mdove.civilservantcommunity.account.fragment.LoginFragment
import com.mdove.civilservantcommunity.account.fragment.RegisterFragment
import com.mdove.civilservantcommunity.base.launcher.ActivityLauncher

/**
 * Created by MDove on 2019-09-02.
 */
class AccountActivity : BaseActivity(), IAccountHandle {

    companion object {
        const val TAG_REGISTER_FRAGMENT = "tag_register_fragment"
        const val TAG_LOGIN_FRAGMENT = "tag_login_fragment"
        const val FADE_DEFAULT_TIME = 300L
        const val MOVE_DEFAULT_TIME = 300L
        const val INTENT_PARAMS = "intent_params_account"

        fun gotoAccount(context: Context){
            val intent = Intent(context, AccountActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.content,
                    LoginFragment(), TAG_LOGIN_FRAGMENT
                )
                .commit()
        }
    }

    override fun onClickRegister() {
        val loginFragment =
            supportFragmentManager.findFragmentByTag(TAG_LOGIN_FRAGMENT) ?: LoginFragment()
        val registerFragment =
            supportFragmentManager.findFragmentByTag(TAG_REGISTER_FRAGMENT) ?: RegisterFragment()

        val exitFade = Fade()
        exitFade.duration = FADE_DEFAULT_TIME
        loginFragment.exitTransition = exitFade

        val enterTransitionSet = TransitionSet()
        enterTransitionSet.addTransition(TransitionInflater.from(this).inflateTransition(android.R.transition.move))
        enterTransitionSet.duration = MOVE_DEFAULT_TIME
        enterTransitionSet.startDelay = FADE_DEFAULT_TIME
        registerFragment.sharedElementEnterTransition = enterTransitionSet

        val enterFade = Fade()
        enterFade.startDelay = FADE_DEFAULT_TIME
        enterFade.duration = MOVE_DEFAULT_TIME
        registerFragment.enterTransition = enterFade

        supportFragmentManager.beginTransaction().apply {
            (loginFragment as? ITransitionProvider)?.providerView()?.let {
                addSharedElement(it, it.transitionName)
            }
            replace(R.id.content, registerFragment, TAG_REGISTER_FRAGMENT)
            commitAllowingStateLoss()
        }
    }

    override fun onBackLogin() {
        val registerFragment =
            supportFragmentManager.findFragmentByTag(TAG_REGISTER_FRAGMENT) ?: RegisterFragment()
        val loginFragment =
            supportFragmentManager.findFragmentByTag(TAG_LOGIN_FRAGMENT) ?: LoginFragment()

        val exitFade = Fade()
        exitFade.duration = FADE_DEFAULT_TIME
        registerFragment.exitTransition = exitFade

        val enterTransitionSet = TransitionSet()
        enterTransitionSet.addTransition(TransitionInflater.from(this).inflateTransition(android.R.transition.move))
        enterTransitionSet.duration = MOVE_DEFAULT_TIME
        enterTransitionSet.startDelay = FADE_DEFAULT_TIME
        loginFragment.sharedElementEnterTransition = enterTransitionSet

        val enterFade = Fade()
        enterFade.startDelay = FADE_DEFAULT_TIME
        enterFade.duration = MOVE_DEFAULT_TIME
        loginFragment.enterTransition = enterFade

        supportFragmentManager.beginTransaction().apply {
            (registerFragment as? ITransitionProvider)?.providerView()?.let {
                addSharedElement(it, it.transitionName)
            }
            replace(R.id.content, loginFragment, TAG_LOGIN_FRAGMENT)
            commitAllowingStateLoss()
        }
    }
}

suspend fun ActivityLauncher.gotoAccountActivity(
    context: Context
): AccountResult {
    val intent = Intent(context, AccountActivity::class.java)
    return startActivityAsync(intent).await().run {
        return if (resultCode == Activity.RESULT_OK) {
            if (data != null) {
                AccountResult(
                    this.data.getParcelableExtra(
                        INTENT_PARAMS
                    ), com.mdove.civilservantcommunity.account.bean.Status.SUC
                )
            } else {
                AccountResult(
                    null,
                    com.mdove.civilservantcommunity.account.bean.Status.CANCEL
                )
            }
        } else {
            AccountResult(
                null,
                com.mdove.civilservantcommunity.account.bean.Status.CANCEL
            )
        }
    }
}