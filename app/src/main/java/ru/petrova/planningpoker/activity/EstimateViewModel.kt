package ru.petrova.planningpoker.activity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class EstimateViewModel : ViewModel() {

    val estimated = MutableLiveData<Boolean>()

    fun estimate(estimate: Boolean) {
        estimated.value = estimate
    }

    fun isEstimated(): Boolean {
        return estimated.value?:false
    }
}