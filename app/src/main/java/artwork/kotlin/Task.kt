package artwork.kotlin

import android.graphics.*
import java.util.*
import kotlin.math.sqrt
import artwork.java.App
import artwork.java.c

class Task {
    var paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    var pathTips: Path = Path()
    var path3: Path = Path()
    var penSize: Int = 10
    var color: Int = 0xff8080ff.toInt()
    val hintColor = 0xff8080ff.toInt()
    val points: Array<Point>
    lateinit var visitedArr: BooleanArray
    var tipsCenters: Vector<Point> = Vector()
    var visited: Int = 0
    var len: Float = 0f
    var koef: Float = 0f
    var taskResId = 0;
    var inited: Boolean = false
/// here may be others params
    constructor(taskSvg: String, taskResId: Int) {
        this.taskResId = taskResId
        paint.isAntiAlias = true
        paint.style = Paint.Style.STROKE
        paint.strokeJoin = Paint.Join.ROUND
        paint.strokeCap = Paint.Cap.ROUND
        paint.setPathEffect(CornerPathEffect(10.0f))
        points = SVGManager(taskSvg).points
    }
    private fun init(){
        inited = true
        val w = App.getInstance().getInt(c.gamew, 0)
        val h = App.getInstance().getInt(c.gameh, 0)
        val sz = (if (w > h) h else w) * 8 / 10
        val x = if (w > h) (w - sz) / 2 else sz / 10
        val y = if (h > w) (h - sz) / 2 else sz / 10
        koef = sz / 100f
 //
        for (i in points.indices) {
            val p = points[i]
            p.x = x + ((p.x + 50) * koef).toInt()
            p.y = y + ((p.y + 50) * koef).toInt()
        }
        for (i in points.indices) {
            path3.addCircle(points[i].x.toFloat(), points[i].y.toFloat(), 10f, Path.Direction.CW)
        }
        makeTips()
    }
    fun makeTips() {
        pathTips.reset()
        val tmpCenters = Vector<Point>()
        for (i in 0 until points.size - 1) makeTip(points[i], points[i + 1], tmpCenters)
        makeTip(points[points.size - 1], points[0], tmpCenters)
        visitedArr = BooleanArray(tmpCenters.size)
        tipsCenters = tmpCenters
        visited = 0
    }
    fun makeTip(p1: Point, p2: Point, tmpCenters: Vector<Point>) {
        val x = p1.x
        val y = p1.y
        val x2 = p2.x - x
        val y2 = p2.y - y
        len = sqrt((x2 * x2 + y2 * y2).toDouble()).toFloat()
        paint.strokeWidth = penSize.toFloat() // todo move

        var cutlen = App.getInstance().getInt(c.seek1, 45).toFloat()
        if (cutlen < 10) cutlen = 10f
        val cuts = len / cutlen
        //path2.addCircle(x+x2*cutlen/2/len, y+y2*cutlen/2/len, cutlen/2, Path.Direction.CW);
        //path2.addCircle(x+x2*(len-cutlen/2)/len, y+y2*(len-cutlen/2)/len, cutlen/2, Path.Direction.CW);
        val px = x + x2 * cutlen / 2 / len
        val py = y + y2 * cutlen / 2 / len
        val px2 = x2 * (len - cutlen / 2) / len
        val py2 = y2 * (len - cutlen / 2) / len

        val pl = len - cutlen
        val k = pl / cuts
        var i = 0
        while (i < cuts) {
            val p = Point((px + px2 * i * k / pl).toInt(), (py + py2 * i * k / pl).toInt())
            tmpCenters.add(p)
            pathTips.addCircle(p.x.toFloat(), p.y.toFloat(), cutlen / 2, Path.Direction.CW)
            i++
        }
    }
    fun draw(canvasGlobal: Canvas) {
        if (!inited)
            init()
        paint.color = color
        paint.strokeWidth = penSize.toFloat()
        canvasGlobal.drawPath(path3, paint)
        if (1 == App.getInstance().getInt(c.option3, 1)) {
            paint.color = hintColor
            paint.strokeWidth = 5f
            canvasGlobal.drawPath(pathTips, paint)
        }
    }
    fun updateProgress(x: Int, y: Int) : Int{
        val sz = tipsCenters.size
        var cutlen = App.getInstance().getInt(c.seek1, 45).toFloat()
        if (cutlen < 10) cutlen = 10f
        cutlen = cutlen * cutlen / 4
        for (i in 0 until sz) {
            if (!visitedArr[i]) {
                val center = tipsCenters[i]
                val dx = x - center.x
                val dy = y - center.y
                val lensq = dx * dx + dy * dy
                if (lensq < cutlen) {
                    visitedArr[i] = true
                    visited++
                }
            }
        }
        return visited * 100 / visitedArr.size
    }
}
