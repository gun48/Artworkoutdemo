package artwork.kotlin

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatImageView
import java.util.*
import artwork.java.App
import artwork.java.c

class GameView : AppCompatImageView {
    lateinit var canvas: Canvas
    var rect: RectF = RectF()
    lateinit var bitmap: Bitmap
    var bmpPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    var paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    var clearPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    var path: Path = Path()
    var sx: Float = 0f
    var sy: Float = 0f
    var penSize: Int = 10
    var paths: Vector<Path> = Vector()
    var currentPathId: Int = 0
    var needRedraw: Boolean = true
    lateinit var progressListener: ProgressListener
    lateinit var pathsListener: UpdatePathsListener
    var brushManager: BrushManager = BrushManager()
    var task: Task? = null
    /**
     * @param context the context
     */
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        progressListener = context as ProgressListener
        pathsListener = context as UpdatePathsListener
        paint.isAntiAlias = true
        paint.style = Paint.Style.STROKE
        paint.strokeJoin = Paint.Join.ROUND
        paint.strokeCap = Paint.Cap.ROUND
        paint.setPathEffect(CornerPathEffect(10.0f))
        clearPaint.color = 0xffffffff.toInt()
        updateBrush()
    }
    fun changeTask(t: Task) {
        task = t
        needRedraw = true
        invalidate()
    }
    public override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        try {
            super.onSizeChanged(w, h, oldw, oldh)
            App.getInstance().putInt(c.gamew, w)
            App.getInstance().putInt(c.gameh, h)
            bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
            canvas = Canvas(bitmap)
            rect[0.0f, 0.0f, w.toFloat()] = h.toFloat() // ???
        } catch (e2: Exception) {
            e2.printStackTrace()
        }
    }

    fun undo() {
        if (0 != currentPathId) {
            currentPathId--
            needRedraw = true
            invalidate()
        }
    }

    fun redo() {
        if (currentPathId != paths.size) {
            currentPathId++
            needRedraw = true
            invalidate()
        }
    }

    fun updateTips() {
        task?.let{
            it.makeTips()
            needRedraw = true
            invalidate()
        }
    }
    fun updateBrush() {
        brushManager.updatePaint(paint)
    }

    fun redraw() {
        needRedraw = false
        canvas.drawRect(rect, clearPaint)
        task?.draw(canvas)
        for (i in 0 until currentPathId) {
            canvas.drawPath(paths[i], paint)
        }
        pathsListener.updatePaths(paths.size, currentPathId)
    }
    public override fun onDraw(canvasGlobal: Canvas) {
        super.onDraw(canvas)
        try {
            if (needRedraw) redraw()
            canvas.drawPath(path, paint)
            canvasGlobal.drawBitmap(bitmap, null as Rect?, this.rect, bmpPaint)
        } catch (exc: Exception) {
            exc.printStackTrace()
        }
    }
    override fun onTouchEvent(motionEvent: MotionEvent): Boolean {
        val x = motionEvent.x
        val y = motionEvent.y
        val action = motionEvent.action
        if (action == 0) { // ACTION_DOWN pressed gesture has started, the motion contains the initial starting location.
            for (i in paths.size - 1 downTo currentPathId) paths.removeAt(i)
            path.reset()
            sx = x
            sy = y
            paint.strokeWidth = penSize.toFloat()
            brushManager.updatePaint(paint)
            path.moveTo(x, y)
        } else if (action == 1) { // ACTION_UP
            path.lineTo(sx, sy)
            canvas.drawPath(path, paint)
            paths.add(path)
            path = Path()
            pathsListener.updatePaths(paths.size, ++currentPathId)
        } else if (action != 2) {
            return false
        } else { /// 2 = ACTION_MOVE
            if (x!=sx && y!=sy) {
                path.quadTo(sx, sy, (sx + x) / 2.0f, (sy + y) / 2.0f)
                sx = x
                sy = y
                task?.updateProgress(x.toInt(), y.toInt())
                task?.let { progressListener.updateProgress(it.progress) }
            }
        }
        invalidate()
        return true
    }
}
