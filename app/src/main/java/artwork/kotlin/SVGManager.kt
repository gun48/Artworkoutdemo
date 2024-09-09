package artwork.kotlin

import android.graphics.Point
import kotlin.collections.ArrayList
import artwork.java.App
import artwork.java.MyXml

class SVGManager {
    lateinit var points: Array<Point>

    constructor(name: String) {
        val assetManager = App.getInstance().assets
        try {
            val root = MyXml.parse(assetManager.open(name))
            val poli = root.findByType("polygon")
            val pnts = poli.gets("points")
            val pnts2 = pnts.trim().split(' ')// pairs of numbers, with ',' : "0,-42.5"
            var pnts3 = ArrayList<Point>()
            for (i in pnts2.indices) {
                val pair = pnts2[i].split(',')
                pnts3.add(Point(pair[0].toFloat().toInt(), pair[1].toFloat().toInt()))// "42.5".toInt() will exception
            }
            points = pnts3.toTypedArray()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
} /*
public class SVGManager {
    Point[] points;
    public SVGManager(String name){
        init(name);
    }
    public void init(String name) {
        AssetManager assetManager = App.getInstance().getAssets();
        try {
            InputStream is = assetManager.open(name);
            MyXml root = MyXml.parse(is);
            MyXml poli = root.findByType("polygon");
            String pnts = poli.gets("points");
            String[] pnts2 = pnts.split(" ");// pairs of numbers, with ',' : "0,-42.5"
            points = new Point[pnts2.length];
            for (int i=0; i<pnts2.length; i++){
                String[] pair = pnts2[i].split(",");
                points[i] = new Point((int) Float.parseFloat(pair[0]), (int) Float.parseFloat(pair[1]));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
 */
