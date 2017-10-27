package com.ysered.custombuttons.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.DecelerateInterpolator
import com.ysered.custombuttons.R
import com.ysered.custombuttons.utils.extensions.animateColorChange
import com.ysered.custombuttons.utils.extensions.toPx
import com.ysered.custombuttons.utils.isInsideCircle


class CircleButton(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int
) : View(context, attrs, defStyleAttr) {

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context) : this(context, null)

    // defaults
    companion object {
        private val ON_CLICK_OFFSET = 2f
        private val DEFAULT_SHADOW_RADIUS = 20f
        private val DEFAULT_SHADOW_Y_OFFSET = 10f
        private val DEFAULT_BUTTON_BG_COLOR = Color.DKGRAY
        private val DEFAULT_BUTTON_SHADOW_COLOR = Color.GRAY
        private val DEFAULT_ICON_WIDTH_DP = 48
        private val DEFAULT_ICON_HEIGHT_DP = 48
        private val DEFAULT_CIRCLE_COLOR_ANIM_DURATION_MS = 600L
    }

    // points and dimens
    private var circleX: Float = 0f
    private var circleY: Float = 0f
    private var circleRadius: Float = 0f
    private var iconWidth: Float = 0f
    private var iconHeight: Float = 0f

    // shadow
    private var isShowShadow = true
    private var shadowY = DEFAULT_SHADOW_Y_OFFSET
    private var shadowRadius = DEFAULT_SHADOW_RADIUS

    // colors
    private var circleColor: Int = DEFAULT_BUTTON_BG_COLOR
    private val circleColorSelected: Int
    private val shadowColor: Int

    private val circlePaint: Paint
    private var iconDrawable: Drawable? = null

    private val animationInterpolator = DecelerateInterpolator()

    private var lastX: Float = 0f
    private var lastY: Float = 0f
    private var isClicked = false

    var onClickListener: (circleButton: CircleButton) -> Unit? = {}

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.CircleButton, defStyleAttr, 0)
        circleColor = a.getColor(R.styleable.CircleButton_circleColor, DEFAULT_BUTTON_BG_COLOR)
        circleColorSelected = a.getColor(R.styleable.CircleButton_circleColorSelected, DEFAULT_BUTTON_BG_COLOR)
        shadowColor = a.getColor(R.styleable.CircleButton_shadowColor, DEFAULT_BUTTON_SHADOW_COLOR)
        shadowRadius = a.getDimension(R.styleable.CircleButton_shadowRadius, DEFAULT_SHADOW_RADIUS)
        a.getResourceId(R.styleable.CircleButton_iconResource, -1).let { resource ->
            if (resource != -1) {
                iconWidth = context.toPx(DEFAULT_ICON_WIDTH_DP)
                iconHeight = context.toPx(DEFAULT_ICON_HEIGHT_DP)
                iconDrawable = ContextCompat.getDrawable(context, resource).apply {
                    setBounds(0, 0, iconWidth.toInt(), iconHeight.toInt())
                }
            }
        }
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
                    lastX = event.x
                    lastY = event.y
                    if (isInsideCircle(lastX, lastY, circleX, circleY, circleRadius)) {
                        isClicked = true
                        isShowShadow = false
                        shadowRadius *= 2
                        circleY += ON_CLICK_OFFSET
                        shadowY = DEFAULT_SHADOW_Y_OFFSET * 2f
                        animateCircleColor(circleColor, circleColorSelected)
                    }
                    true
                }
                MotionEvent.ACTION_UP -> {
                    lastX = event.x
                    lastY = event.y
                    if (isClicked) {
                        isClicked = false
                        isShowShadow = true
                        shadowRadius /= 2
                        circleY -= ON_CLICK_OFFSET
                        shadowY = DEFAULT_SHADOW_Y_OFFSET
                        animateCircleColor(circleColorSelected, circleColor)
                        if (isInsideCircle(lastX, lastY, circleX, circleY, circleRadius))
                            onClickListener(this)
                    }
                    true
                }
                else -> false
            }
        })
    }

    override fun onSizeChanged(newWidth: Int, newHeight: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(newWidth, newHeight, oldWidth, oldHeight)
        circleX = newWidth / 2f
        circleY = newHeight / 2f
        circleRadius = newWidth / 2f - (DEFAULT_SHADOW_Y_OFFSET * 3 + DEFAULT_SHADOW_RADIUS)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        circlePaint.setShadowLayer(shadowRadius, 0f, shadowY, shadowColor)
        canvas?.drawCircle(circleX, circleY, circleRadius, circlePaint)
        canvas?.translate(circleX - iconWidth / 2, circleY - iconHeight / 2)
        iconDrawable?.draw(canvas)
    }

    private fun animateCircleColor(startColor: Int, endColor: Int) {
        animateColorChange(startColor, endColor, animationInterpolator, DEFAULT_CIRCLE_COLOR_ANIM_DURATION_MS) { color ->
            circlePaint.color = color
        }
    }
}
