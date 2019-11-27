package com.mdove.civilservantcommunity.question

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.base.fragment.BaseFragment
import com.mdove.civilservantcommunity.question.adapter.OnClickQuestionListener
import com.mdove.civilservantcommunity.question.adapter.QuestionAdapter
import com.mdove.civilservantcommunity.question.bean.*
import com.mdove.civilservantcommunity.question.viewmodel.QuestionViewModel
import com.mdove.dependent.common.networkenhance.valueobj.Status
import com.mdove.dependent.common.utils.*
import kotlinx.android.synthetic.main.fragment_question.*

/**
 * Created by MDove on 2019-11-24.
 */
class QuestionFragment : BaseFragment() {
    private lateinit var mViewModel: QuestionViewModel
    private var adapter = QuestionAdapter(object : OnClickQuestionListener {
        override fun onClickReply(bean: AnswerDetailBean) {
            CommentSendDialogFragment.newInstance(
                OneCommentSendParams(
                    commentInfo = CommentInfoBean(
                        bean.an?.userInfo?.uid,
                        bean.an?.userInfo?.username
                    ),
                    content = null,
                    anid = bean.an?.anid,
                    listStyle = bean.an?.listStyle
                )
            ).show(childFragmentManager, null)
        }

        override fun onClickMore(bean: AnswerDetailBean) {
            CommentFeedDialogFragment.newInstance(bean).show(childFragmentManager, null)
        }
    })

    companion object {
        const val PARAMS_FRAGMENT = "params_fragment"
        fun newInstance(params: QuestionReqParams): QuestionFragment {
            return QuestionFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(PARAMS_FRAGMENT, params)
                    classLoader = params::class.java.classLoader
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProviders.of(activity!!).get(QuestionViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_question, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getParcelable<QuestionReqParams>(PARAMS_FRAGMENT)?.let {
            mViewModel.questionDetailResp.value = it
        }

        rlv.adapter = adapter
        rlv.layoutManager = LinearLayoutManager(context)

        mViewModel.questionDetailLiveData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissLoading()
                    it.data?.data?.let {
                        it.question?.let {
                            val userName = "来自：${it.userInfo?.username ?: "匿名用户"}"
                            UIUtils.setTextViewSpanColor(
                                tv_question_user,
                                userName,
                                3,
                                userName.length,
                                ContextCompat.getColor(
                                    context!!,
                                    R.color.blue_500
                                )
                            )
                        }
                        tv_question_title.text = it.question?.title ?: "无标题"
                        tv_question_content.text = it.question?.content ?: "无内容"
                        tv_question_time.text = TimeUtils.getDateChinese(it.question?.makeTime)
                        it.answers?.let {
                            adapter.submitList(it)
                        }
                    }
                }
                Status.LOADING -> {
                    showLoading()
                }
                Status.ERROR -> {
                    dismissLoading()
                }
            }
        })

        layout_send.setDebounceOnClickListener {
            CommentSendDialogFragment.newInstance(mViewModel.buildAnswerCommentSendParams())
                .show(childFragmentManager, null)
        }
        view_toolbar.setToolbarBackgroundIsNull()
        view_toolbar.setTitle("提问")
        view_toolbar.setColorForAll(Color.BLACK)
    }
}