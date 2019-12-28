package com.mdove.civilservantcommunity.question

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.base.fragment.BaseFragment
import com.mdove.civilservantcommunity.question.adapter.OnClickQuestionListener
import com.mdove.civilservantcommunity.question.adapter.DetailQuestionAdapter
import com.mdove.civilservantcommunity.question.bean.*
import com.mdove.civilservantcommunity.question.viewmodel.QuestionViewModel
import com.mdove.dependent.common.networkenhance.valueobj.Status
import com.mdove.dependent.common.recyclerview.PaddingDecoration
import com.mdove.dependent.common.recyclerview.PaddingType
import com.mdove.dependent.common.utils.*
import kotlinx.android.synthetic.main.fragment_detail_question.*

/**
 * Created by MDove on 2019-11-24.
 */
class DetailQuestionFragment : BaseFragment() {
    private lateinit var mViewModel: QuestionViewModel
    private var adapter = DetailQuestionAdapter(object : OnClickQuestionListener {
        override fun onClickSendAnswer() {
            CommentSendDialogFragment.newInstance(mViewModel.buildAnswerCommentSendParams())
                .show(childFragmentManager, null)
        }

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
        fun newInstance(params: QuestionReqParams): DetailQuestionFragment {
            return DetailQuestionFragment().apply {
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
        return inflater.inflate(R.layout.fragment_detail_question, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getParcelable<QuestionReqParams>(PARAMS_FRAGMENT)?.let {
            mViewModel.questionDetailResp.value = it
        }

        rlv.addItemDecoration(PaddingDecoration(8, PaddingType.BOTTOM))
        rlv.layoutManager = LinearLayoutManager(context)
        rlv.adapter = adapter

        mViewModel.questionDetailLiveData.observe(this, Observer {res->
            res?.let{
                when (it.status) {
                    Status.SUCCESS -> {
                        dismissLoading()
                        it.data?.data?.let {
                            adapter.submitList(it)
                        }
                    }
                    Status.LOADING -> {
                        showLoading()
                    }
                    Status.ERROR -> {
                        dismissLoading()
                        it.data?.data?.let {
                            adapter.submitList(it)
                        }
                    }
                }
            }
        })

        view_toolbar.setToolbarBackgroundIsNull()
        view_toolbar.setTitle("提问")
        view_toolbar.setColorForAll(Color.WHITE)
    }
}