package artwork.kotlin

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

class ProgressBar : AppCompatImageView {
    private var w = 0f
    private var h = 0f
    private var progress = 0f

    private var paint: Paint

    constructor(context: Context) : super(context) {
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
    }

    override fun onSizeChanged(w: Int, h: Int, ow: Int, oh: Int) {
        super.onSizeChanged(w, h, ow, oh)
        this.w = w.toFloat()
        this.h = h.toFloat()
    }

    fun setProgress(i: Int) {
        this.progress = i.toFloat()
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        paint.color = -0xff0100
        canvas.drawRect(0f, 0f, w * progress / 100, h, paint)
        super.onDraw(canvas)
    }
}
