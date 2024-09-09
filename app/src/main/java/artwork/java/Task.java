package artwork.java;

import android.graphics.Canvas;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import java.util.Vector;
import android.graphics.Point;

public class Task {
    public Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    public Path pathTips = new Path(), path3 = new Path();
    public int penSize = 10;
    public int color = 0xff00ff00;
    Point[] points;
    Vector<Point> tipsCenters = new Vector<>();
    boolean[] visitedArr;
    int visited = 0;
    float len, koef;
    int taskResId = 0;
    boolean inited = false;

    public Task(String taskSvg, int taskResId){
        this.taskResId = taskResId;
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setPathEffect(new CornerPathEffect(10.0f));
        points = new SVGManager(taskSvg).points;
    }
    public void init() {
        inited = true;
        int w = App.getInstance().getInt(c.gamew, 0);
        int h = App.getInstance().getInt(c.gameh, 0);
        int sz = (w > h ? h : w) * 8 / 10;
        int x = w > h ? (w - sz) / 2 : sz / 10;
        int y = h > w ? (h - sz) / 2 : sz / 10;
        koef = sz / 100f;

        for (int i=0; i<points.length; i++){
            Point p = points[i];
            p.x = x + (int) ((p.x + 50) * koef);
            p.y = y + (int) ((p.y + 50) * koef);
        }
        for (int i=0; i<points.length; i++){
            path3.addCircle(points[i].x, points[i].y, 10, Path.Direction.CW);
        }
        makeTips();
    }
    public void makeTips(){
        pathTips.reset();
        Vector<Point> tmpCenters = new Vector<>();
        for (int i=0; i<points.length-1; i++)
            makeTip(points[i], points[i+1], tmpCenters);
        makeTip(points[points.length-1], points[0], tmpCenters);
        visitedArr = new boolean[tmpCenters.size()];
        tipsCenters = tmpCenters;
        visited = 0;
    }
    public void makeTip(Point p1, Point p2, Vector<Point> tmpCenters){
        int x = p1.x, y = p1.y, x2 = p2.x-x, y2 = p2.y-y;
        len = (float) Math.sqrt(x2*x2 + y2*y2);
        paint.setStrokeWidth((float) penSize); // todo move

        float cutlen = App.getInstance().getInt(c.seek1, 45);
        if (cutlen<10)
            cutlen = 10;
        float cuts = len/cutlen;
        //path2.addCircle(x+x2*cutlen/2/len, y+y2*cutlen/2/len, cutlen/2, Path.Direction.CW);
        //path2.addCircle(x+x2*(len-cutlen/2)/len, y+y2*(len-cutlen/2)/len, cutlen/2, Path.Direction.CW);
        float px = x+x2*cutlen/2/len;
        float py = y+y2*cutlen/2/len;
        float px2 = x2*(len-cutlen/2)/len;
        float py2 = y2*(len-cutlen/2)/len;

        float pl = len - cutlen;
        float k = pl/cuts;
        for (int i=0; i<cuts; i++){
            Point p = new Point((int)(px+px2*i*k/pl), (int)(py+py2*i*k/pl));
            tmpCenters.add(p);
            pathTips.addCircle(p.x, p.y, cutlen/2, Path.Direction.CW);
        }
    }
    public void draw(Canvas canvasGlobal){
        if (!inited)
            init();
        paint.setColor(color);
        paint.setStrokeWidth((float) penSize);
        canvasGlobal.drawPath(path3, paint);
        if (1==App.getInstance().getInt(c.option3, 1)){
            paint.setColor(0xffff0000);
            paint.setStrokeWidth((float) 5);
            canvasGlobal.drawPath(pathTips, paint);
        }
    }
    public void updateProgress(int x, int y){
        int sz = tipsCenters.size();
        float cutlen = App.getInstance().getInt(c.seek1, 45);
        if (cutlen<10)
            cutlen = 10;
        cutlen = cutlen*cutlen/4;
        for (int i=0; i<sz; i++){
            if (!visitedArr[i]){
                Point center = tipsCenters.get(i);
                int dx = x - center.x;
                int dy = y - center.y;
                int lensq = dx*dx + dy*dy;
                if (lensq<cutlen){
                    visitedArr[i] = true;
                    visited++;
                }
            }
        }
    }
    public int getProgress(){
        return visited*100/visitedArr.length;
    }
}
