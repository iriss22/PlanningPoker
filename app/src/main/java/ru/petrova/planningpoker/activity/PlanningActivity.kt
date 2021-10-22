package ru.petrova.planningpoker.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.petrova.planningpoker.R

class PlanningActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_planning)

        val estimateBundle = Bundle()
        estimateBundle.putString("roomId", intent.getStringExtra("roomId"))
        estimateBundle.putString("userName", intent.getStringExtra("userName"))
        val estimateFragment = EstimateFragment()
        estimateFragment.arguments = estimateBundle

        supportFragmentManager.beginTransaction().add(R.id.estimateFragment, estimateFragment).commitAllowingStateLoss()

        val resultBundle = Bundle()
        resultBundle.putString("partition", intent.getStringExtra("partition"))
        val resultFragment = ResultFragment()
        resultFragment.arguments = resultBundle

        supportFragmentManager.beginTransaction().add(R.id.resultFragment, resultFragment).commitAllowingStateLoss()
    }
}