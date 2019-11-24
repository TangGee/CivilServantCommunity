package com.mdove.civilservantcommunity.question

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.base.fragment.BaseFragment
import com.mdove.civilservantcommunity.question.bean.QuestionReqParams
import com.mdove.civilservantcommunity.question.viewmodel.QuestionViewModel

/**
 * Created by MDove on 2019-11-24.
 */
class QuestionFragment : BaseFragment() {
    private lateinit var mViewModel: QuestionViewModel

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

        mViewModel.questionDetailLiveData.observe(this, Observer {
            when (it.status) {

            }
        })
    }
}