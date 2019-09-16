package com.mdove.dependent.common.loading

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mdove.dependent.common.R
import kotlinx.android.synthetic.main.dialog_loading.*

open class LoadingDialogFragment : BaseDialogFragment() {

    companion object {
        const val TAG = "LoadingDialog"
        const val EXTRA_HINT = "hint"

        fun newInstance(text: String? = null): LoadingDialogFragment {
            return LoadingDialogFragment().apply {
                arguments = Bundle().apply { putString(EXTRA_HINT, text) }
            }
        }
    }

    private val keyListeners = mutableSetOf<DialogInterface.OnKeyListener>()

    override val backgroundRes: Int = R.drawable.bg_loading_dialog

    override fun createView(layoutInflater: LayoutInflater, container: ViewGroup?): View {
        return layoutInflater.inflate(R.layout.dialog_loading, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLoadingTv(arguments?.getString(EXTRA_HINT))
    }

    fun show(manager: FragmentManager?) {
        try {// 防止多次add
            manager?.executePendingTransactions()
        } catch (e: Exception) {
            Log.d(TAG, e.toString())
        }
        if (!isAdded) {
            BaseDialogFragment.safelyShow(this, manager, TAG)
        }
    }

    override fun dismissAllowingStateLoss() {
        try {
            fragmentManager?.executePendingTransactions()
        } catch (e: Exception) {
            Log.d(TAG, e.toString())
        }
        if (isAdded) {
            super.dismissAllowingStateLoss()
        }
    }

    fun setDialogCancelable(cancelable: Boolean){
        _cancelable = cancelable
    }

    override fun onStart() {
        super.onStart()
        dialog?.setOnKeyListener { dialog, keyCode, event ->
            dispatchKeyListener(dialog, keyCode, event)
            false
        }
    }

    fun setLoadingTv(text: String?) {
        if (isAdded) {
            if (text.isNullOrEmpty()) {
                dialog_loading_tv?.visibility = View.GONE
            } else {
                dialog_loading_tv?.visibility = View.VISIBLE
                dialog_loading_tv?.text = text
            }
        } else {
            arguments?.putString(EXTRA_HINT, text)
        }
    }

    fun registerKeyListener(keyListener: DialogInterface.OnKeyListener){
        keyListeners.add(keyListener)
    }

    fun unRegisterKeyListener(keyListener: DialogInterface.OnKeyListener) {
        keyListeners.remove(keyListener)
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        keyListeners.clear()
    }

    private fun dispatchKeyListener(dialog: DialogInterface?, keyCode: Int, event: KeyEvent?) {
        keyListeners.forEach {
            it.onKey(dialog, keyCode, event)
        }
    }
}
