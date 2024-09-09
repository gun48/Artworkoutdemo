package artwork.java;

import android.content.Context;

import android.graphics.*;
import android.util.AttributeSet;
import android.view.MotionEvent;
import artwork.kotlin.ProgressListener;
import artwork.kotlin.UpdatePathsListener;

import java.util.Vector;

public class GameView extends androidx.appcompat.widget.AppCompatImageView {
    public static Canvas canvas;
    public RectF rect = new RectF();
    public Bitmap bitmap;
    public Paint paintBmp;
    public Paint paint;
    public Paint clearPaint;
    public Path path;
    public float sx;
    public float sy;
    public int penSize = 10;
    public Vector<Path> paths = new Vector<>();
    public int currentPathId = 0;
    boolean needRedraw = true;
    ProgressListener progressListener;
    UpdatePathsListener pathsListener;
    BrushManager brushManager = new BrushManager();
    Task task = null;
    /**
     * @param context the context
     */
    public GameView(Context context) {
        super(context);
    }
    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        progressListener = (ProgressListener)context;
        pathsListener = (UpdatePathsListener)context;
        path = new Path();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setPathEffect(new CornerPathEffect(10.0f));
        clearPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintBmp = new Paint(Paint.ANTI_ALIAS_FLAG); // ANTI_ALIAS_FLAG DITHER_FLAG
        clearPaint.setColor(0xffffffff);
        updateBrush();
        //
    }
    public void changeTask(Task t) {
        task = t;
        needRedraw = true;
        invalidate();
    }
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        try {
            super.onSizeChanged(w, h, oldw, oldh);
            App.getInstance().putInt(c.gamew, w);
            App.getInstance().putInt(c.gameh, h);
            bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            canvas = new Canvas(bitmap);
            rect.set(0.0f, 0.0f, (float) w, (float) h);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }
    public void undo(){
        currentPathId--;
        needRedraw = true;invalidate();
    }
    public void redo(){
        currentPathId++;
        needRedraw = true;invalidate();
    }
    public void updateTips(){
        task.makeTips();
        needRedraw = true;invalidate();
    }
    public void updateBrush(){
        brushManager.updatePaint(paint);
    }

    public void redraw(){
        needRedraw = false;
        canvas.drawRect(rect, clearPaint);
        task.draw(canvas);
        for (int i=0; i<currentPathId; i++){
            canvas.drawPath(paths.get(i), paint);
        }
        pathsListener.updatePaths(paths.size(), currentPathId);
    }
    @Override
    public void onDraw(Canvas canvasGlobal) {
        super.onDraw(canvas);
        try {
            if (needRedraw)
                redraw();
            canvas.drawPath(path, paint);
            canvasGlobal.drawBitmap(this.bitmap, (Rect) null, this.rect, paintBmp);
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }
    public boolean onTouchEvent(MotionEvent motionEvent) {
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        int action = motionEvent.getAction();
        if (action == 0) { // ACTION_DOWN pressed gesture has started, the motion contains the initial starting location.
            for (int i=paths.size()-1;i>=currentPathId; i--)
                paths.remove(i);
            path.reset();
            sx = x;
            sy = y;
            paint.setStrokeWidth((float) penSize);
            brushManager.updatePaint(paint);
            path.moveTo(x, y);

        } else if (action == 1) { // ACTION_UP
            path.lineTo(sx, sy);
            canvas.drawPath(path, paint);
            paths.add(path);
            path = new Path();
            pathsListener.updatePaths(paths.size(), ++currentPathId);
        } else if (action != 2) {
            return false;
        } else { /// 2 = ACTION_MOVE
            if (Math.abs(x - sx) >= 0.0f || Math.abs(y - sy) >= 0.0f) {
                path.quadTo(sx, sy, (sx + x) / 2.0f, (sy + y) / 2.0f);
                sx = x;
                sy = y;
                task.updateProgress((int)x, (int)y);
                progressListener.updateProgress(task.getProgress());
            }
        }
        invalidate();
        return true;
    }
}

