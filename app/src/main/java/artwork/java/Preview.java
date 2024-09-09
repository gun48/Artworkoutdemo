package artwork.java;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import artwork.java.R;

public class Preview extends androidx.appcompat.widget.AppCompatImageView {
    public int w, h;

    /**
     * @param context the context
     */
    public Preview(Context context) {
        super(context);
    }
    public Preview(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setImageResource(R.drawable.image31);
        //
    }
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.w = w; this.h = h;
    }


    @Override
    public void onDraw(Canvas canvasGlobal) {
        super.onDraw(canvasGlobal);
    }

}
