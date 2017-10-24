package com.ysered.custombuttons.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.ysered.custombuttons.R
import com.ysered.custombuttons.utils.resolveAttribute


class CircleButton(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int
) : View(context, attrs, defStyleAttr) {

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context) : this(context, null)

    // defaults
    companion object {
        val ON_CLICK_OFFSET = 2f
        val DEFAULT_SHADOW_RADIUS = 20f
        val DEFAULT_SHADOW_Y_OFFSET = 10f
        val DEFAULT_BUTTON_BG_COLOR = Color.DKGRAY
        val DEFAULT_BUTTON_SHADOW_COLOR = Color.GRAY
    }

    // points and dimens
    private var centerX: Float = 0f
    private var centerY: Float = 0f
    private var radius: Float = 0f

    // shadow
    private var isShowShadow = true
    private var shadowY = DEFAULT_SHADOW_Y_OFFSET
    private val shadowRadius = DEFAULT_SHADOW_RADIUS

    // colors
    private val circleColor: Int
    private val shadowColor: Int

    private val circlePaint: Paint

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setBackgroundResource(context.resolveAttribute(android.R.attr.selectableItemBackgroundBorderless))
        }

        val a = context.obtainStyledAttributes(attrs, R.styleable.CircleButton)
        circleColor = a.getColor(R.styleable.CircleButton_circleColor, DEFAULT_BUTTON_BG_COLOR)
        shadowColor = a.getColor(R.styleable.CircleButton_shadowColor, DEFAULT_BUTTON_SHADOW_COLOR)
        a.recycle()

        circlePaint = Paint().apply {
            isAntiAlias = true
            color = circleColor
            style = Paint.Style.FILL_AND_STROKE
        }

        setLayerType(LAYER_TYPE_SOFTWARE, circlePaint)
        setOnTouchListener({ _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    isShowShadow = false
                    centerY += ON_CLICK_OFFSET
                    shadowY = DEFAULT_SHADOW_Y_OFFSET / 2f
                    invalidate()
                    true
                }
                MotionEvent.ACTION_UP -> {
                    isShowShadow = true
                    centerY -= ON_CLICK_OFFSET
                    shadowY = DEFAULT_SHADOW_Y_OFFSET
                    invalidate()
                    true
                }
                else -> false
            }
        })
    }

    override fun onSizeChanged(newWidth: Int, newHeight: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(newWidth, newHeight, oldWidth, oldHeight)
        centerX = newWidth / 2f
        centerY = newWidth / 2f
        radius = newWidth / 2f - DEFAULT_SHADOW_RADIUS * 2
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        circlePaint.setShadowLayer(shadowRadius, 0f, shadowY, shadowColor)
        canvas?.drawCircle(centerX, centerY, radius, circlePaint)
    }
}
