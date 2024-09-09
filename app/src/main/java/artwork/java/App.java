package artwork.java;

import android.app.Application;
import android.content.SharedPreferences;
import androidx.preference.PreferenceManager;
public class App extends Application {

    private static App instance;
    private SharedPreferences mPrefs;
    private SharedPreferences.Editor ed;
    //
    public App() {
        super();
        instance = this;
        System.out.println("*** App");
    }

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        /*if (LeakCanary.isInAnalyzerProcess(this)) {return;}
        LeakCanary.install(this);*/
        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        ed = mPrefs.edit();
    }

    public void putString(String key, String string) {
        if (null==string)
            ed.remove(key);
        else
            ed.putString(key, string);
        ed.commit();
    }

    public String getString(String key, String def){
        return mPrefs.getString(key, def);
    }

    public void putInt(String key, int i) {
        if (-1==i)
            ed.remove(key);
        else
            ed.putInt(key, i);
        ed.commit();
    }

    public int getInt(String key, int def){
        return mPrefs.getInt(key, def);
    }
}

