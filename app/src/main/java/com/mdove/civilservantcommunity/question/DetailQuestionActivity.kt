package com.mdove.civilservantcommunity.question

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.mdove.civilservantcommunity.R
import com.mdove.civilservantcommunity.base.AbsSlideCloseActivity
import com.mdove.civilservantcommunity.question.bean.QuestionReqParams

/**
 * Created by MDove on 2019-11-24.
 */
class DetailQuestionActivity : AbsSlideCloseActivity() {
    companion object {
        val TAG_QUESTION_FRAGMNET = "tag_question_fragment"
        val INTENT_PARAMS_QUESTION_FRAGMNET = "intent_params_question_fragment"

        fun gotoQuestion(context: Context, reqParams: QuestionReqParams) {
            val intent = Intent(context, DetailQuestionActivity::class.java)
            intent.putExtra(INTENT_PARAMS_QUESTION_FRAGMNET, reqParams)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setStatusBarTextColorIsBlack(true)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(
                    R.id.container,
                    DetailQuestionFragment.newInstance(
                        intent.getParcelableExtra(INTENT_PARAMS_QUESTION_FRAGMNET)
                    ),
                    TAG_QUESTION_FRAGMNET
                )
                .commit()
        }
    }
}