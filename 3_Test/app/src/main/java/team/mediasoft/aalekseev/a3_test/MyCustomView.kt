package team.mediasoft.aalekseev.a3_test

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import kotlin.math.pow
import kotlin.math.sqrt


class MyCustomView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val colors = listOf(
        Color.RED,
        Color.BLUE,
        Color.CYAN,
        Color.GREEN,
        Color.MAGENTA
    )

    private var colorIndex: Int = 0

    private var xStart: Float = 0f
    private var yStart: Float = 0f
    private var radius: Float = 0f

    private val paint = Paint()

    private var animator: ValueAnimator? = null

    init {
        paint.color = Color.RED
    }

    override fun onTouchEvent(event: MotionEvent): Boolean = when (event.action) {
        MotionEvent.ACTION_DOWN -> true
        MotionEvent.ACTION_UP -> {
            startCircleAnimation(
                event.x,
                event.y
            )
            true
        }
        else -> super.onTouchEvent(event)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawCircle(xStart, yStart, radius, paint)
        Log.d("MyCustomView", "onDraw")
    }

    override fun onDetachedFromWindow() {
        animator?.cancel()
        super.onDetachedFromWindow()
    }

    private fun startCircleAnimation(x: Float, y: Float) {
        xStart = x
        yStart = y

        val newColor = colorIndex++ % colors.size
        paint.color = colors[newColor]

        this.animator?.cancel()

        val animator = ValueAnimator.ofFloat(
            0f,
            sqrt(height.toFloat().pow(2f) + width.toFloat().pow(2f))
        )

        animator.addUpdateListener {
            val newRadius = it.animatedValue as Float
            radius = newRadius
            invalidate()
        }
        animator.addListener(object : Animator.AnimatorListener {

            override fun onAnimationEnd(animation: Animator?) {
                radius = 0f
            }

            override fun onAnimationStart(animation: Animator?) {}
            override fun onAnimationCancel(animation: Animator?) {}
            override fun onAnimationRepeat(animation: Animator?) {}
        })

        animator.start()

        this.animator = animator
    }
}