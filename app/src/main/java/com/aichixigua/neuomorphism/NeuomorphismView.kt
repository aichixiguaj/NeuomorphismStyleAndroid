package com.aichixigua.neuomorphism

import android.content.Context
import android.graphics.BlurMaskFilter
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange

/**
 * @author : 爱吃西瓜
 * @description : 轻拟物风格的样式
 * @email : aichixiguaj@qq.com
 * @date : 2021/6/22 14:59
 */
class NeuomorphismView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    // 可变的宽高
    private var viewHeight = 0f
    private var viewWidth = 0f

    // 原始宽高
    private var defaultBodyHeight = 0f
    private var defaultBodyWidth = 0f

    // padding 值
    private var widthPadding = 80f
    private var heightPadding = 80f

    // 圆角弧度
    private var radius = 150f

    // 外部宽度
    private var outsideWidth = 80f

    // 视图的中心点
    private var viewCenterPointX = 0f
    private var viewCenterPointY = 0f

    // 宽高缩放比例
    private var widthScaling = 0f
    private var heightScaling = 0f

    // 偏移值
    private var offsetValueX = 0f
    private var offsetValueY = 0f

    // 光效透明度
    private var lightAlpha = 1f

    // 画笔
    private var paint = Paint(Paint.ANTI_ALIAS_FLAG)

    // 模糊画笔
    private var blurPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        maskFilter = BlurMaskFilter(radius, BlurMaskFilter.Blur.NORMAL)
    }

    init {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        viewHeight = MeasureSpec.getSize(heightMeasureSpec).toFloat()
        viewWidth = MeasureSpec.getSize(widthMeasureSpec).toFloat()

        // 获取视图的中心点
        viewCenterPointX = viewWidth / 2
        viewCenterPointY = viewHeight / 2

        // 获取主体初始的长宽
        defaultBodyHeight = getBodyRadius(true) * 2
        defaultBodyWidth = getBodyRadius(false) * 2

        // 确定视图大小
        setMeasuredDimension(viewWidth.toInt(), viewHeight.toInt())
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            if (!checkWHIsMin(true) || !checkWHIsMin(false)) {
                drawBody(it)
            }
        }
    }

    /**
     *  设置光效的透明度
     */
    fun setLightAlpha(@FloatRange(from = 0.0, to = 1.0) a: Float) {
        this.lightAlpha = a
        invalidate()
    }

    /**
     *  外部宽度
     */
    fun setLightWidth(p: Float) {
        this.outsideWidth = p
        invalidate()
    }

    /**
     *  设置左右内边距
     */
    fun setWidthPadding(p: Float) {
        this.widthPadding = p
        invalidate()
        offsetLightValue()
    }

    /**
     *  设置上下内边距
     */
    fun setHeightPadding(p: Float) {
        this.heightPadding = p
        invalidate()
        offsetLightValue()
    }

    /**
     *  设置圆角
     */
    fun setRadius(r: Float) {
        this.radius = r
        invalidate()
    }

    /**
     *  偏移光的位置
     *  放大视图缩小视图光向内部偏移
     */
    private fun offsetLightValue() {
        // 宽 高 缩放比例
        widthScaling = (getBodyRadius(false) * 2) / defaultBodyWidth
        heightScaling = (getBodyRadius(false) * 2) / defaultBodyHeight

        // 修改外部半径比例
        if (widthScaling >= 0) {
            offsetValueX = outsideWidth * (1 - widthScaling)
        }
        if (heightScaling >= 0) {
            offsetValueY = outsideWidth * (1 - widthScaling)
        }
        invalidate()
    }


    /**
     *  绘制主体
     */
    private fun drawBody(canvas: Canvas) {
        // 绘制高光
        drawRoundRect(
            canvas, Color.WHITE, lightAlpha, true,
            widthPadding + offsetValueX,
            heightPadding + offsetValueY,
            viewWidth - (outsideWidth * 2) - widthPadding + offsetValueY,
            viewHeight - (outsideWidth * 2) - heightPadding + offsetValueY
        )

        // 绘制暗光
        drawRoundRect(
            canvas, Color.parseColor("#A9A9A9"), lightAlpha, true,
            widthPadding + (outsideWidth * 2) - offsetValueX,
            heightPadding + (outsideWidth * 2) - offsetValueY,
            viewWidth - widthPadding - offsetValueX,
            viewHeight - heightPadding - offsetValueY
        )

        // 绘制主体
        drawRoundRect(
            canvas, Color.parseColor("#D3D3D3"), 1f, false,
            outsideWidth + widthPadding,
            outsideWidth + heightPadding,
            viewWidth - outsideWidth - widthPadding,
            viewHeight - outsideWidth - heightPadding
        )
    }

    /**
     *  画圆角矩形
     */
    private fun drawRoundRect(
        canvas: Canvas,
        @ColorInt color: Int,
        @FloatRange(from = 0.0, to = 1.0) alpha: Float,
        isLight: Boolean,
        left: Float,
        top: Float,
        right: Float,
        bottom: Float
    ) {
        if (isLight) {
            // 光效画笔
            setPaintColorAlpha(blurPaint, color, alpha)
        } else {
            // 主体画笔
            setPaintColorAlpha(paint, color, alpha)
        }

        // 绘制圆角矩形
        canvas.drawRoundRect(
            left, top, right, bottom,
            radius, radius,
            if (isLight) blurPaint else paint
        )
    }

    /**
     *  设置画笔的颜色和透明度
     */
    private fun setPaintColorAlpha(
        p: Paint,
        @ColorInt color: Int,
        @FloatRange(from = 0.0, to = 1.0) alpha: Float
    ) {
        p.let {
            it.color = color
            it.alpha = (alpha * 255).toInt()
        }
    }

    /**
     *  确定宽高是否已经变为0
     */
    private fun checkWHIsMin(isHeight: Boolean): Boolean {
        return getBodyRadius(isHeight) < 0
    }

    /**
     * 获取默认主体的宽高
     *  @param isHeight  true ：高 、 false ：宽
     */
    private fun getBodyRadius(isHeight: Boolean) = if (isHeight) {
        viewWidth - (outsideWidth * 2) - (widthPadding * 2)
    } else {
        viewHeight - (outsideWidth * 2) - (heightPadding * 2)
    }


}