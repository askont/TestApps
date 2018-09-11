package com.yakov.weber.calculator.ui.flag.fragment

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.LinearLayout
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.yakov.weber.calculator.R
import com.yakov.weber.calculator.presenter.flag.fragment.FlagFragmentPresenter
import com.yakov.weber.calculator.presenter.flag.fragment.FlagFragmentView
import com.yakov.weber.calculator.toothpick.DI
import com.yakov.weber.calculator.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_main_flag.*
import toothpick.Toothpick
import java.io.InputStream

class MainFlagFragment : BaseFragment(), FlagFragmentView , View.OnClickListener{


    companion object {
        private val TAG = MainFlagFragment::class.java.simpleName
        fun newInstance() = MainFlagFragment()
    }

    private lateinit var animation: Animation
    private var listContainerButton = arrayListOf<LinearLayout>()

    @InjectPresenter
    lateinit var presenter: FlagFragmentPresenter

    @ProvidePresenter
    fun flagFragmentPresenter(): FlagFragmentPresenter = Toothpick
            .openScope(DI.APP_FLAG)
            .getInstance(FlagFragmentPresenter::class.java)

    override val layoutRes: Int
        get() = R.layout.fragment_main_flag

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(false)
        initField()

    }

    private fun initField() {
        animation = AnimationUtils.loadAnimation(activity, R.anim.snake_anim_button_flag)
        animation.repeatCount = 3
        listContainerButton.addAll(listOf(row1_button_container, row2_button_container, row3_button_container, row4_button_container))
        listContainerButton.forEach { container ->
            (0 until container.childCount)
                    .forEach { container.getChildAt(it).setOnClickListener(this) }
        }
    }

    override fun onStart() {
        super.onStart()
        presenter.attachView(this)
    }

    override fun onClick(v: View) {
        flag_image_view.startAnimation(animation)
        presenter.loadNextFlag()
    }

    override fun showRandomButtonCorrectAnswer(row: Int, column: Int, correctAnswer: String) {
        val correctButton = listContainerButton[row].getChildAt(column) as Button
        correctButton.text = correctAnswer
    }


    override fun showButtonFlagAnswer(listFlag: List<String>,countRow:IntRange) {
        countRow.forEach {
            for (column in 0 until listContainerButton[it].childCount){
                val button = listContainerButton[it].getChildAt(column) as Button
                button.isEnabled = true
                val filename = listFlag[(it * 2) + column]
                button.text = filename
            }
        }
    }

    override fun setFlagStream(stream: InputStream,nameFlag:String) {
        flag_image_view.setImageDrawable(Drawable.createFromStream(stream,nameFlag))

    }

    override fun showContainerAnswerButton(countRow: IntRange) {
        listContainerButton.forEach { it.visibility = View.GONE }
        countRow.forEach { listContainerButton[it].visibility = View.VISIBLE }

    }

    override fun showQuestionNumber(message: String) {
        question_number_text_view.text = message
    }

    override fun showError(message: String) {
        question_text_view.text = message
    }
}