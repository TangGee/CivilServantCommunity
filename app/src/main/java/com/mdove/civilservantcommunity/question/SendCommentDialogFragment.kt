package com.mdove.civilservantcommunity.question

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.base.fragment.AbsDialogFragment
import com.mdove.civilservantcommunity.question.bean.QuestionCommentSendParams
import com.mdove.civilservantcommunity.question.viewmodel.CommentViewModel
import com.mdove.dependent.common.networkenhance.valueobj.Status
import com.mdove.dependent.common.utils.SoftKeyBoardListener
import com.mdove.dependent.common.utils.setDebounceOnClickListener
import com.mdove.dependent.common.utils.showLoading
import kotlinx.android.synthetic.main.fragment_dialog_question_send_comment.*

/**
 * Created by DMove on 2019-11-24.
 */
class SendCommentDialogFragment : AbsDialogFragment() {
    private var rootHeight: Int = 0
    private var keyboardHeight: Int = 0
    private var inputMethodManager: InputMethodManager? = null
    private var softKeyBoardListener: SoftKeyBoardListener? = null
    private lateinit var viewModel: CommentViewModel

    companion object {
        const val PARAMS_FRAGMENT = "params_fragment"
        fun newInstance(params: QuestionCommentSendParams): SendCommentDialogFragment {
            return SendCommentDialogFragment().apply {
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initIntent()
        initSoftKeyboardListener()
        initView()
        et_comment.postDelayed({
            showOrHideSoftKeyboard(true)
        }, 200)
    }

    private fun initIntent() {
        arguments?.getParcelable<QuestionCommentSendParams>(PARAMS_FRAGMENT)?.let {
            viewModel.params = it
        }
    }

    private fun initView() {
        btn_send.setDebounceOnClickListener {
            val content = et_comment.text.toString()
            viewModel.sendComment(content)?.observe(this, Observer {
                when (it.status) {
                    Status.ERROR->{
                        dismiss()
                    }
                    Status.LOADING->{
                        showLoading()
                    }
                    Status.SUCCESS->{
                        dismiss()
                        dismissAllowingStateLoss()
                    }
                }
            })
        }
        layout_root.setDebounceOnClickListener {
            dismissAllowingStateLoss()
        }
        et_comment.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                enableSend((s?.isBlank() != true))
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    private fun enableSend(enable: Boolean) {
        context?.let {
            btn_send.setTextColor(
                ContextCompat.getColor(
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

    private fun initSoftKeyboardListener() {
        if (softKeyBoardListener == null) {
            activity?.let {
                softKeyBoardListener = SoftKeyBoardListener(it).apply {
                    onSoftKeyBoardChangeListener =
                        object : SoftKeyBoardListener.OnSoftKeyBoardChangeListener {

                            override fun keyBoardShow(height: Int) {
                                // 部分手机弹起键盘时会把手机隐藏的底部导航栏拉起且推动布局，导致键盘高度计算异常
                                keyboardHeight =
                                    if (rootHeight != 0 && rootHeight > layout_root.height) {
                                        height - (rootHeight - layout_root.height)
                                    } else {
                                        height
                                    }

                                layout_edit?.let { view ->
                                    //下方空白区域和键盘等高
//                                    val lp = view.layoutParams as ConstraintLayout.LayoutParams
//                                    lp.bottomToBottom = keyboardHeight
//                                    view.layoutParams = lp
                                }
                            }

                            override fun keyBoardHide(height: Int) {
                                layout_edit?.let { view ->
                                    val lp = view.layoutParams as ConstraintLayout.LayoutParams
                                    lp.bottomToBottom = 0
                                    view.layoutParams = lp
                                }
                            }
                        }
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        softKeyBoardListener?.release()
        softKeyBoardListener = null
        inputMethodManager?.hideSoftInputFromWindow(
            layout_edit.windowToken,
            0
        )
    }
}