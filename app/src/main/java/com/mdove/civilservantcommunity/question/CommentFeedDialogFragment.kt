package com.mdove.civilservantcommunity.question

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.base.fragment.AbsDialogFragment
import com.mdove.civilservantcommunity.question.adapter.OnQuestionCommentListener
import com.mdove.civilservantcommunity.question.adapter.QuestionCommentAdapter
import com.mdove.civilservantcommunity.question.bean.*
import com.mdove.civilservantcommunity.question.viewmodel.CommentViewModel
import com.mdove.dependent.common.utils.setDebounceOnClickListener
import com.mdove.dependent.common.view.dragroot.DragRootView
import kotlinx.android.synthetic.main.fragment_comment_dialog.*

/**
 * Created by MDove on 2019-11-24.
 */
class CommentFeedDialogFragment : AbsDialogFragment() {
    private var adapter = QuestionCommentAdapter(object : OnQuestionCommentListener {
        override fun onClickSendComment(data: QuestionCommentBean) {
            CommentSendDialogFragment.newInstance(
                OneCommentSendParams(
                    data.info,
                    content = null,
                    anid = data.anid,
                    listStyle = data.listStyle
                )
            ).show(childFragmentManager, null)
        }

        override fun onClickSendCommentChild(data: QuestionCommentPairBean) {
            CommentSendDialogFragment.newInstance(
                TwoCommentSendParams(
                    data.child.commentInfo,
                    content = null,
                    anid = data.child.anid,
                    listStyle = data.child.listStyle
                )
            ).show(childFragmentManager, null)
        }
    })

    companion object {
        const val PARAMS_FRAGMENT = "params_fragment"
        fun newInstance(bean: AnswerDetailBean): CommentFeedDialogFragment {
            return CommentFeedDialogFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(PARAMS_FRAGMENT, bean)
                    classLoader = bean::class.java.classLoader
                }
            }
        }
    }

    private lateinit var viewModel: CommentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CommentDialogFragment_Dialog)
        viewModel = ViewModelProviders.of(activity!!).get(CommentViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return layoutInflater.inflate(R.layout.fragment_comment_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bean = arguments?.getParcelable<AnswerDetailBean>(PARAMS_FRAGMENT) ?: return
        btn_close.setDebounceOnClickListener {
            dismissAllowingStateLoss()
        }
        root.setOnInterceptClickListener(object : DragRootView.OnInterceptClickListener {
            override fun onClick(view: View): Boolean {
                dismissAllowingStateLoss()
                return true
            }
        })
        root.exitInvoke = {
            dismissAllowingStateLoss()
        }
        bean.an?.userInfo?.let {
            tv_title.text = "回复：${it.username}"
        }
        rlv.adapter = adapter
        rlv.layoutManager = LinearLayoutManager(context)
        adapter.submitList(buildData(bean))
    }

    private fun buildData(bean: AnswerDetailBean): List<BaseQuestionCommentBean> {
        val data = mutableListOf<BaseQuestionCommentBean>()
        bean.playCommentOnelist?.forEach { father ->
            val fatherId = father.info?.commentId ?: return@forEach
            val _father = father.toQuestionCommentBean()
            data.add(_father)
            bean.playCommentTwolistplayCommentOnelist?.filter {
                it.fatherId == fatherId
            }?.takeIf {
                it.isNotEmpty()
            }?.let {
                it.forEach {
                    data.add(QuestionCommentPairBean(_father, it.toQuestionCommentChildBean()))
                }
            }
        }
        return data
    }
}