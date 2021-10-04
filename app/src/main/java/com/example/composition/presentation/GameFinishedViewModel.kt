package com.example.composition.presentation

import android.app.Application
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.media.Image
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.composition.R
import com.example.composition.domain.entity.GameResult

class GameFinishedViewModel(application: Application) : AndroidViewModel(application) {

    private lateinit var gameResult: GameResult

    private val context = application

    private val _image = MutableLiveData<Drawable>()
    val image: LiveData<Drawable>
        get() = _image

    private val _minCountOfRightAnswers = MutableLiveData<String>()
    val minCountOfRightAnswers: LiveData<String>
        get() = _minCountOfRightAnswers

    private val _countOfRightAnswers = MutableLiveData<String>()
    val countOfRightAnswers: LiveData<String>
        get() = _countOfRightAnswers

    private val _minPercentOfRightAnswers = MutableLiveData<String>()
    val minPercentOfRightAnswers: LiveData<String>
        get() = _minPercentOfRightAnswers

    private val _percentOfRightAnswers = MutableLiveData<String>()
    val percentOfRightAnswers: LiveData<String>
        get() = _percentOfRightAnswers


    private var minCountAnswers = 0
    private var countAnswers = 0
    private var minPercent = 0
    private var percent = 0

    fun setValues(result: GameResult) {
        setFields(result)
        setImage()
        setMinCountOfRightAnswers()
        setCountOfRightAnswers()
        setMinPercentOfRightAnswers()
        setPercentOfRightAnswers()
    }

    private fun setFields(result: GameResult){
        gameResult = result
        minCountAnswers = gameResult.gameSettings.minCountOfRightAnswer
        countAnswers = gameResult.countOfRightAnswers
        minPercent = gameResult.gameSettings.minPercentOfRightAnswer
        percent = calculatePercentOfRightAnswers()
    }

    private fun setImage() {
        val countResult = countAnswers >= minCountAnswers
        val percentResult = percent >= minPercent
        if(countResult && percentResult){
            _image.value = context.resources.getDrawable(R.drawable.ic_smile) as Drawable
        } else {
            _image.value = context.resources.getDrawable(R.drawable.ic_sad) as Drawable
        }
    }

    private fun setMinCountOfRightAnswers() {
        _minCountOfRightAnswers.value = String.format(
            context.resources.getString(R.string.required_score),
            minCountAnswers
        )
    }

    private fun setCountOfRightAnswers() {
        _countOfRightAnswers.value = String.format(
            context.resources.getString(R.string.score_answers),
            countAnswers
        )
    }

    private fun setMinPercentOfRightAnswers() {
        _minPercentOfRightAnswers.value = String.format(
            context.resources.getString(R.string.required_percentage),
            minPercent
        )
    }

    private fun setPercentOfRightAnswers() {
        _percentOfRightAnswers.value = String.format(
            context.resources.getString(R.string.score_percentage),
            percent
        )
    }


    private fun calculatePercentOfRightAnswers(): Int {
        val countOfQuestions = gameResult.countOfQuestions
        return (countAnswers * 100) / countOfQuestions
    }
}