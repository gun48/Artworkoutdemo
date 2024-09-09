package artwork.kotlin

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import artwork.java.R

class Preview : AppCompatImageView {
    var w: Int = 0
    var h: Int = 0

    /**
     * @param context the context
     */
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        this.setImageResource(R.drawable.image31)
        //
    }

    public override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        this.w = w
        this.h = h
    }

    public override fun onDraw(canvasGlobal: Canvas) {
        super.onDraw(canvasGlobal)
    }
}
