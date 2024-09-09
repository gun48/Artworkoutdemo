package artwork.kotlin

import android.graphics.BitmapFactory
import android.graphics.BitmapShader
import android.graphics.Paint
import android.graphics.Shader.TileMode
import java.io.IOException
import java.util.*
import artwork.java.App
import artwork.java.c

class BrushManager {
    var random: Random = Random()
    fun updatePaint(paint: Paint) {
        val useRandom = 1 == App.getInstance().getInt(c.option2, 1)
        if (1 == App.getInstance().getInt(c.option1, 1)) {
            paint.setShader(getBitmapShader(names[if (useRandom) random.nextInt(names.size) else 0]))
            paint.color = -0x1
        } else {
            paint.setShader(null)
            paint.color = colors[if (useRandom) random.nextInt(colors.size) else 0]
        }
    }

    private val names =
        arrayOf("0.png", "1.png", "2.png", "3.png", "4.png", "5.png", "6.png", "7.png", "8.png", "9.png")
    private val colors =
        intArrayOf(0xffff0000.toInt(),0xff00ff00.toInt(),0xff0000ff.toInt(), 0xffffff00.toInt(), 0xffff00ff.toInt(),
            0xff00ffff.toInt(), 0xffff8800.toInt(), 0xffff0088.toInt(), 0xff88ff00.toInt(), 0xffbbbbbb.toInt() )

    private fun getBitmapShader(name: String): BitmapShader? {
        val assetManager = App.getInstance().assets
        try {
            val bitmap = BitmapFactory.decodeStream(assetManager.open(name)) //todo close is??
            val tileMode = TileMode.REPEAT
            val bitmapShader = BitmapShader(bitmap, tileMode, tileMode)
            return bitmapShader
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }
} /* public class BrushManager {
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
* */
