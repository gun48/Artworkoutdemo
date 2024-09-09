package artwork.java;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

public class ProgressBar extends androidx.appcompat.widget.AppCompatImageView {
    private int w, h, progress = 0;

    private Paint paint;
    public ProgressBar(Context context) {
        super(context);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }
    public ProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }
    @Override
    protected void onSizeChanged(int w, int h, int ow, int oh) {
        super.onSizeChanged(w, h, ow, oh);
        this.w = w;
        this.h = h;
    }
    public void setProgress(int i){
        this.progress = i;
        invalidate();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        paint.setColor(0xff00ff00);
        canvas.drawRect(0,0, w*progress/100, h, paint);
        super.onDraw(canvas);
    }
}
