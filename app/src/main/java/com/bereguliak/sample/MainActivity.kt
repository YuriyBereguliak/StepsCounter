package com.bereguliak.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bereguliak.stepcounter.StepCounterService
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    //region AppCompatActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initClickListeners()
    }
    //endregion

    //region Utility API
    private fun initClickListeners() {
        startServiceButton.setOnClickListener {
            StepCounterService.startStepCounterService(this)
        }
        stopServiceButton.setOnClickListener {
            StepCounterService.stopStepCounterService(this)
        }
    }
    //endregion
}
