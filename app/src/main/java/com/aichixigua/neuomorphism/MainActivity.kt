package com.aichixigua.neuomorphism

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SeekBar
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private lateinit var customView: NeuomorphismView

    private lateinit var scaleText: TextView
    private lateinit var radiusText: TextView
    private lateinit var lightAlpha: TextView
    private lateinit var lightWidth: TextView

    private lateinit var seek: SeekBar
    private lateinit var radiusSeek: SeekBar
    private lateinit var lightAlphaSeek: SeekBar
    private lateinit var lightWidthSeek: SeekBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        startAnim()
    }

    private fun startAnim() {
        lightWidthSeek.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val showMsg = "光效宽度$progress"
                lightWidth.text = showMsg
                customView.setLightWidth(progress.toFloat())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })

        lightAlphaSeek.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val float = 1 - progress.toFloat() / 100
                val showMsg = "光效透明度$float"
                lightAlpha.text = showMsg
                customView.setLightAlpha(float)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })
        seek.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val showMsg = "缩放$progress"
                scaleText.text = showMsg
                customView.setWidthPadding(progress.toFloat())
                customView.setHeightPadding(progress.toFloat())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })
        radiusSeek.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val showMsg = "圆角$progress"
                radiusText.text = showMsg
                customView.setRadius(progress.toFloat())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
    }

    private fun initView() {
        customView = findViewById(R.id.NEView)

        seek = findViewById(R.id.handlePaddingSeek)
        radiusSeek = findViewById(R.id.handleRadiusSeek)
        lightAlphaSeek = findViewById(R.id.lightAlphaSeek)
        lightWidthSeek = findViewById(R.id.lightWidthSeek)

        scaleText = findViewById(R.id.scaleText)
        radiusText = findViewById(R.id.radiusText)
        lightAlpha = findViewById(R.id.lightAlpha)
        lightWidth = findViewById(R.id.lightWidth)
    }
}