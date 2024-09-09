package artwork.java;

import android.content.res.AssetManager;
import android.graphics.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class BrushManager {
    Random random = new Random();
    public void updatePaint(Paint paint){
        boolean useRandom = 1==App.getInstance().getInt(c.option2, 1);
        if (1==App.getInstance().getInt(c.option1, 1)){
            paint.setShader(getBitmapShader(names[useRandom ? random.nextInt(names.length):0]));
            paint.setColor(0xffffffff);
        }else{
            paint.setShader(null);
            paint.setColor(colors[useRandom ? random.nextInt(colors.length): 0]);
        }
    }
    private String[] names = {"0.png","1.png","2.png","3.png","4.png","5.png","6.png","7.png","8.png","9.png",};
    private int[] colors = {0xffff0000,0xff00ff00,0xff0000ff, 0xffffff00, 0xffff00ff, 0xff00ffff, 0xffff8800, 0xffff0088, 0xff88ff00, 0xffbbbbbb};

    private BitmapShader getBitmapShader(String name) {
        AssetManager assetManager = App.getInstance().getAssets();
        try {
            InputStream is = assetManager.open(name);
            Bitmap bitmap = BitmapFactory.decodeStream(is); //todo close is??
            Shader.TileMode tileMode = Shader.TileMode.REPEAT;
            BitmapShader bitmapShader = new BitmapShader(bitmap, tileMode, tileMode);
            return bitmapShader;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
