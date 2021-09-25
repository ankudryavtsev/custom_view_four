package com.example.customviewfour

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.annotation.ColorInt
import kotlin.math.max
import kotlin.math.min

class FinanceProgressView(
    context: Context,
    attributeSet: AttributeSet?
) : View(context, attributeSet) {

    private var progress: Int

    @ColorInt
    private var backgroundArcColor: Int

    @ColorInt
    private var foregroundArcColor: Int

    private var max: Int

    private var strokeWidth: Float

    private var textSize: Float

    private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
    }
    private val foregroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
    }
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val arcRect = RectF()
    private val textRect = Rect()

    init {
        val typedArray = context.theme.obtainStyledAttributes(
            attributeSet,
            R.styleable.FinanceProgressView,
            R.attr.financeProgressDefaultAttr,
            0
        )

        try {
            progress = typedArray.getInt(R.styleable.FinanceProgressView_android_progress, 0)
            backgroundArcColor =
                typedArray.getColor(R.styleable.FinanceProgressView_backgroundArcColor, 0)
            foregroundArcColor =
                typedArray.getColor(R.styleable.FinanceProgressView_foregroundArcColor, 0)
            max = typedArray.getInt(R.styleable.FinanceProgressView_max, 0)
            strokeWidth =
                typedArray.getDimension(R.styleable.FinanceProgressView_strokeWidth, 0F)
            textSize = typedArray.getDimension(R.styleable.FinanceProgressView_textSize, 0F)
        } finally {
            typedArray.recycle()
        }
        configurePaints()
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.apply {
            translate(strokeWidth / 2, strokeWidth / 2)
            drawArc(arcRect, 0F, FULL_CIRCLE_ANGLE, false, backgroundPaint)
            drawArc(
                arcRect,
                TOP_START_ANGLE,
                FULL_CIRCLE_ANGLE / max * progress,
                false,
                foregroundPaint
            )
            drawTextProgress()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        Log.d(TAG, "onSizeChanged() called with: w = $w, h = $h, oldw = $oldw, oldh = $oldh")
        val size = min(w, h) - strokeWidth
        arcRect.set(
            paddingLeft.toFloat(),
            paddingTop.toFloat(),
            size - paddingRight,
            size - paddingBottom
        )
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        Log.d(
            TAG,
            "onMeasure() called with: widthMeasureSpec = ${MeasureSpec.toString(widthMeasureSpec)}, " +
                    "heightMeasureSpec = ${MeasureSpec.toString(heightMeasureSpec)}"
        )
        val s = "$max %"
        textPaint.getTextBounds(s, 0, s.length, textRect)

        val requestedWidth =
            max(textRect.width() + paddingLeft + paddingRight, suggestedMinimumWidth)
        val requestedHeight =
            max(textRect.height() + paddingTop + paddingBottom, suggestedMinimumHeight)

        val requestedSize = (max(requestedWidth, requestedHeight) + strokeWidth * 2).toInt()

        setMeasuredDimension(
            resolveSizeAndState(requestedSize, widthMeasureSpec, 0),
            resolveSizeAndState(requestedSize, heightMeasureSpec, 0)
        )
    }

    private fun Canvas.drawTextProgress() {
        val s = "$progress %"
        textPaint.getTextBounds(s, 0, s.length, textRect)
        drawText(
            s,
            arcRect.centerX() - textRect.centerX(),
            arcRect.centerY() - textRect.centerY(),
            textPaint
        )
    }

    fun setProgress(progress: Int) {
        this.progress = progress
        invalidate()
    }

    private fun configurePaints() {
        backgroundPaint.strokeWidth = strokeWidth
        backgroundPaint.color = backgroundArcColor
        foregroundPaint.strokeWidth = strokeWidth
        foregroundPaint.color = foregroundArcColor
        textPaint.color = foregroundArcColor
        textPaint.textSize = textSize
    }

    companion object {
        private const val TAG = "FinanceProgressView"
        private const val FULL_CIRCLE_ANGLE = 360f
        private const val TOP_START_ANGLE = -90f
    }
}