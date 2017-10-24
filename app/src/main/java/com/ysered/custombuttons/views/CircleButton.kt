package com.ysered.custombuttons.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.ysered.custombuttons.R
import com.ysered.custombuttons.utils.extensions.resolveAttribute
import com.ysered.custombuttons.utils.extensions.toPx


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
        private val DEFAULT_ICON_WIDTH_DP = 58
        private val DEFAULT_ICON_HEIGHT_DP = 58
    }

    // points and dimens
    private var centerX: Float = 0f
    private var centerY: Float = 0f
    private var radius: Float = 0f
    private var iconWidth: Float = 0f
    private var iconHeight: Float = 0f

    // shadow
    private var isShowShadow = true
    private var shadowY = DEFAULT_SHADOW_Y_OFFSET
    private val shadowRadius = DEFAULT_SHADOW_RADIUS

    // colors
    private val circleColor: Int
    private val shadowColor: Int

    private val circlePaint: Paint

    private var iconDrawable: Drawable? = null

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setBackgroundResource(context.resolveAttribute(android.R.attr.selectableItemBackgroundBorderless))
        }

        val a = context.obtainStyledAttributes(attrs, R.styleable.CircleButton, defStyleAttr, 0)
        circleColor = a.getColor(R.styleable.CircleButton_circleColor, DEFAULT_BUTTON_BG_COLOR)
        shadowColor = a.getColor(R.styleable.CircleButton_shadowColor, DEFAULT_BUTTON_SHADOW_COLOR)
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
        centerY = newHeight / 2f
        radius = newWidth / 2f - DEFAULT_SHADOW_RADIUS * 2
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        circlePaint.setShadowLayer(shadowRadius, 0f, shadowY, shadowColor)
        canvas?.drawCircle(centerX, centerY, radius, circlePaint)
        canvas?.translate(centerX - iconWidth / 2, centerY - iconHeight / 2)
        iconDrawable?.draw(canvas)
        //canvas?.translate(0f, 0f)
    }
}
