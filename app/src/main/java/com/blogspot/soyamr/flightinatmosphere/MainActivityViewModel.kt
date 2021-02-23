package com.blogspot.soyamr.flightinatmosphere

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mikephil.charting.data.Entry
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivityViewModel : ViewModel() {
    private val _points: MutableLiveData<ArrayList<Entry>> = MutableLiveData(ArrayList())
    val points: LiveData<ArrayList<Entry>> = _points


//    private val _maxY: MutableLiveData<Int> = MutableLiveData(30)
//    val maxY: LiveData<Int> = _maxY
//
//    private val _maxX: MutableLiveData<Int> = MutableLiveData(30)
//    val maxX: LiveData<Int> = _maxX

    val heightErrorMessage: MutableLiveData<String> = MutableLiveData("")
    val angelErrorMessage: MutableLiveData<String> = MutableLiveData("")
    val speedErrorMessage: MutableLiveData<String> = MutableLiveData("")
    val sizeErrorMessage: MutableLiveData<String> = MutableLiveData("")
    val weightErrorMessage: MutableLiveData<String> = MutableLiveData("")


    val height: MutableLiveData<String> = MutableLiveData("10")
    val angel: MutableLiveData<String> = MutableLiveData("10")
    val speed: MutableLiveData<String> = MutableLiveData("10")
    val size: MutableLiveData<String> = MutableLiveData("10")
    val weight: MutableLiveData<String> = MutableLiveData("10")

    val time: MutableLiveData<Double> = MutableLiveData(0.0)
    var ctr = 0;

    private val ball = Ball()

    var job: Job? = null
    var pause = false

    fun launch() {
        //in case of additional click while app is making the simulation
        if (job != null && job!!.isActive) {
            return
        }
        //if not paused or job is active then start over, [means the chart finished drawing]
        if (!pause) {
            if (isValidateInput())
                resetData()
        }
        //continue or start new chart drawing
        pause = false
        job = viewModelScope.launch {
            while (ball.y >= 0) {
                delay(10)
                time.value = ball.t
                ball.update()
                _points.value?.add(Entry(ball.x.toFloat(), ball.y.toFloat()))
                _points.value = _points.value
            }
        }
    }

    private fun isValidateInput(): Boolean {
        var result = true

        when {
            height.value.isNullOrEmpty() -> {
                heightErrorMessage.value = "can't be empty"
                result = false
            }
            height.value!!.toInt() < 0 -> {
                heightErrorMessage.value = "can't be less than 0"
                result = false
            }
            else -> {
                heightErrorMessage.value = ""
            }
        }

        when {
            speed.value.isNullOrEmpty() -> {
                speedErrorMessage.value = "can't be empty"
                result = false
            }
            speed.value!!.toInt() < 0 -> {
                speedErrorMessage.value = "can't be less than 0"
                result = false
            }
            else -> {
                speedErrorMessage.value = ""
            }
        }

        when {
            angel.value.isNullOrEmpty() -> {
                angelErrorMessage.value = "can't be empty"
                result = false
            }
            angel.value!!.toInt() > 90 -> {
                angelErrorMessage.value = "can't be bigger than 90"
                result = false
            }
            angel.value!!.toInt() < -90 -> {
                angelErrorMessage.value = "can't be less than -90"
                result = false
            }
            else -> {
                angelErrorMessage.value = ""
            }
        }

        when {
            weight.value.isNullOrEmpty() -> {
                weightErrorMessage.value = "can't be empty"
                result = false
            }
            weight.value!!.toInt() < 0 -> {
                weightErrorMessage.value = "can't be less than 0"
                result = false
            }
            else -> {
                weightErrorMessage.value = ""
            }
        }

        when {
            size.value.isNullOrEmpty() -> {
                sizeErrorMessage.value = "can't be empty"
                result = false
            }
            size.value!!.toInt() < 0 -> {
                sizeErrorMessage.value = "can't be less than 0"
                result = false
            }
            else -> {
                sizeErrorMessage.value = ""
            }
        }

        return result
    }


    private fun resetData() {
        ball.reset(
            speed.value!!.toDouble(),
            height.value!!.toDouble(),
            angel.value!!.toDouble(),
            size.value!!.toDouble(),
            weight.value!!.toDouble()
        )
        time.value = 0.0
        _points.value?.clear()
        _points.value = _points.value

//        _maxY.value = ball.maximumHeight.toInt()
//        _maxX.value = ball.maximumWidth.toInt()
        _points.value?.add(Entry(ball.x.toFloat(), ball.y.toFloat()))
        _points.value = _points.value
    }

    fun pause() {
        if (job != null && job!!.isActive) {
            job?.cancel()
            pause = true
        }
    }

    fun reset() {
        job?.cancel()
        resetData()
    }
}