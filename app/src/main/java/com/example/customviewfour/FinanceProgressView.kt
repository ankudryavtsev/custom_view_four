package com.example.customviewfour

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt

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

    //TODO
    private val arcRect = RectF(0f, 0f, 500f, 500f)
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
            drawCircle(arcRect.centerX(), arcRect.centerY(), arcRect.centerX(), backgroundPaint)
            drawArc(arcRect, TOP_START_ANGLE, FULL_CIRCLE_ANGLE / max * progress, false, foregroundPaint)
            drawTextProgress()
        }
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
        private const val FULL_CIRCLE_ANGLE = 360f
        private const val TOP_START_ANGLE = -90f
    }
}