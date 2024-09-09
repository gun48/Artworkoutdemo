package artwork.java;

import android.content.res.AssetManager;
import java.io.InputStream;
import android.graphics.Point;

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
