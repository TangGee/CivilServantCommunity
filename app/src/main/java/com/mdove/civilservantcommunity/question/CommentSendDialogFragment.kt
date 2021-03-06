package com.mdove.civilservantcommunity.question

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getColor
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.base.fragment.AbsDialogFragment
import com.mdove.civilservantcommunity.question.bean.BaseCommentSendParams
import com.mdove.civilservantcommunity.question.viewmodel.CommentViewModel
import com.mdove.civilservantcommunity.question.viewmodel.InputStatus
import com.mdove.dependent.common.networkenhance.valueobj.Status
import com.mdove.dependent.common.utils.UIUtils
import com.mdove.dependent.common.utils.setDebounceOnClickListener
import com.mdove.dependent.common.utils.showLoading
import com.mdove.dependent.common.view.span.RadiusBackgroundSpan
import kotlinx.android.synthetic.main.fragment_dialog_question_send_comment.*

/**
 * Created by DMove on 2019-11-24.
 */
class CommentSendDialogFragment : AbsDialogFragment() {
    private var inputMethodManager: InputMethodManager? = null
    private lateinit var viewModel: CommentViewModel

    companion object {
        const val PARAMS_FRAGMENT = "params_fragment"
        fun newInstance(params: BaseCommentSendParams): CommentSendDialogFragment {
            return CommentSendDialogFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(PARAMS_FRAGMENT, params)
                    classLoader = params::class.java.classLoader
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CommentDialogFragment_Dialog)
        inputMethodManager =
            activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        viewModel = ViewModelProviders.of(activity!!).get(CommentViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dialog_question_send_comment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initIntent()
        initView()
        et_comment.postDelayed({
            viewModel.inputStatusLiveData.value = InputStatus.INPUT_STATUS_SHOW
        }, 200)
    }

    private fun initIntent() {
        arguments?.getParcelable<BaseCommentSendParams>(PARAMS_FRAGMENT)?.let {
            viewModel.params.value = it
        }
    }

    private fun initView() {
        btn_send.setDebounceOnClickListener {
            val content = et_comment.text.toString()
            viewModel.trySendComment(content)?.observe(this, Observer {
                when (it.status) {
                    Status.ERROR -> {
                        dismiss()
                    }
                    Status.LOADING -> {
                        showLoading()
                    }
                    Status.SUCCESS -> {
                        dismiss()
                        dismissAllowingStateLoss()
                    }
                }
            })
        }
        btn_close.setDebounceOnClickListener {
            dismissAllowingStateLoss()
        }
        root.exitInvoke = {
            dismissAllowingStateLoss()
        }
        viewModel.titleLiveData.observe(this, Observer {
            it?.let {
                val spannableString = SpannableString(it.content)
                spannableString.setSpan(
                    ForegroundColorSpan(getColor(context!!, R.color.black)),
                    it.spanStart,
                    it.spanEnd,
                    Spanned.SPAN_INCLUSIVE_INCLUSIVE
                )
                spannableString.setSpan(
                    RadiusBackgroundSpan(
                        getColor(context, R.color.grey_200),
                        UIUtils.dip2Px(context, 2).toInt()
                    ),
                    it.spanStart,
                    it.spanEnd,
                    Spanned.SPAN_INCLUSIVE_INCLUSIVE
                )
                tv_to_name.text = spannableString
            }
        })

        et_comment.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                enableSend((s?.isBlank() != true))
            }

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        viewModel.inputStatusLiveData.observe(this, Observer {
            showOrHideSoftKeyboard(it == InputStatus.INPUT_STATUS_SHOW)
        })
    }

    private fun enableSend(enable: Boolean) {
        context?.let {
            btn_send.setTextColor(
                getColor(
                    it,
                    if (enable) {
                        btn_send.isEnabled = true
                        R.color.blue_500
                    } else {
                        btn_send.isEnabled = false
                        R.color.blue_200
                    }
                )
            )
        }
    }

    /**
     * 处理软键盘
     */
    private fun showOrHideSoftKeyboard(show: Boolean) {
        et_comment?.let {
            if (show) {
                it.requestFocus()
                inputMethodManager?.showSoftInput(
                    it,
                    InputMethodManager.SHOW_IMPLICIT
                )

            } else {
                inputMethodManager?.hideSoftInputFromWindow(
                    it.windowToken,
                    0
                )
            }
        }
    }

    override fun onStop() {
        super.onStop()
        inputMethodManager?.hideSoftInputFromWindow(
            et_comment.windowToken,
            0
        )
    }
}